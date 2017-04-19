package com.example.arturo.fantasyfootball;

import java.io.Serializable;

/**
 * Created by Arturo on 1/4/2016.
 */
public class GameStats implements Serializable {

    private int passAttempts;
    private int passCompletions;
    private int completionPercentage;
    private int passYards;
    private int passTD;
    private int passInt;
    private int passSacked;
    private int rushAttempts;
    private int rushYards;
    private int rushAvg;
    private int rushTD;
    private int rushFumbles;
    private int passDropped;
    private int passCaught;
    private int receivingYards;
    private int receivingTD;
    private int kickAttempts;
    private int kickMade;
    private int kickPercentage;

    public GameStats(){
        passAttempts = 0;
        passCompletions = 0;
        completionPercentage = 0;
        passYards = 0;
        passTD = 0;
        passInt = 0;
        passSacked = 0;
        rushAttempts = 0;
        rushYards = 0;
        rushAvg = 0;
        rushTD = 0;
        rushFumbles = 0;
        passDropped = 0;
        passCaught = 0;
        receivingYards = 0;
        receivingTD = 0;
        kickAttempts = 0;
        kickMade = 0;
        kickPercentage = 0;
    }

    public void resetGameStats() {
        passAttempts = 0;
        passCompletions = 0;
        completionPercentage = 0;
        passYards = 0;
        passTD = 0;
        passInt = 0;
        passSacked = 0;
        rushAttempts = 0;
        rushYards = 0;
        rushAvg = 0;
        rushTD = 0;
        rushFumbles = 0;
        passDropped = 0;
        passCaught = 0;
        receivingYards = 0;
        receivingTD = 0;
        kickAttempts = 0;
        kickMade = 0;
        kickPercentage = 0;
    }

    public int[] playerStatGetter(){
        int[] playerStats = {
                passAttempts,
                passCompletions,
                completionPercentage,
                passYards,
                passTD,
                passInt,
                passSacked,
                rushAttempts,
                rushYards,
                rushAvg,
                rushTD,
                rushFumbles,
                passDropped,
                passCaught,
                receivingYards,
                receivingTD,
                kickAttempts,
                kickMade,
                kickPercentage
        };

        return playerStats;
    }

    public void incPassAttempts(){
        passAttempts++;
        completionPercentage = 100 * passCompletions / passAttempts;
    }
    public void incPassCompletions(){
        passCompletions++;
        completionPercentage = 100 * passCompletions / passAttempts;
    }
    public void incPassYards(int yards){
        passYards += yards;
    }
    public void incPassTD(){
        passTD++;
    }
    public void incPassInt(){
        passInt++;
    }
    public void incPassSacked(){
        passSacked++;
    }
    public void incRushAttempts(){
        rushAttempts++;
    }
    public void incRushYards(int yards){
        rushYards += yards;
        rushYards *= 10;
        rushAvg = rushYards / rushAttempts;
        rushYards /= 10;
    }
    public void incRushTD(){
        rushTD++;
    }
    public void incRushFumbles(){
        rushFumbles++;
    }
    public void incPassDropped(){
        passDropped++;
    }
    public void incPassCaught(){
        passCaught++;
    }
    public void incReceivingYards(int yards){
        receivingYards += yards;
    }
    public void incReceivingTD(){
        receivingTD++;
    }
    public void incKickAttempt(){
        kickAttempts++;
        kickPercentage = 100 * kickMade / kickAttempts;
    }
    public void incKickMade(){
        kickMade++;
        kickPercentage = 100 * kickMade / kickAttempts;
    }

    public String getPassStats(String string){
        String stats;
        stats = (".:Passing:.");
        stats += ("\n" + string);
        stats += ("\nAttempts: " + passAttempts);
        stats += ("\nCompletions: " + passCompletions);
        stats += ("\nCompletion %: " + completionPercentage);
        stats += ("\nPass Yards: " + passYards);
        stats += ("\nPass TDs: " + passTD);
        stats += ("\nInterceptions: " + passInt);
        stats += ("\nSacked: " + passSacked);

        return stats;
    }
    public String getRushStats(String string){
        String stats;
        stats = (".:Rushing:.");
        stats += ("\n" + string);
        stats += ("\nAttempts: " + rushAttempts);
        stats += ("\nRush Yards: " + rushYards);
        if(rushAvg < 0)
            stats += ("\nAverage: -" + Math.abs(rushAvg/10) + "." + Math.abs(rushAvg%10));
        else
            stats += ("\nAverage: " + Math.abs(rushAvg/10) + "." + Math.abs(rushAvg%10));
        stats += ("\nRush TDs" + rushTD);
        stats += ("\nFumbles" + rushFumbles);

        return stats;
    }
    public String getReceivingStats(String string){
        String stats;
        stats = (".:Receiving:.");
        stats += ("\n" + string);
        stats += ("\nCaught: " + passCaught);
        stats += ("\nYards: " + receivingYards);
        stats += ("\nReceiving TDs" + receivingTD);
        stats += ("\nDrops: " + passDropped);

        return stats;
    }
    public String getKickStats(String string){
        String stats;
        stats = (".:Kicking:.");
        stats += ("\n" + string);
        stats += ("Attempts: " + kickAttempts);
        stats += ("Made: " + kickMade);
        stats += ("Completion %: " + kickPercentage);

        return stats;
    }

}