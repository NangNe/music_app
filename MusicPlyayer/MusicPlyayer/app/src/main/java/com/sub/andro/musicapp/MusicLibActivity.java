package com.sub.andro.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MusicLibActivity extends AppCompatActivity {

    ImageView MusicLibraryIV,SubscriptionIV;

    Switch mSwitch;
    DatabaseReference mRef;
    RelativeLayout musicLibRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_lib);

        Intent i =getIntent();
        final String phone = i.getStringExtra("phone");

        MusicLibraryIV = (ImageView)findViewById(R.id.musicLibaryIV);
        SubscriptionIV = (ImageView)findViewById(R.id.subscriptionIV);

        mSwitch = (Switch)findViewById(R.id.switch1);
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        musicLibRL = (RelativeLayout)findViewById(R.id.musicLibRL);

        MusicLibraryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MusicLibActivity.this,SongsListActivity.class);
                i.putExtra("phone",phone);
                startActivity(i);
            }
        });
        SubscriptionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(MusicLibActivity.this,SubscriptionActivity.class);
                i.putExtra("phone",phone);
                startActivity(i);
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    //if checked we will set theme value 1 that is dark theme
                    //background change
                    musicLibRL.setBackgroundResource(R.drawable.back2);
                    //set value in firebase
                    mRef.child(phone).child("theme").setValue("1");
                }
                else
                {
                    //light theme
                    musicLibRL.setBackgroundResource(R.drawable.grad8);
                    mRef.child(phone).child("theme").setValue("0");
                }
            }
        });
    }
}
