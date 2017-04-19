package com.example.arturo.fantasyfootball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import org.w3c.dom.Text;

import java.util.Random;

public class Draft extends AppCompatActivity implements View.OnClickListener{
    //handler for schedule creating
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            toastScheduleSet();
        }
    };


    //text views
    private TextView budgetView;
    private TextView budgetTextView;
    private TextView draftPositionView;
    private TextView teamHomeAway;
    private TextView statDescription1, statDescription2, statDescription3, costView;
    private TextView tvStat1P1, tvStat2P1, tvStat3P1, costP1View;
    private TextView tvStat1P2, tvStat2P2, tvStat3P2, costP2View;
    private TextView tvStat1P3, tvStat2P3, tvStat3P3, costP3View;

    //buttons
    private Button wr1, lt, lg, c, rg, rt, te, wr2, qb, fb, rb;
    private Button fs, ss, lolb, mlb, rolb, cb1, cb2, le, dt1, dt2, re;
    private Button k, p;
    private Button player1, player2, player3;
    private Button simDraft;

    //draft global variables;
    private Team homeTeam, awayTeam;
    private Player createdPlayer1, createdPlayer2, createdPlayer3;
    private int budget;
    private String budgetText;
    private Random numGen;
    private boolean positionChosen;
    private boolean simulateDraft;
    private int teamsDrafted;
    private int humanPlayers;
    private int draftingTeam;
    private int gameMode;
    private League league;
    private boolean seasonMode;
    private boolean scheduleSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft2);

        initialize();

        Bundle playersData = getIntent().getExtras();
        if(playersData == null){
            return;
        }
        else{
            gameMode = playersData.getInt("selectedMode");
            if( gameMode == 2 )
                humanPlayers = 2;
            else
                humanPlayers = 1;
            seasonMode = playersData.getBoolean("seasonMode", false);

            if( gameMode == 3){
                scheduleSet = false;
                Runnable  r = new Runnable(){
                    @Override
                    public void run(){
                        ScheduleSim makeSchedule = new ScheduleSim();
                        makeSchedule.createSchedule(league);
                        scheduleSet = true;
                        handler.sendEmptyMessage(0);
                    }
                };

                Thread simulationThread = new Thread(r);
                simulationThread.start();
            }
        }
    }

    public void initialize(){
        //draft variables
        homeTeam = new Team("Home", 0, true);
        homeTeam.setUserTeam();
        awayTeam = new Team("Away");
        league = new League(0);
        budget = 800;
        numGen = new Random();
        positionChosen = false;
        simulateDraft = false;
        budgetText = "$" + budget;
        teamsDrafted = 0;
        humanPlayers = 0;
        draftingTeam = 1;
        gameMode = 1;
        scheduleSet = true;

        //set views
        budgetView = (TextView)findViewById(R.id.budgetView);
        budgetTextView = (TextView)findViewById(R.id.budgetTextView);
        draftPositionView = (TextView)findViewById(R.id.draftPositionView);
        teamHomeAway = (TextView)findViewById(R.id.teamHomeAway);

        //set view text
        budgetView.setText(budgetText);
        teamHomeAway.setText("Home Team");

        //set textviews for player drafting
        statDescription1 = (TextView)findViewById(R.id.stat1Description);
        statDescription2 = (TextView)findViewById(R.id.stat2Description);
        statDescription3 = (TextView)findViewById(R.id.stat3Description);
        costView = (TextView)findViewById(R.id.costView);

        tvStat1P1 = (TextView)findViewById(R.id.tvStat1P1);
        tvStat2P1 = (TextView)findViewById(R.id.tvStat2P1);
        tvStat3P1 = (TextView)findViewById(R.id.tvStat3P1);
        costP1View = (TextView)findViewById(R.id.costP1View);

        tvStat1P2 = (TextView)findViewById(R.id.tvStat1P2);
        tvStat2P2 = (TextView)findViewById(R.id.tvStat2P2);
        tvStat3P2 = (TextView)findViewById(R.id.tvStat3P2);
        costP2View = (TextView)findViewById(R.id.costP2View);

        tvStat1P3 = (TextView)findViewById(R.id.tvStat1P3);
        tvStat2P3 = (TextView)findViewById(R.id.tvStat2P3);
        tvStat3P3 = (TextView)findViewById(R.id.tvStat3P3);
        costP3View = (TextView)findViewById(R.id.costP3View);

        //set buttons
        wr1 = (Button)findViewById(R.id.wr1);
        lt = (Button)findViewById(R.id.lt);
        lg = (Button)findViewById(R.id.lg);
        c = (Button)findViewById(R.id.c);
        rg = (Button)findViewById(R.id.rg);
        rt = (Button)findViewById(R.id.rt);
        te = (Button)findViewById(R.id.te);
        wr2 = (Button)findViewById(R.id.wr2);
        qb = (Button)findViewById(R.id.qb);
        fb = (Button)findViewById(R.id.fb);
        rb = (Button)findViewById(R.id.rb);
        fs = (Button)findViewById(R.id.fs);
        ss = (Button)findViewById(R.id.ss);
        rolb = (Button)findViewById(R.id.rolb);
        mlb = (Button)findViewById(R.id.mlb);
        lolb = (Button)findViewById(R.id.lolb);
        cb1 = (Button)findViewById(R.id.cb1);
        cb2 = (Button)findViewById(R.id.cb2);
        re = (Button)findViewById(R.id.re);
        dt1 = (Button)findViewById(R.id.dt1);
        dt2 = (Button)findViewById(R.id.dt2);
        le = (Button)findViewById(R.id.le);
        k = (Button)findViewById(R.id.k);
        p = (Button)findViewById(R.id.p);
        player1 = (Button)findViewById(R.id.player1);
        player2 = (Button)findViewById(R.id.player2);
        player3 = (Button)findViewById(R.id.player3);
        simDraft = (Button)findViewById(R.id.simDraft);
        //set button text
        updateViews();
        //set button listener
        wr1.setOnClickListener(this);
        lt.setOnClickListener(this);
        lg.setOnClickListener(this);
        c.setOnClickListener(this);
        rg.setOnClickListener(this);
        rt.setOnClickListener(this);
        wr2.setOnClickListener(this);
        te.setOnClickListener(this);
        qb.setOnClickListener(this);
        fb.setOnClickListener(this);
        rb.setOnClickListener(this);
        fs.setOnClickListener(this);
        ss.setOnClickListener(this);
        rolb.setOnClickListener(this);
        mlb.setOnClickListener(this);
        lolb.setOnClickListener(this);
        cb1.setOnClickListener(this);
        cb2.setOnClickListener(this);
        re.setOnClickListener(this);
        dt1.setOnClickListener(this);
        dt2.setOnClickListener(this);
        le.setOnClickListener(this);
        k.setOnClickListener(this);
        player1.setOnClickListener(this);
        player2.setOnClickListener(this);
        player3.setOnClickListener(this);
        simDraft.setOnClickListener(this);

    }

    private void updateViews(){
        budgetText = "$" + budget;
        budgetView.setText(budgetText);

        if(draftingTeam == 1)
            updatePlayerView(homeTeam);
        else
            updatePlayerView(awayTeam);


        if(draftingTeam == 1){
            if(homeTeam.isTeamFull()){
                budgetView.setText(" ");
                budgetTextView.setText(" ");
                draftPositionView.setText("Draft Completed");
                player1.setText(" ");
                player2.setText(" ");
                player3.setText(" ");
                simDraft.setText("Continue");
            }
            else if(!positionChosen) {
                draftPositionView.setText("Choose a Position to Draft");
                player1.setText(" ");
                player2.setText(" ");
                player3.setText(" ");
                simDraft.setText("Simulate Draft");
            }
        }
        else{
            if(awayTeam.isTeamFull()){
                budgetView.setText(" ");
                budgetTextView.setText(" ");
                draftPositionView.setText("Draft Completed");
                player1.setText(" ");
                player2.setText(" ");
                player3.setText(" ");
                simDraft.setText("Continue");
            }
            else if(!positionChosen) {
                draftPositionView.setText("Choose a Position to Draft");
                player1.setText(" ");
                player2.setText(" ");
                player3.setText(" ");
                simDraft.setText("Simulate Draft");
            }
        }


    }

    private void teamView(){
        setContentView(R.layout.activity_draft2);
    }

    private void draftPositionView(){
        setContentView(R.layout.activity_draft);

        //player1
        tvStat1P1.setText( Integer.toString(createdPlayer1.getStat(1)) );
        tvStat2P1.setText( Integer.toString(createdPlayer1.getStat(2)) );
        tvStat3P1.setText( Integer.toString(createdPlayer1.getStat(3)) );
        costP1View.setText("$0");

        //player2View
        tvStat1P2.setText( Integer.toString(createdPlayer3.getStat(1)) );
        tvStat2P2.setText( Integer.toString(createdPlayer3.getStat(2)) );
        tvStat3P2.setText( Integer.toString(createdPlayer3.getStat(3)) );
        costP1View.setText("$0");

        //player
        tvStat1P3.setText( Integer.toString(createdPlayer3.getStat(1)) );
        tvStat2P3.setText( Integer.toString(createdPlayer3.getStat(2)) );
        tvStat3P3.setText( Integer.toString(createdPlayer3.getStat(3)) );
        costP1View.setText("$0");

    }

    private void updatePlayerView(Team thisTeam){
        if(thisTeam.getWR1().getStatRating() == 0)
            wr1.setText("WR");
        else
            wr1.setText(Integer.toString(thisTeam.getWR1().getStatRating()));

        if(thisTeam.getWR2().getStatRating() == 0)
            wr2.setText("WR");
        else
            wr2.setText(Integer.toString(thisTeam.getWR2().getStatRating()));

        if(thisTeam.getTE().getStatRating() == 0)
            te.setText("TE");
        else
            te.setText(Integer.toString(thisTeam.getTE().getStatRating()));

        if(thisTeam.getLT().getStatRating() == 0)
            lt.setText("LT");
        else
            lt.setText(Integer.toString(thisTeam.getLT().getStatRating()));

        if(thisTeam.getLG().getStatRating() == 0)
            lg.setText("LG");
        else
            lg.setText(Integer.toString(thisTeam.getLG().getStatRating()));

        if(thisTeam.getC().getStatRating() == 0)
            c.setText("C");
        else
            c.setText(Integer.toString(thisTeam.getC().getStatRating()));

        if(thisTeam.getRG().getStatRating() == 0)
            rg.setText("RG");
        else
            rg.setText(Integer.toString(thisTeam.getRG().getStatRating()));

        if(thisTeam.getRT().getStatRating() == 0)
            rt.setText("RT");
        else
            rt.setText(Integer.toString(thisTeam.getRT().getStatRating()));

        if(thisTeam.getQB().getStatRating() == 0)
            qb.setText("QB");
        else
            qb.setText(Integer.toString(thisTeam.getQB().getStatRating()));

        if(thisTeam.getFB().getStatRating() == 0)
            fb.setText("FB");
        else
            fb.setText(Integer.toString(thisTeam.getFB().getStatRating()));

        if(thisTeam.getRB().getStatRating() == 0)
            rb.setText("RB");
        else
            rb.setText(Integer.toString(thisTeam.getRB().getStatRating()));

        if(thisTeam.getLE().getStatRating() == 0)
            le.setText("LE");
        else
            le.setText(Integer.toString(thisTeam.getLE().getStatRating()));

        if(thisTeam.getRE().getStatRating() == 0)
            re.setText("RE");
        else
            re.setText(Integer.toString(thisTeam.getRE().getStatRating()));

        if(thisTeam.getDT1().getStatRating() == 0)
            dt1.setText("DT");
        else
            dt1.setText(Integer.toString(thisTeam.getDT1().getStatRating()));

        if(thisTeam.getDT2().getStatRating() == 0)
            dt2.setText("DT");
        else
            dt2.setText(Integer.toString(thisTeam.getDT2().getStatRating()));

        if(thisTeam.getLOLB().getStatRating() == 0)
            lolb.setText("LB");
        else
            lolb.setText(Integer.toString(thisTeam.getLOLB().getStatRating()));

        if(thisTeam.getROLB().getStatRating() == 0)
            rolb.setText("LB");
        else
            rolb.setText(Integer.toString(thisTeam.getROLB().getStatRating()));

        if(thisTeam.getMLB().getStatRating() == 0)
            mlb.setText("LB");
        else
            mlb.setText(Integer.toString(thisTeam.getMLB().getStatRating()));

        if(thisTeam.getFS().getStatRating() == 0)
            fs.setText("FS");
        else
            fs.setText(Integer.toString(thisTeam.getFS().getStatRating()));

        if(thisTeam.getSS().getStatRating() == 0)
            ss.setText("SS");
        else
            ss.setText(Integer.toString(thisTeam.getSS().getStatRating()));

        if(thisTeam.getCB1().getStatRating() == 0)
            cb1.setText("CB");
        else
            cb1.setText(Integer.toString(thisTeam.getCB1().getStatRating()));

        if (thisTeam.getCB2().getStatRating() == 0)
            cb2.setText("CB");
        else
            cb2.setText(Integer.toString(thisTeam.getCB2().getStatRating()));

        if(thisTeam.getK().getStatRating() == 0)
            k.setText("K");
        else
            k.setText(Integer.toString(thisTeam.getK().getStatRating()));
    }

    private void draftPosition(String position){
        if(draftingTeam == 1){
            if(homeTeam.positionFilled(position))
                return;
        }
        else{
            if(awayTeam.positionFilled(position))
                return;
        }

        positionChosen = true;
        //create players
        createdPlayer1 = new Player(position, getFractionalGrade(7));
        createdPlayer2 = new Player(position, getFractionalGrade(4));
        createdPlayer3 = new Player(position, getFractionalGrade(1));

        //simulate draft pick
        if(simulateDraft && draftingTeam == 1){
            if(homeTeam.isTeamFull())
                budget = 0;
            if(budget == 50){
                budget -= 50;
                draft(createdPlayer2);
            }
            if(budget == 0)
                return;
            switch (numGen.nextInt(2)){
                case 0:
                    budget -= 50;
                    draft(createdPlayer2);
                    break;
                case 1:
                    budget -= 100;
                    draft(createdPlayer3);
                    break;
                default:
                    break;
            }
            return;
        }
        if(simulateDraft && draftingTeam == 2){
            if(awayTeam.isTeamFull())
                budget = 0;
            if(budget == 50){
                budget -= 50;
                draft(createdPlayer2);
            }
            if(budget == 0)
                return;
            switch (numGen.nextInt(2)){
                case 0:
                    budget -= 50;
                    draft(createdPlayer2);
                    break;
                case 1:
                    budget -= 100;
                    draft(createdPlayer3);
                    break;
                default:
                    break;
            }
            return;
        }

        //update views
        player1.setText(createdPlayer1.getRating() + "\n$" + 0);
        player2.setText(createdPlayer2.getRating() + "\n$" + 50);
        player3.setText(createdPlayer3.getRating() + "\n$" + 100);
        draftPositionView.setText("Drafting: " + position);
        simDraft.setText(" ");
    }

    private void draft(Player player){
        if(draftingTeam == 1) {
            homeTeam.addPlayer(player);
            if(budget == 0)
                fillTeam();
        }
        else {
            awayTeam.addPlayer(player);
            if(budget == 0)
                fillTeam();
        }
    }

    private void simulateDraft(Team thisTeam){
        simulateDraft = true;
        while(budget > 0 && !thisTeam.isTeamFull()) {
            draftPosition(thisTeam.allPositions[numGen.nextInt(thisTeam.allPositions.length)]);
        }
        simulateDraft = false;
    }

    private void fillTeam() {
        if(draftingTeam == 1){
            if( homeTeam.getQB().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("QB", 7 + numGen.nextInt(3)));
            if( homeTeam.getRB().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("RB", 7 + numGen.nextInt(3)));
            if( homeTeam.getFB().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("FB", 7 + numGen.nextInt(3)));
            if( homeTeam.getWR1().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("WR1", 7 + numGen.nextInt(3)));
            if( homeTeam.getWR2().getStatRating() == 0)
                homeTeam.addPlayer(new Player("WR2", 7 + numGen.nextInt(3)));
            if( homeTeam.getTE().getStatRating() == 0)
                homeTeam.addPlayer(new Player("TE", 7 + numGen.nextInt(3)));
            if( homeTeam.getLT().getStatRating() == 0)
                homeTeam.addPlayer(new Player("LT", 7 + numGen.nextInt(3)));
            if( homeTeam.getLG().getStatRating() == 0)
                homeTeam.addPlayer(new Player("LG", 7 + numGen.nextInt(3)));
            if( homeTeam.getC().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("C", 7 + numGen.nextInt(3)));
            if( homeTeam.getRG().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("RG", 7 + numGen.nextInt(3)));
            if( homeTeam.getRT().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("RT", 7 + numGen.nextInt(3)));

            if( homeTeam.getLE().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("LE", 7 + numGen.nextInt(3)));
            if( homeTeam.getDT1().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("DT1", 7 + numGen.nextInt(3)));
            if( homeTeam.getDT2().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("DT2", 7 + numGen.nextInt(3)));
            if( homeTeam.getRE().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("RE", 7 + numGen.nextInt(3)));
            if( homeTeam.getLOLB().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("LOLB", 7 + numGen.nextInt(3)));
            if( homeTeam.getMLB().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("MLB", 7 + numGen.nextInt(3)));
            if( homeTeam.getROLB().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("ROLB", 7 + numGen.nextInt(3)));
            if( homeTeam.getCB1().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("CB1", 7 + numGen.nextInt(3)));
            if( homeTeam.getCB2().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("CB2", 7 + numGen.nextInt(3)));
            if( homeTeam.getFS().getStatRating() == 0)
                homeTeam.addPlayer(new Player("FS", 7 + numGen.nextInt(3)));
            if( homeTeam.getSS().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("SS", 7 + numGen.nextInt(3)));

            if( homeTeam.getK().getStatRating() == 0 )
                homeTeam.addPlayer(new Player("K", 7 + numGen.nextInt(3)));
        }
        else if(draftingTeam == 2){
            if( awayTeam.getQB().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("QB", 7 + numGen.nextInt(3)));
            if( awayTeam.getRB().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("RB", 7 + numGen.nextInt(3)));
            if( awayTeam.getFB().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("FB", 7 + numGen.nextInt(3)));
            if( awayTeam.getWR1().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("WR1", 7 + numGen.nextInt(3)));
            if( awayTeam.getWR2().getStatRating() == 0)
                awayTeam.addPlayer(new Player("WR2", 7 + numGen.nextInt(3)));
            if( awayTeam.getTE().getStatRating() == 0)
                awayTeam.addPlayer(new Player("TE", 7 + numGen.nextInt(3)));
            if( awayTeam.getLT().getStatRating() == 0)
                awayTeam.addPlayer(new Player("LT", 7 + numGen.nextInt(3)));
            if( awayTeam.getLG().getStatRating() == 0)
                awayTeam.addPlayer(new Player("LG", 7 + numGen.nextInt(3)));
            if( awayTeam.getC().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("C", 7 + numGen.nextInt(3)));
            if( awayTeam.getRG().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("RG", 7 + numGen.nextInt(3)));
            if( awayTeam.getRT().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("RT", 7 + numGen.nextInt(3)));

            if( awayTeam.getLE().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("LE", 7 + numGen.nextInt(3)));
            if( awayTeam.getDT1().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("DT1", 7 + numGen.nextInt(3)));
            if( awayTeam.getDT2().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("DT2", 7 + numGen.nextInt(3)));
            if( awayTeam.getRE().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("RE", 7 + numGen.nextInt(3)));
            if( awayTeam.getLOLB().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("LOLB", 7 + numGen.nextInt(3)));
            if( awayTeam.getMLB().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("MLB", 7 + numGen.nextInt(3)));
            if( awayTeam.getROLB().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("ROLB", 7 + numGen.nextInt(3)));
            if( awayTeam.getCB1().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("CB1", 7 + numGen.nextInt(3)));
            if( awayTeam.getCB2().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("CB2", 7 + numGen.nextInt(3)));
            if( awayTeam.getFS().getStatRating() == 0)
                awayTeam.addPlayer(new Player("FS", 7 + numGen.nextInt(3)));
            if( awayTeam.getSS().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("SS", 7 + numGen.nextInt(3)));

            if( awayTeam.getK().getStatRating() == 0 )
                awayTeam.addPlayer(new Player("K", 7 + numGen.nextInt(3)));
        }
    }

    private int getFractionalGrade(int grade){
        return (grade + numGen.nextInt(3));
    }

    private void toastScheduleSet(){
        Toast.makeText(this, "Season Schedule Complete", Toast.LENGTH_LONG).show();
    }

    private void endDraft(){
        if( !scheduleSet ){
            Toast.makeText(this, "Please Wait\nCreating Schedule", Toast.LENGTH_SHORT).show();
            return;
        }
        switch(gameMode){
            case 1:
                startPVE();
                break;
            case 2:
                startPVP();
                break;
            case 3:
                startSeason();
                break;
            default:
                break;
        }
    }

    private void startPVE(){
        Intent game = new Intent(this, PVEGame.class);

        //send arcadeMode
        game.putExtra("seasonMode", false);
        //send homeTeam
        game.putExtra("draftedHomeTeam", homeTeam);
        //send awayTeam
        game.putExtra("draftedAwayTeam", awayTeam);

        //go to game
        startActivity(game);
    }

    private void startPVP(){
        Intent game = new Intent(this, PVPGame.class);

        //send arcadeMode
        game.putExtra("seasonMode", false);
        //send homeTeam
        game.putExtra("draftedHomeTeam", homeTeam);
        //send awayTeam
        game.putExtra("draftedAwayTeam", awayTeam);

        //go to game
        startActivity(game);
    }

    private void startSeason() {
        league.insertTeam(homeTeam, 0);


        //start activity switch
        Intent game = new Intent(this, SeasonMode.class);

        //send draftedTeam
        game.putExtra("createdLeague", league);

        //go to season
        startActivity(game);
    }

    @Override
    public void onClick(View viewClick) {

        if(awayTeam.isTeamFull()){
            if (viewClick.getId() == R.id.simDraft)
                endDraft();
        }
        else if(homeTeam.isTeamFull() && draftingTeam == 1){
            if (viewClick.getId() == R.id.simDraft){
                resetDraftClass();
                if (humanPlayers == 1) {
                    budget = 1000;
                    simulateDraft(awayTeam);
                }
            }
        }
        else if( !positionChosen && budget > 0 ){
            switch(viewClick.getId()){
                case R.id.qb:
                    draftPosition("QB");
                    break;
                case R.id.rb:
                    draftPosition("RB");
                    break;
                case R.id.fb:
                    draftPosition("FB");
                    break;
                case R.id.wr1:
                    draftPosition("WR1");
                    break;
                case R.id.wr2:
                    draftPosition("WR2");
                    break;
                case R.id.te:
                    draftPosition("TE");
                    break;
                case R.id.lt:
                    draftPosition("LT");
                    break;
                case R.id.lg:
                    draftPosition("LG");
                    break;
                case R.id.c:
                    draftPosition("C");
                    break;
                case R.id.rg:
                    draftPosition("RG");
                    break;
                case R.id.rt:
                    draftPosition("RT");
                    break;
                case R.id.fs:
                    draftPosition("FS");
                    break;
                case R.id.ss:
                    draftPosition("SS");
                    break;
                case R.id.lolb:
                    draftPosition("LOLB");
                    break;
                case R.id.rolb:
                    draftPosition("ROLB");
                    break;
                case R.id.mlb:
                    draftPosition("MLB");
                    break;
                case R.id.le:
                    draftPosition("LE");
                    break;
                case R.id.re:
                    draftPosition("RE");
                    break;
                case R.id.dt1:
                    draftPosition("DT1");
                    break;
                case R.id.dt2:
                    draftPosition("DT2");
                    break;
                case R.id.cb1:
                    draftPosition("CB1");
                    break;
                case R.id.cb2:
                    draftPosition("CB2");
                    break;
                case R.id.k:
                    draftPosition("K");
                    break;
                case R.id.simDraft:
                    if(draftingTeam == 1)
                        simulateDraft(homeTeam);
                    else
                        simulateDraft(awayTeam);
                    break;
                default:
                    positionChosen = false;
                    break;
            }
        }
        else if(positionChosen && budget > 0){
            positionChosen = false;
            switch(viewClick.getId()){
                case R.id.player1:
                    draft(createdPlayer1);
                    break;
                case R.id.player2:
                    if(budget < 50) {
                        positionChosen = true;
                        break;
                    }
                    budget -= 50;
                    draft(createdPlayer2);
                    break;
                case R.id.player3:
                    if(budget < 100){
                        positionChosen = true;
                        break;
                    }
                    budget -= 100;
                    draft(createdPlayer3);
                    break;
                default:
                    positionChosen = true;
                    break;
            }
        }

        updateViews();

    }

    public void draftPlayer1(View view){
        draft(createdPlayer1);
        positionChosen = false;
    }

    public void draftPlayer2(View view){
        if(budget < 50)
            return;

        draft(createdPlayer2);
        positionChosen = false;
    }

    public void draftPlayer3(View view){
        if(budget < 100)
            return;

        draft(createdPlayer3);
        positionChosen = false;
    }

    private void resetDraftClass() {
        budget = 800;
        budgetTextView.setText("Budget: ");
        simDraft.setText("Simulate Draft");
        positionChosen = false;
        teamHomeAway.setText("Away Team");
        draftingTeam = 2;
        teamsDrafted++;
    }

}