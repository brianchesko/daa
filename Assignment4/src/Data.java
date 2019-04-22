public class Data
{
    private int value;
    private int vertex;

    public Data(int vertex, int value) {
        this.value = value;
        this.vertex = vertex;
    }

    public void setVertex(int vertex) {
        this.vertex = vertex;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getVertex() {
        return vertex;
    }

    public int getValue() {
        return value;
    }

}
