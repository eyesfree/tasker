package com.developer.krisi.tasker.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(String value) {
        if(value != null) {
            Long milliseconds = Long.parseLong(value);
            Timestamp timestamp = new Timestamp(milliseconds);
            return timestamp == null ? null : timestamp;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static Date fromTimestamp(long milliseconds) {
        Timestamp timestamp = new Timestamp(milliseconds);
        return timestamp == null ? null : timestamp;
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        if(date != null) {
            Long milliseconds = date.getTime();
            return milliseconds == null ? null : milliseconds.toString();
        } else {
            return null;
        }
    }
}