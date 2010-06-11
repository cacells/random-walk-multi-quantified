
public class Results {//saves and prints results from 1 run for multiple cells
	int[][] posx;
    int[] d;
    int[] dsq;
    int[] dCount;
    int mind,maxd;
    int maxdCount = 0;
    double[] cellStats = new double[3];
    int lineage = 1;

    Results(int maxRun,int iters,int lin,int gSize){//set up the results arrays
    	posx = new int[maxRun][iters];
    	d = new int[maxRun];
    	dsq = new int[maxRun];
    	dCount = new int[gSize];
    	lineage = lin;
    }
    public void setVals(int maxRun,int iters,int lin,int gSize){//set up the results arrays
    	System.out.println("gets to there ");
    	posx = new int[maxRun][iters];
    	d = new int[maxRun];
    	dsq = new int[maxRun];
    	dCount = new int[gSize];
    	lineage = lin;
    }
    public void setrunStats(int runCount,int maxit){//set up the results arrays
    	int lastpos = posx[runCount][maxit];
    	d[runCount] = lastpos;
    	dsq[runCount] = lastpos*lastpos;
    	dCount[lastpos]++;
    }
    public void calcStats(int runCount){//find stats at the end of the runs
    	int sumd = 0;
    	int sumdsq = 0;
    	int val,ind;
    	maxdCount = 0;
    	mind = 0;
    	maxd = dCount.length-1;
    	for (int i=0;i<dCount.length;i++) if (dCount[i] > maxdCount) maxdCount = dCount[i];
    	for (int i=0;i<runCount;i++){
    		ind = d[i];
    		val = ind - d[0];
    		sumd = sumd + val;
    		sumdsq = sumdsq + val*val;
			if (ind < mind) mind = ind;
			if (ind > maxd) maxd = ind;
    	}
    	if (runCount > 1){
		cellStats[0] = (double)sumd/(double)(runCount);//av d from this run
		cellStats[1] = ((double)sumdsq)/((double)(runCount));
    	}
		cellStats[2] = (double) maxdCount;
    }
    
}
