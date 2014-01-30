/**
 * CubeGeometry.java
 * 
 * A geometry defining a cube
 * 
 * Written Jan 30, 2014.
 * 
 * @author William Wu
 * 
 */
public class CubeGeometry extends Geometry {
	private Vector3 size = new Vector3();

	/**
	 * Creates a new Cube Geometry
	 */
	public CubeGeometry() {
		getVertices().add(new Vector3());
		getVertices().add(new Vector3());
		getVertices().add(new Vector3());
		getVertices().add(new Vector3());
		getVertices().add(new Vector3());
		getVertices().add(new Vector3());
		getVertices().add(new Vector3());
		getVertices().add(new Vector3());

		getLines().add(new Line(0, 1));
		getLines().add(new Line(0, 2));
		getLines().add(new Line(1, 3));
		getLines().add(new Line(2, 3));
		getLines().add(new Line(4, 5));
		getLines().add(new Line(4, 6));
		getLines().add(new Line(5, 7));
		getLines().add(new Line(6, 7));
		getLines().add(new Line(0, 4));
		getLines().add(new Line(1, 5));
		getLines().add(new Line(2, 6));
		getLines().add(new Line(3, 7));
	}

	/**
	 * Creates a new cube geometry with a given width, height, and depth
	 * 
	 * @param width
	 * @param height
	 * @param depth
	 */
	public CubeGeometry(double width, double height, double depth) {
		this();
		size.set(width, height, depth);
		rebuild();
	}

	/**
	 * Creates a new cube geometry with a given box size.
	 * 
	 * @param size
	 */
	public CubeGeometry(Vector3 size) {
		this();
		this.size.copy(size);
		rebuild();
	}

	public void rebuild() {
		getVertices().get(0)
				.set(getSize().getX(), getSize().getY(), getSize().getZ())
				.multiplyScalar(0.5);
		getVertices().get(1)
				.set(getSize().getX(), getSize().getY(), -getSize().getZ())
				.multiplyScalar(0.5);
		getVertices().get(2)
				.set(-getSize().getX(), getSize().getY(), getSize().getZ())
				.multiplyScalar(0.5);
		getVertices().get(3)
				.set(-getSize().getX(), getSize().getY(), -getSize().getZ())
				.multiplyScalar(0.5);
		getVertices().get(4)
				.set(getSize().getX(), -getSize().getY(), getSize().getZ())
				.multiplyScalar(0.5);
		getVertices().get(5)
				.set(getSize().getX(), -getSize().getY(), -getSize().getZ())
				.multiplyScalar(0.5);
		getVertices().get(6)
				.set(-getSize().getX(), -getSize().getY(), getSize().getZ())
				.multiplyScalar(0.5);
		getVertices().get(7)
				.set(-getSize().getX(), -getSize().getY(), -getSize().getZ())
				.multiplyScalar(0.5);
	}

	/**
	 * @return the size
	 */
	public Vector3 getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(Vector3 size) {
		this.size = size;
	}
}
