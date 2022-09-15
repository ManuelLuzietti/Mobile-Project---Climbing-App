package com.example.climbingapp;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserAndCompletamentoBoulder {
    @Embedded public User user;
    @Relation(parentColumn = "id",
    entityColumn = "user_id")
    public List<CompletamentoBoulder> completamentoBoulder;
}
