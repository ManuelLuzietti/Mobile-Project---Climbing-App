package com.example.climbingapp;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.recyclerview.BoulderCardAdapter;
import com.example.climbingapp.viewmodels.FilterViewModel;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONObject;

import java.util.List;

public class MenuFragment extends Fragment {
    private BoulderCardAdapter adapter;
    private ClimbingAppRepository repository;
    private Fragment fragment;
    private InternetManager internetManager;
    private FilterViewModel filterModel;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity()!=null){
            repository = new ClimbingAppRepository(getActivity().getApplication());
        }
        fragment = this;
        setHasOptionsMenu(true);
        filterModel = new ViewModelProvider(getActivity()).get(FilterViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.boulderlist_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_item) {
            if(getActivity()!=null){
                Utils.insertFragment((AppCompatActivity) getActivity(), new FilterFragment(),
                        this.getClass().getSimpleName(), R.id.nav_host_fragment_menu);
            }
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onStart() {
        super.onStart();
        internetManager.registerNetworkCallback();
    }

    @Override
    public void onStop() {
        super.onStop();
        internetManager.unregisterNetworkCallback();
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
        filterModel.getFilterSettings().observe(this, jsonObject -> {
            adapter.getFilter().filter(jsonObject.toString());
        });
        internetManager = new InternetManager(getActivity(), view);
        ((SearchView) view.findViewById(R.id.search_bar)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterModel.setName(s);
                filterModel.setSettings();
                return true;
            }
        });
        ((NavigationBarView) view.findViewById(R.id.bottomnavview_bouldermenu)).setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.reload_boulder_list_item) {
                if (internetManager.isNetworkConnected()) {
                    repository.updateDB();
                } else {
                    internetManager.getSnackbar().show();
                }
            } else {
                return false;
            }
            return true;
        });

    }


    public void setRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.boulders_recycler_view);
        adapter = new BoulderCardAdapter(fragment);
        recyclerView.setAdapter(adapter);
        populateBoulderList();
    }

    private void populateBoulderList() {
        if(getActivity()!=null){
            repository.getBouldersUpdated(getActivity().getSharedPreferences("global_pref",MODE_PRIVATE).getInt("userId",-1)).observe(this, new Observer<List<Boulder.BoulderUpdated>>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onChanged(List<Boulder.BoulderUpdated> boulders) {
                    adapter.setData(boulders);
                    JSONObject filterJson = filterModel.getFilterSettings().getValue();
                    if(filterJson!=null){
                        adapter.getFilter().filter(filterJson.toString());
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }


    }



    @Override
    public void onResume() {
        super.onResume();

    }
}