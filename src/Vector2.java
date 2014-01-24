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
	private double x = 0;
	private double y = 0;

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
	 * @return itself
	 */
	public Vector2 set(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Copies the values of another vector into itself
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector2 copy(Vector2 vector) {
		this.x = vector.x;
		this.y = vector.y;
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
	 * Divides the vector by a scalar.
	 * 
	 * @param scalar
	 * @return itself
	 */
	public Vector2 divideScalar(double scalar) {
		return this.multiplyScalar(scalar == 0 ? 0 : 1 / scalar);
	}

	/**
	 * Rotates a vector counter-clockwise by a given angle.
	 * 
	 * @param angle
	 * @return itself
	 */
	public Vector2 rotate(double angle) {
		double tempX = x, tempY = y;
		this.x = tempX * Math.cos(angle) - tempY * Math.sin(angle);
		this.y = tempX * Math.sin(angle) + tempY * Math.cos(angle);
		return this;
	}

	/**
	 * Inverses the current Vector2.
	 * 
	 * @return itself
	 */
	public Vector2 inverse() {
		return this.multiplyScalar(-1);
	}

	/**
	 * Use the pythagorean theorem on x and y.
	 * 
	 * @param x
	 * @param y
	 * @return the hypotenuse
	 */
	public static double hypotenuse(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Returns the vector in the form (x, y)
	 */
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
}
