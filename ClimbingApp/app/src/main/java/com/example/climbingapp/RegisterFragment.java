package com.example.climbingapp;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.regex.Pattern;


public class RegisterFragment extends Fragment {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        internetManager = new InternetManager(getActivity(),view);
        EditText passwordEt = view.findViewById(R.id.password_registerview);
        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateText(charSequence.toString(), passwordEt);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        EditText usernameEt = view.findViewById(R.id.username_registerview);
        EditText firstnameEt = view.findViewById(R.id.firstnname_registerview);
        EditText lastnameEt = view.findViewById(R.id.lastname_registerview);
        view.findViewById(R.id.register_button_registerview).setOnClickListener(event -> {
            if (!validateText(passwordEt.getText().toString(),passwordEt)){
                return;
            }
            String username = usernameEt.getText().toString();
            String password = passwordEt.getText().toString();
            String firstname = firstnameEt.getText().toString();
            String lastname = lastnameEt.getText().toString();
            registerUser(username,password,firstname,lastname);
        });

    }

    private void registerUser(String username, String password, String firstname, String lastname) {

        String url = InternetManager.URL + "?method=registration&username=" + username +"&password="+password+"&firstname="+firstname+"&lastname="+lastname;
        System.out.println(url);
        StringRequest registerRequest = new StringRequest(Request.Method.GET,url,response -> {
            if(response.equals("registration success")){
                Toast.makeText(getContext(),"registration success",Toast.LENGTH_SHORT).show();
                if(getActivity()!=null){
                    getActivity().onBackPressed();
                }
            } else if(response.equals("registration failed")) {
                new AlertDialog.Builder(getContext()).setMessage("Username already taken").setTitle("Error").show();
            }else {
                    new AlertDialog.Builder(getContext()).setMessage("Something went wrong..").setTitle("Error").show();
                }

        }, Throwable::printStackTrace);
        if(internetManager.isNetworkConnected()){
            VolleySingleton.getInstance(getContext()).add(registerRequest);
        } else {
            internetManager.getSnackbar().show();
        }
    }

    public boolean validateText(String text, EditText view) {

        // check for pattern
        Pattern uppercase = Pattern.compile(getString(R.string.uppercase_pattern));
        Pattern lowercase = Pattern.compile(getString(R.string.lowercase_pattern));
        Pattern digit = Pattern.compile(getString(R.string.number_pattern));

        // if lowercase character is not present
        if (!lowercase.matcher(text).find()) {
            view.setTextColor(Color.RED);
            return false;
        } else {
            // if lowercase character is  present
            view.setTextColor(Color.GREEN);
        }

        // if uppercase character is not present
        if (!uppercase.matcher(text).find()) {
            view.setTextColor(Color.RED);
            return false;
        } else {
            // if uppercase character is  present
            view.setTextColor(Color.GREEN);
        }
        // if digit is not present
        if (!digit.matcher(text).find()) {
            view.setTextColor(Color.RED);
            return false;
        } else {
            // if digit is present
            view.setTextColor(Color.GREEN);
        }
        // if password length is less than 8
        if (text.length() < 8) {
            view.setTextColor(Color.RED);
            return false;
        } else {
            view.setTextColor(Color.GREEN);
        }
        return true;
    }
}