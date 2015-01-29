/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author raf
 */
public class Edmonds {
    public static void main(String[] args){
        int n = 10;
        
        ArrayList<Dumbbell> dumbbells = new ArrayList<Dumbbell>(); // cinky
        ArrayList<HungarianTree> hungarianForest = new ArrayList<HungarianTree>(); // madarsky les
        ArrayList<BlueBlossom> freeBlossoms = new ArrayList<BlueBlossom>(); // volne modre kvety
        Graph myGraph = new Graph(n);
        
        // vrcholy pridame do modrych bublin a zaznacime si tieto bubliny do mnoziny volnych bublin
        for(int i = 0; i < n; i++){
            BlueBlossom b = new BlueBlossom(i);
            
            freeBlossoms.add(b);
            myGraph.vertices.get(i).addToBlossom(b);
        }
    }
}
