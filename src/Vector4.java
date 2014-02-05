/**
 * Vector4.java
 * 
 * A class to create and manipulate 4 dimensional vectors.
 * 
 * Written Jan 22, 2014.
 * 
 * @author William Wu
 * 
 */
public class Vector4 extends Vector3 {
	private double w = 0;

	/**
	 * Creates a new, default Vector3, with the value (0,0,0)
	 */
	public Vector4() {
	}

	/**
	 * Creates a Vector4 based on another Vector3.
	 */
	public Vector4(Vector4 vector) {
		this.copy(vector);
	}

	/**
	 * Creates a new Vector4 x, y, z, and w set to a given value.
	 * 
	 * @param r
	 */
	public Vector4(double r) {
		this.set(r);
	}

	/**
	 * Creates a new Vector3 with the given x, y, z, and w
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Vector4(double x, double y, double z, double w) {
		this.set(x, y, z, w);
	}

	/**
	 * Sets the w, x, y, and z to the same value
	 * 
	 * @param r
	 * @return itself
	 */
	public Vector3 set(double r) {
		super.set(r);
		this.w = r;
		return this;
	}

	/**
	 * Sets the x, y, z, and w to a given x, y, z, and w
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return itself
	 */
	public Vector4 set(double x, double y, double z, double w) {
		super.set(x, y, z);
		this.w = w;
		return this;
	}

	/**
	 * Copies the values of another vector into itself
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector4 copy(Vector4 vector) {
		super.copy(vector);
		this.w = vector.w;
		return this;
	}

	/**
	 * Multiplies another vector into itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector4 multiply(Vector4 vector) {
		super.multiply(vector);
		this.w *= vector.w;
		return this;
	}

	/**
	 * Divides another vector from itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector4 divide(Vector4 vector) {
		super.divide(vector);
		this.w = vector.w == 0 ? 0 : this.w / vector.w;
		return this;
	}

	/**
	 * Adds another vector into itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector4 add(Vector4 vector) {
		super.add(vector);
		this.w += vector.w;
		return this;
	}

	/**
	 * Subtracts another vector from itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector4 subtract(Vector4 vector) {
		super.subtract(vector);
		this.w -= vector.w;
		return this;
	}

	/**
	 * Multiplies the vector by a scalar.
	 * 
	 * @param scalar
	 * @return itself
	 */
	public Vector4 multiplyScalar(double scalar) {
		super.multiplyScalar(scalar);
		this.w *= scalar;
		return this;
	}

	/**
	 * Divides the vector by a scalar.
	 * 
	 * @param scalar
	 * @return itself
	 */
	public Vector4 divideScalar(double scalar) {
		super.divideScalar(scalar);
		return this;
	}

	/**
	 * Normalizes the vector (converts it into a unit vector).
	 * 
	 * @return itself
	 */
	public Vector3 normalize() {
		super.normalize();
		return this;
	}

	/**
	 * Inverses the current Vector4.
	 * 
	 * @return itself
	 */
	public Vector4 inverse() {
		super.inverse();
		return this;
	}

	/**
	 * @param vector
	 * @return the dot product of the two vectors.
	 */
	public double dot(Vector4 vector) {
		return super.dot(vector) + this.w * vector.w;
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
	public double distanceToSquared(Vector4 vector) {
		return hypotenuseSquared(this.getX() - vector.getX(), this.getY()
				- vector.getY(), this.getZ() - vector.getZ(), this.w - vector.w);
	}

	/**
	 * 
	 * @param vector
	 * @return the distance to another vector squared
	 */
	public double distanceTo(Vector4 vector) {
		return Math.sqrt(distanceToSquared(vector));
	}

	/**
	 * Use the half the pythagorean theorem on w, x, y, and z.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param z
	 * @return the hypotenuse squared
	 */
	public static double hypotenuseSquared(double x, double y, double z,
			double w) {
		return x * x + y * y + z * z + w * w;
	}

	/**
	 * Use the pythagorean theorem on w, x, y, and z.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return the hypotenuse
	 */
	public static double hypotenuse(double x, double y, double z, double w) {
		return Math.sqrt(hypotenuseSquared(x, y, z, w));
	}

	/**
	 * Returns the vector in the form (x, y, z, w)
	 */
	public String toString() {
		return "(" + this.getX() + ", " + this.getY() + ", " + this.getZ()
				+ ", " + this.w + ")";
	}

	/**
	 * @return the w
	 */
	public double getW() {
		return w;
	}

	/**
	 * @param w
	 *            the w to set
	 */
	public void setW(double w) {
		this.w = w;
	}
}
