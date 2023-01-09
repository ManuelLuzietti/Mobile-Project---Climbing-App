package com.example.climbingapp.viewmodels;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.climbingapp.database.entities.Boulder;

import java.util.ArrayList;
import java.util.List;

public class SelectedBoulderViewModel extends ViewModel {
    private List<? extends Boulder.BoulderUpdated> boulderList = new ArrayList<>();
    private final MutableLiveData<Boulder.BoulderUpdated> boulder =  new MutableLiveData<>();
    private final MutableLiveData<Bitmap> bitmap = new MutableLiveData<>();
    private int boulderListIndex;
    private int maxIndex ;

    public SelectedBoulderViewModel(){
        this.boulderListIndex = 0;
        this.maxIndex = 0;
    }

    public void select(Boulder.BoulderUpdated boulder){
        this.boulder.setValue(boulder);
    }

    public LiveData<Boulder.BoulderUpdated> getSelected(){
        return boulder;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap.setValue(bitmap);
    }
    public LiveData<Bitmap> getBitmap(){
        return this.bitmap;
    }

    public void setBoulderList(@NonNull  List<? extends Boulder.BoulderUpdated> list){
        this.boulderList = list;
        this.maxIndex = Math.max(list.size() - 1, 0);
    }

    public void setIndex(Boulder.BoulderUpdated boulder){
        this.boulderListIndex = this.boulderList.indexOf(boulder);
    }
    public void increaseIndex(){
        this.boulderListIndex = Math.min(this.boulderListIndex+1,this.maxIndex);
        select(boulderList.get(boulderListIndex));
    }

    public void decreaseIndex(){
        this.boulderListIndex = Math.max(this.boulderListIndex -1,0);
        select(boulderList.get(boulderListIndex));
    }

}
