/*
 * User.java
 * Copyright: TsingSoft (c) 2015
 * Company: 北京清软创新科技有限公司
 */
package entity;

/**
 * user表的相关字段
 * @author LT
 * @version 1.0, 2015年9月20日
 */
public class User {
	private long id;
	private String userName;
	private String name;
	private String pwd;
	private String gender;
	/**
	 * 每一个实体都应该有此空参数类，比如可以用于spring的相关的操作
	 */
	public User(){
		
	}
	/**
	 * 构造体
	 * @param userName
	 * @param name
	 * @param pwd
	 * @param gender
	 */
	public User(String userName, String name, String pwd, String gender) {
		this.userName = userName;
		this.name = name;
		this.pwd = pwd;
		this.gender = gender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
}
