package com.tuoming.lteanalysis.GridMatrix.beans;

public class AlarmList {
	public String	DATE;
	public String	PROVINCE;
	public String	CITY;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public long GRID_ID;
	public long	ECI;
	public String CELL_NAME;
	public String	ALARMNAME;
	public String	INSERT_TIME;
	public String	CLRINSERT_TIME;
	
	@Override
	public String toString() {
		return DATE +  "," + CITY + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + ALARMNAME + ","
				+ INSERT_TIME + "," + CLRINSERT_TIME;
	}
	
	public String sceneToString() {
		return DATE +  "," + CITY + "," + SCENE_MAJOR + "," + SCENE_MINOR + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + ALARMNAME + ","
				+ INSERT_TIME + "," + CLRINSERT_TIME + "\n";
	}
}
