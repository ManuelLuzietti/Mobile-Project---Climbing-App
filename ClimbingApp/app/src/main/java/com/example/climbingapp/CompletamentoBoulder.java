package com.example.climbingapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "completamento_boulder",
    primaryKeys = {"user_id","boulder_id"},
    foreignKeys = {@ForeignKey(entity = User.class,parentColumns = "id",childColumns = "user_id",onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE),
                    @ForeignKey(entity = Boulder.class,parentColumns = "id",childColumns = "boulder_id",onUpdate = ForeignKey.CASCADE,onDelete = ForeignKey.CASCADE),
    @ForeignKey(entity = Comment.class,parentColumns = "id",childColumns = "comment_id",onUpdate = ForeignKey.CASCADE,onDelete = ForeignKey.SET_NULL)})
public class CompletamentoBoulder {
    @ColumnInfo(name = "user_id")
    public int userId;
    @ColumnInfo(name = "boulder_id")
    public int boulderId;
    @TypeConverters(com.example.climbingapp.database.TypeConverters.class)
    public Date date;

    public CompletamentoBoulder(int userId, int boulderId, Date date, int commentId) {
        this.userId = userId;
        this.boulderId = boulderId;
        this.date = date;
        this.commentId = commentId;
    }

    @ColumnInfo(name = "comment_id")
    public int commentId;


}
