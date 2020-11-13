/*
 * GameController.java
 * Created on 19-feb-2006
 *
 * author: Jacopo Penazzi
 */
package rmi;

import interfaces.Configuration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;


import sun.security.x509.AVA;
import util.Die;
import util.MyPriorityQueue;
import gamedata.Card;
import gamedata.Cell;
import gamedata.ChatMessage;
import gamedata.GameBoard;
import gamedata.PathSelector;
import gamedata.Player;
import gamedata.Room;
import gamedata.SuggestionMessage;
import gamedata.Weapon;
import gui.ChatPanel;
import gui.ControlPanel;
import gui.PanelGameBoard;
import gui.SendCardDialog;
import gui.WaitingForStartDialog;

/**
 * @author Jacopo
 * 
 * 
 */
public class GameController extends UnicastRemoteObject implements IController {
    
    private PathSelector ps;
    private Player currentPlayer;
 //   private PanelGameBoard panelBoard;
    private ChatPanel chatPanel;
    private ControlPanel controlPanel;
    private GameBoard gameBoard;
    private MyPriorityQueue messageQueue;
    
    private JScrollPane scrollpane;
    private int movesLeft;
       
    private Die die;
    
    private WaitingForStartDialog wsd;
    
    private GameConfiguration currentConfiguration;
    
    private PingThread pinger;   
    
    ArrayList queuedChatMessages;
        
    
    public GameController() throws RemoteException {
      
        gameBoard = GameBoard.getInstance();
//        panelBoard = p;
        ps = PathSelector.getInstance();
        die = new Die();
        currentPlayer = null;
        movesLeft = 0;
//        currentConfiguration =  new GameConfiguration();
//        currentConfiguration.setPlayingPlayer(currentPlayer);
        messageQueue = new MyPriorityQueue();
        queuedChatMessages = new ArrayList();
    }
    
    
    public synchronized void movePlayer(){
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        
        Cell wantedMove = currentPlayer.getWantedMove();
           // se la mossa scelta è valida 
        if(wantedMove != null && isValidMove(wantedMove)){
               
           //controllo se sto entrando in una stanza
            Iterator r = gc.getRooms().iterator();
            
            boolean found = false;
            Room currentRoom = null;
            
            while(r.hasNext() && !found) {
                Room room = (Room)r.next();
            	if(room.isInside(wantedMove)) {
            	    currentRoom = room;
            		found = true;
            	}
            }
            
            if(found) {
            	//sto entrando in una stanza
                centerViewOnPlayer();
            	movesLeft = 0;
            	currentPlayer.setRoom(currentRoom);
            	currentRoom.addPlayer(currentPlayer);
        //    	panelBoard.highlightRoom(currentRoom,true);        	   
            }
            else {
                //è una normale mossa nel tabellone
            	centerViewOnPlayer();
            	currentPlayer.setPosition(wantedMove);
            	if(currentPlayer.isInARoom()) {
            	    //sto uscendo da una stanza
            	    currentPlayer.getRoom().getPlayers().remove(currentPlayer);
            	//	   panelBoard.highlightRoom(currentPlayer.getRoom(),false);
            	    currentPlayer.setRoom(null);
            		   
            	}
            	   //decrementa il numero delle mosse ancora disponibili
                movesLeft--;
            }
            
            
            //TODO: gestire l'indice del player
               
            System.out.println("SPOSTATO!");
            
            // update GameConfiguration   
            gc.setPlayingPlayer(currentPlayer);
            gc.setSenderIP(currentPlayer.getIP());
            GameConfiguration.updateGameConfiguration(gc);
            
          //FIXME:  forwardGameConfiguration(); // notifica lo spostamento agli altri client
               
               
          // richiama la visualizzazione delle nuove mosse possibili
            if(movesLeft > 0){
                showPaths(currentPlayer);
                currentPlayer.setWantedMove(null);
            }
            else {
            	ps.clearPath();
            	//FIXME
            	// controlla se è dentro una stanza
            	// se è fuori deve passare il turno
            }
            
        }
        else { // mossa iniziale o mossa non valida 
               
            // mostra le mosse possibili al giocatore
            showPaths(currentPlayer);
            if(movesLeft > 0){
                showPaths(currentPlayer);
                currentPlayer.setWantedMove(null);
            }
            else ps.clearPath();
        }
           
     //     panelBoard.repaint();
           
    }
    
    
    public boolean isValidMove(Cell cell){
        if(ps.isAllowedMove(cell)) return true;
        return false;
    }
    
    
    private void showPaths(Player player){
        // disegna i path possibili per la mossa corrente
    	ps.createPath(player);
    }
    
    
    public void setPlayerMove(Cell move){
        currentPlayer.setWantedMove(move);
    }
  
          
    public int getMovesLeft(){
        return movesLeft;
    }
    
