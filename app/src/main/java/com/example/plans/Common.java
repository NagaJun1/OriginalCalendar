package com.example.plans;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import java.time.LocalDateTime;
import java.util.Calendar;

public class Common {
    /**
     * not found view のテキスト
     */
    public static final String NOT_FOUND_VIEW = "not found view";

    /**
     * 通知のチャネルID
     */
    public static final String CHANNEL_ID = "CHANNEL_PLANS";

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
     * 引数の文字列が null、もしくは空か
     *
     * @param str 文字列
     * @return true/false
     */
    public static boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 日付を規定フォーマットの文字列に修正
     *
     * @param year  年
     * @param month 月
     * @param day   日付
     * @return yyyy, mm, dd のフォーマットの文字列
     */
    public static String getStrDate(int year, int month, int day) {
        String strYear = String.valueOf(year);
        String strMonth = String.valueOf(month);
        String strDay = String.valueOf(day);
        return strYear + COMMA + strMonth + COMMA + strDay;
    }

    /**
     * 年月日から、「年」だけを取得
     *
     * @param date 年月日
     * @return 年数
     */
    public static int getYearInDate(String date) {
        return getIntInDate(date, 0);
    }

    /**
     * 年月日から、「年」だけを取得
     *
     * @param date 年月日
     * @return 月数
     */
    public static int getMonthInDate(String date) {
        return getIntInDate(date, 1);
    }

    /**
     * 年月日から、「年」だけを取得
     *
     * @param date 年月日
     * @return 日数
     */
    public static int getDayInDate(String date) {
        return getIntInDate(date, 2);
    }

    /**
     * 年月日のなかから、indexに該当する数値を取得
     *
     * @param date  年月日
     * @param index 「年月日」のいずれかに該当する数値
     * @return 取得された数値
     */
    private static int getIntInDate(String date, int index) {
        String[] dates = date.split(COMMA);
        if (index < dates.length) {
            return Integer.parseInt(dates[index]);
        } else {
            System.out.println("規定の文字列ではない");
            return 0;
        }
    }

    /**
     * 時刻の規定フォーマットを作成
     *
     * @param hour    時
     * @param minutes 分
     * @return hh:mm のフォーマットを作成
     */
    public static String getStrTime(int hour, int minutes) {
        return hour + COLON + minutes;
    }

    /**
     * 時刻文字列から、時間だけを取得
     *
     * @param time 時:分の文字列
     * @return 時間
     */
    public static int getHourInTime(String time) {
        return getIntInTime(time, 0);
    }

    /**
     * 時刻文字列から、「分」だけを取得
     *
     * @param time 時:分の文字列
     * @return 分
     */
    public static int getMinutesInTime(String time) {
        return getIntInTime(time, 1);
    }

    /**
     * 時刻文字列から、indexに該当する数値を取得する
     *
     * @param time  時刻文字列
     * @param index 取得する数値に対応する数値
     * @return 取得された数値
     */
    private static int getIntInTime(String time, int index) {
        String[] times = time.split(COLON);
        if (index < times.length) {
            return Integer.parseInt(times[index]);
        } else {
            System.out.println("規定の文字列ではない");
            return 0;
        }
    }

    /**
     * 空の文字列
     */
    private static final String STRING_EMPTY = "";

    /**
     * 引数の文字列から、前方十文字だけを取得
     * （改行が含まれる場合は、それを切る）
     *
     * @param strText 処理を行う文字列
     * @return 処理後の文字列
     */
    public static String getTopTenChar(String strText) {
        if (isEmptyOrNull(strText)) {
            return STRING_EMPTY;
        } else {
            // 改行除去
            String newText = strText.split("\n")[0];

            // 10文字を超える場合は、超過分を除去
            if (10 < newText.length()) {
                newText = newText.substring(0, 10);
            }
            return newText;
        }
    }

    /**
     * calendar.getTimeInMillis()の実行
     *
     * @param strDay  年月日を結合した文字列
     * @param strTime 時:分を結合した文字列
     * @return calendar.getTimeInMillis()の結果
     */
    public static long getTimeInMillis(String strDay, String strTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                getYearInDate(strDay),
                // Calendarでは、月は本来の数値-1
                getMonthInDate(strDay) - 1,
                getDayInDate(strDay),
                getHourInTime(strTime),
                getMinutesInTime(strTime),
                0
        );
        return calendar.getTimeInMillis();
    }

    /**
     * アラーム処理に追加するリクエストコードの生成
     *
     * @param strDay  年月日を結合した文字列
     * @param strTime 時:分を結合した文字列
     * @return リクエストコード
     */
    public static int getRequestCode(String strDay, String strTime) {
        return getYearInDate(strDay) * 10 ^ 8
                + getMonthInDate(strDay) * 10 ^ 6
                + getDayInDate(strDay) * 10 ^ 4
                + getHourInTime(strTime) * 10 ^ 2
                + getMinutesInTime(strTime);
    }

    /**
     * 通知用のチャネル作成
     * （Android 8.0 以上では、チャネルがないと、通知できない）
     */
    @SuppressLint("NewApi")
    public static void createNotificationChannel(Context context) {
        // 端末に対して、チャネルを追加
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = notificationManager.getNotificationChannel(CHANNEL_ID);
        if (channel == null) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * 今日の日付の取得
     */
    @SuppressLint("NewApi")
    public static String getToday() {
        LocalDateTime localTime = LocalDateTime.now();
        return Common.getStrDate(localTime.getYear(), localTime.getMonth().getValue(), localTime.getDayOfMonth());
    }

    /**
     * 現在時刻を取得
     */
    @SuppressLint("NewApi")
    public static String getNowTime() {
        LocalDateTime localTime = LocalDateTime.now();
        return Common.getStrTime(localTime.getHour(), localTime.getMinute());
    }
}
