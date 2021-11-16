package com.example.originalcalendar.JsonManagement;

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
