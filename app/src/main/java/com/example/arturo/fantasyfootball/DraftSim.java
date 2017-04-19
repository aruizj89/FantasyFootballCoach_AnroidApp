package com.example.arturo.fantasyfootball;

import java.util.Random;

/**
 * Created by Arturo on 7/5/2016.
 */
public class DraftSim {
    private int budget;
    private Team simTeam;
    private Random numGen = new Random();

    public Team simTeamDraft(String teamName, int div){
        budget = 800 + numGen.nextInt(9)*50;
        simTeam = new Team(teamName, div, false);
        simulateDraft();
        return simTeam;
    }

    private void simulateDraft(){
        while(budget > 0 && !simTeam.isTeamFull()) {
            draftPosition(simTeam.allPositions[numGen.nextInt(simTeam.allPositions.length)]);
        }
        fillTeam();
    }

    private void draftPosition(String position){
        if(simTeam.positionFilled(position))
            return;//return if position already drafted

        //create players
        Player createdPlayer1 = new Player(position, getFractionalGrade(7));
        Player createdPlayer2 = new Player(position, getFractionalGrade(4));
        Player createdPlayer3 = new Player(position, getFractionalGrade(1));

        //simulate draft pick
        if(simTeam.isTeamFull())
            budget = 0;
        if(budget == 50){
            budget -= 50;
            simTeam.addPlayer(createdPlayer2);
        }
        if(budget == 0)
            return;
        switch (numGen.nextInt(2)){
            case 0:
                budget -= 50;
                simTeam.addPlayer(createdPlayer2);
                break;
            case 1:
                budget -= 100;
                simTeam.addPlayer(createdPlayer3);
                break;
            default:
                break;
        }
    }

    private int getFractionalGrade(int grade){
        return (grade + numGen.nextInt(3));
    }

    private void fillTeam(){
        if( simTeam.getQB().getStatRating() == 0 )
            simTeam.addPlayer(new Player("QB", 7 + numGen.nextInt(3)));
        if( simTeam.getRB().getStatRating() == 0 )
            simTeam.addPlayer(new Player("RB", 7 + numGen.nextInt(3)));
        if( simTeam.getFB().getStatRating() == 0 )
            simTeam.addPlayer(new Player("FB", 7 + numGen.nextInt(3)));
        if( simTeam.getWR1().getStatRating() == 0 )
            simTeam.addPlayer(new Player("WR1", 7 + numGen.nextInt(3)));
        if( simTeam.getWR2().getStatRating() == 0)
            simTeam.addPlayer(new Player("WR2", 7 + numGen.nextInt(3)));
        if( simTeam.getTE().getStatRating() == 0)
            simTeam.addPlayer(new Player("TE", 7 + numGen.nextInt(3)));
        if( simTeam.getLT().getStatRating() == 0)
            simTeam.addPlayer(new Player("LT", 7 + numGen.nextInt(3)));
        if( simTeam.getLG().getStatRating() == 0)
            simTeam.addPlayer(new Player("LG", 7 + numGen.nextInt(3)));
        if( simTeam.getC().getStatRating() == 0 )
            simTeam.addPlayer(new Player("C", 7 + numGen.nextInt(3)));
        if( simTeam.getRG().getStatRating() == 0 )
            simTeam.addPlayer(new Player("RG", 7 + numGen.nextInt(3)));
        if( simTeam.getRT().getStatRating() == 0 )
            simTeam.addPlayer(new Player("RT", 7 + numGen.nextInt(3)));

        if( simTeam.getLE().getStatRating() == 0 )
            simTeam.addPlayer(new Player("LE", 7 + numGen.nextInt(3)));
        if( simTeam.getDT1().getStatRating() == 0 )
            simTeam.addPlayer(new Player("DT1", 7 + numGen.nextInt(3)));
        if( simTeam.getDT2().getStatRating() == 0 )
            simTeam.addPlayer(new Player("DT2", 7 + numGen.nextInt(3)));
        if( simTeam.getRE().getStatRating() == 0 )
            simTeam.addPlayer(new Player("RE", 7 + numGen.nextInt(3)));
        if( simTeam.getLOLB().getStatRating() == 0 )
            simTeam.addPlayer(new Player("LOLB", 7 + numGen.nextInt(3)));
        if( simTeam.getMLB().getStatRating() == 0 )
            simTeam.addPlayer(new Player("MLB", 7 + numGen.nextInt(3)));
        if( simTeam.getROLB().getStatRating() == 0 )
            simTeam.addPlayer(new Player("ROLB", 7 + numGen.nextInt(3)));
        if( simTeam.getCB1().getStatRating() == 0 )
            simTeam.addPlayer(new Player("CB1", 7 + numGen.nextInt(3)));
        if( simTeam.getCB2().getStatRating() == 0 )
            simTeam.addPlayer(new Player("CB2", 7 + numGen.nextInt(3)));
        if( simTeam.getFS().getStatRating() == 0)
            simTeam.addPlayer(new Player("FS", 7 + numGen.nextInt(3)));
        if( simTeam.getSS().getStatRating() == 0 )
            simTeam.addPlayer(new Player("SS", 7 + numGen.nextInt(3)));

        if( simTeam.getK().getStatRating() == 0 )
            simTeam.addPlayer(new Player("K", 7 + numGen.nextInt(3)));
    }

}
