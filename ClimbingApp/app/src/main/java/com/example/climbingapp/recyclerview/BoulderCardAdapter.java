package com.example.climbingapp.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.BoulderViewFragment;
import com.example.climbingapp.R;
import com.example.climbingapp.Utils;
import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BoulderCardAdapter extends RecyclerView.Adapter<BoulderCardViewHolder> {
    private List<Boulder> list  ;
    private List<Boulder> originalList;
    private View layoutView;
    private Fragment fragment;
    private SelectedBoulderViewModel model;

    public BoulderCardAdapter( Fragment fragment){
        list = new ArrayList<>();
        originalList = new ArrayList<>();
        this.fragment = fragment;
        model = new ViewModelProvider(fragment.getActivity()).get(SelectedBoulderViewModel.class);
    }
    @NonNull
    @Override
    public BoulderCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.boulder_card,parent,false);
        return new BoulderCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoulderCardViewHolder holder, int position) {
        Boulder item = list.get(position);
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
        layoutView.setOnClickListener(ev -> {
            model.select(item);
            Utils.insertFragment((AppCompatActivity) fragment.getActivity(),new BoulderViewFragment(),null,R.id.nav_host_fragment_menu);
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

//    public void updateCardListItems(List<Boulder> list) {
//        setData(list);
//    }

//    public void setData(List<Boulder> list){
//        final BoulderCardDiffCallback diffCallback =
//                new BoulderCardDiffCallback(this.list, list);
//        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//
//        this.list = new ArrayList<>(list);
//
//        diffResult.dispatchUpdatesTo(this);
//    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Boulder> filteredList = new ArrayList<>();
                List<Boulder> filteredFilteredList = new ArrayList<>();

                if(String.valueOf(charSequence).startsWith("name:")){
                    if (charSequence == null || charSequence.length() == 0) {
                        filteredList.addAll(originalList);
                    } else {
                        String filterPattern = charSequence.toString().toLowerCase().trim();
                        if(filterPattern.contains("name:")){
                            String pattern = filterPattern.replace("name:","");
                            for (Boulder item : originalList) {
                                if (item.getPlaceName().toLowerCase().contains(pattern) ) {
                                    filteredList.add(item);
                                }
                            }
                        }
                    }
                    filteredFilteredList.addAll(filteredList);
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(charSequence));
                        if(jsonObject.getInt("rating")== 0){
                            filteredList.addAll(originalList);
                        } else {
                            for(Boulder item : originalList){
                                if( item.getPlaceRating()==jsonObject.getInt("rating")){
                                    filteredList.add(item);
                                }
                            }
                        }
                        if(!jsonObject.getString("grade").equals("")){
                            for(Boulder item : filteredList){
                                if(item.getPlaceGrade().equals(jsonObject.getString("grade"))){
                                    filteredFilteredList.add(item);
                                }
                            }
                        } else {
                            filteredFilteredList.addAll(filteredList);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //if you have no constraint --> return the full list
//                if (filterPattern.contains("grade:")){
//                        String pattern = filterPattern.replace("grade:","");
//                        for (Boulder item : originalList) {
//                            if (item.getPlaceGrade().toLowerCase().contains(pattern) ) {
//                                filteredList.add(item);
//                            }
//                        }
//                    }
//                }
//

                FilterResults results = new FilterResults();
                results.values = filteredFilteredList;
                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Boulder> filteredList = new ArrayList<>();
                List<?> result = (List<?>) results.values;
                for (Object object : result) {
                    if (object instanceof Boulder) {
                        filteredList.add((Boulder) object);
                    }
                }

                //warn the adapter that the data are changed after the filtering
                updateCardListItems(filteredList);
            }
        };
    }

     public void updateCardListItems(List<Boulder> filteredList) {
            final BoulderCardDiffCallback diffCallback =
                    new BoulderCardDiffCallback(this.list, filteredList);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            this.list = new ArrayList<>(filteredList);
            diffResult.dispatchUpdatesTo(this);
        }
    //
    //    /**
    //     * Method that set the list in the Home
    //     * @param list the list to display in the home
    //     */
        public void setData(List<Boulder> list){
            final BoulderCardDiffCallback diffCallback =
                    new BoulderCardDiffCallback(this.list, list);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

            this.list = new ArrayList<>(list);
            this.originalList = new ArrayList<>(list);

            diffResult.dispatchUpdatesTo(this);
        }
}
