package com.chattest.repository;

import com.chattest.dto.ConditionDTO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Properties;
import com.machbase.jdbc.*;

@Repository
public class MachbaseJDBC {
    public Connection connect() {
        Connection conn = null;
        try {
            String sURL = "jdbc:machbase://192.168.1.112:31824/machbasedb";
            Properties sProps = new Properties();
            sProps.put("user", "sys");
            sProps.put("password", "manager");

            Class.forName("com.machbase.jdbc.MachDriver");
            conn = DriverManager.getConnection(sURL, sProps);
        } catch (ClassNotFoundException ex) {
            System.err.println("Exception : unable to load mach jdbc driver class");
        } catch (SQLException e) {
            System.err.println("Exception : " + e.getMessage());
        }
        return conn;
    }

    public ConditionDTO getAirConditionData() {
        ConditionDTO data = new ConditionDTO();
        String query = "SELECT TIME, TEMP, HUMIDITY, MINI_DUST, SUPER_MINI_DUST, CO2 FROM air_condition_data limit 1";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                data.setTime(rs.getString("TIME"));
                data.setTemp(rs.getFloat("TEMP"));
                data.setHumidity(rs.getFloat("HUMIDITY"));
                data.setMiniDust(rs.getFloat("MINI_DUST"));
                data.setSuperMiniDust(rs.getFloat("SUPER_MINI_DUST"));
                data.setCo2(rs.getFloat("CO2"));
            }
            System.out.println("data = " + data);
        } catch (SQLException e) {
            System.err.println("Exception : " + e.getMessage());
        }
        return data;
    }
}