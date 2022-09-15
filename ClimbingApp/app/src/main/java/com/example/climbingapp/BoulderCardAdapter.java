package com.example.climbingapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BoulderCardAdapter extends RecyclerView.Adapter<BoulderCardViewHolder> {
    private List< BoulderCardItem> list ;
    public BoulderCardAdapter(List<BoulderCardItem> list){
        this.list = list;
    }
    @NonNull
    @Override
    public BoulderCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.boulder_card,parent,false);
        layoutView.setOnClickListener(ev-> {
            Log.d("pressed","bho");
        });
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
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
