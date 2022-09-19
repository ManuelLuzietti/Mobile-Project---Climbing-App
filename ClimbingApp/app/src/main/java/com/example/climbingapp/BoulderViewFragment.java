package com.example.climbingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;
import com.google.android.material.navigation.NavigationBarView;


public class BoulderViewFragment extends Fragment {

    private SelectedBoulderViewModel model ;
    public BoulderViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(SelectedBoulderViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_boulder_view, container, false);
        ((NavigationBarView)view.findViewById(R.id.bottomnavview_boulderview)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottomnav_add:
                    break;
                case R.id.bottomnav_comments:
                    break;
                case  R.id.bottomnav_info:
                    break;
                default:
                    return false;

            }
            return true;
        });
        return view;
    }


}