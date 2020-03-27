package com.developer.krisi.tasker.model;

import java.time.LocalDateTime;

import androidx.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public static LocalDateTime fromTimestamp(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }

    @TypeConverter
    public static String dateToTimestamp(LocalDateTime date) {
        return date == null ? null : date.toString();
    }
}