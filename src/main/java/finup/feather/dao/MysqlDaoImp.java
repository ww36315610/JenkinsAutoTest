package finup.feather.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetMetaData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlDaoImp implements MysqlDao {

	public int createTable(String sql, Connection conn) throws SQLException {
		int i = 0;
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		System.out.println("mysql_create>>>" + i);
		return i;
	}

	public int insert(String sql, Connection conn) {
		int i = 0;
		try {
			PreparedStatement pre = conn.prepareStatement(sql);
			i = pre.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("mysql_insert>>>" + i++);
		return i;
	}

	public int update(String sql, Connection conn) {
		int i = 0;
		try {
			PreparedStatement pre = conn.prepareStatement(sql);
			i = pre.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("mysql_update>>>" + i);
		return i;
	}

	public int delete(String sql, Connection conn) {
		int i = 0;
		Statement stmt;
		try {
			stmt = conn.createStatement();
			i = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("mysql_delete>>>" + i);
		return i;
	}

	public JSONArray select(String sql, Connection conn) {
		JSONArray jsonArry = new JSONArray();
		try {
			PreparedStatement pre = conn.prepareStatement(sql);
			ResultSet rs = pre.executeQuery();
			int col = rs.getMetaData().getColumnCount();
			ResultSetMetaData data = (ResultSetMetaData) rs.getMetaData();
			while (rs.next()) {
				JSONObject josnObject = new JSONObject();
				for (int i = 1; i <= col; i++) {
					String columnName = data.getColumnName(i);
					// // 获取mysql列名称跟数据类型
					// String columnClassName = data.getColumnClassName(i);
					// josnObject.put(columnName,
					// columnClassName.split("\\.")[2]);
					String value = rs.getString(i).toString();
					josnObject.put(columnName, value);
				}
				jsonArry.add(josnObject);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jsonArry;
	}
}