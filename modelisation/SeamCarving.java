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
	   int linesize=0; //permet de respecter les 70 caractères par ligne  
	   try {
		   
		   File f=new File(filename);
		   FileWriter fw=new FileWriter(f);
		   BufferedWriter bw = new BufferedWriter(fw);
		   bw.write("P2");
		   bw.newLine();
		   bw.write(image.length+" "+image[0].length);
		   bw.newLine();
		   for (int i=0;i<image[0].length;i++){
			   for (int j=0;j<image.length;j++){
				   if(linesize>70){
					   bw.newLine();
				   }
				   bw.write(image[j][i]);
				   linesize++;
			   }
		   }
		   fw.close();
	   }
	   catch(Throwable t) {
           t.printStackTrace(System.err) ;
       }
   }

   
}
