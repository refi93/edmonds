/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author raf
 */
public class Edmonds {
    public static void main(String[] args){
        int n = 10;
        
        HashSet<Dumbbell> dumbbells = new HashSet<Dumbbell>(); // cinky
        ArrayList<HungarianTree> hungarianForest = new ArrayList<HungarianTree>(); // madarsky les
        ArrayList<BlueBlossom> freeBlossoms = new ArrayList<BlueBlossom>(); // volne modre kvety
        Graph myGraph = new Graph(n);
        
        // vrcholy pridame do modrych bublin a zaznacime si tieto bubliny do mnoziny volnych bublin
        for(int i = 0; i < n; i++){
            BlueBlossom b = new BlueBlossom(i);
            
            freeBlossoms.add(b);
            myGraph.vertices.get(i).addToBlossom(b);
        }
        
        // dokym nemame parovanie
        while (dumbbells.size() < n/2){
            double r = myGraph.getR();
            
            // zmenime naboj vo volnych (modrych) bublinach
            for(int i = 0; i < freeBlossoms.size(); i++){
                freeBlossoms.get(i).zmena(r);
            }
            
            // zmenime naboj v stromoch madarskeho lesa
            for (int i = 0;i < hungarianForest.size(); i++){
                // zmenime a overime, ci nejakej zelenej bubline na neparnej urovni klesol naboj na 0
                hungarianForest.get(i).zmena(r);
            }
            
            
            // fixy na stromoch
            for (int i = 0; i < myGraph.vertexCount; i++){
                for (int j = 0; j < myGraph.vertexCount; j++){
                    Blossom blossom1 = myGraph.vertices.get(i).getOutermostBlossom();
                    Blossom blossom2 = myGraph.vertices.get(j).getOutermostBlossom();
                    
                    double edgeCapacity = myGraph.incidenceMatrix[i][j];
                    
                    if (blossom1.thickness + blossom2.thickness > edgeCapacity){
                        System.err.println("PRESIAHNUTA KAPACITA HRANY, NIECO SA POSRALO");
                    }
                    
                    // ak sa nejaka hrana naplnila
                    if (blossom1.thickness + blossom2.thickness == edgeCapacity){
                        
                        // ak sa naplnila hrana medzi kvetom na parnej urovni a cinkou
                        if (blossom1.levelParity == 1 && blossom2.levelParity == 0){
                            
                            // odstranime cinku
                            Dumbbell dumb = blossom2.dumbbellRef;
                            dumb.b1.dumbbellRef = null;
                            dumb.b2.dumbbellRef = null;
                            dumbbells.remove(dumb);
                            
                            Edge e = new Edge (i, j, myGraph.incidenceMatrix[i][j]);
                            
                            // vytvorime novy vrchol madarskeho stromu
                            TreeNode newNode = new TreeNode(blossom2);
                        }
                    }
                }
            }
            
            //TODO fixy na stromoch
        }
    }
}
