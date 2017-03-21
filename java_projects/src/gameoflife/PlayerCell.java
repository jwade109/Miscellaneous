public class PlayerCell extends BackgroundCell {
	
	int x;
	int y;

	public PlayerCell(int x, int y) {
		super(x, y);
		this.x = x;
		this.y = y;
	}
	
	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isPlayer() {
		return true;
	}
	
}
