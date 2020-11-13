/*
 * Card.java
 * Created on 1-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gamedata;

import java.io.Serializable;

import interfaces.Configuration;

import javax.swing.ImageIcon;

/**
 * @author Jacopo
 * 
 * 
 */
public class Card implements Serializable {
    
    private String name;
    private int type;
    private ImageIcon image;
    private String imagePath;
    
    public Card(String name){
        // carte relative alle stanze
        if(name.equals(Configuration.BALL)){
            type = Configuration.CARD_TYPE_ROOM;
            image = new ImageIcon(Configuration.BALL_ROOM_CARD_PATH);
            imagePath = Configuration.BALL_ROOM_CARD_PATH;
            this.name = name; 
        }
        else if(name.equals(Configuration.STUDY)){
            type = Configuration.CARD_TYPE_ROOM;
            image = new ImageIcon(Configuration.STUDY_ROOM_CARD_PATH);
            imagePath = Configuration.STUDY_ROOM_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.DINING)){
            type = Configuration.CARD_TYPE_ROOM;
            image = new ImageIcon(Configuration.DINING_ROOM_CARD_PATH);
            imagePath = Configuration.DINING_ROOM_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.LIBRARY)){
            type = Configuration.CARD_TYPE_ROOM;
            image = new ImageIcon(Configuration.LIBRARY_ROOM_CARD_PATH);
            imagePath = Configuration.LIBRARY_ROOM_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.KITCHEN)){
            type = Configuration.CARD_TYPE_ROOM;
            image = new ImageIcon(Configuration.KITCHEN_ROOM_CARD_PATH);
            imagePath = Configuration.KITCHEN_ROOM_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.HALL)){
            type = Configuration.CARD_TYPE_ROOM;
            image = new ImageIcon(Configuration.HALL_ROOM_CARD_PATH);
            imagePath = Configuration.HALL_ROOM_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.LOUNGE)){
            type = Configuration.CARD_TYPE_ROOM;
            image = new ImageIcon(Configuration.LOUNGE_ROOM_CARD_PATH);
            imagePath = Configuration.LOUNGE_ROOM_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.CONSERVATORY)){
            type = Configuration.CARD_TYPE_ROOM;
            image = new ImageIcon(Configuration.CONSERVATORY_ROOM_CARD_PATH);
            imagePath = Configuration.CONSERVATORY_ROOM_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.BILLIARD)){
            type = Configuration.CARD_TYPE_ROOM;
            image = new ImageIcon(Configuration.BILLIARD_ROOM_CARD_PATH);
            imagePath = Configuration.BILLIARD_ROOM_CARD_PATH;
            this.name = name;
        }
        
        // carte relative ai personaggi
        else if(name.equals(Configuration.CHARACTER_NAME_1)){
            type = Configuration.CARD_TYPE_CHARACTER;
            image = new ImageIcon(Configuration.RED_PLAYER_CARD_PATH);
            imagePath = Configuration.RED_PLAYER_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.CHARACTER_NAME_2)){
            type = Configuration.CARD_TYPE_CHARACTER;
            image = new ImageIcon(Configuration.GREEN_PLAYER_CARD_PATH);
            imagePath = Configuration.GREEN_PLAYER_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.CHARACTER_NAME_3)){
            type = Configuration.CARD_TYPE_CHARACTER;
            image = new ImageIcon(Configuration.MAGENTA_PLAYER_CARD_PATH);
            imagePath = Configuration.MAGENTA_PLAYER_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.CHARACTER_NAME_4)){
            type = Configuration.CARD_TYPE_CHARACTER;
            image = new ImageIcon(Configuration.BLUE_PLAYER_CARD_PATH);
            imagePath = Configuration.BLUE_PLAYER_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.CHARACTER_NAME_5)){
            type = Configuration.CARD_TYPE_CHARACTER;
            image = new ImageIcon(Configuration.YELLOW_PLAYER_CARD_PATH);
            imagePath = Configuration.YELLOW_PLAYER_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.CHARACTER_NAME_6)){
            type = Configuration.CARD_TYPE_CHARACTER;
            image = new ImageIcon(Configuration.WHITE_PLAYER_CARD_PATH);
            imagePath = Configuration.WHITE_PLAYER_CARD_PATH;
            this.name = name;
        }
        
        // carte relative alle armi
        else if(name.equals(Configuration.GUN)){
            type = Configuration.CARD_TYPE_WEAPON;
            image = new ImageIcon(Configuration.GUN_CARD_PATH);
            imagePath = Configuration.GUN_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.DAGGER)){
            type = Configuration.CARD_TYPE_WEAPON;
            image = new ImageIcon(Configuration.DAGGER_CARD_PATH);
            imagePath = Configuration.DAGGER_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.LEADPIPE)){
            type = Configuration.CARD_TYPE_WEAPON;
            image = new ImageIcon(Configuration.LEADPIPE_CARD_PATH);
            imagePath = Configuration.LEADPIPE_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.ROPE)){
            type = Configuration.CARD_TYPE_WEAPON;
            image = new ImageIcon(Configuration.ROPE_CARD_PATH);
            imagePath = Configuration.ROPE_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.CANDLESTICK)){
            type = Configuration.CARD_TYPE_WEAPON;
            image = new ImageIcon(Configuration.CANDLESTICK_CARD_PATH);
            imagePath = Configuration.CANDLESTICK_CARD_PATH;
            this.name = name;
        }
        else if(name.equals(Configuration.SPANNER)){
            type = Configuration.CARD_TYPE_WEAPON;
            image = new ImageIcon(Configuration.SPANNER_CARD_PATH);
            imagePath = Configuration.SPANNER_CARD_PATH;
            this.name = name;
        }
    }

    /**
     * @return Returns the image.
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the type.
     */
    public int getType() {
        return type;
    }

    public String getImagePath() {
        return imagePath;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName();
    }
    
    
    
    

}
