package com.example.arturo.fantasyfootball;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Arturo on 1/4/2016.
 */
public class Rating implements Serializable {

    private int prjGrade;//(A+, A, A-, B+, B, B- ,C+, C, C-) : (1, 2, 3, 4, 5, 6, 7, 8, 9)
    private int ovrRating;
    private int stat1;
    private int stat2;
    private int stat3;
    private int base;

    private Random numGen = new Random();

    public Rating(int grade){
        prjGrade = grade;
        base = 93 - (((grade - 1) / 3) * 10) - (((grade - 1) % 3) * 2);
        stat1 = 60 + numGen.nextInt(20 - (((grade - 1)/3) * 5));
        stat2 = 60 + numGen.nextInt(20 - (((grade - 1)/3) * 5));
        stat3 = 60 + numGen.nextInt(15);
        if( numGen.nextInt(5) == (stat3%5) )
            stat3 += (stat3%10);
        while(!setOvr())
            setOvr();
    }

    private boolean setOvr(){
        ovrRating = (stat1 * 45 / 100) + (stat2 * 45 / 100) + (stat3 * 10 / 100);
        if(ovrRating > base){
            return true;
        }

        stat1 += numGen.nextInt(15);
        if( numGen.nextInt(5) == (stat1%5) )
            stat1 += (stat1%10);
        stat2 += numGen.nextInt(15);
        if( numGen.nextInt(5) == (stat2%5) )
            stat2 += (stat2%10);
        stat3 += numGen.nextInt(11);
        if( numGen.nextInt(10) == (stat3%10) )
            stat3 += (stat3%5);

        if(stat1 > 99)
            stat1 = 99;
        if(stat2 > 99)
            stat2 = 99;
        if(stat3 > 99)
            stat3 = 99;

        return false;
    }

    public int getStat(int stat){
        switch (stat){
            case 1:
                return stat1;
            case 2:
                return stat2;
            case 3:
                return stat3;
            case 0:
                return ovrRating;
            default:
                return 0;
        }
    }

    public String getGrade(){
        switch (prjGrade){
            case 1:
                return "A+";
            case 2:
                return "A";
            case 3:
                return "A-";
            case 4:
                return "B+";
            case 5:
                return "B";
            case 6:
                return "B-";
            case 7:
                return "C+";
            case 8:
                return "C";
            case 9:
                return "C-";
            default:
                return "F";
        }
    }

}