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
    int thickness; // hrubka bubliny
    Edge parentEdge; // hrana z grafu spajajuca kvet s otcovskym kvetom
    Blossom parent; // kto je jeho parent vramci madarskeho stromu
    ArrayList<Blossom> children; // zoznam deti vramci madarskeho stromu
    int levelParity; // ci je v parovani +1/-1
    
    // inicializacia spolocna pre jednoduchy aj zlozeny blossom
    private void init(){
        this.blossoms = new ArrayList<Blossom>();
        this.thickness = 0;
        this.parent = null;
        this.children = new ArrayList<Blossom>();
        this.levelParity = 1;
        this.parentEdge = null;
    }
    
    // jednovrcholova bublina
    public Blossom(int id){
        init();
        this.id = id;
    }
    
    // bublina pozostavajuca z viacero vrcholov
    public Blossom(ArrayList<Blossom> blossoms){
        init();
        this.id = -1;
        
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
    
    // sluzi na updatovanie hrubky kvetu - do dalsej urovne sa updatuje opacne, cize -r
    public void zmena(int r){
        this.thickness += r;
        
        for(int i = 0;i < children.size(); i++){
            children.get(i).zmena(-r);
        }
    }
}