package gamedata;

import gui.CluedoMain;
import interfaces.Configuration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class GameBoard {

	private int height;
	private int width;
	private Color gridColor;
	private Color defaultBgColor;
	private String boardTexture;
//	private ArrayList rooms;
	private ArrayList blocks;
	private ArrayList startPoints;
//    private ArrayList allCharacters;
//    private ArrayList weapons;
    
	private static GameBoard instance;
	
		
	public static GameBoard getInstance() {
		if(instance == null) {
			instance = new GameBoard(Configuration.DEFAULT_BOARD_NUMCELL_HEIGHT,Configuration.DEFAULT_BOARD_NUMCELL_WIDTH);
		}
		return instance;
	}
	
	
	private GameBoard(int height, int width) {
		this.height = height;
		this.width = width;
//		this.rooms = new ArrayList();
		this.blocks = new ArrayList();
		this.startPoints = new ArrayList();
//        this.weapons = new ArrayList();
		this.gridColor = Color.BLACK;
		this.defaultBgColor = new Color(246,245,158);
//        this.allCharacters = new ArrayList();
	}
	
	
	


/*	public void insertRoom(Room room) {
		rooms.add(room);
	}
*/	
	public void insertBlock(Block block) {
		blocks.add(block);
	}
	
	public void insertStartPoint(StartPoint start) {
		startPoints.add(start);
	}
	
	

	public String getBoardTexture() {
		return boardTexture;
	}



	public void setBoardTexture(String boardTexture) {
		this.boardTexture = boardTexture;
	}



	public Color getDefaultBgColor() {
		return defaultBgColor;
	}



	public void setDefaultBgColor(Color defaultBgColor) {
		this.defaultBgColor = defaultBgColor;
	}


	

	public Color getGridColor() {
		return gridColor;
	}



	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}



	public int getHeight() {
		return height;
	}



	public void setHeight(int height) {
		this.height = height;
	}

/*

	public ArrayList getRooms() {
		return rooms; 
	}



	public void setRooms(ArrayList rooms) {
		this.rooms = rooms;
	}
*/


	public ArrayList getStartPoints() {
		return startPoints;
	}



	public void setStartPoints(ArrayList startPoints) {
		this.startPoints = startPoints;
	}



	public ArrayList getBlocks() {
		return blocks;
	}


	public void setBlocks(ArrayList blocks) {
		this.blocks = blocks;
	}


/*	
    public ArrayList getAllCharacters() {
        return allCharacters;
    }


   
    public void setAllCharacters(ArrayList players) {
        this.allCharacters = players;
    }
  
    
    
    public ArrayList getWeapons() {
        return weapons;
    }


    public void setWeapons(ArrayList weapons) {
        this.weapons = weapons;
    }

    
    public void insertWeapon(Weapon w) {
        weapons.add(w);
    }
    
    

    public void addPlayer(Player player){
        this.allCharacters.add(player);
    }
*/

    public int getWidth() {
		return width;
	}



	public void setWidth(int width) {
		this.width = width;
	}
	
    
    
    
	
	
	public void drawGameBoard(Graphics g) {
		int x1,x2,y1,y2;
				
		TextureHandler th = TextureHandler.getInstance();
		Texture tx = th.getTexture(boardTexture);
				
		//sfondo pavimento
		if(tx != null) {
			for(int i=0; i<height; i++) {
				for(int j=0; j<width; j++) {
					g.drawImage(tx.getImage(),j*Configuration.getCellHeight(),i*Configuration.getCellWidth(),Configuration.getCellWidth(),Configuration.getCellHeight(),null);
				}
			}
		}
		else {
			g.setColor(getDefaultBgColor());
			g.fillRect(0,0,getWidth()*Configuration.getCellWidth(),getHeight()*Configuration.getCellHeight());
		}
		
		      	
		//griglia verticale
		y2 = getHeight()*Configuration.getCellHeight();
		x1 = x2 = y1 = 0;
		while(x1 <= getWidth()*Configuration.getCellWidth()) {
			g.setColor(getGridColor());
			g.drawLine(x1,y1,x2,y2);
        	
			x1 += Configuration.getCellWidth();
			x2 += Configuration.getCellWidth();
		}
        
		//griglia orizzontale
		x2 = getWidth()*Configuration.getCellWidth();
		x1 = y1 = y2 = 0;
		while(y1 <= getHeight()*Configuration.getCellHeight()) {
			g.setColor(getGridColor());
			g.drawLine(x1,y1,x2,y2);
        
			y1 += Configuration.getCellHeight();
			y2 += Configuration.getCellHeight();
		}
				
	}
	
}
