/*
 * SuggestionDialog.java
 * Created on 28-feb-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import gamedata.Card;
import gamedata.GameBoard;
import gamedata.Player;
import gamedata.Room;
import gamedata.SuggestionMessage;
import gamedata.Weapon;

import interfaces.Configuration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ToolTipUI;

import com.sun.media.sound.AlawCodec;

import rmi.GameConfiguration;
import rmi.GameController;
import rmi.IController;

/**
 * @author Jacopo
 * 
 * 
 */
public class SuggestionDialog extends JDialog implements ActionListener {

    private List characters;
    private List weapons;
    private Room room;
    
    private GameController controller;
    
    private PanelGameBoard panelGameBoard;
    
    // componenti di interfaccia
    private JPanel panel;
    private JPanel top;
    private JPanel center;
    private JPanel centerTop;
    private JPanel centerBottom;
    private JPanel bottom;
    
    private JLabel lblRoom;
    private JLabel lblCharacters;
    private JLabel lblWeapons;
    
    private JLabel imgRoom;
    private JLabel imgWeapon;
    private JLabel imgCharacter;
    
    private JComboBox cmbWeapons;
    private JComboBox cmbCharacters;
    
    private JButton btnCancel;
    private JButton btnSuggest;
    
    
        
    public SuggestionDialog(Room room,
                            List characters,
                            List weapons,
                            JFrame parent,
                            GameController controller,
                            PanelGameBoard panelGameBoard){
        super(parent,true);
        this.room = room;
        this.characters = characters;
        this.weapons = weapons;
        this.controller = controller;
        this.panelGameBoard = panelGameBoard;
        
        initDialog();   
    }
    
    
    private void initDialog() {
   
        setTitle("Cluedo - Suggestion Dialog");
        
        lblRoom = new JLabel("Room:",JLabel.CENTER);
        lblCharacters = new JLabel("Character:",JLabel.CENTER);
        lblWeapons = new JLabel("Weapon:",JLabel.CENTER);
        
        cmbWeapons = new JComboBox(weapons.toArray());
        cmbWeapons.addActionListener(this);
        cmbCharacters = new JComboBox(characters.toArray());
        cmbCharacters.addActionListener(this);
        
        if(room != null){
            imgRoom = new JLabel("",room.getRoomCard().getImage(),JLabel.CENTER);
        }
        imgCharacter = new JLabel("",(((Player)cmbCharacters.getSelectedItem()).getCharacterCard().getImage()),JLabel.CENTER);
        imgWeapon = new JLabel("",(((Weapon)cmbWeapons.getSelectedItem()).getWeaponCard().getImage()),JLabel.CENTER);
        
        btnSuggest = new JButton("Suggest");
        btnSuggest.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(),"Make a Suggestion"));
        panel.setLayout(new BorderLayout());
        
        top = new JPanel();
        top.setLayout(new FlowLayout());
        
        center = new JPanel();
        center.setLayout(new BorderLayout());
        
        centerTop = new JPanel();
        centerTop.setLayout(new FlowLayout());
        
        centerBottom = new JPanel();
        centerBottom.setLayout(new FlowLayout());
        
        bottom = new JPanel();
        bottom.setLayout(new FlowLayout());
        
        // inizializza il pannello superiore
        top.add(lblCharacters);
        top.add(lblWeapons);
        top.add(lblRoom);
        
        // inizializza il pannello centrale
        centerTop.add(cmbCharacters);
        centerTop.add(cmbWeapons);
        if(room!=null){
            centerTop.add(new JLabel(room.getName()));
        }
        centerBottom.add(imgCharacter);
        centerBottom.add(imgWeapon);
        if(room!=null){
            centerBottom.add(imgRoom);
        }
        center.add(centerTop,BorderLayout.NORTH);
        center.add(centerBottom,BorderLayout.CENTER);
        
        // inizializza il pannello inferiore
        bottom.add(btnCancel);
        bottom.add(btnSuggest);
        
        // assemblo il panello principale
        panel.add(top,BorderLayout.NORTH);
        panel.add(center,BorderLayout.CENTER);
        panel.add(bottom,BorderLayout.SOUTH);
                
        getContentPane().add(panel);
        
        this.setResizable(false);
        this.pack();
        
    }


    public void actionPerformed(ActionEvent e) {
        
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        
        Object src = e.getSource();
        
        if(src.equals(btnSuggest)){
            System.out.println("SUGGESTION: "+cmbCharacters.getSelectedItem()+" "+cmbWeapons.getSelectedItem()+" "+room.getName());

            // prepara la lista delle carte richieste
            ArrayList requestedCards = new ArrayList();
            requestedCards.add(cmbCharacters.getSelectedItem());
            requestedCards.add(cmbWeapons.getSelectedItem());
            requestedCards.add(room.getRoomCard());
            
            
            //rimuove il sospettato dalla stanza in cui si trovava
            Iterator it = gc.getRooms().iterator();
            boolean found = false;
            boolean contained = false;
            int index = -1;
            
            while(it.hasNext() && !found){
                Room room = (Room)it.next();
                contained = false;
                index = -1;
                Iterator iter = room.getPlayers().iterator();
                while(iter.hasNext() && !contained){
                    Player p = (Player)iter.next();
                    if(p.getPlayerID() == ((Player)cmbCharacters.getSelectedItem()).getPlayerID()){
                        contained = true;
                        found = true;
                        index = room.getPlayers().indexOf(p);
                    }
                }
                if(found){
                    room.getPlayers().remove(index);
                }
            }
            
            
            // aggiunge il sospettato alla stanza in cui mi trovo e lo sposta
            controller.getCurrentPlayer().getRoom().addPlayer((Player)cmbCharacters.getSelectedItem());
            
            Iterator itera = gc.getPlayers().iterator();
            
            while(itera.hasNext()){
                Player player = (Player)itera.next();
                if(player.getPlayerID() == ((Player)cmbCharacters.getSelectedItem()).getPlayerID()){
                    player.setRoom(controller.getCurrentPlayer().getRoom());
                }
            }
            
            // rimuove l'arma dalla stanza in cui si trovava
            it = gc.getRooms().iterator();
            found = false;
            contained = false;
            index = -1;
            
            while(it.hasNext() && !found){
                Room room = (Room)it.next();
                contained = false;
                index = -1;
                Iterator iter = room.getWeapons().iterator();
                while(iter.hasNext() && !contained){
                    Weapon w = (Weapon)iter.next();
                    if(w.getName().equals(((Weapon)cmbWeapons.getSelectedItem()).getName())){
                        contained = true;
                        found = true;
                        index = room.getWeapons().indexOf(w);
                    }
                }
                if(found){
                    room.getWeapons().remove(index);
                }
            }
            
            
            // aggiunge l'arma richiesta alla stanza in cui mi trovo
            controller.getCurrentPlayer().getRoom().addWeapon((Weapon)cmbWeapons.getSelectedItem());
            
            Iterator itero = gc.getRooms().iterator();
            
           // controller.updatePanelBoard();
            panelGameBoard.repaint();
        
            
            // invia la suggestion al client successivo
            System.setSecurityManager(new RMISecurityManager());
            
            Player nextPlayer = controller.findNextPlayer(gc);
            IController remoteController = null;
            
            boolean exception = false;
            SuggestionMessage suggestion = null;
            
            if(nextPlayer != null){
                try {
                    // tenta di connettersi al client successivo
                    remoteController = (IController) Naming.lookup("rmi://"+nextPlayer.getIP()+"/"+Configuration.CLIENT_SERVICE_NAME);
                    //  crea ed invia la suggestion
                    
                    suggestion = new SuggestionMessage(controller.getCurrentPlayer().getIP(),requestedCards);
                    suggestion = remoteController.sendSuggestion(suggestion,gc);
              
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (RemoteException e1) {
                    // il client è morto
                    exception = true;
                    e1.printStackTrace();
                } catch (NotBoundException e1) {
                    // il client è morto
                    exception = true;
                    e1.printStackTrace();
                }
                
                if(exception){
                    // riconfiguro e mostro dialog all'utente x informarlo di ripetere la suggestion
                    JOptionPane.showMessageDialog(this,"Warning","One or more players died during suggestion. Please repeat it.",JOptionPane.WARNING_MESSAGE);
                    controller.alarmDeadPlayer(nextPlayer.getIP());
                }
                else{
                    // analizzo la suggestion che mi hanno ritornato gli altri client
                    if(suggestion.isException()){
                        // qualcuno è morto durante il forwarding della suggestion: devo ripeterla
                        // mostro solo la dialog
                        JOptionPane.showMessageDialog(this,"Warning","One or more players died during suggestion. Please repeat it.",JOptionPane.WARNING_MESSAGE);
                    }
                    else{
                        // mi è stata mostrata una carta di quelle richieste
                        if(suggestion.getReturnCard() != null){
                            Card card = suggestion.getReturnCard();
                            
                            synchronized (gc){
                                synchronized (controller.getCurrentPlayer()){
                                    
                                    Iterator i = gc.getPlayers().iterator();
                                    boolean foundMe = false;
                                    int myIndex = -1; 
                                    
                                    // mi cerco all'interno della configurazione
                                    while(i.hasNext() && !foundMe){
                                        Player pl = (Player)i.next();
                                        if(pl.getPlayerID() == controller.getCurrentPlayer().getPlayerID()){
                                            foundMe = true;
                                            myIndex = gc.getPlayers().indexOf(pl);
                                        }
                                    }
                                    
                                    if(foundMe){
                                        // aggiungo la carta vista alle mie aggiornando la configurazione e il currentPlayer
                                        Player me = (Player)gc.getPlayers().get(myIndex);
                                        me.addSeenCard(card);
                                        controller.setCurrentPlayer((Player)gc.getPlayers().get(myIndex));
                                    }
                                    
                                    // update della GameConfiguration
                                    GameConfiguration.updateGameConfiguration(gc);
                                 
                                    // FIXME: controller.forwardGameConfiguration(gc);
                                    
                                    //FIXME: passa il turno
                                
                                }
                            }
                            
                            // mostra la carta all'utente
                            JOptionPane.showMessageDialog(this,"Got card","You got this card <html><img src=\"file:"+card.getImagePath()+"\"></img></html>",JOptionPane.INFORMATION_MESSAGE);
                            
                        } else {
                            JOptionPane.showMessageDialog(this,"Information","No player has the requested cards",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"All players are offline. The game will be aborted","Game Aborted",JOptionPane.ERROR_MESSAGE);
                // TODO: disabilitare RMI Registry
                System.exit(0);    
            }
       
            dispose();
        }
        else if(src == btnCancel){
            dispose();
        }
        else if(src == cmbCharacters){
            Player p = (Player)cmbCharacters.getSelectedItem();
            imgCharacter.setIcon(p.getCharacterCard().getImage());
            this.repaint();
        }
        else if(src == cmbWeapons){
            Weapon w = (Weapon)cmbWeapons.getSelectedItem();
            imgWeapon.setIcon(w.getWeaponCard().getImage());
            this.repaint();
        }
    }
    
    
    
    
    
    
    
    
}
