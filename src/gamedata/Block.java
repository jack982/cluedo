package gamedata;

import interfaces.Configuration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

public class Block extends Cell {

	private Color defaultColor;
	private String blockTexture;
	
	public Block(int x, int y) {
		super(x,y);
		this.defaultColor = (new Color(0,255,0)).darker();
	}
	
	public Block(int x, int y, Color color) {
		super(x,y);
		this.defaultColor = color;
	}

	public String getBlockTexture() {
		return blockTexture;
	}

	public void setBlockTexture(String blockTexture) {
		this.blockTexture = blockTexture;
	}

	public Color getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}
	
	public void drawBlock(Graphics g) {
		TextureHandler th = TextureHandler.getInstance();		
		Texture tx = th.getTexture(blockTexture);
		
		if(tx != null) {
			g.drawImage(tx.getImage(),Configuration.getCellHeight(),Configuration.getCellWidth(),Color.BLACK,null);
			
		}else {		
			g.setColor(defaultColor);
			g.fillRect(getX()*Configuration.getCellWidth(),getY()*Configuration.getCellHeight(),getWidth(),getHeight());
		}
	}
	
	
}
