package com.example.climbingapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;


public class BoulderViewFragment extends Fragment {

    private SelectedBoulderViewModel model ;
    private InternetManager internetManager;
    private boolean mIsCreated;
    public BoulderViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity()).get(SelectedBoulderViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        internetManager.registerNetworkCallback(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        internetManager.unregisterNetworkCallback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_boulder_view, container, false);
        internetManager = new InternetManager(getActivity(),view);

        ((NavigationBarView)view.findViewById(R.id.bottomnavview_boulderview)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottomnav_add:
                    Utils.insertFragment((AppCompatActivity) getActivity(),new AddViewFragment(),this.getClass().getSimpleName(),R.id.nav_host_fragment_menu);
                    break;
                case R.id.bottomnav_comments:
                    Utils.insertFragment((AppCompatActivity) getActivity(),new CommentsBoulderViewFragment(),this.getClass().getSimpleName(),R.id.nav_host_fragment_menu);
                    break;
                case  R.id.bottomnav_info:
                    Utils.insertFragment((AppCompatActivity) getActivity(),new InfoBoulderViewFragment(),this.getClass().getSimpleName(),R.id.nav_host_fragment_menu);
                    break;
                default:
                    return false;
            }
            return true;
        });
        view.findViewById(R.id.boulder_imageview).setOnClickListener(view1 -> {
            requestImmage();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mIsCreated){
            mIsCreated = true;
            requestImmage();
        } else {
            ((ImageView)getView().findViewById(R.id.boulder_imageview)).setImageBitmap(model.getBitmap().getValue());
        }
    }

    private void requestImmage(){
        if(internetManager.isNetworkConnected()){
            String imgName = model.getSelected().getValue().getImg();
            if(imgName != null){
                VolleySingleton.getInstance(getContext()).add(new JsonObjectRequest(InternetManager.URL + "?img="+imgName,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        byte[] decodedString = new byte[0];
                        try {
                            decodedString = Base64.decode(response.getString("img").getBytes(StandardCharsets.UTF_8),Base64.DEFAULT);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0, decodedString.length);
                        ((ImageView)getView().findViewById(R.id.boulder_imageview)).setImageBitmap(decodedByte);
                        model.setBitmap(decodedByte);
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }));
            }

        } else {
            internetManager.getSnackbar().show();
        }
    }
}