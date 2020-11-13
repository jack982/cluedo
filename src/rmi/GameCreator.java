package rmi;

import gamedata.Card;
import gamedata.Door;
import gamedata.Player;
import gamedata.RectArea;
import gamedata.Room;
import gamedata.SecretPassage;
import gamedata.Weapon;
import gui.StartWizardNew;

import interfaces.Configuration;

import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JDialog;

import util.Deck;





public class GameCreator extends UnicastRemoteObject implements IGameCreator {
	
	private ArrayList availableCharacters;
	private ArrayList joinedPlayers;
    private String serviceName;
    private Deck deck;
    private Player currentPlayer;
    
    private ArrayList monitorThreads;
    
    
    private GameConfiguration gameConfig;
    
    private StartWizardNew wizardGUI;
	
	
	public GameCreator(String serviceName, StartWizardNew parent) throws RemoteException {
		
        this.serviceName = serviceName;
        this.wizardGUI = parent;
        
		availableCharacters = new ArrayList();		
		joinedPlayers = new ArrayList();
		
        deck = new Deck();
                            
        availableCharacters = deck.getCharacterCards();
        
        monitorThreads = new ArrayList();
	}
	
	
    
    
	public ArrayList join() throws RemoteException, MaxNumberOfPlayerJoinedException {
		
		synchronized (joinedPlayers) {
			if(joinedPlayers.size() < Configuration.MAX_PLAYERS) {
				return availableCharacters;
			}
			else {
				throw new MaxNumberOfPlayerJoinedException();
			}
		}
    }
	
	
    
	public ArrayList ready(Player p) throws RemoteException, MaxNumberOfPlayerJoinedException {
		
		ArrayList retList = null;
        
		synchronized (joinedPlayers) {
		    if(joinedPlayers.size() == Configuration.MAX_PLAYERS){
                throw new MaxNumberOfPlayerJoinedException();
            }
            else if(joinedPlayers.size() > Configuration.MAX_PLAYERS){
                System.err.println("GameCreator.ready(): ERRORE MADORNALE!");
            }
            else{
                synchronized (availableCharacters) {
                    
                    Iterator i = availableCharacters.iterator();
                    boolean found = false;
                    Card cardChosen = null;
                    
                    while(i.hasNext() && !found) {
                        Card card = (Card)i.next();
                        if(card.getName().equals(p.getCharacterCard().getName())) {
                            found = true;
                            cardChosen = card;
                        }
                    }
                    
                    if(!found) {
                        retList = availableCharacters;
                    }
                    else {
                        removeCard(cardChosen);
                        joinedPlayers.add(p);
                        wizardGUI.addLabelJoinedPlayers();
                        // lancia il monitor sul client
                        MonitorThread monitor = new MonitorThread(this,p.getIP());
                        monitorThreads.add(monitor);
                        monitor.start();
                    }
                }

            }
        }
		return retList;
	}

    
    // TODO: ciclo di notifica limitato a 1 giocatore
    
