package com.example.climbingapp.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.climbingapp.database.ClimbingDAO;

public class SelectedBoulderViewModel extends ViewModel {
    private final MutableLiveData<ClimbingDAO.BoulderUpdated> boulder =  new MutableLiveData<>();
    private final MutableLiveData<Bitmap> bitmap = new MutableLiveData<>();

    public void select(ClimbingDAO.BoulderUpdated boulder){
        this.boulder.setValue(boulder);

    }

    public LiveData<ClimbingDAO.BoulderUpdated> getSelected(){
        return boulder;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap.setValue(bitmap);
    }
    public LiveData<Bitmap> getBitmap(){
        return this.bitmap;
    }

//    public void setBitmap(Bitmap bitmap){
//        this.bitmap.setValue(bitmap);
//    }
//
//    public LiveData<Bitmap> getBitmap(){
//        return this.bitmap;
//    }
}
