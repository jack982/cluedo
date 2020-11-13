/*
 * PlayerInfoPanel.java
 * Created on 2-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;

import gamedata.Player;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
public class PlayerInfoPanel extends JPanel {

    private JLabel lblImage;
    private JLabel lblName;
    
    
    public PlayerInfoPanel(Player p){
        lblImage = new JLabel(p.getCharacterCard().getImage());
        lblName = new JLabel(p.getName());
        initPanel();
    }
    
    private void initPanel(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBorder(new TitledBorder(new EtchedBorder(),"Player Info"));
        setBackground(new Color(0,255,0).darker());
       // this.setPreferredSize(new Dimension(150,80));
        Box vbox = Box.createVerticalBox();
        Box hbox1 = Box.createHorizontalBox();
        Box hbox2 = Box.createHorizontalBox();
        hbox1.add(Box.createHorizontalStrut(30));
        hbox1.add(lblImage);
        hbox1.add(Box.createHorizontalStrut(30));
        hbox2.add(Box.createHorizontalStrut(30));
        hbox2.add(lblName);
        hbox2.add(Box.createHorizontalStrut(30));
        vbox.add(hbox1);
        vbox.add(hbox2);
        this.add(vbox);
        
    }
}
