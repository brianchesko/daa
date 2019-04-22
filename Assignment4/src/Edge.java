public class Edge{
    private int nextVert;
    private int weight;
    private Edge nextE;

    public Edge(int nextVert, int weight, Edge nextE){
        this.nextVert = nextVert;
        this.weight = weight;
        this.nextE = nextE;
    }

    public int getNextVert(){
        return nextVert;
    }
    public int getWeight(){
        return weight;
    }
    public Edge getNextE(){
        return nextE;
    }       
        

    public void setNextVert(int vert){
        this.nextVert = vert;  
    }

    public void setWeight(int weight){
        this.weight = weight;
    }
    public void setNextE(Edge e){
        this.nextE = e;
    }
}
