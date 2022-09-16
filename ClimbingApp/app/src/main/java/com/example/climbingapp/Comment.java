package com.example.climbingapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment",
foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "id",childColumns = "user_id",onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE)})
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public Comment(String text, int rating, String grade, int userId) {
        this.text = text;
        this.rating = rating;
        this.grade = grade;
        this.userId = userId;
    }

    public String text;

    public int rating;

    public String grade;

    @ColumnInfo(name = "user_id")
    public int userId;


}
