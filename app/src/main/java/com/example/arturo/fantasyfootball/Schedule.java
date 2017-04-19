package com.example.arturo.fantasyfootball;

import android.content.pm.LabeledIntent;
import android.widget.Toast;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Arturo on 7/5/2016.
 */
public class Schedule implements Serializable {
    private LinkedList<Team> opponents = new LinkedList<Team>();
    private LinkedList<Team> gamesPlayed = new LinkedList<Team>();

    //results[game][results]
    //results[0] :: 0 = not played; 1 = win; 2 = loss; 3 = draw;
    //results[1] :: team score
    //results[2] :: opponent score
    private int results[][];
    private int record[];// record[0] is win column; r[1] is loss column; r[2] is draw column
    private boolean homeField[];
    private int homeGames, awayGames;

    public Schedule(){
        opponents.clear();
        gamesPlayed.clear();
        results = new int[16][3];
        record = new int [3];
        homeField = new boolean[16];
        homeGames = 8;
        awayGames = 8;
    }

    public Team getNextGame(){
        return opponents.peekFirst();
    }

    public void completeGame(int week, int winLossDraw, int teamScore, int oppScore){
        results[week][0] = winLossDraw;
        results[week][1] = teamScore;
        results[week][2] = oppScore;
        switch (winLossDraw){
            case 1:
                record[0]++;
                break;
            case 2:
                record[1]++;
                break;
            case 3:
                record[2]++;
                break;
        }
        gamesPlayed.add(opponents.peekFirst());
        opponents.removeFirst();
    }

    public String getRecord(){
        String recordString;
        recordString = "Record (";
        recordString += Integer.toString(record[0]) + "-";
        recordString += Integer.toString(record[1]) + "-";
        recordString += Integer.toString(record[2]);
        recordString += ")";
        return recordString;
    }

    public int getPlayedGames(){
        return gamesPlayed.size();
    }

    public int getGames(boolean home){
        if(home)
            return homeGames;
        else
            return awayGames;
    }

    public boolean getHomeField(int week){
        return homeField[week];
    }

    public void addGame(Team opponent, int week, boolean homeField){
        opponents.add(opponent);
        this.homeField[week] = homeField;
        if( homeField )
            homeGames--;
        else
            awayGames--;
    }

    public int gamesScheduled(){
        return opponents.size();
    }

    public void resetSchedule(){
        opponents.clear();
        gamesPlayed.clear();
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 3; j++)
                results[i][j] = 0;
        }
        for(int i = 0; i < 16; i++){
            homeField[i] = false;
        }
        for(int i = 0; i < 3; i++){
            record[i] = 0;
        }
        homeGames = 8;
        awayGames = 8;
    }


}
