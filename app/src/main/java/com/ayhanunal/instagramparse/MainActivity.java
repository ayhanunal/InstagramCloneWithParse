package com.ayhanunal.instagramparse;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    EditText userNameEditText;
    EditText passEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameEditText = findViewById(R.id.userNameEditText);
        passEditText = findViewById(R.id.passwordEditText);


        ParseUser parseUser = ParseUser.getCurrentUser(); //guncel kullanici - giris yapmis kullanici
        if(parseUser != null){
            Intent intent = new Intent(MainActivity.this,FeedActivity.class);
            startActivity(intent);
            finish();
        }





    }

    public void signIn(View view){

        ParseUser.logInInBackground(userNameEditText.getText().toString(), passEditText.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }else {
                    //giris yapildi.
                    Intent intent = new Intent(MainActivity.this,FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    public void signUp(View view){

        ParseUser user = new ParseUser();
        user.setUsername(userNameEditText.getText().toString());
        user.setPassword(passEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }else {
                    //kullanici kaydedildi.
                    Intent intent = new Intent(MainActivity.this,FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}