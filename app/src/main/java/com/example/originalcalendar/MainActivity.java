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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.originalcalendar.JsonManagement.CurrentProcessingData;
import com.example.originalcalendar.JsonManagement.JsonCalendarManager;
import com.example.originalcalendar.JsonManagement.JsonMemoListManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    /**
     * 画面中央の配置カレンダーの取得
     */
    private CalendarView getCalendarView() {
        CalendarView view = findViewById(R.id.calender);
        if (view == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " calender", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    /**
     * メモ一覧、アラーム一覧を表示するための領域の取得
     */
    private RelativeLayout getListLayout() {
        RelativeLayout layout = findViewById(R.id.included_list_layout);
        if (layout == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " included_list_layout", Toast.LENGTH_LONG).show();
        }
        return layout;
    }

    /**
     * 画面下部のナビゲーションボタン
     */
    private BottomNavigationView getBottomNavigationView() {
        BottomNavigationView view = findViewById(R.id.nav_view);
        if (view == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " included_list_layout", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    /**
     * メモ・アラーム一覧の格納先を取得
     */
    private LinearLayout getListInScrollView() {
        LinearLayout linearLayout = findViewById(R.id.list_in_scroll_view);
        if (linearLayout == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " list_in_scroll_view", Toast.LENGTH_LONG).show();
        }
        return linearLayout;
    }

    /**
     * 新規作成」ボタンの取得
     */
    private Button getBtnAddInList() {
        Button btn = findViewById(R.id.btn_add_in_list);
        if (btn == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " btn_add_in_list", Toast.LENGTH_LONG).show();
        }
        return btn;
    }

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
        getBottomNavigationView().setOnItemSelectedListener(item -> {
            // カレンダーの表示状態の設定（ナビゲーションのカレンダーを押下した場合にだけ、表示状態にする）
            setCalendarVisible(R.id.navigation_calendar == item.getItemId());

            if (item.getItemId() == R.id.navigation_alarm) {
                // アラーム一覧を表示
                setAlarmList();
            } else if (item.getItemId() == R.id.navigation_memo) {
                // メモ一覧を表示
                setMemoList();
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
    private void setMemoList() {
        // メモ一覧を表示状態に変更
        setCalendarVisible(false);

        //「新規作成」ボタンの設定
        setNewMemoButton();

        // メモ一覧の格納先を取得
        LinearLayout linearLayout = getListInScrollView();
        if (linearLayout != null) {
            // 子要素を全部削除
            linearLayout.removeAllViews();

            // tag・memo のペアのリストを取得
            JsonMemoListManager.MemoList memo_list_in_file = JsonMemoListManager.readMemoList(this);
            for (Map.Entry<String, String> aMemo : memo_list_in_file.tagAndText.entrySet()) {
                // list_in_scroll_view に対して、TextView を新規に生成して追加
                TextView newTxtView = createNewTextViewForMemo(aMemo);
                linearLayout.addView(newTxtView);
            }
            // listLayout の高さを調整
            fixHeightOfListLayout();
        }
    }

    /**
     * fixHeightOfListLayout() が実行済みかの判定用フラグ
     */
    private boolean alreadyExecuted = false;

    /**
     * listLayout の高さを調整
     */
    private void fixHeightOfListLayout() {
        // 実行済みでない場合は、実行する
        if (!alreadyExecuted) {
            alreadyExecuted = true;

            // listLayout の高さを調整
            RelativeLayout listLayout = getListLayout();
            ViewGroup.LayoutParams layoutParams = listLayout.getLayoutParams();
            layoutParams.height = listLayout.getHeight() - getBottomNavigationView().getHeight() - 10;
            listLayout.setLayoutParams(layoutParams);
        }
    }

    /**
     * メモの「新規作成」ボタンのイベント設定
     */
    private void setNewMemoButton() {
        Button btn = getBtnAddInList();

        // ボタン押下時に、メモ編集画面に遷移
        btn.setOnClickListener(v -> {
            // 現行処理情報をリセット
            CurrentProcessingData.resetCurrentProcessData(this);

            // メモ編集画面に遷移
            startActivity(new Intent(this, MemoEdit.class));
        });
    }

    /**
     * 新規にメモ一覧のための TextView を生成
     *
     * @param aMemo JsonMemoListManager.A_Memoのオブジェクト
     * @return 生成した TextView
     */
    private TextView createNewTextViewForMemo(Map.Entry<String, String> aMemo) {
        TextView textView = createDefaultTextViewInList();

        // 生成した TextView に、取得されたメモを保存
        textView.setText(Common.getTopTenChar(aMemo.getValue()));

        // 生成された TextView の押下時に アラーム・メモ編集画面に移行
        textView.setOnClickListener(v -> onClickMemoElement(aMemo.getKey()));
        return textView;
    }

    /**
     * メモ・アラーム一覧内に追加する TextViewのデフォルトを生成
     *
     * @return TextView
     */
    private TextView createDefaultTextViewInList() {
        TextView textView = new TextView(this);
        textView.setTextSize(30);
        textView.setBackgroundColor(Color.CYAN);

        // TextViewのレイアウトの設定（Margin と図形の幅、高さを設定）
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(layoutParams);
        margin.setMargins(10, 10, 10, 10);
        textView.setLayoutParams(margin);
        return textView;
    }

    /**
     * メモ一覧内の項目の押下時の処理
     */
    private void onClickMemoElement(String strTag) {
        // CurrentProcessingData の各プロパティを初期化
        // 現画面で指定されたタグを JsonCurrentProcessData に保存
        CurrentProcessingData.JsonCurrentProcessData currentProcessData = new CurrentProcessingData.JsonCurrentProcessData();
        currentProcessData.setTag(this, strTag);

        // メモ編集画面に遷移
        startActivity(new Intent(this, MemoEdit.class));
    }

    /**
     * カレンダーの Visibility を変更
     * （center_layout の Visibility はカレンダーと対になる様に設定）
     */
    private void setCalendarVisible(boolean visible) {
        CalendarView calendarView = getCalendarView();
        RelativeLayout listLayout = getListLayout();
        if (visible) {
            calendarView.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.INVISIBLE);
        } else {
            calendarView.setVisibility(View.INVISIBLE);
            listLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * カレンダー内の日付押下時のイベントを設定
     * →　アラーム一覧を表示
     */
    private void setCalendarEvent() {
        try {
            getCalendarView().setOnDateChangeListener((c, year, month, day) -> {
                // month（月）は、0~11 で処理されるため、+1する
                String strDate = Common.getStrDate(year, month + 1, day);

                // 日付に紐づくアラーム一覧を表示
                setAlarmListInDay(strDate);
            });
        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>>>>>error");
            System.out.println(e.toString());
        }
    }

    /**
     * 日付に紐づくアラーム一覧の表示
     *
     * @param strDate アラーム一覧に表示する日付
     */
    private void setAlarmListInDay(String strDate) {
        // 一覧を表示状態に変更
        setCalendarVisible(false);

        //「新規作成」ボタンの設定
        setNewAlarmButton(strDate);

        // メモ一覧の格納先を取得
        LinearLayout linearLayout = getListInScrollView();
        if (linearLayout != null) {
            // 子要素を全部削除
            linearLayout.removeAllViews();

            // カレンダーに紐づく JSONを取得
            JsonCalendarManager.JsonCalendarDate calendarDate = JsonCalendarManager.getJson(this);
            if (calendarDate != null) {
                // 日付に紐づく範囲の各値を取得
                JsonCalendarManager.ValuesWithDay valuesWithDay = calendarDate.dayAndValues.get(strDate);
                if (valuesWithDay != null) {
                    // 時刻の一覧を画面に表示
                    for (Map.Entry<String, JsonCalendarManager.ValuesWithTime> values : valuesWithDay.timeAndValues.entrySet()) {
                        // list_in_scroll_view に対して、TextView を新規に生成して追加
                        TextView newTxtView = createAlarmTextView(strDate, values.getKey(), values.getValue());
                        linearLayout.addView(newTxtView);
                    }
                }
            }
            // listLayout の高さを調整
            fixHeightOfListLayout();
        }
    }

    /**
     * アラーム一覧に追加する TextViewの生成
     * @param strDate 年月日
     * @param strTime 時刻
     * @param valuesWithTime 時刻と紐づく各データ
     * @return アラーム一覧に追加するための TextView
     */
    private TextView createAlarmTextView(String strDate, String strTime, JsonCalendarManager.ValuesWithTime valuesWithTime) {
        TextView view = createDefaultTextViewInList();

        // TextViewには、時刻とメモ内容の一部を表示
        String strTxt = strTime + " " + Common.getTopTenChar(valuesWithTime.memo);
        view.setText(strTxt);

        // 押下時は、アラーム編集画面へと遷移する
        view.setOnClickListener(v -> {
            // 現処理で使用している値を設定（無関係の設定値の初期化）
            CurrentProcessingData.JsonCurrentProcessData currentProcessData = new CurrentProcessingData.JsonCurrentProcessData();
            currentProcessData.setDate(this, strDate);
            currentProcessData.setTime (this, strTime);

            // アラーム編集画面へ遷移
            startActivity(new Intent(this, AlarmEdit.class));
        });
        return view;
    }

    /**
     * アラームの「新規作成」ボタンのイベント設定
     * @param strDate 現処理での日付情報
     */
    private void setNewAlarmButton(String strDate) {
        Button btn = getBtnAddInList();

        // ボタン押下時に、メモ編集画面に遷移
        btn.setOnClickListener(v -> {
            // 現画面で指定されている日付を設定
            CurrentProcessingData.JsonCurrentProcessData currentProcessData = new CurrentProcessingData.JsonCurrentProcessData();
            currentProcessData.setDate(this, strDate);

            // アラーム編集画面へ遷移
            startActivity(new Intent(this, AlarmEdit.class));
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        setMemoList();
    }
}