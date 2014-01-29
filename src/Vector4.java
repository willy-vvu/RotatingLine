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
	 * Multiplies the current vector by a 4x4 matrix.
	 * 
	 * @return
	 */
	public Vector4 multiplyMatrix(Matrix4 matrix) {
		// Everyone loves matrix multiplication!
		double tempX = matrix.getAt(0, 0) * this.getX() + matrix.getAt(0, 1)
				* this.getY() + matrix.getAt(0, 2) * this.getZ()
				+ matrix.getAt(0, 3) * this.w, tempY = matrix.getAt(1, 0)
				* this.getX() + matrix.getAt(1, 1) * this.getY()
				+ matrix.getAt(1, 2) * this.getZ() + matrix.getAt(1, 3)
				* this.w, tempZ = matrix.getAt(2, 0) * this.getX()
				+ matrix.getAt(2, 1) * this.getY() + matrix.getAt(2, 2)
				* this.getZ() + matrix.getAt(2, 3) * this.w, tempW = matrix
				.getAt(3, 0)
				* this.getX()
				+ matrix.getAt(3, 1)
				* this.getY()
				+ matrix.getAt(3, 2)
				* this.getZ()
				+ matrix.getAt(3, 3)
				* this.w;
		super.set(tempX, tempY, tempZ);
		this.w = tempW;
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
	 * Use the pythagorean theorem on x, y, z, and w.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 * @return the hypotenuse
	 */
	public static double hypotenuse(double x, double y, double z, double w) {
		return Math.sqrt(x * x + y * y + z * z + w * w);
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
