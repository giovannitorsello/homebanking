package util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtil {

    public static LocalDate convertDateToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertLocalDateToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date convertStringHourMinutesToDate(String strHour) {  // in hh:mm format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            return sdf.parse(strHour+":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Time convertStringHourMinutesToTime(String strHour) {  // in hh:mm format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date d=sdf.parse(strHour+":00");
            return new Time(d.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
