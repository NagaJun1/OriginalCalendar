package com.example.plans;

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

import com.example.plans.JsonManagement.CurrentProcessingData;
import com.example.plans.JsonManagement.JsonCalendarManager;
import com.example.plans.JsonManagement.JsonMemoListManager;
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
    private RelativeLayout getIncludedListLayout() {
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
     * メモ・予定一覧の格納先を取得
     */
    private LinearLayout getListInScrollViewForMemo() {
        LinearLayout linearLayout = findViewById(R.id.list_in_scroll_view_for_memo);
        if (linearLayout == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " list_in_scroll_view_for_memo", Toast.LENGTH_LONG).show();
        } else {
            // 取得時に、子要素を全削除
            linearLayout.removeAllViews();
        }
        return linearLayout;
    }

    /**
     * メモ・予定一覧の格納先を取得
     */
    private LinearLayout getListInScrollViewForPlans() {
        LinearLayout linearLayout = findViewById(R.id.list_in_scroll_view_for_plans);
        if (linearLayout == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " list_in_scroll_view_for_plans", Toast.LENGTH_LONG).show();
        } else {
            // 取得時に、子要素を全削除
            linearLayout.removeAllViews();
        }
        return linearLayout;
    }

    /**
     * 「新規作成」ボタンの取得
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
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // 各コントロールの取得
            initViews();

            // 通知用のチャネルを作成
            Common.createNotificationChannel(getApplicationContext());
        } catch (Exception e) {
            System.out.println(">>>>> app plans 起動失敗 >>>>>");
            System.out.println(e.toString());
        }
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
            if (R.id.navigation_calendar == item.getItemId()) {
                showCalendar();
            } else if (item.getItemId() == R.id.navigation_plans_list) {
                // アラーム一覧を表示
                setPlansList();
            } else if (item.getItemId() == R.id.navigation_memo) {
                // メモ一覧を表示
                LinearLayout linearLayout = showMemoList();
                setMemoList(linearLayout);
            }
            return false;
        });
    }

    /**
     * 予定一覧の表示
     *
     * @return 予定一覧の格納先
     */
    private LinearLayout showMemoList() {
        // カレンダーをに非変更
        getCalendarView().setVisibility(View.INVISIBLE);

        // メモ・アラーム一覧の表示領域を表示状態に
        getIncludedListLayout().setVisibility(View.VISIBLE);

        // 予定一覧を非表示に変更
        getListInScrollViewForPlans().setVisibility(View.INVISIBLE);

        // メモ一覧を表示状態に変更
        LinearLayout linearLayoutForPlans = getListInScrollViewForMemo();
        linearLayoutForPlans.setVisibility(View.VISIBLE);
        return linearLayoutForPlans;
    }

    /**
     * 予定一覧の表示
     *
     * @return 予定一覧の格納先
     */
    private LinearLayout showPlansList() {
        // カレンダーをに非変更
        getCalendarView().setVisibility(View.INVISIBLE);

        // メモ・アラーム一覧の表示領域を表示状態に
        getIncludedListLayout().setVisibility(View.VISIBLE);

        // メモ一覧を非表示に変更
        getListInScrollViewForMemo().setVisibility(View.INVISIBLE);

        // 予定一覧を表示状態に変更
        LinearLayout linearLayoutForPlans = getListInScrollViewForPlans();
        linearLayoutForPlans.setVisibility(View.VISIBLE);
        return linearLayoutForPlans;
    }

    /**
     * アラーム一覧の表示
     */
    @SuppressLint("NewApi")
    private void setPlansList() {
        // 予定一覧を表示状態に変更し、予定一覧の LinearLayout を取得
        LinearLayout linearLayout = showPlansList();

        //「新規作成」ボタンの設定（日付は、処理の実行日を設定する）
        setNewAlarmButton(Common.getToday());

        // 予定一覧に、JSON内の予定情報一覧を設定
        setAllPlansList(linearLayout);
    }

    /**
     * メモ一覧の表示
     */
    private void setMemoList(LinearLayout linearLayout) {
        //「新規作成」ボタンの設定
        setNewMemoButton();

        // メモ一覧の格納先を取得
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
            RelativeLayout listLayout = getIncludedListLayout();
            ViewGroup.LayoutParams layoutParams = listLayout.getLayoutParams();
            layoutParams.height = listLayout.getHeight() - getBottomNavigationView().getHeight() - 20;
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
     * メモ・予定一覧内に追加する TextViewのデフォルトを生成
     *
     * @return TextView
     */
    private TextView createDefaultTextViewInList() {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
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
    private void showCalendar() {
        // カレンダーを表示状態に変更
        CalendarView calendarView = getCalendarView();
        calendarView.setVisibility(View.VISIBLE);

        // メモ・アラーム一覧は非表示
        RelativeLayout listLayout = getIncludedListLayout();
        listLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * カレンダー内の日付押下時のイベントを設定
     * →　アラーム一覧を表示
     */
    private void setCalendarEvent() {
        try {
            // カレンダー押下時のイベントを追加
            getCalendarView().setOnDateChangeListener((c, year, month, day) -> {
                // month（月）は、0~11 で処理されるため、+1する
                String strDate = Common.getStrDate(year, month + 1, day);

                // 予定一覧を表示状態に変更
                LinearLayout linearLayout = showPlansList();

                //「新規作成」ボタンの設定
                setNewAlarmButton(strDate);

                // 日付に紐づくアラーム一覧を表示
                setPlansListInDay(linearLayout, strDate);
            });
        } catch (Exception e) {
            System.out.println(">>>>>>>>>>>>>>>error");
            System.out.println(e.toString());
        }
    }

    /**
     * 日付に紐づく予定一覧の表示設定
     *
     * @param linearLayout 予定一覧の LinearLayout
     * @param strDate      アラーム一覧に表示する日付
     */
    private void setPlansListInDay(LinearLayout linearLayout, String strDate) {
        // カレンダーに紐づく JSONを取得
        JsonCalendarManager.JsonCalendarDate calendarDate = JsonCalendarManager.getJson(this);
        if (calendarDate != null) {
            // 日付に紐づく範囲の各値を取得
            JsonCalendarManager.ValuesWithDay valuesWithDay = calendarDate.dayAndValues.get(strDate);
            if (valuesWithDay != null) {
                // 時刻の一覧を画面に表示
                for (Map.Entry<String, JsonCalendarManager.ValuesWithTime> values : valuesWithDay.timeAndValues.entrySet()) {
                    // list_in_scroll_view に対して、TextView を新規に生成して追加
                    setPlanTxtViewInList(linearLayout, strDate, values.getKey(), values.getValue().memo);
                }
            }
        }
        // listLayout の高さを調整
        fixHeightOfListLayout();
    }

    /**
     * 予定一覧に全予定を表示
     */
    private void setAllPlansList(LinearLayout linearLayout) {
        // カレンダーに紐づく JSONを取得
        JsonCalendarManager.JsonCalendarDate calendarDate = JsonCalendarManager.getJson(this);
        if (calendarDate != null) {
            for (Map.Entry<String, JsonCalendarManager.ValuesWithDay> day_value : calendarDate.dayAndValues.entrySet()) {
                for (Map.Entry<String, JsonCalendarManager.ValuesWithTime> time_value : day_value.getValue().timeAndValues.entrySet()) {
                    // 予定一覧に予定を追加
                    setPlanTxtViewInList(linearLayout, day_value.getKey(), time_value.getKey(), time_value.getValue().memo);
                }
            }
        }
        // listLayout の高さを調整
        fixHeightOfListLayout();
    }

    /**
     * アラーム一覧に追加する TextViewの生成
     *
     * @param linearLayout TextViewの挿入先
     * @param strDate      年月日
     * @param strTime      時刻
     * @param strMemo      時刻と紐づくメモ
     */
    private void setPlanTxtViewInList(LinearLayout linearLayout, String strDate, String strTime, String strMemo) {
        TextView view = createDefaultTextViewInList();

        // TextViewには、時刻とメモ内容の一部を表示
        String strTxt = "[day " + strDate + " _Time " + strTime + "] " + Common.getTopTenChar(strMemo);
        view.setText(strTxt);

        // 押下時は、アラーム編集画面へと遷移する
        view.setOnClickListener(v -> {
            // 現処理で使用している値を設定（無関係の設定値の初期化）
            CurrentProcessingData.JsonCurrentProcessData currentProcessData = new CurrentProcessingData.JsonCurrentProcessData();
            currentProcessData.setDate(this, strDate);
            currentProcessData.setTime(this, strTime);

            // アラーム編集画面へ遷移
            startActivity(new Intent(this, AlarmEdit.class));
        });
        linearLayout.addView(view);
    }

    /**
     * アラームの「新規作成」ボタンのイベント設定
     *
     * @param strDate 現処理での日付情報
     */
    private void setNewAlarmButton(String strDate) {
        Button btn = getBtnAddInList();

        // ボタン押下時に、アラーム編集画面に遷移
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

        // メモ一覧の最新化
        LinearLayout forMemo = getListInScrollViewForMemo();
        if (forMemo.getVisibility() == View.VISIBLE) {
            setMemoList(forMemo);
        }

        // 予定一覧の最新化
        LinearLayout forPlans = getListInScrollViewForPlans();
        if (forPlans.getVisibility() == View.VISIBLE) {
            setAllPlansList(forPlans);
        }
    }
}