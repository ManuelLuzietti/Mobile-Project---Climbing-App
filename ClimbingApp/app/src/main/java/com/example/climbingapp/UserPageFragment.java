package com.example.climbingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserPageFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.insertFragment((AppCompatActivity) getActivity(),new PersonalInfoFragment(),this.getClass().getSimpleName(),R.id.fragment_container_view_userpage);
        ((BottomNavigationView)view.findViewById(R.id.bottomnavview_userpage)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottomnav_info_user:
                    Utils.insertFragment((AppCompatActivity) getActivity(),new PersonalInfoFragment(),this.getClass().getSimpleName(),R.id.fragment_container_view_userpage);
                    break;
                case R.id.bottomnav_logbook_user:
                    Utils.insertFragment((AppCompatActivity) getActivity(),new LogbookFragment(),this.getClass().getSimpleName(),R.id.fragment_container_view_userpage);
                    break;
                case R.id.bottomnav_trofei_user:
                    Utils.insertFragment((AppCompatActivity) getActivity(),new TrofeiFragment(),this.getClass().getSimpleName(),R.id.fragment_container_view_userpage);
                    break;
                default:
                    return false;
            }
            return true;
        });

    }
}