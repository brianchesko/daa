public interface SpecializedMinHeapInterface {
//specialized MinHeap for use with Prim's or Dijkstra's; assumes vertices are numbered 0..numvertices-1 and one of them is the source 

public boolean isEmpty();
//returns true if empty, false otherwise

public boolean insert(int vertex, int value);
//inserts in MinHeap and returns outcome

public Data deleteMin();
//returns reference to object that has been removed 

public boolean decreaseKey(int vertex, int newvalue);
//decreases to newvalue the value of the specified vertex (if lower) and returns the outcome

}// end SpecializedMinHeapInterface 
