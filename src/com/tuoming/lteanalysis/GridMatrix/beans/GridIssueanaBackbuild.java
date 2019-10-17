package com.tuoming.lteanalysis.GridMatrix.beans;

public class GridIssueanaBackbuild {
	public String DATE;
	public String CITY;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public long GRID_ID;
	public long ECI;
	public String CELL_NAME;
	public String BACKBUILD;
	@Override
	public String toString() {
		return DATE + "," + CITY + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + BACKBUILD;
	}
	public String sceneToString() {
		return DATE + "," + CITY + "," + SCENE_MAJOR + "," + SCENE_MINOR + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + BACKBUILD + "\n";
	}
}
