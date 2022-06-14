package com.sub.andro.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    //give reference to home ui button

    Button Register,Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Register = (Button)findViewById(R.id.home_register_button);
        Login = (Button)findViewById(R.id.home_login_button);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,RegisterActivity.class);
                startActivity(i);

                //to navigate from home to register
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
