package com.tuoming.lteanalysis.GridMatrix.beans;

public class GridIssueanaOverdistance {
	public String DATE;
	public String CITY;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public long GRID_ID;
	public long ECI;
	public String CELL_NAME;
	public float COVERRATE;
	public float SITE_DISTANCE;
	public String OVERDISTANCE;
	@Override
	public String toString() {
		return DATE + "," + CITY + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + COVERRATE + "," + SITE_DISTANCE
				+ "," + OVERDISTANCE;
	}

	public String sceneToString() {
		return DATE + "," + CITY + "," + SCENE_MAJOR + "," + SCENE_MINOR + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + COVERRATE + "," + SITE_DISTANCE
				+ "," + OVERDISTANCE + "\n";
	}


}
