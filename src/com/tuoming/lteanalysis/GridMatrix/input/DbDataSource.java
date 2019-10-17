package com.tuoming.lteanalysis.GridMatrix.input;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.tuoming.lteanalysis.GridMatrix.beans.GridLineList;
import com.tuoming.lteanalysis.GridMatrix.beans.GridOverList;
import com.tuoming.lteanalysis.GridMatrix.beans.Matrix;
import com.tuoming.lteanalysis.GridMatrix.beans.MissNeighbor;
import com.tuoming.lteanalysis.GridMatrix.beans.MrsStatAoa;
import com.tuoming.lteanalysis.GridMatrix.beans.MrsStatRsrp;
import com.tuoming.lteanalysis.GridMatrix.beans.SimulCover;
import com.tuoming.lteanalysis.GridMatrix.config.configer;
import com.tuoming.lteanalysis.GridMatrix.tools.MySqlDbHelper;

public class DbDataSource implements IDataSource {
	private static Logger logger = Logger.getLogger(DbDataSource.class);
	private MySqlDbHelper dbcontrol = null;
	final String SPLITER = "_";
	private String gridDate = null;
	private String beforeGridDate = null;
	configer config = null;
	public DbDataSource(configer cr) {
		config = cr;
		dbcontrol = new MySqlDbHelper(cr.dbIp, cr.dbPort, cr.dbName, cr.dbUser, cr.dbPasswd);
	}

