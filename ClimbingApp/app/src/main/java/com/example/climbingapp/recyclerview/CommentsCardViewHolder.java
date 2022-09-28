package com.example.climbingapp.recyclerview;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.R;

public class CommentsCardViewHolder extends RecyclerView.ViewHolder {
    TextView place_user_textview;
    TextView place_date_textview;
    TextView place_grade_textview;
    TextView place_tries_textview;
    TextView place_text_textview;
    RatingBar place_rating_ratingbar;


    public CommentsCardViewHolder(@NonNull View itemView) {
        super(itemView);
        place_user_textview = itemView.findViewById(R.id.place_user_textview_comment);
        place_date_textview = itemView.findViewById(R.id.place_date_textview_comment);
        place_grade_textview = itemView.findViewById(R.id.place_grade_textview_comment);
        place_tries_textview = itemView.findViewById(R.id.place_tries_textview_comment);
        place_text_textview = itemView.findViewById(R.id.place_text_textview_comment);
        place_rating_ratingbar = itemView.findViewById(R.id.place_rating_ratingbar_comment);
    }
}
