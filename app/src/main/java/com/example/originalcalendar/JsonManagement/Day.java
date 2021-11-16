package com.example.originalcalendar.JsonManagement;

import java.util.List;

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
