/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edmonds;

/**
 *
 * @author raf
 */

// one-vertex blossom
public class BlueBlossom extends Blossom{

    Vertex vertex;
    
    public BlueBlossom(Vertex v) {
        this.vertex = v;
        this.thickness = 0;
        this.levelParity = 1; // parita volnej modrej bubliny je 1 (parna)
        v.addToBlossom(this);
    }
    
    @Override
    public String toString(){
        return super.toString() + "[" + (this.vertex.id + 1) + "]";
    }
    
}
