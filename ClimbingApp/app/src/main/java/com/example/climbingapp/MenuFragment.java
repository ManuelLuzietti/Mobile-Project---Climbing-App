package com.example.climbingapp;

import android.content.Context;
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

import java.util.ArrayList;
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
        recyclerView.setHasFixedSize(true);
        populateBoulderList();
//        List<BoulderCardItem> l = populateBoulderList();
//        l.add(new BoulderCardItem("1","nome user",10,"7A",5,false,false));
//        l.add(new BoulderCardItem("2","nome user",10,"7A",5,false,true));
//        l.add(new BoulderCardItem("3","nome user",10,"7A",5,true,false));
//        l.add(new BoulderCardItem("4","nome user",10,"7A",5,true,true));


//        adapter = new BoulderCardAdapter(l,this);
//        recyclerView.setAdapter(adapter);
    }


    //todo: finire di sistemare problema con ricevere dati da db e popolare la gui senza bloccare main thread.
    private void populateBoulderList(){
        int id_user = getActivity().getPreferences(Context.MODE_PRIVATE).getInt("id",-1);

        List<BoulderCardItem> list = new ArrayList<>();
//        List<Boulder> boulders = repository.getBoulders();
        repository.getBoulders().observe(this, new Observer<List<Boulder>>() {
            @Override
            public void onChanged(List<Boulder> boulders) {
                for(Boulder b: boulders){
                    String tracciatore = repository.getTracciatoreFromBoulder(b.id);
                    int repeats = repository.getRepeatsNumberOfBoulder(b.id);
                    boolean checked;
                    if(id_user != -1){
                        checked = repository.isBoulderCompletedByUser(id_user,b.id);
                    } else {
                        checked = false;
                    }
                    BoulderCardItem cardItem = new BoulderCardItem(b.name,tracciatore,repeats,b.grade,b.rating,b.isOfficial,checked);
                    list.add(cardItem);
                }
                adapter = new BoulderCardAdapter(list,fragment);
                recyclerView.setAdapter(adapter);
            }
        });


    }
}