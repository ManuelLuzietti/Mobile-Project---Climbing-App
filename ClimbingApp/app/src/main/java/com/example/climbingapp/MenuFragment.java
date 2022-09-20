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

//        List<Boulder> boulders = repository.getBoulders();
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

//    private void updateBoulderCardValues(BoulderCardItem card,Boulder b){
//        repository.getTracciatoreFromBoulder(b.id).observe(this,(User u) ->{
//            if(u != null){
//                card.setPlaceUser(u.username);
//            }
//        });
//
//    }
}