package com.example.originalcalendar.JsonManagement;

import java.util.List;

/**
 * JsonManagerのプロパティとしてのクラスを一覧として保持
 */
public class JsonProperty {
    /**
     * 一日分の JSON
     */
    public class Day {
        /**
         * 日付、もしくは、曜日（1~7）
         */
        public String day = null;

        /**
         * 時刻に紐づく JSON (配列）
         */
        public List<ByDay> byDay = null;
    }

    /**
     * 日付・曜日に紐づく JSON
     */
    public class ByDay {
        /**
         * 日付（文字列）
         */
        public String time = null;

        /**
         * 時間に紐づく JSON
         */
        public ByTime byTime = null;
    }

    /**
     * 時刻に紐づく JSON
     */
    public class ByTime {
        /**
         * メモのテキスト情報
         */
        public String memo = null;
        /**
         * アラームON/OFF のフラグ
         */
        public boolean flgAlarm = true;
        /**
         * 通知ON/OFF のフラグ
         */
        public boolean flgNotice = true;
        /**
         * リピートON/OFF のフラグ
         */
        public boolean flgRepeat = false;
    }
}
