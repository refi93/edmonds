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
    ArrayList<Edge> edgesBetweenBlossoms; // na i-tej pozicii je hrana medzi i a (i+1) % n
    
    public GreenBlossom(ArrayList<Blossom> blossoms, ArrayList<Edge> edgesBetweenBlossoms){
        
        this.blossoms = blossoms;
        this.edgesBetweenBlossoms = edgesBetweenBlossoms;
        
        if (blossoms.size() % 2 != 1){
            System.err.println("NIEKTO CPE DO BLOSSOMU PARNU KRUZNICU MARHA");
        }
        
        for(int i = 0; i < blossoms.size(); i++){
            this.blossoms.add(blossoms.get(i));
        }
    }
    
    @Override
    public Vertex getStopka(){
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
    
    // vrati zoznam vrcholov vo vnutri bubliny
    public ArrayList<Vertex> getInnerVertices(){
        ArrayList<Vertex> ret = new ArrayList<Vertex>();
        getInnerVertices(ret);
        return ret;
    }
    
    private void getInnerVertices(ArrayList<Vertex> vertexList){
        for (int i = 0; i < blossoms.size(); i++) {
            if (blossoms.get(i) instanceof BlueBlossom){
                vertexList.add(((BlueBlossom)blossoms.get(i)).vertex);
            }
            else if (blossoms.get(i) instanceof GreenBlossom) {
                ((GreenBlossom)blossoms.get(i)).getInnerVertices(vertexList);
            }
            else {
                System.err.println("getInnerVertices: ani zeleny ani modry blossom, to je cudne");
            }
        }
    }
    
}
