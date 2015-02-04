/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author raf
 */
public class TreeNode {
    HungarianTree treeRef; //referencia na strom, aby sme vedeli posudit, ci dva vrcholy su v tom istom strome
    private TreeNode parent; // kto je jeho parent vramci madarskeho stromu
    private ArrayList<TreeNode> children; // zoznam deti vramci madarskeho stromu
    private Edge parentEdge; // hrana z grafu spajajuca kvet s otcovskym kvetom
    Blossom containedBlossom; // najvacsia zelena bublina vo vnutri nodu
    
    public TreeNode(Blossom containedBlossom){
        this.parent = null;
        this.children = new ArrayList<TreeNode>();
        this.parentEdge = null;
        this.containedBlossom = containedBlossom;
        this.containedBlossom.treeNodeRef = this; // upravime vrcholu referenciu na node
        this.treeRef = null;
    }
    
    public void zmena(double r){
        containedBlossom.zmena(r);
        for (int i = 0; i < children.size(); i++){
            // na dalsej urovni menime opacne
            children.get(i).zmena(-r);
        }
    }
    
    public void addChild(TreeNode child, Edge parentEdge){
        children.add(child);
        child.treeRef = this.treeRef;
        child.parent = this;
        child.parentEdge = parentEdge;
    }
    
    public void setParent(TreeNode parent, Edge parentEdge){
        this.parent = parent;
        this.parentEdge = parentEdge;
        if (parent != null){
            parent.children.add(this);
            this.treeRef = parent.treeRef;
        }
    }
    
    public ArrayList<TreeNode> getChildren(){
        return this.children;
    }
    
    public TreeNode getParent(){
        return this.parent;
    }
    
    public Edge getParentEdge(){
        return this.parentEdge;
    }
    
    // vrati zoznam predkov pre vrchol od korena po sameho seba
    public ArrayList<TreeNode> getAncestors(){
        ArrayList<TreeNode> ret = new ArrayList<TreeNode>();
        getAncestors(ret);
        return ret;
    }
    
    public ArrayList<Dumbbell> breakToDumbbells(HashSet<TreeNode> alternatingPath){
        ArrayList<Dumbbell> ret = new ArrayList<Dumbbell>();
        breakToDumbbells(alternatingPath, ret, false);
        return ret;
    }
    
    private void breakToDumbbells(HashSet<TreeNode> alternatingPath, ArrayList<Dumbbell> dumbbells, boolean makeDumbbell){
        if (makeDumbbell == true){
            dumbbells.add(new Dumbbell(this.containedBlossom, parent.containedBlossom, parentEdge));
        }
        
        for(int i = 0; i < children.size(); i++){
            if (alternatingPath.contains(this)){
                if (alternatingPath.contains(children.get(i))){
                    children.get(i).breakToDumbbells(alternatingPath, dumbbells, !makeDumbbell);
                }
                else {
                    children.get(i).breakToDumbbells(alternatingPath, dumbbells, makeDumbbell);
                }
            }
            else{
                children.get(i).breakToDumbbells(alternatingPath, dumbbells, !makeDumbbell);
            }
        }
    }
    
    private void getAncestors(ArrayList<TreeNode> ancestors){
        if (this.parent != null){
            this.parent.getAncestors(ancestors);
        }
        ancestors.add(this);
    }
    
    // vrati hrany na ceste od korena po node
    public ArrayList<Edge> getAncestorEdges(){
        ArrayList<Edge> ret = new ArrayList<Edge>();
        getAncestorEdges(ret);
        if (this.parentEdge != null){
            ret.add(this.parentEdge);
        }
        return ret;
    }
    
    private void getAncestorEdges(ArrayList<Edge> ancestorEdges){
        if (this.parentEdge != null){
            this.parent.getAncestorEdges(ancestorEdges);
            ancestorEdges.add(this.parentEdge);
        }
    }
}
