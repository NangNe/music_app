package com.sub.andro.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sub.andro.musicapp.Model.SongModel;
import com.sub.andro.musicapp.Retrofit.ApiClient;
import com.sub.andro.musicapp.Retrofit.ApiInterface;

import java.util.List;

public class SongsListActivity extends AppCompatActivity {

    //now initialize adpater and recycler view
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;

    TextView TokenTV;
    Button showMore;

    String phone;
    DatabaseReference mRef;
    String tokenValue="";
    String nsong="";
    Integer totalSong;

    String themeValue;
    RelativeLayout songlistRL;

    @Override
    protected void onResume() {
        Intent i =getIntent();
        phone = i.getStringExtra("phone");
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        songlistRL = (RelativeLayout)findViewById(R.id.songlistRL);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
        if(themeValue.equals("0"))
        {
            songlistRL.setBackgroundResource(R.drawable.grad8);
        }
        else
        {
            songlistRL.setBackgroundResource(R.drawable.back2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);

        Intent i =getIntent();
        phone = i.getStringExtra("phone");
        //now i will make the use of this phone number for reference to firebase...
        TokenTV = (TextView)findViewById(R.id.tokenmain);
        showMore = (Button)findViewById(R.id.showmore);

        mRef = FirebaseDatabase.getInstance().getReference("Users");

        //use phone no. here to get data from firebase
        mRef.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tokenValue = snapshot.child("token").getValue().toString();
                nsong = snapshot.child("nsong").getValue().toString();
                TokenTV.setText("Tokens : "+tokenValue);

                //we need to set theme value on create method here for better performance
                themeValue = snapshot.child("theme").getValue().toString();
                checkTheme(themeValue);

                //so this will fetch value from firebase and set to text view...

                //so here we will fetch value as of now it is 2...
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //now on click show more we need to decrease token value by one and increase nsong value by 1 .....for that
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(tokenValue)>0)
                {
                    //decrease token value on  click show more by one and update tokenvalue
                    tokenValue = String.valueOf((Integer.parseInt(tokenValue))-1);
                    nsong = String.valueOf((Integer.parseInt(nsong))+1);
                    //update by increase by 1

                    //now we will set new updated value in firebase ..so that list of song will update according to that values...
                    mRef.child(phone).child("token").setValue(tokenValue);
                    mRef.child(phone).child("nsong").setValue(nsong);

                }
                else
                {
                    //if token will be zero then...
                    Toast.makeText(SongsListActivity.this, "Please purchase tokens....", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //initialize interface to make call and retrieve data from api

        ApiInterface service = ApiClient.getRetrofitInstance().create(ApiInterface.class);

        Call<List<SongModel>> call = service.getStudio();

        call.enqueue(new Callback<List<SongModel>>() {
            @Override
            public void onResponse(Call<List<SongModel>> call, Response<List<SongModel>> response) {
                loadDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<SongModel>> call, Throwable t) {
                Toast.makeText(SongsListActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataList(final List<SongModel> songsList) {

        recyclerView = findViewById(R.id.myRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SongsListActivity.this);
        recyclerView.setLayoutManager(layoutManager);


        mRef.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //fetch nsong value here
                totalSong = Integer.parseInt(snapshot.child("nsong").getValue().toString());
                myAdapter = new MyAdapter(songsList.subList(0,totalSong),phone);
                recyclerView.setAdapter(myAdapter);

                //now suppose nsong value is 2 ...then songlist will contain song detail of index 0,1 i.e only 2 song...

                //we need to update song list according to nsong value

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //so we set our adapter to recycler view
        //now lets run it and see it retrieve data and show properly in recycler view or not
    }
}
