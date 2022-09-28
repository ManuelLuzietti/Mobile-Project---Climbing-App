package com.example.climbingapp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
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

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "comment_id")
    public Integer   commentId;
    @TypeConverters(com.example.climbingapp.database.TypeConverters.class)
    public Date date;
    @ColumnInfo(name = "user_id")
    public int userId;
    @ColumnInfo(name = "boulder_id")
    public int boulderId;
    @ColumnInfo(name = "number_of_tries")
    public int numberOfTries;

    @Ignore
    public CompletedBoulder(int id,int userId, int boulderId, Date date, Integer commentId,int numberOfTries) {
        this.id = id;
        this.userId = userId;
        this.boulderId = boulderId;
        this.date = date;
        this.commentId = commentId;
        this.numberOfTries = numberOfTries;
    }
    public CompletedBoulder(int userId, int boulderId, Date date, Integer commentId,int numberOfTries) {
        this.userId = userId;
        this.boulderId = boulderId;
        this.date = date;
        this.commentId = commentId;
        this.numberOfTries = numberOfTries;
    }




}
