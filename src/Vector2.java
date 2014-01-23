/**
 * Vector2.java
 * 
 * A class to create, manipulate, and rotate 2 dimensional vectors.
 * 
 * Written Jan 22, 2014.
 * 
 * @author William Wu
 * 
 */
public class Vector2 {
	public double x = 0;
	public double y = 0;

	/**
	 * Creates a new, default Vector2, with the value (0,0)
	 */
	public Vector2() {
	}

	/**
	 * Creates a new Vector2 with the given x and y
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2(double x, double y) {
		this.set(x, y);
	}

	/**
	 * Sets the x and y to a given x and y
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Vector2 set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Multiplies another vector into itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector2 multiply(Vector2 vector) {
		this.x *= vector.x;
		this.y *= vector.y;
		return this;
	}

	/**
	 * Adds another vector into itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector2 add(Vector2 vector) {
		this.x += vector.x;
		this.y += vector.y;
		return this;
	}

	/**
	 * Subtracts another vector from itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector2 subtract(Vector2 vector) {
		this.x -= vector.x;
		this.y -= vector.y;
		return this;
	}

	/**
	 * Multiplies the vector by a scalar.
	 * 
	 * @param scalar
	 * @return itself
	 */
	public Vector2 multiplyScalar(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		return this;
	}

	/**
	 * Rotates a vector counter-clockwise by a given angle.
	 * 
	 * @param theta
	 * @return itself
	 */
	public Vector2 rotate(double theta) {
		double tempX = x, tempY = y;
		this.x = tempX * Math.cos(theta) - tempY * Math.sin(theta);
		this.y = tempX * Math.sin(theta) + tempY * Math.cos(theta);
		return this;
	}

	/**
	 * Use the pythagorean theorem on x and y.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static double hypotenuse(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * 
	 * @return the vector in the form (x, y)
	 */
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
