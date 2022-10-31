package com.example.climbingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {
    private TextView warningMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity()!=null){
            if(Utils.isUserLoggedIn(getActivity())){
                Intent intent = new Intent(getContext(),MainMenuActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText etUsername = view.findViewById(R.id.username_loginview);
        EditText etPassword = view.findViewById(R.id.password_loginview);
         warningMessage = view.findViewById(R.id.warning_message_loginview);
        view.findViewById(R.id.login_button).setOnClickListener(event -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            if (username.equals("") || password.equals("")) {
                warningMessage.setVisibility(View.VISIBLE);
                return;
            }
            loginUser(username, password);
        });

        view.findViewById(R.id.register_button).setOnClickListener(event -> {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if(activity!=null){
                Utils.insertFragment(activity,new RegisterFragment(),this.getClass().getSimpleName(),R.id.fragment_container_view_login);
            }
        });

    }


    public void loginUser(String username, String password) {
        String url = InternetManager.URL + "?method=login&username=" + username + "&password=" + password;

        JsonObjectRequest loginRequest = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
//                    System.out.println(response.toString());
                    if (response.getString("status").equals("login success")) {
                        String userId = response.getString("id");
                        String firstName = response.getString("first_name");
                        String lastName = response.getString("last_name");
                        if(getActivity()!=null){
                            SharedPreferences.Editor editor = getActivity().getApplication().getSharedPreferences("global_pref", Context.MODE_PRIVATE).edit();
                            editor.putInt("userId", Integer.parseInt(userId));
                            editor.putString("first_name",firstName);
                            editor.putString("last_name",lastName);
                            editor.putString("username",username);
                            editor.apply();
                            startMainPane();
                        }
                    } else {
                        warningMessage.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        if(getActivity() != null){
            VolleySingleton.getInstance(getActivity().getApplicationContext()).add(loginRequest);
        }

    }

    private void startMainPane() {
        Intent intent = new Intent(getContext(),MainMenuActivity.class);
        startActivity(intent);
        if(getActivity()!= null){
            getActivity().finish();
        }
    }

}