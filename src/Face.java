/**
 * Face.java
 * 
 * A class to hold the indices of three vertices that constitute one face.
 * 
 * Written Jan 24, 2014.
 * 
 * @author William Wu
 * 
 */
public class Face {
	private int a = 0, b = 0, c = 0;

	/**
	 * Creates a new face
	 */
	public Face() {
	}

	/**
	 * Creates a new face, keeping track of the indices of the vertices
	 * 
	 * @param a
	 * @param b
	 * @param c
	 */
	public Face(int a, int b, int c) {
		this.set(a, b, c);
	}

	/**
	 * Sets the indices of all the vertices in the face at once
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return itself
	 */
	public Face set(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
		return this;
	}

	/**
	 * Copyies the indices of all the vertices in antoher face to itself
	 * 
	 * @param face
	 * @return itself
	 */
	public Face copy(Face face) {
		this.a = face.a;
		this.b = face.b;
		this.c = face.c;
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

	/**
	 * @return the c
	 */
	public int getC() {
		return c;
	}

	/**
	 * @param c
	 *            the c to set
	 */
	public void setC(int c) {
		this.c = c;
	}

	/**
	 * Gets the value at a given index to facilitate looping.
	 * 
	 * @param index
	 *            0 -> a, 1 -> b, 2 -> c
	 * @return
	 */
	public int getAt(int index) {
		if (index == 0) {
			return this.a;
		} else if (index == 1) {
			return this.b;
		} else if (index == 2) {
			return this.c;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Face [a=" + a + ", b=" + b + ", c=" + c + "]";
	}
}