    public int rollDie(){
        movesLeft = die.rollDie();
        System.out.println("DADO: "+movesLeft);
        return movesLeft;
    }
    
    
    public void setCurrentPlayer(Player p){
        currentPlayer = p;
        
       // centerViewOnPlayer();
    }
    
    public Player getCurrentPlayer(){
        return currentPlayer;
    }
    /*
    public ArrayList getPlayers(){
        return currentConfiguration.getPlayers();
    }
    */
    public void setScrollPane(JScrollPane scrollpane) {
    	this.scrollpane = scrollpane;
    }
    
    
    
    public void centerViewOnPlayer() {
    	JViewport view = scrollpane.getViewport();
        
        int panelWidth = scrollpane.getWidth();
        int panelHeight = scrollpane.getHeight();
        int playerX = currentPlayer.getX()*Configuration.getCellWidth();
        int playerY = currentPlayer.getY()*Configuration.getCellHeight();
        
        
        
        int newX = playerX - (panelWidth/2);
        int newY = playerY - (panelHeight/2);
        
        view.setViewSize(new Dimension(scrollpane.getWidth(),scrollpane.getHeight()));
        view.setViewPosition(new Point(newX,newY));
        
        scrollpane.setViewport(view);
    	
    }
    
    
    private void updateViewOnPlayer() {
    	JViewport view = scrollpane.getViewport();
    	Point currentView = new Point(
    			currentPlayer.getPosition().getX() * Configuration.getCellWidth(),
    			currentPlayer.getPosition().getY() * Configuration.getCellHeight()
    			);
    	Point newView = new Point(currentPlayer.getWantedMove().getX() * Configuration.getCellWidth(),
    							  currentPlayer.getWantedMove().getY() * Configuration.getCellHeight());
    	view.setViewPosition(
    			new Point(
    					((int)currentView.getX() + ((int)currentView.getX() - (int)newView.getX())),
    					((int)currentView.getY() + ((int)currentView.getY() - (int)newView.getY()))
    	));
    	
    	scrollpane.setViewport(view);
    			
    }

/*
    public void setCurrentConfiguration(GameConfiguration gc){
        this.currentConfiguration = gc;
    }
    
    public GameConfiguration getCurrentConfiguration(){
        return currentConfiguration;
    }
*/
    
