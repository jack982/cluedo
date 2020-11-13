/*
 * StartWizardNew.java
 * Created on 4-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import gamedata.Card;
import gamedata.Player;
import interfaces.Configuration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rmi.GameCreator;

/**
 * @author Jacopo
 * 
 * 
 */
public class StartWizardNew extends JDialog implements ActionListener, CaretListener {

    private int joinedPlayers;
    
    private JButton btnCreate;
    private JButton btnCancel;
    private JButton btnStart;
    
    private JComboBox cmbCharacters;
    private JLabel lblCharacter;
    
    private JTextField txtGameName;
    private String serviceName;
    
    private JLabel lblJoinedPlayer;
    
    private JPanel main;
    
    private GameCreator gameCreator;
    private Player myself;
    
    public StartWizardNew(){
        setTitle("Cluedo - New Game");
       
        main = new JPanel();
        //main.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        btnCreate = new JButton("Create Game");
     //   btnCreate.setEnabled(false);
        btnCreate.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
                
        btnStart = new JButton("Start");
        btnStart.addActionListener(this);
        btnStart.setEnabled(false);
                
        txtGameName = new JTextField("New_game");
        txtGameName.addCaretListener(this);
        
        cmbCharacters = initCombo();
        cmbCharacters.addActionListener(this);
        initCombo();
        
        lblCharacter = new JLabel();
        lblCharacter.setIcon(((Card)cmbCharacters.getSelectedItem()).getImage());
        
        joinedPlayers = 0;
        
        lblJoinedPlayer = new JLabel(joinedPlayers+"/"+Configuration.MAX_PLAYERS);
        lblJoinedPlayer.setToolTipText("Joined Players");
        
        Box hbox1 = Box.createHorizontalBox();
        hbox1.setBorder(new TitledBorder(new EtchedBorder(),"Game name"));
        hbox1.add(txtGameName);
        
        Box hbox2 = Box.createHorizontalBox();
     //   hbox2.setBorder(new TitledBorder(new EtchedBorder(),"Character selection"));
        hbox2.add(cmbCharacters);
        
        Box hbox3 = Box.createHorizontalBox();
        hbox3.add(lblCharacter);
        
        Box hbox4 = Box.createHorizontalBox();
        hbox4.add(btnCancel);
        hbox4.add(btnCreate);
        hbox4.add(btnStart);
        hbox4.add(lblJoinedPlayer);
        
        
        Box vbox = Box.createVerticalBox();
        vbox.setBorder(new TitledBorder(new EtchedBorder(),"Character selection"));
        vbox.add(hbox2);
        vbox.add(hbox3);
        
        /*main.add(hbox1);
        main.add(vbox);
        main.add(hbox4);
        */
        
        
        this.getContentPane().add(hbox1,BorderLayout.NORTH);
        this.getContentPane().add(vbox,BorderLayout.CENTER);
        this.getContentPane().add(hbox4,BorderLayout.SOUTH);
        
        centerInScreen();
        setResizable(false);
        pack();
       
        //setVisible(true);
        
        
    }

    public synchronized void addLabelJoinedPlayers(){
        joinedPlayers++;
        lblJoinedPlayer.setText(joinedPlayers+"/"+Configuration.MAX_PLAYERS);
    }

    public synchronized void decLabelJoinedPlayers(){
        joinedPlayers--;
        lblJoinedPlayer.setText(joinedPlayers+"/"+Configuration.MAX_PLAYERS);
    }
    
    
    private JComboBox initCombo(){
       ArrayList list = new ArrayList();
       list.add(new Card(Configuration.CHARACTER_NAME_1));
       list.add(new Card(Configuration.CHARACTER_NAME_2));
       list.add(new Card(Configuration.CHARACTER_NAME_3));
       list.add(new Card(Configuration.CHARACTER_NAME_4));
       list.add(new Card(Configuration.CHARACTER_NAME_5));
       list.add(new Card(Configuration.CHARACTER_NAME_6));
       JComboBox combo = new JComboBox(list.toArray());
       return combo;
    }
    
    
    private void centerInScreen(){
        Toolkit theKit=this.getToolkit();
        Dimension wndSize=theKit.getScreenSize();
        this.setBounds(wndSize.width/4,wndSize.height/4,wndSize.width/2,wndSize.height/2);
    }
    
        
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if(src.equals(cmbCharacters)){
            lblCharacter.setIcon(((Card)cmbCharacters.getSelectedItem()).getImage());    
        }
        else if(src.equals(btnCancel)){
            dispose();
        }
        else if(src.equals(btnCreate)){
            btnCreate.setEnabled(false);
            serviceName = txtGameName.getText();
            
            myself = new Player(cmbCharacters.getSelectedIndex()+1);
            
            try {
                gameCreator = new GameCreator(serviceName,this);
                gameCreator.setCurrentPlayer(myself);
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
            
            // mi aggiungo alla lista dei giocatori della partita
            gameCreator.getJoinedPlayers().add(myself);
            // elimino la carta del mio personaggio tra quelle disponibili
            gameCreator.removeCard(myself.getCharacterCard());
            
            // aggiorna la label dei giocatori ready
            addLabelJoinedPlayers();
            
            
            // registro e avvio il servizio
            System.setSecurityManager(new RMISecurityManager());
            try {
                Naming.rebind(serviceName, gameCreator);
              
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            System.out.println("servizio di creazione partita registrato correttamente");
            btnStart.setEnabled(true);
        }
        
        
        // bottone START
        else if(src.equals(btnStart)){

        	// elimino il servizio dal registry
            try {
                Naming.unbind(serviceName);
            } catch (RemoteException re) {
                re.printStackTrace();
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (NotBoundException nbe) {
                nbe.printStackTrace();
            }
        	
        	
            //crea la configurazione della partita
            gameCreator.createGameConfig();
            
            // notifica la configurazione della partita a tutti i client
            gameCreator.sendNotifies();
        }
        
       
        
    }


    public void caretUpdate(CaretEvent e) {
        Object src = e.getSource();
        
        if(src.equals(txtGameName)){
            if(txtGameName.getText().length() > 0) {
                btnCreate.setEnabled(true);
            }else btnCreate.setEnabled(false);
        }
        
    }


   
    
        
}
