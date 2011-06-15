package com.Top_Cat.CODMW.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class conn {
    
    Connection conn;
    
    public conn() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(new sqllogin().url);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet query(String q) {
        PreparedStatement pr;
        try {
            pr = conn.prepareStatement(q);
            return pr.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void update(String q) {
        PreparedStatement pr;
        try {
            pr = conn.prepareStatement(q);
            pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}