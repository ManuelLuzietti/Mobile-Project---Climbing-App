package com.example.climbingapp;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {


    public static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag,int fragmentContainer){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer,fragment,tag);
        if(!(fragment instanceof MenuFragment)){
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public static Date stringToDate(String date){

        try {
            return  new SimpleDateFormat("yyyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int numOfTriesConversion(String tries){
        switch (tries) {
            case "flash":
                return 1;
            case "second attempt":
                return 2;
            case "third attempt":
                return 3;
            case "more than three":
                return 4;
            default:
                throw new RuntimeException();
        }
    }

    public static boolean isUserLoggedIn(Activity activity) {
        int id = activity.getSharedPreferences("global_pref",Context.MODE_PRIVATE).getInt("userId",-1);
        return id != -1;
    }

    public static void logOutUser(Activity activity){
        activity.getSharedPreferences("global_pref",Context.MODE_PRIVATE).edit().remove("userId").commit();

    }
}
