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
            // Step 2: Establish the connection to the database. 
            conn = DriverManager.getConnection(new sqllogin().url);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public ResultSet query(String q) {
        PreparedStatement pr;
        try {
            pr = conn.prepareStatement(q);
            return pr.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
	}
	
}