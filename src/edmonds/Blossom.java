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
class Blossom {
    
    double thickness;
    int levelParity;
    Dumbbell dumbbellRef = null; // referencia na cinku, ak sa v nejakej nachadza
    TreeNode treeNodeRef = null; // referencia na vrchol v madarskom strome, ak sa v nejakom nachadza
    
    // sluzi na updatovanie hrubky kvetu
    public void zmena(double r) {
        thickness += r;
    }
    
    public void setStopkaByEdge(Edge e){
        return;
    }
    
    public void setStopkaByEdge(Edge e, int level){
        return;
    }
    
    public int getStopka(){
        return 0;
    }
    
    // vrati cenu parovania vo vnutri bubliny
    public int getMatchingPrice(){
        return 0;
    }
}