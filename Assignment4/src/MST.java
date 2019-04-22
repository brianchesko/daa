import java.io.IOException;

public class MST {

    public static void main(String[] args) throws IOException {
        int numVert = IOTools.promptPositiveInteger("Enter the number of vertices: ");
        SpecializedMinHeap heap = new SpecializedMinHeap(0, numVert);
        Edge[] edges    = new Edge[numVert];
        int[] parent    = new int[numVert];
        int[] distance  = new int[numVert];
        boolean[] visit = new boolean[numVert];

        // Initialize arrays to default values
        for (int i = 0; i < numVert; i++) {
            parent[i] = -1;
            distance[i] = Integer.MAX_VALUE;
            visit[i] = false;
        }

        distance[0] = 0;

        for (int i = 0; i < numVert; i++) {
            heap.insert(i, distance[i]);
        }

        int numEdge = IOTools.promptPositiveInteger("Enter the number of edges: ");
        System.out.println("Enter all edges in the following format, one number per line: vertex 1, vertex 2, " +
                "weight from v1 to v2.");
        for (int i = 0; i < numEdge; i++) {
            int vert1 = IOTools.promptInteger("");
            int vert2 = IOTools.promptInteger("");
            int weight = IOTools.promptInteger("");

            edges[vert1] = new Edge(vert2, weight, edges[vert1]);
            edges[vert2] = new Edge(vert1, weight, edges[vert2]);
        }

        Data u;
        int vert;
        int nextV;
        Edge e;
        while (!heap.isEmpty()) {
            u = heap.deleteMin();
            vert = u.getVertex();
            visit[vert] = true;
            // Only print edges that exist
            if (parent[vert] != -1) {
                System.out.println(parent[vert] + " " + vert + " " +
                        " " + distance[vert]);
            }
            e = edges[vert];
            do {
                nextV = e.getNextVert();
                if (!visit[nextV] && e.getWeight() < distance[nextV]) {
                    distance[nextV] = e.getWeight();
                    heap.decreaseKey(nextV, distance[nextV]);
                    parent[nextV] = vert;
                }
                e = e.getNextEdge();
            } while (e != null);
        }
    }
}
