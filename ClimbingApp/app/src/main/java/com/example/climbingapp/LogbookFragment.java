package com.example.climbingapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.recyclerview.BoulderCardLogbookAdapter;


public class LogbookFragment extends Fragment {
    private RecyclerView recyclerView;
    private BoulderCardLogbookAdapter boulderCardLogbookAdapter;
    private ClimbingAppRepository repo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = new ClimbingAppRepository(getActivity().getApplication());

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logbook, container, false);
    }

    public void setRecyclerView(View view){
        recyclerView = view.findViewById(R.id.logbook_recyclerview);
        boulderCardLogbookAdapter = new BoulderCardLogbookAdapter(this);
        recyclerView.setAdapter(boulderCardLogbookAdapter);
        populateBoulderList();
    }

    private void populateBoulderList() {
        repo.getBouldersLogbook(getActivity().getSharedPreferences("global_pref", Context.MODE_PRIVATE).getInt("userId",-1)).observe(this, boulders -> {
            boulderCardLogbookAdapter.setData(boulders);
            boulderCardLogbookAdapter.notifyDataSetChanged();
        });
    }


}