    public void sendNotifies(){
        System.setSecurityManager(new RMISecurityManager());
        
        Iterator iter = joinedPlayers.iterator();
        
        int index = -1;
        boolean error = false;
        
        while(iter.hasNext() && !error){
            Player player = (Player)iter.next();
            String ip = player.getIP();
            
            try {
                IController remoteController = null;
                remoteController = (IController)Naming.lookup("rmi://"+ip+"/"+Configuration.CLIENT_SERVICE_NAME);
                    
                //  notifica la configurazione della partita al client
                remoteController.notifyGameConfiguration(gameConfig); 
                
            } catch (MalformedURLException e) {
            	e.printStackTrace();
            } catch (NotBoundException e) {
            	index = joinedPlayers.indexOf(player);
            	error = true;
            	e.printStackTrace();
            } catch(RemoteException e) {
            	e.printStackTrace();
            	index = joinedPlayers.indexOf(player);
            	error = true;
            }
        }
        if(error){
            // elimina il player che ha causasto l'errore
            // riconfigura tutto e ripete il ciclo di notify
            joinedPlayers.remove(index);
            createGameConfig();
            sendNotifies();
        }
        else { // ho notificato tutti i giocatori
               
            
            //invio le start a tutti i client che ho notificato
            //con successo
                   
            Iterator p = joinedPlayers.iterator();
            while(p.hasNext()) {
                Player player = (Player)p.next();
                try {
                    IController remoteController = null;
                    remoteController = (IController)Naming.lookup("rmi://"+player.getIP()+"/"+Configuration.CLIENT_SERVICE_NAME);
                    
                    //invio start ai client (compreso me stesso)
                    remoteController.startGame(gameConfig);
                        
                } catch (MalformedURLException e) {
                	e.printStackTrace();
                } catch (NotBoundException e) {
                	
                	// NB: la notbound exception non viene gestita perchè da questo momento entra in gioco
                	// il protocollo di fault tolerance
                	
                	e.printStackTrace();
                }
                catch(RemoteException e) {
                	
                	// NB: la remote exception non viene gestita perchè da questo momento entra in gioco
                	// il protocollo di fault tolerance
                	
                	e.printStackTrace();
                }
            }
        }
    }
    
    
    public void createGameConfig(){
              
        // scegli le 3 carte della soluzione della partita
        ArrayList solution = new ArrayList();
        
        // estrazione di un personaggio
        Card character = deck.pickRandomCard(deck.getCharacterCards());
        solution.add(character);
        // estrazione di un'arma
        Card weapon = deck.pickRandomCard(deck.getWeaponCards());
        solution.add(weapon);
        // estrazione di una stanza
        Card room = deck.pickRandomCard(deck.getRoomCards());
        solution.add(room);
        
        // distribuisci le rimanenti carte ai giocatori
        int index = 0;
        
        while(deck.getSize() > 0){
            Card c = (Card)deck.pickRandomCard(deck.getRemainingCards());
            ((Player)joinedPlayers.get(index % deck.getSize())).addOwnedCard(c);
            index++;
        }
                    
        // inizializza la configurazione
        gameConfig = GameConfiguration.getInstance();
        gameConfig.setPlayers(joinedPlayers);
        gameConfig.setSolution(solution);
        gameConfig.setState(0);
        gameConfig.setSenderIP(currentPlayer.getIP());
        gameConfig.setPlayingPlayer(currentPlayer);
        
        // inserimento notPlayingPlayers
        gameConfig.setNotPlayingCharacters(createNotPlayingCharacters());
        
        // inserimento rooms
        gameConfig.setRooms(createRooms());
        
        // inserimento weapons
        gameConfig.setWeapons(createWeapons());
    
        // assegna le weapons alle room
        assignWeapons();
        
    }
    
    
    

    public void setCurrentPlayer(Player p){
        currentPlayer = p;
    }
	

    public void alarmDeadPlayer(String deadIP){
        
        // elimino il monitor del client morto
        synchronized (monitorThreads) {
            Iterator iter = monitorThreads.iterator();
            boolean found = false;
            int index = -1;
            
            while(iter.hasNext() && !found){
                MonitorThread monitor = (MonitorThread)iter.next();
                if(monitor.getCurrentIP().equals(deadIP)){
                    index = monitorThreads.indexOf(monitor);
                    found = true;
                }
            }
            
            if(found){
                monitorThreads.remove(index);
            }
        }
        
        
        synchronized (joinedPlayers) {
             // cerco il player da rimuovere perchè è morto
             Iterator iter = joinedPlayers.iterator();
             
             boolean found = false;
             int index = -1;
             
             while(iter.hasNext() && !found){
                 Player player = (Player)iter.next();
                 if(player.getIP().equals(deadIP)){
                     index = joinedPlayers.indexOf(player);
                     found = true;
                 }
             }
             
             if(found){
                 joinedPlayers.remove(index);
                 wizardGUI.decLabelJoinedPlayers();
             }
        }
    }
    
    
	public void removeCard(Card cardChosen) {
		
		Iterator i = availableCharacters.iterator();
		boolean found = false;
		int indexToRemove = -1;
		
		while(i.hasNext() && !found) {
			Card card = (Card)i.next();
			if(card.getName().equals(cardChosen.getName())) {
				found = true;
				indexToRemove = availableCharacters.indexOf(card);
			}
		}
		
		if(found) {
			availableCharacters.remove(indexToRemove);
		}
	}


    /**
     * @return Returns the availableCharacters.
     */
    public ArrayList getAvailableCharacters() {
        return availableCharacters;
    }


    /**
     * @param availableCharacters The availableCharacters to set.
     */
    public void setAvailableCharacters(ArrayList availableCharacters) {
        this.availableCharacters = availableCharacters;
    }


    /**
     * @return Returns the joinedPlayers.
     */
    public ArrayList getJoinedPlayers() {
        return joinedPlayers;
    }
 

