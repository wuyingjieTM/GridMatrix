package com.tuoming.lteanalysis.GridMatrix.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Helper {
	
	static public String GetFmtDate(String daytime, int offset) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式

		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(df.parse(daytime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		calendar.add(Calendar.DAY_OF_MONTH, offset);
		Date date1 = calendar.getTime();
		String time = df.format(date1);
		// System.out.println(time);
		return time;
	}
	
	//判断字符是否为null或者空字符
	public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

}
