package com.example.climbingapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment")
public class Comment {
    @PrimaryKey
    public int id;

    public String text;

    public int rating;

    public String grade;

    @ColumnInfo(name = "user_id")
    public int userId;
}
