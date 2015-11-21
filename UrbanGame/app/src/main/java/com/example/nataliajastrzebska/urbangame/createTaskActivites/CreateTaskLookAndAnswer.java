package com.example.nataliajastrzebska.urbangame.createTaskActivites;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.example.nataliajastrzebska.urbangame.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateTaskLookAndAnswer extends AppCompatActivity {

    private int imageIndeks = 0;
    private int orientation;
    private ImageView viewer;
    String[] imagePath;
    String[] filePathColumn= {MediaStore.Images.Media.DATA};
    Uri path = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    Bitmap[] images;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int SELECT_IMAGE_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_look_and_answer);
        viewer = (ImageView)findViewById(R.id.imageView);
        //getPictures();
        //viewImage();
    }

    // to consider - now its taking images directly from driver, takes some time
    // alt version - put all images into RAM - faster, but uses RAM
    // todo taking picture and change to imageswitcher

    public void whatever(View view){
        Intent galleryChooser = new Intent(Intent.ACTION_PICK);
        galleryChooser.setType("image/*");
        startActivityForResult(galleryChooser, SELECT_IMAGE_REQUEST_CODE);

    }

    void viewImage(){
        //viewer.setImageBitmap(BitmapFactory.decodeFile(imagePath[imageIndeks]));
        viewer.setImageBitmap(images[imageIndeks]);
    }

    public void takeAPicture(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK ){
            Bundle extras = data.getExtras();
            Uri path = data.getData();
            Bitmap picture = (Bitmap) extras.get("data");
            viewer.setImageBitmap(picture);
        }
        if(requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri path = data.getData();

            try {
                Bitmap selectedPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                ExifInterface exifInterface = new ExifInterface(getPathFromUri(path));
                orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                if(orientation != 1)//if orientation is wrong
                {
                    Matrix matrix = new Matrix();
                    switch (orientation) {
                        case 3:
                            matrix.postRotate(180);
                            break;
                        case 6:
                            matrix.postRotate(90);
                            break;
                        case 8:
                            matrix.postRotate(270);
                    }
                    selectedPicture = Bitmap.createBitmap(selectedPicture,0,0,selectedPicture.getWidth(),selectedPicture.getHeight(),matrix,true);
                }
                viewer.setImageBitmap(selectedPicture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    String getPathFromUri(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri,filePathColumn,null,null,null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
    }

    public void nextImage(View view){
        imageIndeks = (imageIndeks + 1) % imagePath.length;
        viewImage();
    }

}
