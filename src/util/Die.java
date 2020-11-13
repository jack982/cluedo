/*
 * Die.java
 * Created on 8-feb-2006
 *
 * author: Jacopo Penazzi
 */
package util;

import interfaces.Configuration;

/**
 * @author Jacopo
 * 
 * 
 */
public class Die {

    private int numfaces;
    
    public Die(){
        numfaces = Configuration.MAX_DICE_FACES;
    }
        
    public Die(int numfaces){
        this.numfaces = numfaces;
    }
    
    
    public int rollDie(){
        return (int) (Math.random()*numfaces)+1;
    }
    
    
  
    
}
