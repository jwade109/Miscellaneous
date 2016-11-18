package langton;

public class Ant {
	
	private static int x; // between 0 and board size
	private static int y;
	private static int dir; // 0 up, 1 right, 2 down, 3 left
	
	
	// constructs new ant object and gives it coordinates and a direction
	public Ant(int x, int y, int dir) {	
		Ant.x = x;
		Ant.y = y;
		Ant.dir = dir;
	}
	
	// increments ant's coordinates based on current direction
	public void move() {
		switch (dir) {
		case 0: y--;
		break;
		case 1: x++;
		break;
		case 2: y++;
		break;
		case 3: x--;
		break;
		}
	}
	
	public void reverse() {
		switch(dir) {
		case 0: y++;
		break;
		case 1: x--;
		break;
		case 2: y--;
		break;
		case 3: x++;
		}
	}
	
	// turn(false) turns right, while turn (true) will turn left
	public void turn(boolean a) {
		if(!a) {
			// turn left
			if(dir == 0) {
				dir = 3;
			}
			else dir--;
		}
		
		else {
			// turn right
			if(dir == 3) {
				dir = 0;
			}
			else dir++;
		}
				
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		Ant.x = x;
	}
	
	public void setY(int y) {
		Ant.y = y;
	}
	
	public int getDir() {
		return dir;
	}
	
	public void setDir(int dir) {
		Ant.dir = dir;
	}
	
	public String toString() {
		return x + " " + y + " " + dir;
	}
	
}
