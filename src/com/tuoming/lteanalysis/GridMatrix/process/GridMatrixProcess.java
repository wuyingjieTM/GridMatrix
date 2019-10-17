package com.tuoming.lteanalysis.GridMatrix.process;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.tuoming.lteanalysis.GridMatrix.beans.AlarmCount;
import com.tuoming.lteanalysis.GridMatrix.beans.AlarmList;
import com.tuoming.lteanalysis.GridMatrix.beans.AnaGridData;
import com.tuoming.lteanalysis.GridMatrix.beans.CellInfo;
import com.tuoming.lteanalysis.GridMatrix.beans.CellNeighbor;
import com.tuoming.lteanalysis.GridMatrix.beans.CellUnit;
import com.tuoming.lteanalysis.GridMatrix.beans.CheckParaList;
import com.tuoming.lteanalysis.GridMatrix.beans.CoverStep;
import com.tuoming.lteanalysis.GridMatrix.beans.CoverWeak;
import com.tuoming.lteanalysis.GridMatrix.beans.EngineerData;
import com.tuoming.lteanalysis.GridMatrix.beans.Grid2mgrs;
import com.tuoming.lteanalysis.GridMatrix.beans.GridBase;
import com.tuoming.lteanalysis.GridMatrix.beans.GridInfo;
import com.tuoming.lteanalysis.GridMatrix.beans.GridIssueanaAngle;
import com.tuoming.lteanalysis.GridMatrix.beans.GridIssueanaAntenna;
import com.tuoming.lteanalysis.GridMatrix.beans.GridIssueanaBackbuild;
import com.tuoming.lteanalysis.GridMatrix.beans.GridIssueanaNobestcell;
import com.tuoming.lteanalysis.GridMatrix.beans.GridIssueanaOvercover;
import com.tuoming.lteanalysis.GridMatrix.beans.GridIssueanaOverdistance;
import com.tuoming.lteanalysis.GridMatrix.beans.GridLineList;
import com.tuoming.lteanalysis.GridMatrix.beans.GridOverList;
import com.tuoming.lteanalysis.GridMatrix.beans.GridResult;
import com.tuoming.lteanalysis.GridMatrix.beans.GridTask;
import com.tuoming.lteanalysis.GridMatrix.beans.IssueCitySuggest;
import com.tuoming.lteanalysis.GridMatrix.beans.IssueCollectExport;
import com.tuoming.lteanalysis.GridMatrix.beans.IssueGridResult;
import com.tuoming.lteanalysis.GridMatrix.beans.IssueGridSuggest;
import com.tuoming.lteanalysis.GridMatrix.beans.IssueGridSuggestCount;
import com.tuoming.lteanalysis.GridMatrix.beans.IssueSceneSuggest;
import com.tuoming.lteanalysis.GridMatrix.beans.IssueanaScene;
import com.tuoming.lteanalysis.GridMatrix.beans.Matrix;
import com.tuoming.lteanalysis.GridMatrix.beans.MissNeighbor;
import com.tuoming.lteanalysis.GridMatrix.beans.MrsAoaCover;
import com.tuoming.lteanalysis.GridMatrix.beans.MrsStatAoa;
import com.tuoming.lteanalysis.GridMatrix.beans.MrsStatRsrp;
import com.tuoming.lteanalysis.GridMatrix.beans.SceneTask;
import com.tuoming.lteanalysis.GridMatrix.beans.SimulCover;
import com.tuoming.lteanalysis.GridMatrix.config.configer;
import com.tuoming.lteanalysis.GridMatrix.input.DbDataSource;
import com.tuoming.lteanalysis.GridMatrix.input.FileDataSource;
import com.tuoming.lteanalysis.GridMatrix.input.IDataSource;
import com.tuoming.lteanalysis.GridMatrix.output.FileExporter;
import com.tuoming.lteanalysis.GridMatrix.output.IExporter;
import com.tuoming.lteanalysis.GridMatrix.tools.Helper;

public class GridMatrixProcess {
	// 常量
	final byte SCENE_FLAG = 0;
	final byte CITY_FLAG = 1;
	final byte ALARM = 0;
	final byte MISSNEI = 1;
	final byte ANTENNA = 2;
	final byte ANGLE = 3;
	final byte OVERCOVER = 4;
	final byte OVERDISTANCE = 5;
	final byte BACKBUILD = 6;
	final byte NOBESTCELL = 7;

	final String SPLITER = "_";

	private static Logger logger = Logger.getLogger(GridMatrixProcess.class);
	private String date = "";
	configer config = null;

	IDataSource ds = null;
	// 数据输出句柄、
	IExporter gridResultExporter = null;
	IExporter issueGridExporter = null;
	IExporter issueGridSuggestExporter = null;
	IExporter gridSuggestCountExporter = null;
	IExporter aoACoverExporter = null;
	// 栅格级别
	IExporter gridAlarmExporter = null;
	IExporter gridMissExporter = null;
	IExporter gridTKExporter = null;
	IExporter gridAoaExporter = null;
	IExporter gridNoBestCellExporter = null;//wyj
	IExporter gridOvercoverExporter = null;
	IExporter gridOverdistanceExporter = null;
	IExporter gridBackbuildExporter = null;
	// 场景级别
	IExporter sceneAlarmExporter = null;
	IExporter sceneMissExporter = null;
	IExporter sceneTKExporter = null;
	IExporter sceneAoaExporter = null;
	IExporter sceneOvercoverExporter = null;
	IExporter sceneOverdistanceExporter = null;
	IExporter sceneBackbuildExporter = null;
	// 地市级别
	IExporter cityAlarmExporter = null;
	IExporter cityMissExporter = null;
	IExporter cityTKExporter = null;
	IExporter cityAoaExporter = null;
	IExporter cityOvercoverExporter = null;
	IExporter cityOverdistanceExporter = null;
	IExporter cityBackbuildExporter = null;

	IExporter sceneSuggestExporter = null;
	IExporter sceneExporter = null;
	IExporter cityExporter = null;
	//问题点管理汇总导出
	IExporter collectExporter = null;

	// 仿真数据
	IExporter gridTaskExporter = null;
	IExporter sceneTaskExporter = null;

	// 数据加载
	Map<String, GridResult> gridResultMap = new HashMap<String, GridResult>();

	Map<String, Matrix> matrixMap = null;
	Map<Long, CellUnit> cellUnitMap = null;
	Map<String, GridLineList> gridLineMap = null;
	Map<String, GridOverList> gridOverMap = null;
	Map<Long, CellNeighbor> cellNeighborMap = null;
	Map<Long, CellInfo> cellMap = null;
	Map<Long, CellInfo> cellMap_history1 = null;
	Map<Long, CellInfo> cellMap_history2 = null;
	Map<Long, CellInfo> cellMap_history3 = null;
	Map<Long, AlarmCount> alarmCountMap = null;
	Map<Long, List<AlarmList>> alarmListMap = null;
	Map<Long, List<CoverStep>> coverStepMap = null;
	Map<Long, CoverWeak> coverWeakMap = null;
	Map<Long, EngineerData> engdataMap = null;
	Map<Long, List<MissNeighbor>> missNeighborMap = null;
	Map<String, GridInfo> gridMap = null;
	Map<String, GridInfo> beforeGridMap = null;
	Map<String, Map<Long, GridBase>> gridBaseMap = null;
	Map<Long, List<GridBase>> gridBaseEciMap = new HashMap<Long, List<GridBase>>();
	Map<String, Grid2mgrs> grid2mgrsMap = null;
	Map<Long, CheckParaList> checkParaListMap = null;
	Map<Long, MrsAoaCover> mrsAoaCover60Map = new HashMap<Long, MrsAoaCover>();
	Map<Long, MrsStatAoa> mrsStatAoaMap = null;
	Map<Long, MrsStatRsrp> mrsStatRsrpMap = null;
	Map<String, SimulCover> simulCoverMap = null;
	
	Map<String, AnaGridData> anaGridDataMap = null;

	// 场景级八维度结果缓存
	HashMap<String, HashMap<Byte, HashMap<String, Object>>> sceneSummaryMap = new HashMap<String, HashMap<Byte, HashMap<String, Object>>>();
	// 地市级把唯独结果缓存
	HashMap<String, HashMap<Byte, HashMap<String, Object>>> citySummaryMap = new HashMap<String, HashMap<Byte, HashMap<String, Object>>>();
	// 场景级问题栅格结论汇总
	HashMap<String, HashMap<Byte, HashMap<String, Long>>> sceneGridsCountMap = new HashMap<String, HashMap<Byte, HashMap<String, Long>>>();
	// 地市级问题栅格结论汇总
	HashMap<String, HashMap<Byte, HashMap<String, Long>>> cityGridsCountMap = new HashMap<String, HashMap<Byte, HashMap<String, Long>>>();
	// 场景级问题栅格置信度汇总
	HashMap<String, HashMap<Byte, Long>> sceneBeliveSummaryMap = new HashMap<String, HashMap<Byte, Long>>();

	public GridMatrixProcess(configer config, String exportPath, String date) {
		this.config = config;
		this.date = date;
	}

	public int init() {
		String dataSourceType = config.GetDataSourceType();
		if (dataSourceType.equals("1") && config.GetDbInfo() != -1) {
			ds = new DbDataSource(config);
		} else if (dataSourceType.equals("2")) {
			ds = new FileDataSource(config);
		} else {
			logger.error("GridMatrixProcess init error");
			return -1;
		}

		gridResultExporter = new FileExporter("ana_grid_result_" + date + ".txt");
		if (gridResultExporter.init(config) != 0) {
			return -1;
		}
		issueGridExporter = new FileExporter("web_issueana_grid_" + date + ".txt");
		if (issueGridExporter.init(config) != 0) {
			return -1;
		}

		issueGridSuggestExporter = new FileExporter("web_issueana_grid_suggest_" + date + ".txt");
		if (issueGridSuggestExporter.init(config) != 0) {
			return -1;
		}

		gridSuggestCountExporter = new FileExporter("web_issueana_grid_suggest_count_" + date + ".txt");
		if (gridSuggestCountExporter.init(config) != 0) {
			return -1;
		}
		aoACoverExporter = new FileExporter("web_mrs_aoa_cover_" + date + ".txt");
		if (aoACoverExporter.init(config) != 0) {
			return -1;
		}
		// 栅格级别
		gridAlarmExporter = new FileExporter("web_grid_issueana_alarm_" + date + ".txt");
		if (gridAlarmExporter.init(config) != 0) {
			return -1;
		}

		gridMissExporter = new FileExporter("web_grid_issueana_missnei_" + date + ".txt");
		if (gridMissExporter.init(config) != 0) {
			return -1;
		}
		gridTKExporter = new FileExporter("web_grid_issueana_antenna_" + date + ".txt");
		if (gridTKExporter.init(config) != 0) {
			return -1;
		}
		gridAoaExporter = new FileExporter("web_grid_issueana_angle_" + date + ".txt");
		if (gridAoaExporter.init(config) != 0) {
			return -1;
		}
		gridNoBestCellExporter = new FileExporter("web_grid_issueana_nobestcell_" + date + ".txt");
		if (gridNoBestCellExporter.init(config) != 0) {
			return -1;
		}
		gridOvercoverExporter = new FileExporter("web_grid_issueana_overcover_" + date + ".txt");
		if (gridOvercoverExporter.init(config) != 0) {
			return -1;
		}
		gridOverdistanceExporter = new FileExporter("web_grid_issueana_overdistance_" + date + ".txt");
		if (gridOverdistanceExporter.init(config) != 0) {
			return -1;
		}
		gridBackbuildExporter = new FileExporter("web_grid_issueana_backbuild_" + date + ".txt");
		if (gridBackbuildExporter.init(config) != 0) {
			return -1;
		}

		// 场景级别
		sceneAlarmExporter = new FileExporter("web_scene_issueana_alarm_" + date + ".txt");
		if (sceneAlarmExporter.init(config) != 0) {
			return -1;
		}

		sceneMissExporter = new FileExporter("web_scene_issueana_missnei_" + date + ".txt");
		if (sceneMissExporter.init(config) != 0) {
			return -1;
		}
		sceneTKExporter = new FileExporter("web_scene_issueana_antenna_" + date + ".txt");
		if (sceneTKExporter.init(config) != 0) {
			return -1;
		}
		sceneAoaExporter = new FileExporter("web_scene_issueana_angle_" + date + ".txt");
		if (sceneAoaExporter.init(config) != 0) {
			return -1;
		}
		sceneOvercoverExporter = new FileExporter("web_scene_issueana_overcover_" + date + ".txt");
		if (sceneOvercoverExporter.init(config) != 0) {
			return -1;
		}
		sceneOverdistanceExporter = new FileExporter("web_scene_issueana_overdistance_" + date + ".txt");
		if (sceneOverdistanceExporter.init(config) != 0) {
			return -1;
		}
		sceneBackbuildExporter = new FileExporter("web_scene_issueana_backbuild_" + date + ".txt");
		if (sceneBackbuildExporter.init(config) != 0) {
			return -1;
		}

		// 地市级别
		cityAlarmExporter = new FileExporter("web_city_issueana_alarm_" + date + ".txt");
		if (cityAlarmExporter.init(config) != 0) {
			return -1;
		}

		cityMissExporter = new FileExporter("web_city_issueana_missnei_" + date + ".txt");
		if (cityMissExporter.init(config) != 0) {
			return -1;
		}
		cityTKExporter = new FileExporter("web_city_issueana_antenna_" + date + ".txt");
		if (cityTKExporter.init(config) != 0) {
			return -1;
		}
		cityAoaExporter = new FileExporter("web_city_issueana_angle_" + date + ".txt");
		if (cityAoaExporter.init(config) != 0) {
			return -1;
		}
		cityOvercoverExporter = new FileExporter("web_city_issueana_overcover_" + date + ".txt");
		if (cityOvercoverExporter.init(config) != 0) {
			return -1;
		}
		cityOverdistanceExporter = new FileExporter("web_city_issueana_overdistance_" + date + ".txt");
		if (cityOverdistanceExporter.init(config) != 0) {
			return -1;
		}
		cityBackbuildExporter = new FileExporter("web_city_issueana_backbuild_" + date + ".txt");
		if (cityBackbuildExporter.init(config) != 0) {
			return -1;
		}

		sceneSuggestExporter = new FileExporter("web_issueana_scene_suggest_" + date + ".txt");
		if (sceneSuggestExporter.init(config) != 0) {
			return -1;
		}
		sceneExporter = new FileExporter("web_issueana_scene_" + date + ".txt");
		if (sceneExporter.init(config) != 0) {
			return -1;
		}
		cityExporter = new FileExporter("web_issueana_city_suggest_" + date + ".txt");
		if (cityExporter.init(config) != 0) {
			return -1;
		}
		//问题点管理汇总导出
		collectExporter = new FileExporter("web_grid_suggestunion_" + date + ".txt");
		if (collectExporter.init(config) != 0) {
			return -1;
		}
		// 仿真数据
		gridTaskExporter = new FileExporter("web_grid_simul_" + date + ".txt");
		if (gridTaskExporter.init(config) != 0) {
			return -1;
		}
		sceneTaskExporter = new FileExporter("web_scene_simul_" + date + ".txt");
		if (sceneTaskExporter.init(config) != 0) {
			return -1;
		}
		logger.info("GridMatrixProcess init success");
		return 0;
	}

