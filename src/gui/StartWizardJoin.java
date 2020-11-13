/*
 * StartWizardJoin.java
 * Created on 14-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import gamedata.Card;
import gamedata.Player;
import interfaces.Configuration;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rmi.GameController;
import rmi.GameCreator;
import rmi.IController;
import rmi.IGameCreator;
import rmi.MaxNumberOfPlayerJoinedException;

/**
 * @author Jacopo
 * 
 * 
 */
public class StartWizardJoin extends JDialog implements ActionListener {
    
    private JButton btnJoin;
    private JButton btnCancel;
    private JButton btnReady;
        
    private JComboBox cmbCharacters;
    private JLabel lblCharacter;
    
    private JTextField txtServerIP;
    private JTextField txtPlayerName;
    
    private JPanel main;
    
    private IGameCreator gameCreator;
    private Player myself;
    
    private ArrayList characters;
    
    private GameController controller;
       
    
    public StartWizardJoin(GameController controller){
        setTitle("Cluedo - Join Game");
       
        this.controller = controller;
        
        main = new JPanel();
        //main.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        
        btnJoin = new JButton("Join Game");
     //   btnJoin.setEnabled(false);
        btnJoin.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
                
        btnReady = new JButton("Ready");
        btnReady.addActionListener(this);
        btnReady.setEnabled(false);
        
    
        txtServerIP = new JTextField(30);
        
        txtPlayerName = new JTextField(30);
        txtPlayerName.setEnabled(false);
        
      
        cmbCharacters = new JComboBox();
        cmbCharacters.setEnabled(false);
        cmbCharacters.addActionListener(this);
        
        
        lblCharacter = new JLabel();
              
        
        
        Box hbox1 = Box.createHorizontalBox();
        hbox1.setBorder(new TitledBorder(new EtchedBorder(),"Server IP"));
        hbox1.add(txtServerIP);
        
        Box hbox2 = Box.createHorizontalBox();
     //   hbox2.setBorder(new TitledBorder(new EtchedBorder(),"Character selection"));
        hbox2.add(cmbCharacters);
        
        Box hbox3 = Box.createHorizontalBox();
        hbox3.add(lblCharacter);
        
        
        Box hbox4 = Box.createHorizontalBox();
        hbox4.setBorder(new TitledBorder(new EtchedBorder(),"Player name"));
        hbox4.add(txtPlayerName);
        
        Box hbox5 = Box.createHorizontalBox();
        hbox5.add(btnCancel);
        hbox5.add(btnJoin);
        hbox5.add(btnReady);
        
        
        Box vbox = Box.createVerticalBox();
        vbox.setBorder(new TitledBorder(new EtchedBorder(),"Character selection"));
        vbox.add(hbox2);
        vbox.add(hbox3);
        vbox.add(hbox4);
      //  vbox.add(hbox5);
        
        /*main.add(hbox1);
        main.add(vbox);
        main.add(hbox4);
        */
        
        
        this.getContentPane().add(hbox1,BorderLayout.NORTH);
        this.getContentPane().add(vbox,BorderLayout.CENTER);
        this.getContentPane().add(hbox5,BorderLayout.SOUTH);
        
        centerInScreen();
        setResizable(false);
        pack();
       
        //setVisible(true);
        
        
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
            txtPlayerName.setText(cmbCharacters.getSelectedItem().toString());
        }
        else if(src.equals(btnCancel)){
            dispose();
        }
        else if(src.equals(btnJoin)){
            System.setSecurityManager(new RMISecurityManager());
         
            try {
                gameCreator = (IGameCreator)Naming.lookup("rmi://"+txtServerIP.getText()+"/New_game");
           
                // JOIN
                characters = gameCreator.join();
                        
            } catch (MalformedURLException e1) {
                JOptionPane.showMessageDialog(this,"Malformed URL","Error",JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
                dispose();
            
            } catch (NotBoundException e1) {
                JOptionPane.showMessageDialog(this,"Service not bound","Error",JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
                dispose();
            } catch(RemoteException e1) {
                JOptionPane.showMessageDialog(this,"Unreachable host","Error",JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
                dispose();
            } catch (MaxNumberOfPlayerJoinedException e1) {
                JOptionPane.showMessageDialog(this,"Max number of player already reached","Error",JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
                dispose();
            }
         
            Iterator i = characters.iterator();
            // aggiorna la combo con i characters disponibili
            while(i.hasNext()){
                cmbCharacters.addItem(i.next());
            }
            
            cmbCharacters.setEnabled(true);
            txtPlayerName.setEnabled(true);
            btnReady.setEnabled(true);
            txtServerIP.setEnabled(false);
            btnJoin.setEnabled(false);
            
            pack();        
            
        }
        // bottone START
        else if(src.equals(btnReady)){

            //crea il giocatore ...
            Card card = (Card)cmbCharacters.getSelectedItem();
            String cardname = card.getName();
            
            if(cardname.equals(Configuration.CHARACTER_NAME_1)){
                myself = new Player(Configuration.PLAYER_1);
            }
            else if(cardname.equals(Configuration.CHARACTER_NAME_2)){
                myself = new Player(Configuration.PLAYER_2);
            }
            else if(cardname.equals(Configuration.CHARACTER_NAME_3)){
                myself = new Player(Configuration.PLAYER_3);
            }
            else if(cardname.equals(Configuration.CHARACTER_NAME_4)){
                myself = new Player(Configuration.PLAYER_4);
            }
            else if(cardname.equals(Configuration.CHARACTER_NAME_5)){
                myself = new Player(Configuration.PLAYER_5);
            }
            else if(cardname.equals(Configuration.CHARACTER_NAME_6)){
                myself = new Player(Configuration.PLAYER_6);
            }
            
            // ... e lo notifica al server
            try {
                characters = gameCreator.ready(myself);
            } catch (RemoteException e1) {
                JOptionPane.showMessageDialog(this,"Server not ready. Try Again","Error",JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (MaxNumberOfPlayerJoinedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
            if(characters == null){ // il server ci ha accettato
                WaitingForStartDialog wsd = new WaitingForStartDialog();
                controller.setWaitDialog(wsd);
                wsd.setVisible(true);
                
            // il server non ha accettato il character scelto
            }else{
                JOptionPane.showMessageDialog(this,"Character already choosen by another player","Warning",JOptionPane.WARNING_MESSAGE);
                Iterator iter = characters.iterator();
                cmbCharacters.removeAllItems();
                while(iter.hasNext()){
                    cmbCharacters.addItem(iter.next());
                }
            }
            
        }
    }
}
