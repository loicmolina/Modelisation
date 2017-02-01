package modelisation;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
public class SeamCarving {
	
	static boolean visite[];

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
	
	public static void writepgm(int[][] image, String filename) {
		assert(image.length>0):"Tableau vide, impossible de creer l'image :(";	
		try {
			/*int curseur=0,linesize=0;
		   	if (image.length>30){
			   linesize=30;
		   	}else{
			   linesize=image.length;
		   	}*/
			File f=new File(filename+".pgm");
			FileWriter fw=new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("P2");
			bw.newLine();
			bw.write(image[0].length+" "+image.length);
			bw.newLine();
			bw.write("255");
			bw.newLine();

			for (int j=0;j<image.length;j++) {
				for (int i=0;i<image[0].length;i++) {
					bw.write(image[j][i]+"\t");
					/*curseur++;
				   	if (linesize>0 && curseur==linesize){
					   bw.newLine();
					   curseur=0;
				   	}*/
				}
				bw.newLine();
				/*if(linesize==0){
				  
			   	}*/
			}
			bw.close();
			fw.close();
		}
		catch(Throwable t) {
			t.printStackTrace(System.err);
		}
	}
   
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

   public static Graph tograph(int[][] itr){
	   GraphArrayList graph=new GraphArrayList(itr.length*itr[0].length+2);
	   
	   for (int j=0;j<itr[0].length;j++){
		   graph.addEdge(new Edge(itr.length*itr[0].length+1,j,0));
	   }
	   
	   for (int j=0;j<itr.length-1;j++){
		   graph.addEdge(new Edge(j*itr[0].length,(j+1)*itr[0].length,itr[j][0]));
		   graph.addEdge(new Edge(j*itr[0].length,(j+1)*itr[0].length+1,itr[j][0]));
		   for (int i=1;i<itr[0].length-1;i++){
			   graph.addEdge(new Edge(i+j*itr[0].length,i-1+(j+1)*itr[0].length,itr[j][i]));
			   graph.addEdge(new Edge(i+j*itr[0].length,i+(j+1)*itr[0].length,itr[j][i]));
			   graph.addEdge(new Edge(i+j*itr[0].length,i+1+(j+1)*itr[0].length,itr[j][i]));
		   }
		   graph.addEdge(new Edge(itr.length+j*itr[0].length,itr.length+(j+1)*itr[0].length,itr[j][itr[0].length-1]));
		   graph.addEdge(new Edge(itr.length+j*itr[0].length,itr.length-1+(j+1)*itr[0].length,itr[j][itr[0].length-1]));
	   }
	   
	   
	   for (int j=0;j<itr[0].length;j++){
		   graph.addEdge(new Edge(itr[0].length*(itr.length-1)+j,itr.length*itr[0].length,itr[itr.length-1][j]));
	   }
	   
	   graph.writeFile("please.dot");
	   return graph;
	   
   }

   public static void dfs(Graph g, int u, ArrayList<Integer> ordre) {
		visite[u] = true;		
		for (Edge e: g.next(u)){
		  if (!visite[e.to]){
			dfs(g,e.to,ordre);
			ordre.add(e.to);
		  }
		}
   }
   
   public static ArrayList<Integer> tritopo(Graph g) {
	   ArrayList<Integer> ordre=new ArrayList<Integer>();
	   visite=new boolean[g.vertices()];
	   int u = g.vertices()-1;
	   dfs(g,u,ordre);
	   ordre.add(g.vertices()-1);
	   Collections.reverse(ordre);
	   
	   return ordre;
   }
   
   public static void main(String[] args) {
	   int[][] hello=readpgm("test.pgm");
	   hello=interest(hello);
	   tritopo(tograph(hello));
   }
}
