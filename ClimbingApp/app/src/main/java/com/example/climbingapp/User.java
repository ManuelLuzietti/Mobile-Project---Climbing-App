package com.example.climbingapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="user")
public class User {

    public User(int id, String username, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @PrimaryKey
    public int id;

    @ColumnInfo(name="username")
    public String username;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;
}
