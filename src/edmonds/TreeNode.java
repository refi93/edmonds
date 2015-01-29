/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.ArrayList;

/**
 *
 * @author raf
 */
public class TreeNode {
    TreeNode parent; // kto je jeho parent vramci madarskeho stromu
    ArrayList<TreeNode> children; // zoznam deti vramci madarskeho stromu
    Edge parentEdge; // hrana z grafu spajajuca kvet s otcovskym kvetom
    GreenBlossom containedBlossom; // najvacsia zelena bublina vo vnutri nodu
    
    public TreeNode(GreenBlossom containedBlossom){
        this.parent = null;
        this.children = new ArrayList<TreeNode>();
        this.parentEdge = null;
        this.containedBlossom = containedBlossom;
    }
    
    public void zmena(int r){
        containedBlossom.zmena(r);
        for (int i = 0; i < children.size(); i++){
            // na dalsej urovni menime opacne
            children.get(i).zmena(-r);
        }
    }
}
