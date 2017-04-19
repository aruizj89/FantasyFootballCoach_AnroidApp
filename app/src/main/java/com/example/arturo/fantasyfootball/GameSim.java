package com.example.arturo.fantasyfootball;

import java.util.Random;

/**
 * Created by Arturo on 7/14/2016.
 */
public class GameSim {

    public void simGame(Team home, Team away, int week){
        //initialize settings and teams
        initializeGame();
        homeTeam = home;
        awayTeam = away;

        //simulate game
        while( !gameOver ){
            if(offensePossesion == 1){
                aiPlayHome();
            }
            else{
                aiPlayAway();
            }
            updateGame();
        }

        //finalize game results
        if(userScore > aiScore){//homeTeam wins
            homeTeam.getSchedule().completeGame(week, 1, userScore, aiScore);
            awayTeam.getSchedule().completeGame(week, 2, aiScore, userScore);
        }
        else if(aiScore > userScore){//awayTeam wins
            homeTeam.getSchedule().completeGame(week, 2, userScore, aiScore);
            awayTeam.getSchedule().completeGame(week, 1, aiScore, userScore);
        }
        else{//draw
            homeTeam.getSchedule().completeGame(week, 3, userScore, aiScore);
            awayTeam.getSchedule().completeGame(week, 3, aiScore, userScore);
        }
        homeTeam.endGame(week);
        awayTeam.endGame(week);
    }


    //team variables
    private Team homeTeam;
    private Team awayTeam;

    //global game variables
    private Random ran = new Random();

    //important stuffs
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
    private boolean turnover;
    private int userTO, aiTO;//timeouts
    private int userUrgency, aiUrgency;//(-1)winning (0)tie (1)tieEnding (2)1TDGame (3)1TDGameEnding (4)>1TDGame
    private int userMomentum, aiMomentum;//momentum
    private String showPlayDetails;
    private int offensePlayBonus;
    private int defensePlayBonus;
    private boolean aiTests = false;
    private int RunSuccessCount = 1000;
    private int PassSuccessCount = 1000;

    private void initializeGame(){
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
        offensePlayBonus = 0;
        defensePlayBonus = 0;
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
            turnover = true;
            fieldPosition = 20;
            if( offensePossesion == 1 )
                aiScore += 2;
            else
                userScore += 2;
        }

