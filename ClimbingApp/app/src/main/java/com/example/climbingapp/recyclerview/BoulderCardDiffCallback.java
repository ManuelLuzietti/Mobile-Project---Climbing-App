package com.example.climbingapp.recyclerview;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.climbingapp.database.ClimbingDAO;

import java.util.List;

public class BoulderCardDiffCallback extends DiffUtil.Callback {
    private List<ClimbingDAO.BoulderUpdated> oldBoulderList;
    private List<ClimbingDAO.BoulderUpdated> newBoulderList;

    public BoulderCardDiffCallback(List<ClimbingDAO.BoulderUpdated> oldBoulderList, List<ClimbingDAO.BoulderUpdated> newBoulderList){
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
        ClimbingDAO.BoulderUpdated newItem = newBoulderList.get(newItemPosition);
        ClimbingDAO.BoulderUpdated oldItem = oldBoulderList.get(oldItemPosition);

        return newItem.getPlaceName().equals(oldItem.getPlaceName()) &&
                newItem.getId() == oldItem.getId() &&
                newItem.getPlaceUser() == oldItem.getPlaceUser() &&
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
