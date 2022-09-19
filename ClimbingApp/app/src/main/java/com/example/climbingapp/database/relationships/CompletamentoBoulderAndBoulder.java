package com.example.climbingapp.database.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.database.entities.CompletedBoulder;

import java.util.List;

public class CompletamentoBoulderAndBoulder {
    @Embedded public Boulder boulder;
    @Relation(parentColumn = "id",
    entityColumn = "boulder_id")
    public List<CompletedBoulder> completamentoBoulder;
}
