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
	private static final Vector2 tempV2 = new Vector2();

	/**
	 * Creates a new, default Vector2, with the value (0,0)
	 */
	public Vector2() {
	}

	/**
	 * Creates a Vector2 based on another Vector2.
	 */
	public Vector2(Vector2 vector) {
		this.copy(vector);
	}

	/**
	 * Creates a new Vector2 both x and y set to a given value.
	 * 
	 * @param r
	 */
	public Vector2(double r) {
		this.set(r);
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
	 * Sets the x and y to the same value
	 * 
	 * @param r
	 * @return itself
	 */
	public Vector2 set(double r) {
		this.x = r;
		this.y = r;
		return this;
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
	 * Divides another vector from itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector2 divide(Vector2 vector) {
		this.x = vector.x == 0 ? 0 : this.x / vector.x;
		this.y = vector.y == 0 ? 0 : this.y / vector.y;
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
		return this.multiplyScalar(scalar == 0 ? Double.NaN : 1 / scalar);
	}

	/**
	 * Normalizes the vector (converts it into a unit vector).
	 * 
	 * @return itself
	 */
	public Vector2 normalize() {
		return this.divideScalar(this.length());
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
	 * Linearly interpolates from the current vector to a target vector given a
	 * ratio
	 * 
	 * @param vector
	 * @param ratio
	 * @return itself
	 */
	public Vector2 lerp(Vector2 vector, double ratio) {
		tempV2.copy(vector).multiplyScalar(ratio);
		this.multiplyScalar(1 - ratio).add(tempV2);
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
	 * @param vector
	 * @return the dot product of the two vectors.
	 */
	public double dot(Vector2 vector) {
		return this.x * vector.x + this.y * vector.y;
	}

	/**
	 * 
	 * @param vector
	 * @return the length (magnitude) squared of the current vector.
	 */
	public double lengthSquared() {
		return this.dot(this);
	}

	/**
	 * 
	 * @param vector
	 * @return the length (magnitude) of the current vector.
	 */
	public double length() {
		return Math.sqrt(this.lengthSquared());
	}

	/**
	 * 
	 * @param vector
	 * @return the distance to another vector squared
	 */
	public double distanceToSquared(Vector2 vector) {
		return hypotenuseSquared(this.x - vector.x, this.y - vector.y);
	}

	/**
	 * 
	 * @param vector
	 * @return the distance to another vector squared
	 */
	public double distanceTo(Vector2 vector) {
		return Math.sqrt(distanceToSquared(vector));
	}

	/**
	 * Use the half the pythagorean theorem on x and y.
	 * 
	 * @param x
	 * @param y
	 * @return the hypotenuse squared
	 */
	public static double hypotenuseSquared(double x, double y) {
		return x * x + y * y;
	}

	/**
	 * Use the pythagorean theorem on x and y.
	 * 
	 * @param x
	 * @param y
	 * @return the hypotenuse
	 */
	public static double hypotenuse(double x, double y) {
		return Math.sqrt(hypotenuseSquared(x, y));
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
