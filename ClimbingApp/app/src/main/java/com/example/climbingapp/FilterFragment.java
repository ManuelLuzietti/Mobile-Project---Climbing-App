package com.example.climbingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.climbingapp.viewmodels.FilterViewModel;
import com.google.android.material.slider.Slider;

import java.util.Arrays;


public class FilterFragment extends Fragment {
    private Slider gradeSlider ;
    private RatingBar ratingbar;
    private String[] grades;
    private FilterViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity()).get(FilterViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gradeSlider = view.findViewById(R.id.slider_filter);
        grades = getActivity().getResources().getStringArray(R.array.grades);
        gradeSlider.setValueTo(grades.length-1);
        gradeSlider.setLabelFormatter(value -> {
            return grades[(int)value];
        });
        ratingbar = view.findViewById(R.id.rating_bar_filter);
        ratingbar.setRating(model.getRating().getValue());
        gradeSlider.setValue(Arrays.asList(grades).indexOf(model.getGrade().getValue()));
        ((Button)view.findViewById(R.id.filter_apply_button)).setOnClickListener(event->{
            model.setGrade(grades[((int)gradeSlider.getValue())]);
            model.setRating((int)ratingbar.getRating());
            model.setSettings();
            getActivity().onBackPressed();
        });
    }
}