package com.example.arturo.fantasyfootball;

import java.io.Serializable;

public class Team implements Serializable{
	private String teamName;
	private boolean userTeam;
	private int division;
	private Schedule schedule;
	
	private Player allPlayers[] = new Player[23];
	private int currentPlayersFilled;
	
	private Player qb;
	private Player rb;
	private Player fb;
	private Player wr1;
	private Player wr2;
	private Player te;
	private Player lt;
	private Player lg;
	private Player c;
	private Player rg;
	private Player rt;
	
	private Player re;
	private Player dt1;
	private Player dt2;
	private Player le;
	private Player lolb;
	private Player mlb;
	private Player rolb;
	private Player cb1;
	private Player cb2;
	private Player fs;
	private Player ss;
	
	private Player k;
	
	
	/*
	 * Integer values store will be used for game simulation
	 * It averages the overall of your rating type(i.e. pass, run, block etc)
	 */
	private int qbs;
	private int rbs;
	private int wrs;
	private int tes;
	private int ols;
	private int dls;
	private int lbs;
	private int cbs;
	private int fss;
	private int ks;
	
	
	public int[] positionRatings = new int[23];
	public final String[] allPositions = { "QB", "RB", "FB",
									 "WR1", "WR2", "TE",
									 "LT", "LG", "C","RG","RT",
									 "LE", "DT1", "DT2", "RE",
									 "LOLB", "MLB", "ROLB",
									 "CB1", "CB2", "FS", "SS",
									 "K"};
	
	
	
	public Team(){
		schedule = new Schedule();
		userTeam = false;

		currentPlayersFilled = 0;
		
		qb = new Player("QB");
		rb = new Player("RB");
		fb = new Player("FB");
		wr1 = new Player("WR");
		wr2 = new Player("WR");
		te = new Player("TE");
		lt = new Player("LT");
		lg = new Player("LG");
		c = new Player("C");
		rg = new Player("RG");
		rt = new Player("RT");
		
		le = new Player("LE");
		dt1 = new Player("DT");
		dt2 = new Player("DT");
		re = new Player("RE");
		lolb = new Player("LOLB");
		mlb = new Player("MLB");
		rolb = new Player("ROLB");
		cb1 = new Player("CB");
		cb2 = new Player("CB");
		fs = new Player("FS");
		ss = new Player("SS");
		
		k = new Player("K");
		
		for(int i = 0; i<23; i++){
			positionRatings[i] = 0;
		}

	}
	
	public Team(String newTeamName){
		teamName = newTeamName.toUpperCase();
		userTeam = false;
		schedule = new Schedule();
		
		currentPlayersFilled = 0;
		
		qb = new Player("QB");
		rb = new Player("RB");
		fb = new Player("FB");
		wr1 = new Player("WR");
		wr2 = new Player("WR");
		te = new Player("TE");
		lt = new Player("LT");
		lg = new Player("LG");
		c = new Player("C");
		rg = new Player("RG");
		rt = new Player("RT");
		
		le = new Player("LE");
		dt1 = new Player("DT");
		dt2 = new Player("DT");
		re = new Player("RE");
		lolb = new Player("LOLB");
		mlb = new Player("MLB");
		rolb = new Player("ROLB");
		cb1 = new Player("CB");
		cb2 = new Player("CB");
		fs = new Player("FS");
		ss = new Player("SS");
		
		k = new Player("K");
		
		for(int i = 0; i<23; i++){
			positionRatings[i] = 0;
		}

		qbs = 0;
		rbs = 0;
		wrs = 0;
		tes = 0;
		ols = 0;
		dls = 0;
		lbs = 0;
		cbs = 0;
		fss = 0;
		ks = 0;
	}

