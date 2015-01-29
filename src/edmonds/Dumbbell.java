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
public class Dumbbell extends Blossom{

    int levelParity;
    Blossom b1, b2;
    
    public Dumbbell(BlueBlossom b1, BlueBlossom b2) {
        this.levelParity = 0;
        this.b1 = b1;
        this.b2 = b2;
    }
    
}
