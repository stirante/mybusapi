package com.stirante.mybusapi;

import com.stirante.mybusapi.model.BusStop;
import com.stirante.mybusapi.model.Road;

import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusDatabase {

    private Connection conn;

    BusDatabase(File db) throws SQLException {
        String url = "jdbc:sqlite:" + db.getAbsolutePath();
        conn = DriverManager.getConnection(url);
    }

    private int getCount(String table) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM " + table);
        rs.next();
        return rs.getInt(1);
    }

    public List<BusStop> getStops() throws SQLException {
        int size = getCount("PRZYSTANKI");
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM PRZYSTANKI");
        ArrayList<BusStop> stops = new ArrayList<>(size);
        while (rs.next()) {
            stops.add(new BusStop(rs));
        }
        return stops;
    }

    public List<Road> getRoads() throws SQLException {
        int size = getCount("ULICE");
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM ULICE");
        ArrayList<Road> roads = new ArrayList<>(size);
        while (rs.next()) {
            roads.add(new Road(rs));
        }
        return roads;
    }

    public DatabaseVersion getDatabaseVersion() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM WERSJE");
        rs.next();
        DatabaseVersion ver = new DatabaseVersion(rs);
        rs.close();
        return ver;
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    class DatabaseVersion {
        public int id;
        public Date validSince;
        public int generation;

        public DatabaseVersion(ResultSet rs) throws SQLException {
            id = rs.getInt("id_wersja");
            try {
                validSince = DATE_FORMAT.parse(rs.getString("wazna_od"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            generation = rs.getInt("generacja");
        }

        @Override
        public String toString() {
            return "DatabaseVersion{" +
                    "id=" + id +
                    ", validSince=" + validSince +
                    ", generation=" + generation +
                    '}';
        }
    }

}
