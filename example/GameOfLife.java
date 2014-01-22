/**
 * GameOfLife.java - A class providing the rule set and dynamics of simulating
 * John Conway's Game Of Life that allows for rewinding.
 * 
 * @see MultiBufferBoard
 * 
 * @author William
 * 
 */
public class GameOfLife {
	/**
	 * Test three generations of a glider.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int b = 1 << BUFFERS;
		GameOfLife game = new GameOfLife(new int[][] { { 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 }, { 0, 0, b, 0, 0, 0 },
				{ 0, 0, 0, b, 0, 0 }, { 0, b, b, b, 0, 0 },
				{ 0, 0, 0, 0, 0, 0 } });
		System.out.println(game);
		game.nextGeneration();
		System.out.println(game);
		game.nextGeneration();
		System.out.println(game);
	}

	private MultiBufferBoard board;

	/*
	 * deadCellSpawns defines how many neighbors it takes for dead cells to
	 * spawn. 00100000 written downwards means
	 * 
	 * 0: A dead cell with 1 neighbor will remain dead
	 * 
	 * 0: A dead cell with 2 neighbors will remain dead.
	 * 
	 * 1: A dead cell with 3 neighbors will BECOME LIVING.
	 * 
	 * 0: A dead cell with 4 neighbors will remain dead.
	 * 
	 * 0: A dead cell with 5 neighbors will remain dead. etc...
	 */
	private byte deadCellSpawns = 0b00100000;

	/*
	 * aliveCellLives defines how many neighbors it takes for a living cell stay
	 * alive. 01100000 written downwards means
	 * 
	 * 0: A living cell with 1 neighbor will die.
	 * 
	 * 1: A living cell with 2 neighbors will STAY ALIVE.
	 * 
	 * 1: A living cell with 3 neighbors will STAY ALIVE.
	 * 
	 * 0: A living cell with 4 neighbors will die.
	 * 
	 * 0: A living cell with 5 neighbors will die. etc...
	 */
	private byte aliveCellLives = 0b01100000;
	/*
	 * Define neighbor offsets to aid in neighbor counting.
	 */
	private static final byte[][] neighbors = { { 1, 1 }, { 0, 1 }, { -1, 1 },
			{ 1, 0 }, { -1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 } };
	// The number of buffers to keep in the MultiBufferBoard
	public static final byte BUFFERS = 30;

	/**
	 * Creates a new GameOfLife from an existing 2D integer array
	 * 
	 * @param board
	 *            An existing 2D integer array
	 * @param BUFFERS
	 *            The number of buffers to consider
	 */
	public GameOfLife(int[][] board) {
		this.board = new MultiBufferBoard(board);
	}

	/**
	 * Creates a new GameOfLife with the specified width and height
	 * 
	 * @param width
	 *            The specified width
	 * @param height
	 *            The specified height
	 * @param BUFFERS
	 *            The number of buffers to consider
	 */
	public GameOfLife(int width, int height) {
		this.board = new MultiBufferBoard(width, height);
	}

	/**
	 * Returns a grid of # symbols that represent the current board at the
	 * current buffer.
	 */
	public String toString() {
		return this.toString(BUFFERS);
	}

	/**
	 * Returns a grid of # symbols that represent the current board at the given
	 * buffer.
	 * 
	 * @param buffer
	 *            the buffer of the array to print
	 */
	public String toString(int buffer) {
		String string = "";
		for (int row = 0; row < this.getHeight(); row++) {
			for (int col = 0; col < this.getWidth(); col++) {
				string += this.getCell(row, col, buffer) == 0 ? " " : "#";
			}
			string += "\n";
		}
		return string;
	}

	/**
	 * Randomizes the current board at the given buffer with a given fill
	 * factor.
	 * 
	 * @param fillFactor
	 *            a fill factor of 0 yields a completely dead board, and a fill
	 *            factor of 1 yields a completely live board.
	 */
	public void randomize(double fillFactor) {
		this.clearBoard();
		for (int row = 0; row < this.getHeight(); row++) {
			for (int col = 0; col < this.getWidth(); col++) {
				this.setCurrentCell(row, col, Math.random() >= fillFactor ? 0
						: 1);
			}
		}
	}

	/**
	 * Sets the cell in the current buffer to a specified value.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @param value
	 *            The given value
	 */
	public void setCurrentCell(int row, int col, int value) {
		this.setCell(row, col, BUFFERS, value);
	}

	/**
	 * Sets the cell in the given buffer to a specified value.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @param buffer
	 *            The given buffer
	 * @param value
	 *            The given value
	 */
	public void setCell(int row, int col, int buffer, int value) {
		board.setAt(row, col, buffer, value);
	}

