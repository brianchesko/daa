import java.io.IOException;

public class MST{
    
    public static void main(String args[]) throws IOException{
        int numVert = IOTools.promptPositiveInteger("");
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

        int numEdge = IOTools.promptPositiveInteger("");
        for(int i = 0; i < numEdge; i++){
            int edge1 = IOTools.promptInteger("");
            int edge2 = IOTools.promptInteger("");
            int weight = IOTools.promptInteger("");
            
            edges[edge1] = new Edge(edge2, weight, edges[edge1]);
            
            edges[edge2] = new Edge(edge1, weight, edges[edge2]);    
        }
        Data u;
        int vert;
        int nextV;
        Edge e;
        while(!heap.isEmpty()){
            u = heap.deleteMin();
            vert = u.getVertex();
            visit[vert] = true;
            System.out.println(parent[vert] + " " + vert + " " +
                                " " + distance[vert]);
            e = edges[vert];
            do{
                nextV = e.getNextVert();
                if(!visit[nextV] && e.getWeight() < distance[nextV]){
                    distance[nextV] = e.getWeight();
                    heap.decreaseKey(nextV, distance[nextV]);
                    parent[nextV] = vert;
                }
                e = e.getNextE();
            }while(e != null);
        } 
    }
}
