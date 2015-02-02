/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
                            
                            // vytvorime novy vrchol madarskeho stromu
                            TreeNode newNode = new TreeNode(blossom2);
                            // novy vrchol v strome dostane referenciu na strom, na ktory bol pripojeny
                            newNode.treeRef = blossom1.treeNodeRef.treeRef;
                            
                            // kvet dostane referenciu na node v strome
                            blossom2.treeNodeRef = newNode;
                            
                            // pridame novy vrchol ako syna kvetu blossom1
                            blossom1.treeNodeRef.children.add(newNode);
                            
                            // nastavime blossom1 ako otca vrcholu blossom2
                            blossom2.treeNodeRef.parent = blossom1.treeNodeRef;
                            
                            // nastavime otcovsku hranu pre blossom2
                            blossom2.treeNodeRef.parentEdge = new Edge (i, j, myGraph.incidenceMatrix[i][j]);
                        }
                        
                        // ak sa naplni hrana medzi dvomi kvetmi v strome (stat sa to moze len na parnej urovni, lebo len tam je kladny prirastok
                        else if (blossom1.treeNodeRef.treeRef == blossom2.treeNodeRef.treeRef){
                            ArrayList<TreeNode> ancestors1 = blossom1.treeNodeRef.getAncestors();
                            ArrayList<TreeNode> ancestors2 = blossom2.treeNodeRef.getAncestors();
                            
                            //hladanie najblizsieho spolocneho predka (ideme od korena stromu)
                            int k;
                            for(k = 0; ancestors1.get(k) == ancestors2.get(k); k++){
                                
                            }
                            // k je teraz index prveho nodu, kde sa postupnost predkov od korena lisi
                            // k - 1 je tym padom index posledneho spolocneho predka
                            List<TreeNode> path1 = ancestors1.subList(k - 1, ancestors1.size());
                            List<TreeNode> path2 = ancestors2.subList(k , ancestors2.size());
                            Collections.reverse(path2);
                            
                            // vytvorime cestu od spolocneho predka (novej stopky) po nasledujuci vrchol v kruznici, ktora bude v novej zelenej bubline
                            ArrayList<TreeNode> oddCycleNodes = new ArrayList<TreeNode>(path1);
                            oddCycleNodes.addAll(path2);
                            
                            
                            // prevedieme tree nody na prisluchajuce bubliny
                            ArrayList<Blossom> oddCycleBlossoms = new ArrayList<Blossom>();
                            for (int l = 0; l < oddCycleNodes.size(); l++){
                                oddCycleBlossoms.add(oddCycleNodes.get(l).containedBlossom);
                            }
                            
                            // vytvorime novy zeleny kvet obsahujuci neparny cyklus a prislusny node v strome
                            GreenBlossom newBlossom = new GreenBlossom(oddCycleBlossoms);
                            TreeNode newNode = new TreeNode(newBlossom);
                            
                            newBlossom.treeNodeRef = newNode;
                            newBlossom.levelParity = 1; // je na parnej urovni
                            
                            
                            // updatujeme kvetom v neparnom cykle referenciu na node do ktoreho patria
                            for (int l = 0; l < oddCycleBlossoms.size(); l++) {
                                oddCycleBlossoms.get(i).treeNodeRef = newNode;
                            }
                            
                            // vytvorime zoznam deti pre novo pridany node
                            ArrayList<TreeNode> newNodeChildren = new ArrayList<TreeNode>();
                            for (int l = 0; l < oddCycleNodes.size(); l++){
                                TreeNode oddCycleNode = oddCycleNodes.get(l);
                                for (int m = 0; m < oddCycleNode.children.size(); m++){
                                    TreeNode child = oddCycleNode.children.get(m);
                                    // nechceme updatovat kvety vo vnutri newNode, len tie vonku a tie su zaroven childovia nasho noveho nodu, tak ich tam rovno pridame
                                    if (child.containedBlossom.treeNodeRef != newNode) {
                                        // updatujeme detom vnutornych nodov mimo noveho nodu referenciu na parenta
                                        child.parent = newNode;
                                        // pridame vrcholy mimo noveho nodu do zoznamu deti noveho nodu
                                        newNodeChildren.add(child);
                                    }
                                }
                            }
                            
                            // priradime deti novemu vrcholu v strome
                            newNode.children = newNodeChildren;
                            
                            // parent stopkoveho nodu je odteraz parentom noveho nodu
                            newNode.parent = oddCycleNodes.get(0).parent;
                            
                            // parent edge stopkoveho nodu je odteraz parent edge noveho nodu
                            newNode.parentEdge = oddCycleNodes.get(0).parentEdge;
                        } 
                    }
                }
            }
            
            //TODO fixy na stromoch
        }
    }
}
