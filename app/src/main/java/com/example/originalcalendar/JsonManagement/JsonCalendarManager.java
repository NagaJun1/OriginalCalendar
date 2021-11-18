package com.example.originalcalendar.JsonManagement;

import android.content.Context;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * JsonManagerのプロパティとしてのクラスを一覧として保持
 */
public class JsonCalendarManager {
    /**
     * カレンダーの関連データを保存するファイル名
     */
    private static final String CALENDAR_DATA_FILE = "calendar_data.json";

    /**
     * カレンダーとしてのデータを全て持つ JSON と互換のあるクラス
     */
    public static class JsonCalendarDate {
        /**
         * 日付、曜日に紐づく情報一覧
         */
        public List<Day> days = new ArrayList<Day>();
    }

    /**
     * 一日分の JSON
     */
    public static class Day {
        /**
         * 日付、もしくは、曜日（1~7）
         */
        public String day = null;

        /**
         * 時刻に紐づく JSON (配列）
         */
        public List<ByDay> times = new ArrayList<ByDay>();
    }

    /**
     * 日付・曜日に紐づく JSON
     */
    public static class ByDay {
        /**
         * 日付（文字列）
         */
        public String time = null;

        /**
         * 時間に紐づく JSON
         */
        public ByTime byTime = new ByTime();
    }

    /**
     * 時刻に紐づく JSON
     */
    public static class ByTime {
        /**
         * メモのテキスト情報
         */
        public String memo = null;
        /**
         * アラームON/OFF のフラグ
         */
        public boolean flgAlarm = true;
        /**
         * 通知ON/OFF のフラグ
         */
        public boolean flgNotice = true;
        /**
         * リピートON/OFF のフラグ
         */
        public boolean flgRepeat = false;
    }

    /**
     * ローカルファイルにデータを追加
     * @param context 処理画面のContext
     * @param strDay 処理対象の日付・曜日
     * @param strTime 処理対象の時刻
     * @param text 格納したい文章
     */
    public static void setMemoDate(Context context,String strDay,String strTime,String text){
        // 時刻が設定されていない場合は、時刻は"0"で設定
        if(strTime == null || strTime.length()==0){
            strTime = "0";
        }
        // ローカルファイルに保存されているJSONを取得
        JsonCalendarDate json = getJson(context);

        // 日付が一致するJSONの格納先
        Day dayData = null;

        // 日付に紐づく情報を取得
        for (int i = 0;i < json.days.size();i++){
            // 日付・曜日が一致するデータを取得し、json内から削除する
            if(json.days.get(i).day.equals(strDay)) {
                dayData = json.days.get(i);
                json.days.remove(i);
                break;
            }
        }
        // 日付の一致するJSONが取得されなかった場合
        if(dayData == null) {
            dayData = new Day();
            dayData.day = strDay;
        }
        // 取得された dayDataに情報を埋め込み -> 処理結果を json に格納
        Day newDayDate = setMemoDateInDay(dayData, strTime, text);
        json.days.add(newDayDate);

        // ファイルに保存
        saveInFile(context,json);
    }

    /**
     * 引数の dayに対し、timeと一致する情報を埋め込む
     */
    private static Day setMemoDateInDay(Day day,String time,String text){
        if(day == null){
            System.out.println("例外\nDayがnullです\nsetMemoDateInDay()");
            return null;
        }
        // 検索結果の格納先
        ByDay byDay = null;
        for(int i = 0; i < day.times.size(); i++){
            // 時刻が一致する情報はローカル変数に格納し、day.time からは削除（情報が重複するため）
            if(day.times.get(i).time.equals(time)){
                byDay = day.times.get(i);
                day.times.remove(i);
                break;
            }
        }
        // 検索結果が取得されなかった場合は、新規作成
        if(byDay == null){
            byDay = new ByDay();
        }
        // 埋め込む情報の書き込み
        byDay.time = time;
        byDay.byTime.memo = text;
        day.times.add(byDay);
        return day;
    }

    /**
     * 保存データの書き換え（全て上書き）
     * @param context 処理の実行画面のContext
     * @param jsonCalendar ファイルに書き込むテキストデータ
     */
    private static void saveInFile(Context context, JsonCalendarDate jsonCalendar) {
        try (FileWriter writer = new FileWriter(context.getFilesDir().getAbsolutePath() + "\\" + CALENDAR_DATA_FILE)) {
            ObjectMapper mapper = new ObjectMapper();
            String strJson = mapper.writeValueAsString(jsonCalendar);
            writer.write(strJson);
        } catch (Exception e) {
            Toast.makeText(context, "メモの書き込みに失敗しました。", Toast.LENGTH_SHORT).show();
            System.out.println(e.toString());
        }
    }

    /**
     * 保存データの読み込み（全て読み込み）
     * @param context 処理の実行画面の Context
     * @return 読み込んだデータ
     */
    public static JsonCalendarDate getJson(Context context) {
        try {
            String dirPath = context.getFilesDir().getAbsolutePath();
            File file = new File(dirPath + "\\" + CALENDAR_DATA_FILE);

            // 読み込むファイルが存在しない場合は処理しない
            if (file.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(file,JsonCalendarDate.class);
            }
        } catch (Exception e) {
            Toast.makeText(context, "メモの取得に失敗しました。", Toast.LENGTH_SHORT).show();
            System.out.println(e.toString());
        }
        return new JsonCalendarDate();
    }
}
