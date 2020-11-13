/*
 * ControlPanel.java
 * Created on 2-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import gamedata.Card;
import gamedata.ChatMessage;
import gamedata.GameBoard;
import gamedata.Player;
import gamedata.Room;
import gamedata.SecretPassage;
import gamedata.Weapon;
import interfaces.Configuration;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import rmi.GameConfiguration;
import rmi.GameController;
import rmi.IController;


/**
 * @author Jacopo
 * 
 *  
 */
public class ControlPanel extends JPanel implements ActionListener {

    private GameController controller;
    private PanelGameBoard panelGameBoard;
     
    private JButton btnDie;
    private JButton btnSecretPassage;
    private JButton btnSuggest;
    private JButton btnAccuse;
    private JButton btnCards;
    private JButton btnPassTurn;
    
    private JLabel lblMovesLeft;
    private JLabel lblDie;
    
    private JFrame parent;
    
      
    public ControlPanel(PanelGameBoard panelGameBoard, GameController controller,JFrame parent){
        // definisce l'aspetto del panel
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBackground((new Color(0,255,0)).darker());
        setBorder(new TitledBorder(new EtchedBorder(),"Controls"));
        
        // imposta il controller della partita
        this.controller = controller;
        
        // imposta il componente in cui si trova il parent
        this.parent = parent;
        
        
        this.panelGameBoard = panelGameBoard;
        
        // inizializza i componenti
        initPanel();
        
        
    }
    
    
    private void initPanel(){
        // inizializza i bottoni
        btnAccuse = new JButton("Accuse");
        btnSuggest = new JButton("Suggest");
        btnDie = new JButton();
        btnCards = new JButton("Show cards");
        btnSecretPassage = new JButton("Secret Passage");
        btnPassTurn = new JButton("Pass Turn");
        
        //imposta le icone dei bottoni
        btnDie.setText("<html><img src=\"file:"+Configuration.getImagesPath()+"dice.gif"+"\"></html>");
        //TODO: btnAccuse.setText();
        //TODO: btnSuggest.setText();
        //TODO: btnCards.setText();
       
        // imposta i listener per i bottoni
        btnAccuse.addActionListener(this);
        btnSuggest.addActionListener(this);
        btnDie.addActionListener(this);
        btnCards.addActionListener(this);
        btnSecretPassage.addActionListener(this);       
        btnPassTurn.addActionListener(this);
        // inizializza le label
        lblMovesLeft = new JLabel("No moves left");
        lblDie = new JLabel("no die thrown");
        
            
        
        // box principale verticale
        Box vbox = Box.createVerticalBox();
        
        
        Box hbox = Box.createHorizontalBox();
        hbox.add(Box.createHorizontalStrut(10));
        hbox.add(lblMovesLeft);
        
        
        
        // box relativa al dado
        Box hbox2 = Box.createHorizontalBox();
        hbox2.add(Box.createHorizontalStrut(10));
        hbox2.add(lblDie);
        
        
        // box relative ai button
        Box hbox1 = Box.createHorizontalBox();
        hbox1.add(Box.createHorizontalStrut(10));
        hbox1.add(btnSecretPassage);
                
        Box hbox3 = Box.createHorizontalBox();
        hbox3.add(Box.createHorizontalStrut(5));
        hbox3.add(btnDie);
        hbox3.add(Box.createHorizontalStrut(5));
        
        Box hbox4 = Box.createHorizontalBox();
        hbox4.add(Box.createHorizontalStrut(10));
        hbox4.add(btnCards);
        hbox4.add(Box.createHorizontalStrut(5));
        
        Box hbox5 = Box.createHorizontalBox();
        hbox5.add(Box.createHorizontalStrut(10));
        hbox5.add(btnSuggest);
        hbox5.add(Box.createHorizontalStrut(5));
        
        Box hbox6 = Box.createHorizontalBox();
        hbox6.add(Box.createHorizontalStrut(10));
        hbox6.add(btnAccuse);
        hbox6.add(Box.createHorizontalStrut(5));
        
        Box hbox7 = Box.createHorizontalBox();
        hbox7.add(Box.createHorizontalStrut(10));
        hbox7.add(btnPassTurn);
        hbox7.add(Box.createHorizontalStrut(5));
        
        // assembla l'interfaccia
       
        vbox.add(hbox3);
        vbox.add(Box.createVerticalStrut(10));
        vbox.add(hbox);
        vbox.add(Box.createVerticalStrut(5));
        vbox.add(hbox2);
        vbox.add(Box.createVerticalStrut(5));
        vbox.add(hbox1);
        vbox.add(Box.createVerticalStrut(5));
        vbox.add(hbox4);
        vbox.add(Box.createVerticalStrut(5));
        vbox.add(hbox5);
        vbox.add(Box.createVerticalStrut(5));
        vbox.add(hbox6);
        vbox.add(Box.createVerticalStrut(5));
        vbox.add(hbox7);
        this.add(vbox);
        
   }
    
    
    // TODO: gestire i vari controlli in base al turno
    public void actionPerformed(ActionEvent e) {
        
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        
        Object src = e.getSource();
        
        // lancia il dado ed effettua la mossa
        if(src.equals(btnDie)){
          
           //il bottone funziona solo se è il mio turno
           if(gc.getPlayingPlayer().getPlayerID()==
                   controller.getCurrentPlayer().getPlayerID()){

                controller.rollDie();
                panelGameBoard.repaint();
                lblDie.setText("");
                lblDie.setIcon(new ImageIcon(Configuration.getImagesPath()+"die"+controller.getMovesLeft()+".png"));
                lblMovesLeft.setText("Moves Left: "+controller.getMovesLeft());
                controller.movePlayer();
                
            }
           else { //mostra un messaggio di avvertimento
               JOptionPane.showMessageDialog(null,"It's not your turn.","Warning",JOptionPane.WARNING_MESSAGE);
           }
            
           
            
        }
        /* mostra la dialog x l'annuncio: usa tutte le carte
           e la stanza in cui si trova il player
        */
        else if(src.equals(btnSuggest)){
                        
            ArrayList characters = gc.getAllCharacters();
            ArrayList weapons = gc.getWeapons();
            
                        
            Room playerRoom = controller.getCurrentPlayer().getRoom();
            
            SuggestionDialog sd = new SuggestionDialog(playerRoom,characters,weapons,parent,controller,panelGameBoard);
            sd.setVisible(true);
        } 
        
        /* mostra la dialog x l'accusa: mostra al giocatore solo
           le carte che non ha ancora visto
         */
        else if(src.equals(btnAccuse)){
                      
            ArrayList characters = new ArrayList();
            ArrayList weapons = new ArrayList();
            ArrayList rooms = new ArrayList();
            
            Player p = controller.getCurrentPlayer();
            
            Iterator it = p.getNotSeenCards().iterator();
            
            
            // inizializza le carte non ancora viste
            Iterator iterPlayers = null;
            Iterator iterWeapons = null;
            Iterator iterRooms = null;
            
            boolean found = false;
          
            while(it.hasNext()){
            	Card c = (Card)it.next();
                
                switch(c.getType()){
                    case Configuration.CARD_TYPE_CHARACTER : {
                       
                    	iterPlayers = gc.getAllCharacters().iterator();
                    	
                        while(iterPlayers.hasNext() && !found){
                            Player player = (Player)iterPlayers.next();
                            
                            if(player.getCharacterCard().getName().equals(c.getName())){
                                characters.add(player);
                                found = true;
                            }
                        }
                        break;
                    }
                    case Configuration.CARD_TYPE_WEAPON : {
                    	
                    	iterWeapons = gc.getWeapons().iterator();
                    	
                        while(iterWeapons.hasNext() && !found){
                            Weapon weap = (Weapon)iterWeapons.next();
                                                       
                            if(weap.getWeaponCard().getName().equals(c.getName())){
                                weapons.add(weap);
                                found = true;
                            }
                        }
                        break;
                    }
                    case Configuration.CARD_TYPE_ROOM : {
                                        
                    	iterRooms = gc.getRooms().iterator();
                    	
                    	while(iterRooms.hasNext() && !found){
                            Room r = (Room)iterRooms.next();
                            
                            if(!r.getName().equals(Configuration.CENTER)){
                            	if(r.getRoomCard().getName().equals(c.getName())){
                                    rooms.add(r);
                                    found = true;
                            	}
                            }
                        }
                        break;
                    }
                }
                found = false;
            }
            
            AccuseDialog ad = new AccuseDialog(rooms,characters,weapons,parent);
            ad.setVisible(true);
        }
        
                
        /* mostra la dialog delle carte viste/possedute
           dal giocatore
        */
        else if(src.equals(btnCards)){
            CardsDialog cd = new CardsDialog(controller.getCurrentPlayer(),parent);
            cd.setVisible(true);
        }
        
        /*
         * utilizza il passaggio segreto se possibile
         */
        else if(src.equals(btnSecretPassage)){
            Player p = controller.getCurrentPlayer();
            Room r = p.getRoom();
            if(r != null){
                SecretPassage passage = r.getPassage();
                if(passage != null){
                    r.getPlayers().remove(p);
                    p.setRoom(passage.getLink());
                    passage.getLink().addPlayer(p);
                    controller.setCurrentPlayer(p);
               
                    // aggiorno me stesso nella configurazione
                    Iterator i = gc.getPlayers().iterator();
                    int index = -1;
                    while(i.hasNext()){
                        Player myself = (Player)i.next();
                        if(myself.getPlayerID() == p.getPlayerID()){
                            index = gc.getPlayers().indexOf(myself);
                         
                        }
                    }

                    gc.getPlayers().set(index,p);
                    
                    // update della GameConfiguration
                    GameConfiguration.updateGameConfiguration(gc);
                    
                    // forward della GameConfiguration
                    //controller.forwardGameConfiguration(gc);
                    
                }
            }
        }
        
        /*
         * passa il turno di gioco al prossimo giocatore
         */
        else if(src.equals(btnPassTurn)){
            
            
        	//cerco il giocatore successivo
        	Iterator i = gc.getPlayers().iterator();
        	boolean found = false;
        	Player nextPlayer = null;
        	
        	while(i.hasNext() && !found) {
        		nextPlayer = (Player)i.next();
        		if(nextPlayer.getPlayerID() == controller.getCurrentPlayer().getPlayerID()) {
        			nextPlayer = (Player)i.next();
        			found = true;
        		}
        	}
        	if(found) {
        		System.setSecurityManager(new RMISecurityManager());
        		
        		try {
					IController remoteController = (IController) Naming.lookup("rmi://"+nextPlayer.getIP()+"/"+Configuration.CLIENT_SERVICE_NAME);
					
					// passa il turno al successore
					synchronized (gc) {
						remoteController.beginTurn(gc.getState());
					}
										
					
					// invia SYSTEM chat message per informare del passaggio del turno
                    String msg = "Player "+nextPlayer+" begins his turn.";
                    ChatMessage chatMsg = new ChatMessage(null,msg);
                    remoteController.sendChatMessage(chatMsg,ChatMessage.SYSTEM_MESSAGE_TYPE,gc);
				
        		} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					
					
					controller.alarmDeadPlayer(nextPlayer.getIP());
					
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					
					controller.alarmDeadPlayer(nextPlayer.getIP());
					
					e1.printStackTrace();
				}
        		
        	}
        	 
            
            // disabilita i pulsanti di gioco
            controller.enableGUIButtons(false);
            
        }
        
    }

    
    
    public void setMovesLeft(String s){
        lblMovesLeft.setText(s);
    }
    
    public void setDie(String s){
        lblDie.setText(s);
        lblDie.setIcon(null);
    }


    public void enableDieButton(boolean b) {
        btnDie.setEnabled(b);
    }


    public void enableSuggestButton(boolean b) {
        btnSuggest.setEnabled(b);
    }


    public void enableAccuseButton(boolean b) {
        btnAccuse.setEnabled(b);
    }


    public void enableSecretPassageButton(boolean b) {
        btnSecretPassage.setEnabled(b);
    }


    public void enablePassTurnButton(boolean b) {
        btnPassTurn.setEnabled(b);
    }


    
    
    
}
