/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author raf
 */
public class Edmonds {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        
        
        int n = input.nextInt();
        int edgeCount = input.nextInt();
        
        HashSet<Dumbbell> dumbbells = new HashSet<Dumbbell>(); // cinky
        ArrayList<HungarianTree> hungarianForest = new ArrayList<HungarianTree>(); // madarsky les
        Graph myGraph = new Graph(n);
        
        for(int i = 0; i < edgeCount; i++){
            int u = input.nextInt() - 1, 
                v = input.nextInt() - 1, 
                price = input.nextInt();
            myGraph.addEdge(u, v, price);
        }
        /*
        myGraph.addEdge(0, 1, 10);
        myGraph.addEdge(1, 3, 2);
        myGraph.addEdge(1, 2, 1);
        myGraph.addEdge(2, 3, 1);
        myGraph.addEdge(2, 0, 10);
        */
        
        // vrcholy pridame do modrych bublin a vytvorime pre kazdu z nich novy madarsky strom a pridame do lesa
        for(int i = 0; i < n; i++){
            Vertex v = myGraph.vertices.get(i);
            
            BlueBlossom b = new BlueBlossom(v);
            TreeNode node = new TreeNode(b);
            HungarianTree t = new HungarianTree(node);
            
            hungarianForest.add(t);
        }
        
