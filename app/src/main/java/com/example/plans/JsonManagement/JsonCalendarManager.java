package com.example.plans.JsonManagement;

import android.content.Context;
import android.widget.Toast;

import com.example.plans.Common;
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
        public Map<String, ValuesWithDay> dayAndValues = new HashMap<>();

        /**
         * 保存データの書き換え（全て上書き）
         *
         * @param context      処理の実行画面のContext
         */
        private void saveInFile(Context context  ) {
            try (FileWriter writer = new FileWriter(context.getFilesDir().getAbsolutePath() + "\\" + CALENDAR_DATA_FILE)) {
                ObjectMapper mapper = new ObjectMapper();
                String strJson = mapper.writeValueAsString(this);
                writer.write(strJson);
            } catch (Exception e) {
                Toast.makeText(context, "メモの書き込みに失敗しました。", Toast.LENGTH_SHORT).show();
                System.out.println(e.toString());
            }
        }

        /**
         * 引数の日付・時刻に紐づく情報の取得
         */
        public ValuesWithTime getValuesWithTime(String strDay, String strTime) {
            if (!Common.isEmptyOrNull(strDay) && !Common.isEmptyOrNull(strTime)) {
                ValuesWithDay valuesWithDay = dayAndValues.get(strDay);
                if (valuesWithDay != null) {
                    return valuesWithDay.getValuesWithTime(strTime);
                }
            }
            return new ValuesWithTime();
        }

        /**
         * 引数の時刻に紐づく ValuesWithTime を削除
         *
         * @return この処理で削除された ValuesWithTime
         */
        public ValuesWithTime deleteTime(Context context,String strDay, String strTime) {
            if (!Common.isEmptyOrNull(strDay) && !Common.isEmptyOrNull(strTime)) {
                ValuesWithDay valuesWithDay = dayAndValues.get(strDay);
                if (valuesWithDay != null) {
                    // 時刻に紐づく各値を削除（削除された各値を取得）
                    ValuesWithTime valuesWithTime = valuesWithDay.timeAndValues.remove(strTime);
                    if(valuesWithTime!=null) {
                        // 削除後の結果を再格納
                        // （get()で参照できているとしたら、無意味な処理）
                        dayAndValues.put(strDay, valuesWithDay);

                        // 要素の削除後の JSONで、.jsonファイルを更新
                        saveInFile(context);
                        return valuesWithTime;
                    }
                }
            }
            return new ValuesWithTime();
        }

        /**
         * ローカルファイルにデータを追加
         *
         * @param context        処理画面のContext
         * @param strDay         処理対象の日付・曜日
         * @param strTime        処理対象の時刻
         * @param valuesWithTime 格納したい値
         */
        public void setValuesWithTime(Context context, String strDay, String strTime, ValuesWithTime valuesWithTime) {
            // 時刻が設定されていない場合は、時刻は"0"で設定
            if (Common.isEmptyOrNull(strTime)) {
                Toast.makeText(context, "error:strDay > not set valuesWithTime", Toast.LENGTH_LONG).show();
                return;
            }
            // 日付に紐づく各値を取得
            ValuesWithDay valuesWithDay = dayAndValues.get(strDay);
            if (valuesWithDay == null) {
                // 日付の一致するJSONが取得されなかった場合は新規に生成
                valuesWithDay = new ValuesWithDay();
            }
            // 時刻に紐づけて、メモを挿入
            valuesWithDay.timeAndValues.put(strTime, valuesWithTime);

            // 元のMapに再格納
            dayAndValues.put(strDay, valuesWithDay);

            // ファイルに保存
            saveInFile(context);
        }
    }

    /**
     * 日付・曜日に紐づく JSON
     */
    public static class ValuesWithDay {
        /**
         * 時間に紐づく値
         */
        public Map<String, ValuesWithTime> timeAndValues = new HashMap<>();

        /**
         * 日付に紐づく ValuesWithTime を取得
         */
        private ValuesWithTime getValuesWithTime(String strTime) {
            ValuesWithTime valuesWithTime = timeAndValues.get(strTime);
            if (valuesWithTime == null) {
                return new ValuesWithTime();
            }
            return valuesWithTime;
        }
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
     * 保存データの読み込み（全て読み込み）
     *
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
                return mapper.readValue(file, JsonCalendarDate.class);
            }
        } catch (Exception e) {
            Toast.makeText(context, "error:JsonCalendarDate.getJson()", Toast.LENGTH_SHORT).show();
            System.out.println(e.toString());
        }
        return new JsonCalendarDate();
    }
}
