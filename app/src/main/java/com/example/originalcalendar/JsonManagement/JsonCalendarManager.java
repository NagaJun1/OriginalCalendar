package com.example.originalcalendar.JsonManagement;

import android.content.Context;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

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
        public
        Map<String,ValuesWithDay> dayAndValues = new HashMap<>();
    }

    /**
     * 日付・曜日に紐づく JSON
     */
    public static class ValuesWithDay {
        /**
         * 時間に紐づく値
         */
        public Map<String,ValuesWithTime> timeAndValues = new HashMap<>();
    }

    /**
     * 時刻に紐づく JSON
     */
    public static class ValuesWithTime {
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

        // 日付に紐づく各値を取得
        ValuesWithDay valuesWithDay = json.dayAndValues.get(strDay);

        // 日付の一致するJSONが取得されなかった場合
        if(valuesWithDay == null) {
            valuesWithDay = new ValuesWithDay();
        }

        // 時刻に紐づけて、メモを挿入
        ValuesWithTime newValuesWithTime = new ValuesWithTime();
        newValuesWithTime.memo = text;
        valuesWithDay.timeAndValues.put(strTime,newValuesWithTime);

        // 元のMap
        json.dayAndValues.put(strDay,valuesWithDay);

        // ファイルに保存
        saveInFile(context, json);
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
