package com.activitytrackhse.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Map;

@Data
public class DateTime {
    private Date date;
    private int workTime;
    private double activity;

    public DateTime(Date date, int workTime, double activity) {
        this.date = date;
        this.workTime = workTime;
        this.activity = activity;
    }

    public static DateTime get(Map<String, Object> data){
        return new DateTime((Date) data.get("date"), ((BigInteger) data.get("work_time")).intValue(), ((BigDecimal) data.get("activity")).doubleValue());
    }
}
