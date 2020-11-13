/*
 * PathSelector.java
 * Created on 19-feb-2006
 *
 * author: Jacopo Penazzi
 */
package gamedata;

import gui.PanelGameBoard;

import interfaces.Configuration;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

import rmi.GameConfiguration;
import sun.security.krb5.Config;

/**
 * @author Jacopo
 * 
 * 
 */
public class PathSelector {
    
    private static PathSelector instance;
    private ArrayList nodes;
    private Cell left, midLeft, midRight, right;
    
    
    private PathSelector(){
       nodes = new ArrayList();
    }
    
    public static PathSelector getInstance(){
        if(instance == null){
            instance = new PathSelector();
        }
        return instance;
    }
    
    
    public void clearPath(){
        nodes.clear();
    }
    
    
    public void createPath(Player player) {
        clearPath();
        
        if(player.isInARoom()) {
        	exitPath(player);
        }
        else {
        	normalPath(player);
        }
        
    }
    
    
    public void drawPathSelection(Graphics g) {
        Iterator i = nodes.iterator();
        System.out.println("ho disegnato le mosse possibili"); 
        
        while(i.hasNext()) {
        	Cell c;
        	Object obj = i.next();
        	System.out.println("PATH: "+obj);
        	if(obj != null) { 
        		c = (Cell)obj;
                // System.out.println("MOSSA: "+c.getX()+", "+c.getY());
        		g.setColor(new Color(0,0,0,80));
                g.fillRect(c.getX()*Configuration.getCellWidth(),c.getY()*Configuration.getCellWidth(),c.getWidth(),c.getHeight());
            }
        }
    }
    
    
    
    
       
    private boolean isDoor(Cell cell, Player player) {
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        ArrayList rooms = gc.getRooms();
        Iterator iroom = rooms.iterator();
        
        boolean isdoor = false;
        while(iroom.hasNext() && !isdoor) {
            Room room = (Room)iroom.next();
                        
            Iterator idoor = room.getDoors().iterator(); 
            while(idoor.hasNext() && !isdoor) {
                Door door = (Door)idoor.next();
                if(door.getX() == cell.getX() && door.getY() == cell.getY()) {
                	System.out.println(">>>>>>>>>>C'e' una bella porta!!!!");
                	switch(door.getSide()) {
                		case Configuration.DOOR_SIDE_NORTH : {
                			System.out.println("NORTH");
                			if(door.getY() == (player.getY() + 1) &&
                			   door.getX() == player.getX()) {
                				isdoor = true;
                			}
                			break;
                		}
                		case Configuration.DOOR_SIDE_EAST : {
                			System.out.println("EAST");
                			if(door.getX() == (player.getX() - 1) &&
                     		   door.getY() == player.getY()) {
                     			isdoor = true;
                     		}
                			break;
                		}
                		case Configuration.DOOR_SIDE_SOUTH : {
                			System.out.println("SOUTH");
                			if(door.getY() == (player.getY() - 1) &&
                     		   door.getX() == player.getX()) {
                     			isdoor = true;
                     		}
                			break;
                		}
                		case Configuration.DOOR_SIDE_WEST : {
                			System.out.println("WEST");
                			if(door.getX() == (player.getX() + 1) &&
                          	   door.getY() == player.getY()) {
                          		isdoor = true;
                          	}
                			break;
                		}
                	}           	
                }
            }
        }
        return isdoor;
    }

    
    private boolean isOutOfBoard(Cell cell) {
        int boardWidth = GameBoard.getInstance().getWidth();
        int boardHeight = GameBoard.getInstance().getHeight();
        
        if(cell.getX() < 0 || cell.getX() >= boardWidth || cell.getY() < 0 || cell.getY() >= boardHeight)
            return true;
        else return false;
    }

    private boolean isBlock(Cell cell) {
        ArrayList blocks = GameBoard.getInstance().getBlocks();
        Iterator iblock = blocks.iterator();
        
        boolean isblock = false;
        while(iblock.hasNext() && !isblock) {
            Block block = (Block)iblock.next();
            if(block.getX() == cell.getX() && block.getY() == cell.getY()) isblock = true;
        }
        
        return isblock;
    }

