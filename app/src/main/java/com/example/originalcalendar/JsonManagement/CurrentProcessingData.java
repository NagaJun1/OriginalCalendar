package com.example.originalcalendar.JsonManagement;

import android.content.Context;
import android.widget.Toast;

import com.example.originalcalendar.Common;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;

/**
 * Intent の変わりのロジック
 * 現行画面で使用されている情報
 */
public class CurrentProcessingData {
    /**
     * このクラスで使用する JSON を保存するファイル
     */
    private static final String CURRENT_PROCESS_DATA_FILE = "CURRENT_PROCESS_DATA_FILE.json";

    /**
     * 現行処理上で、使用されるデータ
     */
    public static class JsonCurrentProcessData{
        /**
         * 年月日の文字列
         */
        public String strDate = null;
        /**
         * 時刻
         */
        public String strTime = null;
        /**
         * 曜日
         */
        public int intDayOfWeek = 0;
        /**
         * メモ編集画面を既に開いているかの判定
         */
        public Boolean alreadyOpenEditMemo = false;
        /**
         * アラーム編集画面を既に開いているかの判定
         */
        public Boolean alreadyOpenEditAlarm = false;
        /**
         * メモに紐づくタグ
         */
        public String strTag = null;
    }

    /**
     * CURRENT_PROCESS_DATA_FILE に年月日を保存
     */
    public static void setDate(Context context, String date){
        JsonCurrentProcessData json = getJSON(context);
        json.strDate = date;
        writeSaveFile(context, json);
    }

    /**
     * CURRENT_PROCESS_DATA_FILE に時刻を保存
     */
    public static void setTime(Context context, String time){
        JsonCurrentProcessData json = getJSON(context);
        json.strTime = time;
        writeSaveFile(context, json);
    }

    /**
     * CURRENT_PROCESS_DATA_FILE に曜日を保存
     */
    public static void setDayOfWeek(Context context, int dayOfWeek){
        JsonCurrentProcessData json = getJSON(context);
        json.intDayOfWeek = dayOfWeek;
        writeSaveFile(context, json);
    }

    /**
     * CURRENT_PROCESS_DATA_FILE に曜日を保存
     */
    public static void setTag(Context context, String tag){
        JsonCurrentProcessData json = getJSON(context);
        json.strTag = tag;
        writeSaveFile(context, json);
    }

    /**
     * メモ編集画面が開かれているかのフラグを設定
     */
    public static void setOpenEditMemo(Context context, boolean openEditMemo){
        JsonCurrentProcessData json = getJSON(context);
        json.alreadyOpenEditMemo = openEditMemo;
        writeSaveFile(context, json);
    }

    /**
     * アラーム編集画面が開かれているかのフラグを設定
     */
    public static void setOpenEditAlarm(Context context,boolean openEditAlarm){
        JsonCurrentProcessData json = getJSON(context);
        json.alreadyOpenEditAlarm = openEditAlarm;
        writeSaveFile(context, json);
    }

    /**
     * CURRENT_PROCESS_DATA_FILE.json 内の JSONを取得する
     * @param context 処理の実行画面の Context
     * @return ファイルから抽出した CurrentProcessData
     */
    public static JsonCurrentProcessData getJSON(Context context){
        try {
            String dirPath = context.getFilesDir().getAbsolutePath();
            File file = new File(dirPath + "\\" + CURRENT_PROCESS_DATA_FILE);
            if (file.exists()) {
                // ファイルの中身を JSON用のクラスに変換して取得
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(file, JsonCurrentProcessData.class);
            }
        }catch (Exception e){
            System.out.println(e.toString());
            Toast.makeText(context,"CURRENT_PROCESS_DATA_FILE.json の読み込みに失敗",Toast.LENGTH_LONG).show();
        }
        return new JsonCurrentProcessData();
    }

    /**
     * CURRENT_PROCESS_DATA_FILE.json に JSONを書き込み
     * @param context 処理の実行画面の Context
     * @param data JSON 用のクラス
     */
    private static void writeSaveFile(Context context, JsonCurrentProcessData data){
        String dirPath = context.getFilesDir().getAbsolutePath();
        String filePath = dirPath + "\\" + CURRENT_PROCESS_DATA_FILE;
        try(FileWriter writer = new FileWriter(filePath)) {
            // CurrentProcessData を JSON 文字列に変換して、ファイルに書き込み
            ObjectMapper mapper = new ObjectMapper();
            String strJson = mapper.writeValueAsString(data);
            writer.write(strJson);
        }catch (Exception e){
            Toast.makeText(context,"CURRENT_PROCESS_DATA_FILE.jsonの書き込みに失敗",Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
        }
    }

    /**
     * CURRENT_PROCESS_DATA_FILE.json の中の JSON のプロパティを初期状態にリセット
     */
    public static void resetCurrentProcessData(Context context){
        writeSaveFile(context, new JsonCurrentProcessData());
    }
}
