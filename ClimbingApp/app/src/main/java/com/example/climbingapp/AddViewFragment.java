package com.example.climbingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.climbingapp.database.entities.Comment;
import com.example.climbingapp.database.entities.CompletedBoulder;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AddViewFragment extends Fragment {
    private SelectedBoulderViewModel model;
    private ClimbingAppRepository repo;
    private int user_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this.getActivity()).get(SelectedBoulderViewModel.class);
        repo = new ClimbingAppRepository(this.getActivity().getApplication());

    }


    private boolean checkUserLoggedIn() {
        user_id = Objects.requireNonNull(this.getActivity()).getPreferences(Context.MODE_PRIVATE).getInt("id",-1);
        if(user_id == -1) {
            TextView warning_message = ((TextView)getView().findViewById(R.id.warning_massage_addview));
            warning_message.setText("User not logged in");
            warning_message.setVisibility(View.VISIBLE);
            getView().findViewById(R.id.button_add_view).setOnClickListener(null);
            return false;
        }
        return true;
    }



    private void setListeners() {
        View view = getView();
        view.findViewById(R.id.button_add_view).setOnClickListener(event->{
            String text = ((TextView)view.findViewById(R.id.text_comment_addview)).getText().toString();
            String grade = ((TextView)view.findViewById(R.id.grade_menu_textview)).getText().toString();
            String tries =  ((TextView)view.findViewById(R.id.tries_menu_textview)).getText().toString();
            float rating =  ((RatingBar)view.findViewById(R.id.rating_bar_add_view)).getRating();
            if(!checkInput(grade,tries,rating)){
                return;
            }
            insertCompletion(text,grade,tries,rating);
        });
    }

    private void insertCompletion(String text, String grade, String tries, float rating) {
//        if(!text.equals(getString(R.string.comment_request_addview))){
//           //todo:continuare da qui
//        }
//        try {
//            long idreturned = repo.insertBoulder(new Boulder("el gato loco","7a",new Date(),false,"cione.png")).get();
//            System.out.println(idreturned);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Long idInsertedComment = null;
        if(!text.equals(getString(R.string.comment_request_addview))){
            try {
                idInsertedComment = repo.insertComment(new Comment(text,(int)rating,grade,user_id)).get();
            } catch(ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
        }
        repo.insertCompletedBoulder(new CompletedBoulder(user_id,model.getSelected().getValue().id,new Date(),idInsertedComment == null ? null : idInsertedComment.intValue(),Utils.numOfTriesConversion(tries)));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("boulder logged with success");
        builder.setTitle("Congratulation for the ascent");
        builder.setCancelable(false);
        builder.setPositiveButton("ok",(dialogInterface, i) -> {
            getActivity().onBackPressed();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean checkInput( String grade, String tries,float rating) {
        boolean valid = true;
        View gradeWarning = getView().findViewById(R.id.warning_grade_message_addview);
        View triesWarning = getView().findViewById(R.id.warning_tries_message_addview);
        View ratingWarning = getView().findViewById(R.id.warning_rating_addview);
       if(grade.equals(getString(R.string.grade_request_addview))){
           gradeWarning.setVisibility(View.VISIBLE);
           valid = false;
       } else {
           gradeWarning.setVisibility(View.INVISIBLE);
       }
       if(tries.equals(getString(R.string.request_tries_addview))){
           triesWarning.setVisibility(View.VISIBLE);
           valid = false;
       } else {
           triesWarning.setVisibility(View.INVISIBLE);

       }
       if(rating == 0){
           ratingWarning.setVisibility(View.VISIBLE);
           valid = false;
       } else{
           ratingWarning.setVisibility(View.INVISIBLE);
       }
       return valid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        try {
            if(checkUserLoggedIn()){
                checkAlreadyCompletedByUser();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        List<String> grades = Arrays.asList(getResources().getStringArray(R.array.grades));
        ArrayAdapter gradesAdapter = new ArrayAdapter(getContext(), R.layout.option_layout, grades);
         AutoCompleteTextView gradesTv = view.findViewById(R.id.grade_menu_textview);
        gradesTv.setAdapter(gradesAdapter);
        List<String> tries = Arrays.asList(getResources().getStringArray(R.array.num_of_tries));
        ArrayAdapter triesAdapter = new ArrayAdapter(getContext(), R.layout.option_layout, tries);
        AutoCompleteTextView triesTv = view.findViewById(R.id.tries_menu_textview);
        triesTv.setAdapter(triesAdapter);

    }

    private void checkAlreadyCompletedByUser() throws Exception {
        int boulderId = model.getSelected().getValue().getId();
        repo.getBoulderIfCompletedByUser(this.user_id,boulderId).observe(this,boulders -> {
                if (boulders.size()!=0){
                    getView().findViewById(R.id.warning_massage_addview).setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.button_add_view).setOnClickListener(null);
                }
            });

    }

}