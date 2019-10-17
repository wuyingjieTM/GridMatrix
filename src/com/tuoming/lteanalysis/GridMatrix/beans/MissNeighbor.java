package com.tuoming.lteanalysis.GridMatrix.beans;

/**
 * 
* @ClassName: MissNeighbor 
* @Description: TODO 邻区漏配信息
* @author kouxuejin 
* @date 2018年11月6日 下午4:49:00 
*
 */
public class MissNeighbor {
	public String DATE;
	public String CITY;
	public String SCENE_MAJOR;
	public String SCENE_MINOR;
	public long GRID_ID;
	public long S_ECI;
	public String S_CELL_NAME;
	public int S_CELL_NUM;
	public float S_CELL_RATE;
	public long D_ECI;
	public String D_CELL_NAME;
	public int D_CELL_NUM;
	public float D_CELL_RATE;
	public float distance;
	@Override
	public String toString() {
		return DATE + "," + CITY + "," + GRID_ID + "," + S_ECI + "," + S_CELL_NAME + "," + S_CELL_NUM + ","
				+ S_CELL_RATE + "," + D_ECI + "," + D_CELL_NAME + "," + D_CELL_NUM + "," + D_CELL_RATE;
	}
	
	public String sceneToString() {
		return DATE + "," + CITY + "," + SCENE_MAJOR + "," + SCENE_MINOR + "," + GRID_ID + "," + S_ECI + "," + S_CELL_NAME + "," + S_CELL_NUM + ","
				+ S_CELL_RATE + "," + D_ECI + "," + D_CELL_NAME + "," + D_CELL_NUM + "," + D_CELL_RATE + "\n";
	}
}
