package com.tuoming.lteanalysis.GridMatrix.beans;

public class IssueCollectExport {
	public String DATE;
	public String PROVINCE;
	public String CITY;
	public String REGION;
	public long GRID_ID;
	public float LON;
	public float LAT;
	public String AVGRSRP;
	public int C_SUM_COUNT;
	public int C_SUM_POOR;
	public float C_COV_RATE;
	public int U_SUM_COUNT;
	public float U_COV_RATE;
	public int T_SUM_COUNT;
	public float T_COV_RATE;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public long TOP1ECI;
	public String TOP1NAME;
	public int TOP1MRPOORNUM;
	public float TOP1_POOR_RATE;
	public int TOP1DIR;
	public int TOP1HEIGHT;
	public int TOP1TILT;
	public float TOP1DIS;
	public String TOP1BAND;
	public long TOP2ECI;
	public String TOP2NAME;
	public int TOP2MRPOORNUM;
	public float TOP2_POOR_RATE;
	public int TOP2DIR;
	public int TOP2HEIGHT;
	public int TOP2TILT;
	public float TOP2DIS;
	public String TOP2BAND;
	public long TOP3ECI;
	public String TOP3NAME;
	public int TOP3MRPOORNUM;
	public float TOP3_POOR_RATE;
	public int TOP3DIR;
	public int TOP3HEIGHT;
	public int TOP3TILT;
	public float TOP3DIS;
	public String TOP3BAND;
	public String NEAR_TDD_OUT;
	public float NEAR_TDD_DIS;
	public String NEAR_FDD_OUT;
	public float NEAR_FDD_DIS;
	public String NEAR_INDOOR;
	public float NEAR_INDOOR_DIS;
	public String SUGGEST;
	@Override
	public String toString() {
		return DATE + "," + PROVINCE + "," + CITY + "," + REGION + "," + GRID_ID + "," + LON + "," 
				+ LAT + "," + AVGRSRP + "," + C_SUM_COUNT + "," + C_SUM_POOR + "," + C_COV_RATE + "," 
				+ U_SUM_COUNT + "," + U_COV_RATE + "," + T_SUM_COUNT + "," + T_COV_RATE + "," 
				+ SCENE_MAJOR + "," + SCENE_MINOR + "," + TOP1ECI + "," + TOP1NAME + "," + TOP1MRPOORNUM + "," 
				+ TOP1_POOR_RATE + "," + TOP1DIR + "," + TOP1HEIGHT + "," + TOP1TILT + "," + TOP1DIS + "," 
				+ TOP1BAND + "," + TOP2ECI + "," + TOP2NAME + "," + TOP2MRPOORNUM + "," + TOP2_POOR_RATE + "," 
				+ TOP2DIR + "," + TOP2HEIGHT + "," + TOP2TILT + "," + TOP2DIS + "," + TOP2BAND + "," + TOP3ECI + "," 
				+ TOP3NAME + "," + TOP3MRPOORNUM + "," + TOP3_POOR_RATE + "," + TOP3DIR + "," + TOP3HEIGHT + "," 
				+ TOP3TILT + "," + TOP3DIS + "," + TOP3BAND + "," + NEAR_TDD_OUT + "," + NEAR_TDD_DIS + "," 
				+ NEAR_FDD_OUT + "," + NEAR_FDD_DIS + "," + NEAR_INDOOR + "," + NEAR_INDOOR_DIS + "," + SUGGEST;
	}

}
