package mines;

import java.util.Random;

public class Mines {

	private boolean showAll = false;
	private Place[][] board;
	private int height;
	private int width;
	private int numMines;

	public Place[][] getBoard() {
		return board;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getNumMines() {
		return numMines;
	}

	// Inner class represent a place on the board
	private class Place {

		private boolean open = false;
		private boolean mine = false;
		private boolean flag = false;
		private int x;
		private int y;

		// Contractor
		public Place(int x, int y) {
			this.x = x;
			this.y = y;
		}

		// Check if there is a mine in the nearby places
		public int nearbyMines() {
			int count = 0;
			if (onBoard(x - 1, y + 1) && board[x - 1][y + 1].mine)
				count++;
			if (onBoard(x, y + 1) && board[x][y + 1].mine)
				count++;
			if (onBoard(x + 1, y + 1) && board[x + 1][y + 1].mine)
				count++;
			if (onBoard(x + 1, y) && board[x + 1][y].mine)
				count++;
			if (onBoard(x - 1, y) && board[x - 1][y].mine)
				count++;
			if (onBoard(x - 1, y - 1) && board[x - 1][y - 1].mine)
				count++;
			if (onBoard(x, y - 1) && board[x][y - 1].mine)
				count++;
			if (onBoard(x + 1, y - 1) && board[x + 1][y - 1].mine)
				count++;

			return count;
		}

		// Return the string representation of a place when it's open
		public String showOpenPlacetoSting() {
			if (mine) {
				return "X";
			}
			int closeMines = nearbyMines();
			if (closeMines == 0) {
				return " ";
			}
			return String.format("%d", closeMines);
		}

		// Return the string representation of a place
		@Override
		public String toString() {
			if (open) {
				return showOpenPlacetoSting();
			}
			if (flag) {
				return "F";
			}
			return ".";
		}
	}

	// Contractor
	public Mines(int height, int width, int numMines) {

		this.height = height;
		this.width = width;
		this.numMines = numMines;
		this.board = new Place[height][width];

		// Create the board
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				this.board[i][j] = new Place(i, j);
			}
		}

		// randomize the mines on the board
		Random rand = new Random();
		int count = 0;
		int x;
		int y;
		while (count < numMines) {
			x = rand.nextInt(height);
			y = rand.nextInt(width);
			if (!this.board[x][y].mine) {
				this.board[x][y].mine = true;
				count++;
			}
		}

	}

	// Add a mine in a place
	public boolean addMine(int row, int col) {
		if (!onBoard(row, col)) {
			return false;
		}
		if (!this.board[row][col].mine) {
			this.board[row][col].mine = true;
			return true;
		}
		return false;
	}

	public boolean open(int row, int col) {
		// Check if the place is on the board
		if (!onBoard(row, col)) {
			return false;
		}
		// Check if the place is already open or has a mine
		if (board[row][col].mine) {
			return false;
		}
		if (board[row][col].open) {
			return true;
		}
		// Open the place
		board[row][col].open = true;

		// If there are no nearby mines, open the adjacent cells recursively
		if (board[row][col].nearbyMines() == 0) {
			// Open all adjacent cells only if they are within bounds and not yet open
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (i != 0 || j != 0) { // Skip the current cell
						open(row + i, col + j);
					}
				}
			}
		}
		return true;
	}

	// Toggle the flag on a place
	public void toggleFlag(int row, int col) {
		this.board[row][col].flag = !this.board[row][col].flag;
	}

	// Check if the game is done
	public boolean isDone() {
		for (Place[] row : this.board) {
			for (Place place : row) {
				if (!place.open && !place.mine) {
					return false;
				}
			}
		}
		return true;
	}

	// Get the string representation of a place
	public String get(int row, int col) {
		// If showAll is true return the string representation of the place like it's
		// open
		if (this.showAll) {
			return this.board[row][col].showOpenPlacetoSting();
		}
		return board[row][col].toString();
	}

	// Set the showAll attribute
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	// Return the string representation of the board
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				sb.append(get(i, j));
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	// Check if the place is on the board
	public boolean onBoard(int row, int col) {
		if (row < 0 || col < 0 || row > height - 1 || col > width - 1) {
			return false;
		}
		return true;
	}

}
