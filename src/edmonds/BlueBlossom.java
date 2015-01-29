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

    int id;
    
    public BlueBlossom(int id) {
        this.id = id;
        this.thickness = 0;
        this.levelParity = 1; // parita volnej modrej bubliny je 1 (parna)
    }
    
    @Override
    public int getStopka(){
        return id;
    }
    
}
