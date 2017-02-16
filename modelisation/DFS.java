package modelisation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Stack;

class DFS {
	
	/*
	 * Le problème de la fonction est le parcours des sommets. Celui-ci est effectué en largeur au lieu d'en profondeur.
	 * On a donc les sommets 1, 2 et 3 qui seront visité à la suite, alors qu'après la visite de 1, on devrait parcourir 4, 3 et 5. 
	 */
    public static void botched_dfs1(Graph g, int s) {
    	Stack<Integer> stack = new Stack<Integer>();
    	boolean visited[] = new boolean[g.vertices()];
    	stack.push(s);
    	visited[s] = true;	    	
    	while (!stack.empty()) {
    		int u = stack.pop();
    		System.out.println(u);
    		for (Edge e: g.next(u)) {
    			if (!visited[e.to]) {
    				visited[e.to] = true;
    				stack.push(e.to);
    			}
    		}
    	}
    }

   
    /*
     * L'affichage se situe au niveau du parcours des voisins de 0. Il affiche donc 1,2,3 
     * à la suite alors qu'il ne devrait pas les visiter dans cet ordre. On a donc encore un 
     * parcours en "largeur" et un soucis d'affichage.
     * 
     */
    public static void botched_dfs2(Graph g, int s) {
    	Stack<Integer> stack = new Stack<Integer>();
    	boolean visited[] = new boolean[g.vertices()];
    	stack.push(s);
    	System.out.println(s);
    	visited[s] = true;	    	
    	while (!stack.empty()) {
    		int u = stack.pop();
    		for (Edge e: g.next(u)) {
    			if (!visited[e.to]) {
    				System.out.println(e.to);			
    				visited[e.to] = true;
    				stack.push(e.to);
    			}
    		}
    	}
    }
    
    /*
     * La pile peut être plus grand que O(n) car la fonction peut ajouter des 
     * sommets non visités mais déja présent dans la pile.
     * 
     * Pour un graphe à 100 sommets, plus il y a d'arêtes entre les sommets plus il y a de chances que la pile
     * contienne plusieurs fois le meme sommet (exemple du graphe complet qui possède le plus d'arretes possibles).
     */
    public static void botched_dfs3(Graph g, int s) {
    	Stack<Integer> stack = new Stack<Integer>();
    	boolean visited[] = new boolean[g.vertices()];
    	stack.push(s);
    	while (!stack.empty()) {
    		int u = stack.pop();
    		if (!visited[u]) {
    			visited[u] = true;
    			System.out.println(u);
    			for (Edge e: g.next(u)) {
    				if (!visited[e.to]) {
    					stack.push(e.to);
    				}
    			}
    		}
	    }
	}

    /*
     * Le problème de dfs4 est qu'il repasse plusieurs fois sur les même arêtes pour verifier que le voisin a deja 
     * été visité. Ainsi, en arrivant au bout d'un "chemin", en retournant en arriere, il revérifie chaque arrête de chaque 
     * sommet dans tous les cas. Le break dans le "if (!visited[])" permet de visiter uniquement le premier voisin non visité
     * et donc effectue une vérification à la fois.
     * 
     * exemple des 100 sommets : si le sommet initial est relié à tous les autres, alors, lors du parcours de tous les voisins
     * du sommet initial, on parcoura autant de fois la premiere arête qu'il y a de sommets.
     */
    public static void botched_dfs4(Graph g, int s) {
    	Stack<Integer> stack = new Stack<Integer>();
    	boolean visited[] = new boolean[g.vertices()];
    	stack.push(s);
    	visited[s] = true;
    	System.out.println(s);
    	while (!stack.empty()) {
    		boolean end = true;
    		/* (a) Soit u le sommet en haut de la pile */
    		/* (b) Si u a un voisin non visité, alors */
    		/*     (c) on le visite et on l'ajoute sur la pile */
    		/* Sinon */
    		/*     (d) on enlève u de la pile */
	   
    		/* (a) */
		    int u = stack.peek();
		    for (Edge e: g.next(u)) {
				if (!visited[e.to]) { /* (b) */
					visited[e.to] = true;
					System.out.println(e.to);			
					stack.push(e.to); /*(c) */
					end = false;
					break;
				}
		    }
		    if (end) { /*(d)*/
		    	stack.pop();
		    }
		}
		System.out.println(stack.capacity());
    }
    
    

    
    public static void testGraph() {
    	//int n = 5;
		//int i,j;
    	
    	
		GraphArrayList g = new GraphArrayList(6);
		g.addEdge(new Edge(0, 1, 1));
		g.addEdge(new Edge(0, 2, 1));
		g.addEdge(new Edge(0, 3, 1));
		g.addEdge(new Edge(1, 4, 1));
		g.addEdge(new Edge(4, 3, 1));
		g.addEdge(new Edge(3, 5, 1));
		g.addEdge(new Edge(5, 1, 1));
		g.writeFile("h.dot");
		//botched_dfs1(g, 0);
		//botched_dfs2(g, 0);
		//botched_dfs3(g, 0);
		botched_dfs4(g, 0);
    }
    
    
    
    public static void main(String[] args) {
    	testGraph();
    }
}
