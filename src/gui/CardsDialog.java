/*
 * CardsDialog.java
 * Created on 1-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import interfaces.Configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import gamedata.Card;
import gamedata.Player;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * @author Jacopo
 * 
 * 
 */
public class CardsDialog extends JDialog implements ActionListener {

    private Player player;
    
    private JPanel main;
    private JPanel cardPanel;
    private JPanel top;
    private JPanel middle;
    private JPanel bottom;
    private JPanel buttonPanel;
    
    private JButton btnClose;
    
    private ArrayList characters;
    private ArrayList weapons;
    private ArrayList rooms;
    
        
    
    public CardsDialog(Player player,JFrame parent){
        super(parent,true);
        this.player = player;
        initDialog();
    }
    
    private void initDialog(){
        setTitle("Cluedo - Player Cards");
        
        main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(new TitledBorder(new EtchedBorder(),"Your cards"));
        main.setBackground(new Color(0,255,0).darker());
       
        
        cardPanel = new JPanel();
        cardPanel.setBackground(new Color(0,255,0).darker());
        cardPanel.setLayout(new BorderLayout());
        
        top = new JPanel();
        top.setBorder(new TitledBorder(new EtchedBorder(),"Characters"));
        top.setBackground(new Color(0,255,0).darker());
        top.setLayout(new FlowLayout());
        
        middle = new JPanel();
        middle.setBorder(new TitledBorder(new EtchedBorder(),"Weapons"));
        middle.setBackground(new Color(0,255,0).darker());
        middle.setLayout(new FlowLayout());
                
        bottom = new JPanel();
        bottom.setBorder(new TitledBorder(new EtchedBorder(),"Rooms"));
        bottom.setBackground(new Color(0,255,0).darker());
        bottom.setLayout(new FlowLayout());
        
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0,255,0).darker());
        buttonPanel.setLayout(new FlowLayout());
        
        characters = new ArrayList();
        rooms = new ArrayList();
        weapons = new ArrayList();
        
        drawCards(player);
        
    }
    
    
    private void drawCards(Player p){
        ArrayList cards = p.getCardsSeen();
        
        Iterator it = cards.iterator();
        while(it.hasNext()){
            Card card = (Card)it.next();
            switch(card.getType()){
                case Configuration.CARD_TYPE_ROOM: {
                    rooms.add(card);
                    break;
                }
                case Configuration.CARD_TYPE_WEAPON: {
                    weapons.add(card);
                    break;
                }
                case Configuration.CARD_TYPE_CHARACTER: {
                    characters.add(card);
                    break;
                }
            }
        }
        
        // disegna le carte relative ai personaggi
        for(int i=0;i<characters.size();i++){
            JLabel lblChar = new JLabel();
            lblChar.setIcon(((Card)characters.get(i)).getImage());
            top.add(lblChar);
        }
        
        // disegna le carte relative alle armi
        for(int i=0;i<weapons.size();i++){
            JLabel lblWeap = new JLabel();
            lblWeap.setIcon(((Card)weapons.get(i)).getImage());
            middle.add(lblWeap);
        }
        
        // disegna le carte relative alle stanze
        for(int i=0;i<rooms.size();i++){
            JLabel lblRoom = new JLabel();
            lblRoom.setIcon(((Card)rooms.get(i)).getImage());
            bottom.add(lblRoom);
        }
        
        btnClose = new JButton("Close");
        btnClose.addActionListener(this);
        
        buttonPanel.add(btnClose);
        
        cardPanel.add(top,BorderLayout.NORTH);
        cardPanel.add(middle,BorderLayout.CENTER);
        cardPanel.add(bottom,BorderLayout.SOUTH);
        
        main.add(cardPanel,BorderLayout.CENTER);
        main.add(buttonPanel,BorderLayout.SOUTH);
        
        getContentPane().add(main);
        
        setResizable(false);
        pack();
        repaint();        
        
        
    }
    

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        // chiude la finestra
        if(src.equals(btnClose)){
            dispose();
        }
    }
    
}
