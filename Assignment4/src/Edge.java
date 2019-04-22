public class Edge{
    private int nextVert;
    private int weight;
    private Edge nextEdge;

    public Edge(int nextVert, int weight, Edge nextEdge){
        this.nextVert = nextVert;
        this.weight = weight;
        this.nextEdge = nextEdge;
    }

    public int getNextVert(){
        return nextVert;
    }
    public int getWeight(){
        return weight;
    }
    public Edge getNextEdge(){
        return nextEdge;
    }       
        

    public void setNextVert(int vert){
        this.nextVert = vert;  
    }

    public void setWeight(int weight){
        this.weight = weight;
    }
    public void setNextEdge(Edge e){
        this.nextEdge = e;
    }
}
