/**
 * Rotation.java
 * 
 * A class to store and manipulate rotations as an axis and angle of rotation.
 * 
 * Written Jan 23, 2014.
 * 
 * @author William Wu
 * 
 */
public class Rotation extends Vector3 {
	public double angle = 0;

	/**
	 * Creates a new Rotation object
	 */
	public Rotation() {
		this.setX(1);
	}

	/**
	 * Creates a new Rotation object
	 * 
	 * @param angle
	 *            the specified rotation angle
	 */
	public Rotation(double angle) {
		this();
		this.angle = angle;
	}

	/**
	 * Creates a new Rotation object
	 * 
	 * @param angle
	 *            the specified rotation angle
	 * @param axis
	 *            the rotation axis
	 */
	public Rotation(double angle, Vector3 axis) {
		this(angle);
		this.copy(axis);
	}

	/**
	 * Creates a new Rotation object
	 * 
	 * @param x
	 *            component of the rotation axis
	 * @param y
	 *            component of the rotation axis
	 * @param z
	 *            component of the rotation axis
	 * @param angle
	 *            the specifed rotation angle
	 */
	public Rotation(double x, double y, double z, double angle) {
		this(angle);
		this.set(x, y, z);
	}

	/**
	 * Multiplies the current rotation by a specified scalar
	 * 
	 * @param scalar
	 * @return itself
	 */
	public Rotation multiplyAngle(double scalar) {
		this.angle *= scalar;
		return this;
	}

	/**
	 * Sets the Rotation from a Quaternion.
	 * 
	 * @param quaternion
	 * @return
	 */
	public Rotation setFromQuaternion(Quaternion quaternion) {
		this.angle = 2 * Math.acos(quaternion.getW());
		this.copy(quaternion);
		this.divideScalar(Math.sin(this.angle / 2));
		return this;
	}

	/**
	 * Adds a given angle to the rotation
	 * 
	 * @param angle
	 */
	public void add(double angle) {
		this.angle += angle;
	}

	/**
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * @param angle
	 *            the angle to set
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

}
