package com.example.climbingapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "boulder",indices = {@Index(value={"name"},unique = true),@Index(value = {"img"},unique = true)})
public class Boulder {
    @PrimaryKey
    public int id;
    public int tracciatore_id;
    @NonNull
    public String name;
    public int rating;
    @NonNull
    public String grade;
    @NonNull
    public Date date;
    public boolean isOfficial;
    @NonNull
    public String img;
}
