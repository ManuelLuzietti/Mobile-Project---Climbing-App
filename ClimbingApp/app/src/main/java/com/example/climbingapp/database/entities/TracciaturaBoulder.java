package com.example.climbingapp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tracciatura_boulder",
indices = {@Index(value={"user_id","boulder_id"},unique = true)},
foreignKeys = {@ForeignKey(entity = User.class,parentColumns = "id",childColumns = "user_id",onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Boulder.class,parentColumns = "id",childColumns = "boulder_id",onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE)})
public class TracciaturaBoulder {

    @Ignore
    public TracciaturaBoulder(int idTracciatura,int userId, int boulderId) {
        this.idTracciatura = idTracciatura;
        this.userId = userId;
        this.boulderId = boulderId;
    }
    public TracciaturaBoulder(int userId, int boulderId) {
        this.userId = userId;
        this.boulderId = boulderId;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_tracciatura")
    public int idTracciatura;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "boulder_id")
    public int boulderId;

}
