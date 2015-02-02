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
    HungarianTree treeRef; //referencia na strom, aby sme vedeli posudit, ci dva vrcholy su v tom istom strome
    TreeNode parent; // kto je jeho parent vramci madarskeho stromu
    ArrayList<TreeNode> children; // zoznam deti vramci madarskeho stromu
    Edge parentEdge; // hrana z grafu spajajuca kvet s otcovskym kvetom
    Blossom containedBlossom; // najvacsia zelena bublina vo vnutri nodu
    
    public TreeNode(Blossom containedBlossom){
        this.parent = null;
        this.children = new ArrayList<TreeNode>();
        this.parentEdge = null;
        this.containedBlossom = containedBlossom;
        this.treeRef = null;
    }
    
    public void zmena(double r){
        containedBlossom.zmena(r);
        for (int i = 0; i < children.size(); i++){
            // na dalsej urovni menime opacne
            children.get(i).zmena(-r);
        }
    }
    
    // vrati zoznam predkov pre vrchol od korena po sameho seba
    public ArrayList<TreeNode> getAncestors(){
        ArrayList<TreeNode> ret = new ArrayList<TreeNode>();
        getAncestors(ret);
        ret.add(this);
        
        return ret;
    }
    
    private void getAncestors(ArrayList<TreeNode> ancestors){
        if (this.parent != null){
            getAncestors(ancestors);
            ancestors.add(this.parent);
        }
    }
}
