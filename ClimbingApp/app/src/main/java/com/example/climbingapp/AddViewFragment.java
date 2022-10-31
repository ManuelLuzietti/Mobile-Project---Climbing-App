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

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.climbingapp.database.TypeConverters;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddViewFragment extends Fragment {
    private SelectedBoulderViewModel model;
    private ClimbingAppRepository repo;
    private int user_id;
    private InternetManager internetManager;


    @Override
    public void onStart() {
        super.onStart();
        internetManager.registerNetworkCallback();
    }

    @Override
    public void onStop() {
        super.onStop();
        internetManager.unregisterNetworkCallback();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = new ViewModelProvider(this.getActivity()).get(SelectedBoulderViewModel.class);
            repo = new ClimbingAppRepository(this.getActivity().getApplication());
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        internetManager = new InternetManager(getActivity(),view);
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

    private boolean checkUserLoggedIn() {
        user_id = Objects.requireNonNull(this.getActivity()).getSharedPreferences("global_pref",Context.MODE_PRIVATE).getInt("userId",-1);
        if(user_id == -1) {
            if (getView() != null) {
                TextView warning_message = ((TextView)getView().findViewById(R.id.warning_massage_addview));
                warning_message.setText("User not logged in");
                warning_message.setVisibility(View.VISIBLE);
                getView().findViewById(R.id.button_add_view).setOnClickListener(null);
            }
            return false;
        }
        return true;
    }



    private void setListeners() {
        View view = getView();
        if (view != null) {
            view.findViewById(R.id.button_add_view).setOnClickListener(event->{
                String text = ((TextView)view.findViewById(R.id.text_comment_addview)).getText().toString();
                String grade = ((TextView)view.findViewById(R.id.grade_menu_textview)).getText().toString();
                String tries =  ((TextView)view.findViewById(R.id.tries_menu_textview)).getText().toString();
                float rating =  ((RatingBar)view.findViewById(R.id.rating_bar_add_view)).getRating();
                try {
                    if(!checkInput(grade,tries,rating)){
                        return;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                insertCompletion(text,grade,tries,rating);
            });
        }

    }

    private void insertCompletion(String text, String grade, String tries, float rating) {
        if (model.getSelected().getValue() == null) {
            return;
        }
        String url = InternetManager.URL + "?method=insert_completedboulder" +
                "&rating=" + String.valueOf((int)rating)+
                "&grade=" + grade +
                "&user_id=" + String.valueOf(user_id)+
                "&date=" + TypeConverters.toString(new Date()) +
                "&boulder_id=" + String.valueOf(model.getSelected().getValue().id)+
                "&number_of_tries=" + tries;
        if(!text.equals(getString(R.string.comment_request_addview))){
            url += "&text=" + text;
        } else {
            url += "&text=\"\"";
        }


        StringRequest insertCompletionRequest = new StringRequest(Request.Method.GET,url,response -> {
            if (getActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            repo.updateDB();
            builder.setTitle("Congratulation for the ascent");
            builder.setCancelable(false);
            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                getActivity().onBackPressed();
            });
            AlertDialog dialog = builder.create();
            if(response.equals("success")){
                builder.setMessage("boulder logged with success");
            } else {
                builder.setMessage("Boulder not logged, please retry later.");
            }
            dialog.show();
        }, Throwable::printStackTrace);
        if(internetManager.isNetworkConnected()){
            VolleySingleton.getInstance(getContext()).add(insertCompletionRequest);
        }else {
            internetManager.getSnackbar().show();
        }

    }

    private boolean checkInput( String grade, String tries,float rating) throws Exception {
        boolean valid = true;
        View view = getView();
        if(view==null){
            throw new Exception();
        }
        View gradeWarning = view.findViewById(R.id.warning_grade_message_addview);
        View triesWarning = view.findViewById(R.id.warning_tries_message_addview);
        View ratingWarning = view.findViewById(R.id.warning_rating_addview);
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



    private void checkAlreadyCompletedByUser()  {
        if(model.getSelected().getValue()==null){
            return;
        }
        int boulderId = model.getSelected().getValue().getId();
        repo.getBoulderIfCompletedByUser(this.user_id,boulderId).observe(this,boulders -> {
                if (boulders.size()!=0){
                    View view = getView();
                    if(view==null){
                        return;
                    }
                    view.findViewById(R.id.warning_massage_addview).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.button_add_view).setOnClickListener(null);
                }
            });

    }

}