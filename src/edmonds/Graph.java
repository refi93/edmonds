/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author raf
 */
public class Graph {
    HashSet<Edge> edges;
    HashSet<Vertex> vertices;
    
    public Graph(HashSet<Vertex> vertices, HashSet<Edge> edges) {
        this.vertices = new HashSet<Vertex>();
        for (Vertex v : vertices){
            this.vertices.add(v.copy());
        }
        
        this.edges = new HashSet<Edge>();
        for (Edge e : edges){
            this.edges.add(e.copy());
        }
    }
    
    public Graph copy(){
        return new Graph(this.vertices, this.edges);
    }
    
    public Graph(int vertexCount) {
        edges = new HashSet<Edge>();
        vertices = new HashSet<Vertex>();
        for(int i = 0; i < vertexCount; i++) {
            vertices.add(new Vertex(i));
        }
    }
    
    public Graph(){
        edges = new HashSet<Edge>();
        vertices = new HashSet<Vertex>();
    }
    
    public HashMap<Vertex, Integer> getVertexDegrees() {
        HashMap<Vertex, Integer> vertexDegree = new HashMap<>();
        
        for (Vertex v : this.vertices){
            vertexDegree.put(v, 0);
        }
        
        for (Edge e : this.edges){
            vertexDegree.put(e.u, vertexDegree.get(e.u) + 1);
            vertexDegree.put(e.v, vertexDegree.get(e.v) + 1);
        }
        
        return vertexDegree;
    }
    
    public boolean addEdge(Edge e) {
        if (vertices.contains(e.u) && vertices.contains(e.v)){
            return edges.add(e);
        }
        else return false;
    }
    
    public boolean removeEdge(Edge e) {
        return edges.remove(e);
    }
    
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + vertices.hashCode();
        hash = hash * 31 + edges.hashCode();
        return hash;
    }
    
    @Override
    public boolean equals(Object obj){
       if (!(obj instanceof Graph))
            return false;
        if (obj == this)
            return true;

        Graph g = (Graph) obj;
        return (g.vertices.equals(this.vertices) &&
                g.edges.equals(this.edges));
    }
}
