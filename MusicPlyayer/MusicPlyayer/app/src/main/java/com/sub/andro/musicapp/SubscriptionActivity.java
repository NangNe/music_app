package com.sub.andro.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SubscriptionActivity extends AppCompatActivity {

    String phone;
    DatabaseReference mRef;
    RelativeLayout subscriptionRl;
    String themeValue;
    @Override
    protected void onResume() {
        Intent i = getIntent();
        phone = i.getStringExtra("phone");
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        subscriptionRl = (RelativeLayout)findViewById(R.id.subscriptionRL);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //fetch theme value
                themeValue = snapshot.child(phone).child("theme").getValue().toString();
                checkTheme(themeValue);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        super.onResume();
    }

    private void checkTheme(String themeValue) {
        //make seprate function to check theme value and set background according to that value
        if(themeValue.equals("0"))
        {
            //if value 0 then light theme else dark theme
            subscriptionRl.setBackgroundResource(R.drawable.grad8);
        }
        else
        {
            subscriptionRl.setBackgroundResource(R.drawable.back2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
    }
}
