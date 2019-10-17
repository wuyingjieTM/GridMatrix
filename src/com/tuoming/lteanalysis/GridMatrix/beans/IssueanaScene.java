package com.tuoming.lteanalysis.GridMatrix.beans;

public class IssueanaScene {
	public String DATE;
	public String PROVINCE;
	public String CITY;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public int ANTENNA;
	public int ANGLE;
	public int NOBESTCELL;
	public int OVERCOVER;
	public int ALARM;
	public int MISSNEIGH;
	public int OVERDISTANCE;
	public int BACKBUILD;
	@Override
	public String toString() {
		return DATE + "," + PROVINCE + "," + CITY + "," + SCENE_MAJOR + "," + SCENE_MINOR + "," + ANTENNA + "," + ANGLE
				+ "," + NOBESTCELL + "," + OVERCOVER + "," + ALARM + "," + MISSNEIGH + "," + OVERDISTANCE + ","
				+ BACKBUILD;
	}

	
}
