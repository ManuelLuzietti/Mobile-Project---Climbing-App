package com.example.climbingapp;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


public class BoulderViewFragment extends Fragment {

    private SelectedBoulderViewModel model;
    private InternetManager internetManager;
    private boolean mIsCreated;

    public BoulderViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            model = new ViewModelProvider(getActivity()).get(SelectedBoulderViewModel.class);
        }

    }

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

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boulder_view, container, false);
        setTouchListener(view);
        internetManager = new InternetManager(getActivity(), view);
        ((NavigationBarView) view.findViewById(R.id.bottomnavview_boulderview)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottomnav_add:
                    try {
                        NavHostFragment.findNavController(FragmentManager.findFragment(view))
                                .navigate(R.id.action_boulderViewFragment_to_addViewFragment);
                    } catch (IllegalStateException is) {
                        if (getActivity() != null) {
                            Utils.insertFragment((AppCompatActivity) getActivity(), new AddViewFragment(), getClass().getSimpleName(), R.id.nav_host_fragment_menu);
                        }
                    }
                    break;
                case R.id.bottomnav_comments:
                    try {
                        NavHostFragment.findNavController(FragmentManager.findFragment(view)).navigate(R.id.action_boulderViewFragment_to_commentsBoulderViewFragment);
                    } catch (IllegalStateException is) {
                        if (getActivity() != null) {
                            Utils.insertFragment((AppCompatActivity) getActivity(), new CommentsBoulderViewFragment(), getClass().getSimpleName(), R.id.nav_host_fragment_menu);
                        }
                    }
                    break;
                case R.id.bottomnav_info:
                    try {
                        NavHostFragment.findNavController(FragmentManager.findFragment(view)).navigate(R.id.action_boulderViewFragment_to_infoBoulderViewFragment);
                    } catch (IllegalStateException is) {
                        if (getActivity() != null) {
                            Utils.insertFragment((AppCompatActivity) getActivity(), new InfoBoulderViewFragment(), getClass().getSimpleName(), R.id.nav_host_fragment_menu);
                        }
                    }
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
        if (!mIsCreated) {
            mIsCreated = true;
            requestImmage();
        } else {
            if (getView() != null) {
                ((ImageView) getView().findViewById(R.id.boulder_imageview)).setImageBitmap(model.getBitmap().getValue());
            }
        }
    }

    private void requestImmage() {
        String imgName;
        if (model.getSelected().getValue() != null) {
            imgName = model.getSelected().getValue().getImg();
        } else {
            imgName = null;
        }
        if (imgName != null) {
            String[] files = getContext().fileList();
            List filesList = Arrays.asList(files);
            if (filesList.contains(imgName)) {
                String buffer = "";
                try {
                    FileInputStream fIn = new FileInputStream( new File(this.getContext().getFilesDir(),imgName));
                    InputStreamReader reader = new InputStreamReader(fIn);
                    BufferedReader bReader = new BufferedReader(reader);
                    String dataRow = "";
                    while ((dataRow = bReader.readLine()) != null) {
                        buffer += dataRow;
                    }
                    bReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setBitmap(buffer);
            } else {
                if (internetManager.isNetworkConnected()) {
                    volleyRequestForImage(imgName);
                } else {
                    internetManager.getSnackbar().show();
                }
            }
        }
    }


    private void volleyRequestForImage(String imgName) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(InternetManager.URL + "?img=" + imgName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                byte[] decodedString = new byte[0];
                String imgData = null;
                try {
                    imgData = response.getString("img");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setBitmap(imgData);
                createImgFile(imgName, imgData);
            }
        }, Throwable::printStackTrace);
        VolleySingleton.getInstance(getContext()).add(jsonRequest);
    }

    private void createImgFile(String imgName, String imgData) {
        File file = new File(getContext().getFilesDir(), imgName);
        try (FileOutputStream fos = getContext().openFileOutput(imgName, Context.MODE_PRIVATE)) {
            fos.write(imgData.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBitmap(String data){
        byte[] decodedString = Base64.decode(data.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (getView() != null) {
            ((ImageView) getView().findViewById(R.id.boulder_imageview)).setImageBitmap(decodedByte);
        }
        model.setBitmap(decodedByte);
    }

    private void setTouchListener(View view) {
        view.findViewById(R.id.boulder_imageview).setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                System.out.println("right");

                model.decreaseIndex();
                requestImmage();
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                System.out.println("left");
                model.increaseIndex();
                requestImmage();
            }
        });
    }
}