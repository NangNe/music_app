package com.sub.andro.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    //  Now give reference to UI elements
    LinearLayout L1,L2;
    TextView tv;

    Animation DowntoTop,Fade;

    //make xml file for animation ......
    //here on tag line i will give fading animation and on linear layout 2 i will give down to top animation
    //lets make animation folder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        L1 = (LinearLayout)findViewById(R.id.l1);
        L2 = (LinearLayout)findViewById(R.id.l2);

        tv = (TextView)findViewById(R.id.tag);

        DowntoTop = AnimationUtils.loadAnimation(this,R.anim.downtotop);
        Fade = AnimationUtils.loadAnimation(this,R.anim.fade);

        L2.setAnimation(DowntoTop);
        tv.setAnimation(Fade);

        final Intent i = new Intent(MainActivity.this,HomeActivity.class);

        Thread thread =new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(5000); //to stay on splash screen   =>>>>   sleep for 5 second
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };thread.start();

    }
}
