package seung.kimchi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;

public class SDate {

	public static final String _S_MILLISECOND = "ms";
	public static final String _S_SECOND = "s";
	public static final String _S_MINUTE = "m";
	public static final String _S_HOUR = "h";
	public static final String _S_WEEK = "w";
	public static final String _S_DAY = "day";
	public static final String _S_MONTH = "month";
	public static final String _S_YEAR = "year";
	
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
	
	public static Date date(
			final String date
			, final String pattern
			, final TimeZone time_zone
			, final Locale locale
			) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
		simpleDateFormat.setTimeZone(time_zone);
		return simpleDateFormat.parse(date);
	}// end of date
	public static Date date(
			final String date
			, final String pattern
			, final TimeZone time_zone
			) throws ParseException {
		return date(date, pattern, time_zone, Locale.getDefault());
	}// end of date
	public static Date date(
			final String date
			, final String pattern
			) throws ParseException {
		return date(date, pattern, TimeZone.getDefault(), Locale.getDefault());
	}// end of date
	
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
	
}
