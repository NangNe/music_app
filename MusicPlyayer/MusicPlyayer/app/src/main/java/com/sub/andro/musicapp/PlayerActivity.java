package com.sub.andro.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ImageView CoverImage;
    TextView SongTitle,SongArtist;

    //button for next and prev
    Button Play,Pause,Next,Prev,Share;
    MediaPlayer mediaPlayer; //to play song

    SeekBar seekBar;
    TextView Pass,Due; //for time due and pass

    Handler handler;
    String out,out2;
    Integer difference;
    Integer position;

    ArrayList<String> arrayListUrl;
    ArrayList<String> arrayListSong;
    ArrayList<String> arrayListArtist;
    ArrayList<String> arrayListImage;

    Animation uptodown,fade;

    String phone;
    DatabaseReference mref;
    RelativeLayout playerRL;
    String themeValue;

    String shareUrl;

    @Override
    protected void onResume() {
        Intent i = getIntent();
        phone = i.getStringExtra("phone");
        mref = FirebaseDatabase.getInstance().getReference("Users");
        playerRL = (RelativeLayout)findViewById(R.id.playerRL) ;

        mref.addValueEventListener(new ValueEventListener() {
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
            //if value 0 then light theme else dark theme
            playerRL.setBackgroundResource(R.drawable.grad8);
        }
        else
        {
            playerRL.setBackgroundResource(R.drawable.back2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //this error will gone when i restart

        //so here i want those value that we pass in intent
        // for that


        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        fade = AnimationUtils.loadAnimation(this,R.anim.fade);

        CoverImage = (ImageView)findViewById(R.id.coverImageView);
        SongTitle  =(TextView)findViewById(R.id.song_title);
        SongArtist = (TextView)findViewById(R.id.song_artist);

        Play = (Button)findViewById(R.id.playBtn);
        Pause = (Button)findViewById(R.id.pauseBtn);
        seekBar = (SeekBar)findViewById(R.id.seek_bar);
        Pass = (TextView)findViewById(R.id.tv_pass);
        Due = (TextView)findViewById(R.id.tv_due);

        Next = (Button)findViewById(R.id.next);
        Prev = (Button)findViewById(R.id.prev);
        Share = (Button)findViewById(R.id.share);

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label",shareUrl);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(PlayerActivity.this, "Song url : "+shareUrl+" copied to clipboard...", Toast.LENGTH_SHORT).show();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //so here we check condition that if user reach to the last song i.e at index 9 (10th song) so the next song should be play from first
                //that is index 0 for that...
                if(arrayListUrl.size() == position +1)
                {
                    //when user at last song, next position
                    position = 0;
                    //after getting position we will call function init

                    //so we will pass next data by this method
                    init(arrayListSong.get(position),arrayListArtist.get(position),arrayListUrl.get(position),arrayListImage.get(position));

                }
                else
                {
                    //if user not at last song...if in middle or in starting...in that condition
                    position = position +1;
                    init(arrayListSong.get(position),arrayListArtist.get(position),arrayListUrl.get(position),arrayListImage.get(position));
                    //increase position by 1 for next song.......
                }
            }
        });
        Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if user at first song and click prev ..at this codition last song should play ...for that
                if(position ==0)
                {
                    position = arrayListUrl.size() -1;
                    init(arrayListSong.get(position),arrayListArtist.get(position),arrayListUrl.get(position),arrayListImage.get(position));
                }
                else
                {
                    //in middle
                    position = position -1;
                    init(arrayListSong.get(position),arrayListArtist.get(position),arrayListUrl.get(position),arrayListImage.get(position));
                    //decrease position by 1
                }

            }
        });

        //give reference to next and prev button

        //
        mediaPlayer = new MediaPlayer();

        handler = new Handler();

        Intent i =getIntent();
        String song  = i.getStringExtra("song");
        String url = i.getStringExtra("url");
        String artist = i.getStringExtra("artist");
        String cover_image = i.getStringExtra("cover_image");

        arrayListUrl = i.getStringArrayListExtra("arrayListUrl");
        arrayListSong = i.getStringArrayListExtra("arrayListSong");
        arrayListArtist = i.getStringArrayListExtra("arrayListArtist");
        arrayListImage = i.getStringArrayListExtra("arrayListImage");
        position = Integer.parseInt(i.getStringExtra("position"));

        //retrieve position



        //coverimage url

        //make sure names are correct
        //lets test it with show toast
        Toast.makeText(this, song, Toast.LENGTH_SHORT).show();

        //now i will set text

        init(song,artist,url,cover_image);

        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        //now i will create method to initialize seekbar
        initializeSeekBar();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser)
                {
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void init(String song, String artist, String url, String cover_image) {

        //now when new data come here it will update this....


        SongTitle.setText(song);
        SongArtist.setText(artist);

        //now set animation
        SongTitle.setAnimation(fade);
        SongArtist.setAnimation(fade);

        Glide.with(this)
                .load(cover_image)
                .override(300,200)
                .into(CoverImage);

        //this will load image from url and set into our CoverImage View

        CoverImage.setAnimation(uptodown);

        //now i will set url of song to the media player
        //url that we get from intent

        //before playing new song we need to check condition here , is any song is playing we need to stop that song
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        //finally initialize seek bar
        initializeSeekBar();

        shareUrl = url;
        //so every time new song will play then its url will be set to shareUrl

    }

    private void initializeSeekBar() {

        seekBar.setMax(mediaPlayer.getDuration()/1000); //let max limit of song //set total duration

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null)
                {
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() /1000;
                    seekBar.setProgress(mCurrentPosition);

                    out = String.format("%02d:%02d",seekBar.getProgress() / 60,seekBar.getProgress() % 60);
                    //this will store string of time in min and sec
                    //now i will set this string to pass text view
                    Pass.setText(out);

                    //now for time left
                    difference = mediaPlayer.getDuration()/1000 - mediaPlayer.getCurrentPosition()/1000;

                    out2 = String.format("%02d:%02d",difference / 60 , difference % 60);
                    Due.setText(out2);

                }
                handler.postDelayed(this,1000);
            }
        });
    }

    private void play() {
        //now when user click on play then play button visibility should gone and pause button should be visible
        //and media player should start
        mediaPlayer.start();
        Play.setVisibility(View.INVISIBLE);
        Pause.setVisibility(View.VISIBLE);
    }

    private void pause() {
        //here media player should pause and play button should visible
        mediaPlayer.pause();
        Play.setVisibility(View.VISIBLE);
        Pause.setVisibility(View.INVISIBLE);
    }

}
