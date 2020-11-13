package gamedata;

import interfaces.Configuration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;


public class TextureHandler {

	private Hashtable textures;		//hashtable delle texture caricate in memoria
	private Properties properties;
	private String fileproperties;	//path del file di configurazione per le texture
	private static TextureHandler instance;
    private Toolkit toolkit;
	
	//costruttore che supporta design pattern singleton
	public static TextureHandler getInstance() {
		if(instance == null) {
			instance = new TextureHandler();
		}
		return instance;
	}
	
	//costruttore privato
	private TextureHandler() {
		this.textures = new Hashtable();
		this.properties = new Properties();
		this.fileproperties = Configuration.getTexturesPath() + "textures.ini";
        this.toolkit = Toolkit.getDefaultToolkit();
	}
	
	
	public Hashtable getTextures() {
		return textures;
	}

	public void setTextures(Hashtable textures) {
		this.textures = textures;
	
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	
	public String getFileproperties() {
		return fileproperties;
	}

	public void setFileproperties(String fileproperties) {
		this.fileproperties = fileproperties;
	}
	
	
	
	//carica in memoria le texture specificate nel file di configurazione
	public void initialize() throws IOException {
		File file = new File(fileproperties);
		FileInputStream inputfile = new FileInputStream(file);
		
		properties.load(inputfile);
		
		Enumeration keys = properties.propertyNames();
		while(keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String pathTexture = properties.getProperty(key);
			            		
			textures.put(key,new Texture(pathTexture));
        }
        inputfile.close();
	}
	
    
  
	
	//salva su disco le texture correnti
	public void finalize() throws IOException {
		File file = new File(fileproperties);
		FileOutputStream outfile = new FileOutputStream(file);
		
		properties.store(outfile,null);	
        outfile.close();
        
	}

	
	//inserisce una nuova texture
	public void addTexture(String pathTexture, String key) {
		properties.put(key,pathTexture);	
					
		textures.put(key,new Texture(pathTexture));
			
	}
	
	
	
	//ritorna una texture da disegnare
	public Texture getTexture(String key) {
		Texture ret = null;
		
		if(key != null) {
			ret = (Texture)textures.get(key);
		}
		
		return ret;
	}
	
		

}