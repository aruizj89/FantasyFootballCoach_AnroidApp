package com.example.arturo.fantasyfootball;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SeasonMode extends AppCompatActivity {

    private int currentTeam;

    private Team userTeam;
    private League league;
    private int week;
    private boolean newSeason;

    //views
    TextView tvTeamRating;
    TextView tvRecord;
    TextView tvTeamName;
    TextView tvOpponent;
    TextView tvWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season_mode_main);

        Bundle teamData = getIntent().getExtras();
        if(teamData == null){
            return;
        }
        else{
            Intent intent = getIntent();
            league = (League) intent.getSerializableExtra("theLeague");
        }

        initializeViews();

        initialize();

    }

    public void onClickPlayNow(View v){

        Runnable  r = new Runnable(){
            @Override
            public void run(){
                simulateWeek();
            }
        };

        Thread simulationThread = new Thread(r);
        simulationThread.start();
        //playNow();
        currentTeam = 0;
        setMainView();
    }

    public void onClickRosterView(View v){
        setRosterView();
    }

    private void initializeViews(){
        tvTeamRating = (TextView)findViewById(R.id.tvTeamRating);
        tvRecord = (TextView)findViewById(R.id.tvRecord);
        tvTeamName = (TextView)findViewById(R.id.tvTeamName);
        tvOpponent = (TextView)findViewById(R.id.tvOpponent);
        tvWeek = (TextView)findViewById(R.id.tvWeek);
    }

    private void initialize() {
        currentTeam = 0;
        setMainView();
    }

    private void createLeague() {
        league = new League(1, userTeam);
        ScheduleSim schedule = new ScheduleSim();
        schedule.createSchedule(league);
        //league.getTeam(0).getSchedule().addGame(league.getTeam(1), 0, true);
    }

    private void setMainView(){
        /*
         *shows:
         *  name
         *  record
         *  current week
         *  current opponent (vs opp, @ opp)
         *
         * buttons:
         *  playNow()
         *  rosterView
         *  statsView
         *  seasonScheduleView
         *  leagueSettingsView
         *
         */

        tvTeamName.setText("Team " +  league.getTeam(currentTeam%32).getTeamName());
        tvTeamRating.setText(Integer.toString(league.getTeam(currentTeam % 32).getTeamOvr()));
        tvRecord.setText(league.getTeam(currentTeam % 32).getSchedule().getRecord());

        if(week < 17) { //regular season View
            tvWeek.setText("Week " + Integer.toString(week + 1));//show current week

            //set tvOpponent
            String opponentString;
            opponentString = league.getTeam(currentTeam % 32).getSchedule().getNextGame().getTeamName();
            if (league.getTeam(currentTeam % 32).getSchedule().getHomeField(week))
                tvOpponent.setText("VS Team" + opponentString);
            else
                tvOpponent.setText("@ Team" + opponentString);
        }else{
            tvWeek.setText("Season Over");
            tvOpponent.setText("(Playoffs Coming Soon)");

        }
    }



    private void setRosterView(){
        /*
         *shows:
         *   teamRating:
         *     passing
         *     running
         *     blocking
         *     passRushing
         *     runStopping
         *     passCoverage
         *     kicking
         *   positions | ovrRating
         *
         *   buttons
         *   back (mainView)
         */
        currentTeam++;
        setMainView();
    }

    private void statsView(){
        /*
         *shows:
         *   4 offense/defense (top3 + user)
         *buttons:
         *   teamStatsView
         *   playerStatsView
         *   back (mainView)
         */
    }

    private void teamStatsView(){
        /*
         *shows:
         *   title
         *   all 32 teams shown, scroll to get more teams
         *   oStats(total | passing | rushing) :: yards
         *     sideScroll for details(td's | turnovers)
         *   dStats(total | passing | rushing)
         *     sideScroll for details(sacks | turnovers)
         *buttons:
         *   alternateView( oStats | dStats )
         *   back
         */
    }

    private void playerStatsView(){
        /*
         *shows:
         *   player( pos | name | # | rating )
         *   stats
         *     player.getStats()
         *   swipe ( left | right ) to change player
         *buttons:
         *   back (mainView)
         */

    }

    private void seasonScheduleView(){
        /*
         *shows:
         *   past games( week & opponent & result)
         *   upcoming games( week & opponent & opponent record)
         *
         *buttons:
         *   back (mainView)
         *
         */

    }

    private void leagueSettingsView(){
        /*
         *buttons:
         *   back (mainView)
         *   save
         *   deleteLeague & exit
         */
    }

    private void playNow(){
        Toast.makeText(this, "play_now", Toast.LENGTH_SHORT).show();
        //simulateAIGames
        //simulateWeek();

        //get info for user game ready
        Intent game = new Intent(this, PVPGame.class);
        //send seasonMode
        game.putExtra("seasonMode", true);
        //send league
        game.putExtra("league", league);
        //send homeTeam
        game.putExtra("draftedTeam", league.getTeam(0));
        //send awayTeam
        game.putExtra("draftedAwayTeam", league.getTeam(0).getSchedule().getNextGame());

        //go to game
        startActivity(game);
    }

    private void simulateWeek() {
        WeekSim sim = new WeekSim();
        sim.simGame(league, week);
        week++;
    }

}
