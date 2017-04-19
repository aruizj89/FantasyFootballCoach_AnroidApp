package com.example.arturo.fantasyfootball;

import java.util.Random;

/**
 * Created by Arturo on 7/6/2016.
 */
public class ScheduleSim {
    private int homeTeam;
    private int awayTeam;
    private int week, games;
    private boolean scheduleCompleted;
    private League league;

    private Random numGen = new Random();

    public void createSchedule(League league){
        week = 0; games = 0;
        homeTeam = 0; awayTeam = 0;
        scheduleCompleted = false;
        this.league = league;
        startScheduling();
    }

    public void startScheduling(){
        while( !scheduleCompleted ){
            selectHomeTeam();
            selectAwayTeam();
            createMatch();
        }
    }

    private void selectHomeTeam() {
        homeTeam = numGen.nextInt(32);

        //checks to see if team is viable to be home team for the current week
        if( league.getTeam(homeTeam).getSchedule().gamesScheduled() == week )
            if( league.getTeam(homeTeam).getSchedule().getGames(true) > 0 )
                return;//exits out of recursive call if all cases are met

        selectHomeTeam();//recursive call to try new team
    }

    private void selectAwayTeam() {
        awayTeam = numGen.nextInt(32);

        //checks to see if team is viable to be away team for the current week
        if( awayTeam != homeTeam )
            if( league.getTeam(awayTeam).getSchedule().gamesScheduled() == week )
                if( league.getTeam(awayTeam).getSchedule().getGames(false) > 0 )
                    return;//exits out of recursive call if all cases are met

        selectAwayTeam();//recursive call to try new team
    }

    private void createMatch() {
        league.getTeam(homeTeam).getSchedule().addGame(league.getTeam(awayTeam), week, true);
        league.getTeam(awayTeam).getSchedule().addGame(league.getTeam(homeTeam), week, false);
        games++;
        if( games == 16 )
            finishWeek();
    }

    private void finishWeek() {
        week++;
        games = 0;
        if( week == 8 )
            scheduleCompleted = true;
    }

}