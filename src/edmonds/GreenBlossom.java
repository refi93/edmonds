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
public class GreenBlossom extends Blossom{
    
    ArrayList<Blossom> blossoms; // zoznam bublin vnutri bubliny
    
    public GreenBlossom(ArrayList<Blossom> blossoms){
        
        this.blossoms = new ArrayList<Blossom>();
        
        if (blossoms.size() % 2 != 1){
            System.err.println("NIEKTO CPE DO BLOSSOMU PARNU KRUZNICU MARHA");
        }
        
        for(int i = 0; i < blossoms.size(); i++){
            this.blossoms.add(blossoms.get(i));
        }
    }
    
    @Override
    public int getStopka(){
        return blossoms.get(0).getStopka();
    }
    
    // sluzi na updatovanie hrubky kvetu
    @Override
    public void zmena(double r) {
        thickness += r;
        if (thickness == 0){
            System.err.println("BUBLINE KLESLA HRUBKA NA 0!!!!! - DOIMPLEMENTUJ");
        }
    }
    
    
}
