package com.example.arturo.fantasyfootball;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.util.Random;

public class PVEGame extends AppCompatActivity implements View.OnClickListener{
    //quit app
    private boolean quit;
    private boolean seasonMode;

    //league variable
    private League league;

    //team variables
    private Team homeTeam;
    private Team awayTeam;

    //global game variables
    private Random ran = new Random();
    private int teamStatsView;

    //important stuffs
    private boolean playGame;
    private boolean gameOver, twoMinWarn;
    private int gameQuarter;
    private int gameTime;
    private int timeOffClock;
    private int userScore, aiScore;
    private int offensePossesion;//(1)home (2)away
    private int halfTimePossesion;
    private int driveDown;//1st, 2nd, etc.
    private int yardsForFirst;//yards needed for first
    private int fieldPosition;//field position; yards to TD
    private int ballMovement;//playYardageOutcome
    private int lastBallSpot;
    private boolean turnover;
    private int userTO, aiTO;//timeouts
    private int userUrgency, aiUrgency;//(-1)winning (0)tie (1)tieEnding (2)1TDGame (3)1TDGameEnding (4)>1TDGame
    private int userMomentum, aiMomentum;//momentum
    private boolean simEntireGame;
    private boolean simToEnd;
    private String showPlayDetails;
    private int offensePlayBonus;
    private int defensePlayBonus;
    private boolean aiTests = false;
    private int RunSuccessCount = 1000;
    private int PassSuccessCount = 1000;

    //layout indicators
    private boolean specialTeamsPlay;
    private boolean pauseGame;
    private boolean statsView;

    //textViews
    private TextView quarter, time, downDetails, playDetails, possession, teamStats;
    private TextView homeTeamName, homeTeamScore, awayTeamName, awayTeamScore;
    private String toastMessage;

    //buttons
    private Button pauseContinue, simEnd, passSave, runQuit;

    //imageViews
    private ImageView field;
    private ImageView fdl;
    private ImageView ball;