   /* 
    public synchronized void updateGameConfiguration(GameConfiguration newConfiguration){
        // se non sono il sender della nuova configurazione ...
        if(!newConfiguration.getSenderIP().equals(currentPlayer.getIP())){
            // ... controllo se ci sono aggiornamenti relativi ai client
            if(currentConfiguration.getPlayers().size() == newConfiguration.getPlayers().size()){
                // controllo se è il mio turno (mio turno impossibile)
                if(newConfiguration.getPlayingPlayer().getPlayerID() != currentPlayer.getPlayerID()){
                    if(newConfiguration.getState() == currentConfiguration.getState()){
                        //UPDATE PLAYING PLAYER POS
                        currentConfiguration.setPlayingPlayer(newConfiguration.getPlayingPlayer());
                        //SET SENDER IP
                        currentConfiguration.setSenderIP(newConfiguration.getSenderIP());
                    	
                    }else{
                        //UPDATE PLAYING PLAYER POS + MY TURN
                        currentConfiguration.setPlayingPlayer(newConfiguration.getPlayingPlayer());
                        currentConfiguration.setState(newConfiguration.getState());
                        //SET SENDER IP
                        currentConfiguration.setSenderIP(newConfiguration.getSenderIP());
                    }
                }
            }
            else { // liste diverse
                if(newConfiguration.getPlayingPlayer().getPlayerID() == currentPlayer.getPlayerID()){
                    // è il mio turno
                    if(newConfiguration.getState() < currentConfiguration.getState()){
                        // remove dead players
                        currentConfiguration.setPlayers(newConfiguration.getPlayers());
                        // cambio sender ip con il mio
                        currentConfiguration.setSenderIP(currentPlayer.getIP());
                    }
                    else {
                        // remove dead players
                        currentConfiguration.setPlayers(newConfiguration.getPlayers());
                        //SET SENDER IP
                        currentConfiguration.setSenderIP(newConfiguration.getSenderIP());
                    }
                }
                else { // non è il mio turno
                    if(newConfiguration.getState() == currentConfiguration.getState()){
                        // remove dead players
                        currentConfiguration.setPlayers(newConfiguration.getPlayers());
                        //SET SENDER IP
                        currentConfiguration.setSenderIP(newConfiguration.getSenderIP());
                    }
                    else {
                        // remove dead players
                        currentConfiguration.setPlayers(newConfiguration.getPlayers());
                        // update mio turno
                        currentConfiguration.setState(newConfiguration.getState());
                        //SET SENDER IP
                        currentConfiguration.setSenderIP(newConfiguration.getSenderIP());
                    }
                }
            }
        }
        // aggiorno il mio stato globale in base alla nuova configurazione
        Iterator i = currentConfiguration.getPlayers().iterator();
        boolean found = false;
        Player player = null;
        while(i.hasNext() && !found){
            player = (Player)i.next();
            if(player.getPlayerID() == currentPlayer.getPlayerID()){
                found = true;
            }
        }
        if(found){
            //mantengo in memoria l'attuale stanza in cui mi trovo
        	Room currentRoom = null;
        	
        	if(currentPlayer.isInARoom()) {
        		currentRoom = currentPlayer.getRoom();
        	}
        	        	
        	this.setCurrentPlayer(player);
            
            //controllo se sono stato spostato in una stanza
            if(currentPlayer.isInARoom()) {
 //           	panelBoard.highlightRoom(currentRoom,false);	//spengo la precedente stanza
 //           	panelBoard.highlightRoom(currentPlayer.getRoom(),true);	//accendo la nuova stanza
            }
//            panelBoard.repaint();
        }
        
        
        //forward della configurazione al mio successore
        forwardGameConfiguration();
        
        
    }
    
  */  
    
