package com.tuoming.lteanalysis.GridMatrix.beans;

public class GridIssueanaNobestcell {
	public String DATE;
	public String CITY;
	public long GRID_ID;
	public long ECI;
	public String CELL_NAME;
	public String vendor_name;
	public String param_name;
	public String real_value;
	public String plan_value;
	public String NOBESTCELL;
	public String cell_type;
	@Override
	public String toString() {
		return DATE + "," + CITY + "," + GRID_ID + "," + ECI + "," + CELL_NAME + "," + vendor_name + "," + param_name
				+ "," + real_value + "," + plan_value + "," + NOBESTCELL;
	}
	
}
