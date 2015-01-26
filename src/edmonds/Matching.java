/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.HashMap;

/**
 *
 * @author raf
 */
public class Matching extends Graph{

    public Matching(int vertexCount) {
        super(vertexCount);
    }
    
    public Matching() {
        super();
    }
    
    // verify, that our graph is a matching, maybe not perfect (degree 0 allowed)
    private boolean isMatching() {
        HashMap<Vertex, Integer> vertexDegree = this.getVertexDegrees();
        for (Vertex v : this.vertices){
            if (vertexDegree.get(v) > 1) return false;
        }
        return true;
    }
    
    // verify, that our graph is a perfect matching, maybe not perfect (degree 1 required)
    private boolean isPerfectMatching() {
        HashMap<Vertex, Integer> vertexDegree = this.getVertexDegrees();
        for (Vertex v : this.vertices){
            if (vertexDegree.get(v) == 1) return false;
        }
        return true;
    }
    
    /* we try to add an edge and we keep it there only if it is a valid matching */
    @Override
    public boolean addEdge(Edge e) {
        boolean ret = edges.add(e);
        if (this.isMatching()) {
            return ret;
        }
        else {
            edges.remove(e);
            return false;
        }
    }
}
