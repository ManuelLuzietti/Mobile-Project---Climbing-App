package com.example.climbingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;


public class PersonalInfoFragment extends Fragment {

    private String username;
    private String firstName;
    private String lastName;
    private TextInputEditText usernameEt;
    private TextInputEditText firstNameEt;
    private TextInputEditText lastNameEt;
    private String id;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences global_pref = getActivity().getApplication().getSharedPreferences("global_pref", Context.MODE_PRIVATE);
        username = global_pref.getString("username","");
        firstName = global_pref.getString("first_name","");
        lastName = global_pref.getString("last_name","");
        id = String.valueOf(global_pref.getInt("userId",-1));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameEt = view.findViewById(R.id.username_textInputEditText_userPage);
        firstNameEt = view.findViewById(R.id.firstname_textInputEditText_userPage);
        lastNameEt = view.findViewById(R.id.lastname_textInputEditText_userPage);
        usernameEt.setText(username);
        firstNameEt.setText(firstName);
        lastNameEt.setText(lastName);
        view.findViewById(R.id.update_button_personalinfo).setOnClickListener(event->{
            String usernameText = usernameEt.getText().toString();
           if(!usernameText.equals(username)){
               updateUsername(usernameText);
           }
           String firstnameText = firstNameEt.getText().toString();
            if(!firstnameText.equals(firstName)){
                updateFirstname(firstnameText);
            }
            String lastnameText = lastNameEt.getText().toString();
            if(!lastnameText.equals(lastName)){
                updateLastname(lastnameText);
            }
        });


    }

    private void updateLastname(String lastnameText) {
        StringRequest updateLastnameReq = new StringRequest(Request.Method.GET,InternetManager.URL+"?method=update_lastname&lastname="+lastnameText+"&id="+id,response -> {
            checkResponse(response);
        },error -> {
            error.printStackTrace();
        });
        VolleySingleton.getInstance(getContext()).add(updateLastnameReq);
    }



    private void updateFirstname(String firstnameText) {
        StringRequest updateFirstnameReq = new StringRequest(Request.Method.GET,InternetManager.URL+"?method=update_firstname&firstname="+firstnameText+"&id="+id,response -> {
            checkResponse(response);
        },error -> {
            error.printStackTrace();
        });
        VolleySingleton.getInstance(getContext()).add(updateFirstnameReq);
    }

    private void updateUsername(String usernameText) {
        StringRequest updateUsernameReq = new StringRequest(Request.Method.GET,InternetManager.URL+"?method=update_username&username="+usernameText+"&id="+id,response -> {
            checkResponse(response);
        },error -> {
            error.printStackTrace();
        });
        VolleySingleton.getInstance(getContext()).add(updateUsernameReq);

    }

    private void checkResponse(String response) {
        switch (response) {
            case "update success username":
                Toast.makeText(getContext(),"update username success",Toast.LENGTH_SHORT).show();
                updatePreference("username",usernameEt.getText().toString());
                break;
            case "update success firstname":
                Toast.makeText(getContext(),"update firstname success",Toast.LENGTH_SHORT).show();
                updatePreference("firstname",firstNameEt.getText().toString());
                break;
            case "update success lastname":
                Toast.makeText(getContext(),"update lastname success",Toast.LENGTH_SHORT).show();
                updatePreference("lastname",lastNameEt.getText().toString());
                break;
            case "username already taken":
                Toast.makeText(getContext(),"username already taken",Toast.LENGTH_SHORT).show();
            default:
                Toast.makeText(getContext(),   "update fail", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePreference(String key,String value){
        SharedPreferences global_pref = getContext().getSharedPreferences("global_pref", Context.MODE_PRIVATE);
        global_pref.edit().putString(key,value);
    }
}