    /**
     * @param joinedPlayers The joinedPlayers to set.
     */
    public void setJoinedPlayers(ArrayList joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
    }

    
   
    private ArrayList createNotPlayingCharacters(){
        
        // crea tutti i characters esistenti
        ArrayList allCharacters = new ArrayList();
        allCharacters.add(new Player(Configuration.PLAYER_1));
        allCharacters.add(new Player(Configuration.PLAYER_2));
        allCharacters.add(new Player(Configuration.PLAYER_3));
        allCharacters.add(new Player(Configuration.PLAYER_4));
        allCharacters.add(new Player(Configuration.PLAYER_5));
        allCharacters.add(new Player(Configuration.PLAYER_6));
        
        Iterator jp = joinedPlayers.iterator();
        
        // elimina i characters scelti dai giocatori per trovare quelli non scelti 
        
        while(jp.hasNext()){
            Player joinedPlayer = (Player)jp.next();
            Iterator ac = allCharacters.iterator();    
            boolean found = false;
            int index = -1;
            while(ac.hasNext() && !found){
                Player aCharacter = (Player)ac.next();
                if(aCharacter.getPlayerID() == joinedPlayer.getPlayerID()){
                    index = allCharacters.indexOf(aCharacter);
                    found = true;
                }
            }
            if(found){
                allCharacters.remove(index);
            }
            
        }
        
        // ritorna la lista dei soli characters non giocanti
        return allCharacters;
        
        
    }
	
	
    private ArrayList createRooms(){
       
        ArrayList rooms = new ArrayList();
    
        Room cucina,salaBallo,studio,salotto,centro,veranda,
             anticamera,salaBiliardo,biblioteca,salaPranzo;
   
        //crea cucina
        cucina = new Room(Configuration.KITCHEN,Color.ORANGE);
        cucina.insertPart(new RectArea(0,1,5,1));
        cucina.insertPart(new RectArea(1,1,6,5));
        cucina.setPassage(new SecretPassage(5,1,null));
        cucina.insertDoor(new Door(4,6,Configuration.DOOR_SIDE_SOUTH));
                
        //crea sala da ballo
        salaBallo = new Room(Configuration.BALL,Color.CYAN);
        salaBallo.insertPart(new RectArea(8,2,6,2));
        salaBallo.insertPart(new RectArea(10,0,8,4));
        salaBallo.insertPart(new RectArea(14,2,6,2));
        salaBallo.insertDoor(new Door(8,5,Configuration.DOOR_SIDE_WEST));
        salaBallo.insertDoor(new Door(9,7,Configuration.DOOR_SIDE_SOUTH));
        salaBallo.insertDoor(new Door(14,7,Configuration.DOOR_SIDE_SOUTH));
        salaBallo.insertDoor(new Door(15,5,Configuration.DOOR_SIDE_EAST));
        
        //crea salotto
        salotto = new Room(Configuration.LOUNGE,Color.YELLOW);
        salotto.insertPart(new RectArea(18,1,4,1));
        salotto.insertPart(new RectArea(19,1,5,4));
        salotto.insertPart(new RectArea(23,1,4,1));
        salotto.setPassage(new SecretPassage(22,5,null));
        salotto.insertDoor(new Door(18,4,Configuration.DOOR_SIDE_SOUTH));
        
        
        //crea centro
        centro = new Room(Configuration.CENTER,Color.BLACK);
        centro.insertPart(new RectArea(10,10,7,5));
        
        
        //crea sala da pranzo
        salaPranzo = new Room(Configuration.DINING,Color.PINK);
        salaPranzo.insertPart(new RectArea(0,9,7,5));
        salaPranzo.insertPart(new RectArea(5,10,6,3));
        salaPranzo.insertDoor(new Door(7,12,Configuration.DOOR_SIDE_EAST));
        salaPranzo.insertDoor(new Door(6,15,Configuration.DOOR_SIDE_SOUTH));
        
        //crea sala biliardo
        salaBiliardo = new Room(Configuration.BILLIARD,Color.GREEN);
        salaBiliardo.insertPart(new RectArea(18,8,5,6));
        salaBiliardo.insertDoor(new Door(18,9,Configuration.DOOR_SIDE_WEST));
        salaBiliardo.insertDoor(new Door(22,12,Configuration.DOOR_SIDE_SOUTH));
        
        // crea biblioteca
        biblioteca = new Room(Configuration.LIBRARY,Color.MAGENTA);
        biblioteca.insertPart(new RectArea(17,15,3,1));
        biblioteca.insertPart(new RectArea(18,14,5,5));
        biblioteca.insertPart(new RectArea(23,15,3,1));
        biblioteca.insertDoor(new Door(17,16,Configuration.DOOR_SIDE_WEST));
        biblioteca.insertDoor(new Door(20,14,Configuration.DOOR_SIDE_NORTH));
        
        
        // crea veranda
        veranda = new Room(Configuration.CONSERVATORY,Color.lightGray);
        veranda.insertPart(new RectArea(0,19,6,6));
        veranda.insertPart(new RectArea(6,19,5,1));
        veranda.insertDoor(new Door(6,19,Configuration.DOOR_SIDE_NORTH));
        veranda.setPassage(new SecretPassage(0,19,null));
        
        
        
        // crea anticamera
        anticamera = new Room(Configuration.HALL,Color.WHITE);
        anticamera.insertPart(new RectArea(9,18,7,6));
        anticamera.insertDoor(new Door(11,18,Configuration.DOOR_SIDE_NORTH));
        anticamera.insertDoor(new Door(12,18,Configuration.DOOR_SIDE_NORTH));
        anticamera.insertDoor(new Door(14,20,Configuration.DOOR_SIDE_EAST));
        
        // crea studio
        studio =  new Room(Configuration.STUDY,Color.GRAY);
        studio.insertPart(new RectArea(17,21,3,1));
        studio.insertPart(new RectArea(18,21,4,6));
        studio.insertDoor(new Door(17,21,Configuration.DOOR_SIDE_NORTH));
        studio.setPassage(new SecretPassage(23,21,null));
        
        
        //mapping dei passaggi segreti
        cucina.getPassage().setLink(studio);
        studio.getPassage().setLink(cucina);
        veranda.getPassage().setLink(salotto);
        salotto.getPassage().setLink(veranda);
        
        
        //inserimento stanze
        rooms.add(cucina);
        rooms.add(salaBallo);
        rooms.add(salotto);
        rooms.add(centro);
        rooms.add(salaPranzo);
        rooms.add(salaBiliardo);
        rooms.add(biblioteca);
        rooms.add(veranda);
        rooms.add(anticamera);
        rooms.add(studio);
                
        // ritorna la lista delle rooms
        return rooms;
    }


