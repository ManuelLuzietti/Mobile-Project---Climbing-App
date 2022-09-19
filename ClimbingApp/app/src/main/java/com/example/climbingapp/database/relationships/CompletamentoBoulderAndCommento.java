package com.example.climbingapp.database.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.climbingapp.database.entities.Comment;
import com.example.climbingapp.database.entities.CompletedBoulder;

public class CompletamentoBoulderAndCommento {
    @Embedded public Comment comment;
    @Relation(parentColumn = "id",
    entityColumn = "comment_id")
    public CompletedBoulder completamentoBoulder;
}
