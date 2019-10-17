package com.tuoming.lteanalysis.GridMatrix.beans;

public class IssueSceneSuggest {
	public String DATE;
	public String PROVINCE;
	public String CITY;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public String ANTENNA;
	public String ANGLE;
	public String NOBESTCELL;
	public String OVERCOVER;
	public String ALARM;
	public String MISSNEIGH;
	public String OVERDISTANCE;
	public String BACKBUILD;
	@Override
	public String toString() {
		return DATE + "," + PROVINCE + "," + CITY + "," + SCENE_MAJOR + "," + SCENE_MINOR + "," + ANTENNA + "," + ANGLE
				+ "," + NOBESTCELL + "," + OVERCOVER + "," + ALARM + "," + MISSNEIGH + "," + OVERDISTANCE + ","
				+ BACKBUILD;
	}
	
	
	
}
