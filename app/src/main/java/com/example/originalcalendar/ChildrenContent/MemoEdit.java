package com.example.originalcalendar.ChildrenContent;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MemoEdit {
    /**
     * center_layout に、他画面で選択された情報に紐づくメモを表示
     * @param centerLayout center_layout
     */
    public static void setMemoEditor(LinearLayout centerLayout, String date, String time, int dayOfWeek){
        // center_layoutの内部を空にする
        centerLayout.removeAllViews();

        // 編集可能なテキストボックスを生成し、center_layout に追加
        EditText editText = createEditText(centerLayout.getContext());
        centerLayout.addView(editText);

        // 保存されている情報から、該当する情報を取得し、editTextに設定
        editText.setText(Common.getTextInRecord(date,time,dayOfWeek));
    }

    /**
     * EditTextを新規に生成
     * @return 生成したEditText
     */
    private static EditText createEditText(Context context){
        EditText editText = new EditText(context);
        editText.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );
        return editText;
    }
}
