public class SpecializedMinHeap implements SpecializedMinHeapInterface {
    private static final int INITIAL_SIZE = 5; // initial min size of empty heap arrays
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
     * Initializes the min heap with a @param numVertices number of vertices.
     * The source vertex is given a value of 0 and the remaining vertices
     * are given a value of Integer.MAX_VALUE.
     */
    public SpecializedMinHeap(int source, int numVertices) {
        int arraySize = numVertices > INITIAL_SIZE ? numVertices : INITIAL_SIZE;
        this.heapIndices = new int[arraySize];
        this.minHeap = new Data[arraySize];
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
            for (int i = source + 1; i < numVertices; i++) {
                heapIndices[i] = i;
                minHeap[i] = new Data(i, Integer.MAX_VALUE);
            }
        }

        for (int i = numVertices; i < heapIndices.length; i++) {
            heapIndices[i] = -1;
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
        // That vertex already exists, quit early.
        if (vertex < heapIndices.length && heapIndices[vertex] != -1) {
            return false;
        }

        // Resize arrays if too small
        if (vertex >= minHeap.length) {
            // Make sure the new arrays are at least big enough to fit the new vertex.
            int doubleCurrentSize = minHeap.length * 2;
            int newSize = vertex < doubleCurrentSize ? doubleCurrentSize : vertex + 1;
            Data[] newMinHeap = new Data[newSize];
            int[] newHeapIndices = new int[newSize];

            // copy old info
            for (int i = 0; i < minHeap.length; i++) {
                newMinHeap[i] = minHeap[i];
                newHeapIndices[i] = heapIndices[i];
            }

            // initialize new entries
            for (int i = minHeap.length; i < newSize; i++) {
                newHeapIndices[i] = -1;
            }

            this.minHeap = newMinHeap;
            this.heapIndices = newHeapIndices;
        }

        int currentIndex = numVertices;
        Data newNode = new Data(vertex, value);
        // insert in leaf position
        this.heapIndices[vertex] = currentIndex;
        this.minHeap[currentIndex] = newNode;
        this.numVertices++;

        heapFilterUp(currentIndex);

        return true;
    }

    /**
     * Does not check for good input. "Bubbles up" the heap into the correct position
     * so that the array becomes heapified starting at the specified index.
     * @param index Index of the node in the heap array to filter up.
     */
    private void heapFilterUp(int index) {
        Data node = minHeap[index];
        // bubble up to position satisfying min heap property
        int parentIndex = (index - 1) / 2;
        Data parent = minHeap[parentIndex];

        // stop when it's at the top or min heap satisfied
        while (parentIndex != index && parent.getValue() > node.getValue()) {
            // swap parent and new node
            minHeap[parentIndex] = node;
            minHeap[index] = parent;
            heapIndices[parent.getVertex()] = index;
            heapIndices[node.getVertex()] = parentIndex;

            // set new current index and update parent info
            index = parentIndex;
            parentIndex = (index - 1) / 2;
            parent = minHeap[parentIndex];
        }
    }

    /**
     * Does not check for good input. "Trickles down" the heap into the correct position
     * so that the array becomes heapified starting at the specified index.
     * @param index Index of the node in the heap array to filter up.
     */
    private void heapFilterDown(int index) {
        // index is used as the working index.
        Data node = minHeap[index];
        boolean heapified;

        do {
            heapified = true;
            int smallerChildIndex = index;
            int smallestVal = minHeap[smallerChildIndex].getValue();
            int leftIndex = 2 * index + 1;
            int rightIndex = 2 * index + 2;

            if (leftIndex < numVertices) {
                if (minHeap[leftIndex].getValue() < smallestVal) {
                    smallerChildIndex = leftIndex;
                    smallestVal = minHeap[smallerChildIndex].getValue();
                }

                if (rightIndex < numVertices && minHeap[rightIndex].getValue() < smallestVal) {
                    smallerChildIndex = rightIndex;
                }

                // There is a swap to be done
                if (smallerChildIndex != index) {
                    heapified = false;

                    // Place child into parent spot
                    minHeap[index] = minHeap[smallerChildIndex];
                    // Place parent into child spot
                    minHeap[smallerChildIndex] = node;

                    heapIndices[minHeap[index].getVertex()] = index;
                    heapIndices[node.getVertex()] = smallerChildIndex;

                    index = smallerChildIndex;
                }
            }
        } while (!heapified);
    }

