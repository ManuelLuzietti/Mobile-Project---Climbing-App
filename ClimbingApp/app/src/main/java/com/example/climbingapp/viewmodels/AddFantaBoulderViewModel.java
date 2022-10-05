package com.example.climbingapp.viewmodels;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddFantaBoulderViewModel extends ViewModel {

    private MutableLiveData<Bitmap> bitmap;
    private MutableLiveData<Uri> imageUri;

    public AddFantaBoulderViewModel() {
        super();
        bitmap = new MutableLiveData<>();
        imageUri = new MutableLiveData<>();
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap.setValue(bitmap);
    }

    public MutableLiveData<Bitmap> getBitmap(){
        return this.bitmap;
    }

    public LiveData<Uri> getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri.setValue(imageUri);
    }
}
