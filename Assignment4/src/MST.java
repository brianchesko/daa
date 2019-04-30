import java.io.IOException;

/**
 * Finds a minimum spanning tree for a given graph.
 * Authors: Joseph Leclercq and Brian Chesko
 * Status: Completed and tested
 * Version: 2019.04.22
 */
public class MST {

    public static void main(String[] args) throws IOException {
        int numVert = IOTools.promptPositiveInteger("Enter the number of vertices: ");
        int numEdge = IOTools.promptPositiveInteger("Enter the number of edges: ");
        System.out.println("Enter all edges in the following format, one number per line: vertex 1, vertex 2, " +
                "weight from v1 to v2.");
        Edge[] edges  = new Edge[numVert];
        for (int i = 0; i < numEdge; i++) {
            int vert1 = IOTools.promptIntegerSilent("");
            int vert2 = IOTools.promptIntegerSilent("");
            int weight = IOTools.promptIntegerSilent("");

            edges[vert1] = new Edge(vert2, weight, edges[vert1]);
            edges[vert2] = new Edge(vert1, weight, edges[vert2]);
        }

        System.out.println("All edges entered.");

        int[][] mst = findMinimumSpanningTree(edges);
        int sum = 0;
        int weight;
        System.out.println("Minimum spanning tree:");
        // Start at vertex 1 since 0 is the root.
        for (int i = 1; i < numVert; i++) {
            weight = mst[i][1];
            sum += weight;
            System.out.printf("%d %d %d\n", mst[i][0], i, weight);
        }
        System.out.printf("Total Weight: %d\n", sum);
    }

    /**
     * Finds the minimum spanning tree (MST) for a given graph
     * @return a 2D array holding the parent vertex and distance for each vertex in the MST.
     * result[i, 0] is the parent vertex of vertex i
     * result[i, 1] is the weight to the parent vertex of i in the MST
     */
    private static int[][] findMinimumSpanningTree(Edge[] graph) {
        int numVertices = graph.length;
        int[][] result = new int[numVertices][2];
        SpecializedMinHeap heap = new SpecializedMinHeap(0, numVertices);
        int[] parent    = new int[numVertices];
        int[] distance  = new int[numVertices];
        boolean[] visit = new boolean[numVertices];

        // Initialize arrays to default values
        for (int i = 0; i < numVertices; i++) {
            parent[i] = -1;
            distance[i] = Integer.MAX_VALUE;
            visit[i] = false;
        }

        // Root node always has a distance of 0
        distance[0] = 0;

        for (int i = 0; i < numVertices; i++) {
            heap.insert(i, distance[i]);
        }

        Data u;
        int vert;
        int nextV;
        Edge e;
        while (!heap.isEmpty()) {
            u = heap.deleteMin();
            vert = u.getVertex();
            visit[vert] = true;
            e = graph[vert];
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

        for (int i = 0; i < numVertices; i++) {
            result[i][0] = parent[i];
            result[i][1] = distance[i];
        }

        return result;
    }
}
