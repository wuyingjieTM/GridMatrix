package com.tuoming.lteanalysis.GridMatrix.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;

public class configer {
	//该类非线程安全
	private static Logger logger = Logger.getLogger(configer.class);
	private Properties pro = new Properties();
	private String configFilePath = "";
	//db info config
	public String dbIp = "";
	public String dbPort = "";
	public String dbName  = "";
	public String dbUser = "";
	public String dbPasswd= "";
	
	public String dataSourceType = "";
	public String sourcePath = "";
	
	public String lastGridDate="";
	
	public configer(String path) {
		this.configFilePath = path;
	}
	
	public String GetDataSourceType() {
		if(ReadconfigFile() != 0) {
			return "";
		}
		dataSourceType = GetOneConfig("DataSourceType");
		if(dataSourceType == null) return "";		
		return dataSourceType;
	}
	
	public int GetDbInfo() {
		if(ReadconfigFile() != 0) {
			return -1;
		}
		dbIp = GetOneConfig("DbIp");
		if(dbIp == null) return -1;
		
		dbPort = GetOneConfig("DbPort");
		if(dbPort == null) return -1;
		
		dbName = GetOneConfig("DbName");
		if(dbName == null) return -1;
		
		dbUser = GetOneConfig("DbUser");
		if(dbUser == null) return -1;
		
		dbPasswd = GetOneConfig("DbPasswd");
		if(dbPasswd == null) return -1;		
		
		return 0;
	}
	
	private int ReadconfigFile() {
		try {
			FileInputStream configstream = new FileInputStream(configFilePath);
			InputStreamReader in = new InputStreamReader(configstream,"UTF-8");
			pro.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("read config file " + configFilePath + "error:\n" + e.getMessage());
			return -1;
		}
		return 0;
	}
	
	
	public String GetOneConfig(String pname) {
		String value = pro.getProperty(pname);
//		if(value == null || value.length()==0){
//			logger.error(pname + " config error");
//			return null;
//		}
		return value;
	}
	
	public void SetOneConfig(String key,String value) {
		try {
			FileInputStream configstream = new FileInputStream(configFilePath);
			InputStreamReader in = new InputStreamReader(configstream,"UTF-8");
			pro.load(in);
			pro.setProperty(key, value);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}	
}