    private boolean isInsideRoom(Cell cell) {
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        ArrayList rooms = gc.getRooms();
        Iterator iter = rooms.iterator();
        
        boolean occupied = false;
        while(iter.hasNext() && !occupied) {
            
            Room room = (Room)iter.next();
            occupied = occupied || room.isInside(cell);         
        }
        
        return occupied;
    }
    
    
    private boolean isOccupied(Cell cell) {
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        Iterator p = gc.getAllCharacters().iterator();
    	
        boolean isplayer = false;
        
    	while(p.hasNext() && !isplayer) {
            Player player = (Player)p.next();
            if(player.getX() == cell.getX() && player.getY() == cell.getY()) isplayer = true;
        }
        
        return isplayer;
    }
    
    
    
   
    
    
    public boolean isAllowedMove(Cell c){
        boolean found = false;
        Iterator i = nodes.iterator();
        while(i.hasNext() && !found){
            Object obj = i.next();
            if(obj != null){
                Cell cell = (Cell)obj;
                if(cell.getX() == c.getX() &&
                   cell.getY() == c.getY()){
                        found = true;
                }
            }
        }
        return found;
    }

    
    
        
    
    private void exitPath(Player player) {
        GameConfiguration gc = ((GameConfiguration)GameConfiguration.getInstance().clone());
        Iterator i = gc.getRooms().iterator();
		
        boolean found = false;
		ArrayList doors = null;
		while(i.hasNext() && !found) {
			Room room = (Room)i.next();
			if(room.getName().equals(player.getRoom().getName())) {
				found = true;
				doors = room.getDoors();
			}
		}
		if(found) {
			Iterator d = doors.iterator();
			while(d.hasNext()) {
				Door door = (Door)d.next();
				Cell cell = null;
				switch(door.getSide()) {
					case Configuration.DOOR_SIDE_NORTH : {
						cell = new Cell(door.getX(),door.getY()-1);
						break;
					}
					case Configuration.DOOR_SIDE_EAST : {
						cell = new Cell(door.getX()+1,door.getY());
						break;
					}
					case Configuration.DOOR_SIDE_SOUTH : {
						cell = new Cell(door.getX(),door.getY()+1);
						break;
					}
					case Configuration.DOOR_SIDE_WEST : {
						cell = new Cell(door.getX()-1,door.getY());
						break;
					}
				}				
				nodes.add(cell);
			}
		}
    }
    
    private void normalPath(Player player) {
    	Cell cell = player.getPosition();
    	
    	left = new Cell();
        midLeft = new Cell();
        midRight = new Cell();
        right = new Cell();
        
        
        left.setX(cell.getX());
        left.setY(cell.getY()-1);
        midLeft.setX(cell.getX()+1);
        midLeft.setY(cell.getY());
        midRight.setX(cell.getX());
        midRight.setY(cell.getY()+1);
        right.setX(cell.getX()-1);
        right.setY(cell.getY());
        
        checkMoves(player);
        
        nodes.add(left);
        nodes.add(midLeft);
        nodes.add(midRight);
        nodes.add(right);
    }
    
    
    private void checkMoves(Player player){
    	if((isInsideRoom(left) ||
    		isBlock(left) ||
            isOutOfBoard(left) ||
            isOccupied(left)) && 
            !isDoor(left,player)) {
            left = null;
        }
            
        if((isInsideRoom(midLeft) ||
            isBlock(midLeft) ||
            isOutOfBoard(midLeft) ||
            isOccupied(midLeft)) && 
            !isDoor(midLeft,player)) {
             midLeft = null;
         }
            
         if((isInsideRoom(midRight) ||
             isBlock(midRight) ||
             isOutOfBoard(midRight) ||
             isOccupied(midRight)) && 
             !isDoor(midRight,player)) {
             midRight = null;
         }
            
         if((isInsideRoom(right) ||
             isBlock(right) ||
             isOutOfBoard(right) ||
             isOccupied(right)) && 
             !isDoor(right,player)) {
             right = null;
         }
    }
     
}
