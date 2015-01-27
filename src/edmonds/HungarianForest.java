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
public class HungarianForest {
    ArrayList<HungarianTree> forest;
    
    public HungarianForest(){
        forest = new ArrayList<HungarianTree>();
    }
    
    public void zmena(int r){
        for (int i = 0;i < forest.size(); i++){
            forest.get(i).zmena(r);
        }
    }
    
    public void addTree(HungarianTree t){
        forest.add(t);
    }
    
    public void removeTree(HungarianTree t){
        forest.remove(t);
    }
}
