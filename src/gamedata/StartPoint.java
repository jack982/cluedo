/*
 * StartPoint.java
 * Created on 2-feb-2006
 *
 * author: Jacopo Penazzi
 */
package gamedata;

import interfaces.Configuration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class StartPoint extends Cell {

    private Color defaultColor;
    
    public StartPoint(int x, int y, Color color){
        super(x,y);
        this.defaultColor = color;
    }
    
    
    public void drawStart(Graphics g){
        int[] xs = new int[4];
        int[] ys = new int[4];

        int l = Configuration.getCellWidth();
        int h = Configuration.getCellHeight();
        
        //disegna dei rombi
        xs[0] = getX()*l + getWidth()/2;
        xs[1] = getX()*l + getWidth();
        xs[2] = getX()*l + getWidth()/2;
        xs[3] = getX()*l;
        
        ys[0] = getY()*h;
        ys[1] = getY()*h + getHeight()/2;
        ys[2] = getY()*h + getHeight();
        ys[3] = getY()*h + getHeight()/2;
        
    	g.setColor(defaultColor);
        g.fillPolygon(xs,ys,4);
        
    }
    
    
}