    //imageView resources
    int[] fdlImages = new int[99];
    int[] ballImages = new int[99];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamefield);
        quit = false;

        initializeGame();
        initializeLayout();

        Intent intent = getIntent();
        homeTeam = (Team) intent.getSerializableExtra("draftedHomeTeam");
        awayTeam = (Team) intent.getSerializableExtra("draftedAwayTeam");
        homeTeam.setTeamName("Home");
        awayTeam.setTeamName("Away");
        seasonMode = intent.getBooleanExtra("seasonMode", false);
        if(seasonMode)
            league = (League) intent.getSerializableExtra("league");
        updateView();
    }

    @Override
    public void onClick(View view) {
        if(gameOver){
            switch(view.getId()){
                case R.id.btnPauseContinue:
                    quitGame();
                    break;
                case R.id.btnSimEnd:
                    replayGame();
                    break;
                case R.id.btnPassSave:
                    //homeStatsView
                    break;
                case R.id.btnRunQuit:
                    //awayStatsView
                    break;
                default:
                    break;
            }
        }
        else if(pauseGame){
            switch(view.getId()){
                case R.id.btnPauseContinue:
                    statsView = false;
                    pauseGame = false;
                    break;
                case R.id.btnSimEnd:
                    quitGame();
                    break;
                case R.id.btnPassSave:
                    teamStatsView(homeTeam);
                    break;
                case R.id.btnRunQuit:
                    teamStatsView(awayTeam);
                    break;
                default:
                    break;
            }
        }
        else if(offensePossesion == 1){
            if(specialTeamsPlay){
                switch(view.getId()){
                    case R.id.btnPauseContinue:
                        pauseGame = true;
                        break;
                    case R.id.btnSimEnd:
                        specialTeamsPlay = false;
                        break;
                    case R.id.btnPassSave:
                        int validFG;
                        validFG = fgAttempt(homeTeam, awayTeam);
                        if(validFG != -1)
                            updateGame();
                        break;
                    case R.id.btnRunQuit:
                        punt();
                        updateGame();
                        break;
                    default:
                        break;
                }
            }
            else{
                switch(view.getId()){
                    case R.id.btnPauseContinue:
                        pauseView();
                        pauseGame = true;
                        break;
                    case R.id.btnSimEnd:
                        specialTeamsPlay = true;
                        break;
                    case R.id.btnPassSave:
                        pass(homeTeam, awayTeam);
                        updateGame();
                        break;
                    case R.id.btnRunQuit:
                        run(homeTeam, awayTeam);
                        updateGame();
                        break;
                    default:
                        break;
                }
            }
        }
        else{
            switch(view.getId()){
                case R.id.btnPauseContinue:
                    pauseView();
                    pauseGame = true;
                    break;
                case R.id.btnSimEnd:
                    aiPlay(2);
                    updateGame();
                    break;
                case R.id.btnPassSave:
                    aiPlay(1);
                    updateGame();
                    break;
                case R.id.btnRunQuit:
                    aiPlay(0);
                    updateGame();
                    break;
                default:
                    break;
            }
        }


        updateView();

    }

    @Override
    public void onBackPressed(){

        if(quit){
            Intent quitGame = new Intent(this, MainActivity.class);
            quitGame.putExtra("quitApp", true);
            startActivity(quitGame);
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

    //layout functions
    private void initializeLayout() {
        playGame = false;
        initializeWidgets();
        offenseView();
        homeTeam = new Team("Home");
        awayTeam = new Team("Away");
    }

    private void initializeWidgets(){
        //text views
        quarter = (TextView)findViewById(R.id.tvQuarter);
        time= (TextView)findViewById(R.id.tvTime);
        playDetails = (TextView)findViewById(R.id.tvPlayDetails);
        downDetails = (TextView)findViewById(R.id.tvDownDetails);
        homeTeamName = (TextView)findViewById(R.id.homeTeamName);
        homeTeamScore = (TextView)findViewById(R.id.homeTeamScore);
        awayTeamName = (TextView)findViewById(R.id.awayTeamName);
        awayTeamScore = (TextView)findViewById(R.id.awayTeamScore);
        possession = (TextView)findViewById(R.id.tvPossession);
        teamStats = (TextView)findViewById(R.id.tvTeamStats);

        //buttons
        pauseContinue = (Button)findViewById(R.id.btnPauseContinue);
        simEnd = (Button)findViewById(R.id.btnSimEnd);
        passSave = (Button)findViewById(R.id.btnPassSave);
        runQuit = (Button)findViewById(R.id.btnRunQuit);

        //listeners
        pauseContinue.setOnClickListener(this);
        simEnd.setOnClickListener(this);
        passSave.setOnClickListener(this);
        runQuit.setOnClickListener(this);

        //image view
        field = (ImageView)findViewById(R.id.imageField);
        fdl = (ImageView)findViewById(R.id.imageFDL);
        ball = (ImageView)findViewById(R.id.imageBall);
        initFDLImageRes();
        initBallImageRes();
    }

    private void initFDLImageRes(){
        fdlImages[0] = R.drawable.fdl01;
        fdlImages[1] = R.drawable.fdl02;
        fdlImages[2] = R.drawable.fdl03;
        fdlImages[3] = R.drawable.fdl04;
        fdlImages[4] = R.drawable.fdl05;
        fdlImages[5] = R.drawable.fdl06;
        fdlImages[6] = R.drawable.fdl07;
        fdlImages[7] = R.drawable.fdl08;
        fdlImages[8] = R.drawable.fdl09;
        fdlImages[9] = R.drawable.fdl10;

        fdlImages[10] = R.drawable.fdl11;
        fdlImages[11] = R.drawable.fdl12;
        fdlImages[12] = R.drawable.fdl13;
        fdlImages[13] = R.drawable.fdl14;
        fdlImages[14] = R.drawable.fdl15;
        fdlImages[15] = R.drawable.fdl16;
        fdlImages[16] = R.drawable.fdl17;
        fdlImages[17] = R.drawable.fdl18;
        fdlImages[18] = R.drawable.fdl19;
        fdlImages[19] = R.drawable.fdl20;

        fdlImages[20] = R.drawable.fdl21;
        fdlImages[21] = R.drawable.fdl22;
        fdlImages[22] = R.drawable.fdl23;
        fdlImages[23] = R.drawable.fdl24;
        fdlImages[24] = R.drawable.fdl25;
        fdlImages[25] = R.drawable.fdl26;
        fdlImages[26] = R.drawable.fdl27;
        fdlImages[27] = R.drawable.fdl28;
        fdlImages[28] = R.drawable.fdl29;
        fdlImages[29] = R.drawable.fdl30;

        fdlImages[30] = R.drawable.fdl31;
        fdlImages[31] = R.drawable.fdl32;
        fdlImages[32] = R.drawable.fdl33;
        fdlImages[33] = R.drawable.fdl34;
        fdlImages[34] = R.drawable.fdl35;
        fdlImages[35] = R.drawable.fdl36;
        fdlImages[36] = R.drawable.fdl37;
        fdlImages[37] = R.drawable.fdl38;
        fdlImages[38] = R.drawable.fdl39;
        fdlImages[39] = R.drawable.fdl40;

        fdlImages[40] = R.drawable.fdl41;
        fdlImages[41] = R.drawable.fdl42;
        fdlImages[42] = R.drawable.fdl43;
        fdlImages[43] = R.drawable.fdl44;
        fdlImages[44] = R.drawable.fdl45;
        fdlImages[45] = R.drawable.fdl46;
        fdlImages[46] = R.drawable.fdl47;
        fdlImages[47] = R.drawable.fdl48;
        fdlImages[48] = R.drawable.fdl49;
        fdlImages[49] = R.drawable.fdl50;

        fdlImages[50] = R.drawable.fdl51;
        fdlImages[51] = R.drawable.fdl52;
        fdlImages[52] = R.drawable.fdl53;
        fdlImages[53] = R.drawable.fdl54;
        fdlImages[54] = R.drawable.fdl55;
        fdlImages[55] = R.drawable.fdl56;
        fdlImages[56] = R.drawable.fdl57;
        fdlImages[57] = R.drawable.fdl58;
        fdlImages[58] = R.drawable.fdl59;
        fdlImages[59] = R.drawable.fdl60;

        fdlImages[60] = R.drawable.fdl61;
        fdlImages[61] = R.drawable.fdl62;
        fdlImages[62] = R.drawable.fdl63;
        fdlImages[63] = R.drawable.fdl64;
        fdlImages[64] = R.drawable.fdl65;
        fdlImages[65] = R.drawable.fdl66;
        fdlImages[66] = R.drawable.fdl67;
        fdlImages[67] = R.drawable.fdl68;
        fdlImages[68] = R.drawable.fdl69;
        fdlImages[69] = R.drawable.fdl70;

        fdlImages[70] = R.drawable.fdl71;
        fdlImages[71] = R.drawable.fdl72;
        fdlImages[72] = R.drawable.fdl73;
        fdlImages[73] = R.drawable.fdl74;
        fdlImages[74] = R.drawable.fdl75;
        fdlImages[75] = R.drawable.fdl76;
        fdlImages[76] = R.drawable.fdl77;
        fdlImages[77] = R.drawable.fdl78;
        fdlImages[78] = R.drawable.fdl79;
        fdlImages[79] = R.drawable.fdl80;

        fdlImages[80] = R.drawable.fdl81;
        fdlImages[81] = R.drawable.fdl82;
        fdlImages[82] = R.drawable.fdl83;
        fdlImages[83] = R.drawable.fdl84;
        fdlImages[84] = R.drawable.fdl85;
        fdlImages[85] = R.drawable.fdl86;
        fdlImages[86] = R.drawable.fdl87;
        fdlImages[87] = R.drawable.fdl88;
        fdlImages[88] = R.drawable.fdl89;
        fdlImages[89] = R.drawable.fdl90;

        fdlImages[90] = R.drawable.fdl91;
        fdlImages[91] = R.drawable.fdl92;
        fdlImages[92] = R.drawable.fdl93;
        fdlImages[93] = R.drawable.fdl94;
        fdlImages[94] = R.drawable.fdl95;
        fdlImages[95] = R.drawable.fdl96;
        fdlImages[96] = R.drawable.fdl97;
        fdlImages[97] = R.drawable.fdl98;
        fdlImages[98] = R.drawable.fdl99;
    }

    private void initBallImageRes(){
        ballImages[0] = R.drawable.b01;
        ballImages[1] = R.drawable.b02;
        ballImages[2] = R.drawable.b03;
        ballImages[3] = R.drawable.b04;
        ballImages[4] = R.drawable.b05;
        ballImages[5] = R.drawable.b06;
        ballImages[6] = R.drawable.b07;
        ballImages[7] = R.drawable.b08;
        ballImages[8] = R.drawable.b09;
        ballImages[9] = R.drawable.b10;

        ballImages[10] = R.drawable.b11;
        ballImages[11] = R.drawable.b12;
        ballImages[12] = R.drawable.b13;
        ballImages[13] = R.drawable.b14;
        ballImages[14] = R.drawable.b15;
        ballImages[15] = R.drawable.b16;
        ballImages[16] = R.drawable.b17;
        ballImages[17] = R.drawable.b18;
        ballImages[18] = R.drawable.b19;
        ballImages[19] = R.drawable.b20;

        ballImages[20] = R.drawable.b21;
        ballImages[21] = R.drawable.b22;
        ballImages[22] = R.drawable.b23;
        ballImages[23] = R.drawable.b24;
        ballImages[24] = R.drawable.b25;
        ballImages[25] = R.drawable.b26;
        ballImages[26] = R.drawable.b27;
        ballImages[27] = R.drawable.b28;
        ballImages[28] = R.drawable.b29;
        ballImages[29] = R.drawable.b30;

        ballImages[30] = R.drawable.b31;
        ballImages[31] = R.drawable.b32;
        ballImages[32] = R.drawable.b33;
        ballImages[33] = R.drawable.b34;
        ballImages[34] = R.drawable.b35;
        ballImages[35] = R.drawable.b36;
        ballImages[36] = R.drawable.b37;
        ballImages[37] = R.drawable.b38;
        ballImages[38] = R.drawable.b39;
        ballImages[39] = R.drawable.b40;

        ballImages[40] = R.drawable.b41;
        ballImages[41] = R.drawable.b42;
        ballImages[42] = R.drawable.b43;
        ballImages[43] = R.drawable.b44;
        ballImages[44] = R.drawable.b45;
        ballImages[45] = R.drawable.b46;
        ballImages[46] = R.drawable.b47;
        ballImages[47] = R.drawable.b48;
        ballImages[48] = R.drawable.b49;
        ballImages[49] = R.drawable.b50;

        ballImages[50] = R.drawable.b51;
        ballImages[51] = R.drawable.b52;
        ballImages[52] = R.drawable.b53;
        ballImages[53] = R.drawable.b54;
        ballImages[54] = R.drawable.b55;
        ballImages[55] = R.drawable.b56;
        ballImages[56] = R.drawable.b57;
        ballImages[57] = R.drawable.b58;
        ballImages[58] = R.drawable.b59;
        ballImages[59] = R.drawable.b60;

        ballImages[60] = R.drawable.b61;
        ballImages[61] = R.drawable.b62;
        ballImages[62] = R.drawable.b63;
        ballImages[63] = R.drawable.b64;
        ballImages[64] = R.drawable.b65;
        ballImages[65] = R.drawable.b66;
        ballImages[66] = R.drawable.b67;
        ballImages[67] = R.drawable.b68;
        ballImages[68] = R.drawable.b69;
        ballImages[69] = R.drawable.b70;

        ballImages[70] = R.drawable.b71;
        ballImages[71] = R.drawable.b72;
        ballImages[72] = R.drawable.b73;
        ballImages[73] = R.drawable.b74;
        ballImages[74] = R.drawable.b75;
        ballImages[75] = R.drawable.b76;
        ballImages[76] = R.drawable.b77;
        ballImages[77] = R.drawable.b78;
        ballImages[78] = R.drawable.b79;
        ballImages[79] = R.drawable.b80;

        ballImages[80] = R.drawable.b81;
        ballImages[81] = R.drawable.b82;
        ballImages[82] = R.drawable.b83;
        ballImages[83] = R.drawable.b84;
        ballImages[84] = R.drawable.b85;
        ballImages[85] = R.drawable.b86;
        ballImages[86] = R.drawable.b87;
        ballImages[87] = R.drawable.b88;
        ballImages[88] = R.drawable.b89;
        ballImages[89] = R.drawable.b90;

        ballImages[90] = R.drawable.b91;
        ballImages[91] = R.drawable.b92;
        ballImages[92] = R.drawable.b93;
        ballImages[93] = R.drawable.b94;
        ballImages[94] = R.drawable.b95;
        ballImages[95] = R.drawable.b96;
        ballImages[96] = R.drawable.b97;
        ballImages[97] = R.drawable.b98;
        ballImages[98] = R.drawable.b99;
    }

    private void updateView(){
        if(statsView)
            return;
        if(offensePossesion == 1){
            homeTeamName.setText(homeTeam.getTeamName() + " *");
            awayTeamName.setText(awayTeam.getTeamName());
        }
        else{
            homeTeamName.setText(homeTeam.getTeamName());
            awayTeamName.setText("* " + awayTeam.getTeamName());
        }

        updateField();

        if(gameOver) {
            gameStatsView();
            endView();
        }
        else if(pauseGame)
            pauseView();
        else if(offensePossesion == 1)
            offenseView();
        else
            defenseView();
    }

    private void updateField(){
        field.setImageResource(R.drawable.field_layout);

        if(offensePossesion == 1){
            ball.setImageResource(ballImages[100 - fieldPosition - 1]);
        }
        else{
            ball.setImageResource(ballImages[fieldPosition - 1]);
        }

        if(fieldPosition - yardsForFirst  > 0){
            if(offensePossesion == 1){
                fdl.setImageResource(fdlImages[((100 - fieldPosition) + yardsForFirst) - 1]);
            }
            else{
                fdl.setImageResource(fdlImages[(fieldPosition - yardsForFirst) - 1]);
            }
        }
        else
            fdl.setImageResource(R.drawable.clear_field);
    }

    private void quitView(){
        pauseContinue.setText("Continue**");
        simEnd.setText("Sim to End**");
        passSave.setText("Save**");
        runQuit.setText("Quit**");
    }

    private void pauseView(){
        pauseContinue.setText("Continue");
        simEnd.setText("Quit");
        passSave.setText("Home Stats**");
        runQuit.setText("Away Stats**");
        field.setImageResource(R.drawable.clear_field);
        fdl.setImageResource(R.drawable.clear_field);
        ball.setImageResource(R.drawable.clear_field);
        teamStats.setText("qb " + homeTeam.getQBS() + "qb " + awayTeam.getQBS() + "\nk " + homeTeam.getKS());

    }

    private void teamStatsView(Team thisTeam){
        playDetails.setText(thisTeam.getTeamName());
        passSave.setText("More Stats");
        runQuit.setText("Team Ratings");
        possession.setText(" ");
        teamStatsView++;
        field.setImageResource(R.drawable.clear_field);
        fdl.setImageResource(R.drawable.clear_field);
        ball.setImageResource(R.drawable.clear_field);
        if (!statsView)
            teamStatsView = 0;
        statsView = true;
        switch (teamStatsView % 6){
            case 0:
                teamStats.setText(thisTeam.getQB().getPassStats());
                break;
            case 1:
                teamStats.setText(thisTeam.getRB().getRushStats());
                break;
            case 2:
                teamStats.setText(thisTeam.getWR1().getReceivingStats());
                break;
            case 3:
                teamStats.setText(thisTeam.getWR2().getReceivingStats());
                break;
            case 4:
                teamStats.setText(thisTeam.getTE().getReceivingStats());
                break;
            case 5:
                teamStats.setText(thisTeam.getK().getKickStats());
                break;
            default:
                break;
        }
    }

    private void offenseView() {
        teamStatsView = 0;
        teamStats.setText(" ");
        possession.setText("Offense");
        gameStatsView();
        if( specialTeamsPlay ){
            pauseContinue.setText("Pause");
            simEnd.setText("Run / Pass");
            passSave.setText("Field Goal");
            runQuit.setText("Punt");
        }
        else{
            pauseContinue.setText("Pause");
            simEnd.setText("Special Teams");
            passSave.setText("Pass");
            runQuit.setText("Run");
        }
    }

    private void gameStatsView(){
        switch (gameQuarter){
            case 1:
                quarter.setText("Quarter\n" + Integer.toString(gameQuarter) + "st");
                break;
            case 2:
                quarter.setText("Quarter\n" + Integer.toString(gameQuarter) + "nd");
                break;
            case 3:
                quarter.setText("Quarter\n" + Integer.toString(gameQuarter) + "rd");
                break;
            case 4:
                quarter.setText("Quarter\n" + Integer.toString(gameQuarter) + "th");
                break;
            default:
                break;

        }

        //time view
        String minutes = Integer.toString(gameTime/60);
        String seconds;
        if( (gameTime%60) < 10 )
            seconds = "0" + (gameTime%60);
        else
            seconds = Integer.toString(gameTime%60);
        time.setText("Time\n" + minutes + ":" + seconds);

        //down details
        String detailString = Integer.toString(driveDown) + " & ";
        if(fieldPosition < yardsForFirst)
            detailString += "Goal";
        else
            detailString += Integer.toString(yardsForFirst);
        if(fieldPosition < 51)
            detailString =  detailString + " at the " + fieldPosition;
        else
            detailString =  detailString + " at the " + (100 - fieldPosition);
        downDetails.setText(detailString);


        playDetails.setText(showPlayDetails);

        homeTeamScore.setText(Integer.toString(userScore));
        awayTeamScore.setText(Integer.toString(aiScore));

        teamStats.setText(" ");
    }

    private void defenseView(){
        possession.setText("Defense");
        gameStatsView();
        if( driveDown == 4 ){
            pauseContinue.setText("Pause");
            simEnd.setText("Continue");
            passSave.setText("Continue");
            runQuit.setText("Continue");
        }
        else{
            pauseContinue.setText("Pause");
            simEnd.setText("Play Safe");
            passSave.setText("Stop Pass");
            runQuit.setText("Stop Run");
        }
    }

    private void endView(){
        homeTeamName.setText(homeTeam.getTeamName());
        awayTeamName.setText(awayTeam.getTeamName());
        pauseContinue.setText("Quit");
        simEnd.setText("Replay");
        passSave.setText("Home Stats**");
        runQuit.setText("Away Stats**");

        quarter.setText(" ");
        time.setText(" ");
    }

    /*
     *
     *Game Functions
     *
     */
    private void initializeGame(){
        gameOver = false;
        twoMinWarn = false;
        gameTime = (60 * 15);//15min*60sec
        timeOffClock = 0;
        gameQuarter = 1;
        userScore = 0;
        aiScore = 0;
        offensePossesion = ran.nextInt(2) + 1;
        System.out.print("The ");
        if( offensePossesion == 1 ){
            showPlayDetails = "Home Team has Received the Kick";
            halfTimePossesion = 2;
        }
        else{
            showPlayDetails = "Away Team has Received the Kick";
            halfTimePossesion = 1;
        }
        System.out.println(" has one the coin toss and will start on offense.");
        driveDown = 1;
        yardsForFirst = 10;
        fieldPosition = 80;
        ballMovement = 0;
        turnover = false;
        userTO = 0;
        aiTO = 0;
        userUrgency = 0;
        aiUrgency = 0;
        userMomentum = 0;
        aiMomentum = 0;
        simEntireGame = false;
        simToEnd = false;
        offensePlayBonus = 0;
        defensePlayBonus = 0;

        //view indicators
        specialTeamsPlay = false;
        pauseGame = false;
        statsView = false;
        toastMessage = "";
    }

    private void updateGame(){

        if(offensePossesion == 1)
            showPlayDetails = ("Home Team " + showPlayDetails);
        else
            showPlayDetails = ("Away Team " + showPlayDetails);
        
        gameTime -= timeOffClock;
        fieldPosition -= ballMovement;
        yardsForFirst -= ballMovement;
        driveDown++;

        // First Down
        if( yardsForFirst < 1 ){
            yardsForFirst = 10;
            driveDown = 1;
        }

        //safety
        if( fieldPosition > 99 ){
            if(!toastMessage.equals(""))
                toastMessage += "\n";
            toastMessage += "SAFETY";
            turnover = true;
            fieldPosition = 20;
            if( offensePossesion == 1 )
                aiScore += 2;
            else
                userScore += 2;
        }

        // Touchdown
        if( fieldPosition < 1 ){
            if(!toastMessage.equals(""))
                toastMessage += "\n";
            toastMessage += "TOUCHDOWN";
            turnover = true;
            fieldPosition = 20;
            if( offensePossesion == 1 )
                userScore += 7;
            else
                aiScore += 7;
        }

        //game time updates
        if( gameTime <= 0 ){
            gameQuarter++;
            if( gameQuarter > 4 ){
                gameOver = true;
                gameTime = 0;
            }
            else{
                //time reset for new quarter
                gameTime = 15 * 60;//15min*60sec
                //halftime update
                if( gameQuarter == 3 ){
                    if(!toastMessage.equals(""))
                        toastMessage += "\n";
                    toastMessage += "HALFTIME";
                    offensePossesion = halfTimePossesion;
                    turnover = false;
                    driveDown = 1;
                    twoMinWarn = false;
                    fieldPosition = 80;
                    driveDown = 1;
                    yardsForFirst = 10;
                }
            }
        }
        if( (gameQuarter % 2) == 0 && gameTime <= 120 && twoMinWarn == false ){
            twoMinWarn = true;
            gameTime = 120;
            if(!toastMessage.equals(""))
                toastMessage += "\n";
            toastMessage += "TWO MINUTE WARNING";
        }

        // Turnover
        if( turnover || driveDown > 4 ){
            if(!toastMessage.equals(""))
                toastMessage += "\n";
            toastMessage += "BALL SWITCHED SIDES";
            if( offensePossesion == 1 )
                offensePossesion = 2;
            else
                offensePossesion = 1;

            driveDown = 1;
            yardsForFirst = 10;
            fieldPosition = 100 - fieldPosition;
            specialTeamsPlay = false;
        }

        showToast();

        //reset game Keys
        turnover = false;
        timeOffClock = 0;
        ballMovement = 0;
        offensePlayBonus = 0;
        defensePlayBonus = 0;

        return;
    }

    private void showToast() {
        //Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, 0);
        if(toastMessage.equals(""))
            return;
        Toast toast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();
        toastMessage = "";
    }

    private int matchup(int offense, int defense){
        //critO & critD are thresholds
        //randO & randD are die rolls
        //range: crit(1, 33 + (o - d)), hit(34 + (o-d), (34 + (o-d)) + 32), miss(crit + 33, 99)
        offense += offensePlayBonus;
        defense += defensePlayBonus;
        int critO = (25 + ((offense - defense) * 2 / 2));
        int critD = (15 + ((defense - offense) * 3 / 2));
        int randO = (ran.nextInt(100) + 1), randD = (ran.nextInt(100) + 1);
        int outcome = 0;//offWin(1), stalemate(0), defWin(-1)



        if( randO <= critO )
            outcome++;
        if( randO > 95 )
            outcome--;
        if( randD <= critD )
            outcome--;
        if( randD > 85 )
            outcome++;

        if( outcome > 1 )
            outcome--;
        if( outcome < -1 )
            outcome++;

        return outcome;
    }

    private void punt(){
        showPlayDetails = "has punted the ball.";
        turnover = true;
        timeOffClock = ran.nextInt(4) + 4;
        fieldPosition -= (35 + ran.nextInt(26));
        if( fieldPosition < 1 )
            fieldPosition = 20;
    }

    private int fgAttempt(Team offense, Team defense){
        if(fieldPosition > 45){
            Toast.makeText(this, "OUT OF FIELD GOAL RANGE", Toast.LENGTH_SHORT).show();
            return -1;
        }

        offense.getK().incKickAttempt();
        turnover = true;
        ballMovement = 0;

        //play time run off
        timeOffClock = ran.nextInt(4) + 4;

        if( matchup(offense.getOLS(), defense.getDLS()) == -1 ){
            if( (defense.getDLS() / 10) > ran.nextInt(100) ){
                showPlayDetails = ("had their field goal attempt blocked.");
                toastMessage += "BLOCKED FIELD GOAL\n";
                ballMovement = 0 - ran.nextInt(11);
                return 2;
            }
        }

        if( (offense.getKS() - (fieldPosition + 17) / 10) > ran.nextInt(offense.getKS() + (fieldPosition + 17) / 5) ){
            offense.getK().incKickMade();
            fieldPosition = 20;
            showPlayDetails = ("has scored a field goal.");
            toastMessage += "FIELD GOAL\n";
            if( offensePossesion == 1)
                userScore += 3;
            else
                aiScore += 3;
            return 1;
        }
        else{
            toastMessage += "MISSED FIELD GOAL";
            showPlayDetails = ("missed a field goal.");
        }

        return 0;

    }

    private int pass(Team offense, Team defense){
        int outcome = 0;//(-3)int, (-2)sack/fumble, (-1)sack, (0)incomplete, (1)short, (2)medium, (3)scramble
        int ODM;//passrush-(olineV.dline)
        int option = 0;//who receives the ball (1)WR1 (2)TE (3)RB (4)WR2

        Player primaryWR;
        Player secondaryWR;
        Player primaryCB;
        Player secondaryCB;
        int target;

        //set primary receiver
        if(ran.nextInt(2) == 0){
            target = 1;
            primaryWR = offense.getWR1();
            secondaryWR = offense.getWR2();
            primaryCB = defense.getCB1();
            secondaryCB = offense.getCB2();
        }
        else{
            target = 2;
            primaryWR = offense.getWR2();
            secondaryWR = offense.getWR1();
            primaryCB = defense.getCB2();
            secondaryCB = offense.getCB1();
        }

        //play simulation
        switch( ODM = matchup(offense.getOLS(1), defense.getDLS(1)) ){
            case -1:switch( matchup(primaryWR.getStat(1), primaryCB.getStat(1)) ){
                case -1:if( (outcome = matchup(offense.getQBS(3), defense.getDLS(2)) - 1) == 0 ){
                    switch( matchup(offense.getQBS(1), defense.getLBS(1)) ){
                        case -1:outcome = -1;
                            break;//shutdown corner
                        case  0:outcome = 3;//start scramble
                            if( matchup(primaryWR.getStat(1), primaryCB.getStat(1)) > -1 )
                                if( matchup(offense.getQBS(2), primaryCB.getStat(1)) > -1 ){
                                    option = 1;
                                    outcome = matchup(primaryWR.getStat(3), primaryCB.getStat(2)) + 1;
                                }
                            break;
                        case  1:outcome = 0;
                            if( matchup(offense.getTES(3), defense.getLBS(2)) > -1 ){
                                outcome++;
                                option = 2;
                            }
                            break;//wr release
                    }//end switch
                }//end if
                    break;//(-1)cb jams wr at line
                case  0:option = 1;
                    if( ( (outcome = matchup(offense.getQBS(1), primaryCB.getStat(1)) ) == -1) ){
                        outcome = 0;
                        if( matchup(primaryWR.getStat(2), primaryCB.getStat(1)) == -1 )
                            outcome = -3;
                    }
                    else if(outcome == 1){
                        outcome = matchup(primaryWR.getStat(2), primaryCB.getStat(1)) + 1;
                        option = 1;
                    }
                    else{
                        if( (outcome = matchup(offense.getQBS(3), defense.getDLS(2)) - 1) == 0 ){
                            switch( ( outcome = matchup(offense.getTES(3), defense.getLBS(2)) ) ){
                                case -1:outcome = 0;
                                    if( matchup(offense.getQBS(1), defense.getLBS(2)) == -1 )
                                        outcome = -3;
                                    break;//shutdown corner
                                case  0:outcome = 0;
                                    switch( matchup(offense.getQBS(1), defense.getLBS(2)) ){
                                        case -1:if( matchup(offense.getTES(3), defense.getLBS(2)) == -1 )
                                            outcome = -3;
                                            break;
                                        case  0:if( matchup(offense.getQBS(1), defense.getLBS(2)) == 1 )
                                            outcome = 3;
                                            break;
                                        case  1:if( matchup(offense.getTES(3), defense.getLBS(2)) > -1 ){
                                            outcome = 1;
                                            option = 2;
                                        }
                                            break;
                                    }
                                    break;//2nd option scenario
                                case  1:outcome = 0;
                                    if( matchup(offense.getTES(3), defense.getLBS(2)) > -1 ){
                                        outcome ++;
                                        option = 2;
                                    }
                                    break;//wr release
                            }//end switch
                        }//end if
                    }//end else
                    break;//(0)even matchup between wr/cb
                case  1:outcome = 1;
                    option = 1;
                    if( matchup(offense.getQBS(1), primaryCB.getStat(1)) == -1 )
                        outcome = 0;
                    else if( matchup(primaryWR.getStat(1), defense.getFSS(1)) == 1 )
                        outcome = 2;
                    break;
            }//end switch WRvCB
                break;//(-1)snap jumped
            case  0:if(  matchup(primaryWR.getStat(1), primaryCB.getStat(1))  == 1 ){
                outcome =  1 + matchup(offense.getQBS(1), primaryCB.getStat(1));
                ODM++;
                option = 1;
            }//open WR

                if( ODM == 0 ){
                    if( matchup(offense.getOLS(1), defense.getDLS(1)) > -1 ){
                        if( matchup(offense.getTES(3), defense.getLBS(2)) == 1 ){
                            outcome = 1 + matchup(offense.getQBS(1), defense.getLBS(2));
                            option = 2;
                        }
                    }
                    else{
                        if( matchup(offense.getRBS(1), defense.getLBS(2)) == 1 ){
                            outcome = 1;
                            option = 3;
                        }
                        else{
                            if( (outcome = matchup(offense.getQBS(3), defense.getDLS(2)) - 1) == 0 )
                                outcome = 3;
                        }
                    }
                }//end if( ODM == 0 .::. 2nd look)
                break;//end case 0
            case  1:
                if( matchup(primaryWR.getStat(1), primaryCB.getStat(1)) == 1 ){
                    outcome = matchup(offense.getQBS(1), primaryCB.getStat(1)) + 1;
                    ODM++;
                    option = 1;
                }
                else{
                    if( matchup(offense.getQBS(1), primaryCB.getStat(1)) == -1 ){
                        option = 1;
                        outcome = matchup(primaryWR.getStat(2), primaryCB.getStat(1));
                        if( outcome == -1 ){
                            outcome = -3;
                            ODM++;
                        }
                    }
                }

                //second look
                if( ODM == 1 ){
                    option = 2;
                    if( matchup(offense.getTES(3), defense.getLBS(2)) == 1 ){
                        outcome = matchup(offense.getQBS(2), defense.getLBS(2)) + 1;
                        ODM++;
                    }
                }
                else{
                    if( matchup(offense.getQBS(1), defense.getLBS(2)) == -1 ){
                        outcome = matchup(offense.getTES(3), defense.getLBS(2));
                        if( outcome == -1 ){
                            outcome = -3;
                            ODM++;
                        }
                    }
                }//end 2nd look

                //2nd chance sack
                int ODM2 = matchup(offense.getOLS(1), defense.getDLS(1)) + 1;
                if( ODM == 1 && ODM2 ==0 ){
                    if( matchup(offense.getQBS(3), defense.getDLS(3)) == - 1 ){
                        outcome = matchup(offense.getQBS(3), defense.getDLS(1)) - 1;
                        ODM++;
                    }
                }//end 2nd sack

                //3rd look
                if( ODM == 1 ){
                    option = 3;
                    if( matchup(offense.getRBS(1), defense.getLBS(2)) == 1 ){
                        outcome = matchup(offense.getQBS(2), defense.getLBS(2)) + 1;
                        ODM++;
                    }
                    else{
                        if( matchup(offense.getQBS(1), defense.getLBS(2)) == -1 ){
                            outcome = matchup(offense.getRBS(1), defense.getLBS(2));
                            if( outcome == -1 ){
                                outcome = -3;
                                ODM++;
                            }
                        }
                    }
                }

                //3rd sack attempt
                ODM2 += matchup(offense.getOLS(1), defense.getDLS(1));
                if( ODM == 1 && ODM2 < 1 ){
                    if( ODM2 == -1 ){
                        outcome = matchup(offense.getQBS(3), defense.getDLS(2));
                        ODM++;
                    }
                    else{
                        outcome = 3;
                        ODM++;
                    }
                }

                //final option(2nd receiver)
                if( ODM == 1 ){
                    option = 4;
                    switch( matchup(secondaryWR.getStat(1), secondaryCB.getStat(1)) ){
                        case -1:outcome = 3;
                            break;//scramble
                        case  0:if( ( outcome = matchup(offense.getQBS(1), secondaryCB.getStat(1)) ) == -1)
                            outcome = -3;
                        else
                            outcome = 3;
                            break;
                        case  1:outcome = matchup(offense.getQBS(2), secondaryCB.getStat(1)) + 1;
                            break;
                    }
                }//end final option
                break;//end play (OL>DL)
        }//end playSim

        //check catch
        if(outcome == 1 || outcome == 2){
            switch(option){
                case 1:
                    if( matchup(primaryWR.getStat(2), primaryWR.getStat(2)) < 0 ){
                        //dropped catch
                        if(target == 1)
                            offense.getWR1().incPassDropped();
                        else
                            offense.getWR2().incPassDropped();
                        outcome = 0;
                    }
                    break;
                case 2:
                    if( matchup(offense.getTE().getStat(1), offense.getTE().getStat(1)) < 0 ){
                        //dropped catch
                        offense.getTE().incPassDropped();
                        outcome = 0;
                    }
                    break;
                case 3:
                    if( matchup(offense.getRB().getStat(3), offense.getRB().getStat(3)) < 0 ){
                        //dropped catch
                        offense.getRB().incPassDropped();
                        outcome = 0;
                    }
                    break;
                case 4:
                    if( matchup(secondaryWR.getStat(2), secondaryWR.getStat(2)) < 0 ){
                        //dropped catch
                        if(target == 2)
                            offense.getWR1().incPassDropped();
                        else
                            offense.getWR2().incPassDropped();
                        outcome = 0;
                    }
                    break;
                default:
                    Toast.makeText(this, "Check Catch Error", Toast.LENGTH_SHORT).show();
                    break;
            }

        }


        //decides yardage and time simulation
        switch(outcome){
            case -3://interception
                ballMovement = ran.nextInt(10) + 3 - bigPlay( defense.getCBS(target, 3), offense.getWRS(target, 3) );
                turnover = true;
                timeOffClock = ran.nextInt(5) + 3;
                showPlayDetails = ("has thrown an interception.");
                break;
            case -2://sack and qb fumble
                ballMovement = (ran.nextInt(6) - 6);
                boolean playOver = false;
                timeOffClock = ran.nextInt(5) + 5;
                while( playOver == false ){
                    switch( matchup(offense.getRBS(1), defense.getLBS(1)) ){
                        case -1:turnover = true;
                            playOver = true;
                            break;
                        case  0:switch( matchup(offense.getOLS(3), defense.getDLS(3)) ){
                            case -1:turnover = true;
                                playOver = true;
                                break;
                            case  0:switch( matchup(offense.getRBS(1), defense.getFSS(3)) ){
                                case -1:turnover = true;
                                    playOver = true;
                                    break;
                                case  0:switch( matchup(offense.getWRS(ran.nextInt(2) + 1, 3), defense.getCBS(ran.nextInt(2) + 1, 3)) ){
                                    case -1:turnover = true;
                                        playOver = true;
                                        break;
                                    case  0://sim thorugh again for ball possesion
                                        timeOffClock = ran.nextInt(2) + 1;
                                        break;
                                    case  1:playOver = true;
                                        break;
                                }
                                    break;
                                case  1:playOver = true;
                                    break;
                            }
                                break;
                            case  1:playOver = true;
                        }
                            break;
                        case  1:playOver = true;
                            break;
                    }
                }
                if ( turnover == true )
                    showPlayDetails = ("has been sacked for " + ballMovement + " yards and fumbled the ball.\nThe ball was recovered by the defense.");
                if( turnover == false ){
                    timeOffClock += timeManagement();
                    showPlayDetails = ("has been sacked for " + ballMovement + " yards and fumbled the ball.\nThe ball was recovered by the offense.");
                }
                break;
            case -1://sack
                ballMovement = (ran.nextInt(6) - 6);
                timeOffClock = ran.nextInt(5) + 2 + timeManagement();
                showPlayDetails = ("has been sacked for " + ballMovement + " yards.");
                break;
            case  0://incomplete pass
                timeOffClock = ran.nextInt(5) + 3;
                showPlayDetails = ("threw an incomplete pass.");
                break;
            case  1://short pass
                timeOffClock = ran.nextInt(5) + 2 + timeManagement();
                switch( option ){
                    case 1://wr catch
                        ballMovement += ( ran.nextInt(5) + 3 );
                        if(target == 1){
                            if(matchup(offense.getWR1().getStat(3), defense.getCB1().getStat(2)) == 1)
                                ballMovement += bigPlay(offense.getWR1().getStat(3), defense.getFSS(3));
                        }
                        else{
                            if(matchup(offense.getWR2().getStat(3), defense.getCB2().getStat(2)) == 1)
                                ballMovement += bigPlay(offense.getWR2().getStat(3), defense.getFSS(3));
                        }
                        break;
                    case 2://te catch
                        ballMovement += ( ran.nextInt(5) + 3 );
                        if(matchup(offense.getTES(3), defense.getLBS(1)) == 1)
                            ballMovement += bigPlay(offense.getTES(3), defense.getFSS(3));
                        break;
                    case 3://rb catch
                        ballMovement += ( ran.nextInt(7) - 2 );
                        if(matchup(offense.getRBS(2), defense.getLBS(1)) == 1)
                            ballMovement += bigPlay(offense.getRBS(2), defense.getFSS(3));
                        break;
                    case 4:
                        ballMovement += ( ran.nextInt(5) + 3 );
                        if(target == 2){
                            if(matchup(offense.getWR1().getStat(3), defense.getCB1().getStat(2)) == 1)
                                ballMovement += bigPlay(offense.getWR1().getStat(3), defense.getFSS(3));
                        }
                        else{
                            if(matchup(offense.getWR2().getStat(3), defense.getCB2().getStat(2)) == 1)
                                ballMovement += bigPlay(offense.getWR2().getStat(3), defense.getFSS(3));
                        }
                        break;
                    default://error shouldn't reach here
                        break;
                }
                if( (fieldPosition - ballMovement) < 1 )
                    ballMovement = fieldPosition;
                if( turnover == true )
                    showPlayDetails = ("passed for a gain of " + ballMovement + " yards. " + showPlayDetails);
                else
                    showPlayDetails = ("passed for a gain of " + ballMovement + " yards.");
                break;
            case  2://medium pass
                timeOffClock = ran.nextInt(5) + 3 + timeManagement();
                switch( option ){
                    case 1://wr catch
                        if(target == 1){
                            ballMovement += ( ran.nextInt(5) + 15 );
                            ballMovement += bigPlay(offense.getWR1().getStat(3), defense.getFSS(3));
                        }
                        else{
                            ballMovement += ( ran.nextInt(5) + 15 );
                            ballMovement += bigPlay(offense.getWR2().getStat(3), defense.getFSS(3));
                        }
                        break;
                    case 2://te catch
                        ballMovement += ( ran.nextInt(5) + 10 );
                        ballMovement += bigPlay(offense.getTES(3), defense.getFSS(3));
                        break;
                    case 3://rb catch
                        ballMovement += ( ran.nextInt(5) + 5 );
                        ballMovement += bigPlay(offense.getRBS(2), defense.getFSS(3));
                        break;
                    case 4:
                        if(target == 2){
                            ballMovement += ( ran.nextInt(5) + 15 );
                            ballMovement += bigPlay(offense.getWR1().getStat(3), defense.getFSS(3));
                        }
                        else{
                            ballMovement += ( ran.nextInt(5) + 15 );
                            ballMovement += bigPlay(offense.getWR2().getStat(3), defense.getFSS(3));
                        }
                        break;
                    default://error shouldn't reach here
                        break;
                }
                if( (fieldPosition - ballMovement) < 1 )
                    ballMovement = fieldPosition;
                if( turnover == true )
                    showPlayDetails = ("passed for a gain of " + ballMovement + " yards. " + showPlayDetails);
                else
                    showPlayDetails = ("passed for a gain of " + ballMovement + " yards. ");
                break;
            case  3://scramble
                timeOffClock = ran.nextInt(5) + 5 + timeManagement();
                ballMovement = (ran.nextInt(4) + 1);
                if( matchup(offense.getQBS(3), defense.getLBS(1)) == 1 )
                    ballMovement += ran.nextInt(3) + 1 + bigPlay(offense.getQBS(), defense.getFSS());
                if( (fieldPosition - ballMovement) < 1 )
                    ballMovement = fieldPosition;
                if( turnover == true )
                    showPlayDetails = ("scrambled for a gain of " + ballMovement + " yards. " + showPlayDetails);
                else
                    showPlayDetails = ("scrambled for a gain of " + ballMovement + " yards. ");
                break;
            default://error shouldn't reach here
                break;
        }

        passStatUpdate(offense, outcome, option, target);
        return outcome;

    }//end pass()

    private void passStatUpdate(Team offense, int outcome, int option, int target){
        if(aiTests)
            return;

        //(-3)int, (-2)sack/fumble, (-1)sack, (0)incomplete, (1)short, (2)medium, (3)scramble

        if( outcome == 3 ){
            //scramble
            offense.getQB().incRushAttempts();
            offense.getQB().incRushYards(ballMovement);
            if( fieldPosition - ballMovement < 1 )//td
                offense.getQB().incRushTD();
        }
        else if( outcome == -2 || outcome == -1 ){
            //sack
            offense.getQB().incPassSacked();
            offense.getQB().incRushAttempts();
            offense.getQB().incRushYards(ballMovement);
            if( outcome == -2 )
                offense.getQB().incRushFumbles();
        }
        else{
            //pass
            offense.getQB().incPassAttempts();
            if( outcome == -3 )//interception
                offense.getQB().incPassInt();
            else if( outcome > 0 ){
                //completed pass
                offense.getQB().incPassCompletions();
                offense.getQB().incPassYards(ballMovement);
                if( fieldPosition - ballMovement < 1 )//td
                    offense.getQB().incPassTD();
                switch( option ){
                    case 1:
                        if( target == 1 ){
                            offense.getWR1().incPassCaught();
                            offense.getWR1().incReceivingYards(ballMovement);
                            if( fieldPosition - ballMovement < 1 )//td
                                offense.getWR1().incReceivingTD();
                        }
                        else{
                            offense.getWR2().incPassCaught();
                            offense.getWR2().incReceivingYards(ballMovement);
                            if( fieldPosition - ballMovement < 1 )//td
                                offense.getWR2().incReceivingTD();
                        }
                        break;
                    case 2:
                        offense.getTE().incPassCaught();
                        offense.getTE().incReceivingYards(ballMovement);
                        if( fieldPosition - ballMovement < 1 )//td
                            offense.getTE().incReceivingTD();
                        break;
                    case 3:
                        offense.getRB().incPassCaught();
                        offense.getRB().incReceivingYards(ballMovement);
                        if( fieldPosition - ballMovement < 1 )//td
                            offense.getRB().incReceivingTD();
                        break;
                    case 4:
                        if( target == 2 ){
                            offense.getWR1().incPassCaught();
                            offense.getWR1().incReceivingYards(ballMovement);
                            if( fieldPosition - ballMovement < 1 )//td
                                offense.getWR1().incReceivingTD();
                        }
                        else{
                            offense.getWR2().incPassCaught();
                            offense.getWR2().incReceivingYards(ballMovement);
                            if( fieldPosition - ballMovement < 1 )//td
                                offense.getWR2().incReceivingTD();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private int run(Team offense, Team defense){
        int outcome = 0;//fumble(-2), lossOfYards(-1), short(0), medium(1), long(2)
        int yards = 0;
        int ODM = 0;//oLine vs dLine matchup

        //lineman matchup
        switch( ODM = matchup(offense.getOLS(2), defense.getDLS(1)) )
        {
            case -1:if( ( outcome = matchup(offense.getRBS(2), defense.getDLS(2)) ) < 1 )
                if( matchup(offense.getRBS(1), defense.getDLS(2)) == -1 )
                    outcome--;
                break;//DL beats OL and hits RB behind the line
            case  0:if( ( outcome = matchup(offense.getRBS(1), defense.getDLS(2)) ) == 1 )
                ODM++;//RB beats DL
                break;//DL&OL stalemate
            case  1:
                break;//OL beats DL
        }
        //RB and LB matchup
        if( ODM == 1 )
        {
            switch( outcome = matchup(offense.getRBS(1), defense.getLBS(2)) )
            {
                case -1:if( matchup(offense.getRBS(2), defense.getLBS(1)) == -1 )//bigHit
                    if( matchup(offense.getRBS(1), defense.getLBS(1)) == -1 )//fumble
                        outcome --;
                    break;//LB big play
                case  0:outcome = matchup(offense.getRBS(2), defense.getLBS(1));
                    if( outcome > 0 )
                        outcome = 0;
                    break;//even
                case  1:if( matchup(offense.getRBS(1), defense.getLBS(2)) == 1 )
                    outcome++;
                    break;//RB beats LB
            }
        }//end run outcome simulation


        //decides yardage and time spent on play
        switch(outcome){
            case -2://fumble
                boolean over = false;//accounts for chance for recovery on either offense or defense
                ballMovement += ran.nextInt(5) - 2;
                timeOffClock += ran.nextInt(7) + 2;
                while( over == false ){//case (-1) turnover (0)keep simming fumble recovery (1)offense maintains possesion
                    switch( matchup(offense.getRBS(1), defense.getDLS(3)) ){
                        case -1:turnover = true;
                            over = true;
                            break;
                        case  0:switch( matchup(offense.getOLS(3), defense.getDLS(3)) ){
                            case -1:turnover = true;
                                if( (fieldPosition - ballMovement) < 1 )
                                    ballMovement = fieldPosition;
                                over = true;
                                break;
                            case  0:switch( matchup(offense.getRBS(1), defense.getFSS(3)) ){
                                case -1:turnover = true;
                                    over = true;
                                    break;
                                case  0:switch( matchup(offense.getWRS(ran.nextInt(2)+1, 3), defense.getCBS(ran.nextInt(2)+1, 3)) ){
                                    case -1:turnover = true;
                                        over = true;
                                        break;
                                    case  0://starts loop again to sim recovery
                                        ballMovement += ran.nextInt(2) + 1;
                                        break;
                                    case  1:over = true;
                                        break;
                                }
                                    break;
                                case  1:over = true;
                                    break;
                            }
                                break;
                            case  1:over = true;
                                break;
                        }
                            break;
                        case  1:over = true;
                            break;
                    }
                }
                if( turnover == false ){
                    timeOffClock += timeManagement();
                    showPlayDetails = ("ran for " + ballMovement + " yards and fumbled the ball.\nThe ball was recovered by the offense.");
                }
                if( turnover == true )
                    showPlayDetails = ("ran for " + ballMovement + " yards and fumbled the ball.\nThe ball was recovered by the defense.");
                break;
            case -1:ballMovement += ran.nextInt(6) - 3;
                timeOffClock += ran.nextInt(2) + 1 + timeManagement();
                if( (fieldPosition - ballMovement) < 1 )
                    ballMovement = fieldPosition;
                showPlayDetails = ("ran the ball for " + ballMovement + " yards.");
                break;
            case  0:ballMovement += ran.nextInt(4)+1;
                timeOffClock += ran.nextInt(6) + 3 + timeManagement();
                if( (fieldPosition - ballMovement) < 1 )
                    ballMovement = fieldPosition;
                showPlayDetails = ("ran the ball for " + ballMovement + " yards.");
                break;
            case  1:ballMovement += ran.nextInt(10) + 4;
                timeOffClock += ran.nextInt(6) + 5 + timeManagement();
                if( (fieldPosition - ballMovement) < 1 )
                    ballMovement = fieldPosition;
                showPlayDetails = ("ran the ball for " + ballMovement + " yards.");
                break;
            case  2:ballMovement += ( ran.nextInt(6) + 11 + bigPlay(offense.getRBS(2), defense.getFSS(3)) );
                timeOffClock += ran.nextInt(6) + 5 + timeManagement();
                if( (fieldPosition - ballMovement) < 1 )
                    ballMovement = fieldPosition;
                if ( turnover == true )
                    showPlayDetails = ("ran the ball for " + ballMovement + " yards. " + showPlayDetails);
                else
                    showPlayDetails = ("ran the ball for " + ballMovement + " yards. ");
                break;
            default:
                break;
        }

        runStatUpdate(outcome, offense);
        return outcome;
    }

    private void runStatUpdate(int outcome, Team offense){
        if(aiTests)
            return;

        //fumble(-2), lossOfYards(-1), short(0), medium(1), long(2)
        offense.getRB().incRushAttempts();
        offense.getRB().incRushYards(ballMovement);
        if( outcome == -2 )
            offense.getRB().incRushFumbles();
        if( fieldPosition - ballMovement < 1 )
            offense.getRB().incRushTD();
    }

    //change parameters to (player offense, player defense)
    private int bigPlay(int offenseStat, int defenseStat){
        int extraYards = ran.nextInt(3) + 1;
        int extraTime = 1;
        boolean playOver = false;

        if( fieldPosition < (ballMovement + extraYards) )
            playOver = true;

        while( playOver == false ){
            if( fieldPosition < (ballMovement + extraYards) )
                playOver = true;
            extraTime++;
            switch( matchup(offenseStat, defenseStat) ){
                case -1://big hit
                    playOver = true;
                    if( matchup(offenseStat, defenseStat) == -1 )//forced fumble
                        if( matchup(offenseStat, defenseStat) == -1 )//defense recovers
                            turnover = true;
                    break;
                case  0://tackle made
                    playOver = true;
                    if( matchup(offenseStat, defenseStat) == 1 )//falls forward after tackle
                        extraYards += ran.nextInt(2) + 1;
                    break;
                case  1://runner outrunning defender
                    extraYards += ran.nextInt(3);
                    while( matchup(offenseStat, defenseStat) == 1 ){
                        extraYards += ran.nextInt(2) + 2;
                        extraTime++;
                    }
                    break;
            }

        }

        if( turnover == true )
            showPlayDetails = ("\nThe ball was fumbled at the end of play and recovered by the defense.");

        timeOffClock += extraTime;
        return extraYards;
    }

    private int timeManagement(){
        if( turnover == true )
            return 0;
        else if(twoMinWarn)
            return 4  + ran.nextInt(10);
        else
            return ran.nextInt(11) + 25;
    }

    private void replayGame(){
        gameOver = false;
        twoMinWarn = false;
        gameTime = (60 * 15);//15min*60sec
        timeOffClock = 0;
        gameQuarter = 1;
        userScore = 0;
        aiScore = 0;
        offensePossesion = ran.nextInt(2) + 1;
        if( offensePossesion == 1 ){
            showPlayDetails = "Home Team has Received the Kick";
            halfTimePossesion = 2;
        }
        else{
            showPlayDetails = "Away Team has Received the Kick";
            halfTimePossesion = 1;
        }
        driveDown = 1;
        yardsForFirst = 10;
        fieldPosition = 80;
        ballMovement = 0;
        turnover = false;
        userTO = 0;
        aiTO = 0;
        userUrgency = 0;
        aiUrgency = 0;
        userMomentum = 0;
        aiMomentum = 0;
        simEntireGame = false;
        simToEnd = false;
        offensePlayBonus = 0;
        defensePlayBonus = 0;
        specialTeamsPlay = false;
        pauseGame = false;
    }

    private void quitGame(){
        if(seasonMode)
            returnToSeason();
        else {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
        }
    }

    private void returnToSeason(){
        Intent season = new Intent(this, SeasonMode.class);

        season.putExtra("league", league);
    }

    /*
    private void aiPlay(int guess){
        //random for initial testing purposes
        if( driveDown == 4){
            if( fieldPosition < 44 )
                fgAttempt(awayTeam, homeTeam);
            else
                punt();
        }
        else{
            int play = ran.nextInt(2);
            if(guess != 2)//2 = play it safe
                guessDefense(play, guess);
            if( play == 0 )
                run(awayTeam, homeTeam);
            else
                pass(awayTeam, homeTeam);
        }
        updateGame();

    }
    */

    //ai algorithm for choosing play
    private void aiPlay(int guess){

        if( (gameQuarter % 2) == 0 && gameTime < 25 ) {
            if (gameQuarter == 4) {
                if ((aiScore + 3) >= userScore) {
                    fgAttempt(awayTeam, homeTeam);
                    return;
                }
            }
            else if (fieldPosition < 44) {
                fgAttempt(awayTeam, homeTeam);
                return;
            }
        }

        //4th down decisions
        if( driveDown == 4){
            if( gameQuarter == 4 && gameTime < (4 * 60) && aiScore < userScore){
                if( (aiScore + 3) >= userScore ) {
                    if (fieldPosition < 44)
                        fgAttempt(awayTeam, homeTeam);
                    return;
                }
                else{
                    //continue with pass or run
                }
            }
            else{
                if( fieldPosition < 44 )
                    fgAttempt(awayTeam, homeTeam);
                else
                    punt();
                return;
            }
        }

        aiTests = true;
        int[] RunYardSim = new int[RunSuccessCount];
        int[] PassYardSim = new int[PassSuccessCount];


        //AI Offense
        //Simulate 1000 Run Plays and get an average result.
        int i = RunSuccessCount - 1;
        while (i > -1){
            run(awayTeam, homeTeam);
            if (turnover) {
                RunSuccessCount--;
            }
            else {
                RunYardSim[i] = ballMovement;
            }
            i--;
            turnover = false;
            timeOffClock = 0;
            ballMovement = 0;
        }
        int RunAverage = 0;
        i = 0;
        while (i < RunYardSim.length){
            RunAverage = RunAverage + RunYardSim[i];
            i++;
        }
        //System.out.println(RunAverage);
        RunAverage = RunAverage * RunSuccessCount / (RunYardSim.length * RunYardSim.length);

        //Simulate 1000 Pass Plays and get an average result.
        i = PassSuccessCount - 1;
        while (i > -1){
            pass(awayTeam, homeTeam);
            if (turnover) {
                PassSuccessCount--;
            }
            else {
                PassYardSim[i] = ballMovement;
            }
            i--;
            turnover = false;
            timeOffClock = 0;
            ballMovement = 0;
        }
        int PassAverage = 0;
        i = 0;
        while (i < RunYardSim.length){
            PassAverage = PassAverage + PassYardSim[i];
            i++;
        }
        //System.out.println(PassAverage);
        PassAverage = PassAverage * PassSuccessCount / (PassYardSim.length * PassYardSim.length);

        //reset keys
        PassSuccessCount = 1000;
        RunSuccessCount = 1000;
        aiTests = false;
        if( RunAverage < 0 && PassAverage >= 0){
            guessDefense(1, guess);
            pass(awayTeam, homeTeam);
        }
        else if (RunAverage >= 0 && PassAverage < 0){
            guessDefense(0, guess);
            run(awayTeam, homeTeam);
        }
        else if( RunAverage == PassAverage ){
            if( ran.nextInt(2) == 0 ) {
                guessDefense(1, guess);
                pass(awayTeam, homeTeam);
            }
            else {
                guessDefense(0, guess);
                run(awayTeam, homeTeam);
            }
        }
        else if (RunAverage < 0 && PassAverage < 0){
            if(RunAverage < PassAverage){
                guessDefense(1, guess);
                pass(awayTeam, homeTeam);
            }
            else {
                guessDefense(0, guess);
                run(awayTeam, homeTeam);
            }
        } else {

            if ( ran.nextInt(Math.max(2, RunAverage + PassAverage)) < RunAverage ) {
                guessDefense(0, guess);
                run(awayTeam, homeTeam);
            }
            else {
                guessDefense(1, guess);
                pass(awayTeam, homeTeam);
            }
        }
    }

    private void aiDefense(String userPlay){
        
    }

    private void guessDefense(int play, int guess){
        if( play == guess){
            offensePlayBonus = -(5 + ran.nextInt(11));
        }
        else{
            defensePlayBonus = -(5 + ran.nextInt(11));;
        }
    }

}
