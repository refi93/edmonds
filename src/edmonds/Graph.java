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
    int[][] incidenceMatrix;
    
    ArrayList<Vertex> vertices; // mnozina vrcholov - k vrcholu si pamatame bubliny, kam patri
    
    public Graph(int vertexCount) {
        this.vertexCount = vertexCount;
        incidenceMatrix = new int[vertexCount][vertexCount];
        vertices = new ArrayList<Vertex>();
        
        for(int i = 0; i < vertexCount; i++){
            vertices.add(new Vertex(i));
        }
        
        // nastavime ceny hran na nekonecno
        for (int i = 0; i < vertexCount; i++){
            for (int j = 0; j < vertexCount; j++){
                incidenceMatrix[i][j] = Variables.INFTY;
            }
        }
    }
    
    public void addEdge(int v1, int v2, int price) {
        incidenceMatrix[v1][v2] = price;
        incidenceMatrix[v2][v1] = price;
    }
    
    public double getR(){
        double r = Variables.INFTY;
        
        // najprv overime, ake r treba na naplnenie niektorej hrany
        for (int i = 0; i < vertexCount; i++){
            for(int j = 0; j < vertexCount; j++){
                Blossom b1 = vertices.get(i).getOutermostBlossom();
                Blossom b2 = vertices.get(j).getOutermostBlossom();
                
                double changeNeeded = r;
                // zmysel to ma riesit len pre dva rozne vonkajsie kvety na parnej urovni
                // cinky maju uroven 0 (ani parna, ani neparna), cize pre tie to neriesime
                if ((b1 != b2) && (b1.levelParity == 1) && (b2.levelParity == 1)){  
                    //TODO - co ak cena hrany neni parna????
                    changeNeeded = (incidenceMatrix[i][j] - b1.thickness - b2.thickness) / 2;
                }
                
                // ak jedna z bublin je cinka (ta co ma levelParity 0)
                if ((b1 != b2) && (b1.levelParity == 0) && (b2.levelParity == 1)){
                    changeNeeded = incidenceMatrix[i][j] - b1.thickness - b2.thickness;
                }
                
                if (changeNeeded < r) {
                    r = changeNeeded;
                }
            }
        }
        
        // teraz sa pozrieme na bubliny na neparnej urovni, ze o kolko ich potrebujeme splasnut, aby sa niektora dostala na 0
        for (int i = 0; i < vertexCount; i++){
            Blossom b = vertices.get(i).getOutermostBlossom();
            if (b.levelParity == -1){
                double changeNeeded = b.thickness;
                if (changeNeeded < r){
                    r = changeNeeded;
                }
            }
        }
        
        return r;
    }
}
