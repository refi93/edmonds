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
public class HungarianTree {
    Blossom root;
    
    public HungarianTree(Blossom root){
        this.root = root;
    }
    
    
    public void zmena(int r){
        root.zmena(r);
    }
}