	private int loaddata() {
		String fdate = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
		String fdate_1 = Helper.GetFmtDate(fdate, -1);
		String fdate_2 = Helper.GetFmtDate(fdate, -2);
		String fdate_3 = Helper.GetFmtDate(fdate, -3);

		// load data
		gridMap = ds.LoadGrid(config.GetOneConfig("LastGridDate"));
		if (gridMap.size() == 0) {
			logger.error("no valid grid data!!!");
			return -1;
		}

		beforeGridMap = ds.LoadBeforeGrid();
		matrixMap = ds.LoadMatrix();

		gridLineMap = ds.LoadGridLineList(fdate);
		gridOverMap = ds.LoadGridOverList(fdate);
		cellUnitMap = ds.LoadCellUnit(fdate);
		cellNeighborMap = ds.LoadCellneighbor(fdate);
		cellMap = ds.LoadCell(fdate);
		cellMap_history1 = ds.LoadCell(fdate_1);
		cellMap_history2 = ds.LoadCell(fdate_2);
		cellMap_history3 = ds.LoadCell(fdate_3);
		checkParaListMap = ds.LoadCheckParaList(fdate);
		alarmCountMap = ds.LoadAlarmCount(fdate);
		alarmListMap = ds.LoadAlarmList(fdate);
		coverStepMap = ds.LoadCoverStep(fdate);
		coverWeakMap = ds.LoadCoverWeak(fdate);
		engdataMap = ds.LoadEngineerData(fdate);
		missNeighborMap = ds.LoadMissNeighbor(fdate);// no test
		simulCoverMap = ds.LoadSimulCover(fdate);
		gridBaseMap = ds.LoadGridBase(fdate);
		grid2mgrsMap = ds.LoadGrid2Mgrs();
		mrsStatAoaMap = ds.LoadMrsStatAoa(fdate);
		// mrsAoaCoverMap = ds.LoadMrsAoaCover(fdate);
		mrsStatRsrpMap = ds.LoadMrsStatRsrp(fdate);
		
		anaGridDataMap = ds.LoadAnaGridData(fdate,engdataMap);

		for (Map<Long, GridBase> it : gridBaseMap.values()) {
			for (Entry<Long, GridBase> gb : it.entrySet()) {
				List<GridBase> gblist = gridBaseEciMap.get(gb.getKey());
				if (gblist == null) {
					gblist = new ArrayList<GridBase>();
					gridBaseEciMap.put(gb.getKey(), gblist);
				}
				gblist.add(gb.getValue());
			}
		}
		return 0;
	}

	public void process() {
		logger.info("start processing...");
		long b = System.currentTimeMillis();
		if (loaddata() != 0) {
			return;
		}
		// 计算并输出极坐标
		AoaCoverProcess();
		// 输出ana_grid_result
		GridResultProcess();
		// 输出web_issueana_grid
		IssueGridProcess();
		// 输出栅格分析各维度结论建议
		GridConclusionProcess();
		// 输出场景维度问题栅格置信度表
		SceneBeliveProcess();
		// 输出场景维度栅格汇总结论建议
		SceneConclusionProcess();
		// 输出地市维度栅格汇总结论建议
		CityConclusionProcess();
		// 问题管理导出汇总
		IssuesCollectExportProcess();

		long e = System.currentTimeMillis();
		logger.info("Processor total use " + String.format("%.3f", 1e-3 * (e - b)) + " sec");
	}

	private void AoaCoverProcess() {
		long timevalue = 0;
		for (Entry<Long, MrsStatAoa> entry : mrsStatAoaMap.entrySet()) {
			long b = System.currentTimeMillis();
			MrsStatAoa source = entry.getValue();
			if (source.reportnum == 0) {
				continue;
			}
			MrsAoaCover result60 = new MrsAoaCover();
			result60.date = source.date;
			result60.eci = entry.getKey();
			result60.avgaoa = source.avgaoa;
			result60.inclinationtype = 1;
			// 计算60度夹角内采样点数
			// 正30度夹角
			for (int i = 0; i < 72; i++) {
				int sum = 0;
				for (int j = i; j <= i + 5; j++) {
					sum += source.aoa[j > 71 ? j - 72 : j];
				}
				// 负30度夹角
				for (int k = i - 1; k >= i - 6; k--) {
					sum += source.aoa[k < 0 ? 72 + k : k];
				}
				result60.aoa[i] = (float) sum / source.reportnum;
			}
			mrsAoaCover60Map.put(result60.eci, result60);
			// 计算120度夹角内采样点数
			MrsAoaCover result120 = new MrsAoaCover();
			result120.date = source.date;
			result120.eci = entry.getKey();
			result120.avgaoa = source.avgaoa;
			result120.inclinationtype = 2;
			for (int i = 0; i < 72; i++) {
				// 正60度夹角
				int sum = 0;
				for (int j = i; j <= i + 11; j++) {
					sum += source.aoa[j > 71 ? j - 72 : j];
				}
				// 负60度夹角
				for (int k = i - 1; k >= i - 12; k--) {
					sum += source.aoa[k < 0 ? 72 + k : k];
				}
				result120.aoa[i] = (float) sum / source.reportnum;
			}
			timevalue += System.currentTimeMillis() - b;
			aoACoverExporter.Export(result60);
			aoACoverExporter.Export(result120);
		}
		logger.info("web_mrs_aoa_cover Process use " + String.format("%.3f", 1e-3 * timevalue) + " sec");
		aoACoverExporter.finish();
	}

	private void GridResultProcess() {
		long timevalue = 0;

		for (GridInfo g : gridMap.values()) {
			long b = System.currentTimeMillis();
			GridResult result = new GridResult();
			result.DATE = g.DATE;
			result.CITY = g.CITY;
			result.PROVINCE = g.PROVINCE;
			result.GRID_ID = g.GRID_ID;
			result.TOP1ECI = g.TOP1ECI;
			result.TOP2ECI = g.TOP2ECI;
			result.TOP3ECI = g.TOP3ECI;
			result.TOP1CELLRATE = GetCellRate(g.TOP1MRPOORNUM, g.MRPOORNUM, matrixMap.get("TOP1CELLRATE"));
			result.TOP2CELLRATE = GetCellRate(g.TOP2MRPOORNUM, g.MRPOORNUM, matrixMap.get("TOP2CELLRATE"));
			result.TOP3CELLRATE = GetCellRate(g.TOP3MRPOORNUM, g.MRPOORNUM, matrixMap.get("TOP3CELLRATE"));
			result.TOP1COVERRATE = GetCoverRate(mrsStatRsrpMap.get(g.TOP1ECI), matrixMap.get("TOP1COVERRATE"));
			result.TOP2COVERRATE = GetCoverRate(mrsStatRsrpMap.get(g.TOP2ECI), matrixMap.get("TOP2COVERRATE"));
			result.TOP3COVERRATE = GetCoverRate(mrsStatRsrpMap.get(g.TOP3ECI), matrixMap.get("TOP3COVERRATE"));
			result.TOP1DIRDIFFAOA = GetDirDiffAoa(g.TOP1ANGLE, engdataMap.get(g.TOP1ECI),
					matrixMap.get("TOP1DIRDIFFAOA"));
			result.TOP1DIRDIFFAOA = GetDirDiffAoa(g.TOP2ANGLE, engdataMap.get(g.TOP2ECI),
					matrixMap.get("TOP2DIRDIFFAOA"));
			result.TOP1DIRDIFFAOA = GetDirDiffAoa(g.TOP3ANGLE, engdataMap.get(g.TOP3ECI),
					matrixMap.get("TOP3DIRDIFFAOA"));
			result.TOP1DISOVERAVGSITE = GetDisOverAvgSite(g.TOP1DIS, engdataMap.get(g.TOP1ECI),
					matrixMap.get("TOP1DISOVERAVGSITE"));
			result.TOP2DISOVERAVGSITE = GetDisOverAvgSite(g.TOP2DIS, engdataMap.get(g.TOP2ECI),
					matrixMap.get("TOP2DISOVERAVGSITE"));
			result.TOP3DISOVERAVGSITE = GetDisOverAvgSite(g.TOP3DIS, engdataMap.get(g.TOP3ECI),
					matrixMap.get("TOP3DISOVERAVGSITE"));
			result.TOP1OVERCOVER = GetTop1OverCover(g.TOP1ECI, matrixMap.get("TOP1OVERCOVER"));
			result.TOP1POORCOVER = GetPoorCover(g, matrixMap.get("TOP1POORCOVER"));
			result.CELLLIST = GetCellList(g, matrixMap.get("CELLLIST"));
			result.HISMRDEV = GetHisMrDev(g, matrixMap.get("HISMRDEV"));
			result.TOP1MISSNEI = missNeighborMap.get(g.TOP1ECI) == null ? 0 : 1;
			result.TOP1MISSECIMR = 0;
			result.TOP1MISSECIMRRATE = GetTop1MissEciMrRate(g.TOP1ECI, matrixMap.get("TOP1MISSECIMRRATE"));
			result.TOPALARMINCELL = GetAlarmIncell(g, alarmCountMap, matrixMap.get("TOPALARMINCELL"));
			result.ALARMEFFCOVER = 0;
			result.OTHERCELLEXIST = GetOtherCellExist(g, gridLineMap.get(g.GRID_ID + SPLITER + g.TOP1ECI), alarmCountMap,
					matrixMap.get("OTHERCELLEXIST"));
			result.TRAFFIC = GetTaffic(cellMap.get(g.TOP1ECI), matrixMap.get("TRAFFIC"));
			result.ESRVCCHORATE = GetErvccHoRate(cellMap.get(g.TOP1ECI), matrixMap.get("ESRVCCHORATE"));
			result.HISESRVCCHO = GetHisEssrvccHo(g.TOP1ECI, matrixMap.get("HISESRVCCHO"));
			result.SIMUlCOVER = GetSimulCover(g, matrixMap.get("SIMUlCOVER"));
			result.ADJDIR = GetAdjDir(g, matrixMap.get("ADJDIR"));
			result.ADJANGLE = GetAdjAngle(g, matrixMap.get("ADJANGLE"));
			result.POVERERROR = GetPoverError(g, matrixMap.get("POVERERROR"));
			String scene[] = GetSceneMinor(result.CITY, result.GRID_ID);
			result.SCENE_MAJOR = scene[0];
			result.SCENE_MINOR = scene[1];
			gridResultMap.put(result.CITY + SPLITER + result.GRID_ID, result);
			timevalue += System.currentTimeMillis() - b;
			gridResultExporter.Export(result);
		}
		logger.info("ana_grid_result Process use " + String.format("%.3f", 1e-3 * timevalue) + " sec");
		gridResultExporter.finish();
	}

	private void IssueGridProcess() {
		long timevalue = 0;
		for (GridResult g : gridResultMap.values()) {
			long b = System.currentTimeMillis();
			IssueGridResult result = new IssueGridResult();
			result.DATE = g.DATE;
			result.PROVINCE = g.PROVINCE;
			result.CITY = g.CITY;
			result.GRID_ID = g.GRID_ID;
			result.TOP1ECI = g.TOP1ECI;
			result.TOP2ECI = g.TOP2ECI;
			result.TOP3ECI = g.TOP3ECI;
			// 获取一条GridResult 的矩阵汇总结果
			List<IssueGridResult> oneIssuetempList = new ArrayList<IssueGridResult>();
			oneIssuetempList.add(SetOneItems(g.TOP1CELLRATE, matrixMap.get("TOP1CELLRATE")));
			oneIssuetempList.add(SetOneItems(g.TOP1COVERRATE, matrixMap.get("TOP1COVERRATE")));
			oneIssuetempList.add(SetOneItems(g.TOP1DIRDIFFAOA, matrixMap.get("TOP1DIRDIFFAOA")));
			oneIssuetempList.add(SetOneItems(g.TOP1DISOVERAVGSITE, matrixMap.get("TOP1DISOVERAVGSITE")));
			oneIssuetempList.add(SetOneItems(g.TOP2CELLRATE, matrixMap.get("TOP2CELLRATE")));
			oneIssuetempList.add(SetOneItems(g.TOP2COVERRATE, matrixMap.get("TOP2COVERRATE")));
			oneIssuetempList.add(SetOneItems(g.TOP2DIRDIFFAOA, matrixMap.get("TOP2DIRDIFFAOA")));
			oneIssuetempList.add(SetOneItems(g.TOP2DISOVERAVGSITE, matrixMap.get("TOP2DISOVERAVGSITE")));
			oneIssuetempList.add(SetOneItems(g.TOP3CELLRATE, matrixMap.get("TOP3CELLRATE")));
			oneIssuetempList.add(SetOneItems(g.TOP3COVERRATE, matrixMap.get("TOP3COVERRATE")));
			oneIssuetempList.add(SetOneItems(g.TOP3DIRDIFFAOA, matrixMap.get("TOP3DIRDIFFAOA")));
			oneIssuetempList.add(SetOneItems(g.TOP3DISOVERAVGSITE, matrixMap.get("TOP3DISOVERAVGSITE")));
			oneIssuetempList.add(SetOneItems(g.CELLLIST, matrixMap.get("CELLLIST")));
			oneIssuetempList.add(SetOneItems(g.HISMRDEV, matrixMap.get("HISMRDEV")));
			oneIssuetempList.add(SetOneItems(g.TOP1OVERCOVER, matrixMap.get("TOP1OVERCOVER")));
			oneIssuetempList.add(SetOneItems(g.TOP1POORCOVER, matrixMap.get("TOP1POORCOVER")));
			oneIssuetempList.add(SetOneItems(g.TOP1MISSNEI, matrixMap.get("TOP1MISSNEI")));
			oneIssuetempList.add(SetOneItems(g.TOP1MISSECIMR, matrixMap.get("TOP1MISSECIMR")));
			oneIssuetempList.add(SetOneItems(g.TOP1MISSECIMRRATE, matrixMap.get("TOP1MISSECIMRRATE")));
			oneIssuetempList.add(SetOneItems(g.TOPALARMINCELL, matrixMap.get("TOPALARMINCELL")));
			oneIssuetempList.add(SetOneItems(g.ALARMEFFCOVER, matrixMap.get("ALARMEFFCOVER")));
			oneIssuetempList.add(SetOneItems(g.OTHERCELLEXIST, matrixMap.get("OTHERCELLEXIST")));
			oneIssuetempList.add(SetOneItems(g.TRAFFIC, matrixMap.get("TRAFFIC")));
			oneIssuetempList.add(SetOneItems(g.ESRVCCHORATE, matrixMap.get("ESRVCCHORATE")));
			oneIssuetempList.add(SetOneItems(g.HISESRVCCHO, matrixMap.get("HISESRVCCHO")));
			oneIssuetempList.add(SetOneItems(g.SIMUlCOVER, matrixMap.get("SIMUlCOVER")));
			oneIssuetempList.add(SetOneItems(g.POVERERROR, matrixMap.get("POVERERROR")));
			oneIssuetempList.add(SetOneItems(g.ADJDIR, matrixMap.get("ADJDIR")));
			oneIssuetempList.add(SetOneItems(g.ADJANGLE, matrixMap.get("ADJANGLE")));
			SumarryOneGridResult(result, oneIssuetempList);
			SumarrySceneBelive(g, result);
			timevalue += System.currentTimeMillis() - b;
			issueGridExporter.Export(result);
		}
		logger.info("web_issueana_grid Process use " + String.format("%.3f", 1e-3 * timevalue) + " sec");
		issueGridExporter.finish();
	}

