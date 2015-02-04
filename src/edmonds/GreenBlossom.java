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
    
    ArrayList<Blossom> blossoms; // zoznam bublin vnutri bubliny, v tej prvej v poradi je vzdy stopka
    ArrayList<Edge> edgesBetweenBlossoms; // na i-tej pozicii je hrana medzi i a (i+1) % n
    
    public GreenBlossom(ArrayList<Blossom> blossoms, ArrayList<Edge> edgesBetweenBlossoms){
        
        this.blossoms = blossoms;
        
        // vymazeme kvetom referenciu na stary node TODO
        //for(int i = 0; i < blossoms.size(); i++){
        //    blossoms.get(i).treeNodeRef = null;
        //}
        
        this.edgesBetweenBlossoms = edgesBetweenBlossoms;
        
        if (blossoms.size() % 2 != 1){
            System.err.println("NIEKTO CPE DO BLOSSOMU PARNU KRUZNICU MARHA");
        }
        
        // updatujeme vnutornym vrcholom referenciu na blossom
        for(int i = 0; i < blossoms.size(); i++){
            Blossom b = blossoms.get(i);
            if (b instanceof GreenBlossom) {
                ArrayList<Vertex> innerVertices = ((GreenBlossom)b).getInnerVertices();
                for(int j = 0; j < innerVertices.size(); j++){
                    innerVertices.get(j).addToBlossom(this);
                }
            }
            else {
                ((BlueBlossom)b).vertex.addToBlossom(this);
            }
        }
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
    
    @Override
    public int getMatchingPrice(){
        int ret = 0;
        for(int i = 0; i < edgesBetweenBlossoms.size(); i++){
            if (i % 2 == 1){
                ret += edgesBetweenBlossoms.get(i).price;
            }
        }
        for (int i = 0; i < blossoms.size(); i++){
            ret += blossoms.get(i).getMatchingPrice();
        }
        return ret;
    }
    
    
    // rekurzivne updatuje stopku bubliny tak, aby pasovala na danu hranu
    public void setStopkaByEdge(Edge e){
        setStopkaByEdge(e, 1);
    }
    
    // level je potrebny, aby sme sa pozerali na bublinu na spravnej urovni
    private void setStopkaByEdge(Edge e, int level){
        int newStopkaIndex = 0;
        for(newStopkaIndex = 0;newStopkaIndex < this.blossoms.size(); newStopkaIndex++){
            if ((e.u.getNthOutermostBlossom(level) == this) || (e.v.getNthOutermostBlossom(level) == this)){
                break;
            }
        }
        // cyklicky posunieme nas arrayList blossomov a hran o newStopkaIndex policok
        // vytvorime si pomocne pole, vykoname cyklicky posun a skopirujeme namiesto povodnych
        ArrayList<Blossom> newBlossomsOrder = new ArrayList<Blossom>();
        ArrayList<Edge> newEdgesBetweenBlossomsOrder = new ArrayList<Edge>();
        
        for(int i = 0; i < blossoms.size();i++){
            newBlossomsOrder.add(blossoms.get((i + newStopkaIndex) % blossoms.size()));
        }
        
        for(int i = 0; i < newEdgesBetweenBlossomsOrder.size(); i++){
            newEdgesBetweenBlossomsOrder.add(edgesBetweenBlossoms.get((i + newStopkaIndex) % edgesBetweenBlossoms.size()));
        }
        
        this.blossoms = newBlossomsOrder;
        this.edgesBetweenBlossoms = newEdgesBetweenBlossomsOrder;
        
        // rekurzivne sa vnorime, aby sme updatovali stopku
        if (this.blossoms.get(0) instanceof GreenBlossom){
            ((GreenBlossom)this.blossoms.get(0)).setStopkaByEdge(e, level + 1);
        }
    }
    
    public void pop(){
        ArrayList<Vertex> innerVertices = new ArrayList<Vertex>();
        this.getInnerVertices(innerVertices);
        
        for(int i = 0; i < innerVertices.size(); i++){
            innerVertices.get(i).popOutermostBlossom();
        }
    }
    
}
