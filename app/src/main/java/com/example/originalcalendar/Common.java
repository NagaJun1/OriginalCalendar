package com.example.originalcalendar;

import android.content.Context;
import android.widget.Toast;

import com.example.originalcalendar.JsonManagement.JsonManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Common {
    /**
     * コンマの固定文字列
     */
    public static final String COMMA = ",";

    /**
     * コロンの固定文字列
     */
    public static final String COLON = ":";

    /**
     * 一週間分の曜日の日本語配列
     */
    public static final String[] ONE_WEEK = {"error", "日", "月", "火", "水", "木", "金", "土"};

    /**
     * Intent に年月日を設定、取り出すためのキー
     */
    public static final String DATE = "DATE";

    /**
     * Intent に時刻を設定、取り出すためのキー
     */
    public static final String TIME = "TIME";

    /**
     * Intent に曜日識別用の数字を設定、取り出すためのキー
     */
    public static final String DAY_OF_WEEK = "DAY_OF_WEEK";

    /**
     * Intent に「メモ編集画面を既に開いているかの判定」を設定、取り出すためのキー
     */
    public static final String ALREADY_OPEN_EDIT_MEMO = "ALREADY_OPEN_EDIT_MEMO";

    /**
     * Intent に「アラーム編集画面を既に開いているかの判定」を設定、取り出すためのキー
     */
    public static final String ALREADY_OPEN_EDIT_ALARM = "ALREADY_OPEN_EDIT_ALARM";

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
     * 時刻の規定フォーマットを作成
     * @param hour 時
     * @param minutes 分
     * @return hh:mm のフォーマットを作成
     */
    public static String getStrTime(int hour,int minutes){
        return String.valueOf(hour) + COLON + String.valueOf(minutes);
    }

    /**
     * JSONのフォーマットに修正して、ローカルファイルにメモを記録
     * @param context 処理実行画面の Context
     * @param date 年月日
     * @param time 時刻
     * @param dayOfWeek 曜日
     * @param text 書き込みを行いたい文字列
     */
    public static void writeSaveData(Context context, String date, String time, int dayOfWeek, String text) {
        try {
            // ローカルファイルに保存されたJSON の取得
            String strJson = readSaveData(context);

            // TODO null の回避
            if (strJson != null) {
                // ファイルから読み込んだ中身を JSONのオブジェクトに変換
                ObjectMapper mapper = new ObjectMapper();
                JsonManager json = mapper.readValue(strJson, JsonManager.class);

                // TODO 既存情報の確認
            } else {
                writeInFile(context, text);
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    /**
     * 保存されている情報から、引数とマッチする情報を取得
     * @param date      年月日
     * @param time      時刻
     * @param dayOfWeek 曜日
     * @param context 処理実行画面の Context
     * @return 取得された情報（文字列）
     */
    public static String getTextInRecord(String date, String time, int dayOfWeek, Context context) {
        // ローカルファイルから、データを読み込み
        String readData = readSaveData(context);

        /*
        // 曜日が未指定の場合は、日付から曜日を取得
        if (dayOfWeek == 0) {
            dayOfWeek = getDayOfWeek(date);
        }
         */

        // TODO 条件の設定がされていないため、後で修正
        return readData;
    }

    /**
     * メモのデータを保存するファイル名
     */
    private static final String FILE_NAME = "memo_save_data.json";

    /**
     * 保存データの書き換え（全て上書き）
     * @param context 処理の実行画面のContext
     * @param text ファイルに書き込むテキストデータ
     */
    private static void writeInFile(Context context,String text) {
        try (FileWriter writer = new FileWriter(context.getFilesDir().getAbsolutePath() + "\\" + FILE_NAME)) {
            writer.write(text);
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
    private static String readSaveData(Context context) {
        String dirPath = context.getFilesDir().getAbsolutePath();
        File file = new File(dirPath + "\\" + FILE_NAME);

        // 読み込むファイルが存在しない場合は処理しない
        if(!file.exists()){
            return null;
        }
        try (
                BufferedReader br = new BufferedReader(new FileReader(file))
        ) {
            // 一行ずつ読み込み、一つの文章として、読み込み結果を返す
            StringBuilder text = new StringBuilder();
            String line = br.readLine();
            while (line != null){
                text.append(line).append("\n");
                line = br.readLine();
            }
            return text.toString();
        } catch (Exception e) {
            Toast.makeText(context, "メモの取得に失敗しました。", Toast.LENGTH_SHORT).show();
            System.out.println(e.toString());
            return null;
        }
    }
}
