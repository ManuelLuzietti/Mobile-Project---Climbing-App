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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.climbingapp.database.entities.Boulder;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private ProgressBar progressBar;
    private ImageView imageView;
    private FloatingActionButton fab ;
    private TextView nameTextView;
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

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_boulder_view, container, false);
        setTouchListener(view);
        progressBar = (ProgressBar) view.findViewById(R.id.boulder_progressBar);
        imageView = (ImageView) view.findViewById(R.id.boulder_imageview);
        internetManager = new InternetManager(getActivity(), view);
        nameTextView =(TextView) view.findViewById(R.id.boulder_nameTextView);
        nameTextView.setText(model.getSelected().getValue().getPlaceName());
        fab = (FloatingActionButton) view.findViewById(R.id.buoulder_fab);
        model.getSelected().observe(this, (Boulder.BoulderUpdated boulder)->{
            nameTextView.setText(boulder.getPlaceName());
        });

        fab.setOnClickListener(view1 -> {
            try {
                NavHostFragment.findNavController(FragmentManager.findFragment(view))
                        .navigate(R.id.action_boulderViewFragment_to_addViewFragment);
            } catch (IllegalStateException is) {
                if (getActivity() != null) {
                    Utils.insertFragment((AppCompatActivity) getActivity(), new AddViewFragment(), getClass().getSimpleName(), R.id.nav_host_fragment_menu);
                }
            }
        });
        NavigationBarView navView = ((NavigationBarView) view.findViewById(R.id.bottomnavview_boulderview));
        navView.setItemIconTintList(null);
        BottomNavigationItemView menuItem1 = navView.findViewById(R.id.bottomnav_update);
        menuItem1.setIconSize(navView.getItemIconSize()+30);
        BottomNavigationItemView menuItem2 = navView.findViewById(R.id.bottomnav_info);
        menuItem2.setPadding(0, (int) (30*getContext().getResources().getDisplayMetrics().density),0,0);
        BottomNavigationItemView menuItem3 = navView.findViewById(R.id.bottomnav_comments);
        menuItem3.setPadding(0,(int) (30*getContext().getResources().getDisplayMetrics().density),0,0);

        ((NavigationBarView) view.findViewById(R.id.bottomnavview_boulderview)).setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottomnav_update:
                    updateImmage();
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





        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsCreated) {
            mIsCreated = true;
            requestImmage();
        } else {
            if (getView() != null) {
                progressBar.setVisibility(View.GONE);
                ((ImageView) getView().findViewById(R.id.boulder_imageview)).setImageBitmap(model.getBitmap().getValue());
            }
        }
    }

    private void updateImmage(){
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
                File f = new File(this.getActivity().getFilesDir(), imgName);
                f.delete();
            }
        }
        requestImmage();

    }

    private void requestImmage() {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setImageDrawable(getContext().getDrawable(R.drawable.whitebackground));
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
                    FileInputStream fIn = new FileInputStream(new File(this.getContext().getFilesDir(), imgName));
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageDrawable(getContext().getDrawable(R.drawable.ic_baseline_image_not_supported_24));
                progressBar.setVisibility(View.GONE);
            }
        });
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

    private void setBitmap(String data) {
        byte[] decodedString = Base64.decode(data.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (getView() != null) {
            progressBar.setVisibility(View.GONE);
            imageView.setImageBitmap(decodedByte);
            //imageView.setVisibility(View.VISIBLE);
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