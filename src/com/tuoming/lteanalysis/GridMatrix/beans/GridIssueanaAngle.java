package com.tuoming.lteanalysis.GridMatrix.beans;

public class GridIssueanaAngle {
	public String DATE;
	public String CITY;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public long GRID_ID;
	public long ECI;
	public String CELL_NAME;
	public float S_ANGLE;
	public float avgaoa;
	public float ANGLE;
	@Override
	public String toString() {
		return DATE + "," + CITY + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + S_ANGLE + "," + avgaoa + ","
				+ ANGLE;
	}
	
	public String sceneToString() {
		return DATE + "," + CITY + "," + SCENE_MAJOR + "," + SCENE_MINOR + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + S_ANGLE + "," + avgaoa + ","
				+ ANGLE + "\n";
	}
}
