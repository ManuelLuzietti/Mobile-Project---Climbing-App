package com.example.climbingapp;

import androidx.room.Embedded;
import androidx.room.Relation;

public class BoulderAndTracciatura {
    @Embedded
    public Boulder boulder;
    @Relation(entity = TracciaturaBoulder.class,entityColumn = "boulder_id",parentColumn = "id")
    public TracciaturaBoulder tracciaturaBoulder;
}
