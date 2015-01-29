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
    TreeNode root;
    
    public HungarianTree(TreeNode root){
        this.root = root;
    }
    
    
    public void zmena(double r){
        root.zmena(r);
    }
}
