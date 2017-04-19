package com.example.arturo.fantasyfootball;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button pvp;
    private Button pve;
    private Button season;
    private Button help;

    private boolean quit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    @Override
    public void onBackPressed(){

        if(quit){
            finish();
            System.exit(0);
        }

        if(!quit){
            Toast.makeText(this, "Press Back Again To Quit", Toast.LENGTH_SHORT).show();
            quit = true;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                quit = false;
            }
        }, 2000);
    }

    private void initialize() {
        pvp = (Button)findViewById(R.id.btnPVP);
        pve = (Button)findViewById(R.id.btnPVE);
        season = (Button)findViewById(R.id.btnSeason);
        help = (Button)findViewById(R.id.btnHelp);

        pvp.setOnClickListener(this);
        pve.setOnClickListener(this);
        season.setOnClickListener(this);
        help.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnPVP:
                pvp();
                break;
            case R.id.btnPVE:
                pve();
                break;
            case R.id.btnSeason:
                season();
                break;
            default:
                break;
        }
    }

    private void pvp(){
        draftActivity(2);
    }

    private void pve(){
        draftActivity(1);
    }

    private void season() {
        draftActivity(3);
    }

    private void draftActivity(int mode) {
        Intent i = new Intent(this, Draft.class);
        i.putExtra("selectedMode", mode);
        i.putExtra("seasonMode", true);
        if(mode == 3)
            i.putExtra("newSeason", true);
        startActivity(i);
    }
}
