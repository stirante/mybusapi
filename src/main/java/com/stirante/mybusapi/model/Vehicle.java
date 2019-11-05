package com.stirante.mybusapi.model;

public class Vehicle {

    private int id;
    private int vehicleNumber;
    private String busNumber;
    private String variant;
    private String direction;
    private String directionName;
    private double lat;
    private double lon;
    private double stopLat;
    private double stopLon;
    private int delay;

    private Vehicle() {

    }

    public int getId() {
        return id;
    }

    public int getVehicleNumber() {
        return vehicleNumber;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getVariant() {
        return variant;
    }

    public String getDirection() {
        return direction;
    }

    public String getDirectionName() {
        return directionName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getStopLat() {
        return stopLat;
    }

    public double getStopLon() {
        return stopLon;
    }

    public int getDelay() {
        return delay;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", vehicleNumber=" + vehicleNumber +
                ", busNumber='" + busNumber + '\'' +
                ", variant='" + variant + '\'' +
                ", direction='" + direction + '\'' +
                ", directionName='" + directionName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", stopLat=" + stopLat +
                ", stopLon=" + stopLon +
                ", delay=" + delay +
                '}';
    }

    public static class VehicleBuilder {
        private int id;
        private int nb;
        private String busNumber;
        private String variant;
        private String direction;
        private String directionName;
        private double lat;
        private double lon;
        private double stopLat;
        private double stopLon;
        private int delay;

        public VehicleBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public VehicleBuilder setNb(int nb) {
            this.nb = nb;
            return this;
        }

        public VehicleBuilder setBusNumber(String busNumber) {
            this.busNumber = busNumber;
            return this;
        }

        public VehicleBuilder setVariant(String variant) {
            this.variant = variant;
            return this;
        }

        public VehicleBuilder setDirection(String direction) {
            this.direction = direction;
            return this;
        }

        public VehicleBuilder setDirectionName(String directionName) {
            this.directionName = directionName;
            return this;
        }

        public VehicleBuilder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        public VehicleBuilder setLon(double lon) {
            this.lon = lon;
            return this;
        }

        public VehicleBuilder setStopLat(double stopLat) {
            this.stopLat = stopLat;
            return this;
        }

        public VehicleBuilder setStopLon(double stopLon) {
            this.stopLon = stopLon;
            return this;
        }

        public VehicleBuilder setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public Vehicle createVehicle() {
            Vehicle v = new Vehicle();
            v.id = id;
            v.vehicleNumber = nb;
            v.busNumber = busNumber;
            v.variant = variant;
            v.direction = direction;
            v.directionName = directionName;
            v.lat = lat;
            v.lon = lon;
            v.stopLat = stopLat;
            v.stopLon = stopLon;
            v.delay = delay;
            return v;
        }
    }
}
