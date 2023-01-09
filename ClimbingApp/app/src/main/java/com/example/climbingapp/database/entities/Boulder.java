package com.example.climbingapp.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "boulder", indices = {@Index(value = {"name"}, unique = true)})
public class Boulder {
    @Ignore
    public Boulder(int id, String name, @NonNull String grade, @NonNull Date date, boolean isOfficial, @NonNull String img) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.date = date;
        this.isOfficial = isOfficial;
        this.img = img;
    }

    public Boulder(String name, @NonNull String grade, @NonNull Date date, boolean isOfficial, @NonNull String img) {
        this.name = name;
        this.grade = grade;
        this.date = date;
        this.isOfficial = isOfficial;
        this.img = img;
    }

    @PrimaryKey(autoGenerate = true)
    public int id ;
    public String name;
    @NonNull
    public String grade;
    @NonNull
    @TypeConverters(com.example.climbingapp.database.TypeConverters.class)
    public Date date;
    @ColumnInfo(name = "is_official")
    public boolean isOfficial;

    @NonNull
    public String img;



    public int getId() {
        return id;
    }

    public String getPlaceName() {
        return name;
    }



    public String getPlaceGrade() {
        return grade;
    }


    @NonNull
    public String getImg() {
        return img;
    }



    public boolean isOfficial() {
        return isOfficial;
    }


    public void setPlaceName(String name) {
        this.name = name;
    }



    public void setPlaceGrade(String grade) {
        this.grade = grade;
    }


    public void setImg(@NonNull String img) {
        this.img = img;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }


    public static class BoulderUpdated extends Boulder{

        public String user;
        public int rating;
        @ColumnInfo(name = "completions")
        public int repeats;
        public boolean checked;

        public BoulderUpdated(int id, String name, String grade, Date date, boolean isOfficial, String img, String user, int rating, int repeats, boolean checked) {
            super(id,name,grade,date,isOfficial,img);
            this.user = user;
            this.rating = rating;
            this.repeats = repeats;
            this.checked = checked;
        }

        public int getId() {
            return id;
        }

        public String getPlaceName() {
            return name;
        }

        public String getPlaceUser() {
            return user;
        }

        public int getPlaceRepeats() {
            return repeats;
        }

        public String getPlaceGrade() {
            return grade;
        }

        public int getPlaceRating() {
            return rating;
        }

        public String getImg() {
            return img;
        }


        public boolean isChecked() {
            return checked;
        }

        public boolean isOfficial() {
            return isOfficial;
        }


        public void setPlaceName(String name) {
            this.name = name;
        }

        public void setPlaceUser(String user) {
            this.user = user;
        }

        public void setPlaceRepeats(int repeats) {
            this.repeats = repeats;
        }

        public void setPlaceGrade(String grade) {
            this.grade = grade;
        }

        public void setPlaceRating(int rating
        ) {
            this.rating = rating;
        }

        public void setImg( String img) {
            this.img = img;
        }

        public void setOfficial(boolean official) {
            isOfficial = official;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }


    }


    public static  class BoulderLogbook extends  BoulderUpdated{


        @ColumnInfo(name = "number_of_tries")
        public int numberOfTries;
        @ColumnInfo(name = "completion_date")
        @TypeConverters(com.example.climbingapp.database.TypeConverters.class)
        public Date dateCompletion;
        @ColumnInfo(name = "rating_user")
        public int ratingUser;
        @ColumnInfo(name = "grade_user")
        public String gradeUser;
        @ColumnInfo(name = "comment_text")
        public String commentText;

        public BoulderLogbook(int id, String name, String grade, Date date, boolean isOfficial, String img, String user, int rating, int repeats, boolean checked,
                              int numberOfTries,Date dateCompletion,int ratingUser,String gradeUser,String commentText) {
            super(id, name, grade, date, isOfficial, img, user, rating, repeats, checked);
            this.numberOfTries = numberOfTries;
            this.dateCompletion = dateCompletion;
            this.ratingUser = ratingUser;
            this.gradeUser = gradeUser;
            this.commentText = commentText;
        }

        public int getNumberOfTries(){
            return numberOfTries;
        }

        public Date getDateCompletion(){
            return dateCompletion;
        }

        public int ratingUser(){
            return ratingUser;
        }

        public String getGradeUser(){
            return gradeUser;
        }

        public String getCommentText(){
            return commentText;
        }


        public int getId() {
            return id;
        }

        public String getPlaceName() {
            return name;
        }


        public int getPlaceRepeats() {
            return repeats;
        }

        public String getPlaceGrade() {
            return grade;
        }

        public int getPlaceRating() {
            return rating;
        }

        public String getImg() {
            return img;
        }


        public boolean isChecked() {
            return checked;
        }

        public boolean isOfficial() {
            return isOfficial;
        }


        public void setPlaceName(String name) {
            this.name = name;
        }

        public void setPlaceUser(String user) {
            this.user = user;
        }

        public void setPlaceRepeats(int repeats) {
            this.repeats = repeats;
        }

        public void setPlaceGrade(String grade) {
            this.grade = grade;
        }

        public void setPlaceRating(int rating) {
            this.rating = rating;
        }

        public void setImg( String img) {
            this.img = img;
        }

        public void setOfficial(boolean official) {
            isOfficial = official;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }
    }
}
