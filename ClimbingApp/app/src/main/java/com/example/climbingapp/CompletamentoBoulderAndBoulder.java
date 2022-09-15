package com.example.climbingapp;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CompletamentoBoulderAndBoulder {
    @Embedded public Boulder boulder;
    @Relation(parentColumn = "id",
    entityColumn = "boulder_id")
    public List<CompletamentoBoulder> completamentoBoulder;
}
