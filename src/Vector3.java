/**
 * Vector3.java
 * 
 * A class to create and manipulate 3 dimensional vectors.
 * 
 * Written Jan 22, 2014.
 * 
 * @author William Wu
 * 
 */
public class Vector3 extends Vector2 {
	private double z = 0;
	private static final Vector3 tempV3 = new Vector3();
	private static final Quaternion tempQ = new Quaternion();
	private static final Quaternion tempQ_2 = new Quaternion();

	/**
	 * Creates a new, default Vector3, with the value (0,0,0)
	 */
	public Vector3() {
	}

	/**
	 * Creates a Vector3 based on another Vector3.
	 */
	public Vector3(Vector3 vector) {
		this.copy(vector);
	}

	/**
	 * Creates a new Vector3 x, y, and z set to a given value.
	 * 
	 * @param r
	 */
	public Vector3(double r) {
		this.set(r);
	}

	/**
	 * Creates a new Vector3 with the given x, y, and z
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3(double x, double y, double z) {
		this.set(x, y, z);
	}

	/**
	 * Sets the x, y, and z to the same value
	 * 
	 * @param r
	 * @return itself
	 */
	public Vector3 set(double r) {
		super.set(r);
		this.z = r;
		return this;
	}

	/**
	 * Sets the x, y, and z to a given x, y and z
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return itself
	 */
	public Vector3 set(double x, double y, double z) {
		super.set(x, y);
		this.z = z;
		return this;
	}

	/**
	 * Copies the values of another vector into itself
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector3 copy(Vector3 vector) {
		super.copy(vector);
		this.z = vector.z;
		return this;
	}

	/**
	 * Multiplies another vector into itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector3 multiply(Vector3 vector) {
		super.multiply(vector);
		this.z *= vector.z;
		return this;
	}

	/**
	 * Divides another vector from itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector3 divide(Vector3 vector) {
		super.divide(vector);
		this.z = vector.z == 0 ? 0 : this.z / vector.z;
		return this;
	}

	/**
	 * Adds another vector into itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector3 add(Vector3 vector) {
		super.add(vector);
		this.z += vector.z;
		return this;
	}

	/**
	 * Subtracts another vector from itself.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Vector3 subtract(Vector3 vector) {
		super.subtract(vector);
		this.z -= vector.z;
		return this;
	}

	/**
	 * Multiplies the vector by a scalar.
	 * 
	 * @param scalar
	 * @return itself
	 */
	public Vector3 multiplyScalar(double scalar) {
		super.multiplyScalar(scalar);
		this.z *= scalar;
		return this;
	}

	/**
	 * Divides the vector by a scalar.
	 * 
	 * @param scalar
	 * @return itself
	 */
	public Vector3 divideScalar(double scalar) {
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
	 * Linearly interpolates from the current vector to a target vector given a
	 * ratio
	 * 
	 * @param vector
	 * @param ratio
	 * @return itself
	 */
	public Vector3 lerp(Vector3 vector, double ratio) {
		tempV3.copy(vector).multiplyScalar(ratio);
		this.multiplyScalar(1 - ratio).add(tempV3);
		return this;
	}

	/**
	 * Inverses the current Vector3.
	 * 
	 * @return itself
	 */
	public Vector3 inverse() {
		super.inverse();
		return this;
	}

	/**
	 * Cross products another vector into itself.
	 * 
	 * @param vector
	 * @return
	 */
	public Vector3 cross(Vector3 vector) {
		double tempX = this.getY() * vector.getZ() - vector.getY()
				* this.getZ(), tempY = this.getX() * vector.getZ()
				- vector.getX() * this.getZ(), tempZ = this.getX()
				* vector.getY() - vector.getX() * this.getY();
		this.set(tempX, tempY, tempZ);
		return this;
	}

	/**
	 * Rotates the current vector by a Quaternion
	 * 
	 * @param quaternion
	 * @return itself
	 */
	public Vector3 rotate(Quaternion quaternion) {
		// p' = qpq
		Vector3.tempQ_2.copy(this);
		Vector3.tempQ_2.setW(0);
		Vector3.tempQ.copy(quaternion);
		Vector3.tempQ.multiply(Vector3.tempQ_2);
		Vector3.tempQ_2.copy(quaternion);
		Vector3.tempQ_2.inverse();
		Vector3.tempQ.multiply(Vector3.tempQ_2);
		this.copy(Vector3.tempQ);
		return this;
	}

	/**
	 * @param vector
	 * @return the dot product of the two vectors.
	 */
	public double dot(Vector3 vector) {
		return super.dot(vector) + this.z * vector.z;
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
	public double distanceToSquared(Vector3 vector) {
		return hypotenuseSquared(this.getX() - vector.getX(), this.getY()
				- vector.getY(), this.z - vector.z);
	}

	/**
	 * 
	 * @param vector
	 * @return the distance to another vector squared
	 */
	public double distanceTo(Vector3 vector) {
		return Math.sqrt(distanceToSquared(vector));
	}

	/**
	 * Use the half the pythagorean theorem on x, y, and z.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the hypotenuse squared
	 */
	public static double hypotenuseSquared(double x, double y, double z) {
		return x * x + y * y + z * z;
	}

	/**
	 * Use the pythagorean theorem on x, y, and z.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the hypotenuse
	 */
	public static double hypotenuse(double x, double y, double z) {
		return Math.sqrt(hypotenuseSquared(x, y, z));
	}

	/**
	 * Returns the vector in the form (x, y, z)
	 */
	public String toString() {
		return "(" + this.getX() + ", " + this.getY() + ", " + z + ")";
	}

	/**
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}
}
