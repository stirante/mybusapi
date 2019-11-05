package com.stirante.mybusapi.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Road {

    public int id;
    public String name;

    public Road(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        name = rs.getString("nazwa");
    }

    @Override
    public String toString() {
        return "Road{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
