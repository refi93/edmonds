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
    public String toString(){
        return (this.u.id + 1) + " " + (this.v.id + 1) + " " + this.price;
    }
}
