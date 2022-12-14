package com.example.climbingapp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Comment {


    @Ignore
    public Comment(int id, String text, int rating, String grade, int userId) {
        this.id = id;
        this.text = text;
        this.rating = rating;
        this.grade = grade;
        this.userId = userId;
    }
    public Comment( String text, int rating, String grade, int userId) {
        this.text = text;
        this.rating = rating;
        this.grade = grade;
        this.userId = userId;
    }
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String text;

    public int rating;

    public String grade;

    @ColumnInfo(name = "user_id")
    public int userId;

    @Ignore
    private String username;
    @Ignore
    private int numOfTries;
    @Ignore
    private String date;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumOfTries() {
        return numOfTries;
    }

    public void setNumOfTries(int numOfTries) {
        this.numOfTries = numOfTries;
    }


    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public String getGrade() {
        return grade;
    }

    public int getUserId() {
        return userId;
    }


    public static class CommentUpdated{
        public int id;
        public String text;
        public int rating;
        public String grade;
        private String username;
        @ColumnInfo(name = "number_of_tries")
        private int numOfTries;
        private String date;
        @ColumnInfo(name = "user_id")
        public int userId;
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }


        public void setUsername(String username) {
            this.username = username;
        }

        public int getNumOfTries() {
            return numOfTries;
        }

        public void setNumOfTries(int numOfTries) {
            this.numOfTries = numOfTries;
        }


        public String getUsername() {
            return username;
        }

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public int getRating() {
            return rating;
        }

        public String getGrade() {
            return grade;
        }

        public int getUserId() {
            return userId;
        }

    }
}
