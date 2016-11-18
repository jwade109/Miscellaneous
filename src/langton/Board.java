package langton;

public class Board {
	
	private static int boardSize;
	private static boolean[][] board;
	private static int[][] count;
	
	
	// generates new board of specified size and populates it with falsehoods
	public Board(int size) {
		
		boardSize = size;
		
		board = new boolean[boardSize][boardSize];
		count = new int[boardSize][boardSize];
		
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				setCellState(true, i, j);
				count[i][j] = 0;
			}
		}
		
	}
	
	// returns the value in a given cell
	public boolean getCellState(int x, int y) {
		return board[x][y];
	}
	
	public int getCellCount(int x, int y) {
		return count[x][y];
	}
	
	// a method which sets the cells to any integer value
	public void setCellState(boolean val, int x, int y) {
		board[x][y] = val;
	}
	
	public int invertCell(int x, int y, int z) {
		board[x][y] = !board[x][y];
		count[x][y] += z;
		return count[x][y];
	}
	
	
	// prints the board onto the console in a visually unappealing way.
	public String toString() {
		
		String string = "";
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				
				if(!board[i][j]) string += " ";
				else string += "O";
				}
				string += " ";
			}
			string += "\n";
		
		return string;
	}
	
	public String toCountString() {
		
		String string = "";
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(board[i][j]) string += "+";
				else string += "-";
				string += count[i][j]+"\t";
			}
			string += "\n";
		}
		
		return string;
	}
	
	public int getLargestCount() {
		
		int largest = 0;
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j++) {
				if(count[i][j] > largest) largest = count[i][j];
			}
		}
		return largest;
	}
	
}
