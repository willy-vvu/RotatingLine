/**
 * Line.java
 * 
 * A class to hold the indices of two vertices that constitute one line.
 * 
 * Written Jan 24, 2014.
 * 
 * @author William Wu
 * 
 */
public class Line {
	private int a = 0, b = 0;

	/**
	 * Creates a new line
	 */
	public Line() {
	}

	/**
	 * Creates a new line, keeping track of the indices of the vertices
	 * 
	 * @param a
	 * @param b
	 */
	public Line(int a, int b) {
		this.set(a, b);
	}

	/**
	 * Sets the indices of the vertices in the line
	 * 
	 * @param a
	 * @param b
	 * @return itself
	 */
	public Line set(int a, int b) {
		this.a = a;
		this.b = b;
		return this;
	}

	/**
	 * Copies the indices of the vertices in another line to itself
	 * 
	 * @param line
	 * @return itself
	 */
	public Line copy(Line line) {
		this.a = line.a;
		this.b = line.b;
		return this;
	}

	/**
	 * @return the a
	 */
	public int getA() {
		return a;
	}

	/**
	 * @param a
	 *            the a to set
	 */
	public void setA(int a) {
		this.a = a;
	}

	/**
	 * @return the b
	 */
	public int getB() {
		return b;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public void setB(int b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "Line [a=" + a + ", b=" + b + "]";
	}
}
