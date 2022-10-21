//package com.example.climbingapp;
////
////import android.content.Intent;
////import android.net.Uri;
////import android.os.Bundle;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////import java.net.URI;
////
////import ja.burhanrashid52.photoeditor.PhotoEditor;
////import ja.burhanrashid52.photoeditor.PhotoEditorView;
////
////public class PhotoEditorActivity extends AppCompatActivity {
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_photo_editor);
////        PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);
////        Intent intent = getIntent();
////        if (intent != null) {
//////            mPhotoEditorView.getSource().setImageBitmap((Bitmap) intent.getExtras().get("data"));
////            mPhotoEditorView.getSource().setImageURI(Uri.parse(((URI)intent.getExtras().get("data")).toString()));
////        }
////        PhotoEditor mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
////                .setPinchTextScalable(true)
////                .setClipSourceImage(true)
////                .build();
////        mPhotoEditor.setBrushDrawingMode(true);
////
////    }
////
////
////
////
////}
//
//import android.app.Activity;
//import android.content.ContentValues;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore.Images.Media;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import java.io.OutputStream;
//import java.net.URI;
//
//public class PhotoEditorActivity extends Activity implements OnClickListener,
//        OnTouchListener {
//
//    ImageView choosenImageView;
//    Button choosePicture;
//    Button savePicture;
//
//    Bitmap bmp;
//    Bitmap alteredBitmap;
//    Canvas canvas;
//    Paint paint;
//    Matrix matrix;
//    float downx = 0;
//    float downy = 0;
//    float upx = 0;
//    float upy = 0;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        choosenImageView = (ImageView) this.findViewById(R.id.ChoosenImageView);
//        Intent intent = getIntent();
//        Bitmap bitmap;
//        if(intent!=null){
//            //bitmap = (Bitmap) intent.getParcelableExtra("data");;
//            Uri imageUri = Uri.parse(((URI)intent.getExtras().get("data")).toString());
//            choosenImageView.setImageURI(imageUri);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            bitmap = BitmapFactory.decodeFile(imageUri.toString(
//
//            ), options);
////            canvas = new Canvas(bitmap);
//
//            Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//            canvas = new Canvas(mutableBitmap);
//
//
//            try {
//                //todo:canvas viene settato a null..trovare modo di settare canvas.
//                paint = new Paint();
//                paint.setColor(Color.GREEN);
//                paint.setStrokeWidth(5);
//                matrix = new Matrix();
//                canvas.drawBitmap(bmp, matrix, paint);
//                choosenImageView.setImageBitmap(bitmap);
//                choosenImageView.setOnTouchListener(this);
//            } catch (Exception e) {
//                Log.v("ERROR", e.toString());
//            }
//        }
//        savePicture = (Button) this.findViewById(R.id.SavePictureButton);
//
//        savePicture.setOnClickListener(this);
//        choosenImageView.setOnTouchListener(this);
//    }
//
//    public void onClick(View v) {
//
//        if (v == savePicture) {
//
//            if (alteredBitmap != null) {
//                ContentValues contentValues = new ContentValues(3);
//                contentValues.put(Media.DISPLAY_NAME, "Draw On Me");
//
//                Uri imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
//                try {
//                    OutputStream imageFileOS = getContentResolver().openOutputStream(imageFileUri);
//                    alteredBitmap.compress(CompressFormat.JPEG, 90, imageFileOS);
//                    Toast t = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT);
//                    t.show();
//
//                } catch (Exception e) {
//                    Log.v("EXCEPTION", e.getMessage());
//                }
//            }
//        }
//    }
//
//
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                downx = event.getX();
//                downy = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                upx = event.getX();
//                upy = event.getY();
//                canvas.drawLine(downx, downy, upx, upy, paint);
//                choosenImageView.invalidate();
//                downx = upx;
//                downy = upy;
//                break;
//            case MotionEvent.ACTION_UP:
//                upx = event.getX();
//                upy = event.getY();
//                canvas.drawLine(downx, downy, upx, upy, paint);
//                choosenImageView.invalidate();
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
//
//}