/*
 * This is the main part of the simulation of the CA version of the model 
 */

import java.util.*;
 
public class CAGridStatic {
	
    int[] savedx;
    int[][] savedxs;
    int[] savedy;
	public ArrayList <CACell> tissue;// List of cells that make up the tissue
	public static ParamVals params = new ParamVals();//parameters of the experiment 
	//cells may actually be spaces (type = 0)
	private Random rand = new Random();
	
	public CAGridStatic(int size,int maxit,int dsize) {// Create new instance of simulation with size of grid maximum CA cycle and fraction of stem cells 
        //uncomment for interactive setup params.SetParamVals();//set param vals using windows
 
		CABoxStatic[][] grid = new CABoxStatic[size][1];// Temporary 2D array to hold boxes in Cartesian grid so that connections can be made
		CACell cell;
		tissue = new ArrayList<CACell>();// Creates the list structure for the cells that constitute the tissue
		int lineage =0;
		int ttype;
		// Grid looped through and new CABox and CACell created for each element in the array
		for (int x = 0; x < size; x++) {
			//ttype=rand.nextInt(2);// Cell type set randomly to either 0 or 1
            ttype = 0;
            if ((x>=maxit) && (x < (maxit+dsize)))ttype = 1;
			grid[x][0] = new CABoxStatic(x,0);// New instance of CABox created and added to 2D grid
		    if (ttype == 1){
				lineage++;//lineage starts at 1
		    	cell = new CACell(grid[x][0],lineage);// New instance of CACell created and given unique lineage id
                cell.type = ttype;
		    	grid[x][0].occupant = cell;// The new cell is added to the CABox              
		    }
			else{
				cell = new CACell(grid[x][0],0);// New instance of CACell created and given unique lineage id
                cell.type = ttype;
				grid[x][0].occupant = cell;// The new cell is added to the CABox
			}
			tissue.add(cell);// Add new cell to list of cells that constitute the tissue
	}
		//System.out.println("max lineage "+lineage);
		//int sc = (int)(64*64*frac);  Calculate the number of SC required for this grid size for a given fraction
		for (int x = 0; x < size; x++) { //  Loop through all the boxes in the grid 
			//if (grid[x][0].occupant.type == 1) System.out.println("grid spot" + grid[x][0].x);
		        for (int xx = x - 1; xx <= x + 1; xx++) {
						if(x!=xx) // Form links with their 2 immediate neighbours
			            grid[x][0].addNeighbour(grid[bounds(xx,size)][0]);
						//Add left nbour then right nbour			        
			    }
			
	    } 
		
		savedx = new int[maxit];
		savedxs = new int[dsize][maxit];
		savedy = new int[maxit];
	}

	private int bounds(int a,int size) {  // Creates the toroidal links between top and bottom and left and right
		if (a < 0) return size + a;
		if (a >= size) return a - size;
		return a;
	}
	
	public void stain(){ // Stains all cells in the tissue list
		for (CACell c : tissue) {
		    if(c.type>0){
				c.stain=1.0;
			}
	    }
	}

   public void setNewGridParams(){
       params.SetParamVals();//set param vals using windows
       System.out.println("new param vals: "+params);
   }
	public void iterate() { // The main iterative loop of the simulation
        CACell cHold;
        ArrayList<CACell> moveArray = new ArrayList<CACell>(); // Create a list of cells
        for (CACell c : tissue) { // loop through the tissue
		    c.maintain(params); // Updates the status of the tissue
			if(c.type==1)moveArray.add(c); // If there is a cell, add to cell list
	    }
        //for debug System.out.println("yo "+moveArray.size());
		while(moveArray.size()>0){ // Randomly loop through the list of cells
			cHold=moveArray.remove(rand.nextInt(moveArray.size()));
			cHold.grow();// Test to see if the cell moves
		}
	}
	public int iterate(int iters) { // The main iterative loop of the simulation
        CACell cHold;
        ArrayList<CACell> moveArray = new ArrayList<CACell>(); // Create a list of cells
        for (CACell c : tissue) { // loop through the tissue
		    c.maintain(params); // Updates the status of the tissue
			if(c.type==1)moveArray.add(c); // If there is a cell, add to cell list
	    }
        //for debug System.out.println("yo "+moveArray.size());
		while(moveArray.size()>0){ // Randomly loop through the list of cells
			cHold=moveArray.remove(rand.nextInt(moveArray.size()));
			cHold.grow();// Test to see if the cell moves
		}
		iters++;
		return(iters);
	}
}
