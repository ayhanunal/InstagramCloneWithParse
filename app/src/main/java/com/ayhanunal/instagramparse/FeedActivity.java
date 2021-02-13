package com.ayhanunal.instagramparse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<String> userNameFP;
    ArrayList<String> userCommentFP;
    ArrayList<Bitmap> userImageFP;
    PostClass adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.feed_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.newPost){
            Intent intent = new Intent(FeedActivity.this,UploadActivity.class);
            startActivity(intent);

        }else if(item.getItemId() == R.id.signOut){
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null){
                        Toast.makeText(FeedActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }else {
                        Intent intent = new Intent(FeedActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        listView = findViewById(R.id.listView);

        userNameFP = new ArrayList<>();
        userCommentFP = new ArrayList<>();
        userImageFP = new ArrayList<>();

        downloadData();

        adapter = new PostClass(userNameFP,userCommentFP,userImageFP,this);
        listView.setAdapter(adapter);



    }


    public void downloadData(){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e != null){
                    Toast.makeText(FeedActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }else {

                    if(objects.size() > 0){
                        for (final ParseObject object : objects){

                            ParseFile parseFile = (ParseFile) object.get("image");
                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if (e != null && data != null){
                                        Toast.makeText(FeedActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                    }else {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                        userImageFP.add(bitmap);

                                        userNameFP.add(object.getString("userName"));
                                        userCommentFP.add(object.getString("comment"));

                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            });



                        }
                    }

                }
            }
        });

    }

}