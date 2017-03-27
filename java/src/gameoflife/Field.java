public class Field {
	
	private static boolean[][] field;
	private static boolean[][] temp;
	private static BackgroundCell[][] background;
	private static int board;
	private static int tick;
	private static int ticksPassed; // amount of times update has been called
	
	public Field(int board, int tick) {
		
		field = new boolean[board][board];
		temp = new boolean[board][board];
		setBackground(new BackgroundCell[board][board]);
		for(int i = 0; i < board; i++) {
			for(int j = 0; j < board; j++) {
				getBackground()[i][j] = new BackgroundCell(i, j, 0);
			}
		}
		Field.board = board;
		Field.tick = tick;
		
	}
	
	public static boolean getCellState(int x, int y) {
		return field[x][y];
	}
	
	public static int getBackgroundCellState(int x, int y) {
		return getBackground()[x][y].getState();
	}
	
	public static void setCellState(int x, int y, boolean b) {
		field[x][y] = b;
	}
	
	public static void setBackgroundCellState(int x, int y, int c) {
		getBackground()[x][y].setColor(c);
	}
	
	public static void invertCellState(int x, int y) {
		field[x][y] = !field[x][y];
		getBackground()[x][y].setColor(5);
	}
	
	public static void updateBackground(boolean tracers) {
		for(int i = 0; i < board; i++) {
			for(int j = 0; j < board; j++) {
				if(field[i][j] && tracers) getBackground()[i][j].setColor(25);
				else getBackground()[i][j].age();
			}
		}
	}
	
	public static int getNeighborCount(int x, int y) {
		int count = 0;
		if(x == 0 && y == 0) {
			if(getCellState(1,0)) count++;
			if(getCellState(1,1)) count++;
			if(getCellState(0,1)) count++;
			
		}
		
		else if(x == 0 && y == board-1) {
			if(getCellState(0,board-2)) count++;
			if(getCellState(1,board-2)) count++;
			if(getCellState(1,board-1)) count++;
			
		}
		
		else if(x == board-1 && y == board-1) {
			if(getCellState(board-2, board-1)) count++;
			if(getCellState(board-2, board-2)) count++;
			if(getCellState(board-1, board-2)) count++;
			
		}
		
		else if(x == board-1 && y == 0) {
			if(getCellState(board-2, 0)) count++;
			if(getCellState(board-1, 1)) count++;
			if(getCellState(board-2, 1)) count++;
			
		}
		
		else if(x == 0 && !(y == board-1) && !(y == 0)) {
			if(getCellState(0, y-1)) count++;
			if(getCellState(0, y+1)) count++;
			if(getCellState(1, y-1)) count++;
			if(getCellState(1, y)) count++;
			if(getCellState(1, y+1)) count++;
			
		}
		
		else if(y == 0 && !(x == board-1) && !(x == 0)) {
			if(getCellState(x-1, 0)) count++;
			if(getCellState(x+1, 0)) count++;
			if(getCellState(x-1, 1)) count++;
			if(getCellState(x, 1)) count++;
			if(getCellState(x+1, 1)) count++;
			
		}
		
		else if(x == board-1 && !(y == board-1) && !(y == 0)) {
			if(getCellState(board-1, y-1)) count++;
			if(getCellState(board-1, y+1)) count++;
			if(getCellState(board-2, y-1)) count++;
			if(getCellState(board-2, y)) count++;
			if(getCellState(board-2, y+1)) count++;
			
		}
		
		else if(y == board-1 && !(x == board-1) && !(x == 0)) {
			if(getCellState(x-1, board-1)) count++;
			if(getCellState(x+1, board-1)) count++;
			if(getCellState(x-1, board-2)) count++;
			if(getCellState(x, board-2)) count++;
			if(getCellState(x+1, board-2)) count++;
			
		}
		
		
		
		else {
			if(getCellState(x-1, y-1)) count++;
			if(getCellState(x-1, y)) count++;
			if(getCellState(x-1, y+1)) count++;
			if(getCellState(x, y-1)) count++;
			if(getCellState(x, y+1)) count++;
			if(getCellState(x+1, y-1)) count++;
			if(getCellState(x+1, y)) count++;
			if(getCellState(x+1, y+1)) count++;
		}
		
		return count;
		
	}

	public static void update(boolean tracers) {
		
		updateBackground(tracers);
		addTime();
		
		for(int i = 0; i < board; i++) {
			for(int j = 0; j < board; j++) {
				temp[i][j] = getCellState(i, j);
			}
		}
		
		for(int i = 0; i < board; i++) {
			for(int j = 0; j < board; j++) {
				if(getCellState(i, j)) {
					if(getNeighborCount(i, j) < 2) temp[i][j] = false;	// live cell dies due to underpopulation
					else if(getNeighborCount(i, j) > 3) temp[i][j] = false; // or dies due to overpopulation
				}
				if(!getCellState(i, j)) {
					if(getNeighborCount(i, j) == 3) temp[i][j] = true; // dead cell lives due to reproduction
				}
			}
		}
		
		for(int i = 0; i < board; i++) {
			for(int j = 0; j < board; j++) {
				setCellState(i, j, temp[i][j]);
			}
		}
	}

	public static void populate(int sparsity) {
		boolean trueFalse;
		for(int i = 0; i < board; i++) {
			for(int j = 0; j < board; j++) {
				trueFalse = toBoolean((int)(Math.random()*sparsity));
				setCellState(i, j, trueFalse);
			}
		}
		
	}
	
	public static void spiceItUp(int gliderRate) {
		if((int)(Math.random()*gliderRate) == 1) {
			
			int dir = (int)(Math.random()*4);
			
			int centX = (int)(Math.random()*(board-10) + 5);
			int centY = (int)(Math.random()*(board-10) + 5);
			
			if(dir == 0) {
				// down right glider
				field[centX][centY-1] = true;
				field[centX+1][centY] = true;
				field[centX+1][centY+1] = true;
				field[centX][centY+1] = true;
				field[centX-1][centY+1] = true;
			}
			
			else if(dir == 1) {
				// down left glider
				field[centX][centY-1] = true;
				field[centX-1][centY] = true;
				field[centX-1][centY+1] = true;
				field[centX][centY+1] = true;
				field[centX+1][centY+1] = true;
			}

			else if(dir == 2) {
				// up left glider
				field[centX][centY+1] = true;
				field[centX-1][centY] = true;
				field[centX-1][centY-1] = true;
				field[centX][centY-1] = true;
				field[centX+1][centY-1] = true;
				}
			
			else if(dir == 3) {
				// up right glider
				field[centX][centY+1] = true;
				field[centX+1][centY] = true;
				field[centX+1][centY-1] = true;
				field[centX][centY-1] = true;
				field[centX-1][centY-1] = true;
			}
			
		}
		
	}
	
	private static boolean toBoolean(int k) {
		if(k == 1) return true;
		return false;
	}

	public int getTime() {
		return ticksPassed;
	}
	
	public double getTimeInSeconds() {
		return ((double)ticksPassed)/(1000/((double)tick));
	}
	
	public static void addTime() {
		ticksPassed++;
	}
	
	public int getPopulation() {
		int pop = 0;
		for(int i = 0; i < board; i++) {
			for(int j = 0; j < board; j++) {
				if(getCellState(i, j)) pop++;
			}
		}
		return pop;
	}

	public static BackgroundCell[][] getBackground() {
		return background;
	}

	public static void setBackground(BackgroundCell[][] background) {
		Field.background = background;
	}
	
	public static BackgroundCell getBackgroundCellAt(int x, int y) {
		return background[x][y];
	}

	
	

}
