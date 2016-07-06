package com.gewara.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {
	
	/**
	 * format:yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static boolean matchDateFormat(String date){
		String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(date);
		return m.matches();
	}
	
	
	public static String toFormatString(Date date,String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);   
		return sdf.format(date);  
	}
	
	/**

	 * 计算两个时间相差多少天

	 * @param date1

	 * @param date2

	 * @return

	 */
	public static int differenceDay(Date date1,Date date2){
		Long num = date2.getTime() - date1.getTime();
		num = ((num/1000)/60/60/24);
		return num.intValue();
	}
	
	public static String format(Date date,String pattern){
		SimpleDateFormat changer = new SimpleDateFormat(pattern);
		return changer.format(date);
	}
	
	public static Date parse(String dateStr,String pattern) throws ParseException{
		SimpleDateFormat changer = new SimpleDateFormat(pattern);
		return changer.parse(dateStr);
	}
	
	public static boolean isSameDay(Date date1,Date date2){
		String str1 = format(date1, "yyyy-MM-dd");
		String str2 = format(date2, "yyyy-MM-dd");
		return str1.equals(str2);
	}
	
	public static Date addMinute(Date date,Integer amount){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, amount);
		return calendar.getTime();
	}
	
	public static Date addday(Date date,Integer amount){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, amount);
		return calendar.getTime();
	}
	
	public static Date getCurrentMonthStart(){
		Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
        return cal.getTime();
	}
	
	public static Date getCurrentMonthEnd(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
        return cal.getTime();
	}
	
	public static Date getCurrentYearStart(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.MONTH,cal.getActualMinimum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
        return cal.getTime();
	}
	
	public static Date getCurrentYearEnd(){
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.MONTH,cal.getActualMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
        return cal.getTime();
	}
	
	/**

	 * 获取当前小时开始时间

	 * @param date

	 * @param hourVaue

	 * @return

	 */
	public static Date getHourStart(Date date,Integer hourVaue){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,hourVaue);
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        return cal.getTime();
	}
	
	/**

	 * 获取当前小时结束时间

	 * @param date

	 * @param hourVaue

	 * @return

	 */
	public static Date getHourEnd(Date date,Integer hourVaue){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY,hourVaue);
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        return cal.getTime();
	}

	/**

	 * 获取当前天开始时间 00:00:00

	 * @param date

	 * @return

	 */
	public static Date getDayStart(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, cal.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
        return cal.getTime();
	}
	
	/**

	 * 获取当前天结束时间 23:59:59

	 * @param date

	 * @return

	 */
	public static Date getDayEnd(Date date){
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getActualMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
        return cal.getTime();
	}
	
	public static String[] getDateArray(String start_date,String end_date,String format){
		//query_day = new String[end_day.compareTo(start_day)+1];
		String[] query_day = null;
		try {
			Date start_day = DateUtils.parse(start_date, format);
			Date end_day = DateUtils.parse(end_date, format);
			query_day = new String[end_day.compareTo(start_day)+1];
			for(int i = 0;i<query_day.length;i++){
				query_day[i] = DateUtils.format(DateUtils.addday(start_day, i), format);
			}
			return query_day;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
