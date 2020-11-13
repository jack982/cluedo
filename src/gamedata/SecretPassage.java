package gamedata;

public class SecretPassage extends Cell {

	
	private Room link;

	
	public SecretPassage(int x, int y, Room link) {
		super(x, y);
		this.link = link;
	}
	
	public Room getLink() {
		return link;
	}

	public void setLink(Room link) {
		this.link = link;
	}
}
