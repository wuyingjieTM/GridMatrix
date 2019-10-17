package com.tuoming.lteanalysis.GridMatrix.beans;

public class IssueGridSuggest {
	public String	DATE;
	public String	PROVINCE;
	public String	CITY;
	public long	GRID_ID;
	public String	ANTENNA;
	public String	ANGLETOP1;
	public String	ANGLETOP2;
	public String	ANGLETOP3;
	public String	NOBESTCELL;
	public String	OVERCOVERTOP1;
	public String	OVERCOVERTOP2;
	public String	OVERCOVERTOP3;
	public String	ALARM;
	public String	MISSNEIGH;
	public String	OVERDISTANCE;
	public String	BACKBUILD;
	@Override
	public String toString() {
		return DATE + "," + PROVINCE + "," + CITY + "," + GRID_ID + "," + ANTENNA + "," + ANGLETOP1 + "," + ANGLETOP2
				+ "," + ANGLETOP3 + "," + NOBESTCELL + "," + OVERCOVERTOP1 + "," + OVERCOVERTOP2 + "," + OVERCOVERTOP3
				+ "," + ALARM + "," + MISSNEIGH + "," + OVERDISTANCE + "," + BACKBUILD;
	}
}
