package com.stirante.mybusapi.model;

import org.w3c.dom.Node;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Departure {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    static {
        TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    private String directionSymbol;
    /**
     * Planned time of arrival. This is number of seconds since midnight
     */
    private int plannedTime;
    private int id;
    private boolean isGreen;
    private String name;
    /**
     * Estimated time of arrival string
     */
    private String time;
    private String direction;
    /**
     * Bus type:
     * A - normal, R - trolleybus, T - tram
     */
    private String type;
    /**
     * Bus features:
     * K - Air conditioning, N - wheel chair, B - ticket machine
     */
    private String features;
    /**
     * Available information about vehicle:
     * 3 - only departure time, 2 - gps available, 1 - right now on bus stop
     */
    private int timeType;
    private int vehicleNumber;

    public Departure(Node id, Node name, Node time, Node direction, Node directionSymbol, Node plannedTime, Node green, Node type, Node timeType, Node features, Node vehicleNumber) {
        if (directionSymbol != null)
            this.directionSymbol = directionSymbol.getTextContent();
        this.plannedTime = Integer.parseInt(plannedTime.getTextContent());
        this.timeType = Integer.parseInt(timeType.getTextContent());
        if (green != null)
            this.isGreen = Integer.parseInt(green.getTextContent()) != 0;
        this.id = Integer.parseInt(id.getTextContent());
        this.name = name.getTextContent();
        this.time = time.getTextContent();
        this.direction = direction.getTextContent();
        if (type != null)
            this.type = type.getTextContent();
        if (features != null)
            this.features = features.getTextContent();
        if (this.type == null || this.type.isEmpty()) this.type = "A";
        if (vehicleNumber != null) this.vehicleNumber = Integer.parseInt(vehicleNumber.getTextContent());
    }


    public int getPlannedTime() {
        return plannedTime;
    }

    public String getPlannedTimeString() {
        return TIME_FORMAT.format(new Date(plannedTime * 1000));
    }

    public int getId() {
        return id;
    }

    public boolean isGreen() {
        return isGreen;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDirection() {
        return direction;
    }

    public String getDirectionSymbol() {
        return directionSymbol;
    }

    //3 - only departure time, 2 - gps available, 1 - right now on bus stop
    public int getTimeType() {
        return timeType;
    }

    //contains letter: K - Air conditioning, N - wheel chair, B - ticket machine
    public String getFeatures() {
        if (features != null)
            return features.toUpperCase();
        return "";
    }

    //A - normal, R - trol, T - tram
    public String getType() {
        return type;
    }

    public int getVehicleNumber() {
        return vehicleNumber;
    }

    @Override
    public String toString() {
        return "Departure{" +
                "directionSymbol='" + directionSymbol + '\'' +
                ", plannedTime=" + getPlannedTimeString() +
                ", id=" + id +
                ", isGreen=" + isGreen +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", direction='" + direction + '\'' +
                ", type='" + type + '\'' +
                ", features='" + features + '\'' +
                ", timeType=" + timeType +
                ", nb=" + vehicleNumber +
                '}';
    }
}
