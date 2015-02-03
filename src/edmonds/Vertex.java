/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author raf
 */
public class Vertex {
    int id;
    Deque<Blossom> blossomStack; // stack bublin, do ktorych patri
    
    public Vertex(int id){
        this.id = id;
        this.blossomStack = new ArrayDeque<Blossom>();
    }
    
    public void addToBlossom(Blossom b){
        blossomStack.push(b);
    }
    
    public void popOutermostBlossom(){
        blossomStack.pop();
    }
    
    // vrati aktualnu bublinu pre vrchol
    public Blossom getOutermostBlossom(){
        return blossomStack.getLast();
    }
}