    public void setWaitDialog(WaitingForStartDialog wsd){
        this.wsd = wsd;
    }
    
    
    
    
    public synchronized void alarmDeadPlayer(String deadIP) {
        
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        
        boolean isPlayingPlayerDead = false;
        boolean found = false;
        boolean exception = false;
        int index = -1;
        
        IController remoteController = null;
        Player nextPlayer = null;
        
        Player deadPlayer = null; 
        
        // imposto il sender
        gc.setSenderIP(this.getCurrentPlayer().getIP());
         
        // cerco il player da rimuovere perchè è morto
        Iterator iter = gc.getPlayers().iterator();
         
       
         
        while(iter.hasNext() && !found){
            deadPlayer = (Player)iter.next();
            if(deadPlayer.getIP().equals(deadIP)){
                index = gc.getPlayers().indexOf(deadPlayer);
                found = true;
            }
        }
         
        // controllo se sono riuscito a trovare il player morto. Se non lo trovo esco dalla alarm
        // perchè significa che la configurazione è già aggiornata
        if(found){
            gc.getPlayers().remove(index);
             
            // controllo se il client morto era il playing player
            if(deadPlayer.getPlayerID() == gc.getPlayingPlayer().getPlayerID()){
                isPlayingPlayerDead = true;
            }
            
            // "ridistribuisce" le carte del giocatore morto
            Iterator i = gc.getPlayers().iterator();
            Iterator j = null;
             
            // x ogni giocatore...
            while(i.hasNext()){
                Player p = (Player)i.next();
                j = deadPlayer.getCardsOwned().iterator();
                // ... aggiunge le carte del morto
                while(j.hasNext()){
                    Card c = (Card)j.next();
                    p.addSeenCard(c);
                }
            }
            
            // aggiorno il mio currentPlayer in base alla nuova configurazione
            iter = gc.getPlayers().iterator();
            found = false;
            Player myself = null;
            
            // trovo me stesso nella nuova configurazione
            while(i.hasNext() && !found){
                myself = (Player)i.next();
                if(myself.getPlayerID() == currentPlayer.getPlayerID()){
                    found = true;
                }
            }
            // setto il currentPlayer a me stesso nella nuova configurazione
            if(found){
                this.setCurrentPlayer(myself);
            }
        
            
            // aggiorno la GameConfiguration
            GameConfiguration.updateGameConfiguration(gc);
            
            
            //cerca il nuovo client successore
            nextPlayer = findNextPlayer(gc);
            
            if(nextPlayer != null) {
            
	            found = true;
	            exception = false;
	            
	            
	            System.setSecurityManager(new RMISecurityManager());
	            
	            // controllo se il playing player era morto e in questo caso setto
	            // il successore come nuovo plating player
	            if(isPlayingPlayerDead){
	            	gc.setPlayingPlayer(nextPlayer);
	            }
	                    
	            
	            ChatMessage chatMsg = null;
	            try {
	            	// tenta di connettersi al client successivo
	            	remoteController = (IController) Naming.lookup("rmi://"+nextPlayer.getIP()+"/"+Configuration.CLIENT_SERVICE_NAME);
	                        
	                        
	            	// prepara SYSTEM chat message per informare che un client è morto
	            	String mess = "Player "+deadPlayer+" died. The game will be reconfigured.";
	            	chatMsg = new ChatMessage(null,mess);
	                                              
	            	Iterator m = queuedChatMessages.iterator();
	                        
	            	// invia i messaggi accodati
	            	while(m.hasNext()) {
	            		ChatMessage msg = (ChatMessage)m.next();
	            		int msgIndex = queuedChatMessages.indexOf(msg);
	            		remoteController.sendChatMessage(msg,ChatMessage.SYSTEM_MESSAGE_TYPE,gc);
	            		queuedChatMessages.remove(msgIndex);
	            	}
	                        
	            	remoteController.sendChatMessage(chatMsg,ChatMessage.SYSTEM_MESSAGE_TYPE,gc);
	                        
	            	// notifica la nuova configurazione
                   	remoteController.notifyGameConfiguration(gc);
	            	
	            } catch (MalformedURLException e) {
	            	e.printStackTrace();
	            } catch (RemoteException e) {
	            	// il client è morto
	            	exception = true;
	            	found = false;
	                        
	            	//accodo il messaggio
	            	queuedChatMessages.add(chatMsg);
	                        
	            	e.printStackTrace();
	            } catch (NotBoundException e) {
	            	// il client è morto
	            	exception = true;
	            	found = false;
	                        
	            	//accodo il messaggio
	            	queuedChatMessages.add(chatMsg);
	                        
	            	e.printStackTrace();
	            }
	            
		        if(exception){
		        	// tenta con il successivo
		        	alarmDeadPlayer(nextPlayer.getIP());
		        }
		        // setto il pinger sul nuovo client 
		        else if(found) {
		                
		        	if(isPlayingPlayerDead){
		        			
		        		try {
		        			// tenta di connettersi al client successivo
		        			remoteController = (IController) Naming.lookup("rmi://"+nextPlayer.getIP()+"/"+Configuration.CLIENT_SERVICE_NAME);
		        			
		        			// passa il turno
		        			remoteController.beginTurn(gc.getState());
		        			
		        			// invia SYSTEM chat message per informare del passaggio del turno
		        			String msg = "Player "+nextPlayer+" begins his turn.";
		        			chatMsg = new ChatMessage(null,msg);
		        			remoteController.sendChatMessage(chatMsg,ChatMessage.SYSTEM_MESSAGE_TYPE,gc);
		                        
		        		} catch (MalformedURLException e) {
		        			e.printStackTrace();
		        		} catch (RemoteException e) {
		        			// il client è morto
		        			exception = true;
		                                                
		        			e.printStackTrace();
		        		} catch (NotBoundException e) {
		        			e.printStackTrace();
		                }
		                    
		        		if(exception){
		        			alarmDeadPlayer(nextPlayer.getIP());
		                }
		        	}
		        	
		        	PingThread pinger = new PingThread(this,nextPlayer.getIP());
		        	pinger.start();
		        }
     
	        }
	        // sono rimasto da solo a giocare ... partita finita :(
	        else {
	        	JOptionPane.showMessageDialog(null,"All players are offline. The game will be aborted","Game Aborted",JOptionPane.ERROR_MESSAGE);
	        	// TODO: disabilitare RMI Registry
	        	System.exit(0);
	        }
        }
    }
        
        
    
    
    private synchronized void startNewPinger(GameConfiguration gc){
        //trovo il mio successore
        Player nextPlayer = findNextPlayer(gc);
    	        
        // avvia il pinger
        if(nextPlayer != null){
            pinger = new PingThread(this,nextPlayer.getIP());
            pinger.start();
        }
    }
        
    
    
