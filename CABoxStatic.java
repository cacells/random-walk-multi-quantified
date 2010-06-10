/**
 * Defines properties of the part of the grid - box or home - for each cell.
 * 
 * @param rand type: Random instance
 * @param occupant a CACell - the occupant of this box
 * @param x an integer - the x position of this box
 */

import java.util.*;



public class CABoxStatic {
	public static Random rand = new Random();
	
	public CACell occupant; // The cell in the box
	public int x,y; // Cartesian cordinates of the box this is only used fro graphical display
	public ArrayList<CABoxStatic> neighbours;// list of neighbouring Boxes
	
	public CABoxStatic(int x, int y) { // Create new instance of the box
		this.x=x;
		this.y=y;
		neighbours  = new java.util.ArrayList<CABoxStatic>();
	}

	public CACell getNeighbour(int n){
		return neighbours.get(n).occupant;// Returns a neighbour of this box
	}

	public void addNeighbour(CABoxStatic newNeighbour) {
		this.neighbours.add(newNeighbour);// Adds neighbour to list (see CAGridStatic)
	}
}


