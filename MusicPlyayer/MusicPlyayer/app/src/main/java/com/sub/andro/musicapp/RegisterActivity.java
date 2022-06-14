package com.sub.andro.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText RegisterPhone,RegisterPassword,RegisterName;
    String Phone, Password,Name;                        //to save phone pass and name from edit text
    Button RegisterButton;
    ProgressDialog LoadingBar;
    //loading bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterPhone = (EditText)findViewById(R.id.register_phone);
        RegisterPassword = (EditText)findViewById(R.id.register_password);
        RegisterName = (EditText)findViewById(R.id.register_name);

        RegisterButton = (Button)findViewById(R.id.register_btn);

        LoadingBar = new ProgressDialog(this);

        LoadingBar.setTitle("Creating Account..");
        LoadingBar.setMessage("Please wait,while we are checking our credentials.....");
        LoadingBar.setCanceledOnTouchOutside(false);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //now on click button we will take text from edit text and validate it is empty or not
                //save edit text entries in string that we take in starting
                Phone = RegisterPhone.getText().toString();
                Password = RegisterPassword.getText().toString();
                Name = RegisterName.getText().toString();

                CreateNewAccount(Phone,Password,Name);

            }
        });

    }

    private void CreateNewAccount(final String phone, final String password, final String name) {
        //now check it is empty or not

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please enter your phone number..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please enter your name..", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //start creating account
            LoadingBar.show();

            final DatabaseReference mRef;
            mRef = FirebaseDatabase.getInstance().getReference();

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //now check number that user enter is present in our database or not
                    if(!(snapshot.child("Users").child(phone).exists()))
                    {
                        //if user not exist then we create account in database
                        HashMap<String,Object> userdata = new HashMap<>();
                        userdata.put("phone",phone);
                        userdata.put("password",password);
                        userdata.put("name",name);
                        userdata.put("nsong","2");
                        userdata.put("token",2);
                        userdata.put("theme","0");

                        mRef.child("Users").child(phone).updateChildren(userdata)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful())
                                        {
                                            LoadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            LoadingBar.dismiss();
                                            Toast.makeText(RegisterActivity.this, "Please try again after sometime..", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                    else
                    {
                        //if user exist
                        LoadingBar.dismiss();
                        Toast.makeText(RegisterActivity.this, "User with this number already exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}









