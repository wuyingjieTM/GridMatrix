package com.tuoming.lteanalysis.GridMatrix.beans;

public class GridIssueanaOvercover {
	public String DATE;
	public String CITY;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public long GRID_ID;
	public long ECI;
	public String CELL_NAME;
	public float S_DIR;
	public float D_DIR = -1;
	public String OVERCOVER;
	
	@Override
	public String toString() {
		return DATE + "," + CITY + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + S_DIR + "," + (D_DIR == -1 ? "\\N": "" + D_DIR) + ","
				+ OVERCOVER;
	}
	
	public String sceneToString() {
		return DATE + "," + CITY + "," + SCENE_MAJOR + "," + SCENE_MINOR + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + S_DIR + "," + (D_DIR == -1 ? "\\N": "" + D_DIR) + ","
				+ OVERCOVER + "\n";
	}
}
