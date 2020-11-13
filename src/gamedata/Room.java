package gamedata;

import interfaces.Configuration;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import javax.swing.ImageIcon;

import sun.java2d.loops.DrawLine;


public class Room implements Serializable {
	
	private String name;
	private Color defaultColor;
	private Color color;
	private Color wallsColor;
	private String roomTexture;
	private ArrayList parts;
	private ArrayList doors;
	private SecretPassage passage;
    private Card roomCard;
    private ArrayList players;
    private ArrayList weapons;
	
		
	public Room(String name, Color color) {
		this.name = name;
		this.color = color;
		this.defaultColor = color;
		this.parts = new ArrayList();
		this.doors = new ArrayList();
		this.players = new ArrayList();
		this.weapons = new ArrayList();
		this.wallsColor = Color.RED;
        
        if(name.equals(Configuration.KITCHEN)){
            roomCard = new Card(Configuration.KITCHEN);
        }
        else if(name.equals(Configuration.STUDY)){
            roomCard = new Card(Configuration.STUDY);
        }
        else if(name.equals(Configuration.BILLIARD)){
            roomCard = new Card(Configuration.BILLIARD);
        }
        else if(name.equals(Configuration.HALL)){
            roomCard = new Card(Configuration.HALL);
        }
        else if(name.equals(Configuration.LOUNGE)){
            roomCard = new Card(Configuration.LOUNGE);
        }
        else if(name.equals(Configuration.CONSERVATORY)){
            roomCard = new Card(Configuration.CONSERVATORY);
        }
        else if(name.equals(Configuration.LIBRARY)){
            roomCard = new Card(Configuration.LIBRARY);
        }
        else if(name.equals(Configuration.BALL)){
            roomCard = new Card(Configuration.BALL);
        }
        else if(name.equals(Configuration.DINING)){
            roomCard = new Card(Configuration.DINING);
        }
	}
	
	
	public void insertPart(RectArea part) {
		parts.add(part);
	}
	
	public void insertDoor(Door door) {
		doors.add(door);		
	}
	
	
		
	public ArrayList getDoors() {
		return doors;
	}


	public void setDoors(ArrayList doors) {
		this.doors = doors;
	}


	public ArrayList getParts() {
		return parts;
	}


	public void setParts(ArrayList parts) {
		this.parts = parts;
	}


	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setDefaultColor(Color color) {
		this.defaultColor = color;
	}
	
