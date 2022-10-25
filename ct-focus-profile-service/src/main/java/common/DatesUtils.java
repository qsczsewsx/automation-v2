package common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DatesUtils {

  public static String covertDateToString(Timestamp ts) {
    String dateString = "";
    if (ts.toString() != null) {
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      Date date = new Date(ts.getTime());
      dateString = format.format(date);
    }
    return dateString;
  }

  public static String covertDateToStringWithFormat(Timestamp ts, String formatValue) {
    String dateString = "";
    if (ts.toString() != null) {
      SimpleDateFormat format = new SimpleDateFormat(formatValue);
      Date date = new Date(ts.getTime());
      dateString = format.format(date);
    }
    return dateString;
  }

  public static Timestamp convertStringToTimestamp(String value) {

    return Timestamp.valueOf(
      LocalDateTime.parse(value,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  }

  public static Timestamp convertStringtoTimestampDayMonthYear(String value) {
    return Timestamp.valueOf(
      LocalDateTime.parse(value,
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
  }

  public static String convertTimestampToString(String value) {

    String dateString;
    Timestamp ts = Timestamp.valueOf(
      LocalDateTime.parse(value,
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date(ts.getTime());
    dateString = format.format(date);

    return dateString;
  }

  public static String covertTimeStampToStringDate(Timestamp ts) {
    SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date d = new Date(ts.getTime());

    return output.format(d);
  }

  public static String convertTimestampToStringWithFormat(Object value, String formatValue) {

    String dateString;
    Timestamp ts = Timestamp.valueOf(
      LocalDateTime.parse(value.toString(),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
    SimpleDateFormat format = new SimpleDateFormat(formatValue);
    Date date = new Date(ts.getTime());
    dateString = format.format(date);

    return dateString;
  }

  public static String convertTimestampToStringWithFormat(Timestamp ts, String formatValue) {

    String dateString;
    SimpleDateFormat format = new SimpleDateFormat(formatValue);
    Date date = new Date(ts.getTime());
    dateString = format.format(date);

    return dateString;
  }

  public static String convertStringToDateString(String value) {

    String dateString;
    Timestamp ts = Timestamp.valueOf(LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date(ts.getTime());
    dateString = format.format(date);
    return dateString;
  }

  public static Timestamp getDatePlusMinutes(Timestamp currentTime, long milis) {

    long current = currentTime.getTime();
    return new Timestamp(current + milis);
  }

  public static Timestamp getTimeMinusMinutes(Timestamp currentTime, Timestamp beforeTime) {
    long current = currentTime.getTime();
    long before = beforeTime.getTime();

    return new Timestamp(current - before);
  }

  public static long getSubtractionMinutes(Timestamp currentTime, Timestamp beforeTime) {
    long current = currentTime.getTime();
    long before = beforeTime.getTime();

    return current - before;
  }

  public static int getDayOfWeek(Timestamp ts) {
    Calendar c = Calendar.getInstance();
    c.setTime(ts);

    return c.get(Calendar.DAY_OF_WEEK);
  }

  public static String addPlusDay(String currentDate, int day) {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance();
    try {
      c.setTime(sdf.parse(currentDate));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    c.add(Calendar.DATE, day);
    return sdf.format(c.getTime());
  }


}