	public Team(String newTeamName, int div, boolean userTeam){
		teamName = newTeamName.toUpperCase();
		this.userTeam = userTeam;
		division = div;
		schedule = new Schedule();

		currentPlayersFilled = 0;

		qb = new Player("QB");
		rb = new Player("RB");
		fb = new Player("FB");
		wr1 = new Player("WR");
		wr2 = new Player("WR");
		te = new Player("TE");
		lt = new Player("LT");
		lg = new Player("LG");
		c = new Player("C");
		rg = new Player("RG");
		rt = new Player("RT");

		le = new Player("LE");
		dt1 = new Player("DT");
		dt2 = new Player("DT");
		re = new Player("RE");
		lolb = new Player("LOLB");
		mlb = new Player("MLB");
		rolb = new Player("ROLB");
		cb1 = new Player("CB");
		cb2 = new Player("CB");
		fs = new Player("FS");
		ss = new Player("SS");

		k = new Player("K");

		for(int i = 0; i<23; i++){
			positionRatings[i] = 0;
		}

		qbs = 0;
		rbs = 0;
		wrs = 0;
		tes = 0;
		ols = 0;
		dls = 0;
		lbs = 0;
		cbs = 0;
		fss = 0;
		ks = 0;
	}
	
	public void resetTeam(){
		
		currentPlayersFilled = 0;
		
		qb = new Player("QB");
		rb = new Player("RB");
		fb = new Player("FB");
		wr1 = new Player("WR");
		wr2 = new Player("WR");
		te = new Player("TE");
		lt = new Player("LT");
		lg = new Player("LG");
		c = new Player("C");
		rg = new Player("RG");
		rt = new Player("RT");
		
		le = new Player("LE");
		dt1 = new Player("DT");
		dt2 = new Player("DT");
		re = new Player("RE");
		lolb = new Player("LOLB");
		mlb = new Player("MLB");
		rolb = new Player("ROLB");
		cb1 = new Player("CB");
		cb2 = new Player("CB");
		fs = new Player("FS");
		ss = new Player("SS");
		
		k = new Player("K");
		
		for(int i = 0; i<23; i++){
			positionRatings[i] = 0;
		}

		qbs = 0;
		rbs = 0;
		wrs = 0;
		tes = 0;
		ols = 0;
		dls = 0;
		lbs = 0;
		cbs = 0;
		fss = 0;
		ks = 0;
	}

	public void setUserTeam(){
		userTeam = true;
	}
	
	/*
	 * GETTERS
	 */

	//getSchedule
	public Schedule getSchedule(){
		return schedule;
	}
	//get boolean userTeam
	public boolean isUserTeam(){
		return userTeam;
	}

	//getPlayers
	public Player getQB(){
		return qb;
	}
	public Player getRB(){
		return rb;
	}
	public Player getFB(){
		return fb;
	}
	public Player getTE(){
		return te;
	}
	public Player getWR1(){
		return wr1;
	}
	public Player getWR2(){
		return wr2;
	}
	public Player getLT(){
		return lt;
	}
	public Player getLG(){
		return lg;
	}
	public Player getC(){
		return c;
	}
	public Player getRG(){
		return rg;
	}
	public Player getRT(){
		return rt;
	}
	
	public Player getLE(){
		return le;
	}
	public Player getDT1(){
		return dt1;
	}
	public Player getDT2(){
		return dt2;
	}
	public Player getRE(){
		return re;
	}
	public Player getLOLB(){
		return lolb;
	}
	public Player getMLB(){
		return mlb;
	}
	public Player getROLB(){
		return rolb;
	}
	public Player getCB1(){
		return cb1;
	}
	public Player getCB2(){
		return cb2;
	}
	public Player getFS(){
		return fs;
	}
	public Player getSS(){
		return ss;
	}
	
	public Player getK(){
		return k;
	}
	
	//get teamRatings
	public int getQBS(){
		return qbs;
	}
	public int getQBS(int stat){
        return qb.getStat(stat);
    }
	
	public int getRBS(){
		return rbs;
	}
    public int getRBS(int stat){
        if(stat == 1){
            if(fb.getStat(stat) > rb.getStat(stat))
                return ( (fb.getStat(stat) + rb.getStat(stat)) / 2);
        }

        return rb.getStat(stat);
    }
	
	public int getWRS(){
		return wrs;
	}
    public int getWRS(int option, int stat){
        if(option==1){
            wr1.getStat(stat);
        }
        else if(option==2){
            wr2.getStat(stat);
        }
        return wrs;
    }
	
	public int getTES(){
		return tes;
	}
    public int getTES(int stat){
        return te.getStat(stat);
    }
	
