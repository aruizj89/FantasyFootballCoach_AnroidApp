package com.example.arturo.fantasyfootball;

/**
 * Created by Arturo on 7/30/2016.
 */
public class WeekSim {

    private int week;
    private League league;
    private GameSim simulation = new GameSim();

    public void simGame(League league, int week){
        this.week = week;
        this.league = league;
        for(int i = 0; i < 32; i++)
            setTeams(i);
    }

    private void setTeams(int team){
        //if( league.getTeam(team).isUserTeam() )
        //return;
        if(week != league.getTeam(team).getSchedule().getPlayedGames() )
            return;
        if(league.getTeam(team).getSchedule().getHomeField(week)){
            //current team is home | simGame(currentTeam, opponent, week)
            simulation.simGame(league.getTeam(team), league.getTeam(team).getSchedule().getNextGame(), week);
        }
        else{
            //current team is away | simGame(opponent, currentTeam, week)
            simulation.simGame(league.getTeam(team).getSchedule().getNextGame(), league.getTeam(team), week);
        }
    }

}
