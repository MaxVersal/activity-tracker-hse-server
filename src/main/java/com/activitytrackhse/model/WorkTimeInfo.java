package com.activitytrackhse.model;

import lombok.Data;

import java.util.List;

@Data
public class WorkTimeInfo {
    private int total;
    private double totalActivity;
    private int lastWeek;
    private double lastWeekActivity;
    private List<DateTime> allDays;
}
