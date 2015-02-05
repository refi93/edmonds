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
        this.thickness = 0;
        this.blossoms = blossoms;
        
        // vymazeme kvetom referenciu na stary node TODO
        //for(int i = 0; i < blossoms.size(); i++){
        //    blossoms.get(i).treeNodeRef = null;
        //}
        
        this.edgesBetweenBlossoms = edgesBetweenBlossoms;
        
        if (blossoms.size() % 2 != 1){
            System.err.println("NIEKTO CPE DO BLOSSOMU PARNU KRUZNICU MARHA");
        }
        
        if (blossoms.size() != edgesBetweenBlossoms.size()) {
            System.err.println(blossoms + " " + edgesBetweenBlossoms + " " + "NIEKTO SA SNAZI VYTVORIT BLOSSOM S PRIMALO HRANAMI!!!!!");
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
    
    @Override
    public String toString(){
        String ret = super.toString() + "[";
        ArrayList<Vertex> arr = this.getInnerVertices();
        for (Vertex v : arr){
            ret += (v.id + 1) + " ";
        }
        ret += "]";
        
        ret += "{";
        for(Edge e : this.edgesBetweenBlossoms){
            ret += e + ", ";
        }
        ret += "}";
        return ret;
    }
    
    // sluzi na updatovanie hrubky kvetu
    @Override
    public void zmena(double r) {
        if (this.levelParity == -1 && r >0 ){
            System.err.println("NESEDI PARITA -1");
        }
        
        if (this.levelParity == 1 && r <0 ){
            System.err.println("NESEDI PARITA 1");
        }
        if (thickness == 7.5){
            System.err.println("MARHAAAAAAAA " + this + " " + (thickness+r));
        }
        thickness += r;
        
        if (thickness == 0 && levelParity == -1){
            System.err.println("BUBLINE KLESLA HRUBKA NA 0!!!!!");
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
        //System.out.println(this + " " + edgesBetweenBlossoms.size());
        for(int i = 0; i < edgesBetweenBlossoms.size(); i++){
            if (i % 2 == 1){
                System.out.println(edgesBetweenBlossoms.get(i));
                ret += edgesBetweenBlossoms.get(i).price;
            }
        }
        for (int i = 0; i < blossoms.size(); i++){
            ret += blossoms.get(i).getMatchingPrice();
        }
        return ret;
    }
    
    
    public int getStopka(){
        return this.blossoms.get(0).getStopka();
    }
    
    // rekurzivne updatuje stopku bubliny tak, aby pasovala na danu hranu
    public void setStopkaByEdge(Edge e){
        setStopkaByEdge(e, 1);
        System.err.println(this + " UPDATE STOPKY: nova stopka=" + (this.getStopka() + 1) + " a hrana bola " + e );
    }
    
    // level je potrebny, aby sme sa pozerali na bublinu na spravnej urovni
    public void setStopkaByEdge(Edge e, int level){
        System.err.println(this + " LEVEL " + level);
        int newStopkaIndex = 0;
        for(newStopkaIndex = 0; true; newStopkaIndex++){ // musime tu stopku najst, inak nieco neni vporiadku
            if ((e.u.getNthOutermostBlossom(level) == this.blossoms.get(newStopkaIndex)) || (e.v.getNthOutermostBlossom(level) == this.blossoms.get(newStopkaIndex))){
                System.err.println(newStopkaIndex + " xxx " + blossoms.get(newStopkaIndex) + " JUPI");
                break;
            }
        }
        // cyklicky posunieme nas arrayList blossomov a hran o newStopkaIndex policok
        // vytvorime si pomocne pole, vykoname cyklicky posun a skopirujeme namiesto povodnych
        ArrayList<Blossom> newBlossomsOrder = new ArrayList<Blossom>();
        ArrayList<Edge> newEdgesBetweenBlossomsOrder = new ArrayList<Edge>();
        
        for(int i = newStopkaIndex; i < blossoms.size() + newStopkaIndex;i++){
            newBlossomsOrder.add(blossoms.get(i % blossoms.size()));
        }
        
        
        for(int i = 0; i < edgesBetweenBlossoms.size(); i++){
            newEdgesBetweenBlossomsOrder.add(edgesBetweenBlossoms.get((i + newStopkaIndex) % edgesBetweenBlossoms.size()));
        }
        
        this.blossoms = newBlossomsOrder;
        this.edgesBetweenBlossoms = newEdgesBetweenBlossomsOrder;
        
        
        // rekurzivne sa vnorime, aby sme updatovali stopku
        this.blossoms.get(0).setStopkaByEdge(e, level + 1);
        for(int i = 0; i < blossoms.size(); i++){
            if (i % 2 == 1){
                this.blossoms.get(i).setStopkaByEdge(edgesBetweenBlossoms.get(i), level + 1);
                this.blossoms.get(i + 1).setStopkaByEdge(edgesBetweenBlossoms.get(i), level + 1);
            }
        }
        /*
        // rekurzivne sa vnorime, aby sme updatovali stopku
        if (this.blossoms.get(0) instanceof GreenBlossom){
            ((GreenBlossom)this.blossoms.get(0)).setStopkaByEdge(e, level + 1);
        }*/
    }
    
    public void pop(){
        ArrayList<Vertex> innerVertices = new ArrayList<Vertex>();
        this.getInnerVertices(innerVertices);
        
        for(int i = 0; i < innerVertices.size(); i++){
            innerVertices.get(i).popOutermostBlossom();
        }
    }
    
}
