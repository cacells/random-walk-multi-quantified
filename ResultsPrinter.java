import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import javax.swing.JOptionPane;


public class ResultsPrinter {
    CAStatic c;
    String EPSFilename = "file.eps";
    String baseFilename = "base";
    String latexFilename = "file.tex";
    String cssFilename = "file.css";
    String timeString = "00";
    static 	DecimalFormat twoPlaces = new DecimalFormat("0.00");
    File dir;
    static int resultcount = 0;
    static int maxdots = 50;//max size of histogram
    int sf = 1;
	public ResultsPrinter(CAStatic orig){
		c = orig;
		//set date and create directory?
	}
	
public void printLaTeX(int ind,boolean multi){
		try {
			int startind;
			int lastind;
			java.io.FileWriter file = new java.io.FileWriter(new File(dir,latexFilename));
			java.io.BufferedWriter buffer = new java.io.BufferedWriter(file);
			System.out.println(latexFilename);
			if (multi) {
				startind = 0;
				lastind = ind;
			}
			else {
				startind = ind;
				lastind = ind+1;
			}
			buffer.write("\\documentclass[11pt,a4paper]{article}\n");
			buffer.write("\n");
			buffer.write("\\usepackage{longtable}\n");
			buffer.write("\\newcommand \\bt{\\begin{longtable}{p{0.65\\textwidth}p{0.25\\textwidth}}}\n");
			buffer.write("\\newcommand \\et{\\end{longtable}}\n");
			buffer.write(" \n");
			buffer.write("\\usepackage[pdftex,usenames,dvipsnames]{color}\n");
			buffer.write("\\usepackage{graphicx,psfig,epsfig}\n");
			buffer.write("\\usepackage{epstopdf}\n");
			buffer.write("\n");
			buffer.write("\\definecolor{classbg}{rgb}{0.707,0.648,0.586}\n");
			buffer.write("\\definecolor{fieldbg}{rgb}{0.363,0.641,0.746}\n");
			buffer.write("\\definecolor{conbg}{rgb}{0.711,0.793,0.836}\n");
			buffer.write("\\definecolor{descriptbg}{rgb}{0.848,0.918,0.953}\n");
			buffer.write("\n");
			buffer.write("\\usepackage[T1]{fontenc}\n");
			buffer.write("\\renewcommand*\\familydefault{\\sfdefault}\n");
			buffer.write("\n");
			buffer.write("\\usepackage[hmargin=2.5cm,vmargin=2.5cm]{geometry}\n");
			buffer.write("\\setlength{\\parindent}{0.0\\textwidth}\n");
			buffer.write("\n");
			buffer.write("\\begin{document}\n");
			buffer.write("\n");
			buffer.write("\\section*{Results from 1D random walk}\n");
			buffer.write("Experiment run on "+timeString+"\\\\\\\\\\\n");
			buffer.write("\\colorbox{descriptbg}{\\large{Parameters}}\n");
			buffer.write("\\bt\n");
			buffer.write("Number of moves in each walk&"+c.maxit+"\\\\\n");
			buffer.write("Number of walks &"+c.runCount+"\\\\\n");
			buffer.write("Probability of moving right &"+CAGridStatic.params.pr+"\\\\\n");
			buffer.write("Probability of moving left &"+CAGridStatic.params.pl+"\n");
			buffer.write("\\et\n");
			buffer.write("\\colorbox{descriptbg}{\\large{Theoretical Values}}\n");
			buffer.write("\n");
			buffer.write("\\bt\n");
			buffer.write("expected distance travelled in "+c.maxit+" non-blocked moves &" +twoPlaces.format(c.runStats[0])+"\\\\\n");
			buffer.write("expected mean square distance for "+c.maxit+" non-blocked moves &"+ twoPlaces.format(c.runStats[1])+"\\\\\n");
			buffer.write("\\et\n");
			buffer.write("\\colorbox{descriptbg}{\\large{Measured Values}}\n");
			for (int i=startind;i<lastind;i++){
				buffer.write("\\subsection*{Cell "+i+"}\n");	
				buffer.write("\\bt\n");			
				buffer.write("average distance travelled in "+c.maxit+" moves &  " +twoPlaces.format(c.saved[i].cellStats[0])+"\\\\\n");
				buffer.write("average square distance for "+c.maxit+" moves & " +twoPlaces.format(c.saved[i].cellStats[1])+"\\\\\n");
				buffer.write("maximum frequency of distance & " + ((int)c.saved[i].cellStats[2])+"\\\\\n");
				buffer.write("pathways and frequency histogram & Figure \\ref{fig" +i+"}\n");
				buffer.write("\\et\n");
				//buffer.write("\\colorbox{descriptbg}{\\large{Visualisation of results}}\n");
				buffer.write("\\begin{figure}[htbp]\n");
				buffer.write("\\begin{center}\n");
				makeEPSFilename(i);
				buffer.write("\\includegraphics[width=0.8\\textwidth]{"+EPSFilename+"}\n");
				buffer.write("\\caption{Cell "+i+" pathways and frequency histogram. Upper panel shows cell pathways with vertical position representing timestep. \n");
				buffer.write("Lower panel shows the distribution of final (horizontal) cell positions. Each dot in the histogram represents ");
					if (sf > 1) buffer.write(sf+" results.}\n");
					else buffer.write(sf+" result.}\n");
				buffer.write("\\label{fig"+i+"}\n");
				buffer.write("\\end{center}\n");
				buffer.write("\\end{figure}\n");
				buffer.write("\n");
			}
			buffer.write("\n");
			buffer.write("\\end{document}\n");			
			buffer.close();
		} catch (java.io.IOException e) {
			System.out.println(e.toString());
		}
	}
	public void printCSS2col(){
        String fileName = cssFilename;
		try {
			java.io.FileWriter file = new java.io.FileWriter(fileName);
			java.io.BufferedWriter buffer = new java.io.BufferedWriter(file);
			System.out.println(fileName);
			buffer.write(".CHdr {\n");
			buffer.write("	background-color: #B4A595;\n");
			buffer.write("}\n");
			buffer.write(".FHdr {\n");
			buffer.write("	background-color: #5CA3BE;\n");
			buffer.write("}\n");
			buffer.write(".ConHdr {\n");
			buffer.write("	background-color: #B5CAD5;\n");
			buffer.write("}\n");
			buffer.write(".MHdr {\n");
			buffer.write("	background-color: #D8EAF3;\n");
			buffer.write("}\n");
			buffer.write("td {\n");
			buffer.write("vertical-align:top\n");
			buffer.write("}\n");
			buffer.write("td.FList {\n");
			buffer.write("width:40%\n");
			buffer.write("vertical-align:top;\n");
			buffer.write("}\n");
			buffer.write("td.ConList {\n");
			buffer.write("width:40%;\n");
			buffer.write("vertical-align:top;\n");
			buffer.write("}\n");
			buffer.write("td.MList {\n");
			buffer.write("width:40%;\n");
			buffer.write("vertical-align:top;\n");
			buffer.write("}\n");
			buffer.write("td.midcol{\n");
			buffer.write("width:20%;\n");
			buffer.write("}\n");
			buffer.write("td.col3 {\n");
			buffer.write("width:40%\n");
			buffer.write("}\n");
			buffer.write(".indent {\n");
			buffer.write("padding-left: 20px;\n");
			buffer.write("}\n");
			buffer.write("\n");
			buffer.write("td.hdr {\n");
			buffer.write("	padding-top: 10px;\n");
			buffer.write("}\n");
			buffer.write("td.ftr {\n");
			buffer.write("	padding-bottom: 10px;\n");
			buffer.write("}\n");
			buffer.write("\n");
			buffer.close();
		} catch (java.io.IOException e) {
			System.out.println(e.toString());
		}
	}

