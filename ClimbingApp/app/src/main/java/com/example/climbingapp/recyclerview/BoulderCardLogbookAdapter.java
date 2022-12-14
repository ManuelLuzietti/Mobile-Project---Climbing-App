package com.example.climbingapp.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.BoulderViewFragment;
import com.example.climbingapp.R;
import com.example.climbingapp.Utils;
import com.example.climbingapp.database.TypeConverters;
import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;

import java.util.ArrayList;
import java.util.List;

public class BoulderCardLogbookAdapter extends RecyclerView.Adapter<BoulderCardLogbookViewholder> {
    private final Fragment fragment;
    private List<Boulder.BoulderLogbook> list;
    private final SelectedBoulderViewModel model;
    private View layoutView;


    public BoulderCardLogbookAdapter(Fragment fragment) throws Exception {
        list = new ArrayList<>();
        this.fragment = fragment;
        if (fragment.getActivity() != null) {
            model = new ViewModelProvider(fragment.getActivity()).get(SelectedBoulderViewModel.class);
        } else {
            throw new Exception();
        }
    }

    @NonNull
    @Override
    public BoulderCardLogbookViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.boulder_card_logbook, parent, false);
        return new BoulderCardLogbookViewholder(layoutView);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BoulderCardLogbookViewholder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.checkBoulderImage.setVisibility(View.INVISIBLE);
        holder.officialBoulderImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BoulderCardLogbookViewholder holder) {
        super.onViewAttachedToWindow(holder);
        Boulder.BoulderUpdated b = list.get(holder.getAdapterPosition());
        if (b.checked) {
            holder.checkBoulderImage.setVisibility(View.VISIBLE);
        }
        if (b.isOfficial) {
            holder.officialBoulderImage.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull BoulderCardLogbookViewholder holder, int position) {
        Boulder.BoulderLogbook item = list.get(position);
        holder.place_boulder_name_textview.setText(item.getPlaceName());
        holder.place_user_textview.setText(fragment.getString(R.string.boulderCard_tracciatoreTextView) + item.getPlaceUser());
        holder.place_repeats_textview.setText(fragment.getString(R.string.boulderCard_repeatsTextView) + String.valueOf(item.getPlaceRepeats()));
        holder.place_grade_textview.setText(fragment.getString(R.string.boulderCard_repeatsTextView) + item.getPlaceGrade());
        holder.place_rating_ratingbar.setRating(item.getPlaceRating());
        if (!item.isChecked()) {
            holder.checkBoulderImage.setVisibility(View.INVISIBLE);
        }
        if (!item.isOfficial()) {
            holder.officialBoulderImage.setVisibility(View.INVISIBLE);
        }

        if (item.getCommentText().equals("null")) {
            holder.commentImage.setVisibility(View.INVISIBLE);
        } else {
            holder.commentImage.setOnClickListener(view -> {
                if (fragment.getContext() != null) {
                    new AlertDialog.Builder(fragment.getContext()).setTitle("Comment").setMessage(item.getCommentText()).show();
                }
            });
        }
        holder.ratingBar.setRating(item.ratingUser);
        holder.place_gradeuser.setText(fragment.getString(R.string.logbookCard_gradeProposed) + item.gradeUser);
        holder.place_triesuser.setText(fragment.getString(R.string.logbookCard_numOfTries) + Utils.numOfTriesConversion(item.getNumberOfTries() + 1));
        holder.place_dateuser.setText(fragment.getString(R.string.logbookCard_completionDate) + TypeConverters.toString(item.dateCompletion));
        holder.ratingProposedLabel.setText(fragment.getString(R.string.logbookCard_ratingProposedLabel));
        layoutView.setOnClickListener(ev -> {
            Boulder.BoulderUpdated boulder = new Boulder.BoulderUpdated(item.id, item.name, item.grade, item.date, item.isOfficial, item.img, item.user, item.rating, item.repeats, item.checked);
            model.select(boulder);
            if (fragment.getActivity() != null) {
                Utils.insertFragment((AppCompatActivity) fragment.getActivity(), new BoulderViewFragment(), null, R.id.nav_host_fragment_menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void setData(List<Boulder.BoulderLogbook> boulders) {
        this.list = boulders;
    }
}