    private Data deleteNode(int index) {
        if (index >= numVertices || index < 0) {
            return null;
        }

        // Delete node. Insert what's in the right most bottom index into the deleted index. heapify.
        Data toDelete = minHeap[index];
        int rightMostIndex = numVertices - 1;
        Data rightMost = minHeap[rightMostIndex];
        numVertices--;

        minHeap[index] = rightMost;
        minHeap[rightMostIndex] = null;
        heapIndices[rightMost.getVertex()] = index;
        heapIndices[toDelete.getVertex()] = -1;

        heapFilterUpOrDown(index);

        return toDelete;
    }

    private Data deleteVertex(int vertex) {
        if (vertex < 0 || vertex > heapIndices.length) {
            return null;
        }

        return deleteNode(heapIndices[vertex]);
    }

    /**
     * Deletes the vertex with the smallest value from the min heap.
     *
     * @return the wrapper for the vertex deleted
     */
    public Data deleteMin() {
        return deleteNode(0);
    }

    private void heapFilterUpOrDown(int index) {
        Data beingShifted = minHeap[index];
        int parentIndex = (index - 1) / 2;
        // If the new replacement node is smaller than its new parent, bubble up
        // otherwise trickle down
        if (numVertices > 1) {
            if (beingShifted.getValue() < minHeap[parentIndex].getValue()) {
                heapFilterUp(index);
            } else {
                heapFilterDown(index);
            }
        }
    }

    /**
     * Attempts, if possible, to modify the value of the given vertex
     * to a new value. Moves the vertex to the correct new position in
     * the heap if it has changed.
     * @return true if the vertex decreased in value and was moved,
     * false otherwise
     */
    public boolean changeKey(int vertex, int newValue) {
        if (vertex >= heapIndices.length || heapIndices[vertex] == -1) {
            return false;
        }

        int index = heapIndices[vertex];
        minHeap[index].setValue(newValue);
        heapFilterUpOrDown(index);

        return true;
    }

    /**
     * Attempts, if possible, to decrease the value of the given vertex
     * to a new value. Moves the vertex to the correct new position in
     * the heap if it has changed.
     * @return true if the vertex decreased in value and was moved,
     * false otherwise
     */
    public boolean decreaseKey(int vertex, int newValue) {
        if (vertex >= heapIndices.length || heapIndices[vertex] == -1) {
            return false;
        }

        int index = heapIndices[vertex];
        minHeap[index].setValue(newValue);
        heapFilterUp(index);

        return true;
    }

    /**
     * Debug method. Prints out a table displaying the state of the heap, including the contents
     * of the heap and the array that tracks where each vertex lies in the heap.
     */
    public void printInfo() {
        System.out.println();
        System.out.print("\t");
        for (int i = 0; i < heapIndices.length; i++) {
            System.out.printf("%d\t", i);
        }
        System.out.println();
        System.out.print("HI:\t");
        for (int heapIndex : heapIndices) {
            System.out.printf("%d\t", heapIndex);
        }
        System.out.println();
        System.out.print("HVal:\t");
        for (int i = 0; i < heapIndices.length; i++) {
            System.out.printf("%d\t", minHeap[i] == null ? null : minHeap[i].getValue());
        }
        System.out.println();
        System.out.print("HVert:\t");
        for (int i = 0; i < heapIndices.length; i++) {
            System.out.printf("%d\t", minHeap[i] == null ? null : minHeap[i].getVertex());
        }
        System.out.println();
        int pow = 0;
        int sum = 0;
        while (sum < numVertices) {
            int i = 0;
            while (i < Math.pow(2, pow) && sum < numVertices) {
                System.out.printf("%d\t", minHeap[sum].getValue());
                i++;
                sum++;
            }
            System.out.println();
            pow++;
        }
    }
}