	public int getOLS(){
		return ols;
	}
    public int getOLS(int stat){
        int retStat = lt.getStat(stat) + lg.getStat(stat) + c.getStat(stat) + rg.getStat(stat) + rt.getStat(stat);
        int group = 5;
        if(stat == 2){
            //run blocking
            if(fb.getStat(1) > retStat){
                group++;
                retStat += fb.getStat(1);
            }
            if (te.getStat(2) > retStat) {
                group++;
                retStat += te.getStat(2);
            }

        }
        return (retStat / group);
    }
	
	public int getDLS(){
		return dls;
	}
    public int getDLS(int stat){
        return ((le.getStat(stat) + re.getStat(stat) + dt1.getStat(stat) + dt2.getStat(stat)) / 4);
    }
	
	public int getLBS(){
		return lbs;
	}
    public int getLBS(int stat){
        int retStat = lolb.getStat(stat) + mlb.getStat(stat) + rolb.getStat(stat);
        int group = 3;
        if(stat == 1){
           if(ss.getStat(2) > retStat){
               group++;
               retStat += ss.getStat(2);
           }
        }
        return (retStat / group);
    }
	
	public int getCBS(){
		return cbs;
	}
    public int getCBS(int option, int stat){
        int retStat;
        int group = 1;
        if(option == 1){
            retStat = cb1.getStat(stat);
        }
        else if(option == 2){
            retStat = cb2.getStat(stat);
        }
        else
            retStat = cbs;

        if(fs.getStat(1) > retStat){
            group++;
            retStat += fs.getStat(1);
        }

        return (retStat/group);
    }
	
	public int getFSS(){
		return fss;
	}
    public int getFSS(int stat){
        return ((fs.getStat(stat) + ss.getStat(stat)) / 2);
    }
	
	public int getKS(){
		return ks;
	}
    public int getKS(int stat){
        return k.getStat(stat);
    }
	
	public Player[] getPlayerArray(){
	  return allPlayers;
	}

	public String getTeamName(){
	  return teamName;
	}
	
	//team Functions
	public void addPlayer(Player adding){
		switch( adding.getPosition() ){
			case "QB":	qb = adding;
						positionRatings[0] = qb.getStatRating();
				break;
			case "RB":	rb = adding;
						positionRatings[1] = rb.getStatRating();
				break;
			case "FB":	fb = adding;
						positionRatings[2] = fb.getStatRating();
				break;
            case "WR1": wr1 = adding;
                        positionRatings[3] = wr1.getStatRating();
				break;
            case "WR2": wr2 = adding;
                        positionRatings[4] = wr2.getStatRating();
                break;
			case "TE": te = adding;
						positionRatings[5] = te.getStatRating();
				break;
			case "LT": lt = adding;
						positionRatings[6] = lt.getStatRating();
				break;
			case "LG": lg = adding;
						positionRatings[7] = lg.getStatRating();
				break;
			case "C" : c = adding;
						positionRatings[8] = c.getStatRating();
				break;
			case "RG": rg = adding;
						positionRatings[9] = rg.getStatRating();
				break;
			case "RT": rt = adding;
						positionRatings[10] = rt.getStatRating();
				break;
			case "LE": le = adding;
						positionRatings[11] = le.getStatRating();
				break;
			case "DT1": dt1 = adding;
                        positionRatings[12] = dt1.getStatRating();
                break;
            case "DT2": dt2 = adding;
						positionRatings[13] = dt2.getStatRating();
				break;
			case "RE": re = adding;
						positionRatings[14] = re.getStatRating();
				break;
			case "LOLB": lolb = adding;
						positionRatings[15] = lolb.getStatRating();
				break;
			case "MLB": mlb = adding;
						positionRatings[16] = mlb.getStatRating();
				break;
			case "ROLB": rolb = adding;
						positionRatings[17] = rolb.getStatRating();
				break;
			case "CB1": cb1 = adding;
						positionRatings[18] = cb1.getStatRating();
			    break;
            case "CB2": cb2 = adding;
                        positionRatings[19] = cb2.getStatRating();
				break;
			case "FS": fs = adding;
						positionRatings[20] = fs.getStatRating();
				break;
			case "SS": ss = adding;
						positionRatings[21] = ss.getStatRating();
				break;
			case "K" : k = adding;
						positionRatings[22] = k.getStatRating();
				break;
			default:
				break;
		}
		
		allPlayers[currentPlayersFilled] = adding;
		currentPlayersFilled++;
		fillStats();
	}

