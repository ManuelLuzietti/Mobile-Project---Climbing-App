package com.example.climbingapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {


    public static final int REQUEST_IMAGE_CAPTURE = 1;

    public static void insertFragment(AppCompatActivity activity, Fragment fragment, String tag, int fragmentContainer){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer,fragment,tag);
        if(!(fragment instanceof MenuFragment)){
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    public static Date stringToDate(String date){

        try {
            return  new SimpleDateFormat("yyyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int numOfTriesConversion(String tries){
        switch (tries) {
            case "flash":
                return 1;
            case "second attempt":
                return 2;
            case "third attempt":
                return 3;
            case "more than three":
                return 4;
            default:
                throw new RuntimeException();
        }
    }

    public static boolean isUserLoggedIn(Activity activity) {
        int id = activity.getSharedPreferences("global_pref",Context.MODE_PRIVATE).getInt("userId",-1);
        return id != -1;
    }

    public static void logOutUser(Activity activity){
        activity.getSharedPreferences("global_pref",Context.MODE_PRIVATE).edit().remove("userId").commit();

    }

    public static void saveImage(Bitmap bitmap,Activity activity,String username) throws FileNotFoundException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ITALY).format(new Date());
        String name = username + timestamp + ".jpeg";

        ContentResolver contentResolver = activity.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");

        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        OutputStream outputStream = contentResolver.openOutputStream(imageUri);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        try{
            outputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
