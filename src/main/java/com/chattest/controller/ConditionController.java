//package com.chattest.controller;
//
//import com.chattest.dto.ConditionDTO;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Repository;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.text.SimpleDateFormat;
//import java.util.Properties;
//
//@Log4j2
//@Repository
//public class ConditionController {
//    private static Connection connect() {
//        Connection conn = null;
//        try {
//            String DB_user = "USER";
//            String DB_Password = "PASSWORD";
//            String DB_URL = "URL";
//            String DB_sURL = "jdbc:machbase://"+DB_URL+"/machbasedb";
//
//            log.info("======================");
//            log.info("url {}", DB_sURL);
//
//            Properties props = new Properties();
//            props.put("user", DB_user);
//            props.put("password", DB_Password);
//            log.info("=======================");
//            log.info("user + pw {} {}", DB_user, DB_Password);
//
//            Class.forName("com.machbase.jdbc.MachDriver");
//            conn = DriverManager.getConnection(DB_sURL, props);
//        } catch (ClassNotFoundException ex) {
//            System.err.println("Exception: unable to load machbase jdbc driver class");
//        } catch(Exception e) {
//            System.err.println("Exception: " + e.getMessage());
//        }
//        return conn;
//    }
//
//    public ConditionDTO getConditionDTO() throws Exception {
//        Connection conn = null;
//        log.info("getConditionDTO");
//        Statement stmt = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        ConditionDTO conditionDTO = null;
//        try {
//            conn = connect();
//            if( conn != null) {
//                log.info("MachbaseDB JDBC Connected");
//                stmt = conn.createStatement();
//
//                String temp_humidity_query = "select value from temperature_humidity_data where name like 'temperature' and 'humidity' limit 1 ";
//                String dust_query = "select value from dust_sensor_data where name in ('PM10.0_ATM', 'PM2.5_ATM', 'PM1.0_ATM') limit 1";
//                String co2_query = "select value from co2_data limit 1";
//
//                ResultSet rs_temp_humidity = stmt.executeQuery(temp_humidity_query);
//                ResultSet rs_dust = stmt.executeQuery(dust_query);
//                ResultSet rs_co2 = stmt.executeQuery(co2_query);
//
//                while(rs_temp_humidity.next()) {
//                }
//
//            }
//        } catch(Exception e) {
//            
//        }
//    }
//}
