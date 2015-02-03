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
    
    public Edge(Vertex u, Vertex v, int price) {
        this.u = u;
        this.v = v;
        this.price = price;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + u.id * v.id;
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
        
        boolean ret1 = (e.u == this.u) && 
                (e.v == this.v);
        boolean ret2 = (e.u == this.v) && 
                (e.v == this.u);
        
        return (ret1 || ret2) && (e.price == this.price);
    }
}