	/**
	 * Gets the cell in the previous buffer.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @return The value of the cell
	 */
	public int getPreviousCell(int row, int col) {
		return this.getCell(row, col, BUFFERS - 1);
	}

	/**
	 * Gets the cell in the current buffer.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @return The value of the cell
	 */
	public int getCurrentCell(int row, int col) {
		return this.getCell(row, col, BUFFERS);
	}

	/**
	 * Gets the cell in the given buffer, wrapping if neccessary.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @return The value of the cell
	 */
	public int getCell(int row, int col, int buffer) {
		return board.getAt(wrap(row, this.board.getHeight()),
				wrap(col, this.board.getWidth()), buffer);
	}

	/**
	 * Gets the number of occupied neighbors in the previous buffer.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @return The number of occupied neighbors
	 */
	private byte getOccupiedNeighbors(int row, int col) {
		byte occupiedNeighbors = 0;
		for (int i = 0; i < neighbors.length; i++) {
			// Use the neighbor array to determine offsets
			if (this.getPreviousCell(row + neighbors[i][0], col
					+ neighbors[i][1]) != 0) {
				occupiedNeighbors++;
			}
		}
		return occupiedNeighbors;
	}

	/**
	 * Determines whether a cell is alive or dead the next generation given its
	 * information
	 * 
	 * @param neighbors
	 *            the number of neighbors a cell has
	 * @param alreadyAlive
	 *            whether or not the cell is alive (1 or 0)
	 * @return whether a cell is alive or dead
	 */
	private int rules(int neighbors, int alreadyAlive) {
		if (alreadyAlive == 0) {
			return (deadCellSpawns & (256 >> neighbors)) == 0 ? 0 : 1;
		} else {
			return (aliveCellLives & (256 >> neighbors)) == 0 ? 0 : 1;
		}
	}

	/**
	 * Computes the next generation for the board.
	 * 
	 */
	public void nextGeneration() {
		// Make way for the new generation
		this.board.bitShiftRight(1);
		// Calculate the next value for each cell
		for (int row = 0; row < this.getHeight(); row++) {
			for (int col = 0; col < this.getWidth(); col++) {
				nextCell(row, col);
			}
		}
	}

	/**
	 * Computes the next generation for a specified cell.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 */
	public void nextCell(int row, int col) {
		// Set it according to the rules
		this.setCurrentCell(
				row,
				col,
				rules(this.getOccupiedNeighbors(row, col),
						this.getPreviousCell(row, col)));
	}

	/**
	 * Wraps an integer a over a length b. In other words, computes a % b, then
	 * adds b to a if a is negative.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private int wrap(int a, int b) {
		int result = a % b;
		return result < 0 ? result + b : result;
	}

	/**
	 * Gets the rules for whether or not a dead cell comes to life
	 * 
	 * @return the rules whether or not a dead cell comes to life
	 */
	public byte getDeadCellSpawns() {
		return deadCellSpawns;
	}

	/**
	 * Sets the rules for whether or not a dead cell comes to life
	 * 
	 * @param deadCellSpawns
	 *            the rules whether or not a dead cell comes to life
	 */
	public void setDeadCellSpawns(byte deadCellSpawns) {
		this.deadCellSpawns = deadCellSpawns;
	}

	/**
	 * Gets the rules for whether or not an alive cell stays alive
	 * 
	 * @return the rules for whether or not an alive cell stays alive
	 */
	public byte getAliveCellLives() {
		return aliveCellLives;
	}

	/**
	 * Sets the rules for whether or not an alive cell stays alive
	 * 
	 * @param aliveCellLives
	 *            the rules for whether or not an alive cell stays alive
	 */
	public void setAliveCellLives(byte aliveCellLives) {
		this.aliveCellLives = aliveCellLives;
	}

	/**
	 * Gets the width of the board.
	 * 
	 * @return The width of the board
	 */
	public int getWidth() {
		return this.board.getWidth();
	}

	/**
	 * Gets the height of the board.
	 * 
	 * @return The height of the board
	 */
	public int getHeight() {
		return this.board.getHeight();
	}

	/**
	 * Bit-shifts all buffers to the left.
	 * 
	 * @param buffers
	 *            The number of buffers to shift
	 */
	public void bitShiftLeft(int buffers) {
		this.board.bitShiftLeft(buffers);
	}

	/**
	 * Bit-shifts all buffers to the right.
	 * 
	 * @param buffers
	 *            The number of buffers to shift
	 */
	public void bitShiftRight(int buffers) {
		this.board.bitShiftRight(buffers);
	}

	/**
	 * Clears the board.
	 */
	public void clearBoard() {
		this.bitShiftRight(1);
		for (int row = 0; row < this.getHeight(); row++) {
			for (int col = 0; col < this.getWidth(); col++) {
				this.setCurrentCell(row, col, 0);
			}
		}
	}

}
