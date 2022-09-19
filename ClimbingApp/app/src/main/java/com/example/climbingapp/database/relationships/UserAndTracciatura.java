package com.example.climbingapp.database.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.climbingapp.database.entities.TracciaturaBoulder;
import com.example.climbingapp.database.entities.User;

import java.util.List;

public class UserAndTracciatura {
    @Embedded
    public User user;
    @Relation(entity = User.class, parentColumn = "id",entityColumn = "user_id")
    public List<TracciaturaBoulder> tracciature;
}