	public void makeEPSFilename(int ind){
		EPSFilename = baseFilename+"_"+ind+".eps";
	}
    public void findScaleFactor(int maxdCount){
        sf = (int) Math.ceil((double)maxdCount/(double) maxdots);
    }
	public void makeFilenames(){
		Date now = new Date();
        SimpleDateFormat format =
            new SimpleDateFormat("yyyyMMdd_HHmm");
        String datestr = format.format(now);
        SimpleDateFormat format2 =
            new SimpleDateFormat("d/M/yyyy 'at' HH.mm");
        timeString = format2.format(now);
		 //assume we have a baseDirname
		//make the htmldir
		dir = new File(datestr+"_"+resultcount);
		resultcount++;	
	    boolean success = dir.mkdir();
	    System.out.println("Directory: " + dir + " created"); 
		String probstring = CAGridStatic.params.filename();	  
		//postscriptPrint("CA"+iterations+"."+probstring+"."+epsCount+".eps");
		baseFilename = "CA"+c.maxit+"_"+probstring;
		EPSFilename = baseFilename+".eps";
	    latexFilename = baseFilename+"_results.tex";
	    cssFilename = baseFilename+".css";
	}
	public void printEPSDots(int ind) {

		int xx;
		int yy;
		int xsize = c.gSize*4;
		int dotspace = (int)Math.ceil((double)c.saved[ind].maxdCount/(double) sf);
		int ysize = dotspace*4 + 20;
		int ysize2 = (c.maxit-1)*4 +10;
		int dotfrac;
		int rem = 0;
		int todo = 0;
		xsize = xsize + 30;
		double[] col = new double[3];//tmp colour holder
		makeEPSFilename(ind);
		try {
			java.io.FileWriter file = new java.io.FileWriter(new File(dir,EPSFilename));
			java.io.BufferedWriter buffer = new java.io.BufferedWriter(file);
			System.out.println(EPSFilename);
			buffer.write("%!PS-Adobe-2.0 EPSF-2.0");
			buffer.newLine();
			buffer.write("%%Title: test.eps");
			buffer.newLine();
			buffer.write("%%Creator: gnuplot 4.2 patchlevel 4");
			buffer.newLine();
			buffer.write("%%CreationDate: Thu Jun  4 14:16:00 2009");
			buffer.newLine();
			buffer.write("%%DocumentFonts: (atend)");
			buffer.newLine();
			buffer.write("%%BoundingBox: 0 0 "+xsize+" "+(ysize+ysize2));
			buffer.newLine();
			buffer.write("%%EndComments");
			buffer.newLine();
			col = c.CApicture.EPScolor;
			buffer.write("/lg {"+col[0]+" "+col[1]+" "+col[2]+" setrgbcolor} bind def\n");
			for (xx = 0;xx < c.nnw+1; xx++){
                col = c.epsColours[xx];
                buffer.write("/sc"+xx+" {"+col[0]+" "+col[1]+" "+col[2]+" setrgbcolor} bind def\n");
			}
			buffer.write("/fillrect {/y2 exch def /x2 exch def /y1 exch def /x1 exch def newpath x1 y1 moveto x2 y1 lineto x2 y2 lineto x1 y2 lineto closepath fill} bind def\n");
			buffer.write("/drawrect {/y2 exch def /x2 exch def /y1 exch def /x1 exch def newpath x1 y1 moveto x2 y1 lineto x2 y2 lineto x1 y2 lineto closepath stroke} bind def\n");
			buffer.write("/dodot {/yval exch def /xval exch def newpath xval yval 1.5 0 360 arc fill} def\n");
			buffer.write("/dopartdot {/ang exch def /yval exch def /xval exch def newpath xval yval 1.5 0 ang arc fill} def\n");
			//for (CACell c : experiment.tissue){
			buffer.write("0 0 "+xsize+" "+(ysize+ysize2)+" drawrect\n");
			buffer.write("lg\n");
			buffer.write("5 5 "+(xsize-5)+" "+(ysize-5)+" fillrect\n");
			int a = (ind)%c.nnw+1;
			buffer.write("sc"+a+"\n");//colour 1 is red
			System.out.println("sf = "+sf);
			if (sf > 0){
			for (xx = 0; xx < c.gSize; xx++) {
				rem = c.saved[ind].dCount[xx]%sf;
				todo = (c.saved[ind].dCount[xx]-rem)/sf;
				for (yy = 0; yy < todo; yy++) {
					buffer.write( (xx*4+10) + " " + (ysize-yy*4-10) + " dodot\n");
				}
				//assume yy = todo. Do a part dot.
				if (rem > 0) {
					dotfrac = (rem*360)/sf;
					buffer.write( (xx*4+10) + " " + (ysize-yy*4-10) + " "+dotfrac+" dopartdot\n");
				}
			}
			}
            buffer.write("0 "+ysize+" translate\n");
            System.out.println("runCount from epsprint"+c.runCount);
			for (int i = 0; i < c.runCount; i++) {
				xx = c.saved[ind].posx[i][0];
				buffer.write("newpath \n"+ (xx*4+10) + " " + (ysize2-10) + " moveto\n");
				for (yy = 1; yy < c.maxit; yy++) {
					xx = c.saved[ind].posx[i][yy];
					buffer.write( (xx*4+10) + " " + (ysize2-yy*4-10) + " lineto\n");
				}
				buffer.write("stroke\n");
			}
			buffer.write("showpage");
			buffer.newLine();
			buffer.write("%%Trailer");
			buffer.newLine();
			buffer.write("%%DocumentFonts: Helvetica");
			buffer.newLine();
			buffer.close();
		} catch (java.io.IOException e) {
			System.out.println(e.toString());
		}
	}
}
