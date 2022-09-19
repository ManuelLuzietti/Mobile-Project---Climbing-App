package com.example.climbingapp.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.BoulderCardItem;
import com.example.climbingapp.BoulderViewFragment;
import com.example.climbingapp.R;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;
import com.example.climbingapp.Utils;

import java.util.List;

public class BoulderCardAdapter extends RecyclerView.Adapter<BoulderCardViewHolder> {
    private List<BoulderCardItem> list ;
    private View layoutView;
    private Fragment fragment;
    private SelectedBoulderViewModel model;

    public BoulderCardAdapter(List<BoulderCardItem> list, Fragment fragment){
        this.list = list;
        this.fragment = fragment;
        model = new ViewModelProvider(fragment).get(SelectedBoulderViewModel.class);
    }
    @NonNull
    @Override
    public BoulderCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.boulder_card,parent,false);

        return new BoulderCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoulderCardViewHolder holder, int position) {
        BoulderCardItem item = list.get(position);
        holder.place_boulder_name_textview.setText(item.getPlaceName());
        holder.place_user_textview.setText(item.getPlaceUser());
        holder.place_repeats_textview.setText(String.valueOf(item.getPlaceRepeats()));
        holder.place_grade_textview.setText(item.getPlaceGrade());
        holder.place_rating_textview.setText(String.valueOf(item.getPlaceRating()));
        if(!item.isChecked()){
            holder.checkBoulderImage.setVisibility(View.INVISIBLE);
        }
        if(!item.isOfficial()){
            holder.officialBoulderImage.setVisibility(View.INVISIBLE);
        }
        layoutView.setOnClickListener(ev -> {
            model.select(item);
            Utils.insertFragment((AppCompatActivity) fragment.getActivity(),new BoulderViewFragment(),null);
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
