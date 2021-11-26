package com.example.originalcalendar;

public class Common {
    /**
     * コンマの固定文字列
     */
    private static final String COMMA = ",";

    /**
     * コロンの固定文字列
     */
    private static final String COLON = ":";

    /**
     * 一週間分の曜日の日本語配列
     */
    public static final String[] ONE_WEEK = {"error", "日", "月", "火", "水", "木", "金", "土"};

    /**
     * 時刻に設定する 0 の固定値
     */
    public static final String TIME_ZERO = "0";

    /**
     * 引数の文字列が null、もしくは空か
     * @param str 文字列
     * @return true/false
     */
    public static boolean isEmptyOrNull(String str){
        return str == null || str.isEmpty();
    }

    /**
     * 日付を規定フォーマットの文字列に修正
     * @param year 年
     * @param month 月
     * @param day 日付
     * @return yyyy,mm,dd のフォーマットの文字列
     */
    public static String getStrDate(int year, int month, int day) {
        String strYear = String.valueOf(year);
        String strMonth = String.valueOf(month);
        String strDay = String.valueOf(day);
        return strYear + COMMA + strMonth + COMMA + strDay;
    }

    /**
     * 年月日から、「年」だけを取得
     * @param date 年月日
     * @return 年数
     */
    public static int getYearInDate(String date){
        return getIntInDate(date,0);
    }

    /**
     * 年月日から、「年」だけを取得
     * @param date 年月日
     * @return 月数
     */
    public static int getMonthInDate(String date){
        return getIntInDate(date,1);
    }

    /**
     * 年月日から、「年」だけを取得
     * @param date 年月日
     * @return 日数
     */
    public static int getDayInDate(String date) {
        return getIntInDate(date,2);
    }

    /**
     * 年月日のなかから、indexに該当する数値を取得
     * @param date 年月日
     * @param index 「年月日」のいずれかに該当する数値
     * @return 取得された数値
     */
    private static int getIntInDate(String date, int index){
        String[] dates = date.split(COMMA);
        if(index < dates.length){
            return Integer.parseInt(dates[index]);
        }else {
            System.out.println("規定の文字列ではない");
            return 0;
        }
    }

    /**
     * 時刻の規定フォーマットを作成
     * @param hour 時
     * @param minutes 分
     * @return hh:mm のフォーマットを作成
     */
    public static String getStrTime(int hour,int minutes){
        return hour + COLON + minutes;
    }

    /**
     * 時刻文字列から、時間だけを取得
     * @param time 時:分の文字列
     * @return 時間
     */
    public static int getHourInTime(String time) {
        return getIntInTime(time,0);
    }

    /**
     * 時刻文字列から、「分」だけを取得
     * @param time 時:分の文字列
     * @return 分
     */
    public static int getMinutesInTime(String time) {
        return getIntInTime(time,1);
    }

    /**
     * 時刻文字列から、indexに該当する数値を取得する
     * @param time 時刻文字列
     * @param index 取得する数値に対応する数値
     * @return 取得された数値
     */
    private static int getIntInTime(String time,int index){
        String[] times = time.split(COLON);
        if(index < times.length){
            return Integer.parseInt(times[index]);
        }else {
            System.out.println("規定の文字列ではない");
            return 0;
        }
    }
}
