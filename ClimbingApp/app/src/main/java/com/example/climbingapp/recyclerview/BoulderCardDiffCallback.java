package com.example.climbingapp.recyclerview;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.climbingapp.database.entities.Boulder;

import java.util.List;

public class BoulderCardDiffCallback extends DiffUtil.Callback {
    private List<Boulder> oldBoulderList;
    private List<Boulder> newBoulderList;

    public BoulderCardDiffCallback(List<Boulder> oldBoulderList, List<Boulder> newBoulderList){
        this.newBoulderList = newBoulderList;
        this.oldBoulderList = oldBoulderList;
    }
    @Override
    public int getOldListSize() {
        return this.oldBoulderList.size();
    }

    @Override
    public int getNewListSize() {
        return this.newBoulderList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBoulderList.get(oldItemPosition) == newBoulderList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Boulder newItem = newBoulderList.get(newItemPosition);
        Boulder oldItem = oldBoulderList.get(oldItemPosition);
        return newItem.getPlaceName().equals(oldItem.getPlaceName()) &&
                newItem.getId() == oldItem.getId() &&
                newItem.getPlaceUser().equals(oldItem.getPlaceUser()) &&
                newItem.getPlaceRepeats() == oldItem.getPlaceRepeats() &&
                newItem.getPlaceGrade().equals(oldItem.getPlaceGrade()) &&
                newItem.getPlaceRating() == oldItem.getPlaceRating() &&
                newItem.isChecked() == oldItem.isChecked() &&
                newItem.isOfficial() == oldItem.isOfficial;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
