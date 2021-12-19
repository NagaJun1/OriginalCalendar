package com.example.plans;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.plans.JsonManagement.JsonCalendarManager;

/**
 * アラーム発生処理
 */
public class FireAnAlarm extends BroadcastReceiver {
    /**
     * アラーム・通知処理
     * （指定した時間になると処理される）
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // todo アラームとしての音楽を鳴らす

        // 処理実行の時刻（日付）に紐づく情報の取得
        JsonCalendarManager.ValuesWithTime valuesWithTime = JsonCalendarManager.getJson(context).getNowValueWithTime();

        // 通知を出力
        createNotification(context, valuesWithTime.memo);
    }

    /**
     * 通知の発行
     */
    @SuppressLint({"NewApi", "LocalSuppress"})
    public void createNotification(Context context, String strText) {
        try {
            Notification.Builder builder = new Notification.Builder(context, Common.CHANNEL_ID);
            if (Common.isEmptyOrNull(strText)) {
                builder.setContentText("not found memo from now time");
            } else {
                builder.setContentText(strText);
            }
            builder.setSmallIcon(R.drawable.baseline_calendar_today_24);

            // 通知出力実行
            NotificationManager notificationManager
                    = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(R.string.app_name, builder.build());
        } catch (Exception e) {
            Toast.makeText(context, "通知作成失敗", Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
        }
    }
}
