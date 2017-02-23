package modelisation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class GraphImplicit implements Graph {
    int N;
    int W,H;
    int[][] tabInterest;
    
    GraphImplicit(int N) {
    	this.N = N;
    }

    public GraphImplicit(int[][] interest, int w, int h){
    	this.N=h*w+2;
    	this.W=w;
    	this.H=h;
    	this.tabInterest = interest;
    }
    
    public int vertices() {
    	return N;
    }
    
    public Iterable<Edge> next(int v) {
    	ArrayList<Edge> edges = new ArrayList<Edge>();    	
    	if (v == vertices()-1){ // v == en haut
			for (int i = 0; i < W ; i++) {
	    		edges.add(new Edge(vertices()-1,i,0));
	    	}
    	} else {
	    	if (v<vertices()-W-2){ //v est avant la derniere ligne
					if(v%W==0){ // v == � gauche 
			    		edges.add(new Edge(v,(v+W),tabInterest[v/W][v%W]));
			    		edges.add(new Edge(v,(v+W+1),tabInterest[v/W][v%W]));    			
					}else{
						if (v%W==W-1){ //v == � droite
			        		edges.add(new Edge(v,(v+W),tabInterest[v/W][v%W]));
			        		edges.add(new Edge(v,(v+W-1),tabInterest[v/W][v%W]));   
						}else{ // v != extr�mit�		
		        			edges.add(new Edge(v,(v+W),tabInterest[v/W][v%W]));
		            		edges.add(new Edge(v,(v+W-1),tabInterest[v/W][v%W])); 
		            		edges.add(new Edge(v,(v+W+1),tabInterest[v/W][v%W]));  				        		
			        	}
					}	
				
	    	}else{ // v est sur la derniere ligne
	    		if (v<vertices()-2){
				edges.add(new Edge(v,vertices()-2,tabInterest[v/W][v%W]));
	    		}
			}
    		
		}
		
    	return edges;      
	 }

    
    public Iterable<Edge> prev(int v) {
    	ArrayList<Edge> edges = new ArrayList<Edge>();
    	if (v == vertices()-2){ // v == en bas
    		for (int i = 0; i < W ; i++) {
        		edges.add(new Edge(vertices()-2-W+i,v,tabInterest[v/W-1][(v-W+i)%W]));
        	}
    	}else{
			if (v>=W){ //v est apr�s la premiere ligne
				if(v%W==0){ // v == � gauche 
		    		edges.add(new Edge(v-W,v,tabInterest[v/W-1][v%W]));
		    		edges.add(new Edge(v-W+1,v,tabInterest[v/W-1][v%W+1]));    			
				}else{
					if (v%W==W-1){ //v == � droite
						edges.add(new Edge(v-W,v,tabInterest[(v/W)-1][v%W]));
			    		edges.add(new Edge(v-W-1,v,tabInterest[(v/W)-1][v%W-1]));    	 
					}else{
						if (v < vertices()-2){
							// v != extr�mit�		
		        			edges.add(new Edge(v-W-1,v,tabInterest[v/W-1][v%W-1]));
		            		edges.add(new Edge(v-W,v,tabInterest[v/W-1][v%W])); 
		            		edges.add(new Edge(v-W+1,v,tabInterest[v/W-1][v%W+1]));  
						}
					}
				} 
			}else{ // v est sur la premiere ligne
				edges.add(new Edge(vertices()-2+1,v,0));
			}
    	}
    	
    	return edges; 
	 }

	/* Fct venant de Graph */
	public void writeFile(String s) {
		try
		{			 
			PrintWriter writer = new PrintWriter(s, "UTF-8");
			writer.println("digraph G{");
			int u;
			int n = vertices();
			for (u = 0; u < n;  u++) {
			    for (Edge e: next(u)) {
			    	writer.println(e.from + "->" + e.to + "[label=\"" + e.cost + "\"];");
			    }
			}
			writer.println("}");
			writer.close();
		} catch (IOException e) {
			
		}						
	}
	
	
	/* A SUPPRIMER */
	public static int[][] interest(int[][] image) {
		   assert(image.length>1):"tableau trop petit";
		   int[][]tab=new int[image.length][image[0].length];
		   for (int j=0;j<image.length;j++){
			   tab[j][0]=Math.abs(image[j][0]-image[j][1]);
			   for (int i=1;i<image[0].length-1;i++){
				   int moyenne = (image[j][i-1] + image[j][i+1])/2;
				   tab[j][i]=Math.abs(moyenne-image[j][i]);
			   }
			   tab[j][image[0].length-1]=Math.abs(image[j][image[0].length-1]-image[j][image[0].length-2]);
		   }
		   
		   return tab;
	   }
	
	
	public static int[][] readpgm(String fn) {		
		try {
			InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
            String magic = d.readLine();
            String line = d.readLine();
            while (line.startsWith("#")) {
            	line = d.readLine();
            }
            Scanner s = new Scanner(line);
			int width = s.nextInt();
			int height = s.nextInt();		   
			line = d.readLine();
			s = new Scanner(line);
			int maxVal = s.nextInt();
			int[][] im = new int[height][width];
			s = new Scanner(d);
			int count = 0;
			while (count < height*width) {
				im[count / width][count % width] = s.nextInt();
				count++;
			}
			return im;
		}
		catch(Throwable t) {
			t.printStackTrace(System.err) ;
			return null;
		}
	}
	
	public static void main(String[] args) {
		int[][] test = readpgm("test.pgm");
		int[][] testInt = interest(test);
		GraphImplicit g = new GraphImplicit(testInt,testInt[0].length,testInt.length);
		g.writeFile("testImplicit.dot");
	}
}
