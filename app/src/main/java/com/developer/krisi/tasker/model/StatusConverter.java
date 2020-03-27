package com.developer.krisi.tasker.model;

import androidx.room.TypeConverter;

public class StatusConverter {
    @TypeConverter
    public static Status fromString(String value) {
        return value == null ? null : Status.valueOf(value.toUpperCase());
    }

    @TypeConverter
    public static String statusToString(Status status) {
        return status == null ? null : status.getStatusName();
    }
}
