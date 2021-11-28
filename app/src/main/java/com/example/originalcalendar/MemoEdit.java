package com.example.originalcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.originalcalendar.JsonManagement.CurrentProcessingData;
import com.example.originalcalendar.JsonManagement.JsonCalendarManager;
import com.example.originalcalendar.JsonManagement.JsonMemoListManager;

import java.util.UUID;

/**
 * edit_memo.xmlに対応した処理
 */
public class MemoEdit extends AppCompatActivity {
    /**
     * メモの編集領域
     */
    private EditText centerMemoText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_memo);

        // 画面上のコントロールの取得
        initViews();
    }

    /**
     * 各コントロールの取得（初期化処理）
     */
    private void initViews(){
        // 現在の起動中に設定された、実行中の各プロパティ
        CurrentProcessingData.JsonCurrentProcessData json = CurrentProcessingData.getJSON(this);

        // 画面上部に、メモが紐づく時刻を設定する
        setTopTimeText(json);

        // 画面上部に、メモが紐づく日付を設定する
        setTopDayText(json);

        // "戻る"メニューを設定
        setBackEvent();

        // 中央のメモ編集領域の設定
        setMemoText(json);

        // アラーム編集画面へ遷移するボタンの処理の追加
        setGoEditAlarm(json);
    }

    /**
     * 画面上部に、メモが紐づく時刻を設定する
     */
    private void setTopTimeText(CurrentProcessingData.JsonCurrentProcessData json) {
        // top_time_text に時刻を設定
        TextView topTimeText = findViewById(R.id.top_time_text);

        // strTimeが空ではなく、"0"でも無い
        if (!Common.isEmptyOrNull(json.strTime) && !Common.TIME_ZERO.equals(json.strTime)) {
            topTimeText.setText(json.strTime);
        } else {
            // 時刻文字列を設定しない場合は、非表示
            topTimeText.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 画面上部に、メモが紐づく日付（年月日・曜日）を設定する
     */
    private void setTopDayText(CurrentProcessingData.JsonCurrentProcessData json) {
        // top_day_text に日付（もしくは曜日）を設定
        TextView topDayText = findViewById(R.id.top_day_text);
        if (!Common.isEmptyOrNull(json.strDate)) {
            topDayText.setText(json.strDate);
        } else if (json.intDayOfWeek != 0) {
            topDayText.setText(Common.ONE_WEEK[json.intDayOfWeek]);
        } else {
            // 設定できる情報が無い場合は、top_day_text を非表示に設定
            topDayText.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * back_from_edit_memo に対して、イベントを設定
     */
    private void setBackEvent(){
        Button button = findViewById(R.id.back_from_edit_memo);
        button.setOnClickListener(v -> {
            // 現画面を終了して、元の画面に戻る
            finish();
        });
    }

    /**
     * 保存されたデータ内から、該当するデータを取得
     */
    private void setMemoText(CurrentProcessingData.JsonCurrentProcessData json){
        centerMemoText = findViewById(R.id.center_memo_text);
        centerMemoText.setText(getMemoText(json));
    }

    /**
     * ローカルに保存されているテキストを取得する
     * @return 取得されたテキスト
     */
    private String getMemoText(CurrentProcessingData.JsonCurrentProcessData json) {
        // 日付に紐づくメモを取得
        String textByDay = getMemoTextByDay(json);
        if(!Common.isEmptyOrNull(textByDay)){
            return textByDay;
        }
        // タグに紐づくメモのテキストを取得
        String textByTag = getMemoTextByTag(json.strTag);
        if(!Common.isEmptyOrNull(textByTag)){
            return textByTag;
        }
        // 新しいタグ文字列を生成（strTagに設定される）
        createNewTag();
        return "";
    }

    /**
     * 日付の情報に紐づくメモのテキストを取得
     * @return 取得したメモのテキスト
     */
    private String getMemoTextByDay(CurrentProcessingData.JsonCurrentProcessData json){
        String text = null;
        if(!Common.isEmptyOrNull(json.strDate)){
            // 日付に紐づくメモのテキストの取得
            text = searchTextByDay(json.strDate, json);
        }else if(0 < json.intDayOfWeek){
            // 曜日に紐づくメモのテキストの取得
            text = searchTextByDay(String.valueOf(json.intDayOfWeek), json);
        }
        return text;
    }

    /**
     * ローカルファイル内の strDay に紐づくテキストの取得
     * @param strDay 日付・曜日のいずれかの文字列
     * @return 取得されたテキスト
     */
    private String searchTextByDay(String strDay, CurrentProcessingData.JsonCurrentProcessData jsonCurrentProcessData){
        // ローカルファイルから、カレンダーの保存データを取得
        JsonCalendarManager.JsonCalendarDate json = JsonCalendarManager.getJson(this);

        // JsonCalendarDate の中の、strDayと合致する情報を取得
        for(JsonCalendarManager.Day day :json.days){
            if(day.day.equals(strDay)){
                for(JsonCalendarManager.ByDay time: day.times){
                    // 現画面上で処理されている時刻と一致する情報を取得
                    if(time.time.equals(jsonCurrentProcessData.strTime)){
                        return time.byTime.memo;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 新しいタグ文字列を生成（strTagに設定される）
     */
    private void createNewTag(){
        String newTag;
        while (true) {
            // 既存データ内に存在しないタグを生成
            newTag = UUID.randomUUID().toString();
            String textByTag = getMemoTextByTag(newTag);

            // 既存データ内に存在しないタグが生成された場合は、textByTag が null となる
            if(textByTag == null){
                // 生成したタグは、現行処理の保存ファイルに保存
                CurrentProcessingData.setTag(this, newTag);
                return;
            }
        }
    }

    /**
     * タグに紐づくテキストを、ローカルファイルから取得
     * @return 取得されたメモのテキスト
     */
    private String getMemoTextByTag(String strTag) {
        // strTag が 空なら無視
        if (!Common.isEmptyOrNull(strTag)) {
            // ローカルファイルから、保存されているメモリストを取得
            JsonMemoListManager.MemoList memoList = JsonMemoListManager.readMemoList(this);
            for (JsonMemoListManager.A_Memo aMemo : memoList.list) {
                // タグが一致する情報を取得
                if (strTag.equals(aMemo.tag)) {
                    return aMemo.memo;
                }
            }
        }
        return null;
    }

    /**
     * アラーム編集画面へ遷移するボタンの処理の追加
     */
    private void setGoEditAlarm(CurrentProcessingData.JsonCurrentProcessData json){
        Button btn = findViewById(R.id.go_edit_alarm);

        // 既にメモ編集画面を開いている状態の場合は、ボタンを非表示に設定する
        if(json.alreadyOpenEditAlarm){
            btn.setVisibility(View.INVISIBLE);
        }else {
            // アラーム編集画面への画面遷移処理の追加
            btn.setOnClickListener(v ->{
                startActivity(new Intent(this,AlarmEdit.class));

                // メモ編集画面が開かれた状態であることのフラグを設定
                CurrentProcessingData.setOpenEditMemo(this,true);
            });
        }
    }

    /**
     * 画面復帰時（アラーム編集画面から戻ってきたとき）
     */
    @Override
    protected void onRestart(){
        super.onRestart();

        // アラーム編集画面で編集された時刻の取得
        CurrentProcessingData.JsonCurrentProcessData json = CurrentProcessingData.getJSON(this);
        setTopTimeText(json);
    }

    /**
     * onStop()発生時に、編集内容を保存する
     */
    @Override
    protected void onStop() {
        super.onStop();

        // centerMemoText内のテキストを取得
        String text = centerMemoText.getText().toString();
        if(Common.isEmptyOrNull(text)){
            Toast.makeText(this,"保存を実行しませんでした",Toast.LENGTH_SHORT).show();
        } else {
            // 取得したテキストに記載があるのであれば、保存する
            saveNowData(text);
        }
    }

    /**
     * 現画面上で処理されている内容をローカルファイルに保存する
     */
    private void saveNowData(String textInNowPage){
        // 現行処理で保存されている JSON 情報
        CurrentProcessingData.JsonCurrentProcessData json = CurrentProcessingData.getJSON(this);

        if(!Common.isEmptyOrNull(json.strDate)){
            // 年月日 を使用した場合の処理
            JsonCalendarManager.setMemoDate(this,json.strDate, json.strTime,textInNowPage);
        }else if (0 < json.intDayOfWeek){
            // 曜日を使用した場合の処理
            JsonCalendarManager.setMemoDate(this,String.valueOf(json.intDayOfWeek), json.strTime,textInNowPage);
        }else{
            // 年月日・曜日に依存しないメモを、memo_list.json に保存
            JsonMemoListManager.setMemoList(this, json.strTag, textInNowPage);
        }
    }
}
