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
    private Context thisContext = null;

    /**
     * アラーム・通知処理
     * （指定した時間になると処理される）
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        thisContext = context;

        // 処理実行の時刻（日付）に紐づく情報の取得
        JsonCalendarManager.ValuesWithTime valuesWithTime = JsonCalendarManager.getJson(context).getNowValueWithTime();

        // 通知を出力
        createNotification(valuesWithTime.memo);

        // アラーム停止処理の実装
        if (valuesWithTime.flgAlarm) {
//            thisContext.startActivity(new Intent(thisContext, StopAlarm.class));
            StopAlarm.startMusic(context);
        }
    }

    /**
     * 通知の発行
     */
    @SuppressLint({"NewApi", "LocalSuppress"})
    public void createNotification(String strText) {
        try {
            // 通知出力実行
            NotificationManager notificationManager
                    = (NotificationManager) thisContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(R.string.app_name, getNotification(strText));
        } catch (Exception e) {
            Toast.makeText(thisContext, "通知作成失敗", Toast.LENGTH_LONG).show();
            System.out.println(e.toString());
        }
    }

    /**
     * 通知を生成
     */
    @SuppressLint("NewApi")
    private Notification getNotification(String strText) {
        // "CHANNEL_PLANS"で通知を作成
        Notification.Builder builder = new Notification.Builder(thisContext, Common.CHANNEL_ID);

        // 通知に表示するテキストの設定
        if (Common.isEmptyOrNull(strText)) {
            builder.setContentText("not found memo from now time");
        } else {
            builder.setContentText(strText);
        }
        // 通知のアイコン
        builder.setSmallIcon(R.drawable.baseline_calendar_today_24);

        // 通知押下時に削除する挙動の設定
        builder.setAutoCancel(true);
        return builder.build();
    }
}
