package com.example.originalcalendar.Content;

import java.util.Calendar;

public class Common {
    /**
     * コンマの固定文字列
     */
    public static final String COMMA = ",";

    /**
     * 曜日の日本語配列
     */
    public static final String[] DAY_OF_WEEK = {"error","日","月","火","水","木","金","土"};

    /**
     * 日付を8桁の文字列に修正
     * @return yyyy,mm,dd のフォーマットの文字列
     */
    public static String getStrDate(int year,int month,int day){
        String strYear = String.valueOf(year);
        String strMonth = String.valueOf(month);
        String strDay = String.valueOf(day);
        return strYear + COMMA + strMonth + COMMA + strDay;
    }

    /**
     * 日付から、曜日を取得
     * @param date 日付（年月日は、連結して文字列になっている）
     * @return 曜日に応じた数値
     */
    public static int getDayOfWeek(String date){
         String[] dates = date.split(COMMA);

         // 配列は {y,m,d} となるため、配列の長さは3である必要がある
         if(2 < dates.length){
             Calendar calendar = Calendar.getInstance();
             calendar.set(
                     Integer.parseInt(dates[0]),
                     Integer.parseInt(dates[1]),
                     Integer.parseInt(dates[2]));
             return calendar.get(Calendar.DAY_OF_WEEK);
         }
        return 0;
    }
}
