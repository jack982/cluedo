/*
 * Weapon.java
 * Created on 28-feb-2006
 *
 * author: Jacopo Penazzi
 */
package gamedata;

import interfaces.Configuration;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * @author Jacopo
 * 
 * 
 */
public class Weapon {
    
    private String name;
    private Card weaponCard;
    
    public Weapon(String name){
        if(name.equals(Configuration.GUN)){
            this.name = Configuration.GUN;
            weaponCard = new Card(Configuration.GUN);
        }
        else if(name.equals(Configuration.DAGGER)){
            this.name = Configuration.DAGGER;
            weaponCard = new Card(Configuration.DAGGER);
        }
        else if(name.equals(Configuration.LEADPIPE)){
            this.name = Configuration.LEADPIPE;
            weaponCard = new Card(Configuration.LEADPIPE);
        }
        else if(name.equals(Configuration.SPANNER)){
            this.name = Configuration.SPANNER;
            weaponCard = new Card(Configuration.SPANNER);
        }
        else if(name.equals(Configuration.CANDLESTICK)){
            this.name = Configuration.CANDLESTICK;
            weaponCard = new Card(Configuration.CANDLESTICK);
        }
        else if(name.equals(Configuration.ROPE)){
            this.name = Configuration.ROPE;
            weaponCard = new Card(Configuration.ROPE);
        }
    }
    
   
    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public Card getWeaponCard(){
        return weaponCard;
    }
    
    public String toString(){
        return name;
    }
    
   
    
    
    

}