        // dokym nemame parovanie
        while (hungarianForest.size() != 0){
            double r = myGraph.getR();
            System.out.println(r);
            
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
                        
                        ArrayList<Blossom> innerBlossoms = oldGreenBlossom.blossoms;
                        ArrayList<Edge> edgesBetweenInnerBlossoms = oldGreenBlossom.edgesBetweenBlossoms;
                        
                        ArrayList<Vertex> innerVertices = oldGreenBlossom.getInnerVertices();
                        // overime, ze ma skutocne jedno dieta
                        if (oldNode.getChildren().size() != 1){
                            System.err.println("Vrchol na neparnej urovni nema jedneho syna, problem");
                        }
                        
                        // ideme odstranovat z vrcholov referenciu na bublinu, ktora prave splasla
                        oldGreenBlossom.pop();
                        
                        // ideme zistit, ktorej bubline vo vnutri tej splasnutej prisluchala odchadzajuca hrana
                        Edge parentEdge = oldGreenBlossom.treeNodeRef.getParentEdge();
                        Blossom parentEdgeBlossom = null; // bublina obsahujuca parent edge
                        int parentEdgeBlossomIndex = -1; // index bubliny obsahujucej parent edge
                        
                        for(int j = 0; j < innerBlossoms.size(); j++){
                            // snazime sa trafit jeden z koncov hrany
                            if (innerBlossoms.get(j) == parentEdge.u.getOutermostBlossom()){
                                if (parentEdgeBlossom != null) {
                                    System.err.println("z dvoch bublin odchadza hrana do otca, to je pruser");
                                }
                                parentEdgeBlossom = parentEdge.u.getOutermostBlossom();
                                parentEdgeBlossomIndex = j;
                                break;
                            }
                            else if (innerBlossoms.get(j) == parentEdge.v.getOutermostBlossom()){
                                if (parentEdgeBlossom != null) {
                                    System.err.println("z dvoch bublin odchadza hrana do otca, to je pruser");
                                }
                                parentEdgeBlossom = parentEdge.v.getOutermostBlossom();
                                parentEdgeBlossomIndex = j;
                                break;
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
                                blossomPath.add(innerBlossoms.get(j));
                            }
                            
                            for(int j = 0; j < parentEdgeBlossomIndex; j++){
                                edgePath.add(edgesBetweenInnerBlossoms.get(j));
                            }
                            
                            // zo zvysnych bublin vyrobime cinky
                            for (int j = parentEdgeBlossomIndex + 1; j < oldGreenBlossom.blossoms.size(); j++){
                                if (j % 2 == 1){
                                    dumbbells.add(new Dumbbell(innerBlossoms.get(j), innerBlossoms.get(j + 1), edgesBetweenInnerBlossoms.get(j)));
                                }
                            }
                        }
                        // ak je parent edge na parnej pozicii ideme z opacnej strany kruznice od k po 0 vratane a otocime, aby sa s tym lahsie robilo potom
                        else {
                            for(int j = parentEdgeBlossomIndex; j <= innerBlossoms.size(); j++){
                                blossomPath.add(oldGreenBlossom.blossoms.get(j % innerBlossoms.size()));
                            }
                            
                            for(int j = parentEdgeBlossomIndex; j < innerBlossoms.size(); j++){
                                edgePath.add(oldGreenBlossom.edgesBetweenBlossoms.get(j));
                            }
                            Collections.reverse(blossomPath);
                            Collections.reverse(edgePath);
                            
                            // zo zvysnych bublin vyrobime cinky
                            for (int j = 1; j < parentEdgeBlossomIndex; j++){
                                if (j % 2 == 1){
                                    dumbbells.add(new Dumbbell(innerBlossoms.get(j), innerBlossoms.get(j + 1), edgesBetweenInnerBlossoms.get(j)));
                                }
                            }
                        }
                        
                        // teraz ideme pridat blossomPath do madarskeho stromu
                        
                        // najprv teda vytvorime pre kazdy z blossomov novy node 
                        ArrayList<TreeNode> nodePath = new ArrayList<TreeNode>();
                        
                        for(int j = 0; j < blossomPath.size(); j++){
                            TreeNode newNode = new TreeNode(blossomPath.get(j));
                            newNode.treeRef = currentTree;
                            blossomPath.get(j).levelParity = -1 + 2 * (j % 2); // nastavime spravnu paritu kvetom na ceste v strome
                            nodePath.add(newNode);
                        }
                        
                        
                        
                        // najprv nastavime parent node a parent Edge pre node tesne pred prvou bublinou na ceste
                        TreeNode oldChild = oldNode.getChildren().get(0);
                        oldChild.setParent(nodePath.get(0), oldChild.getParentEdge());
                        // nastavime ostatnym novym nodom parenta a parentEdge
                        for(int j = 0; j < nodePath.size() - 1; j++){
                            nodePath.get(j).setParent(nodePath.get(j + 1), edgePath.get(j));
                        }
                        // napokon nastavime parenta a parentEdge poslednemu nodu na ceste (zdedi ho po splasnutej bubline)
                        nodePath.get(nodePath.size() - 1).setParent(oldNode.getParent(), oldNode.getParentEdge());
                        
                        // nakoniec este stary node odstranime z deti
                        oldNode.getParent().getChildren().remove(oldNode);
                    }
                    else {
                        System.err.println("Ina ako zelena bublina na neparnej urovni ma hrubku 0");
                    }
                    
                }
            }
            
            // teraz overime pripady s naplnenim hrany
            for (int i = 0; i < myGraph.vertexCount; i++){
                for (int j = 0; j < myGraph.vertexCount; j++){
                    if (i == j){
                        continue;
                    }
                    Blossom blossom1 = myGraph.vertices.get(i).getOutermostBlossom();
                    Blossom blossom2 = myGraph.vertices.get(j).getOutermostBlossom();
                    
                    double edgeCapacity = myGraph.incidenceMatrix[i][j];
                    if (i == 3 && j == 31){
                        System.out.println(i + " " + j + " " + blossom1.thickness + " " + blossom2.thickness + " " + blossom1.levelParity + " " + blossom2.levelParity);
                    }
                    
                    if (blossom1.thickness + blossom2.thickness > edgeCapacity){
                        System.err.println(blossom1 + " " + blossom2 + " " + blossom1.levelParity + " " + blossom2.levelParity + " " + blossom1.thickness + " " + blossom2.thickness + " " + edgeCapacity + " " + "PRESIAHNUTA KAPACITA HRANY, NIECO SA POSRALO");
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
                            TreeNode newNodeFirst;
                            TreeNode newNodeSecond;
                            
                            // napasujeme cinku, resp. nove nody na kvet, kde sa naplnila hrana
                            if (dumb.b1 == blossom2){
                                newNodeFirst = new TreeNode(dumb.b1); // tento sa bude prvy pripajat na strom
                                dumb.b1.levelParity = -1;
                                newNodeSecond = new TreeNode(dumb.b2);
                                dumb.b2.levelParity = 1;
                            }
                            else {
                                newNodeFirst = new TreeNode(dumb.b2);
                                dumb.b2.levelParity = -1;
                                newNodeSecond = new TreeNode(dumb.b1);
                                dumb.b1.levelParity = 1;
                            }
                            
                            
                            newNodeFirst.setParent(blossom1.treeNodeRef, fullEdge);
                            newNodeSecond.setParent(newNodeFirst, dumb.connectingEdge);
                        }
                        
                        // (P3) ak sa naplni hrana medzi dvomi kvetmi v strome (stat sa to moze len na parnej urovni, lebo len tam je kladny prirastok
                        else if (blossom1.treeNodeRef != null && blossom2.treeNodeRef != null && 
                                blossom1.levelParity == 1 && blossom2.levelParity == 1 && 
                                blossom1.treeNodeRef.treeRef == blossom2.treeNodeRef.treeRef){
                            
                            HungarianTree currentTree = blossom1.treeNodeRef.treeRef;
                            
                            // zoznam predchodcov vrcholu s prislusnymi hranami
                            ArrayList<TreeNode> ancestors1 = blossom1.treeNodeRef.getAncestors();
                            ArrayList<Edge> ancestorEdges1 = blossom1.treeNodeRef.getAncestorEdges();
                            
                            ArrayList<TreeNode> ancestors2 = blossom2.treeNodeRef.getAncestors();
                            ArrayList<Edge> ancestorEdges2 = blossom2.treeNodeRef.getAncestorEdges();
                            
                            //hladanie najblizsieho spolocneho predka (ideme od korena stromu)
                            int nearestCommonAncestorId;
                            for(nearestCommonAncestorId = 0; nearestCommonAncestorId < Math.min(ancestors1.size(), ancestors2.size()) && ancestors1.get(nearestCommonAncestorId) == ancestors2.get(nearestCommonAncestorId); nearestCommonAncestorId++){ 
                            }
                            nearestCommonAncestorId--;

                            // vytvorime cestu od spolocneho predka (novej stopky) po nasledujuci vrchol v kruznici, ktora bude v novej zelenej bubline
                            List<TreeNode> nodePath1 = ancestors1.subList(nearestCommonAncestorId, ancestors1.size());
                            List<TreeNode> nodePath2 = ancestors2.subList(nearestCommonAncestorId + 1 , ancestors2.size());
                            Collections.reverse(nodePath2);
                            ArrayList<TreeNode> oddCycleNodes = new ArrayList<TreeNode>(nodePath1);
                            oddCycleNodes.addAll(nodePath2);
                            
                            
                            // prevedieme tree nody na prisluchajuce bubliny
                            ArrayList<Blossom> oddCycleBlossoms = new ArrayList<Blossom>();
                            for (int l = 0; l < oddCycleNodes.size(); l++){
                                oddCycleBlossoms.add(oddCycleNodes.get(l).containedBlossom);
                            }
                            
                            // teraz spojime hrany do kruhu
                            List<Edge> edgePath1 = ancestorEdges1.subList(nearestCommonAncestorId, ancestorEdges1.size());
                            List<Edge> edgePath2 = ancestorEdges2.subList(nearestCommonAncestorId, ancestorEdges2.size());
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
                            newNode.treeRef = currentTree; // nastavime novemu nodu treeRef, lebo nie vzdy sa nam pri pripojeni podari ziskat referenciu na strom
                            newBlossom.levelParity = 1; // je na parnej urovni                            
                            
                            // updatujeme kvetom v neparnom cykle referenciu na node do ktoreho patria
                            for (int l = 0; l < oddCycleBlossoms.size(); l++) {
                                oddCycleBlossoms.get(l).treeNodeRef = newNode;
                            }
                            
                            // nastavime novemu nodu parenta - je nim povodny parent novej stopky
                            // tymto sa zaroven zdedi referencia na strom
                            newNode.setParent(oddCycleNodes.get(0).getParent(), oddCycleNodes.get(0).getParentEdge());
                            
                            // 
                            for (int l = 0; l < oddCycleNodes.size(); l++){
                                TreeNode oddCycleNode = oddCycleNodes.get(l);
                                for (int m = 0; m < oddCycleNode.getChildren().size(); m++){
                                    TreeNode child = oddCycleNode.getChildren().get(m);
                                    // nechceme updatovat kvety vo vnutri newNode, len tie vonku a tie su zaroven childovia nasho noveho nodu, tak ich tam rovno pridame
                                    if (child.containedBlossom.treeNodeRef != newNode) {
                                        // pridame vrcholy mimo noveho nodu do zoznamu deti noveho nodu
                                        child.setParent(newNode, child.getParentEdge());
                                    }
                                }
                            }
                            
                            // napokon stopku novej bubliny odregistrujeme spomedzi deti jej parenta
                            if (oddCycleNodes.get(0).getParent() != null) {
                                oddCycleNodes.get(0).getParent().getChildren().remove(oddCycleNodes.get(0));
                            }
                        }
                        
                        // (P4) ak sa naplni hrana medzi dvomi roznymi stromami, oba stromy sa rozpadnu na cinky
                        else if (blossom1.treeNodeRef != null && blossom2.treeNodeRef != null && blossom1.treeNodeRef.treeRef != blossom2.treeNodeRef.treeRef){
                            HungarianTree tree1 = blossom1.treeNodeRef.treeRef;
                            ArrayList<TreeNode> nodePath1 = blossom1.treeNodeRef.getAncestors();
                            ArrayList<Edge> edgePath1 = blossom1.treeNodeRef.getAncestorEdges();
                            
                            HungarianTree tree2 = blossom2.treeNodeRef.treeRef;
                            ArrayList<TreeNode> nodePath2 = blossom2.treeNodeRef.getAncestors();
                            ArrayList<Edge> edgePath2 = blossom2.treeNodeRef.getAncestorEdges();
                            
                            
                            // pridame cinky na ktore sa rozpadli oba stromy, okrem tej cinky, co ma tu naplnenu hranu
                            dumbbells.addAll(tree1.breakToDumbbells(new HashSet<TreeNode>(nodePath1)));
                            dumbbells.addAll(tree2.breakToDumbbells(new HashSet<TreeNode>(nodePath2)));
                            
                            // a este pridame cinku, co vznike naplnenou hranou medzi stromami
                            //Dumbbell dumb = new Dumbbell(nodePath1.get(nodePath1.size() - 1).containedBlossom, nodePath2.get(nodePath2.size() - 1).containedBlossom, fullEdge);
                            Dumbbell dumb = new Dumbbell(blossom1, blossom2, fullEdge);
                            dumbbells.add(dumb);
                            
                            // odstranime stromy z lesa, kedze sa uz rozpadli na cinky
                            hungarianForest.remove(tree1);
                            hungarianForest.remove(tree2);
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
