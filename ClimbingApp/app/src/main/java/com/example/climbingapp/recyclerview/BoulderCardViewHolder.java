package com.example.climbingapp.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.R;

public class BoulderCardViewHolder extends RecyclerView.ViewHolder {
    ImageView checkBoulderImage;
    ImageView officialBoulderImage;
    TextView place_boulder_name_textview;
    TextView place_user_textview;
    TextView place_repeats_textview;
    TextView place_grade_textview;
    RatingBar place_rating_ratingbar;

    public BoulderCardViewHolder(@NonNull View itemView) {
        super(itemView);
        checkBoulderImage = itemView.findViewById(R.id.check_boulder_image);
        officialBoulderImage = itemView.findViewById(R.id.official_boulder_image);
        place_boulder_name_textview = itemView.findViewById(R.id.place_boulder_name_textview);
        place_user_textview = itemView.findViewById(R.id.place_user_textview);
        place_repeats_textview = itemView.findViewById(R.id.place_repeats_textview);
        place_grade_textview = itemView.findViewById(R.id.place_grade_textview);
        place_rating_ratingbar = itemView.findViewById(R.id.place_rating_ratingbar_boulder);
    }
}
