/*
 * IGameCreator.java
 * Created on 8-mar-2006
 *
 * author: Jacopo Penazzi
 */
package rmi;

import gamedata.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Jacopo
 * 
 * 
 */
public interface IGameCreator extends Remote {

    /**
     * Join a new game
     * @return a list of available characters
     * @throws RemoteException
     * @throws MaxNumberOfPlayerJoinedException 
     */
    public ArrayList join() throws RemoteException, MaxNumberOfPlayerJoinedException;
    
    /**
     * Notify to the server the client player
     * @param p client player
     * @return null if server accepts client player, an ArrayList with available characters otherwise
     * @throws RemoteException
     * @throws MaxNumberOfPlayerJoinedException 
     */
    public ArrayList ready(Player p) throws RemoteException, MaxNumberOfPlayerJoinedException;
    
    
}
