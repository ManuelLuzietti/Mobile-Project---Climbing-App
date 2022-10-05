package com.example.climbingapp;

import android.Manifest;
import android.app.AlertDialog;
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

import com.example.climbingapp.viewmodels.AddFantaBoulderViewModel;

public class AddFantaBoulderFragment extends Fragment {

    private AddFantaBoulderViewModel model;

    public AddFantaBoulderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity()).get(AddFantaBoulderViewModel.class);


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
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Option");
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
        ImageView imageView = view.findViewById(R.id.add_fanta_boulder_imageview);
        model.getBitmap().observe(getActivity(),bitmap->{
            imageView.setImageBitmap(bitmap);
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
    }
}