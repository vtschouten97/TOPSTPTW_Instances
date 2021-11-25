import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public class InstanceConverter {
	
	public InstanceConverter() {
		
	}
	
	public void convert(String folderLocation) {
		System.out.printf("\nConverting TSP instances... \n");
		long startTime = System.currentTimeMillis();
		File folder = new File(folderLocation);
		File[] listOfFiles = folder.listFiles();
		
		for (File file : listOfFiles) {
			try {
				Scanner in = new Scanner(file);
				Random rng = new Random(843621795);
				int yMax = Integer.MIN_VALUE;
	            int xMax = Integer.MIN_VALUE;
	            int yMin = Integer.MAX_VALUE; 
	            int xMin = Integer.MAX_VALUE;
	            int nNodes = 0;
	            int maxT = 0;
	            
	            if (file.getName().contains("100")) 
					nNodes = 100;
	            else 
	            	nNodes = 200;

	            int x[] = new int[nNodes];
	            int y[] = new int[nNodes];
	            
	            while (in.hasNext()) {
	            	String line= in.nextLine();
	                String[] values = line.split("\\s+");
	                if (values[0].equals("NAME:") || 
	                		values[0].equals("TYPE:") || 
	                		values[0].equals("COMMENT:") || 
	                		values[0].equals("DIMENSION:") || 
	                		values[0].equals("EDGE_WEIGHT_TYPE") || 
	                		values[0].equals("NODE_COORD_SECTION"))
	                	continue;
	                else if (values[0].equals("EOF"))
	                	break;
	                else {
	                	int index = Integer.parseInt(values[0]) - 1;
	                	int xVal = Integer.parseInt(values[1]);
	                	int yVal = Integer.parseInt(values[2]);
	                	x[index] = xVal;
	                	y[index] = yVal;
	                }
	            }
	            
	            for (int i = 0; i <= 3; i++) {
	            	yMax = Integer.MIN_VALUE;
		            xMax = Integer.MIN_VALUE;
		            yMin = Integer.MAX_VALUE; 
		            xMin = Integer.MAX_VALUE;
            		for (int k = 0; k < nNodes - 25*i; k++) {
            			int xVal = x[k];
	                	int yVal = y[k];
	                	if (xVal < xMin)
	                		xMin = xVal;
	                	if (xVal > xMax)
	                		xMax = xVal;
	                	if (yVal < yMin)
	                		yMin = yVal;
	                	if (yVal > yMax)
	                		yMax = yVal;
            		}
	            	for (int p = 2; p <= 4; p++) {
	            		try {
		            		FileOutputStream outputStream = new FileOutputStream("data/TOPSTPTW/Large/"+nNodes+"/"+file.getName().substring(0,4)+ "" +(nNodes-25*i)+ "-"+p+".txt");
		            		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
		                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		                    bufferedWriter.write("TYPE: TOPSTPTW");
		                    bufferedWriter.newLine();
		                    bufferedWriter.write("NODES: " +(nNodes-25*i));
		                    bufferedWriter.newLine();
		                    bufferedWriter.write("TOURS: " +p);
		                    bufferedWriter.newLine();
		                    maxT = (int) Math.floor(2.5*(xMax - xMin + yMax - yMin)/p);
		                    bufferedWriter.write("T_MAX: " +maxT);
		                    bufferedWriter.newLine();
		                    bufferedWriter.write("ID X Y PROFIT BETA windowStart windowEnd");
		                    bufferedWriter.newLine();
		                    bufferedWriter.write("1 " +x[0]+ " " +y[0]+ " 0 0 0 "+maxT);
		                    bufferedWriter.newLine();
			            	for (int k = 1; k < nNodes - 25*i; k++) {
			            		int profit = 10 + (x[k] + y[k])%90;
			            		double beta = (10 + (((x[k] + y[k])%20)/20.0)*90)/20000.0;
			            		int windowStart = rng.nextInt((int) Math.floor(maxT/2.0));
			            		int windowEnd = windowStart + rng.nextInt(Math.min(windowStart + (int) Math.floor(2*maxT/3.0),maxT)-windowStart);
			            		if (beta < 0.0005 || beta > 0.005)
			            			System.out.println(profit);
			            		bufferedWriter.write(	(k+1)+ " " 
			            								+x[k]+ " " 
			            								+y[k]+ " "
			            								+profit+ " "
			            								+String.format(Locale.US,"%.6f",beta)+ " "
			            								+windowStart+ " "
			            								+windowEnd);
			                    bufferedWriter.newLine();
			            	}
		                    bufferedWriter.close();
		            	} catch (IOException e) {
	    	                e.printStackTrace();
	    	            }
	            	}
	            } 
	            
	            if (file.getName().equals("kroA100.tsp") || file.getName().equals("kroB100.tsp")) {
	            	int tValsOneTour[] = new int[] {5000,7500,10000,12500};
		            int tValsMultipleTours[] = new int[]{5000,7000,9000};
		            nNodes = 15;
	            	for (int i = 0; i <= 2; i++) {
		            	for (int p = 1; p <= 3; p++) {
		            		int tValsIter = tValsMultipleTours.length;
		            		if (p == 1)
		            			tValsIter = tValsOneTour.length;
		            		for (int t = 0; t < tValsIter; t++) {
		            			try {
		            				maxT = tValsOneTour[t];
				                   	if (p > 1)
				                   		maxT = tValsMultipleTours[t];
				            		FileOutputStream outputStream = new FileOutputStream("data/TOPSTPTW/Small/"+file.getName().substring(0,4)+ "" +(nNodes-5*i)+ "-"+p+"-"+maxT+".txt");
				            		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
				                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
				                    bufferedWriter.write("TYPE: TOPSTPTW");
				                    bufferedWriter.newLine();
				                    bufferedWriter.write("NODES: " +(nNodes-5*i));
				                    bufferedWriter.newLine();
				                    bufferedWriter.write("TOURS: " +p);
				                    bufferedWriter.newLine();
				                    bufferedWriter.write("T_MAX: " +maxT);
				                    bufferedWriter.newLine();
				                    bufferedWriter.write("ID X Y PROFIT BETA windowStart windowEnd");
				                    bufferedWriter.newLine();
				                    bufferedWriter.write("1 " +x[0]+ " " +y[0]+ " 0 0 0 "+maxT);
				                    bufferedWriter.newLine();
					            	for (int k = 1; k < nNodes - 5*i; k++) {
					            		int profit = 10 + (x[k] + y[k])%90;
					            		double beta = (10 + (((x[k] + y[k])%20)/20.0)*90)/20000.0;
					            		int windowStart = rng.nextInt((int) Math.floor(maxT/2.0));
					            		int windowEnd = windowStart + rng.nextInt(Math.min(windowStart + (int) Math.floor(2*maxT/3.0),maxT)-windowStart);
					            		if (beta < 0.0005 || beta > 0.005)
					            			System.out.println(profit);
					            		bufferedWriter.write(	(k+1)+ " " 
					            								+x[k]+ " " 
					            								+y[k]+ " "
					            								+profit+ " "
					            								+String.format(Locale.US,"%.6f",beta)+ " "
					            								+windowStart+ " "
					            								+windowEnd);
					                    bufferedWriter.newLine();
					            	}
				                    bufferedWriter.close();
				            	} catch (IOException e) {
			    	                e.printStackTrace();
			    	            }
		            		}
		            	}
		            }
	            }
	            in.close();
			} catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
		}
		System.out.printf("TOPSTPTW instances generated in %.2f seconds!\n\n", (double) (System.currentTimeMillis() - startTime)/1000);
	}
}
