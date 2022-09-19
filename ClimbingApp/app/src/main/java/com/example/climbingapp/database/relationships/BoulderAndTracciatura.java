package com.example.climbingapp.database.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.database.entities.TracciaturaBoulder;

public class BoulderAndTracciatura {
    @Embedded
    public Boulder boulder;
    @Relation(entity = TracciaturaBoulder.class,entityColumn = "boulder_id",parentColumn = "id")
    public TracciaturaBoulder tracciaturaBoulder;
}
