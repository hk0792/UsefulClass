package com.fai.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fai.beans.Report;
import com.fai.jdbc.JdbcConn;

public class Service {
	private Connection conn;
	private Statement st;
	private ResultSet rs;
	private String sql;
	private List list;
	private Report rp;

	public List getReport() {
		list = new ArrayList<Report>();
		conn = JdbcConn.getConnection();
		try {
			st = (Statement) conn.createStatement();
			sql = "SELECT count(*) rigNum FROM fai_server.user_info_t u";
			rs = st.executeQuery(sql);
			rp = new Report();
			while (rs.next()) {
				rp.setRigNum(rs.getInt("rigNum"));
			}

			sql = "SELECT count(*) payNum FROM fai_server.authorize_list_t a";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				rp.setPayNmu(rs.getInt("payNum"));
			}

			list.add(rp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
