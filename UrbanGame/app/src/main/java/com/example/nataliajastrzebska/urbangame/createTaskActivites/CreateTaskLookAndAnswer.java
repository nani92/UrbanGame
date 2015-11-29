package com.example.nataliajastrzebska.urbangame.createTaskActivites;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nataliajastrzebska.urbangame.R;

import java.io.IOException;


public class CreateTaskLookAndAnswer extends AppCompatActivity {

    Uri path;
    private int orientation;

    private String[] filePathColumn= {MediaStore.Images.Media.DATA};
    private String imagePath;
    private Bitmap selectedPicture;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int SELECT_IMAGE_REQUEST_CODE = 101;

    ImageView viewer;
    CheckBox text,date,number;
    EditText question,answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task_look_and_answer);
        viewer = (ImageView) findViewById(R.id.iv_createLookAnsAnswerTask_image);
        answer = (EditText) findViewById(R.id.et_createLookAndAnswerTask_answer);
        question = (EditText) findViewById(R.id.et_createLookAndAnswerTask_question);
        setCheckBox();
    }

    void setCheckBox(){
            text = (CheckBox) findViewById(R.id.cb_createLookAndAnswer_isAnswerText);
            date = (CheckBox) findViewById(R.id.cb_createLookAndAnswer_isAnswerDate);
            number = (CheckBox) findViewById(R.id.cb_createLookAndAnswer_isAnswerNumber);
            text.setChecked(true);
    }

    void unCheckBoxes(CheckBox cb1, CheckBox cb2) {
        cb1.setChecked(false);
        cb2.setChecked(false);
    }

    public void onCheckBoxClicked_Look(View view){
        boolean isChecked = ((CheckBox) view).isChecked();
        if (!isChecked){
            text.setChecked(true);
            answer.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else{
            switch (view.getId()){
                case R.id.cb_createLookAndAnswer_isAnswerText:
                    unCheckBoxes(number,date);
                    answer.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case R.id.cb_createLookAndAnswer_isAnswerNumber:
                    unCheckBoxes(text,date);
                    answer.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case R.id.cb_createLookAndAnswer_isAnswerDate:
                    unCheckBoxes(number,text);
                    answer.setInputType(InputType.TYPE_CLASS_DATETIME);
                    break;
            }
        }
    }

    public void chooseAPicture(View view){
        Intent galleryChooser = new Intent(Intent.ACTION_PICK);
        galleryChooser.setType("image/*");
        startActivityForResult(galleryChooser, SELECT_IMAGE_REQUEST_CODE);
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
            path = data.getData();
        }
        else if(requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            path = data.getData();
        }
        else{
            Toast.makeText(getApplicationContext(),"Access Android Apps Problem",Toast.LENGTH_SHORT).show();
        }

        try {
            selectedPicture = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
            imagePath = getPathFromUri(path);
            ExifInterface exifInterface = new ExifInterface(imagePath);
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

    String getPathFromUri(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri,filePathColumn,null,null,null);
        cursor.moveToFirst();
        String tmp = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
        cursor.close();
        return tmp;

    }


}
