package modelisation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

class GraphImplicit implements Graph {
    int N;
    
    GraphImplicit(int N) {
    	this.N = N;
    }

    public int vertices() {
    	return N;
    }
    
    @SuppressWarnings("unchecked")    
    public Iterable<Edge> next(int v) {
    	ArrayList<Edge> edges = new ArrayList();
    	for (int i = v; i < N; i++) {
    		edges.add(new Edge(v,i,i));
    	}
    	return edges;      
	 }
    
    @SuppressWarnings("unchecked")
    public Iterable<Edge> prev(int v) {
    	ArrayList<Edge> edges = new ArrayList();
    	for (int i = 0; i < v-1; i++) {
    		edges.add(new Edge(i,v,v));
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
