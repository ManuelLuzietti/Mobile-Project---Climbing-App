package com.example.climbingapp;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserAndComment {
    @Embedded public User user;
    @Relation(
            parentColumn = "id",
            entityColumn = "user_id"
    )
    public List<Comment> comment;
}
