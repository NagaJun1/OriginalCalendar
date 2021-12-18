package com.example.plans;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plans.JsonManagement.CurrentProcessingData;
import com.example.plans.JsonManagement.JsonCalendarManager;

/**
 * アラーム編集画面に対応した処理
 */
public class AlarmEdit extends AppCompatActivity {
    /**
     * 　center_time_picker の取得
     */
    private TimePicker getCenterClock() {
        TimePicker timePicker = findViewById(R.id.center_time_picker);
        if (timePicker == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " center_time_picker", Toast.LENGTH_LONG).show();
        }
        return timePicker;
    }

    /**
     * 　top_day_text の取得
     */
    private TextView getTopDayTextView() {
        TextView textView = findViewById(R.id.top_day_text);
        if (textView == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " top_day_text", Toast.LENGTH_LONG).show();
        }
        return textView;
    }

    /**
     * 　back_from_edit_alarm の取得
     */
    private Button getBtnBackFromEditAlarm() {
        Button btn = findViewById(R.id.back_from_edit_alarm);
        if (btn == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " back_from_edit_alarm", Toast.LENGTH_LONG).show();
        }
        return btn;
    }

    /**
     * 　back_from_edit_alarm の取得
     */
    private Button getBtnGoEditMemo() {
        Button btn = findViewById(R.id.go_edit_memo);
        if (btn == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " go_edit_memo", Toast.LENGTH_LONG).show();
        }
        return btn;
    }

    /**
     * check_alarm_sound の取得
     */
    private CheckBox getCheckAlarmSound() {
        CheckBox chkBox = findViewById(R.id.check_alarm_sound);
        if (chkBox == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " check_alarm_sound", Toast.LENGTH_LONG).show();
        }
        return chkBox;
    }

    /**
     * check_notification の取得
     */
    private CheckBox getCheckNotification() {
        CheckBox chkBox = findViewById(R.id.check_notification);
        if (chkBox == null) {
            Toast.makeText(this, Common.NOT_FOUND_VIEW + " check_notification", Toast.LENGTH_LONG).show();
        }
        return chkBox;
    }

    /**
     * 現画面の時刻を"時:分"の文字列として取得
     *
     * @return "時:分"の文字列
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private String getClockTime() {
        TimePicker centerClock = getCenterClock();
        return Common.getStrTime(centerClock.getHour(), centerClock.getMinute());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_alarm);

        // 初期化処理
        initViews();
    }

    /**
     * 初期化処理
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        CurrentProcessingData.JsonCurrentProcessData jsonCurrent = CurrentProcessingData.getJSON(this);

        // カレンダーに紐づく各値を取得
        JsonCalendarManager.JsonCalendarDate jsonCalendarDate = JsonCalendarManager.getJson(this);

        // ON/OFFのチェックボックスを設定
        setChkBox(jsonCurrent, jsonCalendarDate);

        // 日付表示を設定
        if (!Common.isEmptyOrNull(jsonCurrent.getStrDate())) {
            getTopDayTextView().setText(jsonCurrent.getStrDate());
        }

        // center_time_pickerの取得と設定
        setCenterClock(jsonCurrent);

        // "戻る"メニューを設定
        setBackEvent();

        // メモ編集へ遷移するボタンのイベント設定
        setGoEditMemo(jsonCurrent);
    }

    /**
     * 画面上のチェックボックスの設定
     */
    private void setChkBox(
            CurrentProcessingData.JsonCurrentProcessData currentData,
            JsonCalendarManager.JsonCalendarDate calendarDate) {
        // 現行の処理で保持している日付情報の取得
        String strDay = currentData.strDay();

        // 日付に紐づく各値を取得
        JsonCalendarManager.ValuesWithDay valuesWithDay = calendarDate.dayAndValues.get(strDay);
        if (valuesWithDay != null) {
            // 時刻に紐づく各値を取得
            JsonCalendarManager.ValuesWithTime valuesWithTime = valuesWithDay.timeAndValues.get(currentData.getStrTime());
            if (valuesWithTime != null) {
                // 保存情報の中にチェックボックスの状態が保存されていた場合は、既存の設定を使用する
                getCheckNotification().setChecked(valuesWithTime.flgNotice);
                getCheckAlarmSound().setChecked(valuesWithTime.flgAlarm);
            }
        }
    }

    /**
     * center_time_picker の設定
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setCenterClock(CurrentProcessingData.JsonCurrentProcessData jsonCurrentProcessData) {
        TimePicker centerClock = getCenterClock();

        // 24時間表示で固定する
        centerClock.setIs24HourView(true);
        if (!Common.isEmptyOrNull(jsonCurrentProcessData.getStrTime())) {
            // center_time_picker の時刻を設定
            centerClock.setHour(Common.getHourInTime(jsonCurrentProcessData.getStrTime()));
            centerClock.setMinute(Common.getMinutesInTime(jsonCurrentProcessData.getStrTime()));
        }
    }

    /**
     * back_from_edit_memo に対して、イベントを設定
     */
    private void setBackEvent() {
        Button button = getBtnBackFromEditAlarm();
        button.setOnClickListener(v -> {
            // 現画面を終了して、元の画面に戻る
            finish();
        });
    }

    /**
     * メモ画面へ遷移する処理を追加
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setGoEditMemo(CurrentProcessingData.JsonCurrentProcessData jsonCurrentData) {
        Button btn = getBtnGoEditMemo();

        // メモ編集画面から遷移してきた場合は、ボタンを非表示にする
        if (jsonCurrentData.getAlreadyOpenEditMemo()) {
            btn.setVisibility(View.INVISIBLE);
        } else {
            // メモ編集画面への遷移処理の実装
            btn.setOnClickListener(v -> {
                // アラーム編集画面表示済みフラグをON
                jsonCurrentData.setOpenEditAlarm(this, true);
                startActivity(new Intent(this, MemoEdit.class));
            });
        }
    }

    /**
     * 画面復帰時（メモ編集画面から戻ってきたとき？）
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 編集内容を保存する
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPause() {
        super.onPause();

        // 現行処理の情報の取得（時刻情報が変更前の情報の取得）
        CurrentProcessingData.JsonCurrentProcessData currentData = CurrentProcessingData.getJSON(this);
        String strDay = currentData.strDay();

        // カレンダーに紐づく全データ（JSON）の取得
        JsonCalendarManager.JsonCalendarDate calendarDate = JsonCalendarManager.getJson(this);

        // カレンダーに紐づく情報の中から、現画面表示時に設定されていた時刻情報を削除
        // 削除した時刻に紐づいていた ValuesWithTime を取得
        JsonCalendarManager.ValuesWithTime valuesWithTime = calendarDate.deleteTime(this, strDay, currentData.getStrTime());

        // 画面上のチェックボックスの状態を JSON に反映
        valuesWithTime.flgAlarm = getCheckAlarmSound().isChecked();
        valuesWithTime.flgNotice = getCheckNotification().isChecked();

        // 現画面で設定されている最新の時刻を取得
        String newTime = getClockTime();

        // 現画面の情報を使用して、JsonCalendarManagerに各値を保存
        calendarDate.setValuesWithTime(this, strDay, newTime, valuesWithTime);

        // 現行処理管理用の JSON に時刻、曜日を保存
        currentData.setTime(this, newTime);

        // TODO 曜日の設定
//        CurrentProcessingData.setDayOfWeek(this, 0);
    }
}
