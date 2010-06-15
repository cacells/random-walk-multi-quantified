import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class ParamVals {
	double pr,pl,ps;
	static DecimalFormat twoPlaces = new DecimalFormat("0.00");
	static DecimalFormat asPercent = new DecimalFormat("00");
	static boolean nowrap = true;
	//default constructor
	public ParamVals(){
		pr = 0.4;
		pl = 0.4;
		ps = 0.2;
	}
	public void SetParamVals(){
		double tmpd;
		String firstNumber = JOptionPane.showInputDialog("Enter probability of moving right");
		double dpr = Double.parseDouble(firstNumber);
		pr = dpr;
		if ((pr < 0.0) || (pr > 1.0)){
			JOptionPane.showMessageDialog(null, "Probability must be between 0.0 and 1.0. It has been reset to 0.4");
			pr = 0.4;
		}
		tmpd = (1.0-pr);	
		firstNumber = JOptionPane.showInputDialog("Enter probability of moving left");
		dpr = Double.parseDouble(firstNumber);
		pl = dpr;
		if ((pl < 0.0) || (pl > tmpd)){
			pl = tmpd/2.0;
			JOptionPane.showMessageDialog(null, "Probability must be between 0.0 and "+twoPlaces.format(tmpd)+". It has been reset to "+twoPlaces.format(pl));
		}
		ps = 1.0 - pr - pl;
	}
	public void SetParamVals(double dpr,double dpl){
		//note: these are not checked for validity
		pr = dpr;
		pl = dpl;
		ps = 1.0 - dpr - dpl;
	}
	//constructor from buffered input
	public ParamVals(BufferedReader input){
		double tmpd;
		pr = 
			readdouble(input,"Enter probability (< 1.0) of moving right: ");
		if ((pr < 0.0) || (pr > 1.0)){
			System.out.println("Probability must be between 0.0 and 1.0. It has been reset to 0.4");
			pr = 0.4;
		}
		tmpd = (1.0-pr);
		pl = 
			readdouble(input,"Enter probability (< "+twoPlaces.format(tmpd)+") of moving left: ");
		if ((pl < 0.0) || (pl > tmpd)){
			System.out.println("probability must be < "+twoPlaces.format(tmpd)+"It has been reset to"+twoPlaces.format(tmpd/2.0));
			pl = tmpd/2.0;
		}
		ps = 1.0 - pr -pl;		
	}
	public int changeProbabilities(){
		double tmpd;
		String tmpstr;

		System.out.print("renew probabilities? (y/n): ");
		Scanner in = new Scanner(System.in);
		tmpstr = in.nextLine();
		int j = tmpstr.indexOf('y');
		if (j > -1){
			System.out.print("input pr pl separated by spaces: ");
			if (in.hasNextDouble()){
				pr = in.nextDouble();
				if ((pr < 0.0) || (pr > 1.0)){
					System.out.println("Probability must be between 0.0 and 1.0. It has been reset to 0.4");
					pr = 0.4;
				}
				tmpd = (1.0-pr);
				if (in.hasNextDouble()) pl = in.nextDouble();
				if ((pl < 0.0) || (pl > tmpd)){
					System.out.println("Probability must be < "+twoPlaces.format(tmpd)+" It has been reset to "+twoPlaces.format(tmpd/2.0));
					pl = tmpd/2.0;
				}
				ps = 1.0 - pr -pl;
			}
			System.out.println("param vals "+this);
			in.close();
			return -1;
		}
		else {
			System.out.println("param vals "+this);
			System.out.print("Press return to continue or x to finish: ");
			int i = in.nextLine().indexOf('x');
			in.close();
			return i;
		}
	    

	}
	public String toString(){return new String("p: "+twoPlaces.format(pr)+" q: "+twoPlaces.format(pl));}
	public String toString2(){return new String("pr: "+twoPlaces.format(pr)+" pl: "+twoPlaces.format(pl)+" ps: "+twoPlaces.format(ps));}
	public String filename(){return new String("R"+asPercent.format(pr*100)+"L"+asPercent.format(pl*100));}
	public static double readdouble(BufferedReader input,String question){
		System.out.print(question);
		String text;
		try {
			text = input.readLine();
			//System.out.println("spaceval"+text.indexOf(' '));
			//System.out.println("length"+text.length());
			Double d = new Double(text);
			return d.doubleValue();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0;
		}
	}
}