    private ArrayList createWeapons(){
        ArrayList weapons = new ArrayList();
        
        weapons.add(new Weapon(Configuration.CANDLESTICK));
        weapons.add(new Weapon(Configuration.GUN));
        weapons.add(new Weapon(Configuration.SPANNER));
        weapons.add(new Weapon(Configuration.LEADPIPE));
        weapons.add(new Weapon(Configuration.ROPE));
        weapons.add(new Weapon(Configuration.DAGGER));
        
        return weapons;
    }
    

    private void assignWeapons(){
        
        Iterator w = gameConfig.getWeapons().iterator();
        
        while(w.hasNext()){
            Weapon aWeapon =(Weapon)w.next();
            Iterator r = gameConfig.getRooms().iterator();
            boolean found = false;
            
            while(r.hasNext() && !found){
                Room aRoom = (Room)r.next();
                
                if(aWeapon.getName().equals(Configuration.CANDLESTICK) &&
                    aRoom.getName().equals(Configuration.DINING)){
                    aRoom.addWeapon(aWeapon);
                }
                else if(aWeapon.getName().equals(Configuration.DAGGER) &&
                        aRoom.getName().equals(Configuration.CONSERVATORY)){
                        aRoom.addWeapon(aWeapon);
                }
                else if(aWeapon.getName().equals(Configuration.GUN) &&
                        aRoom.getName().equals(Configuration.STUDY)){
                        aRoom.addWeapon(aWeapon);
                }
                else if(aWeapon.getName().equals(Configuration.ROPE) &&
                        aRoom.getName().equals(Configuration.BALL)){
                        aRoom.addWeapon(aWeapon);
                }
                else if(aWeapon.getName().equals(Configuration.LEADPIPE) &&
                        aRoom.getName().equals(Configuration.LOUNGE)){
                        aRoom.addWeapon(aWeapon);
                }
                else if(aWeapon.getName().equals(Configuration.SPANNER) &&
                        aRoom.getName().equals(Configuration.KITCHEN)){
                        aRoom.addWeapon(aWeapon);
                }
            }
        }
        
    }
	
}
