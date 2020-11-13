package gui;

import gamedata.Block;
import gamedata.Cell;
import gamedata.GameBoard;
import gamedata.PathSelector;
import gamedata.Player;
import gamedata.Room;
import gamedata.StartPoint;
import gamedata.Weapon;
import interfaces.Configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.ToolTipManager;
import javax.swing.plaf.ToolTipUI;

import rmi.GameConfiguration;
import rmi.GameController;


public class PanelGameBoard extends JPanel implements MouseMotionListener {
	
	private GameBoard gameBoard;
    private PathSelector ps;
    
    private GameController controller;
    
    
    public PanelGameBoard(GameController controller) {
        super(new BorderLayout());
        this.gameBoard = GameBoard.getInstance();
        this.ps = PathSelector.getInstance();
        this.addMouseMotionListener(this);
        
        this.setPreferredSize(new Dimension(gameBoard.getWidth()*Configuration.getCellWidth() + 2*Configuration.getCellWidth(),
                                            gameBoard.getHeight()*Configuration.getCellHeight() + 2*Configuration.getCellHeight()));
        
        this.controller = controller;
    }		
    
       

    public void paintComponent(Graphics g) {
		super.paintComponent(g);
        
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        
		Iterator i;
		
		//disegna il tabellone
		gameBoard.drawGameBoard(g);
		
			
		//disegna blocks
		i = gameBoard.getBlocks().iterator();
		while(i.hasNext()) {
			Block block = (Block)i.next();
			block.drawBlock(g);
		}
				       
        
        // disegna start points
        i = gameBoard.getStartPoints().iterator();
        while(i.hasNext()){
            StartPoint start = (StartPoint)i.next();
            start.drawStart(g);
        }
               
        //disegna le stanze
		i = gc.getRooms().iterator();
		while(i.hasNext()) {
			Room room = (Room)i.next();
            Iterator iter = room.getPlayers().iterator();
            boolean highlighted = false;
            while(iter.hasNext()){
                Player p = (Player)iter.next();
                if(p.getPlayerID() == controller.getCurrentPlayer().getPlayerID()){
                    highlighted = true;
                }
            }
			room.drawRoom(g,highlighted);
		}
        
        
        
        ps.drawPathSelection(g);
        
        
        //disegna i player
        i = gc.getAllCharacters().iterator();
        while(i.hasNext()) {
            Player player = (Player)i.next();
            player.drawPlayer(g);
        }
        
    	this.revalidate();
        g.dispose();
	}


    
	public void mouseMoved(MouseEvent e) {
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        
		int x = e.getX() / Configuration.getCellWidth();
		int y = e.getY() / Configuration.getCellHeight();
		
		Cell cell = new Cell(x,y);
		
		Iterator i = gc.getRooms().iterator();
		Room currentRoom = null;
		boolean found = false;
		while(i.hasNext() && !found) {
			Room room = (Room)i.next();
			if(room.isInside(cell)) {
				currentRoom = room;
				found = true;
			}
		}
		
		if(found && !currentRoom.getName().equals(Configuration.CENTER)) {
			//setto il colspan necessario
			int colspan = Math.max(currentRoom.getPlayers().size(),currentRoom.getWeapons().size());
			if(colspan == 0) colspan = 1;
			
			
			//setto i parametri del tooltip
			ToolTipManager tipManager = ToolTipManager.sharedInstance();
			tipManager.setDismissDelay(5000);
			tipManager.setReshowDelay(0);
			
			//preparo il body dell'html per il tooltip
			String htmlBody = 
				"<html>"+
					"<table>"+
						"<tr align=\"center\">"+
							"<td colspan="+colspan+"><h2>"+currentRoom.getName()+"</h2></td>"+
						"</tr>"+
						"<tr>"+
							"<td colspan="+colspan+">&nbsp;</td>"+
						"</tr>"+
						"<tr>"+
							"<td colspan="+colspan+"><h4>Players currently in the "+currentRoom.getName()+"</h4></td>"+
						"</tr>";
			
			//se ci sono player nella room corrente ne inserisco le immagini...
			if(currentRoom.getPlayers().size() > 0) {
				Iterator p = currentRoom.getPlayers().iterator();
				ArrayList playerNames = new ArrayList();
				
				htmlBody = htmlBody + "<tr>";	//apro la riga delle immagini dei personaggi
				while(p.hasNext()) {
					Player player = (Player)p.next();
					htmlBody = htmlBody + 
						"<td align=\"center\">"+
							"<img src=\"file:"+player.getCharacterCard().getImagePath()+"\"></img>"+
                        "</td>";
                    playerNames.add(player.getName());
				}
				htmlBody = htmlBody + "</tr>";	//chiudo la riga delle immagini dei personaggi
				
				//...e i nomi degli utenti relativi ad ogni personaggio
				htmlBody = htmlBody + "<tr>";	//apro la riga dei nomi dei personaggi
				p = playerNames.iterator();
				while(p.hasNext()) {
					String name = (String)p.next();
					htmlBody = htmlBody +
						"<td align=\"center\">"+name+"</td>";
				}
				htmlBody = htmlBody + "</tr>";	//chiudo la riga dei nomi dei personaggi
			}
			else {
				//non ci sono player
				htmlBody = htmlBody +
					"<tr>"+
						"<td colspan="+colspan+">No players in the "+currentRoom.getName()+"</td>"+
		            "</tr>";
			}
			
			
			htmlBody = htmlBody + 
				"<tr>"+
					"<td colspan="+colspan+">&nbsp;</td>"+
				"</tr>"+
				"<tr>"+
					"<td colspan="+colspan+"><h4>Weapons currently in the "+currentRoom.getName()+"</h4></td>"+
		        "</tr>";
			
			//se ci sono armi nella room ne disegno le immagini...
			if(currentRoom.getWeapons().size() > 0) {
				Iterator w = currentRoom.getWeapons().iterator();
				ArrayList weaponNames = new ArrayList();
				
				htmlBody = htmlBody + "<tr>";	//apro la riga delle immagini delle armi
				while(w.hasNext()) {
					Weapon weapon = (Weapon)w.next();
					htmlBody = htmlBody + 
						"<td align=\"center\">"+
							"<img src=\"file:"+weapon.getWeaponCard().getImagePath()+"\"></img>"+
						"</td>";
					weaponNames.add(weapon.getName());
				}
				htmlBody = htmlBody + "</tr>";	//chiudo la riga delle immagini delle armi
				
				//...e i nomi
				htmlBody = htmlBody + "<tr>";	//apro la riga dei nomi delle armi
				w = weaponNames.iterator();
				while(w.hasNext()) {
					String name = (String)w.next();
					htmlBody = htmlBody +
						"<td align=\"center\">"+name+"</td>";
				}
				htmlBody = htmlBody + "</tr>";	//chiudo la riga dei nomi delle armi
			}
			else {
				//non ci sono armi
				htmlBody = htmlBody +
					"<tr>"+
						"<td colspan="+colspan+">No weapons in the "+currentRoom.getName()+"</td>"+
		            "</tr>";
			}
			
			htmlBody = htmlBody + "</table></table>";
			
			
		    this.setToolTipText(htmlBody);
		}
		else {
			this.setToolTipText(null);
		}
		
	}



	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


/*
	public void highlightRoom(Room room, boolean on) {
		if(on) {
			room.setColor(new Color(room.getDefaultColor().getRed(),
								    room.getDefaultColor().getGreen(),
								    room.getDefaultColor().getBlue(),
								    200));
			this.repaint();
		}
		else {
			room.setColor(room.getDefaultColor());
			this.repaint();
		}
		
	}
*/
	
}
