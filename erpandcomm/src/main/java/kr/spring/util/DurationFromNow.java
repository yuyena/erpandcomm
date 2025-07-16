package kr.spring.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DurationFromNow {
	/**
	 * 현재부터 "yyyyMMddHHmmss" 포맷의 날짜 차이 레이블
	 * @param date1
	 * @return String
	 */
	public static String getTimeDiffLabel(String date1) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			return getTimeDiffLabel(sdf.parse(date1), new Date());
		} catch (ParseException e) {
			return "-";
		}
	}

	/**
	 * 현재부터 Date 포맷의 날짜 차이 레이블
	 * @param d1
	 * @return String
	 */
	public static String getTimeDiffLabel(Date d1) {
		return getTimeDiffLabel(d1, new Date());
	}

	/**
	 * "yyyyMMddHHmmss" 포맷의 날짜 차이 레이블
	 * @param date1
	 * @param date2
	 * @return String
	 */
	public static String getTimeDiffLabel(String date1, String date2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");
		try {
			return getTimeDiffLabel(sdf.parse(date1), sdf.parse(date2));
		} catch (ParseException e) {
			return "-";
		}
	}

	/**
	 * java.util.Date 포맷의 날짜 차이 레이블
	 * @param d1
	 * @param d2
	 * @return String
	 */
	public static String getTimeDiffLabel(Date d1, Date d2) {
		// 항상 오전/오후 시간 형식으로 표시
		SimpleDateFormat timeFormat = new SimpleDateFormat("a h:mm");
		return timeFormat.format(d1);
	}
}