	@Override
	public Map<String, GridInfo> LoadGrid(String lastDate) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		Map<String, GridInfo> map = new HashMap<String, GridInfo>();
		int invalid = 0;
		int all = 0;
		try {
			String sql = "select date from ana_grid_data group by date order by date desc limit 2";
			ResultSet rs = dbcontrol.Query(sql);
			if (rs == null) {
				return map;
			}
			if (rs.next() == true) {
				gridDate = rs.getString("date");
				if (gridDate == null || gridDate.length() == 0 || (lastDate != null && gridDate.equals(lastDate)))
					return map;
			}
			if (rs.next() == true) {
				beforeGridDate = rs.getString("date");
			}

			sql = "select a.* from ana_grid_data a inner join web_grid_base b on a.GRID_ID = b.GRID_ID and a.CITY = b.CITY "
					+ " where a.date='" + gridDate + "' and b.date= '" + gridDate + "'"
					+ " and b.C_SUM_POOR >50  and (b.DEGRADE in (3,4) or"
					+ " b.C_SUM_POOR/b.C_SUM_COUNT>=0.2 ) ";
			rs = dbcontrol.Query(sql);
			if (rs != null) {
				while (rs.next()) {
					try {
						all++;
						long gridid = rs.getLong("GRID_ID");
						String CITY = rs.getString("CITY");
						if (gridid == 0 || CITY == null || CITY.length() == 0) {
							invalid++;
							continue;
						}
						GridInfo g = new GridInfo();
						g.DATE = rs.getString("DATE");
						g.CITY = CITY;
						g.PROVINCE = rs.getString("PROVINCE");
						g.GRID_ID = gridid;
						g.LON = rs.getFloat("LON");
						g.LAT = rs.getFloat("LAT");
						g.MRNUM = rs.getInt("MRNUM");
						g.MRPOORNUM = rs.getInt("MRPOORNUM");
						g.AVGRSRP = rs.getFloat("AVGRSRP");
						g.CELLSNUM = rs.getInt("CELLSNUM");
						g.POORCELLSNUM = rs.getInt("POORCELLSNUM");
						g.NEIGRIDNUM = rs.getInt("NEIGRIDNUM");
						g.NEIMRNUM = rs.getInt("NEIMRNUM");
						g.NEIMRPOORNUM = rs.getInt("NEIMRPOORNUM");
						g.NEIAVGRSRP = rs.getFloat("NEIAVGRSRP");
						g.NEICELLSNUM = rs.getInt("NEICELLSNUM");
						g.NEIPOORCELLSNUM = rs.getInt("NEIPOORCELLSNUM");
						g.TOP1ECI = rs.getLong("TOP1ECI");
						g.TOP1MRNUM = rs.getInt("TOP1MRNUM");
						g.TOP1MRPOORNUM = rs.getInt("TOP1MRPOORNUM");
						g.TOP1AVGRSRP = rs.getFloat("TOP1AVGRSRP");
						g.TOP1NEIGRID = rs.getInt("TOP1NEIGRID");
						g.TOP1ANGLE = rs.getInt("TOP1ANGLE");
						g.TOP1DIS = rs.getFloat("TOP1DIS");
						g.TOP2ECI = rs.getLong("TOP2ECI");
						g.TOP2MRNUM = rs.getInt("TOP2MRNUM");
						g.TOP2MRPOORNUM = rs.getInt("TOP2MRPOORNUM");
						g.TOP2AVGRSRP = rs.getFloat("TOP2AVGRSRP");
						g.TOP2NEIGRID = rs.getInt("TOP2NEIGRID");
						g.TOP2ANGLE = rs.getInt("TOP2ANGLE");
						g.TOP2DIS = rs.getFloat("TOP2DIS");
						g.TOP3ECI = rs.getLong("TOP3ECI");
						g.TOP3MRNUM = rs.getInt("TOP3MRNUM");
						g.TOP3MRPOORNUM = rs.getInt("TOP3MRPOORNUM");
						g.TOP3AVGRSRP = rs.getFloat("TOP3AVGRSRP");
						g.TOP3NEIGRID = rs.getInt("TOP3NEIGRID");
						g.TOP3ANGLE = rs.getInt("TOP3ANGLE");
						g.TOP3DIS = rs.getFloat("TOP3DIS");
						g.TOPECIALLNUMECI = rs.getInt("TOPECIALLNUMECI");
						g.TOPECIALLNUM = rs.getInt("TOPECIALLNUM");
						g.TOPECIPOORNUM = rs.getInt("TOPECIPOORNUM");
						g.TOPECIAVGRSRP = rs.getFloat("TOPECIAVGRSRP");
						g.TOPECIANGLE = rs.getInt("TOPECIANGLE");
						g.TOPECIDIS = rs.getFloat("TOPECIDIS");
						map.put(CITY + SPLITER + gridid, g);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load Grid ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
				//设置最新一次执行的grid数据的日期。
				if(map.size() > 0) {
					config.SetOneConfig("LastGridDate", gridDate);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Load Grid ResultSet->next() error:\n" + e.getMessage());
		}
		long e = System.currentTimeMillis();
		logger.info("load Grid data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<String, GridLineList> LoadGridLineList(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<String, GridLineList> map = new HashMap<String, GridLineList>();
		String sql = "select a.* from ana_grid_linelist a inner join web_grid_base b on a.GRID_ID = b.GRID_ID  and a.CITY = b.CITY "
				+ "where a.insertdate='" + date + "' and b.date= '" + date + "' "
				+ " and  b.C_SUM_POOR >50  and  (b.DEGRADE in (3,4) or b.C_SUM_POOR/b.C_SUM_COUNT>=0.2)";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("TOPECI");
						long GRID_ID = rs.getLong("GRID_ID");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						GridLineList gl = new GridLineList();
						gl.INSERTDATE = rs.getString("INSERTDATE");
						gl.CITY = rs.getString("CITY");
						gl.GRID_ID = GRID_ID;
						gl.TOPECI = ECI;
						gl.TOPECILIST = rs.getString("TOPECILIST");
						map.put(GRID_ID + SPLITER + ECI, gl);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load GridLine ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load GridLine ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load GridLine data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<String, GridOverList> LoadGridOverList(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<String, GridOverList> map = new HashMap<String, GridOverList>();
		String sql = "select a.* from ana_grid_overlist a inner join web_grid_base b on a.GRID_ID = b.GRID_ID   and a.CITY = b.CITY "
				+ " where  b.date= '" + date + "'"
				// + "a.insertdate='" + date + "' and"
				+ " and b.C_SUM_POOR >50 and (b.DEGRADE in (3,4)  or b.C_SUM_POOR/b.C_SUM_COUNT>=0.2) ";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long GRID_ID = rs.getLong("GRID_ID");
						String CITY = rs.getString("CITY");
						if (GRID_ID == 0 || CITY == null || CITY.length() == 0) {
							invalid++;
							continue;
						}
						GridOverList go = new GridOverList();
						go.INSERTDATE = rs.getString("INSERTDATE");
						go.CITY = CITY;
						go.GRID_ID = GRID_ID;
						go.F_BANDLIST = rs.getString("F_BANDLIST");
						go.D_BANDLIST = rs.getString("D_BANDLIST");
						go.FDD_BANDLIST = rs.getString("FDD_BANDLIST");
						map.put(CITY + SPLITER + GRID_ID, go);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load GridOver ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load GridOver ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load GridOver data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, CellNeighbor> LoadCellneighbor(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, CellNeighbor> map = new HashMap<Long, CellNeighbor>();
		String sql = "select * from ana_cellnei_data_delay where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("ECI");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						CellNeighbor cnb = new CellNeighbor();
						cnb.DATE = rs.getString("DATE");
						cnb.ECI = ECI;
						cnb.ADJECI = rs.getLong("ADJECI");
						cnb.NCMRNUM = rs.getInt("NCMRNUM");
						cnb.NCPOORMRNUM = rs.getInt("NCPOORMRNUM");
						cnb.NCAVGRSRP = rs.getFloat("NCAVGRSRP");
						cnb.ALLMRNUM = rs.getInt("ALLMRNUM");
						cnb.POORMRNUM = rs.getInt("POORMRNUM");
						cnb.SCAVGRSRP = rs.getFloat("SCAVGRSRP");
						map.put(ECI, cnb);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load CellNeighbor ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load CellNeighbor ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load CellNeighbor data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, CellInfo> LoadCell(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, CellInfo> map = new HashMap<Long, CellInfo>();
		String sql = "select * from ana_cell_data where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("ECI");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						CellInfo ci = new CellInfo();
						ci.DATE = rs.getString("DATE");
						ci.CITY = rs.getString("CITY");
						ci.ECI = ECI;
						ci.ReferenceSignalPower = rs.getString("ReferenceSignalPower");
						ci.MaximumTransmissionPower = rs.getString("MaximumTransmissionPower");
						ci.Pb = rs.getString("Pb");
						ci.Pa = rs.getString("Pa");
						ci.IRATHO_SUCCOUTGERAN = rs.getInt("IRATHO_SUCCOUTGERAN");
						ci.IRATHO_ATTOUTGERAN = rs.getInt("IRATHO_ATTOUTGERAN");
						ci.PDCP_UpOctUl = rs.getInt("PDCP_UpOctUl");
						ci.PDCP_UpOctDl = rs.getInt("PDCP_UpOctDl");
						map.put(ECI, ci);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load Cell ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load Cell ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load Cell data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, AlarmCount> LoadAlarmCount(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, AlarmCount> map = new HashMap<Long, AlarmCount>();
		String sql = "select * from web_alarm_count where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("ECI");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						AlarmCount ac = new AlarmCount();
						ac.DATE = rs.getString("DATE");
						ac.ECI = ECI;
						ac.ALARMNAME = rs.getString("ALARMNAME");
						ac.ALARMCOUNT = rs.getInt("ALARMCOUNT");
						map.put(ECI, ac);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load AlarmCount ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load AlarmCount ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load AlarmCount data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, List<CoverStep>> LoadCoverStep(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, List<CoverStep>> map = new HashMap<Long, List<CoverStep>>();
		String sql = "select * from web_cover_overstep where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("taskeci");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						List<CoverStep> csList = map.get(ECI);
						if (csList == null) {
							csList = new ArrayList<CoverStep>();
							map.put(ECI, csList);
						}
						CoverStep cs = new CoverStep();
						cs.date = rs.getString("date");
						cs.taskeci = ECI;
						cs.sceci = rs.getLong("sceci");
						cs.nceci = rs.getLong("nceci");
						cs.dis = rs.getFloat("dis");
						cs.overcoverrate = rs.getFloat("overcoverrate");
						cs.ncreport = rs.getInt("ncreport");
						cs.ncellb6db = rs.getInt("ncellb6db");
						csList.add(cs);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load Cover OverStep ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load Cover OverStep ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load Cover OverStep data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, CoverWeak> LoadCoverWeak(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, CoverWeak> map = new HashMap<Long, CoverWeak>();
		String sql = "select * from web_cover_weak where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("eci");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						CoverWeak cw = new CoverWeak();
						cw.date = rs.getString("date");
						cw.eci = ECI;
						cw.valid_ltescrsrp = rs.getInt("valid_ltescrsrp");
						cw.ltescrsrp_over_counter = rs.getInt("ltescrsrp_over_counter");
						cw.ltescrsrp_weak_counter = rs.getInt("ltescrsrp_weak_counter");
						cw.weak_counter_rate = rs.getFloat("weak_counter_rate");
						cw.ltescrsrp_sum = rs.getInt("ltescrsrp_sum");
						cw.sc_nc_6db_count = rs.getInt("sc_nc_6db_count");
						map.put(ECI, cw);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load Cover weak ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load Cover weak ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load Cover weak data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<String, Matrix> LoadMatrix() {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<String, Matrix> map = new HashMap<String, Matrix>();
		String sql = "select * from ana_matrix";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						String ITEM = rs.getString("ITEM");
						if (ITEM == null || ITEM.length() == 0) {
							invalid++;
							continue;
						}
						Matrix m = new Matrix();
						m.ITEM = ITEM;
						m.THRESHOLD = rs.getString("THRESHOLD");
						m.ANTENNA = rs.getInt("ANTENNA");
						m.ANGLE = rs.getInt("ANGLE");
						m.NOBESTCELL = rs.getInt("NOBESTCELL");
						m.OVERCOVER = rs.getInt("OVERCOVER");
						m.ALARM = rs.getInt("ALARM");
						m.MISSNEIGH = rs.getInt("MISSNEIGH");
						m.OVERDISTANCE = rs.getInt("OVERDISTANCE");
						m.BACKBUILD = rs.getInt("BACKBUILD");
						m.LONGISSUE = rs.getInt("LONGISSUE");
						m.BREAKISSUE = rs.getInt("BREAKISSUE");
						m.GEOISSUE = rs.getInt("GEOISSUE");
						m.INDOORISSUE = rs.getInt("INDOORISSUE");
						m.PARAMERROR = rs.getInt("PARAMERROR");
						m.OTHER = rs.getInt("OTHER");
						map.put(ITEM, m);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load Matrix ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load Matrix ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load Matrix data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, MrsStatRsrp> LoadMrsStatRsrp(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, MrsStatRsrp> map = new HashMap<Long, MrsStatRsrp>();
		String sql = "select * from web_mrs_stat_rsrp where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("ECI");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						MrsStatRsrp msr = new MrsStatRsrp();
						msr.date = rs.getString("date");
						msr.ECI = ECI;
						for (int i = 0; i < 48; i++) {
							int mr_rsrp = rs.getInt("mr_rsrp_" + (i < 10 ? "0" + i : "" + i));
							msr.mr_rsrp[i] = mr_rsrp;
							msr.sum_mr_rsrp += mr_rsrp;
						}
						msr.reportnum = rs.getInt("reportnum");
						map.put(ECI, msr);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load MrsStatRsrp ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load MrsStatRsrp ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load MrsStatRsrp data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, EngineerData> LoadEngineerData(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, EngineerData> map = new HashMap<Long, EngineerData>();
		String sql = "select DAY_KEY from com_distance group by DAY_KEY order by DAY_KEY desc limit 1";
		String distanceDate = null;
		Map<String, Float> distanceMap = new HashMap<String, Float>();
		ResultSet rs = dbcontrol.Query(sql);
		try {
			if (rs != null && rs.next() == true) {
				distanceDate = rs.getString("DAY_KEY");
				if (distanceDate != null && distanceDate.length() > 0) {
					sql = "select ECELL_ZHNAME,MIN_DIS from com_distance where DAY_KEY='" + distanceDate + "'";
					ResultSet rsdis = dbcontrol.Query(sql);
					if (rsdis != null) {
						while (rsdis.next()) {
							try {
								String zhCellname = rsdis.getString("ECELL_ZHNAME");
								float distance = rsdis.getFloat("MIN_DIS");
								if(zhCellname != null && zhCellname.length() > 0 && distance != 0f) {
									distanceMap.put(zhCellname, distance);
								}
							} catch (SQLException e) {
								invalid++;
								logger.info("Load EngineerData ResultSet error:\n" + e.getMessage());
								continue;
							}

						}

					}
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Load com_distance ResultSet->next() error:\n" + e.getMessage());
		}
		boolean validflag = distanceMap.size() > 0 ? true: false;
		sql = "select def_eci,def_cellname,def_sitename,def_cellname_chinese,dir,tilt,etilt,height,site_distance,inoutdoor,region,def_sitename_chinese,band,longitude,latitude from web_engineerdata where date='" + date + "'";
		rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long def_eci = rs.getLong("def_eci");
						if (def_eci == 0) {
							invalid++;
							continue;
						}
						EngineerData ed = new EngineerData();
						// ed.date = rs.getString("date");
						// ed.city = rs.getString("city");
						 ed.region = rs.getString("region");
						// ed.town = rs.getString("town");
						// ed.grid = rs.getString("grid");
						// ed.def_vendor = rs.getString("def_vendor");
						// ed.omc = rs.getString("omc");
						// ed.def_cellid = rs.getInt("def_cellid");
						// ed.def_enbid = rs.getInt("def_enbid");
						ed.def_eci = def_eci;
						// ed.def_tci = rs.getString("def_tci");
						// ed.xmldn_eutranrelationtdd = rs.getString("xmldn_eutranrelationtdd");
						ed.def_cellname = rs.getString("def_cellname");
						ed.def_sitename = rs.getString("def_sitename");
						ed.def_cellname_chinese = rs.getString("def_cellname_chinese");
						ed.def_sitename_chinese = rs.getString("def_sitename_chinese");
						// ed.cellname_long = rs.getString("cellname_long");
						 ed.latitude = rs.getFloat("latitude");
						 ed.longitude = rs.getFloat("longitude");
						// ed.ant_bw = rs.getString("ant_bw");
						// ed.ant_size = rs.getString("ant_size");
						// ed.ant_type = rs.getString("ant_type");
						// ed.cell_type = rs.getString("cell_type");
						ed.dir = rs.getInt("dir");
						ed.tilt = rs.getInt("tilt");
						ed.etilt = rs.getInt("etilt");
						// ed.mtilt = rs.getInt("mtilt");
						ed.height = rs.getInt("height");
						 ed.band = rs.getString("band");
						// ed.earfcn = rs.getString("earfcn");
						// ed.pci = rs.getInt("pci");
						 ed.inoutdoor = rs.getString("inoutdoor");
						// ed.address = rs.getString("address");
						// ed.cov_scene = rs.getString("cov_scene");
						if(validflag && ed.def_cellname_chinese != null && ed.def_cellname_chinese.length() > 0) {
							Float dis = distanceMap.get(ed.def_cellname_chinese);
							ed.site_distance = dis == null ?  rs.getFloat("site_distance") : dis.floatValue();
						}else {
							ed.site_distance =  rs.getFloat("site_distance");
						}
						// ed.cover_area = rs.getString("cover_area");
						// ed.state = rs.getString("state");
						map.put(def_eci, ed);
					} catch (SQLException e) {
						invalid++;
						logger.info("Load EngineerData ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load EngineerData ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load EngineerData data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, List<AlarmList>> LoadAlarmList(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, List<AlarmList>> map = new HashMap<Long, List<AlarmList>>();
		String sql = "select * from web_alarm_list where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("ECI");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						List<AlarmList> allist = map.get(ECI);
						if (allist == null) {
							allist = new ArrayList<AlarmList>();
							map.put(ECI, allist);
						}
						AlarmList al = new AlarmList();
						al.DATE = rs.getString("DATE");
						al.PROVINCE = rs.getString("PROVINCE");
						al.CITY = rs.getString("CITY");
						al.ECI = ECI;
						al.ALARMNAME = rs.getString("ALARMNAME");
						al.INSERT_TIME = rs.getString("INSERT_TIME");
						al.CLRINSERT_TIME = rs.getString("CLRINSERT_TIME");
						allist.add(al);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load AlarmList ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load AlarmList ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load AlarmList data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, List<MissNeighbor>> LoadMissNeighbor(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, List<MissNeighbor>> map = new HashMap<Long, List<MissNeighbor>>();
		String sql = "select s_city_name,s_cell_name,s_eci,d_cell_name,d_eci,distance,DATE_FORMAT(time_stamp,'%Y-%m-%d') as tm from ana_missnei where time_stamp like '"
				+ date + "%'";
		// + " where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("s_eci");
						long D_ECI = rs.getLong("d_eci");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						if (D_ECI == 0) {
							invalid++;
							continue;
						}
						List<MissNeighbor> allist = map.get(ECI);
						if (allist == null) {
							allist = new ArrayList<MissNeighbor>();
							map.put(ECI, allist);
						}
						MissNeighbor mn = new MissNeighbor();
						mn.DATE = rs.getString("tm");
						mn.S_ECI = ECI;
						mn.CITY = rs.getString("s_city_name");
						mn.S_CELL_NAME = rs.getString("s_cell_name");
						mn.D_ECI = D_ECI;
						mn.D_CELL_NAME = rs.getString("d_cell_name");
						mn.distance = rs.getFloat("distance");
						allist.add(mn);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load MissNeighbor ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load MissNeighbor ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load MissNeighbor data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<String, Map<Long, GridBase>> LoadGridBase(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<String, Map<Long, GridBase>> map = new HashMap<String, Map<Long, GridBase>>();
		String sql = "select b.DATE,b.PROVINCE,b.CITY,b.GRID_ID,b.CELL_ID,b.SCENE_MAJOR,b.SCENE_MINOR,b.C_SUM_COUNT,b.C_SUM_POOR,b.LON,b.LAT,"
				+ "b.C_MEAN_RSRP,b.U_SUM_COUNT,b.U_SUM_POOR,b.T_SUM_COUNT,b.T_SUM_POOR from web_grid_base b "
				+ " where  b.date= '" + date + "'" + " and b.C_SUM_POOR >50  and (b.DEGRADE in (3,4) or "
				+ "b.C_SUM_POOR/b.C_SUM_COUNT>=0.2) ";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						String CITY = rs.getString("CITY");
						long GRID_ID = rs.getLong("GRID_ID");
						long ECI = rs.getLong("CELL_ID");
						if (ECI == 0 || CITY == null || CITY.length() == 0 || GRID_ID == 0) {
							invalid++;
							continue;
						}
						Map<Long, GridBase> cmap = map.get(CITY + SPLITER + GRID_ID);
						if (cmap == null) {
							cmap = new HashMap<Long, GridBase>();
							map.put(CITY + SPLITER + GRID_ID, cmap);
						}
						GridBase gb = new GridBase();
						gb.DATE = rs.getString("DATE");
						gb.PROVINCE = rs.getString("PROVINCE");
						gb.CITY = CITY;
						gb.SCENE_MAJOR = rs.getString("SCENE_MAJOR");
						gb.SCENE_MINOR = rs.getString("SCENE_MINOR");
						gb.GRID_ID = GRID_ID;
						gb.LON = rs.getFloat("LON");
						gb.LAT = rs.getFloat("LAT");
						gb.CELL_ID = ECI;
//						gb.DEGRADE = rs.getInt("DEGRADE");
						gb.C_SUM_COUNT = rs.getInt("C_SUM_COUNT");
						gb.C_SUM_POOR = rs.getInt("C_SUM_POOR");
						gb.C_MEAN_RSRP = rs.getFloat("C_MEAN_RSRP");
						gb.U_SUM_COUNT = rs.getInt("U_SUM_COUNT");
						gb.U_SUM_POOR = rs.getInt("U_SUM_POOR");
//						gb.U_MEAN_RSRP = rs.getFloat("U_MEAN_RSRP");
						gb.T_SUM_COUNT = rs.getInt("T_SUM_COUNT");
						gb.T_SUM_POOR = rs.getInt("T_SUM_POOR");
//						gb.T_MEAN_RSRP = rs.getFloat("T_MEAN_RSRP");
//						gb.WO_ID = rs.getString("WO_ID");
						cmap.put(ECI, gb);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load GridBase ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load GridBase ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load GridBase data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<String, Grid2mgrs> LoadGrid2Mgrs() {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<String, Grid2mgrs> map = new HashMap<String, Grid2mgrs>();
		String sql = "select CITY,GRID_ID,MGRS from sys_grid2mgrs";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long GRID_ID = rs.getLong("GRID_ID");
						String MGRS = rs.getString("MGRS");
						String CITY = rs.getString("CITY");
						if (GRID_ID == 0 || CITY == null || CITY.length() == 0 || MGRS == null || MGRS.length() == 0) {
							invalid++;
							continue;
						}
						Grid2mgrs gm = new Grid2mgrs();
						gm.CITY = rs.getString("CITY");
						gm.MGRS = MGRS;
						gm.GRID_ID = GRID_ID;
						map.put(CITY + SPLITER + GRID_ID, gm);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load Grid2mgrs ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load Grid2mgrs ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load Grid2mgrs data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, CheckParaList> LoadCheckParaList(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, CheckParaList> map = new HashMap<Long, CheckParaList>();
		String sql = "select eci,is_identical,param_name,vendor_name,real_value,plan_value from ana_checkparalist where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long eci = rs.getLong("eci");
						String is_identical = rs.getString("is_identical");
						if (eci == 0 || is_identical == null || is_identical.length() == 0) {
							invalid++;
							continue;
						}
						CheckParaList cpl = new CheckParaList();
						cpl.eci = eci;
						cpl.param_name = rs.getString("param_name");
						cpl.is_identical = is_identical;
						cpl.vendor_name = rs.getString("vendor_name");
						cpl.real_value = rs.getString("real_value");
						cpl.plan_value = rs.getString("plan_value");
						map.put(eci, cpl);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load CheckParaList ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load CheckParaList ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load CheckParaList data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	/*
	 * @Override public Map<Long, MrsAoaCover> LoadMrsAoaCover(String date) { //
	 * TODO Auto-generated method stub // TODO Auto-generated method stub long b =
	 * System.currentTimeMillis(); int invalid = 0; int all = 0; Map<Long,
	 * MrsAoaCover> map = new HashMap<Long, MrsAoaCover>(); String sql =
	 * "select * from web_mrs_aoa_cover where date='" + date +
	 * "' and inclinationtype=1"; ResultSet rs = dbcontrol.Query(sql); if (rs !=
	 * null) { try { while (rs.next()) { try { all++; long eci = rs.getLong("eci");
	 * if (eci == 0) { invalid++; continue; } MrsAoaCover mac = new MrsAoaCover();
	 * mac.eci = eci; mac.date = rs.getString("date"); mac.inclinationtype =
	 * rs.getShort("inclinationtype"); for (int i = 0; i < 72; i++) { mac.aoa[i] =
	 * rs.getFloat("aoa" + i * 5); } map.put(eci, mac); } catch (SQLException e) {
	 * invalid++; logger.debug("Load MrsAoaCover ResultSet error:\n" +
	 * e.getMessage()); continue; } } } catch (SQLException e) { // TODO
	 * Auto-generated catch block
	 * logger.error("Load MrsAoaCover ResultSet->next() error:\n" + e.getMessage());
	 * } } long e = System.currentTimeMillis();
	 * logger.info("load MrsAoaCover data: valid " + (all - invalid) + " total " +
	 * all + " use " + String.format("%.3f", 1e-3 * (e - b)) + " sec"); return map;
	 * }
	 */
	@Override
	public Map<Long, MrsStatAoa> LoadMrsStatAoa(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, MrsStatAoa> map = new HashMap<Long, MrsStatAoa>();
		String sql = "select * from web_mrs_stat_aoa where date='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long eci = rs.getLong("eci");
						String reportnum = rs.getString("reportnum");
						if (eci == 0 || reportnum == null || reportnum.length() == 0) {
							invalid++;
							continue;
						}
						MrsStatAoa msa = new MrsStatAoa();
						msa.eci = eci;
						msa.reportnum = Integer.parseInt(reportnum);
						msa.date = rs.getString("date");
						for (int i = 0; i < 72; i++) {
							msa.aoa[i] = rs.getInt("aoa_" + (i < 10 ? "0" + i : "" + i));
						}
						msa.avgaoa = rs.getFloat("avgaoa");
						map.put(eci, msa);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load MrsStatAoa ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load MrsStatAoa ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load MrsStatAoa data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<String, SimulCover> LoadSimulCover(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<String, SimulCover> map = new HashMap<String, SimulCover>();
		String sql = "select GRIDID,ALLMRNUM,POORMRNUM from simul_cover_data where DATE='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						String GRIDID = rs.getString("GRIDID");
						if (GRIDID == null || GRIDID.length() == 0) {
							invalid++;
							continue;
						}
						SimulCover sc = new SimulCover();
						sc.GRIDID = GRIDID;
						sc.ALLMRNUM = rs.getInt("ALLMRNUM");
						sc.POORMRNUM = rs.getInt("POORMRNUM");
						map.put(GRIDID, sc);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load SimulCover ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load SimulCover ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load SimulCover data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<String, GridInfo> LoadBeforeGrid() {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		Map<String, GridInfo> map = new HashMap<String, GridInfo>();
		int invalid = 0;
		int all = 0;
		try {
			if (beforeGridDate != null && beforeGridDate.length() > 0) {
				String sql = "select a.* from ana_grid_data a inner join web_grid_base b on a.GRID_ID = b.GRID_ID and a.CITY = b.CITY "
						+ " where a.date='" + beforeGridDate + "' and b.date= '" + gridDate + "'"
						+ " and b.C_SUM_POOR >50  and (b.DEGRADE in (3,4) or"
						+ " b.C_SUM_POOR/b.C_SUM_COUNT>=0.2 ) ";
				ResultSet rs = dbcontrol.Query(sql);
				if (rs != null) {
					while (rs.next()) {
						try {
							all++;
							long gridid = rs.getLong("GRID_ID");
							String CITY = rs.getString("CITY");
							if (gridid == 0 || CITY == null || CITY.length() == 0) {
								invalid++;
								continue;
							}
							GridInfo g = new GridInfo();
							g.DATE = rs.getString("DATE");
							g.CITY = CITY;
							g.PROVINCE = rs.getString("PROVINCE");
							g.GRID_ID = gridid;
							g.LON = rs.getFloat("LON");
							g.LAT = rs.getFloat("LAT");
							g.MRNUM = rs.getInt("MRNUM");
							g.MRPOORNUM = rs.getInt("MRPOORNUM");
							g.AVGRSRP = rs.getFloat("AVGRSRP");
							g.CELLSNUM = rs.getInt("CELLSNUM");
							g.POORCELLSNUM = rs.getInt("POORCELLSNUM");
							g.NEIGRIDNUM = rs.getInt("NEIGRIDNUM");
							g.NEIMRNUM = rs.getInt("NEIMRNUM");
							g.NEIMRPOORNUM = rs.getInt("NEIMRPOORNUM");
							g.NEIAVGRSRP = rs.getFloat("NEIAVGRSRP");
							g.NEICELLSNUM = rs.getInt("NEICELLSNUM");
							g.NEIPOORCELLSNUM = rs.getInt("NEIPOORCELLSNUM");
							g.TOP1ECI = rs.getLong("TOP1ECI");
							g.TOP1MRNUM = rs.getInt("TOP1MRNUM");
							g.TOP1MRPOORNUM = rs.getInt("TOP1MRPOORNUM");
							g.TOP1AVGRSRP = rs.getFloat("TOP1AVGRSRP");
							g.TOP1NEIGRID = rs.getInt("TOP1NEIGRID");
							g.TOP1ANGLE = rs.getInt("TOP1ANGLE");
							g.TOP1DIS = rs.getFloat("TOP1DIS");
							g.TOP2ECI = rs.getLong("TOP2ECI");
							g.TOP2MRNUM = rs.getInt("TOP2MRNUM");
							g.TOP2MRPOORNUM = rs.getInt("TOP2MRPOORNUM");
							g.TOP2AVGRSRP = rs.getFloat("TOP2AVGRSRP");
							g.TOP2NEIGRID = rs.getInt("TOP2NEIGRID");
							g.TOP2ANGLE = rs.getInt("TOP2ANGLE");
							g.TOP2DIS = rs.getFloat("TOP2DIS");
							g.TOP3ECI = rs.getLong("TOP3ECI");
							g.TOP3MRNUM = rs.getInt("TOP3MRNUM");
							g.TOP3MRPOORNUM = rs.getInt("TOP3MRPOORNUM");
							g.TOP3AVGRSRP = rs.getFloat("TOP3AVGRSRP");
							g.TOP3NEIGRID = rs.getInt("TOP3NEIGRID");
							g.TOP3ANGLE = rs.getInt("TOP3ANGLE");
							g.TOP3DIS = rs.getFloat("TOP3DIS");
							g.TOPECIALLNUMECI = rs.getInt("TOPECIALLNUMECI");
							g.TOPECIALLNUM = rs.getInt("TOPECIALLNUM");
							g.TOPECIPOORNUM = rs.getInt("TOPECIPOORNUM");
							g.TOPECIAVGRSRP = rs.getFloat("TOPECIAVGRSRP");
							g.TOPECIANGLE = rs.getInt("TOPECIANGLE");
							g.TOPECIDIS = rs.getFloat("TOPECIDIS");
							map.put(CITY + SPLITER + gridid, g);
						} catch (SQLException e) {
							invalid++;
							logger.debug("Load LoadBeforGrid ResultSet error:\n" + e.getMessage());
							continue;
						}
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Load LoadBeforGrid ResultSet->next() error:\n" + e.getMessage());
		}
		long e = System.currentTimeMillis();
		logger.info("load LoadBeforGrid data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

	@Override
	public Map<Long, CellUnit> LoadCellUnit(String date) {
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<Long, CellUnit> map = new HashMap<Long, CellUnit>();
		String sql = "select ECI,MRNUMALL,POORGRIDNMUM,GRIDNUM from web_cell_unit where DATE='" + date + "'";
		ResultSet rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						all++;
						long ECI = rs.getLong("ECI");
						if (ECI == 0) {
							invalid++;
							continue;
						}
						CellUnit cu = new CellUnit();
						cu.ECI = ECI;
						cu.MRNUMALL = rs.getInt("MRNUMALL");
						cu.POORGRIDNMUM = rs.getInt("POORGRIDNMUM");
						cu.GRIDNUM = rs.getInt("GRIDNUM");
						map.put(ECI, cu);
					} catch (SQLException e) {
						invalid++;
						logger.debug("Load CellUnit ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load CellUnit ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load CellUnit data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}
	
	@Override
	public Map<String, AnaGridData> LoadAnaGridData(String date,Map<Long, EngineerData> engdataMap) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		long b = System.currentTimeMillis();
		int invalid = 0;
		int all = 0;
		Map<String, AnaGridData> map = new HashMap<String, AnaGridData>();
		String sql = "select CITY,GRID_ID,CONCAT(ANGLETOP1,ANGLETOP2,ANGLETOP3,NOBESTCELL,OVERCOVERTOP1,OVERCOVERTOP2,OVERCOVERTOP3,ALARM,MISSNEIGH,OVERDISTANCE,BACKBUILD)  SUGGEST from web_issueana_grid_suggest where date ='"+date+"'";
		Map<String, String> suggestMap = new HashMap<String, String>();
		ResultSet rs = dbcontrol.Query(sql);
		if(rs != null){
			try {
				while (rs.next()) {
					long GRID_ID = rs.getLong("GRID_ID");
					String CITY = rs.getString("CITY");
					String suggest = rs.getString("SUGGEST");
					suggestMap.put(CITY + SPLITER + GRID_ID, suggest);
				}
	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load web_issueana_grid_suggest ResultSet->next() error:\n" + e.getMessage());
			}
		}
		sql = "select DATE,PROVINCE,CITY,GRID_ID,LON,LAT,TOP1ECI,TOP1MRPOORNUM,TRUNCATE(TOP1MRPOORNUM/MRPOORNUM,2) TOP1_POOR_RATE,TOP1DIS,"
				+ "TOP2ECI,TOP2MRPOORNUM,TRUNCATE(TOP2MRPOORNUM/MRPOORNUM,2) TOP2_POOR_RATE,TOP2DIS,"
				+ "TOP3ECI,TOP3MRPOORNUM,TRUNCATE(TOP3MRPOORNUM/MRPOORNUM,2) TOP3_POOR_RATE,TOP3DIS from ana_grid_data where DATE='" + date + "'";
		rs = dbcontrol.Query(sql);
		if (rs != null) {
			try {
				while (rs.next()) {
					try {
						long GRID_ID = rs.getLong("GRID_ID");
						long top1eci = rs.getLong("TOP1ECI");
						long top2eci = rs.getLong("TOP2ECI");
						long top3eci = rs.getLong("TOP3ECI");
						String CITY = rs.getString("CITY");
						AnaGridData agd = new AnaGridData();
						agd.DATE = rs.getString("DATE");
						agd.PROVINCE = rs.getString("PROVINCE");
						agd.CITY = CITY;
						agd.GRID_ID =GRID_ID;
						agd.LON = rs.getFloat("LON");
						agd.LAT = rs.getFloat("LAT");
						agd.TOP1ECI = top1eci;
						agd.TOP1MRPOORNUM = rs.getInt("TOP1MRPOORNUM");
						agd.TOP1_POOR_RATE = rs.getFloat("TOP1_POOR_RATE");
						agd.TOP1DIS = rs.getFloat("TOP1DIS");
						if(engdataMap.get(top1eci)!=null){
							EngineerData ed = engdataMap.get(top1eci);
							agd.TOP1NAME = ed.def_cellname_chinese;
							agd.TOP1DIR = ed.dir;
							agd.TOP1HEIGHT = ed.height;
							agd.TOP1TILT =ed.tilt;
							agd.TOP1BAND = ed.band;
						}
						
						agd.TOP2ECI = top2eci;
						agd.TOP2MRPOORNUM = rs.getInt("TOP2MRPOORNUM");
						agd.TOP2_POOR_RATE = rs.getFloat("TOP2_POOR_RATE");
						agd.TOP2DIS = rs.getFloat("TOP2DIS");
						if(engdataMap.get(top2eci)!=null){
							EngineerData ed = engdataMap.get(top2eci);
							agd.TOP2NAME = ed.def_cellname_chinese;
							agd.TOP2DIR = ed.dir;
							agd.TOP2HEIGHT = ed.height;
							agd.TOP2TILT = ed.tilt;
							agd.TOP2BAND = ed.band;
						}
						
						agd.TOP3ECI = top3eci;
						agd.TOP3MRPOORNUM = rs.getInt("TOP3MRPOORNUM");
						agd.TOP3_POOR_RATE = rs.getFloat("TOP3_POOR_RATE");
						agd.TOP3DIS = rs.getFloat("TOP3DIS");
						if(engdataMap.get(top3eci)!=null){
							EngineerData ed = engdataMap.get(top3eci);
							agd.TOP3NAME = ed.def_cellname_chinese;
							agd.TOP3DIR = ed.dir;
							agd.TOP3HEIGHT = ed.height;
							agd.TOP3TILT = ed.tilt;
							agd.TOP3BAND = ed.band;
						}
						agd.SUGGEST = suggestMap.get(CITY + SPLITER + GRID_ID);
						map.put(CITY + SPLITER + GRID_ID, agd);
					} catch (SQLException e) {
						invalid++;
						logger.info("Load anaGridData ResultSet error:\n" + e.getMessage());
						continue;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Load anaGridData ResultSet->next() error:\n" + e.getMessage());
			}
		}
		long e = System.currentTimeMillis();
		logger.info("load anaGridData data: valid " + (all - invalid) + " total " + all + " use "
				+ String.format("%.3f", 1e-3 * (e - b)) + " sec");
		return map;
	}

}
