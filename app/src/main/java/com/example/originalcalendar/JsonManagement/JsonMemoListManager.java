package com.example.originalcalendar.JsonManagement;

import android.content.Context;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * memo_list.jsonの処理をするクラス
 */
public class JsonMemoListManager {
    /**
     * 日付、曜日、時刻に紐付かないメモを保存するファイル名
     */
    private static final String MEMO_LIST_FILE = "memo_list.json";

    /**
     * JSONのメモ一覧の保存先（日付、曜日を含まない）
     */
    public static class MemoList{
        /**
         * メモに紐づくタグ(Key)とメモの内容(value)のペア
         */
        public Map<String,String> tagAndText = new HashMap<>();
    }

    /**
     * memo_list.json に情報を保存
     * @param context 処理画面のContext
     * @param tag メモに紐づくタグ
     * @param text メモ設定する文字列
     */
    public static void setMemoTextWithTag(Context context, String tag, String text){
        // 既存のメモリストの読み込み
        MemoList memoList = readMemoList(context);

        // タグに紐づけて、メモを設定（重複時は上書き）
        memoList.tagAndText.put(tag,text);

        // JSONを文字列として、ファイルに保存する
        try (FileWriter writer = new FileWriter(context.getFilesDir().getAbsolutePath() + "\\" + MEMO_LIST_FILE)) {
            ObjectMapper mapper = new ObjectMapper();
            writer.write(mapper.writeValueAsString(memoList));
        } catch (Exception e) {
            Toast.makeText(context, "メモの書き込みに失敗しました。", Toast.LENGTH_SHORT).show();
            System.out.println(e.toString());
        }
    }

    /**
     * ローカルに保存されているメモ一覧の読み込み
     * @param context 処理実行画面のContext
     * @return 取得された List<JsonProperty.A_Memo>
     */
    public static MemoList readMemoList(Context context){
        try {
            String dirPath = context.getFilesDir().getAbsolutePath();
            File file = new File(dirPath + "\\" + MEMO_LIST_FILE);

            // 読み込むファイルが存在しない場合は処理しない
            if (file.exists()) {
                // ファイル内のJSONをクラスに変換する
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(file, MemoList.class);
            }
        }catch (Exception e){
            Toast.makeText(context,"ファイル読み込みに失敗しました\n"+MEMO_LIST_FILE,Toast.LENGTH_SHORT).show();
            System.out.println(e.toString());
        }
        return new MemoList();
    }
}
