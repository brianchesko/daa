import java.io.IOException;

public class MST{
    
    public static void main(String args[]) throws IOException{
        int numVert = IOTools.promptPositiveInteger("Enter the number " + 
          "of vertices");
        Edge[] edges = new Edge[numVert];
        int[] parent = new int[numVert];
        int[] distance = new int[numVert];
        boolean[] visit = new boolean[numVert];
        SpecializedMinHeap heap = new SpecializedMinHeap(0, numVert);
        for(int i = 0; i < numVert; i++){
            parent[i] = -1;
            distance[i] = Integer.MAX_VALUE;
            visit[i] = false;
        }
        distance[0] = 0;
        for(int i = 0; i < numVert; i++){
            heap.insert(i, distance[i]);
        }

        int numEdge = IOTools.promptPositiveInteger("Enter the number " + 
          "of edges");
        for(int i = 0; i < numEdge; i++){
            int edge1 = IOTools.promptInteger("Enter the first " + 
                "edge");
            int edge2 = IOTools.promptInteger("Enter the second" + 
                " edge");
            int weight = IOTools.promptInteger("Enter the weight");
            
            edges[edge1] = new Edge(edge2, weight, edges[edge1]);
            
            edges[edge2] = new Edge(edge1, weight, edges[edge2]);    
        }
            
    }
}
