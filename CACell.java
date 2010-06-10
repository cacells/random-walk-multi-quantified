/*
 * This contains the classes for the Cell object for the CA version of the model
 */

import java.util.*;

class CACell{
	public static int maxCycle = 2; // this sets the CAmax to 4 (see type)
	public static Random rand = new Random();

	public boolean canMoveLeft, canMoveRight,canStay,Available;
	public int type; // 0 = space, 1 = SC, 2=CA_1....5=CA_4
	public CABoxStatic home;// The box the cell sits in
	public double stain;
	public double scRate=1.0;// Relative SC proliferaion rate if scRate = 0.5 SC proliferaion rate would be half CA rate
	public int lineage;
	
	
	public CACell(CABoxStatic home,int lin){
		this.home=home;
		lineage = lin;
		canMoveLeft=false;
		canMoveRight=false;
		canStay=false;
		Available=false;
		stain = 0.0;
	}
	
	public void maintain(ParamVals params){// Determines if a Cell can detach or grow and sets counters
        double pm;
        if (type ==1){
        	Available=false;//can't move here this time
		pm = rand.nextDouble();
		canMoveLeft=false;
		canMoveRight=false;
		canStay=false;
		if (pm < params.pr) {canMoveRight = true;}
		else if (pm < (params.pr + params.pl)) {canMoveLeft = true;}
		else {canStay = true;}
		if (params.nowrap){
			if (canMoveLeft && (home.x == 0)){
				canMoveLeft = false;
				canStay = true;
			}
			if (canMoveRight && (home.x == 63)){
				canMoveRight = false;
				canStay = true;
			}
		}
        }
        else Available=true;//this is a space
	}
	
	public void growth(CACell cHold){ // Growth occurs into cell
			type=0;//this spot empty but not made available
			cHold.lineage = lineage;//transfer lineage
			lineage = 0;
			cHold.type=1; // that spot full			
			cHold.Available=false; //that availability is updated
			//rest is not used
			cHold.stain = cHold.stain/2.0;// stain unused
			stain = cHold.stain;// As above
	}
	
	public boolean grow(){// given movement left or right checks for space and moves
		
		CACell cHold;
		if (canStay){
			return false;
		}
		if (canMoveLeft){
			cHold = home.getNeighbour(0);
			if (cHold.Available){
				growth(cHold);
			    return true;
			}
			else return false;			
		}
		if (canMoveRight){
			cHold = home.getNeighbour(1);
			if (cHold.Available){
				growth(cHold);
			    return true;
			}
			else return false;			
		}
		return false;
	}	

}
