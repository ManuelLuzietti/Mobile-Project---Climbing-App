package com.example.climbingapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.climbingapp.database.entities.Boulder;

public class SelectedBoulderViewModel extends ViewModel {
    private final MutableLiveData<Boulder> boulder =  new MutableLiveData<>();

    public void select(Boulder boulder){
        this.boulder.setValue(boulder);
    }

    public LiveData<Boulder> getSelected(){
        return boulder;
    }
}
