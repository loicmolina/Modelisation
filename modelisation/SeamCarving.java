package modelisation;

import java.util.ArrayList;
import java.util.Stack;
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
			File f=new File(filename);
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
	   int hauteur=itr.length;
	   int largeur = itr[0].length;
	   GraphArrayList graph=new GraphArrayList(hauteur*largeur+2);
	   for (int j=0;j<largeur;j++){
		   graph.addEdge(new Edge(hauteur*largeur+1,j,0));
	   }
	   
	   for (int j=0;j<hauteur-1;j++){
		   graph.addEdge(new Edge(j*largeur,(j+1)*largeur,itr[j][0]));
		   graph.addEdge(new Edge(j*largeur,(j+1)*largeur+1,itr[j][0]));
		   for (int i=1;i<largeur-1;i++){
			   graph.addEdge(new Edge(i+j*largeur,i-1+(j+1)*largeur,itr[j][i]));
			   graph.addEdge(new Edge(i+j*largeur,i+(j+1)*largeur,itr[j][i]));
			   graph.addEdge(new Edge(i+j*largeur,i+1+(j+1)*largeur,itr[j][i]));
		   }
		   graph.addEdge(new Edge(j*largeur+largeur-1,(j+1)*largeur+largeur-1,itr[j][largeur-1]));
		   graph.addEdge(new Edge(j*largeur+largeur-1,(j+1)*largeur+largeur-2,itr[j][largeur-1]));
	   }
	   
	   
	   for (int j=0;j<largeur;j++){
		   graph.addEdge(new Edge(largeur*(hauteur-1)+j,hauteur*largeur,itr[hauteur-1][j]));
	   }
	   
	   //graph.writeFile("please.dot");
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
   
   /*public static ArrayList<Integer> tritopo(Graph g) {
	   ArrayList<Integer> ordre=new ArrayList<Integer>();
	   visite=new boolean[g.vertices()];
	   int u = g.vertices()-1;
	   dfs(g,u,ordre);
	   ordre.add(g.vertices()-1);
	   Collections.reverse(ordre);

	   return ordre;
   }*/
   
   public static ArrayList<Integer> tritopo(Graph g) {
	   Stack<Integer> uStack = new Stack<Integer>();
	   Stack<Iterator> itStack = new Stack<Iterator>();
	   boolean visited[] = new boolean[g.vertices()];
	   int v;
	   ArrayList<Integer> suffixe = new ArrayList<Integer>();
	   
	   
	   /* Ajout de (s,next(s)) */
	   int s = g.vertices()-1;
	   uStack.push(s);
	   itStack.push(g.next(s).iterator());
	   
	   visited[s] = true;
	   
	   while (!uStack.empty() && !itStack.empty()) {
		   int u = uStack.peek();
		   Iterator<Edge> it = itStack.peek();
		   
		   if(it.hasNext()) {
			   v = it.next().to;
			   if(visited[v]) {
				   //break;
			   } else {
				   visited[v] = true;
				   uStack.push(v);
				   itStack.push(g.next(v).iterator());
			   }
		   } else {
			   uStack.pop();
			   itStack.pop();
			   suffixe.add(u);
		   }
	   }

	  Collections.reverse(suffixe);
	  //System.out.println(suffixe);
	  return suffixe;
   	}
   
   public static ArrayList<Integer> Bellman(Graph g,int s,int t,ArrayList<Integer> order){
	   ArrayList<Integer> ccm = new ArrayList<>();
 
	   int[] chemin = creerTabChemin(g,s,order);
	   ccm = creerListChemin(chemin,s,t);

	   return ccm;
   }
   
   public static int[] creerTabChemin(Graph g, int s, ArrayList<Integer> order) {
	   int[] tab = new int[order.size()];
	   int[] chemin = new int[order.size()];
	   
	   for(int i:order) {
		   tab[i]=9999;

		   if (i == s) {
			   tab[i]=0;
		   }
		   for(Edge e:g.prev(i)) {
			   if (tab[i] > tab[e.from]+e.cost) {
				   tab[i] = tab[e.from]+e.cost;
				   chemin[i] = e.from;
			   } 
		   }
	   }
	   
	   return chemin;
   }
   
   public static ArrayList<Integer> creerListChemin(int[] chemin, int s, int t) {
	   ArrayList<Integer> ccm = new ArrayList<>();
	   int k = t;
	   while (k != s) {
		   ccm.add(k);
		   k = chemin[k];
	   }
	   ccm.add(s);
	   
	   Collections.reverse(ccm);
	   
	   return ccm;
   }
   
   public static void Tab(int[][] tab) {
	   for (int y=0 ; y<tab.length ; y++) {
		   for (int x=0 ; x<tab[0].length ; x++) {
			   System.out.print(tab[y][x]+" ");
		   }
		   System.out.println("");
	   }
	   System.out.println("");
   }
   
   public static void deletePX(int nbDelete, String name, int[] posHG, int[] posBD, boolean priorite, boolean usage) {
	   if (!name.contains(".pgm")){
		   System.err.println("Le fichier n'est pas au bon format (.pgm)");
		   return;
	   }
	   int[][] tabImage = readpgm(name);
	   //int[][] tabInterImage = interest(tabImage);

	   Graph g;
	   ArrayList<Integer> ordre;
	   ArrayList<Integer> cheminMin;
	   
	   int largeur = tabImage[0].length;
	   int hauteur = tabImage.length;

	   int[][] nouvelleImage = new int[hauteur][largeur-nbDelete];
	   int j;
	   
	   for (int i=0 ; i<nbDelete ; i++) {

		   /* Tableau tmp au bonne dimmension et rempli */
		   int[][] tmpImage = new int[hauteur][largeur]; // les px
		   int[][] tmpInterImage = new int[hauteur][largeur]; // l'interet
		   
		   for (int y=0 ; y<hauteur ; y++) {
			   for (int x=0 ; x<largeur ; x++) {
				   tmpImage[y][x]=tabImage[y][x];
			   }
		   }
		   
		   /* Calcul sur le tableau tmp */
		   tmpInterImage = interest(tmpImage);
		   if(usage) {
			   prioriteSuppression(tmpInterImage,posHG,posBD,priorite);
		   }
		   g = tograph(tmpInterImage);
		   //g.writeFile("test.dot");
		   ordre = tritopo(g);
		   cheminMin = Bellman(g,g.vertices()-1,g.vertices()-2,ordre);
		   
		   /* Parcours et suppression des pixels inutiles */
		   for (int y=0 ; y<hauteur ; y++) {
			   j=0;
			   for (int x=0 ; x<largeur ; x++) {
				   int position = largeur * y + x;
				   if (position != cheminMin.get(y+1)) {
					   tabImage[y][j]=tabImage[y][x];
					   j++;
				   } 
			   }
		   }
		   
		   largeur--;
	   }
	   
	   /* On transfere l'image dans un tableau au bonne dimension */
	   for (int y=0 ; y<nouvelleImage.length ; y++) {
		   for (int x=0 ; x<nouvelleImage[0].length ; x++) {
			   nouvelleImage[y][x] = tabImage[y][x];
		   }
	   }
	   
		writepgm(nouvelleImage,"modif_image_"+name);
		
		System.out.println("Fichier pret : modif_image_"+name);
   }
   
   public static void prioriteSuppression(int[][] tab, int[] posHG, int[] posBD, boolean priorite) {
	   int valeur=0;
	   if (priorite) {
		   valeur=0;
	   } else {
		   valeur=9999999;
	   }
	   
	   for (int y=0 ; y<tab.length ; y++) {
		   for (int x=0 ; x<tab[0].length ; x++) {
			   if ((x>=posHG[0] && x<=posBD[0]) && (y>=posHG[1] && y<=posBD[1])) {
				   tab[y][x] = valeur;
			   }
		   }
	   }
   }
   
   public static void main(String[] args) {
	   String name = "friends.pgm";
	   int nbPixel = 200;
	   int[][] tab = readpgm(name);
	   tab = interest(tab);
	   //prioriteSuppression(tab,0,0,0,2,true);
	   Graph g = tograph(tab);	
	   tritopo(g);
	   
	   int[] posHG = new int[2];
	   int[] posBD = new int[2];
	   posHG[0] = 316;
	   posHG[1] = 160;
	   posBD[0] = 416;
	   posBD[1] = 386;
	   boolean priorite = false;
	   boolean usage = true;
	   
	   // nombre pixel à supprimer / nom / position haut gauche / position bas droite / priorite / usage
	   deletePX(nbPixel,name,posHG,posBD,priorite,usage);
	   
	   /*if (args.length!=2){
		   System.err.println("Utilisation : java -jar modelisation.jar NomDuFichier NombreDePixelASupprimer");
	   }else{
		   int nbPixel=Integer.parseInt(args[1]);
		   String nom=args[0];
		   deletePX(nbPixel,nom);
	   }*/
   }
}