	public Color getDefaultColor() {
		return this.defaultColor;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SecretPassage getPassage() {
		return passage;
	}
	public void setPassage(SecretPassage passage) {
		this.passage = passage;
	}
	
	public Color getWallsColor() {
		return wallsColor;		
	}
	
	public void setWallsColor(Color color) {
		this.wallsColor = color;
    }
    
    
	/**
     * @return Returns the roomTexture.
     */
    public String getRoomTexture() {
        return roomTexture;
    }


    /**
     * @param roomTexture The roomTexture to set.
     */
    public void setRoomTexture(String roomTexture) {
        this.roomTexture = roomTexture;
    }

   
    public Card getRoomCard(){
        return roomCard;
    }
    
    public ArrayList getPlayers() {
		return players;
	}


	public void setPlayers(ArrayList players) {
		this.players = players;
	}


	public ArrayList getWeapons() {
		return weapons;
	}


	public void setWeapons(ArrayList weapons) {
		this.weapons = weapons;
	}

	public boolean isInside(Cell cell) {
		boolean ret = false;
		Iterator i = parts.iterator();
		
		while(i.hasNext()) {
			RectArea rect = (RectArea)i.next();
			ret = ret || ((cell.getX() >= rect.getX()) && ((cell.getX()*Configuration.getCellWidth() + cell.getWidth()) <= (rect.getX()*Configuration.getCellWidth() + rect.getWidth()*Configuration.getCellWidth()))) &&
					      (cell.getY() >= rect.getY()) && ((cell.getY()*Configuration.getCellHeight() + cell.getHeight()) <= (rect.getY()*Configuration.getCellHeight() + rect.getHeight()*Configuration.getCellHeight()));
		}
		
		return ret;
	}
	
	
	public void drawRoom(Graphics g,boolean highlighted) {
		Iterator i = this.parts.iterator();
        
        TextureHandler th = TextureHandler.getInstance();
        Texture tx = th.getTexture(roomTexture);
		
		/*
		 * 1)disegno parti room
		 * 2)passaggio secreto
		 * 3)texture (text segreto)
		 * 4)muri
		 * 5)porte
         * 6)etichetta
		 */
		
		//disegna le parti della stanza
		while(i.hasNext()) {
            
			RectArea  rect = (RectArea)i.next();
      /*      if(tx != null) {
       
                for(int k=0; k< rect.getHeight()/Configuration.getCellHeight(); k++) {
                    for(int j=0; j<rect.getWidth()/Configuration.getCellWidth(); j++) {
                        g.drawImage(tx.getImage(),j*Configuration.getCellHeight(),k*Configuration.getCellWidth(),Configuration.getCellWidth(),Configuration.getCellHeight(),null);
                    }
                }
            }
            else {
            
      */    
          
            if(highlighted){
            
                g.setColor(new Color(this.getDefaultColor().getRed(),
                        this.getDefaultColor().getGreen(),
                        this.getDefaultColor().getBlue(),
                        200));
                
        //    }
            }else{
                g.setColor(this.color);
            }
                g.fillRect(rect.getX()*Configuration.getCellWidth(),rect.getY()*Configuration.getCellHeight(),
                    rect.getWidth()*Configuration.getCellWidth(),rect.getHeight()*Configuration.getCellHeight());
            
		}
		
		//disegna il passaggio segreto
		if(passage != null) {
			g.setColor(Color.BLACK);
			g.fillOval(passage.getX()*Configuration.getCellWidth(),passage.getY()*Configuration.getCellHeight(),
					   passage.getWidth(),passage.getHeight());
		}
		
		//disegna il perimetro della stanza
		drawRoomWalls(g);
		
		//posiziona le entrate
		try {
			drawRoomDoors(g);
		} catch(Exception e) {
			e.printStackTrace();			
		}
        
        
        // scrive il nome della stanza
		printRoomName(g);
		
		
	}
	
	
	
	
	private void printRoomName(Graphics g) {
		Iterator iter = parts.iterator();
		int larghezzaGlobale = 0;
		int altezzaGlobale = 0;
		int areaGlobale = 0;
		
		while(iter.hasNext()) {
			RectArea rect = (RectArea)iter.next();
			larghezzaGlobale += rect.getWidth()*Configuration.getCellWidth();
			areaGlobale += ((rect.getWidth()*Configuration.getCellWidth()) * (rect.getHeight()*Configuration.getCellHeight()));
		}
		altezzaGlobale = areaGlobale / larghezzaGlobale;
		
		
		RectArea rect = (RectArea)parts.get(0);
		if(name.equals(Configuration.CENTER)){
        	Font font = new Font("monospaced",Font.BOLD+Font.ITALIC,Configuration.getCellHeight());
            g.setFont(font);
        	
        	g.setColor(Color.RED);
        	
        	
        	g.drawString("C",(larghezzaGlobale/6)+rect.getX()*Configuration.getCellWidth(),altezzaGlobale/2 + rect.getY()*Configuration.getCellHeight());
            g.setColor(Color.WHITE);
            g.drawString("luedo",(larghezzaGlobale/6 + Configuration.getCellWidth()/2)+rect.getX()*Configuration.getCellWidth(),altezzaGlobale/2+rect.getY()*Configuration.getCellHeight());
        }
        else {
        	Font font = new Font("monospaced",Font.BOLD,Configuration.getCellHeight()/2);
            g.setFont(font);
        	
            g.setColor(Color.BLACK);
            g.drawString(name,(larghezzaGlobale/6)+rect.getX()*Configuration.getCellWidth(),altezzaGlobale/2 + rect.getY()*Configuration.getCellHeight());
        }
		
	}


	
	
	private void drawRoomWalls(Graphics g) {
		ListIterator i = parts.listIterator();
		ArrayList vertex = new ArrayList();
		
		((Graphics2D)g).setStroke(new BasicStroke(Configuration.getStroke()));
		
		
		//trova i vertici della stanza
		while(i.hasNext()) {
			RectArea p1 = (RectArea)i.next();
						
			vertex.add(new Point(p1.getX(),p1.getY()));
			vertex.add(new Point(p1.getX()+p1.getWidth(),p1.getY()));
		}
		
		do {
			RectArea p1 = (RectArea)i.previous();
			
			vertex.add(new Point(p1.getX()+p1.getWidth(),p1.getY()+p1.getHeight()));
			vertex.add(new Point(p1.getX(),p1.getY()+p1.getHeight()));	
		} while(i.hasPrevious());
		
		
		//disegna il perimetro
		Iterator k = vertex.iterator();
		Iterator h = vertex.iterator();
		
		Point q = (Point)h.next();
		while(h.hasNext()) {
			Point p = (Point)k.next();
			q = (Point)h.next();
			
			g.setColor(this.wallsColor);
			g.drawLine(p.x*Configuration.getCellWidth(),p.y*Configuration.getCellHeight(),
					   q.x*Configuration.getCellWidth(),q.y*Configuration.getCellHeight());
						
		}
		
		//disegna linea tra ultimo vertice e il primo
		g.drawLine(((Point)vertex.get(0)).x*Configuration.getCellWidth(),((Point)vertex.get(0)).y*Configuration.getCellHeight(),
					q.x*Configuration.getCellWidth(),q.y*Configuration.getCellHeight());
	}
	
	private void drawRoomDoors(Graphics g) throws Exception{
		Iterator i = doors.iterator();
		
		while(i.hasNext()) {
			Door door = (Door)i.next();
			
			g.setColor(this.defaultColor);
			switch(door.getSide()) {
			
				case Configuration.DOOR_SIDE_NORTH: {
					g.drawLine(door.getX()*Configuration.getCellWidth(),door.getY()*Configuration.getCellHeight(),
							   door.getX()*Configuration.getCellWidth()+door.getWidth(),door.getY()*Configuration.getCellHeight());
					break;
				}
				case Configuration.DOOR_SIDE_EAST: {
					g.drawLine(door.getX()*Configuration.getCellWidth()+door.getWidth(),door.getY()*Configuration.getCellHeight(),
							   door.getX()*Configuration.getCellWidth()+door.getWidth(),door.getY()*Configuration.getCellHeight()+door.getHeight());
					break;
				}
				case Configuration.DOOR_SIDE_SOUTH: {
					g.drawLine(door.getX()*Configuration.getCellWidth(),door.getY()*Configuration.getCellHeight()+door.getHeight(),
							   door.getX()*Configuration.getCellWidth()+door.getWidth(),door.getY()*Configuration.getCellHeight()+door.getHeight());
					break;
				}
				case Configuration.DOOR_SIDE_WEST: {
					g.drawLine(door.getX()*Configuration.getCellWidth(),door.getY()*Configuration.getCellHeight(),
							   door.getX()*Configuration.getCellWidth(),door.getY()*Configuration.getCellHeight()+door.getHeight());
					break;
				}
				default : throw new Exception("Parametro SIDE non valido.");
			}
			
		}
	}
	
    
	public void addPlayer(Player player) {
		this.players.add(player);
	}
	

	public void addWeapon(Weapon weapon) {
		this.weapons.add(weapon);
	}
	
	public String toString() {
		return name;
	}
	
	
}
