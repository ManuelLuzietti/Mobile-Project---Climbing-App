package com.example.climbingapp;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TracciaturaBoulder {
    @Embedded User user;
    @Relation( parentColumn = "id",
    entityColumn = "tracciatore_id")
    public List<Boulder> boulder;
}
