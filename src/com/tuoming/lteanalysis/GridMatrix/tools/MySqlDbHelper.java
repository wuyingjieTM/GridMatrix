package com.tuoming.lteanalysis.GridMatrix.tools;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;



//该对象非多线程安全

public class MySqlDbHelper {
	private static Logger logger = Logger.getLogger(MySqlDbHelper.class);
	Connection con = null;
	Statement st = null;  
	private String host = "";
	private String port = "";
	private String dbname = "";
	private String user = "";
	private String password = "";
	private boolean autocommit = true;
	
	public MySqlDbHelper(String host, String port, String dbname, String user, String password){
		this.host = host;
		this.port = port;
		this.dbname = dbname;
		this.user = user;
		this.password = password;
		con = getConnection();
	}
	public MySqlDbHelper(String host, String port, String dbname, String user, String password,boolean autocommit,String objectname){
		this.host = host;
		this.port = port;
		this.dbname = dbname;
		this.user = user;
		this.password = password;
		if(autocommit == false) {
			this.autocommit = false;
		}
		con = getConnection();
	}
	
    // 创建用于连接数据库的Connection对
	public Connection getConnection() { 
		
        // 创建用于连接数据库的Connection对象    
        try {  
            // 加载Mysql数据驱动  
			Class.forName("com.mysql.cj.jdbc.Driver");
            // 创建数据连接  
            //con = DriverManager.getConnection("jdbc:postgres://localhost:3306/myuser", "root", ""); 
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",user,password);
            if(autocommit == false) {
            	con.setAutoCommit(false);
            }else {
            	con.setAutoCommit(true);
            }
        	logger.info("Connect DateBase " + host + " " + port + " " + dbname + " Success");
        } catch (SQLException e) { 
        	logger.error("Connect DateBase " + host + " " + port + " " + dbname + " failure : \n" + e.getMessage());  
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
        	logger.error(e.getMessage());
		}  
        // 返回所建立的数据库连接  
        return con;   
    }
	
	public Statement GetStatement() {	

			try {
				if(st == null) {
					if (con == null) {
						con = getConnection();
					}
					st = con.createStatement();
					return st;
				}else {
					return st;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("create Statement  failure:\n" +  e.getMessage());
				return null;
			}
	}
	
	public void Close() {
		try {
			if (st != null) {
				st.close();
			}
			if(con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("create Statement  failure:\n" + e.getMessage());
		}
	}
	

	public ResultSet Query(String sql) {
		try {
			Statement st = GetStatement();
			if(st == null) {
				logger.error("sql:get statement error");
				return null;
			}
			ResultSet rs =  st.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("query[" + sql + "]error:\n" + e.getMessage());
			return null;
		}
	}
	
	public boolean Execute(String sql) {
		try {
			Statement st = GetStatement();
			if(st == null) {
				logger.error("sql:get statement error");
				return false;
			}
			boolean rs =  st.execute(sql);
			logger.info("execute [" + sql + "]\n[" + rs +"] records effect");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("execute[" + sql + "]error:\n" + e.getMessage());
			return false ;
		}
		return true;
	}

}
