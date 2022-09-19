package com.example.climbingapp.database;

import androidx.room.TypeConverter;

import com.example.climbingapp.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeConverters {
    @TypeConverter
    public static Date toDate(String date){
        return date == null ? null : Utils.stringToDate(date);
    }

    @TypeConverter
    public static String toString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return date == null ? null : formatter.format(date);
    }
}
