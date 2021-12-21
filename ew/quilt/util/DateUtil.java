package ew.quilt.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final DateFormat WHOLE = new SimpleDateFormat("yyyy 年 MM 月 dd 日 EE aa hh 點 mm 分 ss 秒");
    private static final DateFormat DATE = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
    private static final DateFormat DATE_AND_WEEK = new SimpleDateFormat("yyyy 年 MM 月 dd 日 EE");
    private static final DateFormat WEEK = new SimpleDateFormat("EE");
    private static final DateFormat TASK = new SimpleDateFormat("HHmmss");

    private static final DateFormat REG_DATE = new SimpleDateFormat("yyyy-MM-dd");

    public static String getWholeDate() {
        return WHOLE.format(new Date());
    }

    public static String getDate() {
        return DATE.format(new Date());
    }

    public static String getDateAndWeek() {
        return DATE_AND_WEEK.format(new Date());
    }

    public static String getWeek() {
        return WEEK.format(new Date());
    }

    public static String getTask() {
        return TASK.format(new Date());
    }

    public static String getRegDate() {
        return REG_DATE.format(new Date());
    }

    public static String getReadableMillis(long startMillis, long endMillis) {
        StringBuilder sb = new StringBuilder();
        double elapsedSecond = (endMillis - startMillis) / 1000D;
        int elapsedSec = ((int) elapsedSecond) % 60;
        int elapsedMinute = (int) (elapsedSecond / 60D);
        int elapsedMin = elapsedMinute % 60;
        int elapsedHr = elapsedMinute / 60;
        int elapsedHour = elapsedHr % 24;
        int elapsedDay = elapsedHr / 24;
        if (elapsedDay > 0) {
            boolean min = elapsedHour > 0;
            sb.append(elapsedDay);
            sb.append(" 天 ");
            if (min) {
                boolean sec = elapsedMin > 0;
                sb.append(elapsedHour);
                sb.append(" 小時 ");
                if (sec) {
                    boolean millis = elapsedSec > 0;
                    sb.append(elapsedMin);
                    sb.append(" 分 ");
                    if (millis) {
                        sb.append(elapsedSec);
                        sb.append(" 秒");
                    }
                }
            }
        } else if (elapsedHour > 0) {
            boolean min = elapsedMin > 0;
            sb.append(elapsedHour);
            sb.append(" 小時 ");
            if (min) {
                boolean sec = elapsedSec > 0;
                sb.append(elapsedMin);
                sb.append(" 分 ");
                if (sec) {
                    sb.append(elapsedSec);
                    sb.append(" 秒");
                }
            }
        } else if (elapsedMinute > 0) {
            boolean sec = elapsedSec > 0;
            sb.append(elapsedMinute);
            sb.append(" 分 ");
            if (sec) {
                sb.append(elapsedSec);
                sb.append(" 秒");
            }
        } else if (elapsedSecond > 0) {
            sb.append((int) elapsedSecond);
            sb.append(" 秒");
        } else {
            sb.append("無");
        }
        return sb.toString();
    }
}