	private void GridConclusionProcess() {
		long timevalue = 0;
		for (GridResult g : gridResultMap.values()) {
			long b = System.currentTimeMillis();
			GridInfo gi = gridMap.get(g.CITY + SPLITER + g.GRID_ID);
			GridOverList goList = gridOverMap.get(g.CITY + SPLITER + g.GRID_ID);
			EngineerData ed1 = engdataMap.get(g.TOP1ECI);
			EngineerData ed2 = engdataMap.get(g.TOP2ECI);
			EngineerData ed3 = engdataMap.get(g.TOP3ECI);
			GridLineList gll1 = gridLineMap.get(g.GRID_ID + SPLITER + g.TOP1ECI);
			GridLineList gll2 = gridLineMap.get(g.GRID_ID + SPLITER + g.TOP2ECI);
			GridLineList gll3 = gridLineMap.get(g.GRID_ID + SPLITER + g.TOP3ECI);
			IssueGridSuggestCount gsc = new IssueGridSuggestCount();
			IssueGridSuggest igs = new IssueGridSuggest();
			gsc.DATE = g.DATE;
			gsc.PROVINCE = g.PROVINCE;
			gsc.CITY = g.CITY;
			gsc.GRID_ID = g.GRID_ID;

			igs.DATE = g.DATE;
			igs.PROVINCE = g.PROVINCE;
			igs.CITY = g.CITY;
			igs.GRID_ID = g.GRID_ID;
			igs.ALARM = GetAlarmConclusion(g, goList, gsc);
			igs.MISSNEIGH = GetMissConclusion(g, gi, gsc);
			igs.ANTENNA = GetTKConclusion(g, gi, gsc);
			igs.OVERCOVERTOP1 = GetOverCoverConclusion(g, 1, gi, gll1, ed1, gsc);
			igs.OVERCOVERTOP2 = GetOverCoverConclusion(g, 2, gi, gll2, ed2, gsc);
			igs.OVERCOVERTOP3 = GetOverCoverConclusion(g, 3, gi, gll3, ed3, gsc);
			igs.ANGLETOP1 = GetAOAConclusion(g, gi.TOP1ANGLE, 1, ed1, gsc);
			igs.ANGLETOP2 = GetAOAConclusion(g, gi.TOP2ANGLE, 2, ed2, gsc);
			igs.ANGLETOP3 = GetAOAConclusion(g, gi.TOP3ANGLE, 3, ed3, gsc);
			igs.NOBESTCELL = GetNoBestCellConclusion(g, gi, gsc);//wyj
			igs.BACKBUILD = GetBackBuildConclusion(g, gi, gsc);
			igs.OVERDISTANCE = GetOverDistanceConclusion(g, gi, gsc);

			if (gsc.OCCount[0] > 0) {
				gsc.OVERCOVER = "共有" + gsc.OCCount[0] + "个小区疑是越区覆盖，建议对" + gsc.OCCount[1] + "个小区天线倾角进行调整。";
			}
			if (gsc.ACount > 0) {
				gsc.ANGLE = "共有" + gsc.ACount + "个小区天线方位角不合理，建议调整。";
			}
			timevalue += System.currentTimeMillis() - b;
			issueGridSuggestExporter.Export(igs);
			gridSuggestCountExporter.Export(gsc);
		}
		logger.info("web_issueana_grid_suggest Process use " + String.format("%.3f", 1e-3 * timevalue) + " sec");
		issueGridSuggestExporter.finish();
		gridSuggestCountExporter.finish();
		gridAlarmExporter.finish();
		gridMissExporter.finish();
		gridTKExporter.finish();
		gridAoaExporter.finish();
		gridNoBestCellExporter.finish();//wyj
		gridOvercoverExporter.finish();
		gridOverdistanceExporter.finish();
		gridBackbuildExporter.finish();
		gridTaskExporter.finish();
	}

	private void SceneConclusionProcess() {
		long timevalue = 0;
		for (Entry<String, HashMap<Byte, HashMap<String, Long>>> e : sceneGridsCountMap.entrySet()) {
			long b = System.currentTimeMillis();
			HashMap<Byte, HashMap<String, Long>> gridinfos = e.getValue();
			HashMap<Byte, HashMap<String, Object>> cellinfos = sceneSummaryMap.get(e.getKey());
			String s[] = e.getKey().split(SPLITER);
			IssueSceneSuggest iss = new IssueSceneSuggest();
			iss.DATE = s[0];
			iss.PROVINCE = s[1];
			iss.CITY = s[2];
			iss.SCENE_MAJOR = s[3];
			iss.SCENE_MINOR = s[4];
			iss.ALARM = SummaryConclusion(SCENE_FLAG, ALARM, gridinfos.get(ALARM),
					cellinfos.get(ALARM) == null ? new HashMap<String, Object>() : cellinfos.get(ALARM));
			iss.ANTENNA = SummaryConclusion(SCENE_FLAG, ANTENNA, gridinfos.get(ANTENNA),
					cellinfos.get(ANTENNA) == null ? new HashMap<String, Object>() : cellinfos.get(ANTENNA));
			iss.ANGLE = SummaryConclusion(SCENE_FLAG, ANGLE, gridinfos.get(ANGLE),
					cellinfos.get(ANGLE) == null ? new HashMap<String, Object>() : cellinfos.get(ANGLE));
			iss.NOBESTCELL = SummaryConclusion(SCENE_FLAG, NOBESTCELL, gridinfos.get(NOBESTCELL),
					cellinfos.get(NOBESTCELL) == null ? new HashMap<String, Object>() : cellinfos.get(NOBESTCELL));
			iss.OVERCOVER = SummaryConclusion(SCENE_FLAG, OVERCOVER, gridinfos.get(OVERCOVER),
					cellinfos.get(OVERCOVER) == null ? new HashMap<String, Object>() : cellinfos.get(OVERCOVER));
			iss.MISSNEIGH = SummaryConclusion(SCENE_FLAG, MISSNEI, gridinfos.get(MISSNEI),
					cellinfos.get(MISSNEI) == null ? new HashMap<String, Object>() : cellinfos.get(MISSNEI));
			iss.OVERDISTANCE = SummaryConclusion(SCENE_FLAG, OVERDISTANCE, gridinfos.get(OVERDISTANCE),
					cellinfos.get(OVERDISTANCE) == null ? new HashMap<String, Object>() : cellinfos.get(OVERDISTANCE));
			iss.BACKBUILD = SummaryConclusion(SCENE_FLAG, BACKBUILD, gridinfos.get(BACKBUILD),
					cellinfos.get(BACKBUILD) == null ? new HashMap<String, Object>() : cellinfos.get(BACKBUILD));
			timevalue += System.currentTimeMillis() - b;
			sceneSuggestExporter.Export(iss);
		}
		logger.info("web_issueana_scene_suggest Process use " + String.format("%.3f", 1e-3 * timevalue) + " sec");
		sceneSuggestExporter.finish();
		sceneAlarmExporter.finish();
		sceneMissExporter.finish();
		sceneTKExporter.finish();
		sceneAoaExporter.finish();
		sceneOvercoverExporter.finish();
		sceneOverdistanceExporter.finish();
		sceneBackbuildExporter.finish();
		sceneTaskExporter.finish();
	}

	private void SceneBeliveProcess() {
		long timevalue = 0;
		long b = System.currentTimeMillis();
		// 按场景组织grid_base栅格数
		HashMap<String, HashSet<Long>> gridBaseSceneSummary = new HashMap<String, HashSet<Long>>();
		for (Map<Long, GridBase> it : gridBaseMap.values()) {
			for (GridBase gb : it.values()) {
				String key = gb.DATE + SPLITER + gb.PROVINCE + SPLITER + gb.CITY + SPLITER + gb.SCENE_MAJOR + SPLITER
						+ gb.SCENE_MINOR;
				HashSet<Long> oneScene = gridBaseSceneSummary.get(key);
				if (oneScene == null) {
					oneScene = new HashSet<Long>();
					gridBaseSceneSummary.put(key, oneScene);
				}
				// key 中有地市，故只需要gridid即可唯一确定一个栅格
				oneScene.add(gb.GRID_ID);
			}
		}

		timevalue += System.currentTimeMillis() - b;
		// 输出场景级置信度结果
		for (Entry<String, HashMap<Byte, Long>> entry : sceneBeliveSummaryMap.entrySet()) {
			b = System.currentTimeMillis();
			IssueanaScene issueScene = new IssueanaScene();
			String s[] = entry.getKey().split(SPLITER);
			issueScene.DATE = s[0];
			issueScene.PROVINCE = s[1];
			issueScene.CITY = s[2];
			issueScene.SCENE_MAJOR = s[3];
			issueScene.SCENE_MINOR = s[4];
			HashSet<Long> oneScene = gridBaseSceneSummary.get(entry.getKey());
			HashMap<Byte, Long> value = entry.getValue();
			issueScene.ALARM = GetSceneBeliveValue(value.get(ALARM), oneScene);
			issueScene.ANGLE = GetSceneBeliveValue(value.get(ANGLE), oneScene);
			issueScene.ANTENNA = GetSceneBeliveValue(value.get(ANTENNA), oneScene);
			issueScene.BACKBUILD = GetSceneBeliveValue(value.get(BACKBUILD), oneScene);
			issueScene.MISSNEIGH = GetSceneBeliveValue(value.get(MISSNEI), oneScene);
			issueScene.NOBESTCELL = GetSceneBeliveValue(value.get(NOBESTCELL), oneScene);
			issueScene.OVERCOVER = GetSceneBeliveValue(value.get(OVERCOVER), oneScene);
			issueScene.OVERDISTANCE = GetSceneBeliveValue(value.get(OVERDISTANCE), oneScene);
			timevalue += System.currentTimeMillis() - b;
			sceneExporter.Export(issueScene);
		}
		logger.info("web_issueana_scene Process use " + String.format("%.3f", 1e-3 * timevalue) + " sec");
		sceneExporter.finish();
	}

	private int GetSceneBeliveValue(long count, HashSet<Long> oneScene) {
		if (oneScene == null)
			return 1;
		if (count == 0)
			return 0;
		if (count <= oneScene.size())
			return 1;
		if (count > oneScene.size() && count <= oneScene.size() * 2)
			return 2;
		return 0;
	}

	private void CityConclusionProcess() {
		long timevalue = 0;
		for (Entry<String, HashMap<Byte, HashMap<String, Long>>> e : cityGridsCountMap.entrySet()) {
			long b = System.currentTimeMillis();
			HashMap<Byte, HashMap<String, Long>> gridinfos = e.getValue();
			HashMap<Byte, HashMap<String, Object>> cellinfos = citySummaryMap.get(e.getKey());
			String s[] = e.getKey().split(SPLITER);
			IssueCitySuggest ics = new IssueCitySuggest();
			ics.DATE = s[0];
			ics.PROVINCE = s[1];
			ics.CITY = s[2];
			ics.ALARM = SummaryConclusion(CITY_FLAG, ALARM, gridinfos.get(ALARM),
					cellinfos.get(ALARM) == null ? new HashMap<String, Object>() : cellinfos.get(ALARM));
			ics.ANTENNA = SummaryConclusion(CITY_FLAG, ANTENNA, gridinfos.get(ANTENNA),
					cellinfos.get(ANTENNA) == null ? new HashMap<String, Object>() : cellinfos.get(ANTENNA));
			ics.ANGLE = SummaryConclusion(CITY_FLAG, ANGLE, gridinfos.get(ANGLE),
					cellinfos.get(ANGLE) == null ? new HashMap<String, Object>() : cellinfos.get(ANGLE));
			ics.NOBESTCELL = SummaryConclusion(CITY_FLAG, NOBESTCELL, gridinfos.get(NOBESTCELL),
					cellinfos.get(NOBESTCELL) == null ? new HashMap<String, Object>() : cellinfos.get(NOBESTCELL));
			ics.OVERCOVER = SummaryConclusion(CITY_FLAG, OVERCOVER, gridinfos.get(OVERCOVER),
					cellinfos.get(OVERCOVER) == null ? new HashMap<String, Object>() : cellinfos.get(OVERCOVER));
			ics.MISSNEIGH = SummaryConclusion(CITY_FLAG, MISSNEI, gridinfos.get(MISSNEI),
					cellinfos.get(MISSNEI) == null ? new HashMap<String, Object>() : cellinfos.get(MISSNEI));
			ics.OVERDISTANCE = SummaryConclusion(CITY_FLAG, OVERDISTANCE, gridinfos.get(OVERDISTANCE),
					cellinfos.get(OVERDISTANCE) == null ? new HashMap<String, Object>() : cellinfos.get(OVERDISTANCE));
			ics.BACKBUILD = SummaryConclusion(CITY_FLAG, BACKBUILD, gridinfos.get(BACKBUILD),
					cellinfos.get(BACKBUILD) == null ? new HashMap<String, Object>() : cellinfos.get(BACKBUILD));
			timevalue += System.currentTimeMillis() - b;
			cityExporter.Export(ics);
		}
		logger.info("web_issueana_scene_suggest Process use " + String.format("%.3f", 1e-3 * timevalue) + " sec");
		cityExporter.finish();
		cityAlarmExporter.finish();
		cityMissExporter.finish();
		cityTKExporter.finish();
		cityAoaExporter.finish();
		cityOvercoverExporter.finish();
		cityOverdistanceExporter.finish();
		cityBackbuildExporter.finish();
	}
	
	//问题汇总导出
	private void IssuesCollectExportProcess() {
		long timevalue = 0;
		for (Entry<String, Map<Long, GridBase>> e : gridBaseMap.entrySet()) {
			long b = System.currentTimeMillis();
			Map<Long, GridBase> gbMap = e.getValue();
			for (Entry<Long, GridBase> es : gbMap.entrySet()) {
				GridBase gb = es.getValue();
				long eci = gb.CELL_ID;
				long GRID_ID = gb.GRID_ID;
				float lon = gb.LON;
				float lat = gb.LAT;
				String CITY = gb.CITY;
				IssueCollectExport ice = new IssueCollectExport();
				ice.DATE = gb.DATE;
				ice.PROVINCE = gb.PROVINCE;
				ice.CITY = CITY;
				ice.GRID_ID = GRID_ID;
				ice.LON = lon;
				ice.LAT = lat;
				ice.AVGRSRP = (gb.C_MEAN_RSRP - 140f)+"";
				ice.C_SUM_COUNT = gb.C_SUM_COUNT;
				ice.C_SUM_POOR = gb.C_SUM_POOR;
				ice.C_COV_RATE = gb.C_SUM_POOR == 0 ? 0 : 1f - (float)gb.C_SUM_POOR / (float)gb.C_SUM_COUNT;
				ice.U_SUM_COUNT = gb.U_SUM_COUNT;
				ice.U_COV_RATE = gb.U_SUM_POOR == 0 ? 0 : 1f - (float)gb.U_SUM_POOR / (float)gb.U_SUM_COUNT;
				ice.T_SUM_COUNT = gb.T_SUM_COUNT;
				ice.T_COV_RATE = gb.T_SUM_POOR == 0 ? 0: 1f - (float)gb.T_SUM_POOR / (float)gb.T_SUM_COUNT;
				ice.SCENE_MAJOR = gb.SCENE_MAJOR;
				ice.SCENE_MINOR = gb.SCENE_MINOR;
				if(anaGridDataMap.get(CITY + SPLITER + GRID_ID)!=null){
					AnaGridData agd = anaGridDataMap.get(CITY + SPLITER + GRID_ID);
					ice.TOP1ECI = agd.TOP1ECI;
					ice.TOP1NAME = agd.TOP1NAME;
					ice.TOP1MRPOORNUM = agd.TOP1MRPOORNUM;
					ice.TOP1_POOR_RATE = agd.TOP1_POOR_RATE;
					ice.TOP1DIR = agd.TOP1DIR;
					ice.TOP1HEIGHT = agd.TOP1HEIGHT;
					ice.TOP1TILT = agd.TOP1TILT;
					ice.TOP1DIS = agd.TOP1DIS;
					ice.TOP1BAND = agd.TOP1BAND;
					ice.TOP2ECI = agd.TOP2ECI;
					ice.TOP2NAME = agd.TOP2NAME;
					ice.TOP2MRPOORNUM = agd.TOP2MRPOORNUM;
					ice.TOP2_POOR_RATE = agd.TOP2_POOR_RATE;
					ice.TOP2DIR = agd.TOP2DIR;
					ice.TOP2HEIGHT = agd.TOP2HEIGHT;
					ice.TOP2TILT = agd.TOP2TILT;
					ice.TOP2DIS = agd.TOP2DIS;
					ice.TOP2BAND = agd.TOP2BAND;
					ice.TOP3ECI = agd.TOP3ECI;
					ice.TOP3NAME = agd.TOP3NAME;
					ice.TOP3MRPOORNUM = agd.TOP3MRPOORNUM;
					ice.TOP3_POOR_RATE = agd.TOP3_POOR_RATE;
					ice.TOP3DIR = agd.TOP3DIR;
					ice.TOP3HEIGHT = agd.TOP3HEIGHT;
					ice.TOP3TILT = agd.TOP3TILT;
					ice.TOP3DIS = agd.TOP3DIS;
					ice.TOP3BAND = agd.TOP3BAND;
					ice.SUGGEST = agd.SUGGEST;
				}
				
				if(engdataMap.get(eci)!=null){
					ice.REGION = engdataMap.get(eci).region;
				}
				for(Entry<Long, EngineerData> et : engdataMap.entrySet()){
					EngineerData ed = et.getValue();
					if(Math.abs(ed.latitude - lat) < 0.05f
							&& Math.abs(ed.longitude - lon) < 0.05f ){
						float dis = getInstance(Double.valueOf(String.valueOf(lon)),Double.valueOf(String.valueOf(lat))
								,Double.valueOf(String.valueOf(ed.longitude)),Double.valueOf(String.valueOf(ed.latitude)));
						if(ed.inoutdoor!=null&&ed.inoutdoor.equals("室内")){
							if(ice.NEAR_INDOOR_DIS==0f || ice.NEAR_INDOOR_DIS>dis){
								ice.NEAR_INDOOR = ed.def_sitename_chinese;
								ice.NEAR_INDOOR_DIS = dis ;
							}
						}else{
							if(ed.band!=null&&(ed.band.equals("D")||ed.band.equals("E")||ed.band.equals("F"))){
								if(ice.NEAR_TDD_DIS==0f || ice.NEAR_TDD_DIS>dis){
									ice.NEAR_TDD_OUT = ed.def_sitename_chinese;
									ice.NEAR_TDD_DIS = dis ;
								}
							}else{
								if(ice.NEAR_FDD_DIS==0f || ice.NEAR_FDD_DIS>dis){
									ice.NEAR_FDD_OUT = ed.def_sitename_chinese;
									ice.NEAR_FDD_DIS = dis ;
								}
							}
						}
					}
				}
				timevalue += System.currentTimeMillis() - b;
				collectExporter.Export(ice);
			}
		}
		logger.info("web_grid_suggestunion Process use " + String.format("%.3f", 1e-3 * timevalue) + " sec");
		collectExporter.finish();
	}
	
