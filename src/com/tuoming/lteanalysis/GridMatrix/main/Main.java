package com.tuoming.lteanalysis.GridMatrix.main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.tuoming.lteanalysis.GridMatrix.config.configer;
import com.tuoming.lteanalysis.GridMatrix.process.GridMatrixProcess;


public class Main {
	private static Logger logger = Logger.getLogger(Main.class);
	private static void initLog4j(String logpath) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, 0);
		Date dBefore = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Properties prop = new Properties();
		if (logpath == null)
			prop.setProperty("log4j.rootLogger", "info, stdout");
		else {
			prop.setProperty("log4j.rootLogger", "info, ServerDailyRollingFile, stdout");
			prop.setProperty("log4j.appender.ServerDailyRollingFile", "org.apache.log4j.DailyRollingFileAppender");
			prop.setProperty("log4j.appender.ServerDailyRollingFile.DatePattern", "'.'yyyy-MM-dd");
			prop.setProperty("log4j.appender.ServerDailyRollingFile.File",
					logpath + "Logs/" + File.separator + df.format(dBefore) + ".log");
			prop.setProperty("log4j.appender.ServerDailyRollingFile.layout", "org.apache.log4j.PatternLayout");
			prop.setProperty("log4j.appender.ServerDailyRollingFile.layout.ConversionPattern",
					"%d{yyyy-MM-dd HH:mm:ss} %p [%c:%L] %m%n");
		}
		prop.setProperty("log4j.appender.ServerDailyRollingFile.Append", "true");
		prop.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%d{yyyy-MM-dd HH:mm:ss} %p [%c:%L] %m%n");
		PropertyConfigurator.configure(prop);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String abJarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String jarPath = abJarPath.substring(0,abJarPath.lastIndexOf("/") + 1);
//		initLog4j(null);
		initLog4j(jarPath);
		if(args.length < 2) {
			logger.error("启动参数太少。\"程序名 输出目录 数据日期(yyyymmdd\"");
			return;
		}

		configer config = new configer(jarPath + File.separator + "config.properties");
		config.sourcePath = args[0];//当数据源为文件时，输出目录其实也是数据源目录	
 		GridMatrixProcess gp = new GridMatrixProcess(config,args[0],args[1]);
		if(gp.init() != 0) {
			return;
		}
		gp.process();
		
	}

}
