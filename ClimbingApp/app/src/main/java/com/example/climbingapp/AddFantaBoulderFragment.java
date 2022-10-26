package com.example.climbingapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.climbingapp.database.TypeConverters;
import com.example.climbingapp.viewmodels.AddFantaBoulderViewModel;
import com.google.android.material.slider.Slider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class AddFantaBoulderFragment extends Fragment {

    private AddFantaBoulderViewModel model;
    private ClimbingAppRepository repo;
    private ImageView imageView;
    private Slider slider;
    private com.google.android.material.textfield.TextInputEditText nameField;
    private String[] grades ;
    private InternetManager internetManager;


    public AddFantaBoulderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity()).get(AddFantaBoulderViewModel.class);
        repo = new ClimbingAppRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_fanta_boulder, container, false);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.add_fanta_boulder_imageview);
        internetManager = new InternetManager(getActivity(),view);

        model.getBitmap().observe(getActivity(),bitmap->{
            if(bitmap==null){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_camera));
            } else {
                imageView.setImageBitmap(bitmap);
            }
        });
        imageView.setOnClickListener(event -> {
            Intent editIntent = new Intent(Intent.ACTION_EDIT);
            if(editIntent.resolveActivity(getActivity().getPackageManager())==null){
                new AlertDialog.Builder(this.getContext()).setTitle("Error").setMessage("Edit photo feature not supported").show();
                return;
            }
            selectImage();
        });
        slider = view.findViewById(R.id.slider_addFantaBoulder);
        grades = getActivity().getResources().getStringArray(R.array.grades);
        slider.setValueTo(grades.length-1);
        slider.setLabelFormatter(value -> grades[(int)value]);
        view.findViewById(R.id.button_addFantaBoulder).setOnClickListener(event->{
            if(checkFields()) {
                checkAndUploadBoulder();
            }
        });
        nameField = view.findViewById(R.id.boulder_name_editext_addfantaboulder);
    }

    private boolean checkFields() {
        if (model.getImageUri().getValue() == null) {
            Toast.makeText(getContext(), "please insert a photo of the boulder", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(grades[(int)slider.getValue()].equals("")){
            Toast.makeText(getContext(), "please select a grade for the boulder", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //check boulder name request:
    private void checkAndUploadBoulder(){
        String checkNameRequest = InternetManager.URL + "?method=check_boulder_name&boulder_name="+nameField.getText().toString();
        StringRequest request = new StringRequest(Request.Method.GET, checkNameRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("name_check_fail")){
                    Toast.makeText(getActivity(), "Name already taken", Toast.LENGTH_SHORT).show();
                } else if(response.equals("name_check_ok")){
                    uploadBoulder();
                } else {
                    System.out.println("bho");
                }
            }
        }, error -> error.printStackTrace());
        if (internetManager.isNetworkConnected()){
            VolleySingleton.getInstance(getContext()).add(request);
        } else{
            internetManager.getSnackbar().show();
        }

        Log.d(this.getClass().getSimpleName(),"button pressed");
    }

    private void uploadBoulder() {
        String insertBoulderRequest = InternetManager.URL;
        JSONObject postData = new JSONObject();
        try {
            postData.put("method","upload_fanta_boulder");
            postData.put("name",nameField.getText().toString());
            postData.put("grade",grades[(int) slider.getValue()]);
            postData.put("date", TypeConverters.toString(new Date()));
            postData.put("img",Utils.bitmapToBase64(model.getBitmap().getValue()));
            postData.put("user_id",getActivity().getSharedPreferences("global_pref", Context.MODE_PRIVATE).getInt("userId",-1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, insertBoulderRequest, postData,response -> {
            model.setImageUri(null);
            model.setBitmap(null);
            repo.updateDB();
            new AlertDialog.Builder(getContext()).setTitle("Thanks for your upload").setMessage("The problem is going to be analyzed soon")
                    .setPositiveButton("Alè",(dialogInterface, i) -> {
                        getActivity().onBackPressed();
                    }).show();
        }, error -> {
            error.printStackTrace();
        });
        if (internetManager.isNetworkConnected()){
            VolleySingleton.getInstance(getContext()).add(jsonRequest);
        } else{
            internetManager.getSnackbar().show();
        }
    }

    private void selectImage() {
        CharSequence[] options;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Option");
        if(model.getBitmap().getValue()!=null){
            options = new CharSequence[]{"Take Photo", "Choose From Gallery","Show selected image","Cancel"};
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        Uri imageUri = getActivity().getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        model.setImageUri(imageUri);
                        getActivity().startActivityForResult(intent, 1);
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getActivity().startActivityForResult(pickPhoto, 2);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    } else if(options[item].equals("Show selected image")){
                        dialog.dismiss();
                        Intent showImage = new Intent(Intent.ACTION_VIEW);
                        showImage.setDataAndType(model.getImageUri().getValue(), "image/*");
                        if(showImage.resolveActivity(getActivity().getPackageManager())!= null){
                            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                                getActivity().startActivity(showImage);
                            } else {
                                Toast.makeText(getActivity(), "You don't have access permission to data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "View action not supported", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            options =new CharSequence[]{"Take Photo", "Choose From Gallery","Cancel"};
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "New Picture");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        Uri imageUri = getActivity().getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        model.setImageUri(imageUri);
                        getActivity().startActivityForResult(intent, 1);
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        getActivity().startActivityForResult(pickPhoto, 2);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
        }
        builder.show();
    }
}