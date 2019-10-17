package com.tuoming.lteanalysis.GridMatrix.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.tuoming.lteanalysis.GridMatrix.config.configer;


public  class FileExporter implements IExporter {
	private static Logger logger = Logger.getLogger(FileExporter.class);
	
	protected String filename = "";
	private FileOutputStream filehandle = null;
	long timevalue = 0;
	
	public FileExporter(String filename) {
		this.filename = filename;
	}
	
	@Override
	public void Export(Object data) {
		long b = System.currentTimeMillis();
		// TODO Auto-generated method stub
		try {
			String str = format(data);
			filehandle.write(str.getBytes());
		}catch(IOException e) {
			logger.error("write error:\n" + e.getMessage());
		}
		timevalue += System.currentTimeMillis() - b;
	}
	protected String format(Object data) {
		return data.toString() + "\n";
	}
	
	@Override
	public void Export(String data) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		// TODO Auto-generated method stub
		try {
			filehandle.write(data.getBytes());
		}catch(IOException e) {
			logger.error("write error:\n" + e.getMessage());
		}
		timevalue += System.currentTimeMillis() - b;
	}


	@Override
	public void finish() {
		// TODO Auto-generated method stub
		try {
			filehandle.close();
			logger.info("export " + filename + " use " + String.format("%.3f",1e-3*timevalue)  + " sec");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("close error:\n" + e.getMessage());
		}
	}


	@Override
	public int init(configer config) {
		// TODO Auto-generated method stub
		try {
			if(config.sourcePath == null || config.sourcePath.length() == 0) {
				logger.error("export path is null");
			}
			File file = new File(config.sourcePath);
			if(!file.exists()) {
				file.mkdirs();
			}
			if(filename == null || filename.length() == 0) {
				return -1;
			}
			filehandle = new FileOutputStream(new File(file.getAbsolutePath() + File.separator + filename));
		}catch(Exception e) {
			logger.error("init error:\n" + e.getMessage());
			return -1;
		}
		return 0;
	}


}
