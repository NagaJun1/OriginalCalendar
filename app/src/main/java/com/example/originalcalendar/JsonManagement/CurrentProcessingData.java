package com.example.originalcalendar.JsonManagement;

import android.content.Context;
import android.widget.Toast;

import com.example.originalcalendar.Common;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;

import lombok.Getter;

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
    @Getter
    public static class JsonCurrentProcessData {
        /**
         * 年月日の文字列
         */
        private String strDate = null;

        /**
         * 時刻
         */
        private String strTime = null;

        /**
         * 曜日
         */
        private int intDayOfWeek = 0;

        /**
         * メモ編集画面を既に開いているかの判定
         */
        private Boolean alreadyOpenEditMemo = false;

        /**
         * アラーム編集画面を既に開いているかの判定
         */
        private Boolean alreadyOpenEditAlarm = false;

        /**
         * メモに紐づくタグ
         */
        private String strTag = null;

        /**
         * 設定されている日付（年月日 or 曜日）を取得
         * memo : getDay() にすると、JSON 内に"day"が作成される
         */
        public String strDay() {
            if (!Common.isEmptyOrNull(strDate)) {
                return strDate;
            } else if (intDayOfWeek != 0) {
                return Integer.toString(intDayOfWeek);
            } else {
                return null;
            }
        }

        /**
         * CURRENT_PROCESS_DATA_FILE.json に JSONを書き込み
         *
         * @param context 処理の実行画面の Context
         */
        private void writeSaveFile(Context context) {
            String dirPath = context.getFilesDir().getAbsolutePath();
            String filePath = dirPath + "\\" + CURRENT_PROCESS_DATA_FILE;
            try (FileWriter writer = new FileWriter(filePath)) {
                // CurrentProcessData を JSON 文字列に変換して、ファイルに書き込み
                ObjectMapper mapper = new ObjectMapper();
                String strJson = mapper.writeValueAsString(this);
                writer.write(strJson);
            } catch (Exception e) {
                Toast.makeText(context, "CURRENT_PROCESS_DATA_FILE.jsonの書き込みに失敗", Toast.LENGTH_LONG).show();
                System.out.println(e.toString());
            }
        }

        /**
         * CURRENT_PROCESS_DATA_FILE に年月日を保存
         */
        public void setDate(Context context, String date) {
            strDate = date;
            writeSaveFile(context);
        }

        /**
         * CURRENT_PROCESS_DATA_FILE に時刻を保存
         */
        public void setTime(Context context, String time) {
            strTime = time;
            writeSaveFile(context);
        }

        /**
         * CURRENT_PROCESS_DATA_FILE に曜日を保存
         */
        public void setDayOfWeek(Context context, int dayOfWeek) {
            intDayOfWeek = dayOfWeek;
            writeSaveFile(context);
        }

        /**
         * CURRENT_PROCESS_DATA_FILE に曜日を保存
         */
        public void setTag(Context context, String tag) {
            strTag = tag;
            writeSaveFile(context);
        }

        /**
         * メモ編集画面が開かれているかのフラグを設定
         */
        public void setOpenEditMemo(Context context, boolean openEditMemo) {
            alreadyOpenEditMemo = openEditMemo;
            writeSaveFile(context);
        }

        /**
         * アラーム編集画面が開かれているかのフラグを設定
         */
        public void setOpenEditAlarm(Context context, boolean openEditAlarm) {
            alreadyOpenEditAlarm = openEditAlarm;
            writeSaveFile(context);
        }
    }

    /**
     * CURRENT_PROCESS_DATA_FILE.json 内の JSONを取得する
     *
     * @param context 処理の実行画面の Context
     * @return ファイルから抽出した CurrentProcessData
     */
    public static JsonCurrentProcessData getJSON(Context context) {
        try {
            String dirPath = context.getFilesDir().getAbsolutePath();
            File file = new File(dirPath + "\\" + CURRENT_PROCESS_DATA_FILE);
            if (file.exists()) {
                // ファイルの中身を JSON用のクラスに変換して取得
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(file, JsonCurrentProcessData.class);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(context, "error:file load >" + CURRENT_PROCESS_DATA_FILE, Toast.LENGTH_LONG).show();
        }
        return new JsonCurrentProcessData();
    }

    /**
     * CURRENT_PROCESS_DATA_FILE.json の中の JSON のプロパティを初期状態にリセット
     */
    public static void resetCurrentProcessData(Context context) {
        new JsonCurrentProcessData().writeSaveFile(context);
    }
}
