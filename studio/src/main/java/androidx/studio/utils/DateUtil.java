package androidx.studio.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public static long getMaxTime(long timeMillis) {
        Date date = new Date(timeMillis);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        Date maxDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return maxDate.getTime();
    }
}
