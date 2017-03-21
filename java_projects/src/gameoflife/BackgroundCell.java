public class BackgroundCell {
	
	int x;
	int y;
	int color; // scale from 0 to 5, 0 being white and 5 being original grey
	
	public BackgroundCell(int x, int y) {
		this.x = x;
		this.y = y;
		color = 0;
	}
	
	public BackgroundCell(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public void setColor(int c) {
		color = c;
	}
	
	public int getState() {
		return color;
	}
	
	public void age() {
		color--;
	}
	
	public String toString() {
		return x + ", " + y + ", " + color;
	}
	
	public boolean isPlayer() {
		return false;
	}

}
