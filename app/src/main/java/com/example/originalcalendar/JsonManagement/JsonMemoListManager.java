package com.example.originalcalendar.JsonManagement;

import android.content.Context;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

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
        public List<A_Memo> list = new ArrayList<A_Memo>();
    }

    /**
     * メモ一つ分のデータ
     */
    public static class A_Memo{
        /**
         * メモに紐付けるためのタグ
         */
        public String tag = null;
        /**
         * メモの本文
         */
        public String memo = null;
    }

    /**
     * memo_list.json に情報を保存
     * @param context 処理画面のContext
     * @param tag メモに紐づくタグ
     * @param text メモ設定する文字列
     */
    public static void setMemoList(Context context, String tag, String text){
        // 既存のメモリストの読み込み
        MemoList memoList = readMemoList(context);
        for(int i = 0;i < memoList.list.size();i++){
            if(memoList.list.get(i).tag.equals(tag)){
                // 既存データの中に、重複するデータがある場合は、該当データを削除
                memoList.list.remove(i);
                break;
            }
        }
        // 今回追加する情報を生成して、クラスに挿入
        A_Memo newMemo = new A_Memo();
        newMemo.tag = tag;
        newMemo.memo = text;
        memoList.list.add(newMemo);

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
