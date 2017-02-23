package modelisation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
					if(v%W==0){ // v == gauche 
			    		edges.add(new Edge(v,(v+W),tabInterest[v/W][v%W]));
			    		edges.add(new Edge(v,(v+W+1),tabInterest[v/W][v%W]));    			
					}else{
						if (v%W==W-1){ //v == droite
			        		edges.add(new Edge(v,(v+W),tabInterest[v/W][v%W]));
			        		edges.add(new Edge(v,(v+W-1),tabInterest[v/W][v%W]));   
						}else{ // v != extremite		
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
			if (v>=W){ //v est apres la premiere ligne
				if(v%W==0){ // v == gauche 
		    		edges.add(new Edge(v-W,v,tabInterest[v/W-1][v%W]));
		    		edges.add(new Edge(v-W+1,v,tabInterest[v/W-1][v%W+1]));    			
				}else{
					if (v%W==W-1){ //v == droite
						edges.add(new Edge(v-W,v,tabInterest[(v/W)-1][v%W]));
			    		edges.add(new Edge(v-W-1,v,tabInterest[(v/W)-1][v%W-1]));    	 
					}else{
						if (v < vertices()-2){
							// v != extremite	
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
	
	
}
