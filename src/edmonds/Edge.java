/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

/**
 *
 * @author raf
 */
public class Edge {
    Vertex u,v;
    int price;
    
    public Edge(int u, int v, int price) {
        this.u = new Vertex(u);
        this.v = new Vertex(v);
        this.price = price;
    }
    
    // deep copy of edge
    public Edge copy() {
        return new Edge(u.copy(), v.copy(), price);
    }
    
    public Edge(Vertex u, Vertex v, int price) {
        this.u = u;
        this.v = v;
        this.price = price;
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + u.hashCode();
        hash = hash * 31 + v.hashCode();
        hash = hash * 13 + price;
        return hash;
    }
    
    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Edge))
            return false;
        if (obj == this)
            return true;

        Edge e = (Edge) obj;
        return (e.u.equals(this.u) && 
                (e.v.equals(this.v)) && 
                (e.price == this.price));
    }
}
