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
	Vector2() {
	}

	/**
	 * Creates a new Vector2 with the given x and y
	 * 
	 * @param x
	 * @param y
	 */
	Vector2(double x, double y) {
		this.set(x, y);
	}

	/**
	 * Sets the x and y to a given x and y
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	Vector2 set(double x, double y) {
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
	Vector2 multiply(Vector2 vector) {
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
	Vector2 add(Vector2 vector) {
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
	Vector2 subtract(Vector2 vector) {
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
	Vector2 multiplyScalar(double scalar) {
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
	Vector2 rotate(double theta) {
		double tempX = x, tempY = y;
		this.x = tempX * Math.cos(theta) - tempY * Math.sin(theta);
		this.y = tempX * Math.sin(theta) + tempY * Math.cos(theta);
		return this;
	}

}
