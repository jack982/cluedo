/*
 * IController.java
 * Created on 9-mar-2006
 *
 * author: Jacopo Penazzi
 */
package rmi;

import gamedata.Card;
import gamedata.ChatMessage;
import gamedata.SuggestionMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author Jacopo
 * 
 * 
 */
public interface IController extends Remote {
    
    public void beginTurn(int state)throws RemoteException;
    
    public void notifyGameConfiguration(GameConfiguration gc)throws RemoteException;
    
    public void startGame(GameConfiguration gc) throws RemoteException;
    
    public void ping() throws RemoteException;
    
    public void sendChatMessage(ChatMessage msg, int priority, GameConfiguration gc) throws RemoteException;
    
    public SuggestionMessage sendSuggestion(SuggestionMessage suggestion, GameConfiguration gc) throws RemoteException;
}
