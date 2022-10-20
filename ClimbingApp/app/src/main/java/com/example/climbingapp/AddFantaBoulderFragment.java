package com.example.climbingapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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



    public AddFantaBoulderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity()).get(AddFantaBoulderViewModel.class);
        repo = new ClimbingAppRepository(getActivity().getApplication());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_fanta_boulder, container, false);
    }

    private void selectImage() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, this.getActivity().getPackageName());
            //todo: aggiungere check permission
//            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            if (!(hasPerm == PackageManager.PERMISSION_GRANTED)) {
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
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                                    getActivity().startActivity(showImage);
                                } else {
                                    System.out.println("ops");
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
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
            } else
                Toast.makeText(this.getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this.getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.add_fanta_boulder_imageview);
//        model.getBitmap().observe(getActivity(),bitmap->{
//            imageView.setImageBitmap(bitmap);
//        });
        model.getImageUri().observe(getActivity(),uri->{
            imageView.setImageURI(uri);
        });
        imageView.setOnClickListener(event -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent editIntent = new Intent(Intent.ACTION_EDIT);
            if(editIntent.resolveActivity(getActivity().getPackageManager())==null){
                new AlertDialog.Builder(this.getContext()).setTitle("Error").setMessage("Edit photo feature not supported").show();
                return;
            }
            selectImage();
//            if(intent.resolveActivity(getActivity().getPackageManager())!= null){
//                getActivity().startActivityForResult(intent,Utils.REQUEST_IMAGE_CAPTURE);
//            }
        });

        slider = view.findViewById(R.id.slider_addFantaBoulder);
        grades = getActivity().getResources().getStringArray(R.array.grades);
        slider.setValueTo(grades.length-1);
        slider.setLabelFormatter(value -> {
            return grades[(int)value];
        });

        view.findViewById(R.id.button_addFantaBoulder).setOnClickListener(event->{
            //todo:check form fields + volley request
            //check form:
            if(checkFields()) {
                checkAndUploadBoulder();
            }
//            checkAndUploadBoulder();
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
        VolleySingleton.getInstance(getContext()).add(new StringRequest(Request.Method.GET, checkNameRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //if the name is ok:
                if(response.equals("name_check_fail")){
                    Toast.makeText(getActivity(), "Name already taken", Toast.LENGTH_SHORT).show();
                } else if(response.equals("name_check_ok")){
                    uploadBoulder();
                } else {
                    System.out.println("bho");
                }
            }
        }, null));

        System.out.println("button pressed");
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
        VolleySingleton.getInstance(getContext()).add(new JsonObjectRequest(Request.Method.POST, insertBoulderRequest, postData,null, error -> {
            error.printStackTrace();
        }));
    }


}