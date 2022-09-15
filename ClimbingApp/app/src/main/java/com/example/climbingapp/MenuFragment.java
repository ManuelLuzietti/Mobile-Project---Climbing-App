package com.example.climbingapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class MenuFragment extends Fragment {
    private RecyclerView recyclerView;
    private BoulderCardAdapter adapter;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView(view);

    }

    public void setRecyclerView(View view){
        recyclerView = view.findViewById(R.id.boulders_recycler_view);
        recyclerView.setHasFixedSize(true);
        List<BoulderCardItem> l = new ArrayList<>();
        l.add(new BoulderCardItem("1","nome user",10,"7A",5,false,false));
        l.add(new BoulderCardItem("2","nome user",10,"7A",5,false,true));
        l.add(new BoulderCardItem("3","nome user",10,"7A",5,true,false));
        l.add(new BoulderCardItem("4","nome user",10,"7A",5,true,true));

        adapter = new BoulderCardAdapter(l);
        recyclerView.setAdapter(adapter);
    }
}