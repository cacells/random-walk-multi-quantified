/*
 * This is a class that will call the CAGrid simulation and will display the results 
 * graphically in a window for the CA version of the model
 */


import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.io.IOException;

import java.text.DecimalFormat;
import java.util.*;
 
public class CAStatic extends JFrame implements Runnable, ActionListener {

    CAGridStatic experiment;
    //int[][] savedvals;
    int maxRun =100;
	int maxit = 30;
    //int[] savedd;// = new int[maxRun];
    //int[] saveddsq;
    //int[] dCount;
    int runCount = 0;
    int epsCount = 0;
    int newframe = 0;
    Random rand = new Random();    
	volatile Thread runner;
	Image backImg1;
	Graphics backGr1;
	CAImagePanel CApicture;
	//CAImagePanel CApicture2;
	JButton startBtn,writeBtn,paramsBtn,wrapBtn;
	JTextArea msgBtn;
	JPanel buttonHolder;
	int iterations;
	int scale = 20;
	int gSize;
	int dsize = 1;
	int maxCellType;
	int maxdCount = 0;
	int lastDrawn = 0;
	//int lin = 1;
	//int linx = maxit + 1;

	boolean started = false;
    Colour palette = new Colour();
	int[] colorindices = {0,1,2,54,4,5};
	int nnw = colorindices.length-1;
//    Color[] colours = {Color.white,Color.black,Color.green,Color.blue,Color.yellow,Color.red,Color.pink};
    Color[] javaColours;
    double[][] epsColours;
    String EPSFilename = "file.eps";
    int rowstoDraw = 60;
    ResultsPrinter outPrinter;
    double[] runStats = new double[2];
    Results[] saved;
    
    
	public CAStatic(int size) {

		//size is the size of the area containing cells
		dsize = size;
	    gSize=size+2*maxit;

	    int wscale = 6;//scale for main panel
	    int btnHeight = 480-384;//found by trial and error - must be a better way!

		//experiment = new CAGridStatic(size, maxC);
	    //int tint = (int)Math.ceil((double)(400*maxit)/(double)gSize)+(480-384);
	    //int tint = (int)Math.ceil((double)(400*(50+maxit))/(double)gSize)+(480-384);
	    int tint = (maxit + rowstoDraw)*wscale + btnHeight;
	    //only going to show up to 50 dots in lower panel
		//add 20 to x and 60 to y bcos not printing onto the full frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container mainWindow = getContentPane();
		mainWindow.setLayout(new BorderLayout());
		setSize(gSize*wscale,tint);

		buttonHolder = new JPanel();
		buttonHolder.setLayout(new GridLayout(2,2));
  
		
	    SpinnerNumberModel model3 = new SpinnerNumberModel(50, 0, 100, 5);
	    JSpinner spinner3 = new JSpinner(model3);
        writeBtn = new JButton("Output Results to file");
        writeBtn.addActionListener(this);
 	
        startBtn = new JButton("Start");
        startBtn.addActionListener(this);  
        
        paramsBtn = new JButton("Set Probabilities");
        paramsBtn.addActionListener(this);
        
        wrapBtn = new JButton("Toggle wrap");
        wrapBtn.addActionListener(this);
        
        buttonHolder.add(writeBtn);
		writeBtn.setVisible(false);
        buttonHolder.add(paramsBtn);
        buttonHolder.add(wrapBtn);
        buttonHolder.add(startBtn);
        
        mainWindow.add(buttonHolder,BorderLayout.SOUTH);
		
        msgBtn = new JTextArea(" Default Parameter Values: "+CAGridStatic.params);
        msgBtn.setEditable(false);

        
	    mainWindow.add(msgBtn,BorderLayout.NORTH);
	    
        CApicture = new CAImagePanel();
        CApicture.rowstoShow = rowstoDraw;
        mainWindow.add(CApicture,BorderLayout.CENTER);

        
		//not here - doesn't work: CApicture.setScale(gSize,maxit,scale);


//pack();

 
		setVisible(true);
		setpalette();
		iterations = 0;
		outPrinter = new ResultsPrinter(this);

		
	}
	
