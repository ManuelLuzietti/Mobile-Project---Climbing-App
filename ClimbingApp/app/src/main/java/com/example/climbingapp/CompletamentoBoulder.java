package com.example.climbingapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.util.Date;

@Entity(tableName = "completamento_boulder",
    primaryKeys = {"user_id","boulder_id"},
    foreignKeys = {@ForeignKey(entity = User.class,parentColumns = "id",childColumns = "user_id",onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE),
                    @ForeignKey(entity = Boulder.class,parentColumns = "id",childColumns = "boulder_id",onUpdate = ForeignKey.CASCADE,onDelete = ForeignKey.CASCADE)})
public class CompletamentoBoulder {
    @ColumnInfo(name = "user_id")
    public int userId;
    @ColumnInfo(name = "boulder_id")
    public int boulderId;
    public Date date;
    @ColumnInfo(name = "comment_id")
    public int commentId;

}
