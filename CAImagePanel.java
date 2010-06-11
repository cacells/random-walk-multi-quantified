
import java.awt.*;

import javax.swing.*;


class CAImagePanel extends JPanel {

	    Image topImg;
	    Image botImg;//second image
	    Graphics backGr,backGr2;
		int columns, rows,columns2,rows2;
		int xScale, yScale,xScale2,yScale2;
		BasicStroke wideStroke = new BasicStroke(3.0f);
		Graphics2D g2;
		float frac = 1.0f;
		boolean twoImage = false;
		int rowstoShow = 100;
		int cind = 7;
		Colour tcolor = new Colour();
		Color Jcolor = tcolor.chooseJavaColour(cind);
		double[] EPScolor = tcolor.chooseEPSColour(cind);

		public void setScale(int noColumns,int noRows, int scale)//just one image
		{
			columns = noColumns;
			rows = noRows;
			xScale = scale;
			yScale = scale;
			//System.out.println("here"+xScale*columns+" "+yScale*rows);
			topImg= createImage(xScale*columns,yScale*rows);
			//topImg= createImage(20*64,2000);
			//System.out.println("here"+topImg);
			backGr= topImg.getGraphics();
			g2 = (Graphics2D) backGr;
			g2.setStroke(wideStroke);
		}
		public void setScale(int noColumns,int noRows,int scale,int noColumns2,int noRows2, int scale2)
		{
			columns = noColumns;
			rows = noRows;
			xScale = scale;
			yScale = scale;
			topImg= createImage(xScale*columns,yScale*rows);
			backGr= topImg.getGraphics();
			g2 = (Graphics2D) backGr;
			g2.setStroke(wideStroke);
			//now for the second image
			columns2 = noColumns2;
			rows2 = noRows2;
			xScale2 = scale2;
			yScale2 = scale2;
			botImg= createImage(xScale2*columns2,yScale2*rows2);
			backGr2= botImg.getGraphics();
			//frac = (float)(xScale*rows)/(float)(xScale*rows + xScale2*rows2);
			frac = (float)(xScale*rows)/(float)(xScale*rows + xScale2*rowstoShow);
			twoImage = true;
		}
		
		public void clearCAPanel()
		{
				backGr.setColor(Color.orange);
				backGr.fillRect(0,0,this.columns*xScale,rows*yScale);
				//backGr.fillRect(0,0,20*64,2000);
		}
		public void clearCAPanel(int panelNum)
		{

			if (panelNum == 1){
				backGr.setColor(Color.white);
				backGr.fillRect(0,0,xScale*columns,yScale*rows);
			}
			if (panelNum == 2){
				backGr2.setColor(Jcolor);
				backGr2.fillRect(0,0,xScale2*columns2,yScale2*rows2);
			}
		}
		
		public void drawCircleAt(int x, int y, Color colour)
		{
			backGr.setColor(colour);
			backGr.fillOval(x*xScale,y*yScale,xScale,yScale);
		}
		public void drawCircleAt(int x, int y, Color colour,int panelNum)
		{
			if (panelNum == 1){
			backGr.setColor(colour);
			backGr.fillOval(x*xScale,y*yScale,xScale,yScale);
			}
			if (panelNum == 2){
				backGr2.setColor(colour);
				backGr2.fillOval(x*xScale2,y*yScale2,xScale2,yScale2);
			}
		}
		
		public void drawALine(int x1, int y1, int x2, int y2,Color colour)
		{
			backGr.setColor(colour);
			backGr.drawLine(x1*xScale,y1*yScale,x2*xScale,y2*yScale);
		}
		
		public void drawALine(int x1, int y1, int x2, int y2,Color colour,int panelNum)
		{
			if (panelNum == 1){
			backGr.setColor(colour);
			backGr.drawLine(x1*xScale,y1*yScale,x2*xScale,y2*yScale);
			}
			if (panelNum == 2){
				backGr2.setColor(colour);
				backGr2.drawLine(x1*xScale2,y1*yScale2,x2*xScale2,y2*yScale2);
			}
		}

		public void updateGraphic() {
		        Graphics g = this.getGraphics();
		        if ((topImg != null) && (g != null)) {		        	
		            g.drawImage(topImg, 0, 0, this.getSize().width, (int)(((float)this.getSize().height)*frac), 0, 0, (int) (xScale * (columns)), (int) (yScale * (rows)), this);
		            if (twoImage)
		            	g.drawImage(botImg, 0, (int)(((float)this.getSize().height)*frac), this.getSize().width, this.getSize().height, 0, 0, (int) (xScale2 * (columns2)), (int) (yScale2 * (rows2)), this);
	            	g.drawImage(botImg, 0, (int)(((float)this.getSize().height)*frac), this.getSize().width, this.getSize().height, 0, 0, (int) (xScale2 * (columns2)), (int) (yScale2 * (rowstoShow)), this);
		        }
		    }

		    @Override
		    public void paintComponent(Graphics g) {
		        updateGraphic();
		    }

		    @Override
		    public void paint(Graphics g) {
		        updateGraphic();
		    }

	}