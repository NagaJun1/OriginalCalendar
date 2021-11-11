package com.example.originalcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.originalcalendar.Content.MemoList;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    /**
     * 起動時の処理
     * @param savedInstanceState デフォルト設定
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // カレンダーの表示状態の設定
                setCalendarVisible(R.id.navigation_calendar == item.getItemId());

                // 押下したボタンに対応して、画面の表示内容を変更
                switch (item.getItemId()){
                    case R.id.navigation_alarm:
                        // アラーム一覧を表示
                        break;
                    case R.id.navigation_memo:
                        // メモ一覧を表示
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * カレンダーの Visibility を変更
     * @return CalendarView
     */
    private void setCalendarVisible(boolean visible){
        CalendarView calendarView = findViewById(R.id.calender);
        if(visible){
            calendarView.setVisibility(View.VISIBLE);
        } else {
            calendarView.setVisibility(View.INVISIBLE);
            // イベントの抑制は必要か？
        }
    }

    /**
     * カレンダークリック時のイベントを設定
     */
    private void setCalendarEvent(){
        CalendarView calendar = findViewById(R.id.calender);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // メモに飛ぶ
                Toast.makeText(calendarView.getContext(), year+"/"+(month+1)+"/"+day, Toast.LENGTH_SHORT).show();
            }
        });
    }
}