    public void setpalette(){
    	int ind = colorindices.length;
    	javaColours = new Color[ind];
    	epsColours = new double[ind][3];
    	for (int i=0;i<ind;i++){
    		System.out.println("color index "+colorindices[i]);
    		javaColours[i] = palette.chooseJavaColour(colorindices[i]);
    		epsColours[i] = palette.chooseEPSColour(colorindices[i]);
    	}
    }

	
	public void saveCA() {
		int a;
		//iterations should be correct to use as array ref
		//backGr1.fillRect(0, 0, this.getSize().width, this.getSize().height);
		for (CACell c : experiment.tissue){
			a = c.lineage - 1;//lineage is 1 based
			//savedvals[xv][yv] = a;
			if (c.type == 1){
				saved[a].posx[runCount][iterations] = c.home.x;
			}
		}
	}

	
	public void saverunStats() {
        for (int i=0;i<experiment.maxlineage;i++) saved[i].setrunStats(runCount, maxit-1);
		drawCount(dsize-1);//well, you can only draw 1 of them
	}
	
	public void showStats(){
		DecimalFormat twoPlaces = new DecimalFormat("0.00");
		double p,q;
        maxdCount = 0;
        Results r;
        p = CAGridStatic.params.pr;
        q = CAGridStatic.params.pl;

		runStats[0] = maxit*(p-q);//expected d
		runStats[1] = (maxit*(p+q) + maxit*(maxit-1)*Math.pow(p-q, 2.0));//expected dsq
		System.out.println("expected d: "+ twoPlaces.format(runStats[0]) +" expected dsq " + twoPlaces.format(runStats[1]));

        for (int i=0;i<experiment.maxlineage;i++) {
        	r = saved[i];
        	r.calcStats(runCount);
        	
		System.out.println("cell "+r.lineage+" av d: "+twoPlaces.format(r.cellStats[0])+" av d sq "+twoPlaces.format(r.cellStats[1]));
		System.out.println("range of d: "+ r.mind + " to " + r.maxd);
		System.out.println("maxdCount " + r.maxdCount);
        }
		System.out.println("runCount = "+runCount);
	   /* for debug java.io.FileWriter file;
		try {
			file = new java.io.FileWriter("stuff.dat");
			java.io.BufferedWriter buffer = new java.io.BufferedWriter(file);
			for (int i=0;i<runCount;i++){
				buffer.write(savedd[i]+" "+saveddsq[i]+"\n");
			}
			buffer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}
	
	public void outputEPS(){
	  epsCount++;
	  //String probstring = CAGridStatic.params.filename();	  
	  //postscriptPrint("CA"+iterations+"."+probstring+"."+epsCount+".eps");
	  //EPSFilename = "CA"+maxit+"_"+probstring+"_"+epsCount+".eps";
	  outPrinter.makeFilenames();
	  outPrinter.printEPSDots(dsize-1);
	  outPrinter.printLaTeX(dsize-1);
	}
	
	public void changeParameters(){
		CAGridStatic.params.SetParamVals();
	    System.out.println("param vals: "+CAGridStatic.params);
	    msgBtn.setText(" Parameter Values: "+CAGridStatic.params);
	}
	
	public void changeWrap(){
		CAGridStatic.params.nowrap = !CAGridStatic.params.nowrap;
	}
	
	public void drawCA() {
        int a;
      	//CApicture.clearCAPanel();
		for (CACell c : experiment.tissue){
			a = c.lineage;
			if (a > 0) a = (a-1)%nnw+1;
			//if(a<7){
				CApicture.drawCircleAt(c.home.x,iterations,javaColours[a]);
			//}else{
				//CApicture.drawCircleAt(c.home.x,iterations,Color.orange);
			//}
		}
	    CApicture.updateGraphic();
	}
	
	public void drawCount(int ind) {
		int xv = saved[ind].d[runCount];
		int a = saved[ind].lineage;
		a = (a-1)%nnw+1;
		CApicture.drawCircleAt(xv,saved[ind].dCount[xv],javaColours[a],2);
	    CApicture.updateGraphic();
	}


	public void drawLines(int it,int runnum){
		int a;
		for (int c = 0;c<experiment.maxlineage;c++){
			a = saved[c].lineage;
			a = (a-1)%nnw+1;
		for (int i = lastDrawn; i < runnum; i++) {
			for (int yy = 1; yy < it; yy++) {
				CApicture.drawALine(saved[c].posx[i][yy-1],yy-1,
						saved[c].posx[i][yy],yy,javaColours[a]);
			}
		}
		}
		lastDrawn = runCount;
	    CApicture.updateGraphic();
	}
	
	public void start() {
		initialise();
		lastDrawn = 0;
        started = true;
		if (runner == null) {
			runner = new Thread(this);
		}
		runner.start();//not the same as this method!
		startBtn.setText("Stop");
		writeBtn.setVisible(false);
		paramsBtn.setVisible(false);
	}
	public void stop(){
		started = false;
		runner = null;

		startBtn.setText("Start");
		writeBtn.setVisible(true);
		paramsBtn.setVisible(true);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == startBtn){
			if(startBtn.getText()=="Start"){
				if (started) {
//					paramsBtn.setText("change parameters and rerun");
//					started = false;
				}
				start();
			}else{
				stop();

			}	
			}
		else if(e.getSource() == writeBtn){
             outputEPS();	
			}
		else if(e.getSource() == paramsBtn){
			changeParameters();
			start();

			}
		else if(e.getSource() == wrapBtn){
			changeWrap();
		}
	}
	
	public void run() {
		int tmprCount = 0;
		boolean running = true;
		for (runCount=0;runCount<maxRun;runCount++){
			running = (runner == Thread.currentThread());
			if (running){
				if (runCount > 0) reset();
				saveCA();
				iterations++;
				while ((iterations < maxit)) {
					experiment.iterate();
					saveCA();
					//drawCA();
					//if ((runCount%10) == 0) 
					//drawLines();
					iterations++;
					//newframe = 0;		
					//while(newframe<500000) newframe++;
					//if((iterations%5)==0)postscriptPrint("CA"+iterations+".eps");
					// This will produce a postscript output of the tissue
				}
				tmprCount = runCount;
				saverunStats();
				if ((runCount%25) == 0) drawLines(maxit,runCount);
			}

		}
		//this will print out aborted results
		//to stop that check if maxit was achieved
		if (!started) runCount = tmprCount+1;//just in case stop was pressed
		//iterations should anyway be equal to maxit
		showStats();
		stop();
	}


	public void initialise(){
		int stuffind = 0;

		    runCount = 0;

		    int a;

			experiment = new CAGridStatic(gSize,maxit,dsize);

			CApicture.setScale(gSize,maxit,scale,gSize,rowstoDraw,scale);

      	    CApicture.clearCAPanel(1);

      	    CApicture.clearCAPanel(2);

		    iterations=0;

		    saved = new Results[experiment.maxlineage];

		    for (CACell c:experiment.tissue){

		    	a = c.lineage;

                if (a > 0 ){
				    saved[a-1] = new Results(maxRun,maxit,a, gSize);

                }
			}

	}
	public void reset(){
        experiment.reSet(maxit,dsize);
		iterations=0;

	}
    
	public static void main(String args[]) {


		double initalSeed = 0.1;
		if(args.length>0){
			initalSeed = Double.parseDouble(args[0]);
			CAStatic s = new CAStatic(1);
/*	        s.initialise();
			s.start();*/
		}else{
			CAStatic s = new CAStatic(3);
/*	        s.initialise();
			s.start();*/
			System.out.println("finished");//one thread gets here
		}
	}


	
	}



