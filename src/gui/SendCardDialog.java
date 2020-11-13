/*
 * SendCardDialog.java
 * Created on 30-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import gamedata.Card;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import rmi.GameController;
import sun.awt.WindowClosingListener;

/**
 * @author Jacopo
 * 
 * 
 */
public class SendCardDialog extends JDialog implements ActionListener {//, WindowListener {

    private ArrayList requestedCards;
    private ArrayList availableCards;
    
    private JComboBox cmbSelection;
    private JButton btnSend;
    private JButton btnClose;
    private JLabel lblCard;
    private JLabel lblCharacter;
    private JLabel lblWeapon;
    private JLabel lblRoom;
    private JLabel lblNoCards;
    
    private Card returnValue;
    
    
    public SendCardDialog(ArrayList requestedCards, ArrayList availableCards, boolean modal){
        
        setModal(modal);
         
        setTitle("Show a card...");
        
        this.requestedCards = requestedCards;
        this.availableCards = availableCards;
        initDialog();
    }
    
    private void initDialog(){
        
        lblCharacter = new JLabel("",null,JLabel.CENTER);
        lblCharacter.setIcon(((Card)requestedCards.get(0)).getImage());
        
        lblWeapon = new JLabel("",null,JLabel.CENTER);
        lblWeapon.setIcon(((Card)requestedCards.get(1)).getImage());
        
        lblRoom = new JLabel("",null,JLabel.CENTER);
        lblRoom.setIcon(((Card)requestedCards.get(2)).getImage());
        
        Box main = Box.createVerticalBox();
        
        Box hbox1 = Box.createHorizontalBox();
        hbox1.setBorder(new TitledBorder(new EtchedBorder(),"Requested cards"));
        hbox1.add(lblCharacter);
        hbox1.add(Box.createHorizontalStrut(10));
        hbox1.add(lblWeapon);
        hbox1.add(Box.createHorizontalStrut(10));
        hbox1.add(lblRoom);
        
        
        
        
        if(availableCards.size() == 0) {
        	lblNoCards = new JLabel("No cards to show");
        	btnClose = new JButton("Close");
        	btnClose.addActionListener(this);
        	
        	main.add(hbox1);
        	main.add(lblNoCards);
        	main.add(btnClose);
        }
        else {
        	
        	cmbSelection = new JComboBox(availableCards.toArray());
        	cmbSelection.addActionListener(this);
        	
	        Box hbox2 = Box.createVerticalBox();
	        hbox2.setBorder(new TitledBorder(new EtchedBorder(),"Select the card you want to show"));
	        hbox2.add(cmbSelection);
	        hbox2.add(Box.createVerticalStrut(10));
	        
	        lblCard = new JLabel("",null,JLabel.CENTER);
	        lblCard.setIcon(((Card)cmbSelection.getSelectedItem()).getImage());
	       
	        hbox2.add(lblCard);
	        hbox2.add(Box.createVerticalStrut(10));
	        
	        btnSend = new JButton("Show");
	        btnSend.addActionListener(this);
	        
	        hbox2.add(btnSend);
	        
	        
	        
	        main.add(hbox1);
	        main.add(hbox2);
        	
        }
        
        
        this.getContentPane().add(main);
        
        
        this.pack();
        this.setVisible(true);
        
    }
    
    
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if(src.equals(cmbSelection)){
            Card c = (Card)cmbSelection.getSelectedItem();
            lblCard.setIcon(c.getImage());
            repaint();
        }
        
        else if(src.equals(btnSend)){
            returnValue = (Card)cmbSelection.getSelectedItem();
            setVisible(false);
        }
        
        else if(src.equals(btnClose)){
            this.dispose();
        }
    }



    public Card getReturnValue(){
        return returnValue;
    }
    
    

    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub
        
    }




    public void windowClosing(WindowEvent arg0) {

        while(true){
            setVisible(true);
        }
        
    }




    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub
        while(true){
            
        }
    }




    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub
        
    }




    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub
        
    }




    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub
        
    }




    public void windowDeactivated(WindowEvent e){
        while(true){
            // TODO Auto-generated method stub
        }
        
    }




    
    
    

}
