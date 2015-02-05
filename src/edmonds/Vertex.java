/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 *
 * @author raf
 */
public class Vertex {
    int id;
    ArrayList<Blossom> blossomStack; // stack bublin, do ktorych patri
    
    public Vertex(int id){
        this.id = id;
        this.blossomStack = new ArrayList<Blossom>();
    }
    
    public void addToBlossom(Blossom b){
        blossomStack.add(b);
    }
    
    public void popOutermostBlossom(){
        blossomStack.remove(blossomStack.size() - 1);
    }
    
    // vrati aktualnu bublinu pre vrchol
    public Blossom getOutermostBlossom(){
        return blossomStack.get(blossomStack.size() - 1);
    }
    
    // vrati n-tu najhlbsiu bublinu (pocinajuc 0)
    public Blossom getNthOutermostBlossom(int level){
        if (blossomStack.size() <= level){
            return null;
        }
        return blossomStack.get(blossomStack.size() - 1 - level);
    }
    
    // vrati celkovy naboj vrcholu
    public double getCharge(){
        double ret = 0;
        for(int i = 0; i < blossomStack.size(); i++){
            ret += blossomStack.get(i).thickness;
        }
        return ret;
    }
}