    public synchronized void forwardGameConfiguration(GameConfiguration gc){
        
        boolean exception = false;
        IController remoteController = null;
        Player nextPlayer = null;
        
         
        // cerca il client successore
        nextPlayer = findNextPlayer(gc);
               
        System.setSecurityManager(new RMISecurityManager());
        
        
        if(nextPlayer != null) {
                
        	try {
        		// tenta di connettersi al client successivo
        		remoteController = (IController) Naming.lookup("rmi://"+nextPlayer.getIP()+"/"+Configuration.CLIENT_SERVICE_NAME);
        		// notifica la nuova configurazione
        		remoteController.notifyGameConfiguration(gc);
                
        	} catch (MalformedURLException e) {
        		e.printStackTrace();
        	} catch (RemoteException e) {
        		// il client è morto
        		exception = true;
        		e.printStackTrace();
        	} catch (NotBoundException e) {
        		// il client è morto
        		exception = true;
        		e.printStackTrace();
        	}
        
        	if(exception){
        		// tenta con il successivo
        		alarmDeadPlayer(nextPlayer.getIP());
        	}
        }
        //  sono rimasto da solo a giocare ... partita finita :(
        else {
        	JOptionPane.showMessageDialog(null,"All players are offline. The game will be aborted","Game Aborted",JOptionPane.ERROR_MESSAGE);
        	// TODO: disabilitare RMI Registry
        	System.exit(0);
        }
    }
    
    
    
    public synchronized SuggestionMessage forwardSuggestion(SuggestionMessage suggestion,GameConfiguration gc){
        boolean exception = false;
               
        Player nextPlayer = findNextPlayer(gc);
            
        if(nextPlayer != null && !nextPlayer.getIP().equals(suggestion.getSenderIP())){
            IController remoteController;
                
            try {
                remoteController = (IController)Naming.lookup("rmi://"+nextPlayer.getIP()+"/"+Configuration.CLIENT_SERVICE_NAME);
                suggestion = remoteController.sendSuggestion(suggestion,gc);
                    
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RemoteException e) {
                exception = true;
                e.printStackTrace();
            } catch (NotBoundException e) {
                exception = true;
                e.printStackTrace();
            }
        
            if(exception){
                alarmDeadPlayer(nextPlayer.getIP());
                suggestion.setException(true);
                suggestion.setReturnCard(null);
            }
        }
        else if(nextPlayer.getIP().equals(suggestion.getSenderIP())){
            suggestion.setReturnCard(null);
            suggestion.setException(false);
        }
        else if(nextPlayer == null){
            JOptionPane.showMessageDialog(null,"All players are offline. The game will be aborted","Game Aborted",JOptionPane.ERROR_MESSAGE);
            // TODO: disabilitare RMI Registry
            System.exit(0);
        }
        
        return suggestion;
        
    }
    
    
  
    
    public void setChatPanel(ChatPanel chatPanel) {
    	this.chatPanel = chatPanel;
    }
    
    

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    
    public void enableGUIButtons(boolean b){
        controlPanel.enableDieButton(b);
        controlPanel.enableSuggestButton(b);
        controlPanel.enableAccuseButton(b);
        controlPanel.enableSecretPassageButton(b);
        controlPanel.enablePassTurnButton(b);
    }
    
    
    
    public synchronized Player findNextPlayer(GameConfiguration gc) {
                
    	Player nextPlayer = null;
    	
    	Iterator i = gc.getPlayers().iterator();
    	boolean found = false;
    	
    	while(i.hasNext() && !found) {
    		nextPlayer = (Player)i.next();
    		if(currentPlayer.getPlayerID() == nextPlayer.getPlayerID()) {
    			nextPlayer = (Player)i.next();
    			found = true;
    		}
    	}
    	
    	return nextPlayer;
    }
    
    
   
    
    
    //----------------- RMI METHODS --------------//
    

    public void beginTurn(int state) throws RemoteException {
        synchronized (currentConfiguration) {
            if(state > currentConfiguration.getState()) {
            	currentConfiguration.addState();
            	currentConfiguration.setPlayingPlayer(currentPlayer);
            	
            	JOptionPane.showMessageDialog(null,"It's your turn","Play!",JOptionPane.INFORMATION_MESSAGE);
          
                enableGUIButtons(true);
            }
        }
    }
    
    
    public void notifyGameConfiguration(GameConfiguration gc) throws RemoteException {
    	//FIXME: controllo del flag attivo/passivo
    	// se sono attivo faccio il merge
    	// se sono passivo faccio update
    	// in entrambi i casi faccio forward
        // faccio il merge tra la configurazione corrente e quella appena ricevuta 
        GameConfiguration.mergeGameConfiguration(gc,currentPlayer);
        // forward della configurazione al mio successore
        forwardGameConfiguration(GameConfiguration.getInstance());
    }


