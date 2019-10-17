package com.tuoming.lteanalysis.GridMatrix.beans;

public class SceneTask {
	public String DATE;
	public String CITY;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public long GRID_ID;
	public float LON;
	public float LAT;
	public long ECI;
	public String DEF_CELLNAME_CHINESE;
	public String SUGGEST;
	public int ORIGINAL = -1;
	public int TARGET = -1;
	public String IS_SIMUL;
	@Override
	public String toString() {
		return DATE + "," + CITY + "," + SCENE_MAJOR + "," + SCENE_MINOR + "," + GRID_ID + "," + LON + "," + LAT + ","
				+ ECI + "," + DEF_CELLNAME_CHINESE + "," + SUGGEST + "," + (ORIGINAL == -1 ? "\\N" : ORIGINAL)  + "," + (TARGET == -1 ? "\\N" : TARGET) + "," + IS_SIMUL;
	}
	
}
