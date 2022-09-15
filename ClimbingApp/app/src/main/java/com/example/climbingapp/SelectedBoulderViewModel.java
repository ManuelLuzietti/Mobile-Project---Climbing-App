package com.example.climbingapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SelectedBoulderViewModel extends ViewModel {
    private final MutableLiveData<BoulderCardItem> boulder =  new MutableLiveData<>();

    public void select(BoulderCardItem boulder){
        this.boulder.setValue(boulder);
    }

    public LiveData<BoulderCardItem> getSelected(){
        return boulder;
    }
}
