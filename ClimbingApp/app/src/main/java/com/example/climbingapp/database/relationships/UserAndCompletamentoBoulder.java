package com.example.climbingapp.database.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.climbingapp.database.entities.CompletedBoulder;
import com.example.climbingapp.database.entities.User;

import java.util.List;

public class UserAndCompletamentoBoulder {
    @Embedded public User user;
    @Relation(parentColumn = "id",
    entityColumn = "user_id")
    public List<CompletedBoulder> completamentoBoulder;
}
