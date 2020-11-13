package interfaces;

import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {

	//costanti
	public static final int CELL_HEIGHT_SMALL = 20;
	public static final int CELL_WIDTH_SMALL = 20;
	public static final int CELL_HEIGHT_MEDIUM = 28;
	public static final int CELL_WIDTH_MEDIUM = 28;
	public static final int CELL_HEIGHT_LARGE = 80;
	public static final int CELL_WIDTH_LARGE = 80;
	public static final int STROKE_SMALL = 2;
	public static final int STROKE_MEDIUM = 3;
	public static final int STROKE_LARGE = 8;
	public static final int MAX_PLAYERS = 6;
    public static final int MIN_PLAYERS = 4;
	public static final int MAX_DICE_FACES = 6;
	public static final int DOOR_SIDE_NORTH = 1;
	public static final int DOOR_SIDE_EAST = 2;
	public static final int DOOR_SIDE_SOUTH = 3;
	public static final int DOOR_SIDE_WEST = 4;
	public static final int DEFAULT_BOARD_NUMCELL_HEIGHT = 25;
	public static final int DEFAULT_BOARD_NUMCELL_WIDTH = 24;
	public static final String CLIENT_SERVICE_NAME = "Controller";
	
	//variabili
	private static int cell_height;
	private static int cell_width;
	private static int dice_faces;
	private static int stroke;
    
    private static String userdir = System.getProperty("user.dir")+File.separator+"data"; 
	private static String texturesPath = userdir + File.separator + "textures" +File.separator;
    private static String soundsPath = userdir + File.separator + "sounds" + File.separator;
    private static String imagesPath = userdir + File.separator + "images" + File.separator;
    private static String fileconfig = userdir + File.separator + "config" + File.separator + "config.ini";
	private static Properties config;	
	
    
    
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final int PLAYER_3 = 3;
    public static final int PLAYER_4 = 4;
    public static final int PLAYER_5 = 5;
    public static final int PLAYER_6 = 6;
    
    public static final String CHARACTER_NAME_1 = "Miss Scarlett";
    public static final String CHARACTER_NAME_2 = "Rev. Green";
    public static final String CHARACTER_NAME_3 = "Prof. Plum";
    public static final String CHARACTER_NAME_4 = "Mrs. Peacock";
    public static final String CHARACTER_NAME_5 = "Col. Mustard";
    public static final String CHARACTER_NAME_6 = "Mrs. White";
    
    public static final String GUN = "Gun";
    public static final String DAGGER = "Dagger";
    public static final String CANDLESTICK = "Candlestick";
    public static final String ROPE = "Rope";
    public static final String LEADPIPE = "Leadpipe";
    public static final String SPANNER = "Spanner";
    
    
    public static final String KITCHEN = "Kitchen";
    public static final String STUDY = "Study";
    public static final String CONSERVATORY = "Conservatory";
    public static final String LOUNGE = "Lounge";
    public static final String HALL = "Hall";
    public static final String DINING = "Dining Room";
    public static final String LIBRARY = "Library";
    public static final String BALL = "Ball Room";
    public static final String BILLIARD = "Billiard Room";
    public static final String CENTER = "Center";
    
    
    // tipi di carte
    public static final int CARD_TYPE_ROOM = 0;
    public static final int CARD_TYPE_WEAPON = 1;
    public static final int CARD_TYPE_CHARACTER = 2;
    
    // immagini delle carte dei personaggi
    public static final String RED_PLAYER_CARD_PATH = imagesPath + "red.gif";
    public static final String GREEN_PLAYER_CARD_PATH = imagesPath + "green.gif";
    public static final String WHITE_PLAYER_CARD_PATH = imagesPath + "white.gif";
    public static final String MAGENTA_PLAYER_CARD_PATH = imagesPath + "magenta.gif";
    public static final String YELLOW_PLAYER_CARD_PATH = imagesPath + "yellow.gif";
    public static final String BLUE_PLAYER_CARD_PATH = imagesPath + "blue.gif";
      
    //  immagini delle carte delle armi
    public static final String GUN_CARD_PATH = imagesPath + "gun.gif";
    public static final String DAGGER_CARD_PATH = imagesPath + "dagger.gif";
    public static final String ROPE_CARD_PATH = imagesPath + "rope.gif";
    public static final String CANDLESTICK_CARD_PATH= imagesPath + "candlestick.gif";
    public static final String LEADPIPE_CARD_PATH = imagesPath + "leadpipe.gif";
    public static final String SPANNER_CARD_PATH = imagesPath + "spanner.gif";
    
    
    // immagini delle carte delle stanze
    public static final String KITCHEN_ROOM_CARD_PATH = imagesPath + "kitchen.gif";
    public static final String HALL_ROOM_CARD_PATH = imagesPath + "hall.gif";
    public static final String BILLIARD_ROOM_CARD_PATH = imagesPath + "billiard.gif";
    public static final String DINING_ROOM_CARD_PATH = imagesPath + "dining.gif";
    public static final String LIBRARY_ROOM_CARD_PATH = imagesPath + "library.gif";
    public static final String STUDY_ROOM_CARD_PATH = imagesPath + "study.gif";
    public static final String CONSERVATORY_ROOM_CARD_PATH = imagesPath + "conservatory.gif";
    public static final String BALL_ROOM_CARD_PATH = imagesPath + "ball.gif";
    public static final String LOUNGE_ROOM_CARD_PATH = imagesPath + "lounge.gif";
    
    
   //chiavi del file di configurazione
	private static String key_cell_height = "cell_height";
	private static String key_cell_width = "cell_width";
    private static String key_dice_faces = "dice_faces";
	private static String key_stroke = "stroke";
	
	//carica la configurazione da file o ne crea una di default
	public static void loadConfiguration() throws IOException {
		File file = new File(fileconfig);
		config = new Properties();
		
		//se non esiste il file di configurazione ne crea uno di default
		if(!file.exists()) {
			System.out.println("Creazione prima configurazione");
			file = createDefaultConfiguration();
		}
		
		InputStream in = new FileInputStream(file);
		config.load(in);
		
		/* carica le impostazioni dal file di configurazione */
		Integer i = new Integer(config.getProperty(key_cell_height));
		setCellHeight(i.intValue());
		
		i = new Integer(config.getProperty(key_cell_width));
		setCellWidth(i.intValue());
        
        i = new Integer(config.getProperty(key_dice_faces));
        setDiceFaces(i.intValue());
        
        i = new Integer(config.getProperty(key_stroke));
        setStroke(i.intValue());
		/* ************************************************* */
		
		in.close();
	}
	
	
	//crea un file di configurazione di default
	private static File createDefaultConfiguration() throws IOException {
		File file = new File(fileconfig);
		OutputStream out = new FileOutputStream(file);
		
		/* salvataggio impostazioni di default */
		config.put(key_cell_height,new Integer(CELL_HEIGHT_SMALL).toString());
		config.put(key_cell_width,new Integer(CELL_WIDTH_SMALL).toString());
		config.put(key_dice_faces,new Integer(MAX_DICE_FACES).toString());
		config.put(key_stroke,new Integer(STROKE_SMALL).toString());
        /* *********************************** */
		
		config.store(out,null);
		
		out.close();
		
		return file;
	}
	
	
	//salva la configurazione corrente in un file
	public static void saveConfiguration() throws IOException {
		File file = new File(fileconfig);
		OutputStream out = new FileOutputStream(file);
		config.store(out,null);
		
	}
	
	public static int getCellHeight(){
		return cell_height;
	}
	
	public static int getCellWidth() {
		return cell_width;
	}
	
	public static void setCellHeight(int val) {
		cell_height = val;
	}
	
	public static void setCellWidth(int val) {
		cell_width = val;
	}

	public static int getDiceFaces() {
		return dice_faces;
	}

	public static void setDiceFaces(int num) {
		dice_faces = num;
	}

	public static String getFileconfig() {
		return fileconfig;
	}

	public static void setFileconfig(String fileconfig) {
		Configuration.fileconfig = fileconfig;
	}

	public static int getStroke() {
		return stroke;
	}
	
	public static void setStroke(int stroke) {
		Configuration.stroke = stroke;
	}
	
    /**
     * @return Returns the userdir.
     */
    public static String getUserdir() {
        return userdir;
    }


    /**
     * @return Returns the soundsdir.
     */
    public static String getSoundsPath() {
        return soundsPath;
    }


    /**
     * @return Returns the texturesdir.
     */
    public static String getTexturesPath() {
        return texturesPath;
    }
	
    /**
     * @return Returns the imagesDir
     */
    public static String getImagesPath(){
        return imagesPath;
    }

}
