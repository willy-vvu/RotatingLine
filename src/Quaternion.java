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
	 * Create a new quaterion
	 */
	public Quaternion() {
	}

	/**
	 * Creates a new Quaternion
	 * 
	 * @param angle
	 *            the specified rotation angle
	 * @param axis
	 *            the rotation axis
	 */
	public Quaternion(double angle, Vector3 axis) {
		this.setAxisAngle(angle, axis);
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
	public Quaternion setAxisAngle(double angle, Vector3 axis) {
		this.copy(axis);
		this.setW(0);
		this.normalize();
		this.multiplyScalar(Math.sin(angle / 2));
		this.setW(Math.cos(angle / 2));
		return this;
	}

	/**
	 * Sets the Quaternion based on a given Rotation
	 * 
	 * @param rotation
	 * @return itself
	 */
	public Quaternion setFromRotation(Rotation rotation) {
		this.setAxisAngle(rotation.angle, rotation);
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
}
