package com.example.climbingapp;

import androidx.room.Embedded;
import androidx.room.Relation;

public class CompletamentoBoulderAndCommento {
    @Embedded public Comment comment;
    @Relation(parentColumn = "id",
    entityColumn = "comment_id")
    public CompletamentoBoulder completamentoBoulder;
}
