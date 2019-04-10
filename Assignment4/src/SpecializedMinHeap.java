

public class SpecializedMinHeap implements SpecializedMinHeapInterface {

    /**
     * Initializes an empty min heap with no vertices.
     */
    public SpecializedMinHeap() {

    }

    /**
     * Initializes the min heap with a distance of 0 for the source vertex
     * and Integer.MAX_INTEGER for the remaining vertices.
     */
    //initializes MinHeap with value (distance) info for source vertex and
    //remaining specified number of vertices; initializes MinHeap location
    //for each vertex
    public SpecializedMinHeap(int source, int numVertices) {

    }

    /**
     * @return true if the min heap is empty, false otherwise.
     */
    public boolean isEmpty() {
        return false;
    }

    /**
     * Inserts a new vertex into the min heap with a given value.
     *
     * @return the outcome of the insertion
     */
    public boolean insert(int vertex, int value) {
        
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
