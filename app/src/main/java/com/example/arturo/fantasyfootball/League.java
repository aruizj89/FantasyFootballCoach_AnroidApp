package com.example.arturo.fantasyfootball;

/**
 * Created by Arturo on 7/5/2016.
 */

import java.io.Serializable;

public class League implements Serializable {
    private int key;
    private int teams;
    private Team[] leagueTeams;
    private int week;
    private int year;

    public League(int key){
        this.key = key;
        leagueTeams = new Team[32];
        teams = 0;
        week = 0;
        year = 2016;
        fillLeague(teams);
    }

    public League(int key, Team team){
        this.key = key;
        leagueTeams = new Team[32];
        leagueTeams[0] = team;
        teams = 1;
        week = 0;
        year = 2016;
        fillLeague(1);
    }

    public void insertTeam(Team insertedTeam, int teamIndex){
        leagueTeams[teamIndex] = insertedTeam;
    }

    public Team getTeam(int teamIndex){
        return leagueTeams[teamIndex];
    }

    private void fillLeague(int userTeams) {
        if( userTeams == 32 )
            return;

        teams = userTeams;
        DraftSim draft  = new DraftSim();

        while( !isLeagueFull() ){
            leagueTeams[teams] = draft.simTeamDraft(Integer.toString(teams + 1), teams%8);
            teams++;
        }

    }

    public boolean isLeagueFull() {
        if( teams == 32 )
            return true;
        return false;
    }

}
