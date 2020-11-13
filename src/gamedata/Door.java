package gamedata;

public class Door extends Cell{

	
	private int side;

	
	public Door(int x, int y, int side) {
		super(x, y);
		this.side = side;
	}
	
	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}
}