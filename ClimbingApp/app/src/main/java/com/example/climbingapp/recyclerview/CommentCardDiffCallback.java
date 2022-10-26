package com.example.climbingapp.recyclerview;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.climbingapp.database.entities.Comment;

import java.util.List;

public class CommentCardDiffCallback extends DiffUtil.Callback {

    private List<Comment.CommentUpdated> oldCommentList;
    private List<Comment.CommentUpdated> newCommentList;

    public CommentCardDiffCallback(List<Comment.CommentUpdated> oldCommentList, List<Comment.CommentUpdated> newCommentList) {
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
        Comment.CommentUpdated oldItem = oldCommentList.get(oldItemPosition);
        Comment.CommentUpdated newItem = newCommentList.get(newItemPosition);
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
