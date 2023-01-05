package com.example.climbingapp.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.R;

public class BoulderCardLogbookViewholder extends RecyclerView.ViewHolder {
    ImageView checkBoulderImage;
    ImageView officialBoulderImage;
    TextView place_boulder_name_textview;
    TextView place_user_textview;
    TextView place_repeats_textview;
    TextView place_grade_textview;
    RatingBar place_rating_ratingbar;
    TextView ratingProposedLabel;
    ImageView commentImage;
    RatingBar ratingBar;
    TextView place_gradeuser;
    TextView place_triesuser;
    TextView place_dateuser;


    public BoulderCardLogbookViewholder(@NonNull View itemView) {
        super(itemView);
        checkBoulderImage = itemView.findViewById(R.id.check_boulder_image_logbook);
        officialBoulderImage = itemView.findViewById(R.id.official_boulder_image_logbook);
        place_boulder_name_textview = itemView.findViewById(R.id.place_boulder_name_textview_logbook);
        place_user_textview = itemView.findViewById(R.id.place_user_textview_logbook);
        place_repeats_textview = itemView.findViewById(R.id.place_repeats_textview_logbook);
        place_grade_textview = itemView.findViewById(R.id.place_grade_textview_logbook);
        place_rating_ratingbar = itemView.findViewById(R.id.place_rating_ratingbar_logbook);
        ratingProposedLabel = itemView.findViewById(R.id.gradeProposed_textView_logbook);

        commentImage = itemView.findViewById(R.id.comment_boulder_logbook);
        ratingBar = itemView.findViewById(R.id.place_ratinguser_ratingbar_logbook);
        place_gradeuser = itemView.findViewById(R.id.place_gradeuser_textview_logbook);
        place_triesuser = itemView.findViewById(R.id.place_triesuser_textview_logbook);
        place_dateuser = itemView.findViewById(R.id.place_dateuser_textview_logbook);
    }




}
