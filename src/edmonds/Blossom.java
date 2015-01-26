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
public class Blossom {
    ArrayList<Blossom> blossoms; // list of blossoms inside blossom
    
    public Blossom(ArrayList<Blossom> blossoms){
        this.blossoms = new ArrayList<Blossom>();
        
        if (blossoms.size() % 2 != 1){
            System.err.println("NIEKTO CPE DO BLOSSOMU PARNU KRUZNICU MARHA");
        }
        
        for(int i = 0; i < blossoms.size(); i++){
            this.blossoms.add(blossoms.get(i));
        }
    }
    
    public Blossom getStopka(){
        return blossoms.get(0);
    }
}
