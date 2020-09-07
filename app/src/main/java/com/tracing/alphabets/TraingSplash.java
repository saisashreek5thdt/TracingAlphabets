package com.tracing.alphabets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

public class TraingSplash extends AppCompatActivity {

    VideoView vid;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traing_splash);

        vid = (VideoView)findViewById(R.id.splash);
        String urlpath ="android.resource://" + getPackageName() + "/" + R.raw.launcher;
        vid.setVideoURI(Uri.parse(urlpath));
        vid.start();

        mHandler.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(TraingSplash.this,A_Trace.class);
                startActivity(i);
                finish();

            }
        }, 7000);

        startService(new Intent(getBaseContext(),MyService.class));

    }
}
