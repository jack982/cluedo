/*
 * WaitingForStartDialog.java
 * Created on 14-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Jacopo
 * 
 * 
 */
public class WaitingForStartDialog extends JDialog implements ActionListener {

    private JPanel main;
    private JButton btnStart;
    private JLabel lblMessage;
    
    
    public WaitingForStartDialog(){
        setTitle("Cluedo - Waiting for starting game ...");
           
        main = new JPanel();
        main.setLayout(new BorderLayout());
        
        lblMessage = new JLabel("Waiting for start confirmation from server...");
        btnStart = new JButton("Start Game");
        btnStart.addActionListener(this);
        btnStart.setEnabled(false);
        
        main.add(lblMessage,BorderLayout.CENTER);
        main.add(btnStart,BorderLayout.SOUTH);
        
        this.getContentPane().add(main);
        
        centerInScreen();
        setResizable(false);
        pack();
    }


    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if(src.equals(btnStart)){
            
        }
        
    }
    
    
    public void enableStartButton(boolean b){
        btnStart.setEnabled(b);
    }
    
    
    private void centerInScreen(){
        Toolkit theKit=this.getToolkit();
        Dimension wndSize=theKit.getScreenSize();
        this.setBounds(wndSize.width/4,wndSize.height/4,wndSize.width/2,wndSize.height/2);
    }
    
}