    public void startGame(GameConfiguration gc) throws RemoteException {
        if(wsd != null){
            wsd.enableStartButton(true);
        }
        
        // avvia il nuovo pinger
        startNewPinger(gc);  
      
    }


    public void ping() throws RemoteException {
        // do nothing       
    }
    


   
	public void sendChatMessage(ChatMessage msg, int priority, GameConfiguration gc) throws RemoteException {

    	messageQueue.push(msg,priority);
    	
    	ChatMessage message = (ChatMessage)messageQueue.pop();
    	
    	chatPanel.printMessage(message);
    	
    	//forward del messaggio al successore
        
    	Player nextPlayer = findNextPlayer(gc);
        
        if(nextPlayer != null) {
        	System.setSecurityManager(new RMISecurityManager());
        	try {
				IController remoteController = remoteController = (IController) Naming.lookup("rmi://"+nextPlayer.getIP()+"/"+Configuration.CLIENT_SERVICE_NAME);
				remoteController.sendChatMessage(msg,priority,gc);
        	} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				
				alarmDeadPlayer(nextPlayer.getIP());
				
				e.printStackTrace();
			} catch (NotBoundException e) {

				alarmDeadPlayer(nextPlayer.getIP());
				
				e.printStackTrace();
			}
        }
        else {
            JOptionPane.showMessageDialog(null,"All players are offline. The game will be aborted","Game Aborted",JOptionPane.ERROR_MESSAGE);
            // TODO: disabilitare RMI Registry
            System.exit(0);
        }
	}


    
	public SuggestionMessage sendSuggestion(SuggestionMessage suggestion, GameConfiguration gc) throws RemoteException {
		
		System.setSecurityManager(new RMISecurityManager());
		
        ArrayList availableCards = new ArrayList();
        
		if(suggestion.isShowYourCards()) { 
			// controllo se ho almeno una delle carte richieste
			Iterator i = null;
			Iterator j = suggestion.getRequestedCards().iterator();
			boolean found = false;
			
			while(j.hasNext()) {
				Object obj = j.next();
				Card c = null;
				
				if(obj instanceof Player) {
					c = ((Player)obj).getCharacterCard();
				}else if(obj instanceof Weapon){
					c = ((Weapon)obj).getWeaponCard();
				}
				else if(obj instanceof Room){
					c = ((Room)obj).getRoomCard();
				}
				
				// cerco la carta richiesta fra le mie carte
				i = currentPlayer.getCardsOwned().iterator();
			
                while(i.hasNext() && !found) {
					Card myCard = (Card)i.next();
					
					if(myCard.getName().equals(c.getName())) {
						found = true;
						availableCards.add(c);
					}
				}
			}
			
			
			if(availableCards.size() > 0) {
				// se possiedo almeno una carta di quelle richieste, mostro la SendCardDialog per la scelta
				SendCardDialog scd = new SendCardDialog(suggestion.getRequestedCards(),availableCards,true);
                Card c = scd.getReturnValue();
                scd.dispose();
                suggestion.setReturnCard(c);
                suggestion.setException(false);
                suggestion.setShowYourCards(false);
                forwardSuggestion(suggestion,gc);
                return suggestion;
			}
			else {
               // non possiedo niente da mostrare quindi creo una dialog informativa sulle carte richieste ... 
               SendCardDialog scd = new SendCardDialog(suggestion.getRequestedCards(),availableCards,false); 
			   // ... e faccio la forward della suggestion
               suggestion.setShowYourCards(true);
               return forwardSuggestion(suggestion,gc);
                
			}
		}else {
            // un player prima di me ha già mostrato una carta tra quelle richieste quindi creo una dialog informativa delle carte richieste
            SendCardDialog scd = new SendCardDialog(suggestion.getRequestedCards(),availableCards,false);
            // e faccio la forward della suggestion
            suggestion.setShowYourCards(false);
            return forwardSuggestion(suggestion,gc);
        }
		
	}


	
    
    
    
}