	public static float getInstance(double src_longitude,
			double src_latitude, double dest_longitude, double dest_latitude) {
		double earth_padius = 6378137.0;
		double radLat1 = 3.141592625 * src_latitude / 180;
		double radLat2 = 3.141592625 * dest_latitude / 180;
		double a = radLat1 - radLat2;
		double b = 3.141592625 * src_longitude / 180 - 3.141592625
				* dest_longitude / 180;
		double s1 = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		double s2 = s1 * earth_padius;
		DecimalFormat format = new DecimalFormat("0.00");
		return Float.parseFloat(format.format(s2));
	}

	private String SummaryConclusion(byte dimensionflag, byte type, HashMap<String, Long> gridInfos,
			HashMap<String, Object> cellInfos) {
		String conclusion = "";
		if (gridInfos == null || gridInfos.size() == 0) {
			return "";
		}
		int gridNum = gridInfos.size();
		if (gridNum == 0) {
			return "";
		}
		switch (type) {
		case ALARM: {
			HashSet<String> ecis = new HashSet<String>();
			int alarmAll = 0, alarmClose = 0, alarmLive = 0;
			for (Entry<String, Object> d : cellInfos.entrySet()) {
				String eci = d.getKey().split(SPLITER)[0];
				AlarmList al = (AlarmList) d.getValue();
				ecis.add(eci);
				alarmAll++;
				if (al.CLRINSERT_TIME != null && al.CLRINSERT_TIME.length() > 0
						&& !al.CLRINSERT_TIME.equals("00000000 00:00:00")) {
					alarmClose += 1;
				} else {
					alarmLive += 1;
				}
				if (dimensionflag == SCENE_FLAG) {
					sceneAlarmExporter.Export(al.sceneToString());
				}
				if (dimensionflag == CITY_FLAG) {
					cityAlarmExporter.Export(al);
				}
			}
			conclusion += "共有" + gridNum + "个弱覆盖栅格周边小区存在告警，" + "共计有" + ecis.size() + "个小区共产生影响覆盖类告警" + alarmAll + "条，"
					+ "已恢复" + alarmClose + "条，需处理" + alarmLive + "条。";
		}
			break;
		case MISSNEI: {
			HashSet<String> ecis = new HashSet<String>();
			int misspair = 0;
			for (Entry<String, Object> d : cellInfos.entrySet()) {
				String eci = d.getKey().split(SPLITER)[0];
				MissNeighbor mn = (MissNeighbor) d.getValue();
				ecis.add(eci);
				misspair += 1;
				if (dimensionflag == SCENE_FLAG) {
					sceneMissExporter.Export(mn.sceneToString());
				}
				if (dimensionflag == CITY_FLAG) {
					cityMissExporter.Export(mn);
				}
			}
			conclusion += "共有" + gridNum + "个弱覆盖栅格   邻区漏配，" + "共有" + ecis.size() + "个小区存在邻区漏配现象，" + "建议添加" + misspair
					+ "条漏配邻区数据。";
		}
			break;
		case ANTENNA: {
			for (Entry<String, Object> d : cellInfos.entrySet()) {
				GridIssueanaAntenna gia = (GridIssueanaAntenna) d.getValue();
				if (dimensionflag == SCENE_FLAG) {
					sceneTKExporter.Export(gia.sceneToString());
				}
				if (dimensionflag == CITY_FLAG) {
					cityTKExporter.Export(gia);
				}
			}
			conclusion += "共有" + gridNum + "个弱覆盖栅格周边" + cellInfos.size() + "个小区疑是存在天馈老化问题，" + "需派单到现场查勘，查看天线方位角与天线倾角合理，"
					+ "是否存在明显障碍物阻挡，如果没有明显障碍物阻挡，判断为天馈老化。";
		}
			break;
		case ANGLE: {
			for (Entry<String, Object> d : cellInfos.entrySet()) {
				GridIssueanaAngle gia = (GridIssueanaAngle) d.getValue();
				if (dimensionflag == SCENE_FLAG) {
					// 仿真数据
					Map<Long, GridBase> gbmap = gridBaseMap.get(gia.CITY + SPLITER + gia.GRID_ID);
					if (gbmap != null && gbmap.size() > 0) {
						GridBase gb = null;
						for (GridBase a : gbmap.values()) {
							if (a != null) {
								gb = a;
								break;
							}
						}
						SceneTask gt = new SceneTask();
						gt.DATE = gia.DATE;
						gt.CITY = gia.CITY;
						gt.SCENE_MAJOR = gia.SCENE_MAJOR;
						gt.SCENE_MINOR = gia.SCENE_MINOR;
						gt.GRID_ID = gia.GRID_ID;
						gt.LON = gb.LON;
						gt.LAT = gb.LAT;
						gt.ECI = gia.ECI;
						gt.DEF_CELLNAME_CHINESE = gia.CELL_NAME;
						gt.SUGGEST = "ANGLE";
						gt.ORIGINAL = (int) gia.S_ANGLE;
						gt.TARGET = (int) gia.ANGLE;
						gt.IS_SIMUL = "no";
						sceneTaskExporter.Export(gt);
					}
					sceneAoaExporter.Export(gia.sceneToString());
				}
				if (dimensionflag == CITY_FLAG) {
					cityAoaExporter.Export(gia);
				}
			}
			conclusion += "经AOA计算共有" + gridNum + "个弱覆盖栅格周边" + cellInfos.size() + "个小区天线方位角需要调整。";
		}
			break;
		case OVERCOVER: {
			int changecount = 0;
			for (Entry<String, Object> d : cellInfos.entrySet()) {
				GridIssueanaOvercover gio = (GridIssueanaOvercover) d.getValue();
				if (gio.D_DIR != -1) {
					changecount++;
				}
				if (dimensionflag == SCENE_FLAG) {
					// 仿真数据
					Map<Long, GridBase> gbmap = gridBaseMap.get(gio.CITY + SPLITER + gio.GRID_ID);
					if (gbmap != null && gbmap.size() > 0) {
						GridBase gb = null;
						for (GridBase a : gbmap.values()) {
							if (a != null) {
								gb = a;
								break;
							}
						}
						SceneTask gt = new SceneTask();
						gt.DATE = gio.DATE;
						gt.CITY = gio.CITY;
						gt.SCENE_MAJOR = gio.SCENE_MAJOR;
						gt.SCENE_MINOR = gio.SCENE_MINOR;
						gt.GRID_ID = gio.GRID_ID;
						gt.LON = gb.LON;
						gt.LAT = gb.LAT;
						gt.ECI = gio.ECI;
						gt.DEF_CELLNAME_CHINESE = gio.CELL_NAME;
						gt.SUGGEST = "DIR";
						gt.ORIGINAL = (int) gio.S_DIR;
						gt.TARGET = (int) gio.D_DIR;
						gt.IS_SIMUL = "no";
						sceneTaskExporter.Export(gt);
					}
					sceneOvercoverExporter.Export(gio.sceneToString());
				}
				if (dimensionflag == CITY_FLAG) {
					cityOvercoverExporter.Export(gio);
				}
			}
			conclusion += "共有" + gridNum + "个弱覆盖栅格周边" + cellInfos.size() + "个小区存在越区覆盖现象，" + "其中" + changecount
					+ "个小区可进行倾角调整。";
		}
			break;
		case OVERDISTANCE: {
			for (Entry<String, Object> d : cellInfos.entrySet()) {
				GridIssueanaOverdistance gio = (GridIssueanaOverdistance) d.getValue();
				if (dimensionflag == SCENE_FLAG) {
					// 仿真数据
					Map<Long, GridBase> gbmap = gridBaseMap.get(gio.CITY + SPLITER + gio.GRID_ID);
					if (gbmap != null && gbmap.size() > 0) {
						GridBase gb = null;
						for (GridBase a : gbmap.values()) {
							if (a != null) {
								gb = a;
								break;
							}
						}
						SceneTask gt = new SceneTask();
						gt.DATE = gio.DATE;
						gt.CITY = gio.CITY;
						gt.SCENE_MAJOR = gio.SCENE_MAJOR;
						gt.SCENE_MINOR = gio.SCENE_MINOR;
						gt.GRID_ID = gio.GRID_ID;
						gt.LON = gb.LON;
						gt.LAT = gb.LAT;
						gt.ECI = gio.ECI;
						gt.DEF_CELLNAME_CHINESE = gio.CELL_NAME;
						gt.SUGGEST = "ADD_SITE";
						gt.ORIGINAL = -1;
						gt.TARGET = -1;
						gt.IS_SIMUL = "no";
						sceneTaskExporter.Export(gt);
					}
					sceneOverdistanceExporter.Export(gio.sceneToString());
				}
				if (dimensionflag == CITY_FLAG) {
					cityOverdistanceExporter.Export(gio);
				}
			}
			conclusion += "共有" + gridNum + "个弱覆盖栅格初步判断为站间距过远。" + "需派单到现场查勘，如果现场反馈有需求（特殊地貌除外，如大水面、山区等），建议在该区域加站。";
		}
			break;
		case BACKBUILD: {
			for (Entry<String, Object> d : cellInfos.entrySet()) {
				GridIssueanaBackbuild gib = (GridIssueanaBackbuild) d.getValue();
				if (dimensionflag == SCENE_FLAG) {
					sceneBackbuildExporter.Export(gib.sceneToString());
				}
				if (dimensionflag == CITY_FLAG) {
					cityBackbuildExporter.Export(gib);
				}
			}
			conclusion += "共有" + gridNum + "个弱覆盖栅格周边" + cellInfos.size() + "个小区疑是存在建筑物阻挡问题，" + "需派单到现场查勘，查看天线方位角与天线倾角合理，"
					+ "是否存在明显障碍物阻挡，如果存在明显障碍物阻挡，判断为建筑物阻挡。";
		}
			break;
		case NOBESTCELL: {
			conclusion += "";
		}
			break;
		}
		return dimensionflag == SCENE_FLAG ? "该场景" + conclusion : "该地市" + conclusion;
	}

	// 站间距过远分析结论
	private String GetOverDistanceConclusion(GridResult g, GridInfo gi, IssueGridSuggestCount gsc) {

		String c = "";
		if (gi.MRNUM == 0)
			return c;

		// 条件一：属于弱覆盖栅格：栅格覆盖率 < 0.8
		Map<Long, GridBase> gbmap = gridBaseMap.get(g.CITY + SPLITER + g.GRID_ID);
		if (gbmap == null) {
			return c;
		}
		int sumpoor = 0, sumcount = 0;
		for (GridBase gb : gbmap.values()) {
			sumpoor += gb.C_SUM_POOR;
			sumcount += gb.C_SUM_COUNT;
		}
		if (sumcount == 0) {
			return c;
		}
		float coverRate = 1 - (float) sumpoor / sumcount;
		if (coverRate >= 0.8) {
			return c;
		}

		/*
		 * 条件二：距离最近站点的距离/周边站点的平均站间距 > 1.5 距离最近站点的距离：分析该栅格的弱覆盖TOP小区，计算该小区经纬度距离该栅格的距离， 入库表
		 * ana_grid_data中TOP1DIS、TOP2DIS、TOP3DIS、TOPECIDIS字段中取最小值； 平均站间距：小区到所有邻区的距离平均值
		 * 入库表 web_engineerdata中site_distance字段
		 */
		float mindis = Math.min(gi.TOP1DIS, gi.TOP2DIS);
		mindis = Math.min(mindis, Math.min(gi.TOP3DIS, gi.TOPECIDIS));
		long targetEci = 0;
		if (mindis == gi.TOP1DIS) {
			targetEci = gi.TOP1ECI;
		} else if (mindis == gi.TOP2DIS) {
			targetEci = gi.TOP2ECI;
		} else if (mindis == gi.TOP3DIS) {
			targetEci = gi.TOP3ECI;
		} else {
			targetEci = gi.TOPECIALLNUMECI;
		}
		EngineerData ed = engdataMap.get(targetEci);
		if (ed == null || ed.site_distance == 0f 
				|| mindis / (ed.site_distance * 1000) <= 1.5 || !ed.inoutdoor.trim().equals("室外")) {
			return c;
		}
		GridLineList gll = gridLineMap.get(g.GRID_ID + SPLITER + targetEci);
		if(gll != null && gll.CITY.equals(g.CITY) && gll.TOPECILIST != null && gll.TOPECILIST.trim().length() > 0)
		{
			return c;
		}
	
		/*
		 * 条件三： 栅格内日均业务量 > 100
		 * 栅格内日均业务量计算方法：统计TOP前3小区的业务量求和，并将TOP前3小区的采样点和栅格总采样点做比例进行加权（会有一定误差）： 栅格日均业务量 =
		 * 栅格内前TOP3小区业务量 / （TOP3前小区栅格内总采样点 / 栅格总采样点）
		 */
		HashMap<Integer, Long> eciMap = new HashMap<Integer, Long>();
		if (gi.TOP1ECI != 0) {
			eciMap.put(1, gi.TOP1ECI);
		}
		if (gi.TOP2ECI != 0) {
			eciMap.put(2, gi.TOP2ECI);
		}
		if (gi.TOP3ECI != 0) {
			eciMap.put(3, gi.TOP3ECI);
		}
		
		int weakcoverCount = 0;
		double topYwl = 0;
		long topCyd = 0;
		long totalCyd = gi.MRNUM;
		for (Entry<Integer, Long> it : eciMap.entrySet()) {
			CellInfo cl = cellMap.get(it.getValue());
			if (cl == null)
				continue;
			double zywl = (cl.PDCP_UpOctDl + cl.PDCP_UpOctUl) / 1024;

			CellUnit cu = cellUnitMap.get(it.getValue());
			if (cu == null || cu.MRNUMALL == 0)
				continue;

			int cydInGrid = 0;
			if (it.getKey() == 1) {
				cydInGrid = gi.TOP1MRNUM;
			} else if (it.getKey() == 2) {
				cydInGrid = gi.TOP2MRNUM;
			} else {
				cydInGrid = gi.TOP3MRNUM;
			}

			topCyd += cydInGrid;
			double ywl = zywl * ((double) cydInGrid / cu.MRNUMALL);
			topYwl += ywl;
			
			//分别计算top前3小区是否覆盖率均小于80%
			MrsStatRsrp msr = mrsStatRsrpMap.get(it.getValue());
			if(msr == null || msr.reportnum == 0) continue;
			int sum = 0;
			for (int i = 0; i <= 6; i++) {
				sum += msr.mr_rsrp[i];
			}
			float value = (float) sum / msr.reportnum;
			if(value > 0.2) weakcoverCount++;	
		}
		if (topCyd == 0)
			return c;
		double dayYwl = topYwl / ((double)topCyd / totalCyd);
		if (dayYwl <= 100)
			return c;
		
		
		c = "该栅格覆盖率为" + coverRate * 100 + "%，属于弱覆盖栅格；距离最近站点的距离为" + mindis + "米，大于周边站点的平均站间距1.5倍；" + "该栅格内日均业务量为"
				+ (long) dayYwl + "MB，大于100 MB" 
				+ (weakcoverCount >= 3 ? "；需派单到现场查勘，如果现场反馈确实有需求（特殊地貌除外，如大水面、山区等），判断为站间距过远，建议在该区域加站。" : "。");
		// 组织输出字段
		GridIssueanaOverdistance gio = new GridIssueanaOverdistance();
		gio.DATE = g.DATE;
		gio.CITY = g.CITY;
		gio.SCENE_MAJOR = g.SCENE_MAJOR;
		gio.SCENE_MINOR = g.SCENE_MINOR;
		gio.ECI = targetEci;
		gio.GRID_ID = g.GRID_ID;
		gio.CELL_NAME = ed.def_cellname_chinese;
		gio.COVERRATE = coverRate;
		gio.SITE_DISTANCE = ed.site_distance * 1000;
		gio.OVERDISTANCE = "初步判断为站间距过远。需派单到现场查勘，如果现场反馈有需求（特殊地貌除外，如大水面、山区等），建议在该区域加站。";
		gridOverdistanceExporter.Export(gio);

		// 仿真数据
		if (gbmap.size() > 0) {
			GridBase gb = null;
			for (GridBase a : gbmap.values()) {
				if (a != null) {
					gb = a;
					break;
				}
			}
			GridTask gt = new GridTask();
			gt.DATE = g.DATE;
			gt.CITY = g.CITY;
			gt.GRID_ID = g.GRID_ID;
			gt.LON = gb.LON;
			gt.LAT = gb.LAT;
			gt.ECI = targetEci;
			gt.DEF_CELLNAME_CHINESE = ed.def_cellname_chinese;
			gt.SUGGEST = "ADD_SITE";
			gt.ORIGINAL = -1;
			gt.TARGET = -1;
			gt.IS_SIMUL = "no";
			gridTaskExporter.Export(gt);
		}

		// 场景级汇总
		SetDimensionOverdistance(
				g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER + g.SCENE_MINOR,
				gio, SCENE_FLAG);
		// 地市级汇总
		SetDimensionOverdistance(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, gio, CITY_FLAG);

		// 栅格场景汇总
		SetDimensionGrid(
				g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER + g.SCENE_MINOR,
				g.CITY + SPLITER + g.GRID_ID, OVERDISTANCE, SCENE_FLAG);
		// 栅格城市汇总
		SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, g.CITY + SPLITER + g.GRID_ID, OVERDISTANCE,
				CITY_FLAG);
		gsc.OVERDISTANCE = "共有1个栅格疑是站间距过远，需现场查勘确认。";

