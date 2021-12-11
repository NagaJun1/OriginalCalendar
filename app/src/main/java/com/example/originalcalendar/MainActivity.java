package com.example.originalcalendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.originalcalendar.JsonManagement.CurrentProcessingData;
import com.example.originalcalendar.JsonManagement.JsonMemoListManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    /**
     * 画面中央に配置してあるカレンダー
     */
    private CalendarView calendarView = null;

    /**
     * メモ一覧、アラーム一覧を表示するための領域
     */
    private RelativeLayout listLayout = null;

    /**
     * 起動時の処理
     *
     * @param savedInstanceState デフォルト設定
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 各コントロールの取得
        initViews();
    }

    /**
     * 各コントロールの取得（初期化処理）
     */
    private void initViews() {
        calendarView = findViewById(R.id.calender);
        listLayout = findViewById(R.id.included_list_layout);

        // カレンダークリック時のイベントを設定
        setCalendarEvent();

        // 画面下部のナビゲーションボタン押下時
        setNavViewEvent();
    }

    /**
     * BottomNavigationViewのイベント設定
     * 画面下記のボタン押下時に画面表示を変更する
     */
    @SuppressLint("NonConstantResourceId")
    private void setNavViewEvent() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            // カレンダーの表示状態の設定（ナビゲーションのカレンダーを押下した場合にだけ、表示状態にする）
            setCalendarVisible(R.id.navigation_calendar == item.getItemId());

            if (item.getItemId() == R.id.navigation_alarm) {
                // アラーム一覧を表示
                setAlarmList();
            } else if (item.getItemId() == R.id.navigation_memo) {
                // メモ一覧を表示
                setMemoList(navView);
            }
            return false;
        });
    }

    /**
     * アラーム一覧の表示
     */
    private void setAlarmList() {
        setCalendarVisible(false);
    }

    /**
     * メモ一覧の表示
     */
    private void setMemoList(BottomNavigationView navigationView) {
        // メモ一覧を表示状態に変更
        setCalendarVisible(false);

        //「新規作成」ボタンの設定
        setNewMemoButton();

        // メモ一覧の格納先を取得
        LinearLayout linearLayout = findViewById(R.id.list_in_scroll_view);

        // 例外回避
        if (linearLayout == null) {
            return;
        }

        // 子要素を全部削除
        linearLayout.removeAllViews();

        // tag・memo のペアのリストを取得
        JsonMemoListManager.MemoList memo_list_in_file
                = JsonMemoListManager.readMemoList(this);


        for (Map.Entry<String,String> aMemo: memo_list_in_file.tagAndText.entrySet()){
            // list_in_scroll_view に対して、TextView を新規に生成して追加
            TextView newTxtView = createNewTextView(aMemo);
            linearLayout.addView(newTxtView);
        }

        // listLayout の高さを調整
        fixHeightOfListLayout(navigationView);
    }

    /**
     * fixHeightOfListLayout() が実行済みかの判定用フラグ
     */
    private boolean alreadyExecuted = false;

    /**
     * listLayout の高さを調整
     */
    private void fixHeightOfListLayout(BottomNavigationView navigationView) {
        if (alreadyExecuted) {
            return;
        } else {
            alreadyExecuted = true;
        }

        // listLayout の高さを調整
        ViewGroup.LayoutParams layoutParams = listLayout.getLayoutParams();
        layoutParams.height = listLayout.getHeight() - navigationView.getHeight() - 10;
        listLayout.setLayoutParams(layoutParams);
    }

    /**
     * 「新規作成」ボタンのイベント設定
     */
    private void setNewMemoButton() {
        Button btn = findViewById(R.id.btn_add_in_list);

        // ボタン押下時に、メモ編集画面に遷移
        btn.setOnClickListener(v -> {
            CurrentProcessingData.resetCurrentProcessData(this);
            startActivity(new Intent(this, MemoEdit.class));
        });
    }

    /**
     * 新規に TextView を生成
     * @param aMemo JsonMemoListManager.A_Memoのオブジェクト
     * @return 生成した TextView
     */
    private TextView createNewTextView(Map.Entry<String,String> aMemo) {
        TextView textView = new TextView(this);
        textView.setTextSize(30);
        textView.setBackgroundColor(Color.CYAN);

        // TextViewのレイアウトの設定（Margin と図形の幅、高さを設定）
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(layoutParams);
        margin.setMargins(10, 10, 10, 10);
        textView.setLayoutParams(margin);

        // 生成した TextView に、取得されたメモを保存
        textView.setText(aMemo.getValue().split("\n")[0]);

        // 生成された TextView の押下時に アラーム・メモ編集画面に移行
        textView.setOnClickListener(v -> onClickMemoElement(aMemo.getKey()));
        return textView;
    }

    /**
     * メモ一覧内の項目の押下時の処理
     */
    private void onClickMemoElement(String tag){
        // CurrentProcessingData の各プロパティを初期値にする
        CurrentProcessingData.resetCurrentProcessData(this);

        // 現画面で指定されたタグを JsonCurrentProcessData に保存
        CurrentProcessingData.setTag(this, tag);

        // メモ編集画面に遷移
        startActivity(new Intent(this, MemoEdit.class));
    }

    /**
     * カレンダーの Visibility を変更
     * （center_layout の Visibility はカレンダーと対になる様に設定）
     */
    private void setCalendarVisible(boolean visible){
        if(visible){
            calendarView.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.INVISIBLE);
        } else {
            calendarView.setVisibility(View.INVISIBLE);
            listLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * カレンダー内の日付押下時のイベントを設定
     */
    private void setCalendarEvent(){
        try {
            calendarView.setOnDateChangeListener((c, year, month, day) -> {
                // month（月）は、0~11 で処理されるため、+1する
                String strDate = Common.getStrDate(year, month+1, day);

                // CurrentProcessingDataで保存されている情報を初期化
                CurrentProcessingData.resetCurrentProcessData(this);

                // 現画面の時刻を保存
                CurrentProcessingData.setDate(this, strDate);

                // メモ編集画面を表示
                startActivity(new Intent(this, MemoEdit.class));
            });
        }catch (Exception e){
            System.out.println(">>>>>>>>>>>>>>>error");
            System.out.println(e.toString());
        }
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }
}