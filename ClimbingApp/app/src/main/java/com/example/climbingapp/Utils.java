package com.example.climbingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Utils {

    public static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView,fragment,tag);
        if(!(fragment instanceof MenuFragment)){
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }
}
