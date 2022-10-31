package com.example.climbingapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class FilterViewModel extends ViewModel {
    private final MutableLiveData<String> nameSelected;
    private final MutableLiveData<String> gradeSelected;
    private final MutableLiveData<JSONObject> filterSetting;
    private final MutableLiveData<Integer> ratingSelected;
    public FilterViewModel(){
        gradeSelected = new MutableLiveData<>();
        filterSetting = new MutableLiveData<>();
        ratingSelected = new MutableLiveData<>();
        nameSelected = new MutableLiveData<>();
        filterSetting.setValue(new JSONObject());
        nameSelected.setValue("");
        gradeSelected.setValue("");
        ratingSelected.setValue(0);
        setSettings();
    }

    public void setName(String name){
        nameSelected.setValue(name);
    }

    public LiveData<String> getName(){
        return this.nameSelected;
    }
    public void setGrade(String grade){
        gradeSelected.setValue(grade);
    }

    public LiveData<String> getGrade(){
        return gradeSelected;
    }

    public void setRating(Integer rating){
        ratingSelected.setValue(rating);
    }
    public LiveData<Integer> getRating(){
        return ratingSelected;
    }


    public void setSettings(){
        try {
            JSONObject json = new JSONObject();
            json.put("grade",gradeSelected.getValue());
            json.put("rating",ratingSelected.getValue());
            json.put("name",nameSelected.getValue());
            filterSetting.setValue(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public LiveData<JSONObject> getFilterSettings(){
        return this.filterSetting;
    }


}
