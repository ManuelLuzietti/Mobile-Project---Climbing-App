package com.example.climbingapp.recyclerview;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.climbingapp.database.entities.Comment;

import java.util.List;

public class CommentCardDiffCallback extends DiffUtil.Callback {

    private List<Comment> oldCommentList;
    private List<Comment> newCommentList;

    public CommentCardDiffCallback(List<Comment> oldCommentList, List<Comment> newCommentList) {
        this.oldCommentList = oldCommentList;
        this.newCommentList = newCommentList;
    }

    @Override
    public int getOldListSize() {
        return this.oldCommentList.size();
    }

    @Override
    public int getNewListSize() {
        return this.newCommentList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
      return oldCommentList.get(oldItemPosition) == newCommentList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Comment oldItem = oldCommentList.get(oldItemPosition);
        Comment newItem = newCommentList.get(newItemPosition);
        return oldItem.getId() == newItem.getId() &&
                oldItem.getText().equals(newItem.getText()) &&
                oldItem.getRating() == newItem.getRating() &&
                oldItem.getGrade().equals(newItem.getGrade()) &&
                oldItem.getUsername().equals(newItem.getUsername()) &&
                oldItem.getNumOfTries() == newItem.getNumOfTries() &&
                oldItem.getDate().equals(newItem.getDate());    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
