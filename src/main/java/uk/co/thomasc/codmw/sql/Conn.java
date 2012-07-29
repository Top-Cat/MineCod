package uk.co.thomasc.codmw.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class Conn {
	
	Connection conn;
	
	public Conn() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(new SqlLogin().url);
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
	
	public int update(String q) {
		PreparedStatement pr;
		try {
			pr = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
			pr.executeUpdate();
			
			ResultSet r = pr.getGeneratedKeys();
			
			if (r.next()) {
				return r.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}