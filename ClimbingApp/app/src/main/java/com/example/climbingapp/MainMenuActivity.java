package com.example.climbingapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.climbingapp.viewmodels.AddFantaBoulderViewModel;
import com.google.android.material.navigation.NavigationView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainMenuActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavController navController;
    private AddFantaBoulderViewModel fantaModel;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_menu);
        navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();
        NavigationView navView = findViewById(R.id.nav_view_menu);
        NavigationUI.setupWithNavController(navView, navController);
        this.fantaModel = new ViewModelProvider(this).get(AddFantaBoulderViewModel.class);
        checkPermission();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1:
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    try {
                        fantaModel.setImageUri( Utils.saveImage(image,this,"prova"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    requestEditOnPhoto(fantaModel.getImageUri().getValue());
                    break;
                case 2:
                    requestEditOnPhoto(data.getData());
                    break;
                case 3:
                    onEditFinished(data.getData());
                    break;

                default:
                    break;
            }
        }
    }



    private void requestEditOnPhoto(Uri uri){
        fantaModel.setImageUri( uri);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you wanna select the holds?");
        CharSequence[] options = new CharSequence[]{"Yes","No","Cancel"};
        builder.setItems(options,(dialogInterface, i) -> {
            if(options[i].equals("Yes")){
                editImage(fantaModel.getImageUri().getValue());
            } else if (options[i].equals("No")){
                onEditFinished(fantaModel.getImageUri().getValue());
            }
            dialogInterface.dismiss();
        });
        builder.show();
    }

    private void editImage(Uri imageUri) {
        if (imageUri != null) {
            Intent editIntent = new Intent(Intent.ACTION_EDIT);
            editIntent.setDataAndType(imageUri, "image/*");
            editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            this.startActivityForResult(editIntent, 3);
        }
    }



    private void onEditFinished(Uri uri){
        try {
            fantaModel.setBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
            fantaModel.setImageUri(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            System.out.println("ciao");
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }
    }

}