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
class Blossom {
    
    int thickness;
    int levelParity;
    
    // rekurzivne vrati stopku bubliny
    public int getStopka(){
        return -10;
    }
    
    // sluzi na updatovanie hrubky kvetu
    public void zmena(int r) {
        thickness += r;
    }
}