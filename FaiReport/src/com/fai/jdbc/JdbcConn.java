package com.fai.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConn {
	public static Connection getConnection() {
		String url = "jdbc:mysql://120.24.248.212:3306/fai_server";
		String name = "root";
		String password = "@&fai!@#@";
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			boolean jdbc;
			conn = DriverManager.getConnection(url, name, password);
			System.out.println("connect mysql success");
		} catch (Exception e) {
			e.printStackTrace();
			// return null;
		}
		return conn;
	}
}
