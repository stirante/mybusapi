package com.stirante.mybusapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BusStop {

    public int id;
    public int roadId;
    public String name;
    public String number;
    public int departures;
    public double lon;
    public double lat;
    public String transport;
    public String linesA;
    public String linesT;
    public String linesR;
    public int sort;
    public String direction;

    public BusStop(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        roadId = rs.getInt("id_ul");
        name = rs.getString("nazwa");
        number = rs.getString("numer");
        departures = rs.getInt("odjazdy");
        lon = rs.getDouble("lon");
        lat = rs.getDouble("lat");
        transport = rs.getString("transport");
        linesA = rs.getString("linieA");
        linesT = rs.getString("linieT");
        linesR = rs.getString("linieR");
        sort = rs.getInt("sort");
        direction = rs.getString("kierunek");
    }

    @Override
    public String toString() {
        return "BusStop{" +
                "id=" + id +
                ", roadId=" + roadId +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", departures=" + departures +
                ", lon=" + lon +
                ", lat=" + lat +
                ", transport='" + transport + '\'' +
                ", linesA='" + linesA + '\'' +
                ", linesT='" + linesT + '\'' +
                ", linesR='" + linesR + '\'' +
                ", sort=" + sort +
                ", direction='" + direction + '\'' +
                '}';
    }
}