		return c;
	}

	// 设置场景级别或地市级别站间距过远信息,flag 0为场景级别，1为地市级别
	private void SetDimensionOverdistance(String dimension, GridIssueanaOverdistance gio, byte flag) {
		HashMap<Byte, HashMap<String, Object>> summayMap = flag == SCENE_FLAG ? sceneSummaryMap.get(dimension)
				: citySummaryMap.get(dimension);
		if (summayMap == null) {
			summayMap = new HashMap<Byte, HashMap<String, Object>>();
			if (flag == SCENE_FLAG) {
				sceneSummaryMap.put(dimension, summayMap);
			} else {
				citySummaryMap.put(dimension, summayMap);
			}
		}
		HashMap<String, Object> infoMap = summayMap.get(OVERDISTANCE);
		if (infoMap == null) {
			infoMap = new HashMap<String, Object>();
			summayMap.put(OVERDISTANCE, infoMap);
		}
		String key = gio.ECI + "";
		Object o = infoMap.get(key);
		if (o == null) {
			o = gio;
			infoMap.put(key, o);

		}
	}

	// 建筑物遮挡结论
	private String GetBackBuildConclusion(GridResult g, GridInfo gi, IssueGridSuggestCount gsc) {
		String c = "";
		if (g.SIMUlCOVER != 0) {
			return c;
		}
		HashSet<Long> eciList = new HashSet<Long>();
		if (gi.TOP1ECI != 0 && gi.TOP1DIS <= 960) {
			eciList.add(gi.TOP1ECI);
		}
		if (gi.TOP2ECI != 0 && gi.TOP2DIS <= 960) {
			eciList.add(gi.TOP2ECI);
		}
		if (gi.TOP3ECI != 0 && gi.TOP3DIS <= 960) {
			eciList.add(gi.TOP3ECI);
		}
		if (gi.TOPECIALLNUMECI != 0 && gi.TOPECIDIS <= 960) {
			eciList.add(gi.TOPECIALLNUMECI);
		}
		ArrayList<GridIssueanaBackbuild> list = new ArrayList<GridIssueanaBackbuild>();
		String cellnames = "";
		for (long eci : eciList) {
			EngineerData ed = engdataMap.get(eci);
			if(ed == null || !ed.inoutdoor.trim().equals("室外")) {
				continue;
			}
			CellUnit cu = cellUnitMap.get(eci);
			if (cu == null || cu.GRIDNUM == 0) {
				continue;
			}
			if ((float) cu.POORGRIDNMUM / cu.GRIDNUM < 0.4) {
				continue;
			}

			CheckParaList cpl = checkParaListMap.get(eci);
			if (cpl == null 
					|| (cpl.param_name != null && 
					(cpl.param_name.toLowerCase().equals("pa") 
							|| cpl.param_name.toLowerCase().equals("pb") 
							|| cpl.param_name.toLowerCase().equals("referencesignalpower")))) {
				cellnames += cellnames.length() == 0 ? ed.def_cellname_chinese : "、" + ed.def_cellname_chinese;
				// 栅格建筑物遮挡详情详情输出
				// 组织输出字段
				GridIssueanaBackbuild gib = new GridIssueanaBackbuild();
				gib.DATE = g.DATE;
				gib.CITY = g.CITY;
				gib.SCENE_MAJOR = g.SCENE_MAJOR;
				gib.SCENE_MINOR = g.SCENE_MINOR;
				gib.ECI = eci;
				gib.GRID_ID = g.GRID_ID;
				gib.CELL_NAME = ed.def_cellname_chinese;
				gib.BACKBUILD = "需派单到现场查勘，确定天线方位角与天线倾角合理，是否存在明显障碍物阻挡，如果存在明显障碍物阻挡，判断为建筑物阻挡。";
				list.add(gib);
			}
		}

		if (cellnames.length() > 0) {
			for (GridIssueanaBackbuild gib : list) {
				gridBackbuildExporter.Export(gib);
				// 场景级汇总
				SetDimensionBackBuild(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR
						+ SPLITER + g.SCENE_MINOR, gib, SCENE_FLAG);
				// 地市级汇总
				SetDimensionBackBuild(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, gib, CITY_FLAG);
			}
			c = "仿真数据在该区域没有出现弱覆盖，但小区" + cellnames + "出现弱覆盖均分布在小区覆盖边缘，小区发射功率正常处于合理范围。需派单到现场查勘，"
					+ "确定天线方位角与天线倾角合理，是否存在明显障碍物阻挡，如果存在明显障碍物阻挡，判断为建筑物阻挡。";
			gsc.BACKBUILD = "共有" + list.size() + "个小区疑是建筑物阻挡，需现场查勘确认。";
			SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER
					+ g.SCENE_MINOR, g.CITY + SPLITER + g.GRID_ID, BACKBUILD, SCENE_FLAG);
			SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, g.CITY + SPLITER + g.GRID_ID, BACKBUILD,
					CITY_FLAG);
		}
		return c;
	}

	// 设置场景级别或地市级别建筑物遮挡信息,flag 0为场景级别，1为地市级别
	private void SetDimensionBackBuild(String dimension, GridIssueanaBackbuild gib, byte flag) {
		HashMap<Byte, HashMap<String, Object>> summayMap = flag == SCENE_FLAG ? sceneSummaryMap.get(dimension)
				: citySummaryMap.get(dimension);
		if (summayMap == null) {
			summayMap = new HashMap<Byte, HashMap<String, Object>>();
			if (flag == SCENE_FLAG) {
				sceneSummaryMap.put(dimension, summayMap);
			} else {
				citySummaryMap.put(dimension, summayMap);
			}
		}
		HashMap<String, Object> infoMap = summayMap.get(BACKBUILD);
		if (infoMap == null) {
			infoMap = new HashMap<String, Object>();
			summayMap.put(BACKBUILD, infoMap);
		}
		String key = gib.ECI + "";
		Object o = infoMap.get(key);
		if (o == null) {
			o = gib;
			infoMap.put(key, o);
			// o.count[0] = 1;// 天馈问题小区
		}
	}

	// 未占用最优小区
	private String GetNoBestCellConclusion(GridResult g, GridInfo gi, IssueGridSuggestCount gsc) {
		String c = "";
		if (g.SIMUlCOVER != 0) {
			gsc.NOBESTCELL = "";
			return c;
		}
		HashSet<Long> eciList = new HashSet<Long>();
		if (gi.TOP1ECI != 0) {
			eciList.add(gi.TOP1ECI);
		}
		if (gi.TOP2ECI != 0) {
			eciList.add(gi.TOP2ECI);
		}
		if (gi.TOP3ECI != 0) {
			eciList.add(gi.TOP3ECI);
		}
//		if (gi.TOPECIALLNUMECI != 0 && gi.TOPECIDIS <= 960) {
//			eciList.add(gi.TOPECIALLNUMECI);
//		}
		ArrayList<GridIssueanaNobestcell> list = new ArrayList<GridIssueanaNobestcell>();
		String cellnames = "";
		for (long eci : eciList) {
			EngineerData ed = engdataMap.get(eci);
//			if(ed == null || !ed.inoutdoor.trim().equals("室外")) {
//				continue;
//			}
//			
//			CellUnit cu = cellUnitMap.get(eci);
//			if (cu == null || cu.GRIDNUM == 0) {
//				continue;
//			}
//			if ((float) cu.POORGRIDNMUM / cu.GRIDNUM < 0.4) {
//				continue;
//			}

			CheckParaList cpl = checkParaListMap.get(eci);
			if (cpl != null 
					&& (cpl.param_name != null && 
					(cpl.param_name.toLowerCase().equals("pa") 
							|| cpl.param_name.toLowerCase().equals("pb")))) {
				cellnames += cellnames.length() == 0 ? ed.def_cellname_chinese : "、" + ed.def_cellname_chinese;
				GridIssueanaNobestcell gin = new GridIssueanaNobestcell();
				gin.DATE = g.DATE;
				gin.CITY = g.CITY;
				gin.ECI = eci;
				gin.param_name = cpl.param_name;
				gin.vendor_name = cpl.vendor_name;
				gin.real_value = cpl.real_value;
				gin.plan_value = cpl.plan_value;
				gin.GRID_ID = g.GRID_ID;
				gin.CELL_NAME = ed.def_cellname_chinese;
				gin.NOBESTCELL = "将现网小区数据与设置规则数据相对比，发现小区"+ed.def_cellname_chinese+"的"+cpl.param_name+"参数配置不合理，原始设置值为"+cpl.real_value+"，建议设置值"+cpl.plan_value;
				list.add(gin);
			}
			
			CellInfo ci = cellMap.get(eci);
			if(ci !=null && !Helper.isEmpty(ci.ReferenceSignalPower)){
				
				int plan_value = 0 ;
				String cell_type = "";
				int rsPower = Integer.parseInt(ci.ReferenceSignalPower);
				String lowCell = "";
				boolean flag = false;
				
				if("D".equals(ed.band.trim()) || "E".equals(ed.band.trim()) || "F".equals(ed.band.trim())){
					plan_value = rsPower + 3 > 12 ? 12 : rsPower + 3;
					cell_type = "TDD";
					if("D".equals(ed.band.trim()))
						flag = true;
				}else{
					plan_value = rsPower + 3 > 15 ? 15 : rsPower + 3;
					cell_type = "FDD";
				}
				
				if((cell_type.equals("TDD") && rsPower < 12) || (cell_type.equals("FDD") && rsPower < 15)){
					cellnames += cellnames.length() == 0 ? ed.def_cellname_chinese : "、" + ed.def_cellname_chinese;
					GridIssueanaNobestcell gin = new GridIssueanaNobestcell();
					gin.DATE = g.DATE;
					gin.CITY = g.CITY;
					gin.ECI = eci;
					gin.param_name = "ReferenceSignalPower";
	//				gin.vendor_name = ed.def_vendor;
					gin.real_value = ci.ReferenceSignalPower;
					gin.plan_value = "" + plan_value;
					gin.cell_type = cell_type;
					gin.GRID_ID = g.GRID_ID;
					gin.CELL_NAME = ed.def_cellname_chinese;
					gin.NOBESTCELL = "将现网小区数据与设置规则数据相对比，发现小区"+ed.def_cellname_chinese+"为"+cell_type+"小区，其小区功率为"+ci.ReferenceSignalPower
							+"，该小区仍然有功率提升空间，建议将小区功率提升至"+plan_value;
					
					if(flag){
						for(Entry<Long, EngineerData> et : engdataMap.entrySet()){
							EngineerData ed1 = et.getValue();
							if(Math.abs(ed.latitude - ed1.latitude) < 0.015f
									&& Math.abs(ed.longitude - ed1.longitude) < 0.015f 
									&& !"D".equals(ed1.band.trim()) && !"E".equals(ed1.band.trim()) ){
								float dis = getInstance(Double.valueOf(String.valueOf(ed.longitude)),Double.valueOf(String.valueOf(ed.latitude))
										,Double.valueOf(String.valueOf(ed1.longitude)),Double.valueOf(String.valueOf(ed1.latitude)));
								
								if(dis < 100f){
									lowCell += lowCell.length() == 0 ? ed1.def_cellname_chinese : "、" + ed1.def_cellname_chinese;
								}
								
							}
						}
						gin.NOBESTCELL += "\r\n该栅格TOP小区" +ed.def_cellname_chinese + "为D频段小区，其100米范围内存在低频段小区" + lowCell + "，建议用" + lowCell + "小区吸收话务。";
					}	
					
					list.add(gin);
				}
			}
		}

		if (cellnames.length() > 0) {
			c = "将现网小区数据与设置规则数据相对比,";
			for (GridIssueanaNobestcell gin : list) {
				gridNoBestCellExporter.Export(gin);
				// 场景级汇总
				SetDimensionNoBestCell(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER
						+ g.SCENE_MINOR, gin, SCENE_FLAG);
				// 地市级汇总
				SetDimensionNoBestCell(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, gin, CITY_FLAG);
			
				if(gin.param_name.equals("ReferenceSignalPower")){
					c += "发现小区"+gin.CELL_NAME+"为"+gin.cell_type+"小区，其小区功率为"+gin.real_value+"，该小区仍然有功率提升空间，建议将小区功率提升至"+gin.plan_value;
				}else{
					c += "发现小区"+gin.CELL_NAME+"的"+gin.param_name+"参数配置不合理，原始设置值为"+gin.real_value+"，建议设置值"+gin.plan_value;
				}
			}
			
			gsc.NOBESTCELL = "共有"+list.size()+"个小区参数设置不合理，建议调整；";
			SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER
					+ g.SCENE_MINOR, g.CITY + SPLITER + g.GRID_ID, NOBESTCELL, SCENE_FLAG);
			SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, g.CITY + SPLITER + g.GRID_ID, NOBESTCELL,
					CITY_FLAG);
		}
		return c;
	}

	// 设置场景级别或地市级别未占用最优小区信息,flag 0为场景级别，1为地市级别
	private void SetDimensionNoBestCell(String dimension, Object notbestcell, byte flag) {
		HashMap<Byte, HashMap<String, Object>> summayMap = flag == SCENE_FLAG ? sceneSummaryMap.get(dimension)
				: citySummaryMap.get(dimension);
		if (summayMap == null) {
			summayMap = new HashMap<Byte, HashMap<String, Object>>();
			if (flag == SCENE_FLAG) {
				sceneSummaryMap.put(dimension, summayMap);
			} else {
				citySummaryMap.put(dimension, summayMap);
			}
		}
		HashMap<String, Object> infoMap = summayMap.get(NOBESTCELL);
		if (infoMap == null) {
			infoMap = new HashMap<String, Object>();
			summayMap.put(NOBESTCELL, infoMap);
		}
		String key = /* notbestcell.eci + */"";
		Object o = infoMap.get(key);
		if (o == null) {
			o = notbestcell;
			infoMap.put(key, o);
			// o.count[0] = 1;// 未占用最优小区
		}
	}

	// 天线方向角分析结论
	private String GetAOAConclusion(GridResult g, int angle, int flag, EngineerData ed, IssueGridSuggestCount gsc) {
		String c = "";
		if (ed == null)
			return c;
		long eci = 0;
		if (flag == 1)
			eci = g.TOP1ECI;
		else if (flag == 2)
			eci = g.TOP2ECI;
		else if (flag == 3)
			eci = g.TOP3ECI;
		if (eci == 0 || ed == null || !ed.inoutdoor.trim().equals("室外")) {
			return c;
		}

		MrsAoaCover mac = mrsAoaCover60Map.get(eci);
		if (mac == null)
			return c;
		// 条件1
		if (mac.aoa[0] >= 0.7)
			return c;
		// 条件2
		angle = angle > 180 ? angle - 360 : angle;
		if (angle * mac.avgaoa < 0)
			return c;
		boolean changeflag = false;
		float minAngle = 0;
		String opposite = "";
		float min = Math.min(angle, mac.avgaoa);
		float max = Math.max(angle, mac.avgaoa);
		if (angle > 0 && mac.avgaoa > 0 && min > 10 && Math.abs(angle-mac.avgaoa)<=60) {
			changeflag = true;
//			minAngle = min + ed.dir < 360 ? min + ed.dir : min + ed.dir - 360;
			minAngle = min ;
			opposite = "顺时针";
		}
		if (angle < 0 && mac.avgaoa < 0 && max < -10 && Math.abs(angle-mac.avgaoa)<=60) {
			changeflag = true;
//			minAngle = max + ed.dir > 0 ? max + ed.dir : max + ed.dir + 360;
			minAngle = max ;
			opposite = "逆时针";
		}
		if (changeflag == false)
			return c;
		
		float newaoa = mac.avgaoa;
//		float newaoa = 0;
//		if (ed.dir + mac.avgaoa >= 360) {
//			newaoa = ed.dir + mac.avgaoa - 360;
//		} else if (ed.dir + mac.avgaoa < 0) {
//			newaoa = ed.dir + mac.avgaoa + 360;
//		} else {
//			newaoa = ed.dir + mac.avgaoa;
//		}
		

		c = "TOP" + flag + "天线方向角结论：@" + "经AOA计算，小区 " + ed.def_cellname_chinese + " 天线到达角为(相对实际角度)"
				+ (int) (newaoa) + "度。" + "该小区原工参天线方向角" + ed.dir + "度，" + "AOA（-30度，+30度）采样点占比为"
				+ mac.aoa[0] * 100 + "% ，小于70%，" + "根据分析结果，建议该小区天线方位角在实际角度上" + opposite + "调整" + Math.abs((int) minAngle)+ "度。";

		// 栅格aoa详情详情输出
		// 组织输出字段
		GridIssueanaAngle gia = new GridIssueanaAngle();
		gia.DATE = g.DATE;
		gia.CITY = g.CITY;
		gia.SCENE_MAJOR = g.SCENE_MAJOR;
		gia.SCENE_MINOR = g.SCENE_MINOR;
		gia.ECI = eci;
		gia.GRID_ID = g.GRID_ID;
		gia.CELL_NAME = ed.def_cellname_chinese;
		gia.S_ANGLE = ed.dir;
		gia.avgaoa = (int) (newaoa);
		gia.ANGLE = (int) minAngle;
		gridAoaExporter.Export(gia);

		// 仿真数据
		Map<Long, GridBase> gbmap = gridBaseMap.get(gia.CITY + SPLITER + gia.GRID_ID);
		if (gbmap != null && gbmap.size() > 0) {
			GridBase gb = null;
			for (GridBase a : gbmap.values()) {
				if (a != null) {
					gb = a;
					break;
				}
			}
			GridTask gt = new GridTask();
			gt.DATE = g.DATE;
			gt.CITY = g.CITY;
			gt.GRID_ID = g.GRID_ID;
			gt.LON = gb.LON;
			gt.LAT = gb.LAT;
			gt.ECI = eci;
			gt.DEF_CELLNAME_CHINESE = ed.def_cellname_chinese;
			gt.SUGGEST = "ANGLE";
			gt.ORIGINAL = ed.dir;
			gt.TARGET = (int) minAngle;
			gt.IS_SIMUL = "no";
			gridTaskExporter.Export(gt);
		}

		// 场景级汇总
		SetDimensionAoa(
				g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER + g.SCENE_MINOR,
				gia, SCENE_FLAG);
		// 地市级汇总
		SetDimensionAoa(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, gia, CITY_FLAG);
		// 栅格汇总
		SetDimensionGrid(
				g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER + g.SCENE_MINOR,
				g.CITY + SPLITER + g.GRID_ID, ANGLE, SCENE_FLAG);
		SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, g.CITY + SPLITER + g.GRID_ID, ANGLE,
				CITY_FLAG);

		gsc.ACount += 1;

		return c;
	}

	// 设置场景级别或地市级别方向角信息,flag 0为场景级别，1为地市级别
	private void SetDimensionAoa(String dimension, GridIssueanaAngle gia, byte flag) {
		HashMap<Byte, HashMap<String, Object>> summayMap = flag == SCENE_FLAG ? sceneSummaryMap.get(dimension)
				: citySummaryMap.get(dimension);
		if (summayMap == null) {
			summayMap = new HashMap<Byte, HashMap<String, Object>>();
			if (flag == SCENE_FLAG) {
				sceneSummaryMap.put(dimension, summayMap);
			} else {
				citySummaryMap.put(dimension, summayMap);
			}
		}
		HashMap<String, Object> infoMap = summayMap.get(ANGLE);
		if (infoMap == null) {
			infoMap = new HashMap<String, Object>();
			summayMap.put(ANGLE, infoMap);
		}
		String key = gia.ECI + "";
		Object o = infoMap.get(key);
		if (o == null) {
			o = gia;
			infoMap.put(key, o);
			// o.count[0] = 1;// 天馈问题小区
		}
	}

	// 小区越区覆盖分析结论
	private String GetOverCoverConclusion(GridResult g, int flag, GridInfo gi, GridLineList gll, EngineerData ed,
			IssueGridSuggestCount gsc) {
		String c = "";
		if (gll == null || gll.TOPECILIST == null || gll.TOPECILIST.length() == 0 
				|| gll.GRID_ID != g.GRID_ID || !gll.CITY.equals(g.CITY)
				|| ed == null|| ed.site_distance == 0f)
			return c;

		// 条件一：小区距离栅格直线距离 > 周边基站平均距离
		long eci = 0;
		float sitdis = ed.site_distance * 1000;
		String cellSite = ed.def_sitename;
		float linedis = 0f;
		if (flag == 1 && gi.TOP1DIS > 960) {
			linedis = gi.TOP1DIS;
			eci = g.TOP1ECI;
		} else if (flag == 2 && gi.TOP2DIS > 960) {
			linedis = gi.TOP2DIS;
			eci = g.TOP2ECI;
		} else if (flag == 3 && gi.TOP3DIS > 960) {
			linedis = gi.TOP3DIS;
			eci = g.TOP3ECI;
		}
		if (eci == 0 || linedis <= sitdis || !ed.inoutdoor.trim().equals("室外"))
			return c;
		// 条件二：小区方向角正负60度范围内距离栅格中间位置，存在其他基站
//		Set<String> sites = new HashSet<String>();
		int sitecount = 0;
		int outdoorcount = 0;
		String otherecis[] = gll.TOPECILIST.split(" ");
		for (String streci : otherecis) {
			streci = streci.trim();
			if (streci.length() == 0)
				continue;

			EngineerData oed = engdataMap.get(Long.parseLong(streci));
			if (oed != null && !oed.def_sitename.equals(cellSite)) {
//				sites.add(oed.def_sitename);
				sitecount++;
			}
			if(oed != null && oed.inoutdoor.trim().equals("室外")) {
				outdoorcount++;
			}
		}
//		if(sites.size() == 0)
//			return c;
		if (sitecount == 0 || outdoorcount == 0)
			return c;

//		String strsites = "";
//		for (String sitename : sites) {
//			strsites += strsites.length() == 0 ? sitename : "、" + sitename;
//		} 
		int ntilt = ed.tilt;
		int suggestTitl = (int) (Math.atan(ed.height / sitdis) * 57.29578) + 10 / 2;
		byte changeFlag = (byte) ((ntilt < suggestTitl && suggestTitl < 16 && suggestTitl - ntilt > 2) ? 1 : 0);
//		if(changeFlag == 0 && ntilt >= suggestTitl) {
//			if(ntilt > 10 && ntilt <= 13) {
//				changeFlag = 1;
//				suggestTitl = ntilt + 3;
//			}
//			if(ntilt > 5 && ntilt <= 10) {
//				changeFlag = 1;
//				suggestTitl = ntilt + 4;
//			}
//			if(ntilt <= 5) {
//				changeFlag = 1;
//				suggestTitl = ntilt + 5;
//			}
//		}
		// 组织输出字段
		GridIssueanaOvercover gio = new GridIssueanaOvercover();
		gio.DATE = g.DATE;
		gio.CITY = g.CITY;
		gio.SCENE_MAJOR = g.SCENE_MAJOR;
		gio.SCENE_MINOR = g.SCENE_MINOR;
		gio.ECI = eci;
		gio.GRID_ID = g.GRID_ID;
		gio.CELL_NAME = ed.def_cellname_chinese;
		gio.S_DIR = ntilt;
		if (changeFlag == 1)
			gio.D_DIR = suggestTitl;
		gio.OVERCOVER = changeFlag == 0 ? "疑是越区覆盖" : "疑是越区覆盖，建议调整为" + suggestTitl + "度。";
		gridOvercoverExporter.Export(gio);

		// 仿真数据
		Map<Long, GridBase> gbmap = gridBaseMap.get(gio.CITY + SPLITER + gio.GRID_ID);
		if (gbmap != null && gbmap.size() > 0) {
			GridBase gb = null;
			for (GridBase a : gbmap.values()) {
				if (a != null) {
					gb = a;
					break;
				}
			}
			GridTask gt = new GridTask();
			gt.DATE = g.DATE;
			gt.CITY = g.CITY;
			gt.GRID_ID = g.GRID_ID;
			gt.LON = gb.LON;
			gt.LAT = gb.LAT;
			gt.ECI = eci;
			gt.DEF_CELLNAME_CHINESE = ed.def_cellname_chinese;
			gt.SUGGEST = "DIR";
			gt.ORIGINAL = ntilt;
			gt.TARGET = changeFlag == 1 ? suggestTitl : -1;
			gt.IS_SIMUL = "no";
			gridTaskExporter.Export(gt);
		}
		// 场景级汇总
		SetDimensionOverCover(
				g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER + g.SCENE_MINOR,
				gio, SCENE_FLAG);
		// 地市级汇总
		SetDimensionOverCover(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, gio, CITY_FLAG);
		// 栅格汇总
		SetDimensionGrid(
				g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER + g.SCENE_MINOR,
				g.CITY + SPLITER + g.GRID_ID, OVERCOVER, SCENE_FLAG);
		SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, g.CITY + SPLITER + g.GRID_ID, OVERCOVER,
				CITY_FLAG);

		c = "top" + flag + "小区越区覆盖分析结论：@" + "小区 " + ed.def_cellname_chinese + " 距离栅格直线距离" + linedis + "米大于栅格周边基站平均距离"
				+ sitdis + "米，" + "该小区方向角正负60度范围内距离栅格中间位置，存在其他基站；" + "小区 " + ed.def_cellname_chinese + " 原天线下倾角" + ntilt
				+ "度，疑是越区覆盖" + (changeFlag == 1 ? "建议调整为" + suggestTitl + "度" : "") + "。";

		gsc.OCCount[0] += 1;
		if (changeFlag == 1)
			gsc.OCCount[1] += 1;

		return c;
	}

	// 设置场景级别或地市级别越区覆盖信息,flag 0为场景级别，1为地市级别
	private void SetDimensionOverCover(String dimension, GridIssueanaOvercover gio, byte flag) {
		HashMap<Byte, HashMap<String, Object>> summayMap = flag == SCENE_FLAG ? sceneSummaryMap.get(dimension)
				: citySummaryMap.get(dimension);
		if (summayMap == null) {
			summayMap = new HashMap<Byte, HashMap<String, Object>>();
			if (flag == SCENE_FLAG) {
				sceneSummaryMap.put(dimension, summayMap);
			} else {
				citySummaryMap.put(dimension, summayMap);
			}
		}
		HashMap<String, Object> infoMap = summayMap.get(OVERCOVER);
		if (infoMap == null) {
			infoMap = new HashMap<String, Object>();
			summayMap.put(OVERCOVER, infoMap);
		}
		String key = gio.ECI + "";
		Object o = infoMap.get(key);
		if (o == null) {
			o = gio;
			infoMap.put(key, o);
//			if (changeFlag == 1) {
//				o.count[0] = 0;// 可能存在越区覆盖
//				o.count[1] = 1;// 可进行倾角调整
//			} else {
//				o.count[0] = 1;// 可能存在越区覆盖
//				o.count[1] = 0;// 可进行倾角调整
//			}
			// 小区越覆盖
		} else {
			// 调整规则 不调整>调整度数较大>调整度数较小
			GridIssueanaOvercover last = (GridIssueanaOvercover) o;
			if (last.D_DIR == -1)
				return;
			if (gio.D_DIR == -1 || gio.D_DIR > last.D_DIR) {
				infoMap.remove(key);
				infoMap.put(key, gio);
				return;
			}
		}
	}

	// 天馈结论
	private String GetTKConclusion(GridResult g, GridInfo gi, IssueGridSuggestCount gsc) {
		String c = "";
		if (g.SIMUlCOVER != 0) {
			gsc.ANTENNA = "";
			return c;
		}
		HashSet<Long> eciList = new HashSet<Long>();
		if (gi.TOP1ECI != 0 && gi.TOP1DIS <= 960) {
			eciList.add(gi.TOP1ECI);
		}
		if (gi.TOP2ECI != 0 && gi.TOP2DIS <= 960) {
			eciList.add(gi.TOP2ECI);
		}
		if (gi.TOP3ECI != 0 && gi.TOP3DIS <= 960) {
			eciList.add(gi.TOP3ECI);
		}
		if (gi.TOPECIALLNUMECI != 0 && gi.TOPECIDIS <= 960) {
			eciList.add(gi.TOPECIALLNUMECI);
		}
		ArrayList<GridIssueanaAntenna> list = new ArrayList<GridIssueanaAntenna>();
		String cellnames = "";
		for (long eci : eciList) {
			EngineerData ed = engdataMap.get(eci);
			if(ed == null || !ed.inoutdoor.trim().equals("室外")) {
				continue;
			}
			
			CellUnit cu = cellUnitMap.get(eci);
			if (cu == null || cu.GRIDNUM == 0) {
				continue;
			}
			if ((float) cu.POORGRIDNMUM / cu.GRIDNUM < 0.4) {
				continue;
			}

			CheckParaList cpl = checkParaListMap.get(eci);
			if (cpl == null 
					|| (cpl.param_name != null && 
					(cpl.param_name.toLowerCase().equals("pa") 
							|| cpl.param_name.toLowerCase().equals("pb") 
							|| cpl.param_name.toLowerCase().equals("referencesignalpower")))) {
				cellnames += cellnames.length() == 0 ? ed.def_cellname_chinese : "、" + ed.def_cellname_chinese;
				GridIssueanaAntenna gia = new GridIssueanaAntenna();
				gia.DATE = g.DATE;
				gia.CITY = g.CITY;
				gia.ECI = eci;
				gia.SCENE_MAJOR = g.SCENE_MAJOR;
				gia.SCENE_MINOR = g.SCENE_MINOR;
				gia.GRID_ID = g.GRID_ID;
				gia.CELL_NAME = ed.def_cellname_chinese;
				gia.ANTENNA = "需派单到现场查勘，确定天线方位角与天线倾角合理，是否存在明显障碍物阻挡，如果没有明显障碍物阻挡，判断为天馈老化。";
				list.add(gia);
			}
		}

		if (cellnames.length() > 0) {
			for (GridIssueanaAntenna gia : list) {
				gridTKExporter.Export(gia);
				// 场景级汇总
				SetDimensionTK(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER
						+ g.SCENE_MINOR, gia, SCENE_FLAG);
				// 地市级汇总
				SetDimensionTK(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, gia, CITY_FLAG);
			}
			c = "仿真数据在该区域没有出现弱覆盖，但小区 " + cellnames + "出现弱覆盖均分布在小区覆盖边缘，小区发射功率正常处于合理范围。"
					+ "需派单到现场查勘，确定天线方位角与天线倾角合理，是否存在明显障碍物阻挡，" + "如果没有明显障碍物阻挡，判断为天馈老化。";
			gsc.ANTENNA = "共有" + list.size() + "个小区疑是天馈老化，需现场查勘确认。";
			SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER
					+ g.SCENE_MINOR, g.CITY + SPLITER + g.GRID_ID, ANTENNA, SCENE_FLAG);
			SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, g.CITY + SPLITER + g.GRID_ID, ANTENNA,
					CITY_FLAG);
		}
		return c;
	}

	// 设置场景级别或地市级别天馈信息,flag 0为场景级别，1为地市级别
	private void SetDimensionTK(String dimension, GridIssueanaAntenna gia, byte flag) {
		HashMap<Byte, HashMap<String, Object>> summayMap = flag == SCENE_FLAG ? sceneSummaryMap.get(dimension)
				: citySummaryMap.get(dimension);
		if (summayMap == null) {
			summayMap = new HashMap<Byte, HashMap<String, Object>>();
			if (flag == SCENE_FLAG) {
				sceneSummaryMap.put(dimension, summayMap);
			} else {
				citySummaryMap.put(dimension, summayMap);
			}
		}
		HashMap<String, Object> infoMap = summayMap.get(ANTENNA);
		if (infoMap == null) {
			infoMap = new HashMap<String, Object>();
			summayMap.put(ANTENNA, infoMap);
		}
		String key = gia.ECI + "";
		Object o = infoMap.get(key);
		if (o == null) {
			o = gia;
			infoMap.put(key, o);
			// o.count[0] = 1;// 天馈问题小区
		}
	}

	// 邻区漏配结论
	private String GetMissConclusion(GridResult g, GridInfo gi, IssueGridSuggestCount gsc) {
		String c = "";
		HashMap<Integer, Long> ecimap = new HashMap<Integer, Long>();
		HashMap<Long, Integer> eciindexmap = new HashMap<Long, Integer>();
		if (gi.TOP1ECI != 0) {
			ecimap.put(1, gi.TOP1ECI);
			eciindexmap.put(gi.TOP1ECI, 1);
		}
		if (gi.TOP2ECI != 0) {
			ecimap.put(2, gi.TOP2ECI);
			eciindexmap.put(gi.TOP2ECI, 2);
		}
		if (gi.TOP3ECI != 0) {
			ecimap.put(3, gi.TOP3ECI);
			eciindexmap.put(gi.TOP3ECI, 3);
		}
		if (gi.TOPECIALLNUMECI != 0) {
			if(!eciindexmap.containsKey(gi.TOPECIALLNUMECI)) {
				ecimap.put(4, gi.TOPECIALLNUMECI);
				eciindexmap.put(gi.TOPECIALLNUMECI, 4);
			}
		}
		int count[] = new int[ecimap.size()];
		int i = 0;
		for (Entry<Integer, Long> it : ecimap.entrySet()) {
			int mrnum = 0;
			int poormrnum = 0;
			if (it.getKey() == 1) {
				mrnum = gi.TOP1MRNUM;
				poormrnum = gi.TOP1MRPOORNUM;
			}
			if (it.getKey() == 2) {
				mrnum = gi.TOP2MRNUM;
				poormrnum = gi.TOP2MRPOORNUM;
			}
			if (it.getKey() == 3) {
				mrnum = gi.TOP3MRNUM;
				poormrnum = gi.TOP3MRPOORNUM;
			}
			if (it.getKey() == 4) {
				mrnum = gi.TOPECIALLNUM;
				poormrnum = gi.TOPECIPOORNUM;
			}
			if (mrnum == 0) {
				i++;
				continue;
			}
			List<MissNeighbor> mlist = missNeighborMap.get(it.getValue());
			if (mlist == null) {
				i++;
				continue;
			}
			boolean flag = false;
			for (MissNeighbor mn : mlist) {
				Integer indexnei = eciindexmap.get(mn.D_ECI);
				if (indexnei == null)
					continue;
				int neimrnum = 0;
				int neipoormunum = 0;
				if (indexnei.intValue() == 1) {
					neimrnum = gi.TOP1MRNUM;
					neipoormunum = gi.TOP1MRPOORNUM;
				}
				if (indexnei.intValue() == 2) {
					neimrnum = gi.TOP2MRNUM;
					neipoormunum = gi.TOP2MRPOORNUM;
				}
				if (indexnei.intValue() == 3) {
					neimrnum = gi.TOP3MRNUM;
					neipoormunum = gi.TOP3MRPOORNUM;
				}
				if (indexnei.intValue() == 4) {
					neimrnum = gi.TOPECIALLNUM;
					neipoormunum = gi.TOPECIPOORNUM;
				}
				if (neimrnum == 0) {
					continue;
				}
				// 条件1
				float rate = (float) neimrnum / mrnum;
				if (rate <= 0.5) {
					continue;
				}
				// 条件2
				float rate1 = 1f - (float) neipoormunum / neimrnum;
				if (rate1 <= 0.9) {
					continue;
				}
				flag = true;
				count[i] += 1;
				// 组织输出字段
				mn.SCENE_MAJOR = g.SCENE_MAJOR;
				mn.SCENE_MINOR = g.SCENE_MINOR;
				mn.GRID_ID = g.GRID_ID;
				mn.S_CELL_NUM = mrnum;
				mn.S_CELL_RATE = 1 - (float) poormrnum / mrnum;
				mn.D_CELL_NUM = neimrnum;
				mn.D_CELL_RATE = rate1;
				gridMissExporter.Export(mn);
				// 场景级汇总
				SetDimensionMiss(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER
						+ g.SCENE_MINOR, mn, SCENE_FLAG);
				// 地市级汇总
				SetDimensionMiss(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, mn, CITY_FLAG);
				// 栅格邻区漏配详情详情输出
			}
			if (flag == true) {
				c += c.length() == 0 ? "从小区栅格覆盖情况来看" + mlist.get(0).S_CELL_NAME : "、" + mlist.get(0).S_CELL_NAME;
			}
			i++;
		}
		if (c.length() > 0) {
			SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER
					+ g.SCENE_MINOR, g.CITY + SPLITER + g.GRID_ID, MISSNEI, SCENE_FLAG);
			SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, g.CITY + SPLITER + g.GRID_ID, MISSNEI,
					CITY_FLAG);
			// 统计
			int sum = 0;
			int sumlin = 0;
			for (int a : count) {
				if (a > 0) {
					sum++;
					sumlin += a;
				}
			}
			gsc.MISSNEIGH = "发现有" + sum + "个小区存在邻区漏配现象，建议添加" + sumlin + "条漏配邻区数据。";
		}
		return c.length() > 0 ? c + "有邻区漏配问题，建议添加漏配邻区。" : "";
	}

	// 设置场景级别或地市级别邻区漏配信息,flag 0为场景级别，1为地市级别
	private void SetDimensionMiss(String dimension, MissNeighbor mn, byte flag) {
		HashMap<Byte, HashMap<String, Object>> summayMap = flag == SCENE_FLAG ? sceneSummaryMap.get(dimension)
				: citySummaryMap.get(dimension);
		if (summayMap == null) {
			summayMap = new HashMap<Byte, HashMap<String, Object>>();
			if (flag == SCENE_FLAG) {
				sceneSummaryMap.put(dimension, summayMap);
			} else {
				citySummaryMap.put(dimension, summayMap);
			}
		}
		HashMap<String, Object> infoMap = summayMap.get(MISSNEI);
		if (infoMap == null) {
			infoMap = new HashMap<String, Object>();
			summayMap.put(MISSNEI, infoMap);
		}
		String key = mn.S_ECI + SPLITER + mn.D_ECI;
		Object o = infoMap.get(key);
		if (o == null) {
			o = mn;
			infoMap.put(key, o);
			// o.count[0] = 1;// 邻区漏配对
		}
	}

	// 告警结论
	private String GetAlarmConclusion(GridResult g, GridOverList goList, IssueGridSuggestCount gsc) {
		// 判断该栅格覆盖数据与历史存在较大偏差

		String c = "";
		boolean flag = false;
		if (g.HISMRDEV == 1) {
			c = "";
			flag = true;
		}
		int eciCount = 0;
		int alarmCount = 0;
		int liveAlarmCount = 0;
		if (goList != null) {
			HashSet<Long> eciList = new HashSet<Long>();
			eciList.addAll(GetEciList(goList.F_BANDLIST));
			eciList.addAll(GetEciList(goList.D_BANDLIST));
			eciList.addAll(GetEciList(goList.FDD_BANDLIST));
			ArrayList<AlarmList> list = new ArrayList<AlarmList>();
			for (long eci : eciList) {
				List<AlarmList> alList = alarmListMap.get(eci);
				if (alList == null) {
					continue;
				}
				eciCount++;
				for (AlarmList al : alList) {
					alarmCount++;
					if (al.CLRINSERT_TIME == null || al.CLRINSERT_TIME.length() == 0) {
						liveAlarmCount++;
					}

					// 栅格告警详情输出
					EngineerData ed = engdataMap.get(al.ECI);
					String cellname = ed.def_cellname_chinese == null ? "" : ed.def_cellname_chinese;
					al.GRID_ID = g.GRID_ID;
					al.CELL_NAME = cellname;
					al.SCENE_MAJOR = g.SCENE_MAJOR;
					al.SCENE_MINOR = g.SCENE_MINOR;
					list.add(al);
				}
			}

			if (eciCount > 0 && flag == true) {
				c = "该栅格覆盖数据与历史数据存在较大偏差";
				for (AlarmList al : list) {
					gridAlarmExporter.Export(al);
					// 场景级汇总
					SetDimensionAlarm(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR
							+ SPLITER + g.SCENE_MINOR, al, SCENE_FLAG);
					// 地市级汇总
					SetDimensionAlarm(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, al, CITY_FLAG);
				}
				c += String.format("且该栅格周边覆盖站点或小区存在影响业务类告警，其中%d个小区共产生告警量%d条，目前已恢复%d条，需处理%d条。", eciCount, alarmCount,
						alarmCount - liveAlarmCount, liveAlarmCount);
				gsc.ALARM = String.format("共有%d个网元共产生影响覆盖类告警%d条，需处理%d条。", eciCount, alarmCount,
						alarmCount - liveAlarmCount, liveAlarmCount);
				SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY + SPLITER + g.SCENE_MAJOR + SPLITER
						+ g.SCENE_MINOR, g.CITY + SPLITER + g.GRID_ID, ALARM, SCENE_FLAG);
				SetDimensionGrid(g.DATE + SPLITER + g.PROVINCE + SPLITER + g.CITY, g.CITY + SPLITER + g.GRID_ID, ALARM,
						CITY_FLAG);
			}
		}
		return c;
	}
	// flag 0为场景级别，1为地市级别

	// 设置场景级别或地市级别告警信息,flag 0为场景级别，1为地市级别
	private void SetDimensionAlarm(String dimension, AlarmList al, byte flag) {
		HashMap<Byte, HashMap<String, Object>> summayMap = flag == SCENE_FLAG ? sceneSummaryMap.get(dimension)
				: citySummaryMap.get(dimension);
		if (summayMap == null) {
			summayMap = new HashMap<Byte, HashMap<String, Object>>();
			if (flag == SCENE_FLAG) {
				sceneSummaryMap.put(dimension, summayMap);
			} else {
				citySummaryMap.put(dimension, summayMap);
			}
		}
		HashMap<String, Object> infoMap = summayMap.get(ALARM);
		if (infoMap == null) {
			infoMap = new HashMap<String, Object>();
			summayMap.put(ALARM, infoMap);
		}

		String key = al.ECI + SPLITER + al.INSERT_TIME + SPLITER
				+ (al.CLRINSERT_TIME != null && al.CLRINSERT_TIME.length() > 0 ? al.CLRINSERT_TIME : "");
		Object o = infoMap.get(key);
		if (o == null) {
			o = al;
			infoMap.put(key, o);
//			if (al.CLRINSERT_TIME != null && al.CLRINSERT_TIME.length() > 0) {
//				o.count[0] = 1;// 已恢复告警条数
//				o.count[1] = 0;// 需处理告警条数
//			} else {
//				o.count[0] = 0;// 已恢复告警条数
//				o.count[1] = 1;// 需处理告警条数
//			}
		}
	}

	private void SetDimensionGrid(String dimension, String key, byte dimensionFlag, byte flag) {
		HashMap<Byte, HashMap<String, Long>> issueGridMap = flag == SCENE_FLAG ? sceneGridsCountMap.get(dimension)
				: cityGridsCountMap.get(dimension);
		if (issueGridMap == null) {
			issueGridMap = new HashMap<Byte, HashMap<String, Long>>();
			if (flag == SCENE_FLAG) {
				sceneGridsCountMap.put(dimension, issueGridMap);
			} else {
				cityGridsCountMap.put(dimension, issueGridMap);
			}
		}
		try{
			HashMap<String, Long> dimensionMap = issueGridMap.get(dimensionFlag);
		
			if (dimensionMap == null) {
				dimensionMap = new HashMap<String, Long>();
				issueGridMap.put(dimensionFlag, dimensionMap);
			}
			Long l = dimensionMap.get(key);
			if (l == null) {
				l = new Long(1);
				dimensionMap.put(key, l);
			}
		}catch(Exception e){
			logger.info("error is "+e);
		}
	}

	private List<Long> GetEciList(String ecis) {
		if (ecis == null || ecis.length() == 0)
			return new ArrayList<Long>();
		List<Long> list = new ArrayList<Long>();
		String[] f_ecis = ecis.split(" ");
		if (f_ecis.length > 0) {
			for (String seci : f_ecis) {
				seci = seci.trim();
				if (seci.length() == 0)
					continue;
				list.add(Long.parseLong(seci));
			}

		}
		return list;
	}

	private String[] GetSceneMinor(String city, long gridid) {
		String[] s = { "未知场景", "未知细分场景" };
		Map<Long, GridBase> gbmap = gridBaseMap.get(city + SPLITER + gridid);
		if (gbmap == null || gbmap.size() == 0)
			return s;
		for (GridBase gb : gbmap.values()) {
			if (gb.SCENE_MINOR != null && gb.SCENE_MINOR.length() > 0) {
				s[1] = gb.SCENE_MINOR;
				if (gb.SCENE_MAJOR != null && gb.SCENE_MAJOR.length() > 0) {
					s[0] = gb.SCENE_MAJOR;
				}
				break;
			}
		}
		return s;
	}

	private int GetHisEssrvccHo(long top1Eci, Matrix matrix) {
		if (matrix == null)
			return 0;
		int count = 0;
		CellInfo ci1 = cellMap.get(top1Eci);
		count += ci1 != null && ci1.IRATHO_ATTOUTGERAN > 100 ? 1 : 0;
		CellInfo ci2 = cellMap_history1.get(top1Eci);
		count += ci2 != null && ci2.IRATHO_ATTOUTGERAN > 100 ? 1 : 0;
		CellInfo ci3 = cellMap_history2.get(top1Eci);
		count += ci2 != null && ci3.IRATHO_ATTOUTGERAN > 100 ? 1 : 0;
		CellInfo ci4 = cellMap_history3.get(top1Eci);
		count += ci2 != null && ci4.IRATHO_ATTOUTGERAN > 100 ? 1 : 0;
		return count > Integer.parseInt(matrix.THRESHOLD) ? 1 : 0;
	}

	private int GetHisMrDev(GridInfo g, Matrix matrix) {
		if (matrix == null)
			return 0;
		GridInfo bg = beforeGridMap.get(g.CITY + SPLITER + g.GRID_ID);
		if (bg == null)
			return 0;
		if (g.MRNUM == 0 || bg.MRNUM == 0)
			return 0;
		float value1 = (float) g.MRNUM / bg.MRNUM;
		float bRate = 1f - (float) bg.MRPOORNUM / bg.MRNUM;
		float value2 = bRate == 0 ? 0f : (1 - (float) g.MRPOORNUM / g.MRNUM) / bRate;
		return (value1 > 1.3 || value1 < 0.7) || (value2 > 1.3 || value2 < 0.7) ? 1 : 0;
	}

	private int GetTop1OverCover(long top1Eci, Matrix matrix) {
		if (top1Eci == 0 || matrix == null)
			return 0;
		List<CoverStep> csList = coverStepMap.get(top1Eci);
		if (csList == null)
			return 0;
		return csList.size() > Integer.parseInt(matrix.THRESHOLD) ? 1 : 0;
	}

	private int GetPoverError(GridInfo g, Matrix matrix) {
		if (matrix == null)
			return 0;
		HashSet<Long> eciList = new HashSet<Long>();
		if (g.TOP1ECI != 0) {
			eciList.add(g.TOP1ECI);
		}
		if (g.TOP2ECI != 0) {
			eciList.add(g.TOP2ECI);
		}
		if (g.TOP3ECI != 0) {
			eciList.add(g.TOP3ECI);
		}
		if (g.TOPECIALLNUMECI != 0) {
			eciList.add(g.TOPECIALLNUMECI);
		}
		int count = 0;
		for (long srceci : eciList) {
			CheckParaList cpl = checkParaListMap.get(srceci);
			if (cpl != null && cpl.is_identical.equals("不合规")) {
				count += 1;
			}
		}
		return count > Integer.parseInt(matrix.THRESHOLD) ? 1 : 0;
	}

	private int GetAdjDir(GridInfo g, Matrix matrix) {
		ArrayList<Float> disList = new ArrayList<Float>();
		ArrayList<Long> eciList = new ArrayList<Long>();
		if (g.TOP1ECI != 0 && g.TOP1DIS > 960) {
			eciList.add(g.TOP1ECI);
			disList.add(g.TOP1DIS);
		}
		if (g.TOP2ECI != 0 && g.TOP2DIS > 960) {
			eciList.add(g.TOP2ECI);
			disList.add(g.TOP2DIS);
		}
		if (g.TOP3ECI != 0 && g.TOP3DIS > 960) {
			eciList.add(g.TOP3ECI);
			disList.add(g.TOP3DIS);
		}
		for (int i = 0; i < eciList.size(); i++) {
			long eci = eciList.get(i);
			float linedis = disList.get(i);
			// 条件一：小区距离栅格直线距离 > 周边基站平均距离
			EngineerData ed = engdataMap.get(eci);
			if (ed == null || ed.site_distance == 0f)
				continue;
			float sitdis = ed.site_distance * 1000;
			String cellSite = ed.def_sitename;
			if (eci == 0 || linedis <= sitdis)
				continue;
			// 条件二：小区方向角正负60度范围内距离栅格中间位置，存在其他基站
			GridLineList gll = gridLineMap.get(g.GRID_ID + SPLITER + eci);
			if (gll == null || gll.TOPECILIST == null || gll.TOPECILIST.length() == 0)
				continue;
			int outdoorcount = 0;
			int sitecount = 0;
			String otherecis[] = gll.TOPECILIST.split(" ");
			for (String streci : otherecis) {
				streci = streci.trim();
				if (streci.length() == 0)
					continue;
				EngineerData oed = engdataMap.get(Long.parseLong(streci));
				if (oed != null && !oed.def_sitename.equals(cellSite)) {
					sitecount++;
				}
				if(oed != null && oed.inoutdoor.trim().equals("室外")) {
					outdoorcount++;
				}
			}
			if (sitecount == 0 || outdoorcount == 0)
				continue;
			return 1;
		}
		return 0;
	}

	private int GetAdjAngle(GridInfo g, Matrix matrix) {
		ArrayList<Integer> angleList = new ArrayList<Integer>();
		ArrayList<Long> eciList = new ArrayList<Long>();
		if (g.TOP1ECI != 0) {
			eciList.add(g.TOP1ECI);
			angleList.add(g.TOP1ANGLE);
		}
		if (g.TOP2ECI != 0) {
			eciList.add(g.TOP2ECI);
			angleList.add(g.TOP2ANGLE);
		}
		if (g.TOP3ECI != 0) {
			eciList.add(g.TOP3ECI);
			angleList.add(g.TOP3ANGLE);
		}

		for (int i = 0; i < eciList.size(); i++) {
			long eci = eciList.get(i);
			int angle = angleList.get(i);
			EngineerData ed = engdataMap.get(eci);
			if (ed == null || !ed.inoutdoor.trim().equals("室外"))
				continue;
			MrsAoaCover mac = mrsAoaCover60Map.get(eci);
			if (mac == null)
				continue;
			// 条件1
			if (mac.aoa[0] >= 0.7)
				continue;
			// 条件2
			angle = angle > 180 ? angle - 360 : angle;
			if (angle * mac.avgaoa < 0)
				continue;
			float min = Math.min(angle, mac.avgaoa);
			float max = Math.max(angle, mac.avgaoa);
			if (angle > 0 && mac.avgaoa > 0 && min > 10 && Math.abs(angle-mac.avgaoa)<=60) {
				return 1;
			}
			if (angle < 0 && mac.avgaoa < 0 && max < -10 && Math.abs(angle-mac.avgaoa)<=60) {
				return 1;
			}
		}
		return 0;
	}

	private int GetSimulCover(GridInfo g, Matrix matrix) {
		if (matrix == null)
			return 0;
		Grid2mgrs g2m = grid2mgrsMap.get(g.CITY + SPLITER + g.GRID_ID);
		if (g2m == null || g2m.MGRS == null || g2m.MGRS.length() == 0)
			return 0;
		SimulCover sc = simulCoverMap.get(g2m.MGRS);
		if (sc == null || sc.ALLMRNUM == 0)
			return 0;
		float value = 1f - (float) sc.POORMRNUM / sc.ALLMRNUM;
		return value < Float.parseFloat(matrix.THRESHOLD) ? 1 : 0;
	}

	private int GetTop1MissEciMrRate(Long top1Eci, Matrix matrix) {
		if (top1Eci == 0 || matrix == null)
			return 0;
		List<MissNeighbor> mnList = missNeighborMap.get(top1Eci);
		if (mnList == null || mnList.size() == 0)
			return 0;
		long neci = 0;
		float mindis = 0;
		for (MissNeighbor mn : mnList) {
			if (mindis == 0) {
				mindis = mn.distance;
			} else {
				mindis = Math.min(mindis, mn.distance);
				if (mindis == mn.distance)
					neci = mn.D_ECI;
			}
		}
		if (mindis == 0 || neci == 0)
			return 0;
		MrsStatRsrp msr = mrsStatRsrpMap.get(neci);
		if (msr == null || msr.reportnum == 0)
			return 0;
		int sum = 0;
		for (int i = 0; i < 21; i++) {
			sum += msr.mr_rsrp[i];
		}
		float value = (float) sum / msr.reportnum;
		return value > Float.parseFloat(matrix.THRESHOLD) ? 1 : 0;
	}

	private int GetCellList(GridInfo g, Matrix matrix) {
		if (matrix == null)
			return 0;
		GridOverList gol = gridOverMap.get(g.CITY + SPLITER + g.GRID_ID);
		if (gol == null)
			return 0;
		String allEcis = (gol.D_BANDLIST == null ? "" : gol.D_BANDLIST) + " "
				+ (gol.F_BANDLIST == null ? "" : gol.F_BANDLIST) + " "
				+ (gol.FDD_BANDLIST == null ? "" : gol.FDD_BANDLIST);
		String strEcis[] = allEcis.replace("\r", "").split(" ");
		for (String eci : strEcis) {
			if (eci.length() == 0)
				continue;
			long neci = Long.parseLong(eci);
			if (neci != 0 && neci != g.TOP1ECI && neci != g.TOP2ECI && neci != g.TOP3ECI && neci != g.TOPECIALLNUMECI) {
				return 1;
			}
		}
		return 0;
	}

	private int GetPoorCover(GridInfo g, Matrix matrix) {
		if (matrix == null)
			return 0;
		int Top1NeiGrid = g.TOP1NEIGRID;
		return Top1NeiGrid > Integer.parseInt(matrix.THRESHOLD) ? 1 : 0;
	}

	// 获取单个GridResult字段的矩阵值组
	private void SumarryOneGridResult(IssueGridResult result, List<IssueGridResult> oneIssuetempList) {
		for (IssueGridResult i : oneIssuetempList) {
			result.ANTENNA += i.ANTENNA;
			result.ANGLE += i.ANGLE;
			result.NOBESTCELL += i.NOBESTCELL;
			result.OVERCOVER += i.OVERCOVER;
			result.ALARM += i.ALARM;
			result.MISSNEIGH += i.MISSNEIGH;
			result.OVERDISTANCE += i.OVERDISTANCE;
			result.BACKBUILD += i.BACKBUILD;
		}
		result.ANTENNA = GetBeliveValue(result.ANTENNA);
		result.ANGLE = GetBeliveValue(result.ANGLE);
		result.NOBESTCELL = GetBeliveValue(result.NOBESTCELL);
		result.OVERCOVER = GetBeliveValue(result.OVERCOVER);
		result.ALARM = GetBeliveValue(result.ALARM);
		result.MISSNEIGH = GetBeliveValue(result.MISSNEIGH);
		result.OVERDISTANCE = GetBeliveValue(result.OVERDISTANCE);
		result.BACKBUILD = GetBeliveValue(result.BACKBUILD);
	}

	// 对单个场景置各维度信度值进行累加
	private void SumarrySceneBelive(GridResult gridResult, IssueGridResult issueGridResult) {
		if (gridResult == null || issueGridResult == null)
			return;
		String key = gridResult.DATE + SPLITER + gridResult.PROVINCE + SPLITER + gridResult.CITY + SPLITER
				+ gridResult.SCENE_MAJOR + SPLITER + gridResult.SCENE_MINOR;
		HashMap<Byte, Long> oneScene = sceneBeliveSummaryMap.get(key);
		if (oneScene == null) {
			oneScene = new HashMap<Byte, Long>();
			oneScene.put(ALARM, new Long(0));
			oneScene.put(MISSNEI, new Long(0));
			oneScene.put(ANTENNA, new Long(0));
			oneScene.put(ANGLE, new Long(0));
			oneScene.put(OVERCOVER, new Long(0));
			oneScene.put(OVERDISTANCE, new Long(0));
			oneScene.put(BACKBUILD, new Long(0));
			oneScene.put(NOBESTCELL, new Long(0));
			sceneBeliveSummaryMap.put(key, oneScene);
		}
		Long alarm = oneScene.get(ALARM);
		alarm += issueGridResult.ALARM;
		oneScene.put(ALARM, alarm);

		Long missnei = oneScene.get(MISSNEI);
		missnei += issueGridResult.MISSNEIGH;
		oneScene.put(MISSNEI, missnei);

		Long antenna = oneScene.get(ANTENNA);
		antenna += issueGridResult.ANTENNA;
		oneScene.put(ANTENNA, antenna);

		Long angle = oneScene.get(ANGLE);
		angle += issueGridResult.ANGLE;
		oneScene.put(ANGLE, angle);

		Long overCover = oneScene.get(OVERCOVER);
		overCover += issueGridResult.OVERCOVER;
		oneScene.put(OVERCOVER, overCover);

		Long overDistance = oneScene.get(OVERDISTANCE);
		overDistance += issueGridResult.OVERDISTANCE;
		oneScene.put(OVERDISTANCE, overDistance);

		Long blackBuild = oneScene.get(BACKBUILD);
		blackBuild += issueGridResult.BACKBUILD;
		oneScene.put(BACKBUILD, blackBuild);

		Long noBestCell = oneScene.get(NOBESTCELL);
		noBestCell += issueGridResult.NOBESTCELL;
		oneScene.put(NOBESTCELL, noBestCell);

	}

	private int GetBeliveValue(int sumvalue) {
		if (sumvalue > 6000)
			return 2;
		if (sumvalue > 4000)
			return 1;
		return 0;
	}

	// 获取单个GridResult字段的矩阵值组
	private IssueGridResult SetOneItems(int cv, Matrix mt) {
		if (mt == null)
			return new IssueGridResult();
		IssueGridResult i = new IssueGridResult();
		i.ANTENNA = cv * mt.ANTENNA;
		i.ANGLE = cv * mt.ANGLE;
		i.NOBESTCELL = cv * mt.NOBESTCELL;
		i.OVERCOVER = cv * mt.OVERCOVER;
		i.ALARM = cv * mt.ALARM;
		i.MISSNEIGH = cv * mt.MISSNEIGH;
		i.OVERDISTANCE = cv * mt.OVERDISTANCE;
		i.BACKBUILD = cv * mt.BACKBUILD;
		return i;
	}

	// private int GetPoverError(CellInfo cell,Matrix matrix) {
	// if(cell == null || matrix == null) return 0;
	// return 0;
	// }

	private int GetErvccHoRate(CellInfo cell, Matrix matrix) {
		if (cell == null || matrix == null)
			return 0;
		if (cell.IRATHO_ATTOUTGERAN == 0)
			return 0;
		int value1 = cell.IRATHO_ATTOUTGERAN > 100 ? 1 : 0;
		int value2 = (float) cell.IRATHO_SUCCOUTGERAN / cell.IRATHO_ATTOUTGERAN > 0.9 ? 1 : 0;
		return value1 & value2;
	}

	private int GetOtherCellExist(GridInfo g, GridLineList linelist, Map<Long, AlarmCount> alarmCountMap,
			Matrix matrix) {
		if (linelist == null || linelist.TOPECILIST == null || linelist.TOPECILIST.length() == 0 || matrix == null
				|| alarmCountMap.size() == 0)
			return 0;
		boolean flag = true;
		String ecis[] = linelist.TOPECILIST.split(" ");
		for (String eci : ecis) {
			eci = eci.trim();
			if (eci.length() == 0)
				continue;
			long neci = Long.parseLong(eci);
			if (neci == g.TOP2ECI || neci == g.TOP3ECI || neci == g.TOPECIALLNUMECI) {
				continue;
			}
			if (alarmCountMap.containsKey(neci)) {
				flag = false;
				break;
			}
		}

		return flag == true ? 1 : 0;
	}

	private int GetTaffic(CellInfo cell, Matrix matrix) {
		if (cell == null || matrix == null)
			return 0;
		int traffic = cell.PDCP_UpOctDl + cell.PDCP_UpOctUl;
		return traffic > Float.parseFloat(matrix.THRESHOLD) ? 1 : 0;
	}

	private int GetAlarmIncell(GridInfo g, Map<Long, AlarmCount> alarmCountMap, Matrix matrix) {
		if (g == null || matrix == null)
			return 0;
		int count = alarmCountMap.containsKey(g.TOP1ECI) == true ? alarmCountMap.get(g.TOP1ECI).ALARMCOUNT : 0;
		count += alarmCountMap.containsKey(g.TOP2ECI) == true ? alarmCountMap.get(g.TOP2ECI).ALARMCOUNT : 0;
		count += alarmCountMap.containsKey(g.TOP3ECI) == true ? alarmCountMap.get(g.TOP3ECI).ALARMCOUNT : 0;
		return count > Float.parseFloat(matrix.THRESHOLD) ? 1 : 0;
	}

	private int GetCellRate(int TOP1MRPOORNUM, int MRPOORNUM, Matrix matrix) {
		if (MRPOORNUM == 0 || matrix == null)
			return 0;
		return 1f - (float) TOP1MRPOORNUM / MRPOORNUM > Float.parseFloat(matrix.THRESHOLD) ? 1 : 0;
	}

	private int GetCoverRate(MrsStatRsrp mrsrsrp, Matrix matrix) {
		if (mrsrsrp == null || mrsrsrp.reportnum == 0 || matrix == null)
			return 0;
		int sum = 0;
		for (int i = 0; i < 21; i++) {
			sum += mrsrsrp.mr_rsrp[i];
		}
		float value = (float) sum / mrsrsrp.reportnum;
		return 1f - value < Float.parseFloat(matrix.THRESHOLD) ? 1 : 0;
	}

	private int GetDirDiffAoa(int dir, EngineerData engdata, Matrix matrix) {
		if (engdata == null || matrix == null)
			return 0;
		dir = dir > 180 ? dir - 360 : dir;
		int value = dir;
		// return value >=matrix.THRESHOLD && value <=matrix.THRESHOLD ? 1: 0;
		return value < -10 || value > 10 ? 1 : 0;
	}

	private int GetDisOverAvgSite(float dis, EngineerData engdata, Matrix matrix) {
		if (engdata == null || matrix == null)
			return 0;
		float value = dis - engdata.site_distance * 1000;
		return value > Float.parseFloat(matrix.THRESHOLD) ? 1 : 0;
	}

}
