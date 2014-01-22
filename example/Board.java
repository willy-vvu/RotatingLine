/**
 * Board.java - Provides a class for the storage and manipulation of two
 * dimensional finite integer game boards.
 * 
 * Written 10/31/2013.
 * 
 * @author William Wu
 * 
 */
public class Board {
	private int[][] board;
	private final int width;
	private final int height;

	/**
	 * Tests storage and retrieval of a 2 by 2 board
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Board b = new Board(new int[][] { { 1, 2 }, { 3, 4 } });
		b.setAt(0, 0, b.getAt(1, 1));
		System.out.println(b);
	}

	/**
	 * Creates a new Board from an existing 2D integer array
	 * 
	 * @param board
	 *            An existing 2D integer array
	 */
	public Board(int[][] board) {
		this(board[0].length, board.length);
		for (int row = 0; row < this.height; row++) {
			for (int col = 0; col < this.width; col++) {
				this.board[row][col] = board[row][col];
			}
		}
	}

	/**
	 * Creates a new Board with the specified width and height
	 * 
	 * @param width
	 *            The specified width
	 * @param height
	 *            The specified height
	 */
	public Board(int width, int height) {
		this.board = new int[height][width];
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns a tab and newline separated table of values that represent the
	 * current board.
	 */
	public String toString() {
		String string = "";
		for (int row = 0; row < this.height; row++) {
			for (int col = 0; col < this.width; col++) {
				string += board[row][col];
				if (col < this.width - 1) {
					string += "\t";
				}
			}
			string += "\n";
		}
		return string;
	}

	/**
	 * Gets the piece on the board on at a specified row and column.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @return The piece
	 */
	public int getAt(int row, int col) {
		if (isValid(row, col)) {
			return this.board[row][col];
		} else {
			return 0;
		}
	}

	/**
	 * Sets the specified element of the board to a given value.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @param value
	 *            The given value
	 */
	public void setAt(int row, int col, int value) {
		if (isValid(row, col)) {
			this.board[row][col] = value;
		}
	}

	/**
	 * Returns whether or not the specified row and column exists in the board.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @return Whether or not the specified row and column is valid
	 */
	public boolean isValid(int row, int col) {
		return row >= 0 && col >= 0 && row < this.height && col < this.width;
	}

	/**
	 * Gets the width of the board.
	 * 
	 * @return The width of the board
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Gets the height of the board.
	 * 
	 * @return The height of the board
	 */
	public int getHeight() {
		return this.height;
	}
}
