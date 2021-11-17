package com.example.originalcalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    /**
     * 前画面から取得してきた年月日
     */
    private String strDate = null;

    /**
     * 前画面から取得してきた時刻
     */
    private String strTime = null;

    /**
     * 設定するテキストに紐づくタグ
     */
    private String strTag = null;

    /**
     * 前画面から取得してきた曜日
     */
    private int intDayOfWeek = 0;

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
        // 前画面の情報を取得
        Intent thisIntent = getIntentData();

        // 画面上部に、メモが紐づく時刻を設定する
        setTopTimeText();

        // "戻る"メニューを設定
        setBackEvent();

        // 中央のメモ編集領域の設定
        setMemoText();

        // アラーム編集画面へ遷移するボタンの処理の追加
        setGoEditAlarm(thisIntent);
    }

    /**
     * 前画面で設定した情報を取得するための Intent
     */
    private Intent getIntentData(){
        Intent intent = this.getIntent();
        strDate = intent.getStringExtra(Common.DATE);
        strTime = intent.getStringExtra(Common.TIME);
        intDayOfWeek = intent.getIntExtra(Common.DAY_OF_WEEK,0);
        strTag = intent.getStringExtra(Common.TAG);
        return intent;
    }

    /**
     * 画面上部に、メモが紐づく時刻を設定する
     */
    private void setTopTimeText(){
        // top_time_text に時刻を設定
        TextView topTimeText = findViewById(R.id.top_time_text);
        if(strTime != null){
            topTimeText.setText(strTime);
        } else {
            topTimeText.setVisibility(View.INVISIBLE);
        }

        // top_day_text に日付（もしくは曜日）を設定
        TextView topDayText = findViewById(R.id.top_day_text);
        if(strDate != null){
            topDayText.setText(strDate);
        } else if(intDayOfWeek != 0){
            topDayText.setText(Common.ONE_WEEK[intDayOfWeek]);
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
    private void setMemoText(){
        centerMemoText = findViewById(R.id.center_memo_text);
        centerMemoText.setText(getMemoText());
    }

    /**
     * ローカルに保存されているテキストを取得する
     * @return 取得されたテキスト
     */
    private String getMemoText() {
        // 日付に紐づくメモを取得
        String textByDay = getMemoTextByDay();
        if(textByDay!=null){
            return textByDay;
        }
        // タグに紐づくメモのテキストを取得
        String textByTag = getMemoTextByTag();
        if (textByTag!=null){
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
    private String getMemoTextByDay(){
        String text = null;
        if(strDate!=null&&0<strDate.length()){
            // 日付に紐づくメモのテキストの取得
            text = searchTextByDay(strDate);
        }else if(0<intDayOfWeek){
            // 曜日に紐づくメモのテキストの取得
            text = searchTextByDay(String.valueOf(intDayOfWeek));
        }
        return text;
    }

    /**
     * ローカルファイル内の strDay に紐づくテキストの取得
     * @param strDay 日付・曜日のいずれかの文字列
     * @return 取得されたテキスト
     */
    private String searchTextByDay(String strDay){
        // ローカルファイルから、カレンダーの保存データを取得
        JsonCalendarManager.JsonCalendarDate json = JsonCalendarManager.getJson(this);

        // JsonCalendarDate の中の、strDayと合致する情報を取得
        for(JsonCalendarManager.Day day :json.days){
            if(day.day.equals(strDay)){
                for(JsonCalendarManager.ByDay time: day.times){
                    // 現画面上で処理されている時刻と一致する情報を取得
                    if(time.time.equals(strTime)){
                        return time.byTime.memo;
                    }
                }
            }
        }
        return null;
    }

    /**
     * タグに紐づくテキストを、ローカルファイルから取得
     * @return 取得されたメモのテキスト
     */
    private String getMemoTextByTag(){
        JsonMemoListManager.MemoList memoList = JsonMemoListManager.readMemoList(this);
        for (JsonMemoListManager.A_Memo aMemo : memoList.list) {
            // タグが一致する情報を取得
            if (strTag.equals(aMemo.tag)) {
                return aMemo.memo;
            }
        }
        return null;
    }

    /**
     * 新しいタグ文字列を生成（strTagに設定される）
     */
    private void createNewTag(){
        while (true) {
            // 既存データ内に存在しないタグを生成
            strTag = UUID.randomUUID().toString();
            String textByTag = getMemoTextByTag();

            // 既存データ内に存在しないタグが生成された場合は、textByTag が null となる
            if(textByTag == null){
                return;
            }
        }
    }

    /**
     * アラーム編集画面へ遷移するボタンの処理の追加
     */
    private void setGoEditAlarm(Intent thisIntent){
        Button btn = findViewById(R.id.go_edit_alarm);

        // 既にメモ編集画面を開いている状態の場合は、ボタンを非表示に設定する
        if(thisIntent.getBooleanExtra(Common.ALREADY_OPEN_EDIT_ALARM,false)){
            btn.setVisibility(View.INVISIBLE);
        }else {
            // アラーム編集画面への画面遷移処理の追加
            btn.setOnClickListener(v -> startActivity(createEditAlarmIntent(this)));
        }
    }

    /**
     * アラーム編集画面の Intent（アラーム編集画面を表示して、戻ってきた場合に使用する
     */
    private Intent alarmEditIntent = null;

    /**
     * edit_alarm ページのIntent を生成
     * @param context 現画面の Context
     * @return 生成した Intent
     */
    private Intent createEditAlarmIntent(Context context){
        alarmEditIntent = new Intent(context, AlarmEdit.class);

        // メモ編集画面が存在する場合は、EXIST_MEMOを trueで設定する
        alarmEditIntent.putExtra(Common.ALREADY_OPEN_EDIT_MEMO, true);

        // 下記は、既存情報を引き継ぐ必要があるため、設定
        alarmEditIntent.putExtra(Common.DATE, strDate);
        alarmEditIntent.putExtra(Common.TIME, strTime);
        alarmEditIntent.putExtra(Common.DAY_OF_WEEK, intDayOfWeek);
        alarmEditIntent.putExtra(Common.TAG,strTag);
        return alarmEditIntent;
    }

    /**
     * 画面復帰時（アラーム編集画面から戻ってきたとき？）
     */
    @Override
    protected void onRestart(){
        super.onRestart();

        // TODO
        // アラーム編集画面の情報から復帰した場合かの確認
        Toast.makeText(this,"onRestart()",Toast.LENGTH_SHORT).show();
        if(alarmEditIntent!=null){
            // アラーム編集画面で編集された時刻の取得
        }
    }

    /**
     * onStop()発生時に、編集内容を保存する
     */
    @Override
    protected void onStop() {
        super.onStop();

        // centerMemoText内のテキストを取得
        String text = centerMemoText.getText().toString();
        if(0 == text.length()) {
            Toast.makeText(this,"保存を実行しませんでした",Toast.LENGTH_SHORT).show();
        } else {
            // 取得したテキストに記載があるのであれば、保存する
            saveNowData();
        }
    }

    /**
     * 現画面上で処理されている内容をローカルファイルに保存する
     */
    private void saveNowData(){
        // 画面中央の EditText から テキストを取得
        String memoText = centerMemoText.getText().toString();

        if(strDate != null && 0 < strDate.length()){
            // 年月日 を使用した場合の処理
            JsonCalendarManager.setMemoDate(this,strDate,strTime,memoText);
        }else if (0 < intDayOfWeek){
            // 曜日を使用した場合の処理
            JsonCalendarManager.setMemoDate(this,String.valueOf(intDayOfWeek),strTime,memoText);
        }else if(strDate!=null){
            // 年月日・曜日に依存しない
            // memo_list.json に保存
            JsonMemoListManager.setMemoList(this, strTag, memoText);
        }
    }
}
