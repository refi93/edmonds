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
        Graph myGraph = new Graph(n);
        
        // vrcholy pridame do modrych bublin a vytvorime pre kazdu z nich novy madarsky strom a pridame do lesa
        for(int i = 0; i < n; i++){
            Vertex v = myGraph.vertices.get(i);
            
            BlueBlossom b = new BlueBlossom(v);
            TreeNode node = new TreeNode(b);
            HungarianTree t = new HungarianTree(node);
            node.treeRef = t; // update referencie na strom pre node
            
            hungarianForest.add(t);
            
            v.addToBlossom(b);
        }
        
        // dokym nemame parovanie
        while (hungarianForest.size() != 0){
            double r = myGraph.getR();
            
            // zmenime naboj v stromoch madarskeho lesa
            for (int i = 0;i < hungarianForest.size(); i++){
                // zmenime a overime, ci nejakej zelenej bubline na neparnej urovni klesol naboj na 0
                hungarianForest.get(i).zmena(r);
            }
            
            
            // fixy na stromoch
            for (int i = 0; i < myGraph.vertexCount; i++){
                Blossom blossom = myGraph.vertices.get(i).getOutermostBlossom();
                // (P1) zelenej bubline na neparnej urovni klesol naboj na 0
                if (blossom.levelParity == -1 && blossom.thickness == 0){
                    
                    // overime, ze skutocne ide o zelenu bublinu
                    if (blossom instanceof GreenBlossom){
                        GreenBlossom oldGreenBlossom = (GreenBlossom) blossom;
                        TreeNode oldNode = oldGreenBlossom.treeNodeRef;
                        HungarianTree currentTree = oldNode.treeRef;
                        ArrayList<Blossom> containedBlossoms = oldGreenBlossom.blossoms;
                        ArrayList<Vertex> innerVertices = oldGreenBlossom.getInnerVertices();
                        // overime, ze ma skutocne jedno dieta
                        if (oldNode.children.size() != 1){
                            System.err.println("Vrchol na neparnej urovni nema jedneho syna, problem");
                        }
                        
                        // ideme odstranovat z vrcholov referenciu na bublinu, ktora prave splasla
                        for (int j = 0; j < innerVertices.size(); j++){
                            innerVertices.get(j).popOutermostBlossom();
                        }
                        
                        // ideme zistit, ktorej bubline vo vnutri tej splasnutej prisluchala odchadzajuca hrana
                        Edge parentEdge = oldGreenBlossom.treeNodeRef.parentEdge;
                        Blossom parentEdgeBlossom = null; // bublina obsahujuca parent edge
                        int parentEdgeBlossomIndex = -1; // index bubliny obsahujucej parent edge
                        
                        for(int j = 0; j < containedBlossoms.size(); j++){
                            if (containedBlossoms.get(j) == parentEdge.u.getOutermostBlossom()){
                                if (parentEdgeBlossom != null) {
                                    System.err.println("z dvoch bublin odchadza hrana do otca, to je pruser");
                                }
                                parentEdgeBlossom = parentEdge.u.getOutermostBlossom();
                                parentEdgeBlossomIndex = j;
                            }
                            else if (containedBlossoms.get(j) == parentEdge.v.getOutermostBlossom()){
                                if (parentEdgeBlossom != null) {
                                    System.err.println("z dvoch bublin odchadza hrana do otca, to je pruser");
                                }
                                parentEdgeBlossom = parentEdge.v.getOutermostBlossom();
                                parentEdgeBlossomIndex = j;
                            }
                        }
                        
                        if (parentEdgeBlossomIndex == -1) {
                            System.err.println("nenasla sa parentEdge bublina - pruser");
                        }
                        
                        ArrayList<Blossom> blossomPath = new ArrayList<Blossom>();
                        ArrayList<Edge> edgePath = new ArrayList<Edge>();
                        
                        // ak je parent edge blossom na neparnej pozicii (cislujeme od 0, teda 0 je neparna pre nase ucely)
                        // ideme od 0 po k vratane
                        if (parentEdgeBlossomIndex %2 == 0){
                            for(int j = 0; j <= parentEdgeBlossomIndex; j++){
                                blossomPath.add(oldGreenBlossom.blossoms.get(j));
                            }
                            
                            for(int j = 0; j < parentEdgeBlossomIndex; j++){
                                edgePath.add(oldGreenBlossom.edgesBetweenBlossoms.get(j));
                            }
                        }
                        // ak je parent edge na parnej pozicii ideme z opacnej strany kruznice od k po 0 vratane a otocime, aby sa s tym lahsie robilo potom
                        else {
                            for(int j = parentEdgeBlossomIndex; j <= containedBlossoms.size(); j++){
                                blossomPath.add(oldGreenBlossom.blossoms.get(j % containedBlossoms.size()));
                            }
                            
                            for(int j = parentEdgeBlossomIndex; j < containedBlossoms.size(); j++){
                                edgePath.add(oldGreenBlossom.edgesBetweenBlossoms.get(j));
                            }
                            Collections.reverse(blossomPath);
                            Collections.reverse(edgePath);
                        }
                        
                        // teraz ideme pridat blossomPath do madarskeho stromu
                        
                        // najprv teda vytvorime pre kazdy z blossomov novy node 
                        ArrayList<TreeNode> nodePath = new ArrayList<TreeNode>();
                        for(int j = 0; j < blossomPath.size(); j++){
                            TreeNode newNode = new TreeNode(blossomPath.get(j));
                            newNode.treeRef = currentTree;
                            blossomPath.get(j).treeNodeRef = newNode;
                            nodePath.add(newNode);
                        }
                        
                        // najprv nastavime parent node pre node tesne pred prvou bublinou na ceste
                        oldNode.children.get(0).parent = nodePath.get(0);
                        // nastavime ostatnym novym nodom parenta
                        for(int j = 0; j < nodePath.size() - 1; j++){
                            nodePath.get(j).parent = nodePath.get(j + 1);
                        }
                        // napokon nastavime parenta poslednemu nodu na ceste (zdedi ho po splasnutej bubline)
                        nodePath.get(nodePath.size() - 1).parent = oldNode.parent;
                        
                        // nastavime novym nodom childa
                        // prvy node na ceste zdedi dieta po splasnutej bubline
                        nodePath.get(0).children.add(oldNode.children.get(0));
                        // child pre ostatne nody
                        for(int j = 1; j < nodePath.size(); j++){
                            nodePath.get(j).children.add(nodePath.get(j - 1));
                        }
                        // upravime childov pre prvy node nad cestou
                        oldNode.parent.children.remove(oldNode); // odstranime stareho childa
                        oldNode.parent.children.add(nodePath.get(nodePath.size() - 1)); // nahradime novym
                        
                        // nastavime nodom na ceste a pred nou parentEdge
                        // prvemu vrcholu pod cestou parent edge menit netreba, ostane ten isty
                        // ideme nastavit tie ostatne okrem posledneho, tomu to spravime dodatocne
                        for(int j = 0; j < nodePath.size() - 1; j++){
                            nodePath.get(j).parentEdge = edgePath.get(j);
                        }
                        nodePath.get(nodePath.size() - 1).parentEdge = oldNode.parentEdge;
                    }
                    else {
                        System.err.println("Ina ako zelena bublina na neparnej urovni ma hrubku 0");
                    }
                    
                }
            }
            
            // teraz overime pripady s naplnenim hrany
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
                        // naplnena hrana
                        Edge fullEdge = new Edge(myGraph.vertices.get(i), myGraph.vertices.get(j), (int)edgeCapacity);
                        // (P2) ak sa naplnila hrana medzi kvetom na parnej urovni a cinkou
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
                            blossom2.treeNodeRef.parentEdge = fullEdge;
                        }
                        
                        // (P3) ak sa naplni hrana medzi dvomi kvetmi v strome (stat sa to moze len na parnej urovni, lebo len tam je kladny prirastok
                        else if (blossom1.treeNodeRef.treeRef == blossom2.treeNodeRef.treeRef){
                            // zoznam predchodcov vrcholu s prislusnymi hranami
                            ArrayList<TreeNode> ancestors1 = blossom1.treeNodeRef.getAncestors();
                            ArrayList<Edge> ancestorEdges1 = blossom1.treeNodeRef.getAncestorEdges();
                            
                            ArrayList<TreeNode> ancestors2 = blossom2.treeNodeRef.getAncestors();
                            ArrayList<Edge> ancestorEdges2 = blossom2.treeNodeRef.getAncestorEdges();
                            
                            //hladanie najblizsieho spolocneho predka (ideme od korena stromu)
                            int k;
                            for(k = 0; ancestors1.get(k) == ancestors2.get(k); k++){
                                
                            }
                            // k je teraz index prveho nodu, kde sa postupnost predkov od korena lisi
                            // k - 1 je tym padom index posledneho spolocneho predka
                            List<TreeNode> nodePath1 = ancestors1.subList(k - 1, ancestors1.size());
                            List<TreeNode> nodePath2 = ancestors2.subList(k , ancestors2.size());
                            Collections.reverse(nodePath2);
                            
                            // vytvorime cestu od spolocneho predka (novej stopky) po nasledujuci vrchol v kruznici, ktora bude v novej zelenej bubline
                            ArrayList<TreeNode> oddCycleNodes = new ArrayList<TreeNode>(nodePath1);
                            oddCycleNodes.addAll(nodePath2);
                            
                            
                            // prevedieme tree nody na prisluchajuce bubliny
                            ArrayList<Blossom> oddCycleBlossoms = new ArrayList<Blossom>();
                            for (int l = 0; l < oddCycleNodes.size(); l++){
                                oddCycleBlossoms.add(oddCycleNodes.get(l).containedBlossom);
                            }
                            
                            // teraz spojime hrany do kruhu
                            List<Edge> edgePath1 = ancestorEdges1.subList(k, ancestorEdges1.size());
                            List<Edge> edgePath2 = ancestorEdges2.subList(k, ancestorEdges2.size());
                            Collections.reverse(edgePath2);
                            // spojime ich do jedneho zoznamu - prva hrana ide zo stopky, posledna ide z poslednej bubliny do stopky
                            ArrayList<Edge> oddCycleEdges = new ArrayList<Edge>(edgePath1);
                            // pridame doprostred cesty este nevu naplnenu hranu
                            oddCycleEdges.add(fullEdge);
                            // pridame zvysne hrany
                            oddCycleEdges.addAll(edgePath2);
                            
                            // vytvorime novy zeleny kvet obsahujuci neparny cyklus a prislusny node v strome
                            GreenBlossom newBlossom = new GreenBlossom(oddCycleBlossoms, oddCycleEdges);
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
                        
                        // (P4) ak sa naplni hrana medzi dvomi roznymi stromami, oba stromy sa rozpadnu na cinky
                        else if (blossom1.treeNodeRef.treeRef != blossom2.treeNodeRef.treeRef){
                            ArrayList<TreeNode> nodePath1 = blossom1.treeNodeRef.getAncestors();
                            ArrayList<Edge> edgePath1 = blossom1.treeNodeRef.getAncestorEdges();
                            
                            ArrayList<TreeNode> nodePath2 = blossom2.treeNodeRef.getAncestors();
                            ArrayList<Edge> edgePath2 = blossom2.treeNodeRef.getAncestorEdges();
                            
                            // teraz vyrobime pozdlz cesty cinky
                            
                            // pre cestu v prvom strome
                            for(int k = 0; k < nodePath1.size() - 1; k++){
                                if (k % 2 == 0){
                                    Dumbbell dumb = new Dumbbell(nodePath1.get(k).containedBlossom, nodePath1.get(k + 1).containedBlossom, edgePath1.get(k));
                                    dumbbells.add(dumb);
                                }
                            }
                            
                            // pre cestu v druhom strome
                            for(int k = 0; k < nodePath2.size() - 1; k++){
                                if (k % 2 == 0){
                                    Dumbbell dumb = new Dumbbell(nodePath2.get(k).containedBlossom, nodePath2.get(k + 1).containedBlossom, edgePath2.get(k));
                                    dumbbells.add(dumb);
                                }
                            }
                            
                            // a este cinku, co vznike medzi stromami
                            Dumbbell dumb = new Dumbbell(nodePath1.get(nodePath1.size() - 1).containedBlossom, nodePath2.get(nodePath2.size() - 1).containedBlossom, fullEdge);
                            dumbbells.add(dumb);
                        }
                    }
                }
            }
        }
        
        int matchingPrice = 0;
        for(Dumbbell dumb : dumbbells){
            matchingPrice += dumb.getTotalMatchingPrice();
        }
        
        System.out.println("Minimal matching price is " + matchingPrice);
    }
}
