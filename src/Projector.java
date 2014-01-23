import java.util.ArrayList;

/**
 * Projector.java
 * 
 * Projects an array of 3D points using a projection matrix onto an array of 2D
 * points.
 * 
 * Written Jan 23, 2014.
 * 
 * @author William Wu
 * 
 */
public class Projector {
	private Vector2 screenSize = null;
	private Vector2 halfScreenSize = new Vector2();
	private Vector3 position = new Vector3();
	private static Vector3 tempVector3 = new Vector3();
	private Rotation rotation = new Rotation();
	private static Rotation tempRotation = new Rotation();
	private Matrix4 cameraTransform = new Matrix4();
	private static final Matrix4 tempMatrix = new Matrix4();
	private double fov = 45;
	private double ez = 0;
	private boolean needsRecomputation = true;

	/**
	 * Creates a new Projector
	 */
	public Projector() {

	}

	/**
	 * Creates a new Projector
	 * 
	 * @param fov
	 *            the field of view (in degrees)
	 */
	public Projector(double fov) {
		this.fov = fov;
	}

	/**
	 * Creates a new Projector
	 * 
	 * @param fov
	 *            the field of view (in degrees)
	 * @param position
	 *            the position of the projector (camera)
	 */
	public Projector(double fov, Vector3 position) {
		this(fov);
		this.position.copy(position);
	}

	/**
	 * Projects an ArrayList of 3D points onto an ArrayList of 2D points.
	 */
	public ArrayList<Vector2> project(ArrayList<Vector3> source,
			ArrayList<Vector2> destination) {
		// Make sure the arrays have matching size
		while (destination.size() < source.size()) {
			destination.add(new Vector2());
		}
		while (destination.size() > source.size()) {
			destination.remove(0);
		}
		if (needsRecomputation) {
			compute();
		}
		for (int i = 0; i < source.size(); i++) {
			tempVector3.copy(source.get(i)).multiplyMatrix(cameraTransform);
			destination
					.get(i)
					.copy(tempVector3)
					.divideScalar(
							halfScreenSize.getX() * ez * tempVector3.getZ())
					.add(halfScreenSize);
		}
		return destination;
	}

	/**
	 * Pre-compute the camera transform matrix to save further computation.
	 * 
	 * @return itself
	 */
	private Projector compute() {
		this.ez = 1 / Math.tan(Math.PI * this.fov / 360);
		Projector.tempRotation.copy(rotation).inverse();
		Projector.tempVector3.copy(position).inverse();
		tempMatrix.makeIdentity().setRotation(tempRotation);
		cameraTransform.makeIdentity().setTranslation(tempVector3)
				.multiplyBeforeMatrix(tempMatrix);
		this.halfScreenSize.copy(screenSize).multiplyScalar(0.5);
		needsRecomputation = false;
		return this;
	}

	/**
	 * @return the screenSize
	 */
	public Vector2 getScreenSize() {
		return screenSize;
	}

	/**
	 * @param screenSize
	 *            the screenSize to set
	 */
	public void setScreenSize(Vector2 screenSize) {
		this.screenSize = screenSize;
		needsRecomputation = true;

	}

	/**
	 * @return the position
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Vector3 position) {
		this.position = position;
		needsRecomputation = true;

	}

	/**
	 * @return the rotation
	 */
	public Rotation getRotation() {
		return rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
		needsRecomputation = true;

	}

	/**
	 * @return the fov
	 */
	public double getFov() {
		return fov;
	}

	/**
	 * @param fov
	 *            the fov to set
	 */
	public void setFov(double fov) {
		this.fov = fov;
		needsRecomputation = true;

	}
}
