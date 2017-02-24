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
			System.out.println("Fichier pret : "+filename);
			bw.close();
			fw.close();
		}
		catch(Throwable t) {
			t.printStackTrace(System.err);
		}
	}
   
	public static int[][] interestVertical(int[][] image) {
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
	
	public static int[][] interestHorizontal(int[][] image) {
		   assert(image.length>1):"tableau trop petit";
		   int[][]tab=new int[image.length][image[0].length];
		   for (int i=0;i<image[0].length;i++){
			  tab[0][i]=Math.abs(image[0][i]-image[1][i]);
			   for (int j=1;j<image.length-1;j++){
			   
				   int moyenne = (image[j-1][i] + image[j+1][i])/2;
				   tab[j][i]=Math.abs(moyenne-image[j][i]);
			   }
			   tab[image.length-1][i]=Math.abs(image[image.length-1][i]-image[image.length-2][i]);
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
	   return graph;	   
   }
   
   public static Graph tographHorizontal(int[][] itr){
	   int hauteur=itr.length;
	   int largeur = itr[0].length;
	   GraphArrayList graph=new GraphArrayList(hauteur*largeur+2);
	   for (int i=0;i<hauteur;i++){
		   graph.addEdge(new Edge(hauteur*largeur+1,i,0));
	   }
	   
	   for (int i=0;i<largeur-1;i++){
		   graph.addEdge(new Edge(i*hauteur , (i+1)*hauteur , itr[0][i]));
		   graph.addEdge(new Edge(i*hauteur , (i+1)*hauteur+1 , itr[0][i]));
		   for (int j=1;j<hauteur-1;j++){
			   graph.addEdge(new Edge(j+i*hauteur , j-1+(i+1)*hauteur , itr[j][i]));
			   graph.addEdge(new Edge(j+i*hauteur , j+(i+1)*hauteur , itr[j][i]));
			   graph.addEdge(new Edge(j+i*hauteur , j+1+(i+1)*hauteur , itr[j][i]));
		   }
		   graph.addEdge(new Edge(i*hauteur+hauteur-1 , (i+1)*hauteur+hauteur-1 , itr[hauteur-1][i]));
		   graph.addEdge(new Edge(i*hauteur+hauteur-1 , (i+1)*hauteur+hauteur-2 , itr[hauteur-1][i]));
	   }
	   
	   
	   for (int i=0;i<hauteur;i++){
		   graph.addEdge(new Edge(hauteur*(largeur-1)+i , largeur*hauteur , itr[i][largeur-1]));
	   }
	   	   
	   return graph;	   
   }
   
   public static Graph tographEA(int[][] itr){
	   int hauteur=itr.length;
	   int largeur = itr[0].length;
	   GraphArrayList graph=new GraphArrayList(hauteur*largeur+2);
	   for (int j=0;j<largeur;j++){
		   graph.addEdge(new Edge(hauteur*largeur+1,j,0));
	   }
	   
	   for (int j=0;j<hauteur-1;j++){
		   graph.addEdge(new Edge(j*largeur,(j+1)*largeur, itr[j][1] ));
		   graph.addEdge(new Edge(j*largeur,(j+1)*largeur+1, Math.abs( itr[j][1] - itr[j+1][0] )));
		   for (int i=1;i<largeur-1;i++){
			   graph.addEdge(new Edge(i+j*largeur,i-1+(j+1)*largeur,Math.abs( itr[j+1][i] - itr[j][i-1] )));   	// bas Ã  gauche
			   graph.addEdge(new Edge(i+j*largeur,i+(j+1)*largeur,Math.abs( itr[j][i+1] - itr[j][i-1] )));		// bas
			   graph.addEdge(new Edge(i+j*largeur,i+1+(j+1)*largeur,Math.abs( itr[j+1][i] - itr[j][i+1] ))); 	// bas Ã  droite
		   }
		   graph.addEdge(new Edge(j*largeur+largeur-1,(j+1)*largeur+largeur-2, Math.abs( itr[j+1][largeur-1] - itr[j][largeur-2] )));
		   graph.addEdge(new Edge(j*largeur+largeur-1,(j+1)*largeur+largeur-1, Math.abs( itr[j][1] - itr[j][largeur-2] )));
	   }
	   
	   
	   for (int j=0;j<largeur;j++){
		   if (j==0){
			   graph.addEdge(new Edge(largeur*(hauteur-1)+j,hauteur*largeur,itr[hauteur-1][j+1]));
		   }else{
			   if (j==largeur-1){
				   graph.addEdge(new Edge(largeur*(hauteur-1)+j,hauteur*largeur,itr[hauteur-1][j-1]));
			   }else{
				   graph.addEdge(new Edge(largeur*(hauteur-1)+j,hauteur*largeur, Math.abs( itr[hauteur-1][j+1] - itr[hauteur-1][j-1] )));
			   }
		   }
	   }
	   
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
   
   public static int[][] deletePX(int nbDelete, String name, boolean[][] zonePxl, boolean priorite, boolean usage, boolean implicite) {
	   if (!name.contains(".pgm")){
		   System.err.println("Le fichier n'est pas au bon format (.pgm)");
		   return null;
	   }
	   
	   int[][] tabImage = readpgm(name);

	   Graph g;
	   ArrayList<Integer> ordre;
	   ArrayList<Integer> cheminMin;
	   
	   int largeur = tabImage[0].length;
	   int hauteur = tabImage.length;
	   
	   System.out.println("[CHARGEMENT]");
	   int j;
	   for (int i=0 ; i<nbDelete ; i++) {
		   // Tableau tmp au bonne dimmension		   
		   int[][] tmpImage = new int[hauteur][largeur]; // les px
		   int[][] tmpInterImage = new int[hauteur][largeur]; // l'interet
		   
		   // On place l'image (precedente) dans un tableau au bonne dimmension pour faire le graphe d'interet
		   for (int y=0 ; y<hauteur ; y++) {
			   for (int x=0 ; x<largeur ; x++) {
				   tmpImage[y][x]=tabImage[y][x];
			   }
		   }
		   
		   /* Calcul sur le tableau tmp */
		   tmpInterImage = interestVertical(tmpImage);
		   if(usage) {
			   prioriteSuppression(tmpInterImage,zonePxl,priorite);
		   }
		   
		   // Selection de l'algo de construction du graphe
		   if(implicite) {
			   g = new GraphImplicit(tmpInterImage,largeur,hauteur);
		   } else {
			   g = tographEA(tmpInterImage);
		   }
		   
		   ordre = tritopo(g);
		   cheminMin = Bellman(g, g.vertices()-1, g.vertices()-2, ordre);
		   
		   /* Parcours et suppression des pixels inutiles */
		   for (int y=0 ; y<hauteur ; y++) {
			   j=0;
			   for (int x=0 ; x<largeur ; x++) {
				   int position = largeur * y + x;
				   if (position != cheminMin.get(y+1)) {
					   tabImage[y][j]=tabImage[y][x];
					   if (usage) {
						   zonePxl[y][j]=zonePxl[y][x];
					   }
					   j++;
				   } 
			   }
		   }
		   largeur--;
	   }
	   
	   /* On ini les lignes plus utilisees pour la zone de pixel */
	   if (usage) {
			for (int y=0 ; y<zonePxl.length ; y++) {
				for (int x=zonePxl[0].length-1 ; x>zonePxl[0].length-1-nbDelete ; x--) {
					zonePxl[y][x]=false;
				}
			}
	   }
	   
	   /* On transfere l'image dans un tableau au bonne dimension */
	   int[][] nouvelleImage = new int[hauteur][largeur-nbDelete];
	   for (int y=0 ; y<nouvelleImage.length ; y++) {
		   for (int x=0 ; x<nouvelleImage[0].length ; x++) {
			   nouvelleImage[y][x] = tabImage[y][x];
		   }
	   }
	   
	   return nouvelleImage;
   }
   
   public static void dessinChemin(int[][] image, int[][] nouvelleImage, String name, boolean colonne) {
	   int[][] tabChemin = image;
	   for (int y=0 ; y<image.length ; y++) {
		   int i=0;
		   for (int x=0 ; x<image[0].length ; x++) {
			   if(colonne) {
				   if (i<nouvelleImage[0].length && tabChemin[y][x]!=nouvelleImage[y][i]) {
					   tabChemin[y][x]=255;
				   } else {
					   i++;
				   }
			   } else {
				   if (i<nouvelleImage.length && tabChemin[y][x]!=nouvelleImage[i][x]) {
					   tabChemin[y][x]=255;
				   } else {
					   i++;
				   }
			   }
		   }
	   }
	   
	   // On affiche le resultat
	   writepgm(tabChemin,"chemin_image_"+name);
   }

   public static void dessinZonePxl(int[][] image, boolean[][] zonePxl, String name) {
	   int[][] tabSelection = image;
	   /* On trace la zone importante */
	   for (int y=0 ; y<tabSelection.length ; y++) {
		   for (int x=0 ; x<tabSelection[0].length ; x++) {
			   if(zonePxl[y][x]) {
				   tabSelection[y][x]=255;
			   }
		   }
	   }
		   
	   // On affiche le resultat
	   writepgm(tabSelection,"selection_image_"+name);
   }
   
   public static int[][] deletePXHorizontal(int nbDelete, String name, boolean[][] zonePxl, boolean priorite, boolean usage) {
	   if (!name.contains(".pgm")){
		   System.err.println("Le fichier n'est pas au bon format (.pgm)");
		   return null;
	   }
	   int[][] tabImage = readpgm(name);

	   Graph g  = tographHorizontal(interestHorizontal(tabImage));
	 	   
	   ArrayList<Integer> ordre;
	   ArrayList<Integer> cheminMin;
	   
	   int largeur = tabImage[0].length;
	   int hauteur = tabImage.length;

	   int[][] nouvelleImage = new int[hauteur-nbDelete][largeur];
	   
	   System.out.println("[CHARGEMENT]");
	   int j;
	   for (int i=0 ; i<nbDelete ; i++) {
		   /* Tableau tmp aux bonnes dimensions */
		   int[][] tmpImage = new int[hauteur][largeur]; // les px
		   int[][] tmpInterImage = new int[hauteur][largeur]; // l'interet
		   
		   for (int y=0 ; y<hauteur ; y++) {
			   for (int x=0 ; x<largeur ; x++) {
				   tmpImage[y][x]=tabImage[y][x];
			   }
		   }
		   
		   /* Calcul sur le tableau tmp */
		   tmpInterImage = interestHorizontal(tmpImage);
		   if(usage) {
			   prioriteSuppression(tmpInterImage,zonePxl,priorite);
		   }
		   g = tographHorizontal(tmpInterImage);
		   //g.writeFile("test.dot");
		   ordre = tritopo(g);
		   cheminMin = Bellman(g,g.vertices()-1,g.vertices()-2,ordre);
		   
		   /* Parcours et suppression des pixels inutiles */
		   for (int x=0 ; x<largeur ; x++) {
			   j=0;
			   for (int y=0 ; y<hauteur ; y++) {
				   int position = hauteur * x + y;
				   if (position != cheminMin.get(x+1)) {
					   tabImage[j][x]=tabImage[y][x];
					   if (usage) {
						   zonePxl[j][x]=zonePxl[y][x];
					   }
					   j++;
				   } 
			   }
		   }
		   hauteur--;
	   	}

		/* On ini les lignes non utilisees pour la zone de pixel */
		if (usage) {
			for (int y=zonePxl.length-1 ; y>zonePxl.length-1-nbDelete ; y--) {
				for (int x=0 ; x<zonePxl[0].length ; x++) {
					zonePxl[y][x]=false;
				}
			}
		}

	   /* On transfere l'image dans un tableau au bonne dimension */
	   for (int y=0 ; y<nouvelleImage.length ; y++) {
		   for (int x=0 ; x<nouvelleImage[0].length ; x++) {
			   nouvelleImage[y][x] = tabImage[y][x];
		   }
	   }
	   
	   return nouvelleImage;
   }
 
   public static void prioriteSuppression(int[][] tab, boolean[][] zonePxl, boolean priorite) {
	   for (int y=0 ; y<tab.length ; y++) {
  			for (int x=0 ; x<tab[0].length ; x++) {	   
  				if(zonePxl[y][x]) {
  	   				if(priorite) {
  	   					tab[y][x] -= 99999;
  	   				} else {
  	   					tab[y][x] += 99999;
  	   				}  
  				}
  			}
  		}
   }
      
   /* Fonction qui initialise les pixels selectionné par l'utilisateur */
   public static void initTabPxl(boolean[][] tab, int posXHG, int posYHG, int posXBD, int posYBD) {
	   if(posXHG>tab[0].length || posXBD>tab[0].length) {
		   System.err.println("Le x depasse la taille de l'image !");
		   return;
	   } else if (posYHG>tab.length || posYBD>tab.length) {
		   System.err.println("Le y depasse la taille de l'image !");
		   return;
	   } else {
		   if (posXHG>posXBD) {
			   System.out.println("Inversion des X");
			   int tmp=posXHG;
			   posXHG=posXBD;
			   posXBD=tmp;
		   } else if (posYHG>posYBD) {
			   System.out.println("Inversion des Y");
			   int tmp=posYHG;
			   posYHG=posYBD;
			   posYBD=tmp;
		   }
		   
		   /* on remplit le tableau */
		   for (int y=posYHG ; y<=posYBD ; y++) {
   				for (int x=posXHG ; x<=posXBD ; x++) {			   
   					tab[y][x]=true;
   				}
	   		}
	   }
   }
   
   public static void main(String[] args) {	   
	   Scanner s = new Scanner(System.in);

	   String name = "ex1.pgm";
	   System.out.print("Veuillez entrer le nom du fichier a traiter (fichier.pgm): ");
	   name = s.nextLine();
	   	   
	   int nbPixel = 0;
	   int[][] image = readpgm(name);
	   boolean[][] zonePxl = new boolean[image.length][image[0].length]; 
	   boolean implicite=true;
	   boolean usage=false;
	   boolean priorite=false;
	   boolean colonnes=true;
	   
	   System.out.print("Le nombre de pixel a traiter: ");
	   nbPixel = s.nextInt();
	   
	   System.out.print("Suppression de lignes (l) ou de colonnes (c): ");	   
	   colonnes = s.next().equals("c");
	   
	   if(colonnes) {
		   System.out.print("Graphe implicite (i) ou energie avant (e): ");	   
		   implicite = s.next().equals("i");
		   
		   System.out.print("Voulez-vous faire usage de la selection de pixel: ");
		   usage =  s.next().equals("oui");
		   
		   if(usage) {
			   System.out.print("La priorite est a la conservation (c) ou a la suppresion (s): ");	   
			   priorite =  s.next().equals("s");
			   
			   if(name.equals("shiba.pgm") || name.equals("friends.pgm")) {
				   boolean utilisation = false;
				   System.out.println("Vous avez selectionne un fichier qui possede des selections pre-enregistrer.");
				   System.out.print("Voulez-vous les utiliser: ");
				   utilisation =  s.next().equals("oui");
				   
				   if (utilisation) {
					   if(name.equals("shiba.pgm")) {
						   initTabPxl(zonePxl,310,127,484,374); // le shiba
					   } else {
						   initTabPxl(zonePxl,25,130,115,365); // la femme
						   initTabPxl(zonePxl,320,170,400,370); // l'homme au centre
					   }
				   } else {
					   int xHG,xBD,yHG,yBD;
					   boolean fin=false;
					   while(!fin) {
						   System.out.print("x en haut à gauche: ");
						   xHG = s.nextInt();
						   System.out.print("y en haut à gauche: ");
						   yHG = s.nextInt();
						   System.out.print("x en bas à droite: ");
						   xBD = s.nextInt();
						   System.out.print("y en bas à droite: ");
						   yBD = s.nextInt();
						   
						   initTabPxl(zonePxl,xHG,yHG,xBD,yBD);
						   
						   System.out.print("Voulez-vous refaire une selection supplementaire: ");
						   fin = !(s.next().equals("oui"));
					   }
				   }   
			   }
		   }
	   }

	   // On supprime les pixels
	   int[][] nouvelleImage; 	   
	   if (colonnes){
		   nouvelleImage = new int[image.length][image[0].length-nbPixel];
		   nouvelleImage = deletePX(nbPixel,name,zonePxl,priorite,usage,implicite);
	   } else {
		   nouvelleImage = new int[image.length-nbPixel][image[0].length];
		   nouvelleImage = deletePXHorizontal(nbPixel,name,zonePxl,priorite,usage);
	   }
	   
	   // On dessine la nouvelle image 
	   writepgm(nouvelleImage,"modif_image_"+name);
		   
		// On dessine le chemin employe
	   if (colonnes) {
		   boolean tracage=true;
		   if (tracage) {
			   dessinChemin(image,nouvelleImage,name,colonnes);
		   }
		   
		   // On dessine la zone de pixel importante
		   boolean zone=true;
		   if(zone && usage)  {
			   dessinZonePxl(nouvelleImage,zonePxl,name);
		   }
	   }
	   
	   s.close();
   }
}