        // Touchdown
        if( fieldPosition < 1 ){
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
        if( (gameQuarter % 2) == 0 && gameTime <= 120 && !twoMinWarn ){
            twoMinWarn = true;
            gameTime = 120;
        }

        // Turnover
        if( turnover || driveDown > 4 ) {
            if (offensePossesion == 1)
                offensePossesion = 2;
            else
                offensePossesion = 1;

            driveDown = 1;
            yardsForFirst = 10;
            fieldPosition = 100 - fieldPosition;
        }

        //reset game Keys
        turnover = false;
        timeOffClock = 0;
        ballMovement = 0;
        offensePlayBonus = 0;
        defensePlayBonus = 0;

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
                ballMovement = 0 - ran.nextInt(11);
                return 2;
            }
        }

        if( (offense.getKS() - (fieldPosition + 17) / 10) > ran.nextInt(offense.getKS() + (fieldPosition + 17) / 5) ){
            offense.getK().incKickMade();
            fieldPosition = 20;
            showPlayDetails = ("has scored a field goal.");
            if( offensePossesion == 1)
                userScore += 3;
            else
                aiScore += 3;
            return 1;
        }
        else{
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
                        case -1:
                            outcome = 3;
                            break;//scramble
                        case  0:
                            if( ( outcome = matchup(offense.getQBS(1), secondaryCB.getStat(1)) ) == -1)
                                outcome = -3;
                        else
                            outcome = 3;
                            break;
                        case  1:
                            outcome = matchup(offense.getQBS(2), secondaryCB.getStat(1)) + 1;
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
                while( !playOver ){
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
                if ( turnover )
                    showPlayDetails = ("has been sacked for " + ballMovement + " yards and fumbled the ball.\nThe ball was recovered by the defense.");
                if( !turnover ){
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
                if( turnover )
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
                if( turnover )
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
                if( turnover )
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
        int ODM;//oLine vs dLine matchup

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
                while( !over ){//case (-1) turnover (0)keep simming fumble recovery (1)offense maintains possesion
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
                if( !turnover ){
                    timeOffClock += timeManagement();
                    showPlayDetails = ("ran for " + ballMovement + " yards and fumbled the ball.\nThe ball was recovered by the offense.");
                }
                if( turnover )
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
                if ( turnover )
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

        while( !playOver ){
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

        if( turnover )
            showPlayDetails = ("\nThe ball was fumbled at the end of play and recovered by the defense.");

        timeOffClock += extraTime;
        return extraYards;
    }

    private int timeManagement(){
        if( turnover)
            return 0;
        else if(twoMinWarn)
            return 4  + ran.nextInt(10);
        else
            return ran.nextInt(11) + 25;
    }

    private void aiPlayHome(){

        if( (gameQuarter % 2) == 0 && gameTime < 25 ) {
            if (gameQuarter == 4) {
                if ((aiScore + 3) >= userScore) {
                    fgAttempt(homeTeam, awayTeam);
                    return;
                }
            }
            else if (fieldPosition < 44) {
                fgAttempt(homeTeam, awayTeam);
                return;
            }
        }

        //4th down decisions
        if( driveDown == 4){
            if( gameQuarter == 4 && gameTime < (4 * 60) && aiScore < userScore){
                if( (aiScore + 3) >= userScore ) {
                    if (fieldPosition < 44)
                        fgAttempt(homeTeam, awayTeam);
                    return;
                }
            }
            else{
                if( fieldPosition < 44 )
                    fgAttempt(homeTeam, awayTeam);
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
            run(homeTeam, awayTeam);
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
        RunAverage = RunAverage * RunSuccessCount / (RunYardSim.length * RunYardSim.length);

        //Simulate 1000 Pass Plays and get an average result.
        i = PassSuccessCount - 1;
        while (i > -1){
            pass(homeTeam, awayTeam);
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
        PassAverage = PassAverage * PassSuccessCount / (PassYardSim.length * PassYardSim.length);

        //reset keys
        PassSuccessCount = 1000;
        RunSuccessCount = 1000;
        aiTests = false;
        if( RunAverage < 0 && PassAverage >= 0){
            pass(homeTeam, awayTeam);
        }
        else if (RunAverage >= 0 && PassAverage < 0){
            run(homeTeam, awayTeam);
        }
        else if( RunAverage == PassAverage ){
            if( ran.nextInt(2) == 0 ) {
                pass(homeTeam, awayTeam);
            }
            else {
                run(homeTeam, awayTeam);
            }
        }
        else if (RunAverage < 0 && PassAverage < 0){
            if(RunAverage < PassAverage){
                pass(homeTeam, awayTeam);
            }
            else {
                run(homeTeam, awayTeam);
            }
        } else {

            if ( ran.nextInt(Math.max(2, RunAverage + PassAverage)) < RunAverage ) {
                run(homeTeam, awayTeam);
            }
            else {
                pass(homeTeam, awayTeam);
            }
        }
    }

    private void aiPlayAway(){

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
        PassAverage = PassAverage * PassSuccessCount / (PassYardSim.length * PassYardSim.length);

        //reset keys
        PassSuccessCount = 1000;
        RunSuccessCount = 1000;
        aiTests = false;
        if( RunAverage < 0 && PassAverage >= 0){
            pass(awayTeam, homeTeam);
        }
        else if (RunAverage >= 0 && PassAverage < 0){
            run(awayTeam, homeTeam);
        }
        else if( RunAverage == PassAverage ){
            if( ran.nextInt(2) == 0 ) {
                pass(awayTeam, homeTeam);
            }
            else {
                run(awayTeam, homeTeam);
            }
        }
        else if (RunAverage < 0 && PassAverage < 0){
            if(RunAverage < PassAverage){
                pass(awayTeam, homeTeam);
            }
            else {
                run(awayTeam, homeTeam);
            }
        } else {

            if ( ran.nextInt(Math.max(2, RunAverage + PassAverage)) < RunAverage ) {
                run(awayTeam, homeTeam);
            }
            else {
                pass(awayTeam, homeTeam);
            }
        }
    }

}
