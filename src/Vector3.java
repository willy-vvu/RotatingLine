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

	/**
	 * Creates a new, default Vector3, with the value (0,0,0)
	 */
	public Vector3() {
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
	 * 
	 * @return the length (magnitude) of the vector.
	 */
	public double length() {
		return Vector3.hypotenuse(this.getX(), this.getY(), this.z);
	}

	/**
	 * Converts the vector into a unit vector.
	 * 
	 * @return itself
	 */
	public Vector3 normalize() {
		return this.divideScalar(this.length());
	}

	/**
	 * Multiplies the current vector by a 4x4 matrix.
	 * 
	 * @return
	 */
	public Vector3 multiplyMatrix(Matrix4 matrix) {
		// Everyone loves matrix multiplication!
		double tempX = matrix.getAt(0, 0) * getX() + matrix.getAt(0, 1)
				* getY() + matrix.getAt(0, 2) * this.z + matrix.getAt(0, 3), tempY = matrix
				.getAt(1, 0)
				* getX()
				+ matrix.getAt(1, 1)
				* getY()
				+ matrix.getAt(1, 2) * this.z + matrix.getAt(1, 3), tempZ = matrix
				.getAt(2, 0)
				* getX()
				+ matrix.getAt(2, 1)
				* getY()
				+ matrix.getAt(2, 2) * this.z + matrix.getAt(2, 3);
		setX(tempX);
		setY(tempY);
		this.z = tempZ;
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
	 * Use the pythagorean theorem on x, y, and z.
	 * 
	 * @param x
	 * @param y
	 * @return the hypotenuse
	 */
	public static double hypotenuse(double x, double y, double z) {
		return Math.sqrt(x * x + y * y + z * z);
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
