package com.example.climbingapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.widget.TextView;

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
import androidx.navigation.ui.NavigationUI;

import com.example.climbingapp.viewmodels.AddFantaBoulderViewModel;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class MainMenuActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavController navController;
    private AddFantaBoulderViewModel fantaModel;
    private Uri uri;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    requestEditOnPhoto(uri);
                } else {
                    new AlertDialog.Builder(this).setMessage("Edit function not available").show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        DrawerLayout drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_menu);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

//        AppBarConfiguration appBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph())
//                        .setDrawerLayout(drawerLayout)
//                        .build();
        NavigationView navView = findViewById(R.id.nav_view_menu);
        navView.getMenu().findItem(R.id.logoutItem).setOnMenuItemClickListener(menuItem -> {
            Utils.logOutUser(this);
            finish();
            Intent intent = new Intent(this, LoginRegisterActivity.class);
            startActivity(intent);
            return true;
        });
        NavigationUI.setupWithNavController(navView, navController);
        this.fantaModel = new ViewModelProvider(this).get(AddFantaBoulderViewModel.class);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(getDrawable(R.drawable.wave_logo));

        TextView nomeUtente = navView.getHeaderView(0).findViewById(R.id.nomeUtente_header_navview);

        SharedPreferences pref = getSharedPreferences("global_pref", MODE_PRIVATE);
        nomeUtente.setText(pref.getString("username", ""));

        MenuItem menuItem = navView.getMenu().getItem(3);
        SpannableString spanString = new SpannableString("Logout");
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.dark_primary_color)), 0,
                spanString.length(), 0);
        menuItem.setTitle(spanString);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    requestEditOnPhoto(fantaModel.getImageUri().getValue());
                    break;
                case 2:
                    if (data != null) {
                        requestEditOnPhoto(data.getData());
                    }
                    break;
                case 3:
                    if (data != null) {
                        if (data.getData() != null) {
                            onEditFinished(data.getData());
                        }
                    } else {
                        onEditFinished(fantaModel.getImageUri().getValue());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void requestEditOnPhoto(Uri uri) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            this.uri = uri;
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do you want to  select the holds?");
        CharSequence[] options = new CharSequence[]{"Yes", "No", "Cancel"};
        builder.setItems(options, (dialogInterface, i) -> {
            if (options[i].equals("Yes")) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(this).setMessage("You don't have the permissions to use edit functionality.").show();
                    return;
                }
                editImage(uri);
            } else if (options[i].equals("No")) {
                onEditFinished(uri);
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


    private void onEditFinished(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            fantaModel.setBitmap(bitmap);
            fantaModel.setImageUri(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}