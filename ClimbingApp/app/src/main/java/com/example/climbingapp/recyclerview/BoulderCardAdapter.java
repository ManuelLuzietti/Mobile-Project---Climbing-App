package com.example.climbingapp.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbingapp.R;
import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BoulderCardAdapter extends RecyclerView.Adapter<BoulderCardViewHolder> {
    private List<Boulder.BoulderUpdated> list;
    private List<Boulder.BoulderUpdated> originalList;
    private View layoutView;
    private SelectedBoulderViewModel model;
    private View parent;
    RecyclerView recyclerView;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public BoulderCardAdapter(Fragment fragment) {
        list = new ArrayList<>();
        originalList = new ArrayList<>();
        if (fragment.getActivity() != null) {
            model = new ViewModelProvider(fragment.getActivity()).get(SelectedBoulderViewModel.class);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BoulderCardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.checkBoulderImage.setVisibility(View.INVISIBLE);
        holder.officialBoulderImage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BoulderCardViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Boulder.BoulderUpdated b = list.get(holder.getAdapterPosition());
        if(b.checked){
            holder.checkBoulderImage.setVisibility(View.VISIBLE);
        }
        if(b.isOfficial){
            holder.officialBoulderImage.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public BoulderCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.boulder_card, parent, false);
        this.parent = parent;
        layoutView.setOnClickListener(view -> {
            int position = recyclerView.getChildLayoutPosition(layoutView);
            Boulder.BoulderUpdated boulder = list.get(position);
            model.select(boulder);
            model.setIndex(boulder);
            NavController navController = null;
            try{
                navController = Navigation.findNavController(parent);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            if(navController!=null){
                navController.navigate(R.id.action_menuFragment_to_boulderViewFragment);
            }
        });
        return new BoulderCardViewHolder(layoutView);
    }


    @Override
    public void onBindViewHolder(@NonNull BoulderCardViewHolder holder, int position) {
        Boulder.BoulderUpdated item = list.get(position);
        holder.place_boulder_name_textview.setText(item.getPlaceName());
        holder.place_user_textview.setText(item.getPlaceUser());
        holder.place_repeats_textview.setText(String.valueOf(item.getPlaceRepeats()));
        holder.place_grade_textview.setText(item.getPlaceGrade());
        holder.place_rating_textview.setText(String.valueOf(item.getPlaceRating()));
        if (!item.isChecked()) {
            holder.checkBoulderImage.setVisibility(View.INVISIBLE);
        }
        if (!item.isOfficial()) {
            holder.officialBoulderImage.setVisibility(View.INVISIBLE);
        }

//        layoutView.setOnClickListener(ev -> {
//            System.out.println(position);
//            System.out.println(((TextView)layoutView.findViewById(R.id.place_boulder_name_textview)).getText().toString());
//            //todo:incasina la lista
//            model.select(item);
//
////            NavHostFragment navHostFragment = (NavHostFragment) FragmentManager.findFragment(parent.getRootView().getRootView().findViewById(R.id.nav_host_fragment_menu));
////            NavController navController = navHostFragment.getNavController();
//            NavController navController = null;
//            try{
//                navController = Navigation.findNavController(parent);
//            } catch (Exception ex){
//                ex.printStackTrace();
//            }
//
//            if(navController!=null){
//                navController.navigate(R.id.action_menuFragment_to_boulderViewFragment);
//            }
//
////            NavHostFragment.findNavController(FragmentManager.findFragment(parent.getRootView().findViewById(R.id.nav_host_fragment_menu))).navigate(R.id.action_menuFragment_to_boulderViewFragment);
//        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public void onViewRecycled(@NonNull BoulderCardViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Boulder.BoulderUpdated> filteredList = new ArrayList<>(originalList);
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(charSequence));
                    if (jsonObject.getInt("rating") != 0) {
                        Iterator<Boulder.BoulderUpdated> iterator = filteredList.iterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().getPlaceRating() != jsonObject.getInt("rating")) {
                                iterator.remove();
                            }
                        }
                    }
                    if (!jsonObject.getString("grade").equals("")) {
                        Iterator<Boulder.BoulderUpdated> iterator = filteredList.iterator();
                        while (iterator.hasNext()) {
                            if (!iterator.next().getPlaceGrade().equals(jsonObject.getString("grade"))) {
                                iterator.remove();
                            }
                        }
                    }
                    if (!jsonObject.getString("name").equals("")) {
                        Iterator<Boulder.BoulderUpdated> iterator = filteredList.iterator();
                        while (iterator.hasNext()) {
                            if (!iterator.next().getPlaceName().contains(jsonObject.getString("name").toLowerCase().trim())) {
                                iterator.remove();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Boulder.BoulderUpdated> filteredList = new ArrayList<>();
                List<?> result = (List<?>) results.values;
                for (Object object : result) {
                    if (object instanceof Boulder.BoulderUpdated) {
                        filteredList.add((Boulder.BoulderUpdated) object);
                    }
                }
                updateCardListItems(filteredList);
            }
        };
    }

    public void updateCardListItems(List<Boulder.BoulderUpdated> filteredList) {
        final BoulderCardDiffCallback diffCallback =
                new BoulderCardDiffCallback(this.list, filteredList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.list = new ArrayList<>(filteredList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void setData(List<Boulder.BoulderUpdated> list) {
        final BoulderCardDiffCallback diffCallback =
                new BoulderCardDiffCallback(this.list, list);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.list = new ArrayList<>(list);
        this.originalList = new ArrayList<>(list);

        diffResult.dispatchUpdatesTo(this);
    }
}