    public boolean positionFilled(String position){
        int i = 0;
        while(i < allPositions.length){
            if(position.equals(allPositions[i]))
                break;
            else
                i++;
        }

        if(positionRatings[i] == 0)
            return false;

        return true;
    }

	/*public void simDraft(){
		DraftSim draft = new DraftSim();

	}*/

	public boolean isTeamFull(){
	  if( currentPlayersFilled == 23 )
	    return true;
	  else
	    return false;
	}

	public void fillStats() {
		if( !isTeamFull() )
			return;

        qbs = qb.getStatRating();
        rbs = rb.getStatRating();
        if (fb.getStatRating() > rbs)
            rbs = (rbs + fb.getStatRating()) / 2;
        wrs = (wr1.getStatRating() + wr2.getStatRating()) / 2;
        tes = te.getStatRating();
        ols = (lt.getStatRating() + lg.getStatRating() + c.getStatRating() + rg.getStatRating() + rt.getStatRating()) / 5;
        if (fb.getStatRating() > ols)
            ols = ((ols * 5) + fb.getStatRating()) / 6;
        if (te.getStatRating() > ols)
            ols = ((ols * 5) + te.getStatRating()) / 6;

        dls = (le.getStatRating() + dt1.getStatRating() + dt2.getStatRating() + re.getStatRating()) / 4;
        lbs = (lolb.getStatRating() + mlb.getStatRating() + rolb.getStatRating()) / 3;
        if (ss.getStatRating() > lbs)
            lbs = (lbs + ss.getStatRating()) / 2;
        cbs = (cb1.getStatRating() + cb2.getStatRating()) / 2;
        if (fs.getStatRating() > cbs)
            cbs = (cbs + fs.getStatRating()) / 2;
        fss = (fs.getStatRating() + ss.getStatRating()) / 2;

        ks = k.getStatRating();
    }

	public  void showTeamLite(){
		   System.out.println("\n\n\tOffense");
		   System.out.print("\t");
		   for(int i = 0; i < 11; i++){
			   System.out.print(allPositions[i] + "\t");
		   }
		   System.out.println();
		   System.out.print("\t");
		   for(int i = 0; i < 11; i++){
			   if( positionRatings[i] == 0)
				   System.out.print("- \t");
			   else
				   System.out.print(positionRatings[i]+ "\t");
		   }
		   
		   System.out.println("\n\n\tDefense");
		   System.out.print("\t");
		   for(int i = 11; i < 22; i++){
			   System.out.print(allPositions[i] + "\t");
		   }
		   System.out.println();
		   System.out.print("\t");
		   for(int i = 11; i < 22; i++){
			   if( positionRatings[i] == 0)
				   System.out.print("- \t");
			   else
				   System.out.print(positionRatings[i]+ "\t");
		   }
		   
		   System.out.println("\n\n\tSpecial Teams");
		   System.out.println("\t" + allPositions[22]);
		   System.out.print("\t");
		   if( positionRatings[22] == 0)
			   System.out.println("- \t");
		   else
			   System.out.println(positionRatings[22]);
		   
			   
	}   

	public String showTeamGrade(){
		String grade;
        grade = "Overall " + getTeamOvr();

        grade += "\nPassing " + qbs;
        grade += "\nRushing " + rbs;
        grade += "\nReceiving " + ((wrs + tes) / 2);
        grade += "\nBlocking " + ols;

        grade += "\nBlock Shedding " + dls;
        grade += "\nRun Stopping " + lbs;
        grade += "\nPass Coverage " + (cbs + fss) / 2;

        grade += "\nKicking " + ks;

        return grade;
	}

	public void setTeamName(String teamName){
        this.teamName = teamName;
    }

    public void setTeamStats(int[] teamStats){
        Player temp;
        for(int i = 0; i < teamStats.length; i++){
            temp = new Player(allPositions[i], teamStats[i]);
            addPlayer(temp);
        }
    }

    public int getTeamOvr(){
        return ( (qbs + rbs + wrs + tes + ols + dls + lbs + cbs + fss) / 9 );
    }

	public void saveTeam(){

	}

	public void endGame(int week){
		for(int i = 0; i < allPlayers.length; i++){
			allPlayers[i].endGame(week);
		}
	}
}