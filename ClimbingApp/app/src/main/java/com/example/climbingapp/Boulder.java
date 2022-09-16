package com.example.climbingapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "boulder",indices = {@Index(value={"name"},unique = true),@Index(value = {"img"},unique = true)})
public class Boulder {
    public Boulder(  String name, int rating, @NonNull String grade, @NonNull Date date, boolean isOfficial, @NonNull String img) {
        this.name = name;
        this.rating = rating;
        this.grade = grade;
        this.date = date;
        this.isOfficial = isOfficial;
        this.img = img;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;


    public String name;
    public int rating;
    @NonNull
    public String grade;
    @NonNull
    @TypeConverters(com.example.climbingapp.database.TypeConverters.class)
    public Date date;
    public boolean isOfficial;
    @NonNull
    public String img;


}
