/*
 * AccuseDialog.java
 * Created on 1-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import gamedata.Card;
import gamedata.Player;
import gamedata.Room;
import gamedata.Weapon;
import interfaces.Configuration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class AccuseDialog extends JDialog implements ActionListener {

    private List characters;
    private List weapons;
    private List rooms;
    
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
    private JComboBox cmbRooms;
    
    private JButton btnCancel;
    private JButton btnSuggest;
    
    
        
    public AccuseDialog(List rooms, List characters, List weapons,JFrame parent){
        super(parent,true);
        this.rooms = rooms;
        this.characters = characters;
        this.weapons = weapons;
        initDialog();   
    }
    
    
    private void initDialog() {
   
        setTitle("Cluedo - Accuse Dialog");
        
        lblRoom = new JLabel("Rooms:",JLabel.CENTER);
        lblCharacters = new JLabel("Character:",JLabel.CENTER);
        lblWeapons = new JLabel("Weapon:",JLabel.CENTER);
        
        
        cmbWeapons = new JComboBox(weapons.toArray());
        cmbWeapons.addActionListener(this);
        cmbCharacters = new JComboBox(characters.toArray());
        cmbCharacters.addActionListener(this);
        cmbRooms = new JComboBox(rooms.toArray());
        cmbRooms.addActionListener(this);
        
        
        imgRoom = new JLabel("",((Room)cmbRooms.getSelectedItem()).getRoomCard().getImage(),JLabel.CENTER);
        imgCharacter = new JLabel("",((Player)cmbCharacters.getSelectedItem()).getCharacterCard().getImage(),JLabel.CENTER);
        imgWeapon = new JLabel("",((Weapon)cmbWeapons.getSelectedItem()).getWeaponCard().getImage(),JLabel.CENTER);
        
        
        btnSuggest = new JButton("Accuse");
        btnSuggest.addActionListener(this);
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        
        panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(),"Make an Accusation"));
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
        centerTop.add(cmbRooms);
        
        centerBottom.add(imgCharacter);
        centerBottom.add(imgWeapon);
        centerBottom.add(imgRoom);
        
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
        Object src = e.getSource();
        
        if(src == btnSuggest){
            System.out.println("Accuse: "+cmbCharacters.getSelectedItem()+" with "+cmbWeapons.getSelectedItem()+" in the "+cmbRooms.getSelectedItem());
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
        else if(src == cmbRooms){
            Room r = (Room)cmbRooms.getSelectedItem();
            imgRoom.setIcon(r.getRoomCard().getImage());
            this.repaint();
        }
        
    }
    
    
}
