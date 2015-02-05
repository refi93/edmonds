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
        b1.dumbbellRef = this;
        b1.treeNodeRef = null;
        
        this.b2 = b2;
        b2.levelParity = 0;
        b2.dumbbellRef = this;
        b2.treeNodeRef = null;
        // este upravime stopky, aby sedeli (mali by byt pri hrane, ktora spaja cinky)
        if (b1 instanceof GreenBlossom){
            GreenBlossom gb1 = (GreenBlossom) b1;
            gb1.setStopkaByEdge(e);
        }
        
        if (b2 instanceof GreenBlossom){
            GreenBlossom gb2 = (GreenBlossom) b2;
            gb2.setStopkaByEdge(e);
        }
        
        connectingEdge = e;
    }
    
    @Override
    public String toString(){
        if (false && b1 instanceof BlueBlossom && b2 instanceof BlueBlossom){
            return (((BlueBlossom)b1).vertex.id + 1) + "--" + (((BlueBlossom)b2).vertex.id + 1);
        }
        else {
            return b1 + " " + b2;
        }
    }
    
    public int getTotalMatchingPrice(){
        System.out.println(connectingEdge);
        return connectingEdge.price + b1.getMatchingPrice() + b2.getMatchingPrice();
    }
    
}
