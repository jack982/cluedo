package gamedata;

import java.io.Serializable;

import interfaces.Configuration;

public class Cell implements Serializable {

	private int x;
	private int y;
	private int height;
	private int width;
	
	
	public Cell() {
		this.height = Configuration.getCellHeight();
		this.width = Configuration.getCellWidth();
	}
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		this.height = Configuration.getCellHeight();
		this.width = Configuration.getCellWidth();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	
    
    public String toString(){
        String s = "CELL: X: "+getX()+" Y:"+getY();
        return s;
    }
		
}
