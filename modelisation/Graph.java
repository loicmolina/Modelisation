package modelisation;

public interface Graph {
   public int vertices();

   public Iterable<Edge> next(int v);
   public Iterable<Edge> prev(int v);
   
   public void writeFile(String s);
  
}
