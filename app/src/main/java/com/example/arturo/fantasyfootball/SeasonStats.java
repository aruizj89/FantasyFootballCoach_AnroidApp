package com.example.arturo.fantasyfootball;

import java.io.Serializable;

/**
 * Created by Arturo on 1/4/2016.
 */
public class SeasonStats implements Serializable {

    private int[] passAttempts;
    private int[] passCompletions;
    private int[] completionPercentage;
    private int[] passYards;
    private int[] passTD;
    private int[] passInt;
    private int[] passSacked;
    private int[] rushAttempts;
    private int[] rushYards;
    private int[] rushAvg;
    private int[] rushTD;
    private int[] rushFumbles;
    private int[] passDropped;
    private int[] passCaught;
    private int[] receivingYards;
    private int[] receivingTD;
    private int[] kickAttempts;
    private int[] kickMade;
    private int[] kickPercentage;

    private int gamesFinished;

    public SeasonStats(){
        //[0] - [15] : game stats
        //[16] : season totals
        passAttempts = new int[17];
        passCompletions = new int[17];
        completionPercentage = new int[17];
        passYards = new int[17];
        passTD = new int[17];
        passInt = new int[17];
        passSacked = new int[17];
        rushAttempts = new int[17];
        rushYards = new int[17];
        rushAvg = new int[17];
        rushTD = new int[17];
        rushFumbles = new int[17];
        passDropped = new int[17];
        passCaught = new int[17];
        receivingYards = new int[17];
        receivingTD = new int[17];
        kickAttempts = new int[17];
        kickMade = new int[17];
        kickPercentage = new int[17];
        gamesFinished = 0;
    }

    public void endGame(){
        passAttempts[16] += passAttempts[gamesFinished];
        passCompletions[16] += passCompletions[gamesFinished];
        completionPercentage[16] += completionPercentage[gamesFinished];
        passYards[16] += passYards[gamesFinished];
        passTD[16] += passTD[gamesFinished];
        passInt[16] += passInt[gamesFinished];
        passSacked[16] += passSacked[gamesFinished];
        rushAttempts[16] += rushAttempts[gamesFinished];
        rushYards[16] += rushYards[gamesFinished];
        rushAvg[16] += rushAvg[gamesFinished];
        rushTD[16] += rushTD[gamesFinished];
        rushFumbles[16] += rushFumbles[gamesFinished];
        passDropped[16] += passDropped[gamesFinished];
        passCaught[16] += passCaught[gamesFinished];
        receivingYards[16] += receivingYards[gamesFinished];
        receivingTD[16] += receivingTD[gamesFinished];
        kickAttempts[16] += kickAttempts[gamesFinished];
        kickMade[16] += kickMade[gamesFinished];
        kickPercentage[16] += kickPercentage[gamesFinished];
        if(gamesFinished < 16)
            gamesFinished++;
    }

    public void incPassAttempts(){
        passAttempts[gamesFinished]++;
        completionPercentage[gamesFinished] = 100 * passCompletions[gamesFinished] / passAttempts[gamesFinished];
    }
    public void incPassCompletions(){
        passCompletions[gamesFinished]++;
        completionPercentage[gamesFinished] = 100 * passCompletions[gamesFinished] / passAttempts[gamesFinished];
    }
    public void incPassYards(int yards){
        passYards[gamesFinished] += yards;
    }
    public void incPassTD(){
        passTD[gamesFinished]++;
    }
    public void incPassInt(){
        passInt[gamesFinished]++;
    }
    public void incPassSacked(){
        passSacked[gamesFinished]++;
    }
    public void incRushAttempts(){
        rushAttempts[gamesFinished]++;
    }
    public void incRushYards(int yards){
        rushYards[gamesFinished] += yards;
        rushYards[gamesFinished] *= 10;
        rushAvg[gamesFinished] = rushYards[gamesFinished] / rushAttempts[gamesFinished];
        rushYards[gamesFinished] /= 10;
    }
    public void incRushTD(){
        rushTD[gamesFinished]++;
    }
    public void incRushFumbles(){
        rushFumbles[gamesFinished]++;
    }
    public void incPassDropped(){
        passDropped[gamesFinished]++;
    }
    public void incPassCaught(){
        passCaught[gamesFinished]++;
    }
    public void incReceivingYards(int yards){
        receivingYards[gamesFinished] += yards;
    }
    public void incReceivingTD(){
        receivingTD[gamesFinished]++;
    }
    public void incKickAttempt(){
        kickAttempts[gamesFinished]++;
        kickPercentage[gamesFinished] = 100 * kickMade[gamesFinished] / kickAttempts[gamesFinished];
    }
    public void incKickMade(){
        kickMade[gamesFinished]++;
        kickPercentage[gamesFinished] = 100 * kickMade[gamesFinished] / kickAttempts[gamesFinished];
    }

    public String getPassStats(String string){
        String stats;
        stats = (".:Passing:.");
        stats += ("\n" + string);
        stats += ("\nAttempts: " + passAttempts[16]);
        stats += ("\nCompletions: " + passCompletions[16]);
        stats += ("\nCompletion %: " + completionPercentage[16]);
        stats += ("\nPass Yards: " + passYards[16]);
        stats += ("\nPass TDs: " + passTD[16]);
        stats += ("\nInterceptions: " + passInt[16]);
        stats += ("\nSacked: " + passSacked[16]);

        return stats;
    }
    public String getRushStats(String string){
        String stats;
        stats = (".:Rushing:.");
        stats += ("\n" + string);
        stats += ("\nAttempts: " + rushAttempts[16]);
        stats += ("\nRush Yards: " + rushYards[16]);
        if(rushAvg[16] < 0)
            stats += ("\nAverage: -" + Math.abs(rushAvg[16]/10) + "." + Math.abs(rushAvg[16]%10));
        else
            stats += ("\nAverage: " + Math.abs(rushAvg[16]/10) + "." + Math.abs(rushAvg[16]%10));
        stats += ("\nRush TDs" + rushTD[16]);
        stats += ("\nFumbles" + rushFumbles[16]);

        return stats;
    }
    public String getReceivingStats(String string){
        String stats;
        stats = (".:Receiving:.");
        stats += ("\n" + string);
        stats += ("\nCaught: " + passCaught[16]);
        stats += ("\nYards: " + receivingYards[16]);
        stats += ("\nReceiving TDs" + receivingTD[16]);
        stats += ("\nDrops: " + passDropped[16]);

        return stats;
    }
    public String getKickStats(String string){
        String stats;
        stats = (".:Kicking:.");
        stats += ("\n" + string);
        stats += ("Attempts: " + kickAttempts[16]);
        stats += ("Made: " + kickMade[16]);
        stats += ("Completion %: " + kickPercentage[16]);

        return stats;
    }
}
