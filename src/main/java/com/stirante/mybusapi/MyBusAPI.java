package com.stirante.mybusapi;

import com.stirante.mybusapi.model.Departure;
import com.stirante.mybusapi.model.Vehicle;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class MyBusAPI {

    private final City city;
    private int age = 60;
    private XPath xPath = XPathFactory.newInstance().newXPath();
    private BusDatabase busDatabase;

    public MyBusAPI(City city) {
        this.city = city;
        try {
            updateAge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateAge() throws Exception {
        age = 60;
        String s = getString(city.getUrl() + "/PingService");
        age = Integer.parseInt(xPath.compile("int").evaluate(loadXMLFromString(s)));
    }

    private HttpURLConnection openUrl(String url) throws Exception {
        URL url1 = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("User-Agent", "myBusOnline");
        urlConnection.setRequestProperty("Age", "" + city.getAgeForCity(age));
        urlConnection.setUseCaches(false);
        urlConnection.connect();
        return urlConnection;
    }

    private String getString(String url) throws Exception {
        HttpURLConnection urlConnection = openUrl(url);
        InputStream inputStream = urlConnection.getInputStream();
        java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
        String result = s.next();
        inputStream.close();
        urlConnection.disconnect();
        return result;
    }

    private Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    public boolean compareVersions(String idVersion, String generation) throws Exception {
        updateAge();
        String s = getString(
                newRequest().method("/CompareScheduleFile")
                        .param("nIdWersja", idVersion)
                        .param("nGeneracja", generation)
                        .build()
        );
        return xPath.compile("int").evaluate(loadXMLFromString(s)).equalsIgnoreCase("1");
    }

    //TODO
//    public List<DispatcherMessage> getDispatcherMessages() throws Exception {
//        updateAge();
//        String s = getString(
//                newRequest().method("/GetDispatcherMessages")
//                        .build()
//        );
//        System.out.println(s);
//        NodeList r = ((Node) xPath.compile("R").evaluate(loadXMLFromString(s), XPathConstants.NODE)).getChildNodes();
//        for (int i = 0; i < r.getLength(); i++) {
//            Node node = r.item(i);
//        }
//        return null;
//    }

    //TODO: /GetRouteVariantWithTransitPoints /GetVehicleTimeTable

    public List<Vehicle> getVehicles(String busName, String busDirection) throws Exception {
        updateAge();
        String s = getString(
                newRequest().method("/GetVehicles")
                        .param("cNbLst", "")
                        .param("cIdLst", "")
                        .param("cRouteLst", busName)
                        .param("cTrackLst", "")
                        .param("cDirLst", busDirection)
                        .param("cKrsLst", "")
                        .build()
        );
//        System.out.println(s);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        NodeList childNodes =
                ((Node) xPath.compile("VL").evaluate(loadXMLFromString(s), XPathConstants.NODE)).getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            NamedNodeMap attributes = childNodes.item(i).getAttributes();
            int id = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
            int nb = Integer.parseInt(attributes.getNamedItem("nb").getNodeValue());
            String bus = attributes.getNamedItem("nr").getNodeValue().trim();
            String variant = attributes.getNamedItem("wt").getNodeValue();
            String direction = attributes.getNamedItem("kr").getNodeValue();
            String directionName = "";
            try {
                directionName = attributes.getNamedItem("op").getNodeValue();
            } catch (Exception ignored) {
            }
            double lon = Double.parseDouble(attributes.getNamedItem("x").getNodeValue().replace(',', '.'));
            double lat = Double.parseDouble(attributes.getNamedItem("y").getNodeValue().replace(',', '.'));
            double stopLon = Double.parseDouble(attributes.getNamedItem("px").getNodeValue().replace(',', '.'));
            double stopLat = Double.parseDouble(attributes.getNamedItem("py").getNodeValue().replace(',', '.'));
            int delay = Integer.parseInt(attributes.getNamedItem("o").getNodeValue());
            vehicles.add(new Vehicle.VehicleBuilder()
                    .setId(id)
                    .setNb(nb)
                    .setBusNumber(bus)
                    .setVariant(variant)
                    .setDirection(direction)
                    .setDirectionName(directionName)
                    .setLat(lat)
                    .setLon(lon)
                    .setStopLat(stopLat)
                    .setStopLon(stopLon)
                    .setDelay(delay)
                    .createVehicle());
        }
        return vehicles;
    }

    public void getRouteVariant(int departureId, String route, String variant) throws Exception {
        updateAge();
        System.out.println(getString(
                newRequest().method("/GetRouteVariantWithTransitPoints")
                        .param("nIdKursu", departureId)
                        .param("cRoute", route)
                        .param("cRouteVariant", variant).build()

        ));
    }

    public InputStream getScheduleFile() throws Exception {
        updateAge();
        HttpURLConnection urlConnection = openUrl(city.getUrl() + "/GetScheduleFile");
        InputStream inputStream = urlConnection.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        inputStream.close();
        urlConnection.disconnect();
        ByteArrayInputStream result = new ByteArrayInputStream(buffer.toByteArray());
        buffer.close();
        return new GZIPInputStream(result);
    }

    public BusDatabase getDatabase() throws Exception {
        if (busDatabase != null) {
            return busDatabase;
        }
        InputStream in = getScheduleFile();
        File temp = File.createTempFile("db_bus_", ".db");
        Files.copy(in, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        temp.deleteOnExit();
        busDatabase = new BusDatabase(temp);
        return busDatabase;
    }

    public ArrayList<Departure> getTimetable(int busStop) throws Exception {
        updateAge();
        ArrayList<Departure> result = new ArrayList<>();
        String s = getString(
                newRequest().method("/GetTimeTableReal").param("nBusStopId", busStop).build()
        );
        Node departures = (Node) xPath.compile("Departures").evaluate(loadXMLFromString(s), XPathConstants.NODE);
        NodeList childNodes = departures.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node n = childNodes.item(i);
            if (n.getNodeName().equalsIgnoreCase("D")) {
                Departure bus = new Departure(
                        n.getAttributes().getNamedItem("i"),//id
                        n.getAttributes().getNamedItem("r"),//name
                        n.getAttributes().getNamedItem("v"),//time
                        n.getAttributes().getNamedItem("d"),//direction
                        n.getAttributes().getNamedItem("dd"),//direction symbol
                        n.getAttributes().getNamedItem("t"),//planned time
                        n.getAttributes().getNamedItem("n"),//is green
                        n.getAttributes().getNamedItem("p"),//type
                        n.getAttributes().getNamedItem("m"),//time type
                        n.getAttributes().getNamedItem("vn"), //features
                        n.getAttributes().getNamedItem("n")
                );
                result.add(bus);
            }
        }
        return result;
    }

    private EndpointBuilder newRequest() {
        return new EndpointBuilder();
    }

    private class EndpointBuilder {

        StringBuilder sb = new StringBuilder();
        int args = 0;

        private EndpointBuilder() {
            sb.append(city.getUrl());
        }

        private EndpointBuilder method(String method) {
            sb.append(method);
            return this;
        }

        private EndpointBuilder param(String name, Object value) {
            if (args == 0) {
                sb.append("?");
            }
            else {
                sb.append("&");
            }
            try {
                sb.append(name).append("=").append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            args++;
            return this;
        }

        private String build() {
            return sb.toString();
        }

    }
}
