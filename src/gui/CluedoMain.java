/*
 * Created on 8-ott-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gui;

import gamedata.Block;
import gamedata.Card;
import gamedata.Cell;
import gamedata.Door;
import gamedata.GameBoard;
import gamedata.Player;
import gamedata.RectArea;
import gamedata.Room;
import gamedata.SecretPassage;
import gamedata.StartPoint;
import gamedata.TextureHandler;
import gamedata.Weapon;
import interfaces.Configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuBar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import com.sun.java.swing.plaf.motif.MotifLookAndFeel;

import de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel;



import rmi.GameConfiguration;
import rmi.GameController;
import sun.security.krb5.internal.crypto.g;

import util.Deck;




public class CluedoMain extends JFrame implements MouseListener, MouseMotionListener, ActionListener{

	//componenti grafiche
	private PanelGameBoard tabellone;
	private JScrollPane scrollpane;
	
	// menu
    private JMenuBar menuBar;
    private JMenu mnuGame;
    private JMenu mnuOptions;
    private JMenu mnuHelp;
    
    private JMenuItem mnuItemNew;
    private JMenuItem mnuItemJoin;
    private JMenuItem mnuItemExit;
    
    private JMenuItem mnuItemColors;
    private JMenuItem mnuItemTextures;
    
    private JMenuItem mnuItemRules;
    private JMenuItem mnuItemAbout;
   
    // pannelli dell'interfaccia
    private ControlPanel controlPanel;
    private ChatPanel chatPanel;
    
    // controller
    private GameController controller;
    
    // giocatori    
    private Player player,player2,player3,player4,player5,player6;
       
    
	//altro
	private GameBoard gameBoard;
	private Room cucina, salaBallo, salotto, centro, salaPranzo,
                 salaBiliardo, biblioteca, veranda, anticamera,
                 studio;
	
    private Weapon candlestick, dagger, gun, leadpipe, rope, spanner;
	
    
    private GameConfiguration gc;
    
	
	public CluedoMain() {

		super("Cluedo - ver 0.48");
        super.addWindowListener(new WindowAdapter(){
            // imposta il comportamento di default alla chiusura della finestra
             public void windowClosing(WindowEvent e) {
                exitGame();
             }
        });
	//	super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout());
		
       
        
        //configurazione impostazioni
        try {
            Configuration.loadConfiguration();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Problemi nel caricamento della configurazione");
        }
        
		
		//Gestione Texture
		TextureHandler th = TextureHandler.getInstance();
		
		//carica texture da file di configurazione
		try {
			th.initialize();
		} catch (IOException e1) {
			System.out.println("Non esiste un file delle texture");
		}
		
		//Qui vanno aggiunte eventuali nuove texture
	//	th.addTexture(Configuration.getTexturesPath()+"boardTexture.jpg","board");
		
		
		//salva le texture sul file
		try {
			th.finalize();
		} catch (IOException e) {
			System.out.println("Impossibile salvare il file delle texture");
		}
		
		
	
		/*try {
			Configuration.saveConfiguration();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Problemi nel salvataggio della configurazione");
		}*/
		
		
		
		//istanzia una game board
		gameBoard = GameBoard.getInstance();
	    
		//crea blocks
		gameBoard.insertBlock(new Block(0,0));
		gameBoard.insertBlock(new Block(1,0));
		gameBoard.insertBlock(new Block(2,0));
		gameBoard.insertBlock(new Block(3,0));
		gameBoard.insertBlock(new Block(4,0));
		gameBoard.insertBlock(new Block(5,0));
		gameBoard.insertBlock(new Block(6,0));
		gameBoard.insertBlock(new Block(7,0));
		gameBoard.insertBlock(new Block(8,0));
		gameBoard.insertBlock(new Block(15,0));
		gameBoard.insertBlock(new Block(16,0));
		gameBoard.insertBlock(new Block(17,0));
		gameBoard.insertBlock(new Block(18,0));
		gameBoard.insertBlock(new Block(19,0));
		gameBoard.insertBlock(new Block(20,0));
		gameBoard.insertBlock(new Block(21,0));
		gameBoard.insertBlock(new Block(22,0));
		gameBoard.insertBlock(new Block(23,0));
		gameBoard.insertBlock(new Block(6,1));
		gameBoard.insertBlock(new Block(17,1));
		gameBoard.insertBlock(new Block(23,5));
		gameBoard.insertBlock(new Block(0,6));
        gameBoard.insertBlock(new Block(23,7));
        gameBoard.insertBlock(new Block(0,8));
        gameBoard.insertBlock(new Block(23,13));
        gameBoard.insertBlock(new Block(23,14));
        gameBoard.insertBlock(new Block(0,16));
        gameBoard.insertBlock(new Block(0,18));
        gameBoard.insertBlock(new Block(23,18));
        gameBoard.insertBlock(new Block(23,20));
        gameBoard.insertBlock(new Block(6,24));
        gameBoard.insertBlock(new Block(8,24));
        gameBoard.insertBlock(new Block(15,24));
        gameBoard.insertBlock(new Block(17,24));
				
        //staring points
         //rosso
        gameBoard.insertStartPoint(new StartPoint(9,0,Color.RED));
        //verde
        gameBoard.insertStartPoint(new StartPoint(14,0,Color.GREEN));
        //viola
        gameBoard.insertStartPoint(new StartPoint(23,6,Color.MAGENTA));
        //bianco
        gameBoard.insertStartPoint(new StartPoint(0,17,Color.WHITE));
        //blu
        gameBoard.insertStartPoint(new StartPoint(23,19,Color.BLUE));
        //giallo
        gameBoard.insertStartPoint(new StartPoint(7,24,Color.YELLOW));
        
        
        
        // crea il controller della partita
        try {
            controller = new GameController(); 
        } catch (RemoteException e) {
            e.printStackTrace();
        }
       
        //DEBUG
        if(controller!=null)System.out.println("CONTROLLER PRESENTE!");
        
        // registro e avvio il servizio
        System.setSecurityManager(new RMISecurityManager());
        try {
            Naming.rebind("Controller",controller);
          
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        System.out.println("controller della partita registrato correttamente");
        
       
        playTest();
        
        initGUI();
        
                

        
	}

		
	
	private void initGUI() {
	    
        tabellone = new PanelGameBoard(controller);
        
        scrollpane = new JScrollPane(tabellone);
        tabellone.setToolTipText(null);
        
        this.getContentPane().add(scrollpane);
        
        controller.setScrollPane(scrollpane);
        
        
        controller.centerViewOnPlayer();
        
        
        //setting delle texture
        gameBoard.setBoardTexture("board");
        
        
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(0,255,0).darker());
        rightPanel.setPreferredSize(new Dimension(150,500));
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.Y_AXIS));
         
        // crea il CONTROLPANEL 
        rightPanel.add(new PlayerInfoPanel(player));
        controlPanel = new ControlPanel(tabellone,controller,this);
        controller.setControlPanel(controlPanel);
        rightPanel.add(controlPanel);
        this.getContentPane().add(rightPanel,BorderLayout.EAST);
        
        //  crea il CHATPANEL
        chatPanel = new ChatPanel(controller.getCurrentPlayer());
        controller.setChatPanel(chatPanel);
        this.getContentPane().add(chatPanel,BorderLayout.SOUTH);
        
        
      //  this.getContentPane().add(menuBar,BorderLayout.NORTH);
        menuBar = buildMenuBar();
        this.setJMenuBar(menuBar);
        
        tabellone.addMouseListener(this);
        tabellone.addMouseMotionListener(this);
        
       
        this.setResizable(true);
        this.setPreferredSize(new Dimension(770,700));
      //  centerInScreen();
        this.pack();
        this.setVisible(true);
        
    }

    
    
    private JMenuBar buildMenuBar(){
        menuBar = new JMenuBar();
        
        mnuGame = new JMenu("Game");
        mnuGame.setMnemonic(KeyEvent.VK_G);
        
        mnuItemNew = new JMenuItem("New Game");
        mnuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_DOWN_MASK));
        mnuItemNew.addActionListener(this);
        
        mnuItemJoin = new JMenuItem("Join Game");
        mnuItemJoin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,InputEvent.CTRL_DOWN_MASK));
        mnuItemJoin.addActionListener(this);
        
        mnuItemExit = new JMenuItem("Exit Game");
        mnuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_DOWN_MASK));
        mnuItemExit.addActionListener(this);
        
        mnuGame.add(mnuItemNew);
        mnuGame.add(mnuItemJoin);
        mnuGame.add(new JSeparator());
        mnuGame.add(mnuItemExit);
        
        mnuOptions = new JMenu("Options");
        mnuOptions.setMnemonic(KeyEvent.VK_O);
        mnuItemColors = new JMenuItem("Colors");
        mnuItemTextures = new JMenuItem("Textures");
        mnuOptions.add(mnuItemColors);
        mnuOptions.add(mnuItemTextures);
        
        mnuHelp = new JMenu("Help");
        mnuHelp.setMnemonic(KeyEvent.VK_H);
        mnuItemRules = new JMenuItem("Rules");
        mnuItemAbout = new JMenuItem("About");
        mnuHelp.add(mnuItemRules);
        mnuHelp.add(new JSeparator());
        mnuHelp.add(mnuItemAbout);
                
        menuBar.add(mnuGame);
        menuBar.add(mnuOptions);
        menuBar.add(mnuHelp);
        
        return menuBar;
    }
    
    
    //--------------------------------------//
    // METODI PER IL CONTROLLO DEL MOUSE    //
    //--------------------------------------//
    
    public void mouseClicked(MouseEvent e) {
        Object src = e.getSource();
        if(src.equals(tabellone)){
            int x = e.getX() / Configuration.getCellWidth();
            int y = e.getY() / Configuration.getCellHeight();
        
           
        
            boolean turnoMossa = true; // da togliere
       
            System.out.println("MOUSECLICKED!");
        
            if(turnoMossa && controller.getMovesLeft() > 0){
       
                System.out.println("mosse rimaste: " + controller.getMovesLeft());    
                Cell wantedMove = new Cell(x,y);
               
                controller.setPlayerMove(wantedMove);
                controller.movePlayer();
            }
            
            controlPanel.setMovesLeft("Moves Left:" +controller.getMovesLeft());
            if(controller.getMovesLeft() == 0){
                controlPanel.setDie("no die thrown");
            }
                //lblMosseRimaste.setText("Mosse: "+controller.getMovesLeft());
        }    	
    }


    public void mousePressed(MouseEvent e) {
      
    }



    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }



    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }



    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }



    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if(src.equals(mnuItemNew)){
            StartWizardNew swn = new StartWizardNew();
            swn.setVisible(true);
        }
        else if(src.equals(mnuItemJoin)){
            StartWizardJoin swj = new StartWizardJoin(controller);
            swj.setVisible(true);
        }
        else if(src.equals(mnuItemExit)){
           exitGame();
        }
    }

    
    
    private void exitGame(){
        int answer = JOptionPane.showConfirmDialog(this,
                                                   "Are you sure to leave game?",
                                                   "Exiting",
                                                   JOptionPane.YES_NO_OPTION);
        
        if(answer == 0){
            dispose();
            System.out.println("Exiting...");
            System.exit(0);
        }
    }


    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }



    public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }
    
    
    
    private void centerInScreen(){
        Toolkit theKit=this.getToolkit();
        Dimension wndSize=theKit.getScreenSize();
        this.setBounds(wndSize.width/2,wndSize.height/2,wndSize.width/2,wndSize.height/2);
    }
    

    
    /*############################ MAIN METHOD ##########################*/

    public static void main(String[] args) {
      /*  try
        {
            UIManager.setLookAndFeel(new SyntheticaBlackStarLookAndFeel());
          
        } 
        catch (Exception e) 
        {
          e.printStackTrace();
        }
     */
     
        new CluedoMain();
        
    }
    
    /*####################################################################*/
    
    
    
    public void playTest(){

        gc = GameConfiguration.getInstance();
        
        // crea cucina
        cucina = new Room(Configuration.KITCHEN,Color.ORANGE);
        cucina.insertPart(new RectArea(0,1,5,1));
        cucina.insertPart(new RectArea(1,1,6,5));
        cucina.setPassage(new SecretPassage(5,1,null));
        cucina.insertDoor(new Door(4,6,Configuration.DOOR_SIDE_SOUTH));
                
        //crea sala da ballo
        salaBallo = new Room(Configuration.BALL,Color.CYAN);
        salaBallo.insertPart(new RectArea(8,2,6,2));
        salaBallo.insertPart(new RectArea(10,0,8,4));
        salaBallo.insertPart(new RectArea(14,2,6,2));
        salaBallo.insertDoor(new Door(8,5,Configuration.DOOR_SIDE_WEST));
        salaBallo.insertDoor(new Door(9,7,Configuration.DOOR_SIDE_SOUTH));
        salaBallo.insertDoor(new Door(14,7,Configuration.DOOR_SIDE_SOUTH));
        salaBallo.insertDoor(new Door(15,5,Configuration.DOOR_SIDE_EAST));
        
        //crea salotto
        salotto = new Room(Configuration.LOUNGE,Color.YELLOW);
        salotto.insertPart(new RectArea(18,1,4,1));
        salotto.insertPart(new RectArea(19,1,5,4));
        salotto.insertPart(new RectArea(23,1,4,1));
        salotto.setPassage(new SecretPassage(22,5,null));
        salotto.insertDoor(new Door(18,4,Configuration.DOOR_SIDE_SOUTH));
        
        
        //crea centro
        centro = new Room(Configuration.CENTER,Color.BLACK);
        centro.insertPart(new RectArea(10,10,7,5));
        
        
        //crea sala da pranzo
        salaPranzo = new Room(Configuration.DINING,Color.PINK);
        salaPranzo.insertPart(new RectArea(0,9,7,5));
        salaPranzo.insertPart(new RectArea(5,10,6,3));
        salaPranzo.insertDoor(new Door(7,12,Configuration.DOOR_SIDE_EAST));
        salaPranzo.insertDoor(new Door(6,15,Configuration.DOOR_SIDE_SOUTH));
        
        //crea sala biliardo
        salaBiliardo = new Room(Configuration.BILLIARD,Color.GREEN);
        salaBiliardo.insertPart(new RectArea(18,8,5,6));
        salaBiliardo.insertDoor(new Door(18,9,Configuration.DOOR_SIDE_WEST));
        salaBiliardo.insertDoor(new Door(22,12,Configuration.DOOR_SIDE_SOUTH));
        
        // crea biblioteca
        biblioteca = new Room(Configuration.LIBRARY,Color.MAGENTA);
        biblioteca.insertPart(new RectArea(17,15,3,1));
        biblioteca.insertPart(new RectArea(18,14,5,5));
        biblioteca.insertPart(new RectArea(23,15,3,1));
        biblioteca.insertDoor(new Door(17,16,Configuration.DOOR_SIDE_WEST));
        biblioteca.insertDoor(new Door(20,14,Configuration.DOOR_SIDE_NORTH));
        
        
        // crea veranda
        veranda = new Room(Configuration.CONSERVATORY,Color.lightGray);
        veranda.insertPart(new RectArea(0,19,6,6));
        veranda.insertPart(new RectArea(6,19,5,1));
        veranda.insertDoor(new Door(6,19,Configuration.DOOR_SIDE_NORTH));
        veranda.setPassage(new SecretPassage(0,19,null));
        
        
        
        // crea anticamera
        anticamera = new Room(Configuration.HALL,Color.WHITE);
        anticamera.insertPart(new RectArea(9,18,7,6));
        anticamera.insertDoor(new Door(11,18,Configuration.DOOR_SIDE_NORTH));
        anticamera.insertDoor(new Door(12,18,Configuration.DOOR_SIDE_NORTH));
        anticamera.insertDoor(new Door(14,20,Configuration.DOOR_SIDE_EAST));
        
        // crea studio
        studio =  new Room(Configuration.STUDY,Color.GRAY);
        studio.insertPart(new RectArea(17,21,3,1));
        studio.insertPart(new RectArea(18,21,4,6));
        studio.insertDoor(new Door(17,21,Configuration.DOOR_SIDE_NORTH));
        studio.setPassage(new SecretPassage(23,21,null));
        
        
        //mapping dei passaggi segreti
        cucina.getPassage().setLink(studio);
        studio.getPassage().setLink(cucina);
        veranda.getPassage().setLink(salotto);
        salotto.getPassage().setLink(veranda);
        
        
        //inserimento stanze
        gc.getRooms().add(cucina);
        gc.getRooms().add(salaBallo);
        gc.getRooms().add(salotto);
        gc.getRooms().add(centro);
        gc.getRooms().add(salaPranzo);
        gc.getRooms().add(salaBiliardo);
        gc.getRooms().add(biblioteca);
        gc.getRooms().add(veranda);
        gc.getRooms().add(anticamera);
        gc.getRooms().add(studio);
        
        // weapons
        candlestick = new Weapon(Configuration.CANDLESTICK);
        gc.getWeapons().add(candlestick);
        salaPranzo.addWeapon(candlestick);
        
        dagger = new Weapon(Configuration.DAGGER);
        gc.getWeapons().add(dagger);
        veranda.addWeapon(dagger);
        
        gun = new Weapon(Configuration.GUN);
        gc.getWeapons().add(gun);
        studio.addWeapon(gun);
        
        leadpipe = new Weapon(Configuration.LEADPIPE);
        gc.getWeapons().add(leadpipe);
        salotto.addWeapon(leadpipe);
        
        rope = new Weapon(Configuration.ROPE);
        gc.getWeapons().add(rope);
        salaBallo.addWeapon(rope);
        
        spanner = new Weapon(Configuration.SPANNER);
        gc.getWeapons().add(spanner);
        cucina.addWeapon(spanner);
    
        
        
        
        // crea il giocatore
        player = new Player(Configuration.PLAYER_1);
        player.setPosition(new Cell(23,6));
        
        // setta le carte non viste dal giocatore (tutto il mazzo)
        player.setNotSeenCards(Deck.getAllDeckCards());
        
            
        
        //setta il giocatore
        controller.setCurrentPlayer(player);
        gc.setPlayingPlayer(player);
        
        //------------- TEST --------------//        
        player.addSeenCard(new Card(Configuration.BALL));
        player.addSeenCard(new Card(Configuration.HALL));
        player.addSeenCard(new Card(Configuration.KITCHEN));
        player.addSeenCard(new Card(Configuration.GUN));
        player.addSeenCard(new Card(Configuration.ROPE));
        //player.addCard(new Card(Configuration.DAGGER));
        player.addSeenCard(new Card(Configuration.CHARACTER_NAME_1));
        //player.addCard(new Card(Configuration.CHARACTER_NAME_3));
        player.addSeenCard(new Card(Configuration.CHARACTER_NAME_4));
        player.addSeenCard(new Card(Configuration.CHARACTER_NAME_5));
        
        
        
     
        //crea un altro giocatore
        player2 = new Player(Configuration.PLAYER_2);
        player2.setPosition(new Cell(21,6));
        
        
        player3 = new Player(Configuration.PLAYER_3);
        player4 = new Player(Configuration.PLAYER_4);
        player5 = new Player(Configuration.PLAYER_5);
        player6 = new Player(Configuration.PLAYER_6);
        
        
        gc.getPlayers().add(player);
        gc.getPlayers().add(player2);
        gc.getNotPlayingCharacters().add(player3);
        gc.getNotPlayingCharacters().add(player4);
        gc.getNotPlayingCharacters().add(player5);
        gc.getNotPlayingCharacters().add(player6);
        
        
        
        
        
        
    }
    
    
    
} //Ends class
