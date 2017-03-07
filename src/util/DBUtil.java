/*
 * DBUtil.java
 * Copyright: TsingSoft (c) 2015
 * Company: 北京清软创新科技有限公司
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * jdbc工具类数据库接口
 * @author LT
 * @version 1.0, 2015年9月10日
 */
public class DBUtil {
	/**
	 * 获取数据库连接sql.connection
	 * @return
	 */
	public static Connection getConnect() {
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
//					"jdbc:mysql://localhost:3306/test",//可能会有乱码
					"jdbc:mysql://localhost:3306/test?Unicode=true&characterEncoding=GBK",
					"root", "root");
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}
	/**
	 * 关闭连接
	 * @param con
	 */
	public static void close(Connection con){
		if(con!=null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	/**
	 * 关闭常用三组连接
	 * @param con
	 * @param sta
	 * @param rs
	 */
	public static void close(Connection con,Statement sta,ResultSet rs){
		close(con);
		close(sta);
		close(rs);
	}
	public static void close(Statement sta){
		if(sta!=null){
			try {
				sta.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void close(ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
