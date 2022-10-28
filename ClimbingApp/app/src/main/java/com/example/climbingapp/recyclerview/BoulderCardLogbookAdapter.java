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
    private  Fragment fragment;
    private List<Boulder.BoulderLogbook> list;
    private List<Boulder.BoulderLogbook> originalList;
    private SelectedBoulderViewModel model;
    private View layoutView;


    public BoulderCardLogbookAdapter(Fragment fragment){
        list = new ArrayList<>();
        originalList = new ArrayList<>();
        this.fragment = fragment;
        model = new ViewModelProvider(fragment.getActivity()).get(SelectedBoulderViewModel.class);
    }
    @NonNull
    @Override
    public BoulderCardLogbookViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.boulder_card_logbook,parent,false);
        return new BoulderCardLogbookViewholder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoulderCardLogbookViewholder holder, int position) {
        Boulder.BoulderLogbook item = list.get(position);
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

        if(item.getCommentText().equals("null")){
            holder.commentImage.setVisibility(View.INVISIBLE);
        } else{
            holder.commentImage.setOnClickListener(view ->{
                new AlertDialog.Builder(fragment.getContext()).setTitle("Comment").setMessage(item.getCommentText()).show();
            });
        }
        holder.ratingBar.setRating(item.ratingUser);
        holder.place_gradeuser.setText(item.gradeUser);
        holder.place_triesuser.setText(Utils.numOfTriesConversion(item.getNumberOfTries()));//todo:converti
        holder.place_dateuser.setText(TypeConverters.toString(item.dateCompletion));
        layoutView.setOnClickListener(ev -> {
            Boulder.BoulderUpdated boulder = new Boulder.BoulderUpdated(item.id,item.name,item.grade,item.date,item.isOfficial,item.img,item.user,item.rating,item.repeats,item.checked);
            model.select(boulder);
            Utils.insertFragment((AppCompatActivity) fragment.getActivity(),new BoulderViewFragment(),null,R.id.nav_host_fragment_menu);
            //NavHostFragment.findNavController(FragmentManager.findFragment(layoutView)).navigate(R.id.action_logbookFragment_to_boulderViewFragment);
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
