/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author raf
 */
public class Graph {
    int vertexCount;
    int[][] incidenceMatrix, fullfillMatrix;
    
    
    public Graph(int vertexCount) {
        this.vertexCount = vertexCount;
        incidenceMatrix = new int[vertexCount][vertexCount];
        fullfillMatrix = new int[vertexCount][vertexCount];
        
        // nastavime ceny hran na nekonecno
        for (int i = 0; i < vertexCount; i++){
            for (int j = 0; j < vertexCount; j++){
                incidenceMatrix[i][j] = 47000;
                fullfillMatrix[i][j] = 0;
            }
        }
    }
    
    public void addEdge(int v1, int v2, int price) {
        incidenceMatrix[v1][v2] = price;
        incidenceMatrix[v2][v1] = price;
    }
    
    // naplnanie hran
    public void fillEdge(int v1, int v2, int val) {
        fullfillMatrix[v1][v2] += val;
        fullfillMatrix[v2][v1] += val;
    }
}
