package com.example.arturo.fantasyfootball;

import java.io.Serializable;

public class Player implements Serializable{
	
    private String name;
    private String pos;
    private String stat;

    private Rating ratings;
    private GameStats gameStats;
    private SeasonStats seasonStats;

    private int statRating;
    private int cost;
   
    public Player(String pos, int grade){
        name = "noName";
        this.pos = pos;
        stat = "tempStat";//type i.e. pass,run,block,etc.
        cost = 0;
        ratings = new Rating(grade);
        statRating = ratings.getStat(0);
        gameStats = new GameStats();
        seasonStats = new SeasonStats();
    }
   
    public Player(String position){
        name = "noName";
        pos = position;
        stat = "noStat";
        statRating = 0;
        cost = 0;
        gameStats = new GameStats();
        seasonStats = new SeasonStats();
    }
   
    public String getName()
    {
      return name;
    }

    public String getPosition()
    {
      return pos;
    }

    public String getStat()
    {
      return stat;
    }

    public int getStatRating()
    {
      return statRating;
    }

    public int getStat(int stat){
        return ratings.getStat(stat);
    }

    public String getRating(){
        return ratings.getGrade();
    }

    public int getCost()
    {
      return cost;
    }

    public void setName(String newName)
    {
      pos = newName;
    }

    public void setPosition(String newPosition)
    {
      pos = newPosition;
    }

    public void setStat(String newStat)
    {
      stat = newStat;
    }

    public void setStatRating(int newStatRating)
    {
      statRating =  newStatRating;
    }

    public void setCost(int newCost)
    {
      cost = newCost;
    }
   
   //function increases player statistics
    public void incPassAttempts(){
        gameStats.incPassAttempts();
        seasonStats.incPassAttempts();
    }
    public void incPassCompletions(){
        gameStats.incPassCompletions();
        seasonStats.incPassCompletions();
    }
    public void incPassYards(int yards){
        gameStats.incPassYards(yards);
        seasonStats.incPassYards(yards);
    }
    public void incPassTD(){
        gameStats.incPassTD();
        seasonStats.incPassTD();
    }
    public void incPassInt(){
        gameStats.incPassInt();
        seasonStats.incPassInt();
    }
    public void incPassSacked(){
        gameStats.incPassSacked();
        seasonStats.incPassAttempts();
    }
    public void incRushAttempts(){
        gameStats.incRushAttempts();
        seasonStats.incRushAttempts();
    }
    public void incRushYards(int yards){
        gameStats.incRushYards(yards);
        seasonStats.incRushYards(yards);
    }
    public void incRushTD(){
        gameStats.incRushTD();
        seasonStats.incRushTD();
    }
    public void incRushFumbles(){
        gameStats.incRushFumbles();
        seasonStats.incRushFumbles();
    }
    public void incPassDropped(){
        gameStats.incPassDropped();
        seasonStats.incPassDropped();
    }
    public void incPassCaught(){
        gameStats.incPassCaught();
        seasonStats.incPassCaught();
    }
    public void incReceivingYards(int yards){
        gameStats.incReceivingYards(yards);
        seasonStats.incReceivingYards(yards);
    }
    public void incReceivingTD(){
        gameStats.incReceivingTD();
        seasonStats.incReceivingTD();
    }
    public void incKickAttempt(){
        gameStats.incKickAttempt();
        seasonStats.incKickAttempt();
    }
    public void incKickMade(){
        gameStats.incKickMade();
        seasonStats.incKickMade();
    }

    //game stats
    public String getPassStats(){
       return gameStats.getPassStats(pos);
   }
    public String getRushStats(){
        return gameStats.getRushStats(pos);
    }
    public String getReceivingStats(){
        return gameStats.getReceivingStats(pos);
   }
    public String getKickStats(){
       return gameStats.getKickStats(pos);
   }

    //season stats
    public String getSeasonStats(){
        return seasonStats.getPassStats(pos);
    }
    public String getSeasonRushStats(){
        return seasonStats.getRushStats(pos);
    }
    public String getSeasonReceivingStats(){
        return seasonStats.getReceivingStats(pos);
    }
    public String getSeasonKickStats(){
        return seasonStats.getKickStats(pos);
    }

    public void endGame(int game){
        gameStats.resetGameStats();
        seasonStats.endGame();
    }
   
}
