/**
 * Quaternion.java
 * 
 * A class to create and manipulate quaternions
 * 
 * Written Jan 23, 2014.
 * 
 * @author William Wu
 * 
 */
public class Quaternion extends Vector4 {
	/**
	 * Create a new quaternion
	 */
	public Quaternion() {
	}

	/**
	 * Creates a new Quaternion
	 * 
	 * @param axis
	 *            the rotation axis
	 * @param angle
	 *            the specified rotation angle
	 */
	public Quaternion(Vector3 axis, double angle) {
		this.setAxisAngle(axis, angle);
	}

	/**
	 * Create a new Quaternion
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public Quaternion(double x, double y, double z, double w) {
		super(x, y, z, w);
	}

	/**
	 * Sets the Quaternion based on a given axis and angle.
	 * 
	 * @return itself
	 */
	public Quaternion setAxisAngle(Vector3 axis, double angle) {
		this.copy(axis);
		this.setW(0);
		this.normalize();
		this.multiplyScalar(Math.sin(angle / 2));
		this.setW(Math.cos(angle / 2));
		return this;
	}

	/**
	 * Finds the inverse of the Quaternion
	 * 
	 * @return itself
	 */
	public Quaternion inverse() {
		super.inverse();
		// Twice-inverse the w
		this.setW(-this.getW());
		return this;
	}

	/**
	 * Multiplies two Quaternions together using the Hamilton product.
	 * 
	 * @param quaternion
	 * @return
	 */
	public Quaternion multiply(Quaternion quaternion) {
		double tempW = this.getW() * quaternion.getW() - this.getX()
				* quaternion.getX() - this.getY() * quaternion.getY()
				- this.getZ() * quaternion.getZ(), tempX = this.getW()
				* quaternion.getX() + this.getX() * quaternion.getW()
				+ this.getY() * quaternion.getZ() - this.getZ()
				* quaternion.getY(), tempY = this.getW() * quaternion.getY()
				- this.getX() * quaternion.getZ() + this.getY()
				* quaternion.getW() + this.getZ() * quaternion.getX(), tempZ = this
				.getW()
				* quaternion.getZ()
				+ this.getX()
				* quaternion.getY()
				- this.getY()
				* quaternion.getX()
				+ this.getZ()
				* quaternion.getW();
		this.setW(tempW);
		this.setX(tempX);
		this.setY(tempY);
		this.setZ(tempZ);
		return this;
	}
}
