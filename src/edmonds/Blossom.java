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
    ArrayList<Blossom> blossoms; // zoznam bublin vnutri bubliny
    int id = -1; // default hodnota vyjadrujuca, ze ide o zlozenu bublinu
    int thickness;
    
    // jednovrcholova bublina
    public Blossom(int id){
        this.blossoms = new ArrayList<Blossom>();
        this.id = id;
        this.thickness = 0;
    }
    
    // bublina pozostavajuca z viacero vrcholov
    public Blossom(ArrayList<Blossom> blossoms){
        this.id = -1;
        this.thickness = 0;
        this.blossoms = new ArrayList<Blossom>();
        
        if (blossoms.size() % 2 != 1){
            System.err.println("NIEKTO CPE DO BLOSSOMU PARNU KRUZNICU MARHA");
        }
        
        for(int i = 0; i < blossoms.size(); i++){
            this.blossoms.add(blossoms.get(i));
        }
    }
    
    // rekurzivne vrati stopku bubliny
    public int getStopka(){
        if (this.id != -1) return id;
        return blossoms.get(0).getStopka();
    }
}