package com.example.climbingapp.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.R;
import com.example.climbingapp.database.entities.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsCardAdapter extends RecyclerView.Adapter<CommentsCardViewHolder> {
    private List<Comment.CommentUpdated> list;

    public CommentsCardAdapter(){
        list = new ArrayList<>();
    }
    @NonNull
    @Override
    public CommentsCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_card,parent,false);
        return new CommentsCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsCardViewHolder holder, int position) {
        Comment.CommentUpdated item = list.get(position);
        holder.place_user_textview.setText( item.getUsername());
        holder.place_grade_textview.setText("grade: " + item.getGrade()+" - ");
        holder.place_rating_ratingbar.setRating(item.getRating());
        holder.place_text_textview.setText(item.getText());
        holder.place_tries_textview.setText("# of tries: " + String.valueOf(item.getNumOfTries()));
        holder.place_date_textview.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Comment.CommentUpdated> list){
        final CommentCardDiffCallback diffCallback =
                new CommentCardDiffCallback(this.list, list);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.list = new ArrayList<>(list);
        diffResult.dispatchUpdatesTo(this);
    }

}
