package com.sub.andro.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    EditText LoginPhone,LoginPassword;
    Button LoginButton;
    String Phone,Password;    //to store phone and password that user enter in edittext
    String userPassword;  //to store actual password from database

    ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoadingBar = new ProgressDialog(this);
        LoginPhone = (EditText)findViewById(R.id.login_phone);
        LoginPassword = (EditText)findViewById(R.id.login_password);
        LoginButton = (Button)findViewById(R.id.login_btn);

        LoadingBar.setTitle("Login Account");
        LoadingBar.setMessage("Please wait, we are checking your credentials in our database");
        LoadingBar.setCanceledOnTouchOutside(false);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Phone = LoginPhone.getText().toString();
                Password = LoginPassword.getText().toString();
                LoginAccount(Phone,Password);
            }
        });


    }

    private void LoginAccount(final String phone, String password) {

        //now check edittext is empty or not
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please enter your phone number..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Password))
        {
            Toast.makeText(this, "Please enter your password..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            LoadingBar.show();

            final DatabaseReference mRef;
            mRef = FirebaseDatabase.getInstance().getReference();

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //now if edit text are not empty then we check that the Phone that the user entered is exist in
                    //our database or not
                    //if it is exist in our database then we retrive passowrd in "userPasssword" and then
                    //match that password with the password that user entered in edittext
                    //if it is match then we redirect user to home activity
                    if(snapshot.child("Users").child(Phone).exists())
                    {
                        //if exist retrive password from database
                        userPassword = snapshot.child("Users").child(Phone).child("password").getValue().toString();

                        //now check
                        if(Password.equals(userPassword))
                        {
                            //then go to musiclibrary activity // make new activity to navigate
                            LoadingBar.dismiss();
                            Intent i = new Intent(LoginActivity.this,MusicLibActivity.class);
                            i.putExtra("phone",phone);
                            startActivity(i);

                            //so if user exist with the number that they enter and with correct password then user will redirect to
                            //music lib activity
                            //lets run and check it
                        }
                        else
                        {
                            //if user exist but password is not correct
                            LoadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "please enter valid password...", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //user not exist
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "User with this number does not exist..", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}









