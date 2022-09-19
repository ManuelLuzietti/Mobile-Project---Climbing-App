package com.example.climbingapp;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.example.climbingapp.database.ClimbingDAO;
import com.example.climbingapp.database.ClimbingRoomDatabase;

public class ClimbingRepository {
    private ClimbingDAO climbingDao;
    private RequestQueue requestQueue;

    public ClimbingRepository(Application app){
        climbingDao = ClimbingRoomDatabase.getDatabase(app.getApplicationContext()).getDao();
        requestQueue = VolleySingleton.getInstance(app.getApplicationContext());
    }

    public void getDumpDBFromInternet(){

    }
}
