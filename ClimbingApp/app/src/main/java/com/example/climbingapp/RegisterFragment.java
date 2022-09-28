package com.example.climbingapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import java.util.regex.Pattern;


public class RegisterFragment extends Fragment {

    private ClimbingAppRepository repository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ClimbingAppRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        String url = InternetManager.URL + "?method=register&username=" + username +"&password="+password+"&firstname="+firstname+"&lastname="+lastname;
        StringRequest registerRequest = new StringRequest(Request.Method.GET,url,response -> {
            System.out.println(response);
            //todo:finire richiesta di registrazione volley

        }, error -> {
            error.printStackTrace();
        });
        VolleySingleton.getInstance(getContext()).add(registerRequest);
    }

    public boolean validateText(String text, EditText view) {

        // check for pattern
        Pattern uppercase = Pattern.compile("[A-Z]");
        Pattern lowercase = Pattern.compile("[a-z]");
        Pattern digit = Pattern.compile("[0-9]");

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