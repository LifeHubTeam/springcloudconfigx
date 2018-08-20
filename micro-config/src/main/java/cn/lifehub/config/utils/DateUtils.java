package cn.lifehub.config.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public final static String DF_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public final static String DF_yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * yyyy-MM-dd
     */
    public final static String DF_yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * yyyy年MM月dd日
     */
    public final static String DF_yyyy_MM_dd2 = "yyyy年MM月dd日";

    /**
     * HH:mm:ss
     */
    public final static String DF_HH_mm_ss = "HH:mm:ss";
    /**
     * MM-dd
     */
    public final static String DF_MM_dd = "MM-dd";
    /**
     * HH:mm:ss.SSS
     */
    public final static String DF_HH_mm_ss_SSS = "HH:mm:ss.SSS";

    /**
     * yyyyMM
     */
    public final static String DF_yyyyMM = "yyyyMM";
    /**
     * yyyyMMddHHmmssSSS
     */
    public final static String DF_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";

    /**
     * yyyyMMddHHmmss
     */
    public final static String DF_yyyyMMddHHmmss = "yyyyMMddHHmmss";

    /**
     * MM-dd mm:ss
     */
    public final static String DF_MM_dd_HH_mm = "MM-dd HH:mm";

    /**
     * yyyy-MM-dd HH:mm
     */
    public final static String DF_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

    /**
     * yyyy年MM月dd日 HH:mm
     */
    public final static String DF_yyyy_MM_dd_HH_mm2 = "yyyy年MM月dd日 HH:mm";

    /**
     * yyyy年MM月dd日 HH:mm:ss
     */
    public final static String DF_yyyy_MM_dd_HH_mm3 = "yyyy年MM月dd日 HH:mm:ss";

    /**
     * yyyy年MM月dd日 HH:mm:ss
     */
    public final static String DF_MM_dd2 = "MM月dd日";

    /**
     * 得到unix时间戳
     *
     * @return
     */
    public static int getUnixTimeStamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }


    /**
     * 获取时间段内日期
     *
     * @param start
     * @param end
     * @return
     */
    public static List<Date> getDateListbetweenDate(Date start, Date end) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(start);
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(start);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(end);
        while (end.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            Date time = calBegin.getTime();
            if(!lDate.contains(time))
                lDate.add(time);
        }
        return lDate;
    }

    public static Object getWeekName(int dayOfWeek) {
        if (Calendar.MONDAY == dayOfWeek) {
            return "星期一";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            return "星期二";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            return "星期三";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            return "星期四";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            return "星期五";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            return "星期六";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            return "星期日";
        } else {
            return "日期错误";
        }
    }

    public static String getWeekDay(Date date){
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0)
            week = 0;
        return weekDays[week];
    }

    // 由日期字符串和日期格式获取日期对象
    public static Date dateString2Date(String dateStr, String datePatten) {
        SimpleDateFormat format = new SimpleDateFormat(datePatten);
        try {
            Date date = format.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期转换字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String date2Str(Date date,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     *时间戳转化为周几；
     */
    public static String timestamp2Dayweek(Integer timestamp){
        GregorianCalendar cal = new GregorianCalendar();
        long time = timestamp;
        cal.setTime(new Date(time*1000));
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println(timestamp*1000+"---------dayWeek-----"+dow);
        switch (dow) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
        }
        return "Unknown";
    }

    /**
     * 整数(秒数)转换为时分秒格式(xx:xx:xx)
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    // 由时间戳得到日期字符串
    public static String unixTimeStampInteger2DateString(Integer timeStamp, String format) {
        long l=(long)timeStamp.intValue()*1000;
        SimpleDateFormat datetimefmt = new SimpleDateFormat(format);
        Date date= new Date(l);
        String retStr = datetimefmt.format(date);
        return retStr;
    }

    // 由(Long)时间戳得到日期字符串
    public static String unixTimeStampLong2DateString(Long timeStamp, String format) {
        SimpleDateFormat datetimefmt = new SimpleDateFormat(format);
        Date date= new Date(timeStamp);
        String retStr = datetimefmt.format(date);
        return retStr;
    }

    // 由时间戳得到hh:mma格式
    public static String timeStampToHourString(Integer timeStamp) {
        long l=(long)timeStamp.intValue()*1000;
        SimpleDateFormat datetimefmt = new SimpleDateFormat("HH:mma", Locale.ENGLISH);
        Date date= new Date(l);
        String retStr = datetimefmt.format(date).toLowerCase();
        return retStr;
    }

    // 由时间戳得到日期字符串
    public static Date unixTimeStampInteger2Date(Integer timeStamp) {
        long l=(long)timeStamp.intValue()*1000;
        Date date= new Date(l);
        return date;
    }

    public static Date unixDataStringToDate(String dataString,String format) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date parsedDate = dateFormat.parse(dataString);
        return parsedDate;

    }

    /**
     * 两个date相差的秒数
     * @param d1
     * @param d2
     * @return
     */
    public static Integer differSecond(Date d1, Date d2){
        if (d1 == null || d2 == null) {
            return 0;
        }
        int second = (int)((d1.getTime() - d2.getTime())/1000);
        return second < 0 ? - second : second;
    }

    /**
     * 两个date相差的分钟数（向上取整）
     * @param d1
     * @param d2
     * @return
     */
    public static Integer differMinuteUp(Date d1, Date d2){
        if (d1 == null || d2 == null) {
            return 0;
        }
        long time = d1.getTime() - d2.getTime();
        time = time < 0 ? - time : time;
        double second = (double)(time / 1000);
        return (int)(Math.ceil(second/60));
    }

    /**
     * 两个date相差的分钟数（向下取整）
     * @param d1
     * @param d2
     * @return
     */
    public static Integer differMinuteDown(Date d1, Date d2){
        if (d1 == null || d2 == null) {
            return 0;
        }
        long time = d1.getTime() - d2.getTime();
        time = time < 0 ? - time : time;
        int second = (int)(time / 1000);
        return second/60;
    }

    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }

    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.before(d2)) ? d1 : d2;
    }
    /**
     * 按指定的字符串格式将字符串型日期转化为java.util.Date型日期
     *
     * @param dateFormatType
     *            "yyyy-MM-dd" 或者 "yyyy-MM-dd hh:mm:ss"
     * @return java.util.Date型日期
     * @Param dateStr 字符型日期
     */
    public static Date turnStrDateToJavaUtilDate(String strDate, String dateFormatType) {

        Date javaUtilDate = null;
        DateFormat df = new SimpleDateFormat(dateFormatType);
        if (strDate != null && (!"".equals(strDate)) && dateFormatType != null && (!"".equals(dateFormatType))) {
            try {

                javaUtilDate = df.parse(strDate);
            } catch (ParseException e) {
                logger.error(e.getMessage());
            }
        }
        return javaUtilDate;
    }




}
