package gamedata;

import interfaces.Configuration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

public class Player implements Serializable{

    private Color playerColor;
    private String playerTexture;
	private Cell position;
    private Cell wantedMove;
    private Room room;
    
    private int playerID;
    private String name;
    private Card characterCard; // carta che identifica il giocatore
    
    private ArrayList cardsOwned; // carte possedute del giocatore
    private ArrayList cardsNotSeen; // carte che il giocatore non ha ancora visto
    private ArrayList cardsSeen; // carte che il giocatore ha visto dagli altri giocatori
    
    private String ip;
    
    
	public Player(int character) {
		switch(character){
            case Configuration.PLAYER_1: {
                playerID = Configuration.PLAYER_1;
                playerColor = Color.RED;
                setName(Configuration.CHARACTER_NAME_1);
                characterCard = new Card(Configuration.CHARACTER_NAME_1);
                cardsOwned = new ArrayList();
                cardsNotSeen = new ArrayList();
                cardsSeen = new ArrayList();
                break;
            }
            
            case Configuration.PLAYER_2: {
                playerID = Configuration.PLAYER_2;
                playerColor = Color.GREEN;
                setName(Configuration.CHARACTER_NAME_2);
                characterCard = new Card(Configuration.CHARACTER_NAME_2);
                cardsOwned = new ArrayList();
                cardsNotSeen = new ArrayList();
                cardsSeen = new ArrayList();
                break;
            }
            
            case Configuration.PLAYER_3: {
                playerID = Configuration.PLAYER_3;
                playerColor = Color.MAGENTA;
                setName(Configuration.CHARACTER_NAME_3);
                characterCard = new Card(Configuration.CHARACTER_NAME_3);
                cardsOwned = new ArrayList();
                cardsNotSeen = new ArrayList();
                cardsSeen = new ArrayList();
                break;
            }
            case Configuration.PLAYER_4: {
                playerID = Configuration.PLAYER_4;
                playerColor = Color.BLUE;
                setName(Configuration.CHARACTER_NAME_4);
                characterCard = new Card(Configuration.CHARACTER_NAME_4);
                cardsOwned = new ArrayList();
                cardsNotSeen = new ArrayList();
                cardsSeen = new ArrayList();
                break;
            }
            case Configuration.PLAYER_5: {
                playerID = Configuration.PLAYER_5;
                playerColor = Color.YELLOW;
                setName(Configuration.CHARACTER_NAME_5);
                characterCard = new Card(Configuration.CHARACTER_NAME_5);
                cardsOwned = new ArrayList();
                cardsNotSeen = new ArrayList();
                cardsSeen = new ArrayList();
                break;
            }
            case Configuration.PLAYER_6: {
                playerID = Configuration.PLAYER_6;
                playerColor = Color.WHITE;
                setName(Configuration.CHARACTER_NAME_6);
                characterCard = new Card(Configuration.CHARACTER_NAME_6);
                cardsOwned = new ArrayList();
                cardsNotSeen = new ArrayList();
                cardsSeen = new ArrayList();
                break;
            }
        }
        
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        position = new Cell();
        wantedMove = null;
	}
	
    
	/**
     * @return Returns the playerColor.
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * @param playerColor The playerColor to set.
     */
    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    /**
     * @return Returns the playerTexture.
     */
    public String getPlayerTexture() {
        return playerTexture;
    }

    /**
     * @param playerTexture The playerTexture to set.
     */
    public void setPlayerTexture(String playerTexture) {
        this.playerTexture = playerTexture;
    }


    public int getX() {
		return position.getX();
	}

	public void setX(int x) {
		position.setX(x);
	}

	public int getY() {
		return position.getY();
	}

	public void setY(int y) {
		position.setY(y);
	}
	
    public void setWantedMove(Cell c){
        wantedMove = c;
    }
    
    public void setPosition(Cell c){
        position = c;
    }
    
    public Cell getWantedMove(){
        return wantedMove;
    }
    
    public Cell getPosition(){
        return position;
    }
	
        
    public Room getRoom() {
		return room;
	}


	public void setRoom(Room room) {
		this.room = room;
	}

    
   
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    
      

	/**
     * @return Returns the playerID.
     */
    public int getPlayerID() {
        return playerID;
    }


    /**
     * @param playerID The playerID to set.
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }


    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    
   
    
    /**
     * @return Returns the characterCard.
     */
    public Card getCharacterCard() {
        return characterCard;
    }


    /**
     * @return Returns the cards.
     */
    public ArrayList getCardsOwned() {
        return cardsOwned;
    }
    
    /**
     * @return Returns the cardsNotSeen
     */
    public ArrayList getNotSeenCards(){
        return cardsNotSeen;
    }
    
    /**
     * @param cards cards not seen yet
     */
    public void setNotSeenCards(ArrayList cards){
        cardsNotSeen = cards;
    }
  
    
    public ArrayList getCardsSeen() {
        return cardsSeen;
    }


    public void setCardsSeen(ArrayList cardsSeen) {
        this.cardsSeen = cardsSeen;
    }


    /**
     * add a card to the player seen cards
     * @param card the card to add
     */

    public void addSeenCard(Card c){
        cardsSeen.add(c); // aggiunge la carta alle carte viste
        removeCard(cardsNotSeen,c); // rimuove la carta da quelle non viste
        System.out.println("carte non viste: " +cardsNotSeen.size());
    }
    
    
    public void addOwnedCard(Card c){
        cardsOwned.add(c);  // aggiungo la carta a quelle possedute
        addSeenCard(c);     // aggiungo la carta tra quelle viste
    }
    
    /**
     * Removes a card from a list
     * @param l card list
     * @param c the card to remove
     * @return true if removed, false otherwise
     */
    public boolean removeCard(List l, Card c){
        boolean found = false;
        int index = -1;
        Iterator it = l.iterator();
        while(it.hasNext() && !found){
            Card card = (Card)it.next();
            if(card.getName().equals(c.getName())){
                found = true;
                index = l.indexOf(card); 
            }
        }
        if(found)l.remove(index);
        return found;    
    }
    
    
    public boolean isInARoom() {
		if(room != null) return true;
		else return false;
	}
	
	public void drawPlayer(Graphics g) {
        TextureHandler th = TextureHandler.getInstance();       
        Texture tx = th.getTexture(playerTexture);
        
        if(!isInARoom()) {
        	if(tx != null) {
        		g.drawImage(tx.getImage(),Configuration.getCellHeight(),Configuration.getCellWidth(),Color.BLACK,null);
            }else {     
            	g.setColor(playerColor);
            	g.fillOval(getX()*Configuration.getCellWidth(),getY()*Configuration.getCellHeight(),Configuration.getCellWidth(),Configuration.getCellHeight());
            }
        }
    }
    
    
    /**
     * @return Returns the IP.
     */
    public String getIP() {
        return ip;
    }


    public String toString(){
        return name;
    }
    
}
