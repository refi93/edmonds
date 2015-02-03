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
public class Dumbbell {

    Blossom b1, b2;
    Edge connectingEdge;
    
    public Dumbbell(Blossom b1, Blossom b2, Edge e) {
        // nastavime kvetom v cinke paritu 0, teda ziadnu, lebo na ne sa nevztahuje
        // zmena(r), cize nema zmysel riesit paritu
        this.b1 = b1;
        b1.levelParity = 0;
        
        this.b2 = b2;
        b2.levelParity = 0;
        
        connectingEdge = e;
    }
    
    public int getTotalMatchingPrice(){
        return connectingEdge.price + b1.getMatchingPrice() + b2.getMatchingPrice();
    }
    
}
