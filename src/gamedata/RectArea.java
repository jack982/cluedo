package gamedata;

import java.io.Serializable;

import interfaces.Configuration;

import com.sun.corba.se.impl.orbutil.closure.Constant;

public class RectArea implements Serializable {

	private int x;
	private int y;
	private int height;
	private int width;
	
	
	
	public RectArea(int x, int y, int num_cell_height, int num_cell_width) {
		this.x = x;
		this.y = y;
		this.height = num_cell_height;
		this.width = num_cell_width;
	}



	public int getHeight() {
		return height;
	}



	public void setHeight(int height) {
		this.height = height;
	}



	public int getWidth() {
		return width;
	}



	public void setWidth(int width) {
		this.width = width;
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
	
	
}
