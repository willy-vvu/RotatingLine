/**
 * Provides a class for the storage and manipulation of multiple boolean arrays
 * from a single Board object by using byte manipulation. Note that buffers are
 * zero-indexed.
 * 
 * MultiBufferBoard.java - 10/31/2013
 * 
 * @see Board
 * 
 * @author William Wu
 * 
 */

public class MultiBufferBoard {
	private Board board;

	/**
	 * Tests a boolean board with four buffers.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MultiBufferBoard b = new MultiBufferBoard(new int[][] { { 1, 2 },
				{ 3, 4 } });
		System.out.println(b.toString(0));
		System.out.println(b.toString(1));
		System.out.println(b.toString(2));
		System.out.println(b.toString(3));
	}

	/**
	 * Creates a new MultiBufferBoard from an existing 2D integer array
	 * 
	 * @param board
	 *            An existing 2D integer array
	 * @param BUFFERS
	 *            The number of buffers to consider
	 */
	public MultiBufferBoard(int[][] board) {
		this.board = new Board(board);
	}

	/**
	 * Creates a new MultiBufferBoard with the specified width and height
	 * 
	 * @param width
	 *            The specified width
	 * @param height
	 *            The specified height
	 * @param BUFFERS
	 *            The number of buffers to consider
	 */
	public MultiBufferBoard(int width, int height) {
		this.board = new Board(width, height);
	}

	/**
	 * Returns a tab and newline separated table of values that represent the
	 * current board.
	 */
	public String toString() {
		return this.board.toString();
	}

	/**
	 * Returns a tab and newline separated table of values that represent the
	 * current board at a given buffer.
	 */
	public String toString(int buffer) {
		String string = "";
		for (int row = 0; row < this.getHeight(); row++) {
			for (int col = 0; col < this.getWidth(); col++) {
				string += (this.getAt(row, col) & (1 << buffer)) == 0 ? 0 : 1;
				if (col < this.getWidth() - 1) {
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
		return this.board.getAt(row, col);
	}

	/**
	 * Sets the specified boolean element of the board in a specified buffer to
	 * a given value.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @param buffer
	 *            The specified buffer
	 * @param value
	 *            The given value
	 */
	public void setAt(int row, int col, int buffer, int value) {
		int existing = this.getAt(row, col);
		// Clear any existing value from the buffer to 1
		existing |= (1 << buffer);
		// Invert the new value if needed
		if (value == 0) {
			existing ^= (1 << buffer);
		}
		this.setAt(row, col, existing);
	}

	/**
	 * Gets the boolean piece on the board on at a specified row, column, and
	 * buffer.
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @param buffer
	 *            The specified buffer
	 * @return The piece
	 */
	public int getAt(int row, int col, int buffer) {
		return (this.getAt(row, col) & (1 << buffer)) == 0 ? 0 : 1;
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
		this.board.setAt(row, col, value);
	}

	/**
	 * Bit-shifts all buffers to the right.
	 * 
	 * @param buffers
	 *            The number of buffers to shift
	 */
	public void bitShiftRight(int buffers) {
		for (int row = 0; row < this.getHeight(); row++) {
			for (int col = 0; col < this.getWidth(); col++) {
				this.setAt(row, col, this.getAt(row, col) >> buffers);
			}
		}
	}

	/**
	 * Bit-shifts all buffers to the left.
	 * 
	 * @param buffers
	 *            The number of buffers to shift
	 */
	public void bitShiftLeft(int buffers) {
		for (int row = 0; row < this.getHeight(); row++) {
			for (int col = 0; col < this.getWidth(); col++) {
				this.setAt(row, col, this.getAt(row, col) << buffers);
			}
		}
	}

	/**
	 * Returns whether or not the specified row and column exists in the board
	 * 
	 * @param row
	 *            The specified row
	 * @param col
	 *            The specified column
	 * @return Whether or not the specified row and column is valid
	 */
	public boolean isValid(int row, int col) {
		return this.board.isValid(row, col);
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
}
