package com.example.climbingapp.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.climbingapp.database.entities.Boulder;

public class SelectedBoulderViewModel extends ViewModel {
    private final MutableLiveData<Boulder> boulder =  new MutableLiveData<>();
    private final MutableLiveData<Bitmap> bitmap = new MutableLiveData<>();

    public void select(Boulder boulder){
        this.boulder.setValue(boulder);

    }

    public LiveData<Boulder> getSelected(){
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
