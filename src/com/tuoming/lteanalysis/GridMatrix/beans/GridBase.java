package com.tuoming.lteanalysis.GridMatrix.beans;

public class GridBase {
	public String	DATE;
	public String	PROVINCE;
	public String	CITY;
	public String	SCENE_MAJOR;
	public String	SCENE_MINOR;
	public long	GRID_ID;
	public float	LON;
	public float	LAT;
	public long	CELL_ID;
	//public int	DEGRADE;
	public int	C_SUM_COUNT;
	public int	C_SUM_POOR;
	public float  C_MEAN_RSRP;//汇总导出
	public int	U_SUM_COUNT;//汇总导出
	public int	U_SUM_POOR;//汇总导出
	//public float	U_MEAN_RSRP;
	public int	T_SUM_COUNT;//汇总导出
	public int	T_SUM_POOR;//汇总导出
	//public float	T_MEAN_RSRP;
	//public String	WO_ID;
}
