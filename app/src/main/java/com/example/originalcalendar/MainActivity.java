package com.example.originalcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
     * 画面下部のナビゲーションバー
     */
    private BottomNavigationView navView = null;

    /**
     * 起動時の処理
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
    private void initViews(){
        calendarView = findViewById(R.id.calender);
        listLayout = findViewById(R.id.included_list_layout);
        navView = findViewById(R.id.nav_view);

        // BottomNavigationViewにイベントを設定
        setNavViewEvent();

        // カレンダークリック時のイベントを設定
        setCalendarEvent();
    }

    /**
     * BottomNavigationViewのイベント設定
     * 画面下記のボタン押下時に画面表示を変更する
     */
    private void setNavViewEvent(){
        navView.setOnItemSelectedListener(item -> {
            // カレンダーの表示状態の設定（ナビゲーションのカレンダーを押下した場合にだけ、表示状態にする）
            setCalendarVisible(R.id.navigation_calendar == item.getItemId());

            // 押下したボタンに対応して、画面の表示内容を変更
            switch (item.getItemId()){
                case R.id.navigation_alarm:
                    // アラーム一覧を表示
                    setAlarmList();
                    return false;
                case R.id.navigation_memo:
                    // メモ一覧を表示
                    setMemoList();
                    return false;
                default:
                    return false;
            }
        });
    }

    /**
     * アラーム一覧の表示
     */
    private void setAlarmList(){
        setCalendarVisible(false);
    }

    /**
     * メモ一覧の表示
     */
    private void setMemoList(){
        setCalendarVisible(false);
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

                // メモ編集画面を表示
                Intent intent = new Intent(calendarView.getContext(), MemoEdit.class);
                intent.putExtra(Common.DATE, strDate);
                startActivity(intent);
            });
        }catch (Exception e){
            System.out.println(">>>>>>>>>>>>>>>error");
            System.out.println(e.toString());
        }
    }
}