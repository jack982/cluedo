/*
 * GameConfiguration.java
 * Created on 8-mar-2006
 *
 * author: Jacopo Penazzi
 */
package rmi;

import gamedata.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Jacopo
 * 
 * 
 */
public class GameConfiguration implements Serializable, Cloneable {

    
    private static GameConfiguration instance;
    
    private int state;
    private String senderIP;
    private Player playingPlayer;
    private ArrayList players;
    private ArrayList solution;
    private ArrayList notPlayingCharacters;
    private ArrayList rooms;
    private ArrayList weapons;
    
    
    public static synchronized GameConfiguration getInstance(){
        if(instance == null){
            instance = new GameConfiguration();
        }
        return instance;
    }
    
    
    
    private GameConfiguration() {
    	this.players = new ArrayList();
    	this.solution = new ArrayList();
    	this.notPlayingCharacters = new ArrayList();
    	this.state = -1;
    	this.senderIP = null;
    	this.playingPlayer = null;
    	this.rooms = new ArrayList();
        this.weapons = new ArrayList();
    }  
    
    
    
    
    /**
     * @return Returns the players.
     */
    public ArrayList getPlayers() {
        return players;
    }
    /**
     * @param players The players to set.
     */
    public void setPlayers(ArrayList players) {
        this.players = players;
    }
    /**
     * @return Returns the solution.
     */
    public ArrayList getSolution() {
        return solution;
    }
    /**
     * @param solution The solution to set.
     */
    public void setSolution(ArrayList solution) {
        this.solution = solution;
    }
    
    /**
     * @return Returns the playingPlayer.
     */
    public Player getPlayingPlayer() {
        return playingPlayer;
    }
    
    /**
     * @param playingPlayer The playingPlayer to set.
     */
    public void setPlayingPlayer(Player playingPlayer) {
        this.playingPlayer = playingPlayer;
    }
    
    /**
     * @return Returns the senderIP.
     */
    public String getSenderIP() {
        return senderIP;
    }
    
    /**
     * @param senderIP The senderIP to set.
     */
    public void setSenderIP(String senderIP) {
        this.senderIP = senderIP;
    }
    
    /**
     * @return Returns the turn.
     */
    public int getState() {
        return state;
    }
    
    /**
     * @param turn The turn to set.
     */
    public void setState(int state) {
        this.state = state;
    }
    
    public void addState(){
        this.state = this.state + 1;
    }
    
	public ArrayList getNotPlayingCharacters() {
		return notPlayingCharacters;
	}
	
	public void setNotPlayingCharacters(ArrayList notPlayingCharacters) {
		this.notPlayingCharacters = notPlayingCharacters;
	}


    public ArrayList getRooms() {
        return rooms;
    }


    public void setRooms(ArrayList rooms) {
        this.rooms = rooms;
    }


    public ArrayList getWeapons() {
        return weapons;
    }

    
    public void setWeapons(ArrayList weapons) {
        this.weapons = weapons;
    }
    
    
    public ArrayList getAllCharacters(){
        ArrayList newlist = new ArrayList();
        
        newlist.addAll(this.getPlayers());
        newlist.addAll(this.getNotPlayingCharacters());
        
        return newlist;
    }
    
    
    public static synchronized void mergeGameConfiguration(GameConfiguration newConfiguration, Player currentPlayer){
        // se non sono il sender della nuova configurazione ...
        if(!newConfiguration.getSenderIP().equals(currentPlayer.getIP())){
            // ... controllo se ci sono aggiornamenti relativi ai client
            if(instance.getPlayers().size() == newConfiguration.getPlayers().size()){
                // controllo se è il mio turno (mio turno impossibile)
                if(newConfiguration.getPlayingPlayer().getPlayerID() != currentPlayer.getPlayerID()){
                    if(newConfiguration.getState() == instance.getState()){
                        //UPDATE PLAYING PLAYER POS
                        instance.setPlayingPlayer(newConfiguration.getPlayingPlayer());
                        //SET SENDER IP
                        instance.setSenderIP(newConfiguration.getSenderIP());
                        
                    }else{
                        //UPDATE PLAYING PLAYER POS + MY TURN
                        instance.setPlayingPlayer(newConfiguration.getPlayingPlayer());
                        instance.setState(newConfiguration.getState());
                        //SET SENDER IP
                        instance.setSenderIP(newConfiguration.getSenderIP());
                    }
                }
            }
            else { // liste diverse
                if(newConfiguration.getPlayingPlayer().getPlayerID() == currentPlayer.getPlayerID()){
                    // è il mio turno
                    if(newConfiguration.getState() < instance.getState()){
                        // remove dead players
                        instance.setPlayers(newConfiguration.getPlayers());
                        // cambio sender ip con il mio
                        instance.setSenderIP(currentPlayer.getIP());
                    }
                    else {
                        // remove dead players
                        instance.setPlayers(newConfiguration.getPlayers());
                        //SET SENDER IP
                        instance.setSenderIP(newConfiguration.getSenderIP());
                    }
                }
                else { // non è il mio turno
                    if(newConfiguration.getState() == instance.getState()){
                        // remove dead players
                        instance.setPlayers(newConfiguration.getPlayers());
                        //SET SENDER IP
                        instance.setSenderIP(newConfiguration.getSenderIP());
                    }
                    else {
                        // remove dead players
                        instance.setPlayers(newConfiguration.getPlayers());
                        // update mio turno
                        instance.setState(newConfiguration.getState());
                        //SET SENDER IP
                        instance.setSenderIP(newConfiguration.getSenderIP());
                    }
                }
            }
        }
    }
    
    
    public static synchronized void updateGameConfiguration(GameConfiguration newGameConfiguration){
        instance = newGameConfiguration;
    }
    
    
    
    public synchronized Object clone(){
        Object copy = null;
        try {
            copy = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return copy;
    }
    
}
