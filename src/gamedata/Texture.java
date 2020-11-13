package gamedata;

import interfaces.Configuration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;

public class Texture {

	private BufferedImage image;
	private Toolkit toolkit;
	
	public Texture(String path) {
		image = this.toBufferedImage(path);
    }
	
	
	
	public Image getImage() {
		return this.image;
	}
	
	
	private BufferedImage toBufferedImage(String path){
	     /*   
	        toolkit = Toolkit.getDefaultToolkit();
	        Image img = toolkit.getImage(path);
	     
	        BufferedImage bi = new BufferedImage(Constants.CELL_WIDTH,Constants.CELL_HEIGHT,BufferedImage.TYPE_INT_RGB);
	        Graphics big = bi.getGraphics();
	        big.drawImage(bi,0,0,Constants.CELL_WIDTH,Constants.CELL_HEIGHT,null);
	        
	        return bi;
	     */
	        
	        Image image = new ImageIcon(path).getImage();
	        
	        BufferedImage bimage = null;
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        try {
	            // Determine the type of transparency of the new buffered image
	            int transparency = Transparency.OPAQUE;
	             
	            // Create the buffered image
	            GraphicsDevice gs = ge.getDefaultScreenDevice();
	            GraphicsConfiguration gc = gs.getDefaultConfiguration();
	            bimage = gc.createCompatibleImage(
	                image.getWidth(null), image.getHeight(null), transparency);
	        } catch (HeadlessException e) {
	            // The system does not have a screen
	        }
	    
	        if (bimage == null) {
	            // Create a buffered image using the default color model
	            int type = BufferedImage.TYPE_INT_RGB;
	        
	            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	        }
	    
	        // Copy image to buffered image
	        Graphics g = bimage.createGraphics();
	    
	        // Paint the image onto the buffered image
	        g.drawImage(image, 0, 0, null);
	        g.dispose();
	    
	        
	        return bimage;
	    }

	
}
