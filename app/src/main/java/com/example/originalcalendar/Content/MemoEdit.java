package com.example.originalcalendar.Content;

import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;

public class MemoEdit {
    /**
     * center_layout に他画面で選択された情報に紐づくメモを表示
     * @param linearLayout center_layout
     */
    public static void setMemoEditor(LinearLayout linearLayout, String date, String	time, int dayOfWeek){
        // 編集可能なテキストボックスを生成し、center_layout に追加

        // 日付から曜日を取得
        int week = Common.getDayOfWeek(date);

        /*
        System.out.println("曜日>");
        System.out.println(Common.DAY_OF_WEEK[week]);
         */
    }
}
