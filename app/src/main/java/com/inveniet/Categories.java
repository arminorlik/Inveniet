package com.inveniet;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Categories extends Activity implements View.OnClickListener {

    String wybor;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        mp = MediaPlayer.create(this, R.raw.btn_click2);


        ImageButton btnRestauracja = (ImageButton) findViewById(R.id.btnRest);
        ImageButton btnKawiarnia = (ImageButton) findViewById(R.id.btnKawiarnia);
        ImageButton btnBar = (ImageButton) findViewById(R.id.btnBar);
        ImageButton btnFastFood = (ImageButton) findViewById(R.id.btnFastFood);
        ImageButton btnKlub = (ImageButton) findViewById(R.id.btnKlub);
        ImageButton btnImpreza = (ImageButton) findViewById(R.id.btnImpreza);
        ImageButton btnMuzyka = (ImageButton) findViewById(R.id.btnMuzyka);
        ImageButton btnKoncert = (ImageButton) findViewById(R.id.btnKoncert);

        btnRestauracja.setOnClickListener(this);
        btnKawiarnia.setOnClickListener(this);
        btnBar.setOnClickListener(this);
        btnFastFood.setOnClickListener(this);
        btnKlub.setOnClickListener(this);
        btnImpreza.setOnClickListener(this);
        btnMuzyka.setOnClickListener(this);
        btnKoncert.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        wybor = "";//zeruje zmiennia wyboru
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRest:
                mp.start();
                wybor = "1";
                uruchomMape(wybor);
                break;
        }
        switch (v.getId()) {
            case R.id.btnKawiarnia:
                mp.start();
                wybor = "2";
                uruchomMape(wybor);
                break;
        }
        switch (v.getId()) {
            case R.id.btnBar:
                mp.start();
                wybor = "3";
                uruchomMape(wybor);
                break;
        }
        switch (v.getId()) {
            case R.id.btnFastFood:
                mp.start();
                wybor = "4";
                uruchomMape(wybor);
                break;
        }
        switch (v.getId()) {
            case R.id.btnKlub:
                mp.start();
                wybor = "5";
                uruchomMape(wybor);
                break;
        }
        switch (v.getId()) {
            case R.id.btnImpreza:
                mp.start();
                wybor = "6";
                uruchomMape(wybor);
                break;
        }
        switch (v.getId()) {
            case R.id.btnMuzyka:
                mp.start();
                wybor = "7";
                uruchomMape(wybor);
                break;
        }
        switch (v.getId()) {
            case R.id.btnKoncert:
                mp.start();
                wybor = "8";
                uruchomMape(wybor);
                break;
        }
    }

    public void uruchomMape(String parametr) {
        Intent intent = new Intent(getApplicationContext(),
                Map.class);
        Bundle b = new Bundle();
        b.putString("selectedItems", parametr);
        intent.putExtras(b);
        startActivity(intent);

    }
}
