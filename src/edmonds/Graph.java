/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author raf
 */
public class Graph {
    int vertexCount;
    
    // matica susednosti, kde si pamatame ceny
    ArrayList<Edge> edgeList;
    
    ArrayList<Vertex> vertices; // mnozina vrcholov - k vrcholu si pamatame bubliny, kam patri
    
    // matica susednosti, kde si pamatame ceny
    //int[][] incidenceMatrix;
    
    public Graph(int vertexCount) {
        this.vertexCount = vertexCount;
        //incidenceMatrix = new int[vertexCount][vertexCount];
        vertices = new ArrayList<>();
        edgeList = new ArrayList<>();
        
        for(int i = 0; i < vertexCount; i++){
            vertices.add(new Vertex(i));
        }
        
        // nastavime ceny hran na nekonecno
        //for (int i = 0; i < vertexCount; i++){
        //    for (int j = 0; j < vertexCount; j++){
        //        incidenceMatrix[i][j] = Variables.INFTY;
        //    }
        //}
    }
    
    public void addEdge(int u, int v, int price) {
        //incidenceMatrix[u][v] = price;
        //incidenceMatrix[v][u] = price;
        edgeList.add(new Edge(vertices.get(u), vertices.get(v), price));
        edgeList.add(new Edge(vertices.get(v), vertices.get(u), price));
    }
    
    public double getR(){
        double r = Variables.INFTY;
        // najprv overime, ake r treba na naplnenie niektorej hrany medzi bublinami pripadne cinkami
        for(Edge e : edgeList){
            Blossom b1 = vertices.get(e.u.id).getOutermostBlossom();
            Blossom b2 = vertices.get(e.v.id).getOutermostBlossom();
            Vertex v1 = vertices.get(e.u.id);
            Vertex v2 = vertices.get(e.v.id);
            
            
            double changeNeeded = r;
            // zmysel to ma riesit len pre dva rozne vonkajsie kvety na parnej urovni
            // cinky maju uroven 0 (ani parna, ani neparna), cize pre tie to neriesime
            if ((b1 != b2) && (b1.levelParity == 1) && (b2.levelParity == 1)){
                changeNeeded = (e.price - v1.getCharge()- v2.getCharge()) / 2;
            }

            // ak jedna z bublin je cinka (ta co ma levelParity 0)
            else if ((b1.levelParity == 0) && (b2.levelParity == 1)){
                changeNeeded = e.price - v1.getCharge() - v2.getCharge();
                /*if (changeNeeded < 0){
                    System.err.println("VYSIEL ZAPORNY CHANGE, NIECO SA POSRALO");
                }*/
            }

            if (changeNeeded < r) {
                r = changeNeeded;
            }
        }
        
        
        // teraz sa pozrieme na zelene bubliny na neparnej urovni, ze o kolko ich potrebujeme splasnut,
        // aby sa niektora dostala na 0
        for (int i = 0; i < vertexCount; i++){
            Blossom b = vertices.get(i).getOutermostBlossom();
            if (b instanceof GreenBlossom && b.levelParity == -1){
                double changeNeeded = b.thickness;
                if (changeNeeded < r){
                    r = changeNeeded;
                }
            }
        }
        return r;
    }
}