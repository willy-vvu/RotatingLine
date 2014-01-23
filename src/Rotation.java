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
public class Rotation {
	private double theta = 0;
	private Vector3 axis = new Vector3(0, 1, 0);

	/**
	 * Creates a new Rotation object
	 */
	public Rotation() {

	}

	/**
	 * Creates a new Rotation object
	 * 
	 * @param theta
	 *            the specified rotation angle
	 */
	public Rotation(double theta) {
		this.theta = theta;
	}

	/**
	 * Creates a new Rotation object
	 * 
	 * @param theta
	 *            the specified rotation angle
	 * @param axis
	 *            the rotation axis
	 */
	public Rotation(double theta, Vector3 axis) {
		this(theta);
		this.setAxis(axis);
	}

	/**
	 * Creates a new Rotation object
	 * 
	 * @param theta
	 *            the specified rotation angle
	 * @param
	 */
	public Rotation(double theta, double x, double y, double z) {
		this(theta);
		this.setAxis(axis);
	}

	/**
	 * Copies the values of another rotation into itself
	 * 
	 * @param rotation
	 * @return itself
	 */
	public Rotation copy(Rotation rotation) {
		this.theta = rotation.theta;
		this.axis.copy(rotation.axis);
		return this;
	}

	/**
	 * Inverses the current rotation
	 * 
	 * @return itself
	 */
	public Rotation inverse() {
		this.theta *= -1;
		return this;
	}

	/**
	 * Multiplies the current rotation by a specified scalar
	 * 
	 * @param scalar
	 * @return itself
	 */
	public Rotation multiplyScalar(double scalar) {
		this.theta *= scalar;
		return this;
	}

	/**
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * @param theta
	 *            the theta to set
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}

	/**
	 * @return the axis
	 */
	public Vector3 getAxis() {
		return axis;
	}

	/**
	 * @param axis
	 *            the axis to set
	 */
	public void setAxis(Vector3 axis) {
		this.axis.copy(axis).normalize();
	}
}
