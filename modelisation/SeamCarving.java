package modelisation;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
public class SeamCarving
{

   public static int[][] readpgm(String fn)
	 {		
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
   
   public static void writepgm(int[][] image, String filename){
	   assert(image.length>0):"Tableau vide, impossible de créer l'image :(";	
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

		   for (int j=0;j<image.length;j++){
			   for (int i=0;i<image[0].length;i++){
				   
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
           t.printStackTrace(System.err) ;
       }
   }
   
   public static int[][] interest(int[][] image){
	   assert(image.length>1);
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

   public static void main(String[] args)
	 {
		int[][] hello=readpgm("test.pgm");
		hello=interest(hello);
		writepgm(hello,"oui");
	 }
}
