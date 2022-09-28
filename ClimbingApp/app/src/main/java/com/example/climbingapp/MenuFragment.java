package com.example.climbingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.recyclerview.BoulderCardAdapter;

import java.util.List;


public class MenuFragment extends Fragment {
    private RecyclerView recyclerView;
    private BoulderCardAdapter adapter;
    private ClimbingAppRepository repository;
    private Fragment fragment ;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ClimbingAppRepository(getActivity().getApplication());
        fragment = this;


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
        adapter = new BoulderCardAdapter(fragment);
        recyclerView.setAdapter(adapter);
        populateBoulderList();
    }


    private void populateBoulderList(){

        repository.getBoulders().observe(this, new Observer<List<Boulder>>() {
            @Override
            public void onChanged(List<Boulder> boulders) {
                for(Boulder b: boulders){
                    b.updateValues(getActivity().getApplication(), fragment,adapter,boulders.indexOf(b));
                }
                adapter.setData(boulders);
                adapter.notifyDataSetChanged();
            }
        });

    }


}