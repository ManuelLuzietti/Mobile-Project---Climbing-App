package com.example.climbingapp;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class UserAndTracciatura {
    @Embedded
    public User user;
    @Relation(entity = User.class, parentColumn = "id",entityColumn = "user_id")
    public List<TracciaturaBoulder> tracciature;
}
