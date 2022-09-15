package com.example.climbingapp;

public class BoulderCardItem {

    private String placeName;
    private String placeUser;
    private int placeRepeats;
    private String placeGrade;
    private int placeRating;
    private boolean isOfficial;
    private boolean isChecked;

    public BoulderCardItem(String name,String user,int repeats,String grade, int rating,boolean official,boolean checked){
        this.placeName = name;
        this.placeUser = user;
        this.placeRepeats = repeats;
        this.placeGrade = grade;
        this.placeRating = rating;
        this.isOfficial = official;
        this.isChecked = checked;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceUser() {
        return placeUser;
    }

    public int getPlaceRepeats() {
        return placeRepeats;
    }

    public String getPlaceGrade() {
        return placeGrade;
    }

    public int getPlaceRating() {
        return placeRating;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isOfficial() {
        return isOfficial;
    }
}
