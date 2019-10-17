package com.tuoming.lteanalysis.GridMatrix.beans;

public class IssueGridResult {
	public String	DATE;
	public String	PROVINCE;
	public String	CITY;
	public long	GRID_ID;
	public long	TOP1ECI;
	public long	TOP2ECI;
	public long	TOP3ECI;
	public int	ANTENNA;
	public int	ANGLE;
	public int	NOBESTCELL;
	public int	OVERCOVER;
	public int	ALARM;
	public int	MISSNEIGH;
	public int	OVERDISTANCE;
	public int	BACKBUILD;
	@Override
	public String toString() {
		return DATE + "," + PROVINCE + "," + CITY + "," + GRID_ID + "," + TOP1ECI + "," + TOP2ECI + "," + TOP3ECI + ","
				+ ANTENNA + "," + ANGLE + "," + NOBESTCELL + "," + OVERCOVER + "," + ALARM + "," + MISSNEIGH + ","
				+ OVERDISTANCE + "," + BACKBUILD;
	}

}
