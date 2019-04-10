

public class SpecializedMinHeap implements SpecializedMinHeapInterface {
    private final int INITIAL_SIZE = 8; // initial min size of empty heap arrays
    private int numVertices;
    private int[] heapIndices;
    private Data[] minHeap;

    /**
     * Initializes an empty min heap with no vertices.
     */
    public SpecializedMinHeap() {
        this(-1, 0);
    }

    /**
     * Initializes the min heap with a distance of 0 for the source vertex
     * and Integer.MAX_VALUE for the remaining vertices.
     */
    //initializes MinHeap with value (distance) info for source vertex and
    //remaining specified number of vertices; initializes MinHeap location
    //for each vertex
    public SpecializedMinHeap(int source, int numVertices) {
        this.heapIndices = new int[numVertices > INITIAL_SIZE ? numVertices : INITIAL_SIZE];
        this.minHeap = new Data[numVertices > INITIAL_SIZE ? numVertices : INITIAL_SIZE];
        this.numVertices = numVertices;

        if (numVertices > 0) {
            // Initial location for all vertices before source is that vertex + 1
            // to account for the shift caused by putting the source at the head of
            // the max heap.
            for (int i = 0; i < source; i++) {
                heapIndices[i] = i + 1;
                minHeap[i + 1] = new Data(i, Integer.MAX_VALUE);
            }

            // Put source at top of the max heap with distance 0.
            heapIndices[source] = 0;
            minHeap[0] = new Data(source, 0);

            // All vertices after the source are in the position corresponding to
            // the index equalling that vertex, but still infinite distance.
            for (int i = source; i < numVertices; i++) {
                heapIndices[i] = i;
                minHeap[i] = new Data(i, Integer.MAX_VALUE);
            }
        }
    }

    /**
     * @return true if the min heap is empty, false otherwise.
     */
    public boolean isEmpty() {
        return numVertices == 0;
    }

    /**
     * Inserts a new vertex into the min heap with a given value.
     *
     * @return the outcome of the insertion
     */
    public boolean insert(int vertex, int value) {
        // Resize arrays if too small
        if (numVertices == minHeap.length) {
            Data[] newMinHeap = new Data[numVertices * 2];
            int[] newHeapIndices = new int[numVertices * 2];

            // copy old info
            for (int i = 0; i < numVertices * 2; i++) {
                newMinHeap[i] = minHeap[i];
                newHeapIndices[i] = heapIndices[i];
            }

            this.minHeap = newMinHeap;
            this.heapIndices = newHeapIndices;
        }

        int currentIndex = numVertices;
        Data newNode = new Data(vertex, value);
        // insert in leaf position
        this.heapIndices[vertex] = currentIndex;
        this.minHeap[currentIndex] = newNode;

        // no shifts needed, exit immediately
        if (numVertices == 0) {
            return true;
        }

        // bubble up to position satisfying min heap property
        int parentIndex = (currentIndex + 1) / 2;
        Data parent = minHeap[parentIndex];

        // stop when it's at the top or min heap satisfied
        while (parentIndex != currentIndex || parent.getValue() > newNode.getValue()) {
            // swap parent and new node
            minHeap[parentIndex] = newNode;
            minHeap[currentIndex] = parent;
            heapIndices[parent.getVertex()] = currentIndex;
            heapIndices[newNode.getVertex()] = parentIndex;

            // set new current index and update parent info
            currentIndex = parentIndex;
            parentIndex = (currentIndex + 1) / 2;
            parent = minHeap[parentIndex];
        }

        this.numVertices++;
        return true;
    }

    /**
     * Deletes the vertex with the smallest value from the min heap.
     * 
     * @return the wrapper for the vertex deleted
     */
    public Data deleteMin() {
        return null;   
    }

    /**
     * Attempts, if possible, to decrease the value of the given vertex
     * to a new value. Moves the vertex to the correct new position in
     * the heap. 
     * @return true if the vertex decreased in value and was moved,
     * false otherwise
     */
    public boolean decreaseKey(int vertex, int newValue) {
        return false;
    }
}
