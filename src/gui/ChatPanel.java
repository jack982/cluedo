/*
 * ChatPanel.java
 * Created on 2-mar-2006
 *
 * author: Jacopo Penazzi
 */
package gui;

import gamedata.ChatMessage;
import gamedata.Player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;



/**
 * @author Jacopo
 * 
 * 
 */
public class ChatPanel extends JPanel implements ActionListener, CaretListener, KeyListener {

	
	private static final String SYSTEM = "System";
	
    private JTextArea txtArea;
    private JTextField txtInsert;
    private JButton btnSend;
    private JScrollPane scroller;
    private String nickname;
    private Color playerColor;
    private Color chatBackgroundColor;
    
    private Player currentPlayer;
    private Player senderPlayer;
    
    private JTextPane textPane;
    
    private AbstractDocument doc;
    private SimpleAttributeSet[] attributeSet = new SimpleAttributeSet[2];
    
    
        
    public ChatPanel(Player currentPlayer){
      
    	chatBackgroundColor = new Color(240,240,130);
    	this.currentPlayer = currentPlayer;
    	nickname = currentPlayer.getName();
    	this.senderPlayer = currentPlayer;
        textPane = new JTextPane();
        textPane.setCaretPosition(0);
        textPane.setMargin(new Insets(5,5,5,5));
        textPane.setBackground(chatBackgroundColor.brighter());
        textPane.setEditable(false);
      //  textPane.setPreferredSize(new Dimension(600,70));
        StyledDocument styledDoc = textPane.getStyledDocument();
        if (styledDoc instanceof AbstractDocument) {
            doc = (AbstractDocument)styledDoc;
      //      doc.setDocumentFilter(new DocumentSizeFilter(MAX_CHARACTERS));
        }
                
        attributeSet = initAttributeSet();
               
        
        scroller = new JScrollPane(textPane);
        scroller.setPreferredSize(new Dimension(600,70));
        
        txtInsert = new JTextField();
        txtInsert.setBackground(Color.WHITE);
        txtInsert.addCaretListener(this);
        txtInsert.addKeyListener(this);
        
        btnSend = new JButton("Send");
        btnSend.addActionListener(this);
        btnSend.setEnabled(false);
                
        setBackground(new Color(0,255,0).darker());
        setBorder(new TitledBorder(new EtchedBorder(),"Chat"));
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        Box hbox1 = Box.createHorizontalBox();
        hbox1.add(scroller);
             
        Box hbox2 = Box.createHorizontalBox();
        hbox2.add(txtInsert);
        hbox2.add(Box.createHorizontalStrut(2));
        hbox2.add(btnSend);
        
        this.add(hbox1);
        this.add(Box.createVerticalStrut(3));
        this.add(hbox2);
        
      //  setPreferredSize(new Dimension(600,100));
        
    }


    private void sendText(String msg){
        if(!msg.equals("")){
            try {
            	if(nickname.equals(SYSTEM)) {
            		doc.insertString(doc.getLength(),nickname+": ",attributeSet[1]);
            		doc.insertString(doc.getLength(),msg+"\n",attributeSet[1]);
            	}
            	else {
            		doc.insertString(doc.getLength(),nickname+": ",attributeSet[0]);
            		doc.insertString(doc.getLength(),msg+"\n",null);
            	}
                
                senderPlayer = currentPlayer;
            } catch (BadLocationException e) {
               e.printStackTrace();
            }
        }
    }
    
    
    private SimpleAttributeSet[] initAttributeSet(){
        attributeSet[0] = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributeSet[0], "SansSerif");
        StyleConstants.setFontSize(attributeSet[0], 12);
        StyleConstants.setForeground(attributeSet[0],senderPlayer.getPlayerColor().darker());
        StyleConstants.setBold(attributeSet[0], true);
        
        attributeSet[1] = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attributeSet[1], "SansSerif");
        StyleConstants.setFontSize(attributeSet[1], 15);
        StyleConstants.setBackground(attributeSet[1],chatBackgroundColor.darker());
        StyleConstants.setForeground(attributeSet[1],Color.BLACK);
        StyleConstants.setBold(attributeSet[1], true);
        
        return attributeSet;
    }
    
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if(src.equals(btnSend)){
            sendText(txtInsert.getText());
            txtInsert.setText("");
        }
        
    }


    public void caretUpdate(CaretEvent e) {
        Object src = e.getSource();
       
        if(src.equals(txtInsert)){
            if(txtInsert.getText().length()>0){
                btnSend.setEnabled(true);
            }
            else{
                btnSend.setEnabled(false);
            }
        }
        
    }


    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }


    public void keyPressed(KeyEvent e) {
        Object src = e.getSource();
        if(src.equals(txtInsert) && e.getKeyCode() == KeyEvent.VK_ENTER){
            sendText(txtInsert.getText());
            txtInsert.setText("");
        }
        
    }


    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }


	public void printMessage(ChatMessage message) {
		if(message.getSender() == null) {
			nickname = SYSTEM;
		}
		else {
			nickname = message.getSender().getName();
			playerColor = message.getSender().getPlayerColor();
		}
		
		this.sendText(message.getMessage());
	}
    
    
  
       
}
    
    

