package com.tuoming.lteanalysis.GridMatrix.beans;

public class IssueGridSuggestCount {
	public String	DATE;
	public String	PROVINCE;
	public String	CITY;
	public long	GRID_ID;
	public String	ANTENNA = "";
	public String	ANGLE = "";
	public int ACount;
	public String	NOBESTCELL = "";
	public String	OVERCOVER = "";
	public int OCCount[] = new int[2];
	public String	ALARM = "";
	public String	MISSNEIGH = "";
	public String	OVERDISTANCE = "";
	public String	BACKBUILD = "";
	@Override
	public String toString() {
		return DATE + "," + PROVINCE + "," + CITY + "," + GRID_ID + "," + ANTENNA + "," + ANGLE + "," + NOBESTCELL
				+ "," + OVERCOVER + "," + ALARM + "," + MISSNEIGH + "," + OVERDISTANCE + "," + BACKBUILD;
	}
	
}
