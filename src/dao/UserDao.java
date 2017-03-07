/*
 * UserDao.java
 * Copyright: TsingSoft (c) 2015
 * Company: 北京清软创新科技有限公司
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.DBUtil;
import entity.User;

/**
 * user表的相关操作
 * @author LT
 * @version 1.0, 2015年9月20日
 */
public class UserDao {
	/**
	 * 保存对象
	 * @param user
	 */
	public void save(User user){
		Connection con = null;
		try{
			con = DBUtil.getConnect();
			PreparedStatement sta = con.prepareStatement("insert into t_user(username,name,pwd,gender) values(?,?,?,?)");
			sta.setString(1, user.getUserName());
			sta.setString(2, user.getName());
			sta.setString(3, user.getPwd());
			sta.setString(4, user.getGender());
			sta.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBUtil.close(con);
		}
	}
	/**
	 * 用用户登录名查询对象,正常的模式，preparedStatement,可防止sql注入
	 * @param UserName
	 * @return
	 */
	public User findByUserName(String UserName){
		Connection con = null;
		User user = null;
		try{
			con = DBUtil.getConnect();
			PreparedStatement sta = con.prepareStatement("select * from t_user where username = ?");
			sta.setString(1, UserName);
			ResultSet rs = sta.executeQuery();
			if(rs.next()){
				user = new User();
				user.setId(rs.getLong("id"));
				user.setUserName(UserName);
				user.setName(rs.getString("name"));
				user.setPwd(rs.getString("pwd"));
				user.setGender(rs.getString("gender"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBUtil.close(con);
		}
		return user;
	}
	/**
	 * 用用户登录名查询对象,正常的模式，preparedStatement,可防止sql注入
	 * @param UserName
	 * @return
	 */
	public User findNormal(String UserName,String pwd){
		Connection con = null;
		User user = null;
		try{
			con = DBUtil.getConnect();
			PreparedStatement sta = con.prepareStatement("select * from t_user where username = ? and pwd = ?");
			sta.setString(1, UserName);
			sta.setString(2, pwd);
			ResultSet rs = sta.executeQuery();
			if(rs.next()){
				user = new User();
				user.setId(rs.getLong("id"));
				user.setUserName(UserName);
				user.setName(rs.getString("name"));
				user.setPwd(rs.getString("pwd"));
				user.setGender(rs.getString("gender"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			DBUtil.close(con);
		}
		return user;
	}
	
	/**
	 * sql注入的不安全写法，使用Statement
	 * 使用Statement不能防止sql注入 如： 用户名（admin） 密码（ 1' or '1' = '1 ） 
	 * @param userName
	 * @param pwd
	 * @return
	 */
	public User findPour(String userName,String pwd){
		User user = null;
		Connection con = DBUtil.getConnect();
		Statement sta = null;
		ResultSet rs = null;
		try {
			sta = con.createStatement();
			rs = sta.executeQuery("select * from t_user where username='"+userName +"' and pwd= '"+pwd+"'");
			if(rs.next()){
				user = new User();
				user.setId(rs.getLong("id"));
				user.setUserName(userName);
				user.setName(rs.getString("name"));
				user.setPwd(rs.getString("pwd"));
				user.setGender(rs.getString("gender"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(con, sta, rs);
		}
		return user;
	}
	public static void main(String[] args){
		UserDao dao = new UserDao();
		dao.save(new User("admin1","管理员","admin","m"));
		User user = dao.findByUserName("admin");
		System.out.println(user.getName());
	}
}
