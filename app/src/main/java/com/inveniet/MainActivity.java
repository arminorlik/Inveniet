package com.inveniet;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.matrixxun.producttour.PrefConstants;
import com.matrixxun.producttour.ProductTourActivity;
import com.matrixxun.producttour.SAppUtil;

import info.ConnectionDetector;

public class MainActivity extends Activity implements Animation.AnimationListener {

    // Animation
    Animation animFadein;
    MediaPlayer mp;
    public boolean jestsiec = false;
    ConnectionDetector sprawdzsiec;
    ImageButton btnStart;
    boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this, R.raw.btn_click2);
        sprawdzsiec = new ConnectionDetector(getApplicationContext());
        doubleBackToExitPressedOnce = false;

        ImageButton btnStart = (ImageButton) findViewById(R.id.butStart);
        checkShowTutorial();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SprawdzInternet();
            }
        });

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);

        // set animation listener
        animFadein.setAnimationListener(this);
    }

    private void checkShowTutorial() { // sprawdzenie czy pierwsze uruchomienie
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = SAppUtil.getAppVersionCode(this);
        if (currentVersionCode > oldVersionCode) {
            startActivity(new Intent(MainActivity.this, ProductTourActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for fade in animation
        if (animation == animFadein) {
            startActivity(new Intent(getApplicationContext(), Categories.class));
            //startActivity(new Intent(getApplicationContext(), info.MainActivity.class));
            finish();
        }
    }

    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    public void SprawdzInternet() {
        jestsiec = sprawdzsiec.isConnectingToInternet();

        if (!jestsiec) {
            Toast.makeText(this, "Wymagane połączenie z internetem",
                    Toast.LENGTH_LONG).show();
        } else {//jesli połączenie z internetem jest aktywne
            mp.start();
            ImageButton btnStart = (ImageButton) findViewById(R.id.butStart);
            btnStart.startAnimation(animFadein);
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Wciśnij ponownie przycisk WSTECZ aby wyjść...", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
