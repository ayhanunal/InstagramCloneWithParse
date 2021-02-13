package com.ayhanunal.instagramparse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    ImageView postImageView;
    EditText commentEditText;

    Bitmap selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        postImageView = findViewById(R.id.postImageView);
        commentEditText = findViewById(R.id.commentEditText);


    }

    public void savePost(View view){

        String commentText = commentEditText.getText().toString();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ParseFile parseFile = new ParseFile("image.png",bytes);

        ParseObject object = new ParseObject("Posts");
        object.put("comment",commentText);
        object.put("image",parseFile);
        object.put("userName", ParseUser.getCurrentUser().getUsername());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(UploadActivity.this,"Post Uploaded.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UploadActivity.this,FeedActivity.class);
                    startActivity(intent);
                }
            }
        });

    }


    public void getGaleries(View view){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }else {
            Intent intentGaleris = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentGaleris,1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //izin verirse ne olacak

        if(requestCode == 2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentGaleris = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGaleris,1);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //resmi secince ne olacak galeriden

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {

                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),uri);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    postImageView.setImageBitmap(selectedImage);
                }else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    postImageView.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}