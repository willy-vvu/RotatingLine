import java.util.ArrayList;

/**
 * Projector.java
 * 
 * Projects an array of 3D points onto an array of 2D points from the
 * perspective of a virtual camera.
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
	private Quaternion rotation = new Quaternion();
	private Quaternion rotationInverse = new Quaternion();
	private double fov = 45;
	private double ez = 0;
	private boolean needsRecomputation = true;

	public static void main(String[] args) {
		Projector p = new Projector(45);
		p.rotation.set(0, 1, 0);
		p.setScreenSize(new Vector2(2, 1));
		ArrayList<Vector3> source = new ArrayList<Vector3>();
		ArrayList<Vector2> destination = new ArrayList<Vector2>();
		source.add(new Vector3(1, 1, 1));
		source.add(new Vector3(-1, -1, 1));
		source.add(new Vector3(1, -1, 2));
		source.add(new Vector3(-1, 1, 2));
		p.project(source, destination);
		System.out.println(destination);
	}

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
	 * 
	 * @param source
	 * @param destination
	 * @return the destination array
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
		for (int i = 0; i < source.size(); i++) {
			project(source.get(i), destination.get(i));
		}
		return destination;
	}

	/**
	 * Projects a single Vector3 onto a Vector2
	 * 
	 * @param source
	 * @param destination
	 * @return the destination vector
	 */
	public Vector2 project(Vector3 source, Vector2 destination) {
		if (needsRecomputation) {
			compute();
		}
		tempVector3.copy(source).subtract(position).rotate(rotationInverse);
		if (tempVector3.getZ() > 0) {
			destination.copy(tempVector3).divideScalar(tempVector3.getZ())
					.multiplyScalar(ez * halfScreenSize.getY())
					.add(halfScreenSize);
		} else {
			destination.set(Double.NaN, Double.NaN);
		}
		return destination;
	}

	/**
	 * Pre-compute the camera transform matrix to save further computation.
	 * 
	 * @return itself
	 */
	public Projector compute() {
		this.ez = 1 / Math.tan(Math.PI * this.fov / 360);
		rotationInverse.copy(this.rotation).inverse();
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
	public Quaternion getRotation() {
		return rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(Quaternion rotation) {
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
