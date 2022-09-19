package com.example.climbingapp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "completed_boulder",
   indices = {@Index(value = {"user_id","boulder_id"},unique = true)},
    foreignKeys = {@ForeignKey(entity = User.class,parentColumns = "id",childColumns = "user_id",onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE),
                    @ForeignKey(entity = Boulder.class,parentColumns = "id",childColumns = "boulder_id",onUpdate = ForeignKey.CASCADE,onDelete = ForeignKey.CASCADE),
    @ForeignKey(entity = Comment.class,parentColumns = "id",childColumns = "comment_id",onUpdate = ForeignKey.CASCADE,onDelete = ForeignKey.SET_NULL)})
public class CompletedBoulder {

    @PrimaryKey
    public int id;
    @ColumnInfo(name = "comment_id")
    public int commentId;
    @TypeConverters(com.example.climbingapp.database.TypeConverters.class)
    public Date date;
    @ColumnInfo(name = "user_id")
    public int userId;
    @ColumnInfo(name = "boulder_id")
    public int boulderId;


    public CompletedBoulder(int id,int userId, int boulderId, Date date, int commentId) {
        this.id = id;
        this.userId = userId;
        this.boulderId = boulderId;
        this.date = date;
        this.commentId = commentId;
    }




}
