package seung.kimchi.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;

import com.ibm.icu.util.ChineseCalendar;

import seung.kimchi.core.types.SException;

public class SDate {

	public static final String _S_MILLISECOND = "ms";
	public static final String _S_SECOND = "s";
	public static final String _S_MINUTE = "m";
	public static final String _S_HOUR = "h";
	public static final String _S_WEEK = "w";
	public static final String _S_DAY = "day";
	public static final String _S_MONTH = "month";
	public static final String _S_YEAR = "year";
	
	public static final int _S_MONDAY_ISO_8601 = 1;
	public static final int _S_TUESDAY_ISO_8601 = 2;
	public static final int _S_WEDNESDAY_ISO_8601 = 3;
	public static final int _S_THURSDAY_ISO_8601 = 4;
	public static final int _S_FRIDAY_ISO_8601 = 5;
	public static final int _S_SATURDAY_ISO_8601 = 6;
	public static final int _S_SUNDAY_ISO_8601 = 7;
	
	public static final String[] _S_HOLIDAYS_KR_SOLAR = {
			"01-01"
			, "03-01"
			, "05-05"
			, "06-06"
			, "08-15"
			, "10-03"
			, "10-09"
			, "12-25"
	};
	
	public static final String[] _S_HOLIDAYS_KR_LUNAR = {
			"12-31"
			, "01-01"
			, "01-02"
			, "04-08"
			, "08-14"
			, "08-15"
			, "08-16"
	};
	
	public static String epoch() {
		return Long.toString(System.currentTimeMillis());
	}// end of epoch
	
	public static String format(
			final String pattern
			, final Date date
			, final TimeZone time_zone
			, final Locale locale
			) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
		simpleDateFormat.setTimeZone(time_zone);
		return simpleDateFormat.format(date);
	}// end of format
	public static String format(
			final String pattern
			, final Date date
			, final TimeZone time_zone
			) {
		return format(pattern, date, time_zone, Locale.getDefault());
	}// end of format
	public static String format(
			final String pattern
			, final Date date
			, final String time_zone
			) {
		return format(pattern, date, TimeZone.getTimeZone(time_zone));
	}// end of format
	public static String format(
			final String pattern
			, final Date date
			) {
		return format(pattern, date, TimeZone.getDefault());
	}// end of format
	public static String format(
			final String pattern
			) {
		return format(pattern, new Date());
	}// end of format
	public static String format(final Date date) {
		return format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", date, TimeZone.getDefault());
	}// end of format
	public static String format() {
		return format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", new Date(), TimeZone.getDefault());
	}// end of format
	
	public static String format(
			String pattern_post
			, String pattern_prev
			, String date
			) {
		return LocalDate.parse(
				date//text
				, DateTimeFormatter.ofPattern(pattern_prev)//formatter
				)
				.format(DateTimeFormatter.ofPattern(pattern_post));
	}// end of format
	
	public static Date date(
			final String date
			, final String pattern
			, final TimeZone time_zone
			, final Locale locale
			) throws SException {
		
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
			simpleDateFormat.setTimeZone(time_zone);
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			throw new SException(e, "[ParseException] Failed to convert to date.");
		}// end of try
		
	}// end of date
	public static Date date(
			final String date
			, final String pattern
			, final TimeZone time_zone
			) throws SException {
		return date(date, pattern, time_zone, Locale.getDefault());
	}// end of date
	public static Date date(
			final String date
			, final String pattern
			) throws SException {
		return date(date, pattern, TimeZone.getDefault(), Locale.getDefault());
	}// end of date
	
	public static int day_of_week_iso_8601(LocalDate date) {
		return date.getDayOfWeek().getValue();
	}// end of day_of_week_iso_8601
	public static int day_of_week_iso_8601() {
		return day_of_week_iso_8601(LocalDate.now());
	}// end of day_of_week_iso_8601
	
	public static boolean is_weekend(LocalDate date) {
		int day_of_week_iso_8601 = day_of_week_iso_8601(date);
		if(day_of_week_iso_8601 == _S_SATURDAY_ISO_8601
				|| day_of_week_iso_8601 == _S_SUNDAY_ISO_8601
				) {
			return true;
		}
		return false;
	}// end of is_weekend
	public static boolean is_weekend() {
		return is_weekend(LocalDate.now());
	}// end of is_weekend
	
	public static boolean is_holiday(LocalDate date) {
		
		boolean is_holiday = false;
		
		while(true) {
			
			if(is_weekend(date)) {
				is_holiday = true;
				break;
			}
			
			String mm_dd = SDate.format("MM-dd", new Date());
			for(String holiday : _S_HOLIDAYS_KR_SOLAR) {
				if(mm_dd.equals(holiday)) {
					is_holiday = true;
					break;
				}
			}// end of solar
			if(is_holiday) {
				break;
			}
			
			mm_dd = lunar_date_format("MM-dd");
			for(String holiday : _S_HOLIDAYS_KR_LUNAR) {
				if(mm_dd.equals(holiday)) {
					is_holiday = true;
					break;
				}
			}// end of lunar
			
			break;
		}// end of while
		
		return is_holiday;
	}// end of is_holiday
	public static boolean is_holiday() {
		return is_holiday(LocalDate.now());
	}// end of is_holiday
	
	public static Long diff(final Date date_from, final Date date_to) {
		return Math.abs(date_to.getTime() - date_from.getTime());
	}// end of diff
	
	public static Date add(
			final Date date
			, final String time_unit
			, final int amount
			) {
		
		Date date_add = null;
		
		switch (time_unit) {
			case _S_MILLISECOND:
				date_add = DateUtils.addMilliseconds(date, amount);
				break;
			case _S_SECOND:
				date_add = DateUtils.addSeconds(date, amount);
				break;
			case _S_MINUTE:
				date_add = DateUtils.addMinutes(date, amount);
				break;
			case _S_HOUR:
				date_add = DateUtils.addHours(date, amount);
				break;
			case _S_WEEK:
				date_add = DateUtils.addWeeks(date, amount);
				break;
			case _S_DAY:
				date_add = DateUtils.addDays(date, amount);
				break;
			case _S_MONTH:
				date_add = DateUtils.addMonths(date, amount);
				break;
			case _S_YEAR:
				date_add = DateUtils.addYears(date, amount);
				break;
			default:
				break;
		}
		
		return date_add;
	}// end of add
	
	public static LocalDate lunar_date(LocalDate date) {
		ChineseCalendar chineseCalendar = new ChineseCalendar();
		chineseCalendar.set(ChineseCalendar.EXTENDED_YEAR, date.getYear() + 2637);
		chineseCalendar.set(ChineseCalendar.MONTH, date.getMonthValue() - 1);
		chineseCalendar.set(ChineseCalendar.DAY_OF_MONTH, date.getDayOfMonth());
		return Instant.ofEpochMilli(chineseCalendar.getTimeInMillis()).atZone(ZoneId.systemDefault()).toLocalDate();
	}// end of lunar_date
	public static LocalDate lunar_date() {
		return lunar_date(LocalDate.now());
	}// end of lunar_date
	
	public static String lunar_date_format(LocalDate date, String pattern) {
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}// end of lunar_date_format
	public static String lunar_date_format(String pattern) {
		return lunar_date_format(LocalDate.now(), pattern);
	}// end of lunar_date_format
	
}
