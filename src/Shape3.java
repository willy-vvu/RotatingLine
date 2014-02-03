import java.util.ArrayList;

/**
 * Shape3.java
 * 
 * A 2 dimensional ngon (if sides>0) or 3 dimensional polyhedra that computes
 * and transforms its vertices.
 * 
 * A 3d polyhedra defined by four values: A, B, C, D
 * 
 * phi = A + B*i where 0<phi<180 theta = C*i + D*j where 0<theta<360
 * 
 * Where i (a whole number) represents a layer of latitude
 * 
 * Where j (a whole number) represents a layer of longitude
 * 
 * Written Jan 22, 2014.
 * 
 * @author William Wu
 * 
 */
public class Shape3 extends Mesh {
	private ArrayList<Vector3> computed = new ArrayList<Vector3>();
	private double rotationSpeed = 0;
	private Rotation rotation = new Rotation(0, 0, 1, 0);
	private static final Quaternion tempQ = new Quaternion();
	private static final Vector3 tempV3 = new Vector3();
	private byte mode = Shape3.INFLATE;
	private int A = 0, B = 0, C = 0, D = 0;
	public static final int TETRAHEDRON = -3;
	public static final int HEXAHEDRON = -6;
	public static final int OCTAHEDRON = -8;
	public static final int ICOSAHEDRON = -12;
	private int sides = 0;
	public static final byte INFLATE = 0;
	public static final byte INSCRIBE = 1;
	// Note that the center is between 0 and 1, relative to the container size.
	private Vector3 center = new Vector3(0.5);
	private boolean needsRecomputation = true;

	/**
	 * Creates a new shape.
	 */
	public Shape3() {
	}

	/**
	 * Create a new ngon with a given number of sides, or a preset
	 * 
	 * @param sides
	 */
	public Shape3(int sides) {
		if (sides > 0) {
			this.setSides(sides);
		} else {
			this.setPreset(sides);
		}
	}

	/**
	 * Sets the parameters of the shape to a given preset.
	 * 
	 * @param preset
	 *            A preset constant.
	 * @return itself
	 */
	public Shape3 setPreset(int preset) {
		if (preset == Shape3.TETRAHEDRON) {
			this.set(0, 120, 0, 120);
		} else if (preset == Shape3.HEXAHEDRON) {
			this.set(55, 71, 0, 90);
		} else if (preset == Shape3.OCTAHEDRON) {
			this.set(0, 90, 0, 90);
		} else if (preset == Shape3.ICOSAHEDRON) {
			this.set(0, 60, 36, 72);
		}
		return this;
	}

	/**
	 * Create a new shape from a given preset and rotation speed.
	 * 
	 * @param sides
	 * @param rotationSpeed
	 */
	public Shape3(int sides, double rotationSpeed) {
		if (sides > 0) {
			this.setSides(sides);
		} else {
			this.setPreset(sides);
		}
		this.rotationSpeed = rotationSpeed;
	}

	/**
	 * Creates a new shape given the four parameters.
	 * 
	 */
	public Shape3(int A, int B, int C, int D) {
		this.set(A, B, C, D);
	}

	/**
	 * Sets the four parameters of the shape at once.
	 * 
	 * @param A
	 * @param B
	 * @param C
	 * @param D
	 * @return
	 */
	public Shape3 set(int A, int B, int C, int D) {
		this.A = A;
		this.B = B;
		this.C = C;
		this.D = D;
		return this;
	}

	/**
	 * Positions the points in the array as an polyhedra with radius 1.
	 * 
	 * Needs to be called before inflating or inscribing.
	 * 
	 * @return itself
	 */
	private Shape3 compute() {
		int numVertices = 0;
		if (getSides() > 0) {
			numVertices = getSides();
		} else {
			// Calculate the number of vertices to resize the ArrayLists
			int layers = (1 + (180 - A) / B);
			int perLayer = 360 / D;
			int specialLayers = (A == 0 ? 1 : 0) + ((180 - A) % B == 0 ? 1 : 0);
			numVertices = specialLayers + perLayer * (layers - specialLayers);
		}
		// Resize the vertex array to match the number of vertices in the shape
		while (getVertices().size() < numVertices) {
			// Fewer vertices? No problem.
			getVertices().add(new Vector3());
		}
		while (getVertices().size() > numVertices) {
			// More vertices? We've got that covered.
			getVertices().remove(0);
		}
		while (computed.size() < numVertices) {
			computed.add(new Vector3());
		}
		while (computed.size() > numVertices) {
			computed.remove(0);
		}
		if (getSides() > 0) {
			compute2D();
		} else {
			compute3D();
		}
		return this;
	}

	/**
	 * Builds a 2D n-gon.
	 */
	private void compute2D() {
		double deltaTheta = 2 * Math.PI / this.getSides();
		while (getLines().size() > computed.size()) {
			getLines().remove(0);
		}
		while (getLines().size() < computed.size()) {
			getLines().add(new Line());
		}
		for (int i = 0; i < computed.size(); i++) {
			computed.get(i).set(1, 0, 0).rotate(i * deltaTheta);
			getLines().get(i).set(i, (i + 1) % computed.size());
		}
	}

	/**
	 * Builds a 3D regular polyhedron.
	 */
	private void compute3D() {
		// Calculate the number of vertices to resize the ArrayLists
		int layers = (1 + (180 - A) / B);
		int perLayer = 360 / D;
		int vertex = 0;
		int verticesInLastLayer = 0;
		int line = 0;
		for (int i = 0; i < layers; i++) {
			int phiDegrees = A + B * i;
			boolean specialLayer = (phiDegrees == 180 || phiDegrees == 0);
			// Make the vertices for this layer
			int startOfLayer = vertex;
			for (int j = 0; ((j < perLayer) && !specialLayer)
					|| ((j < 1) && specialLayer); j++) {
				int thetaDegrees = i * C + j * D;
				double phi = Math.PI * (phiDegrees) / 180, theta = Math.PI
						* (thetaDegrees + 45) / 180;
				double ps = Math.sin(phi), pc = Math.cos(phi), tc = Math
						.cos(theta), ts = Math.sin(theta);
				computed.get(vertex).set(ps * tc, pc, ps * ts);
				vertex++;
			}
			int verticesInLayer = specialLayer ? 1 : perLayer;
			// Connect 'em up to form lines!
			if (verticesInLayer > 1) {
				// Form an edge loop
				for (int j = 0; j < verticesInLayer; j++) {
					line = setNextLine(line, startOfLayer + j, startOfLayer
							+ (j + 1) % verticesInLayer);
				}
			}
			if (phiDegrees == 180) {
				// Form a bottom vertex fan
				for (int j = 0; j < verticesInLastLayer; j++) {
					line = setNextLine(line, startOfLayer - verticesInLastLayer
							+ j, startOfLayer);
				}
			} else if (verticesInLastLayer > 0) {
				// Connect upwards
				for (int j = 0; j < verticesInLayer; j++) {
					line = setNextLine(line, startOfLayer - verticesInLastLayer
							+ j % verticesInLastLayer, startOfLayer + j);
					if (C > 0 && verticesInLastLayer > 1) {
						// Form upwards triangles
						line = setNextLine(line, startOfLayer
								- verticesInLastLayer + (j + 1)
								% verticesInLastLayer, startOfLayer + j);
					}
				}
			}
			while (getLines().size() > line) {
				getLines().remove(getLines().size() - 1);
			}
			verticesInLastLayer = specialLayer ? 1 : perLayer;
		}
	}

	/**
	 * Sets the next line in the line array, creating a new object if necessary.
	 * 
	 * @param line
	 * @param a
	 * @param b
	 * @return
	 */
	private int setNextLine(int line, int a, int b) {
		if (line >= getLines().size() - 1) {
			getLines().add(new Line());
		}
		getLines().get(line).set(a, b);
		return line + 1;
	}

	/**
	 * Recalculates the position of each vertex based on its rotation.
	 * 
	 * @return itself
	 */
	private Shape3 rotate() {
		Shape3.tempQ.setFromRotation(this.rotation);
		for (int i = 0; i < getVertices().size(); i++) {
			getVertices().get(i).copy(computed.get(i)).rotate(Shape3.tempQ);
		}
		return this;
	}

	/**
	 * Transforms (inflates or inscribes) the shape in a given container based
	 * on its mode, then projects it given a projector.
	 * 
	 * @param projector
	 * @param containerSize
	 * @return itself
	 */
	public Shape3 transform(Projector projector, Vector3 containerSize) {
		if (this.needsRecomputation) {
			this.compute();
		}
		this.rotate();
		if (this.mode == Shape3.INFLATE) {
			this.inflate(containerSize);
		} else if (this.mode == Shape3.INSCRIBE) {
			this.inscribe(containerSize);
		}
		this.center(containerSize);
		this.project(projector);
		return this;
	}

	/**
	 * Moves each vertex to the edge of the shape container based on the
	 * respective calculated distance to the edge of each vertex.
	 * 
	 * @param containerSize
	 * @return itself
	 */
	protected Shape3 inflate(Vector3 containerSize) {
		for (int i = 0; i < getVertices().size(); i++) {
			Vector3 currentVertex = this.getVertices().get(i);
			currentVertex.multiplyScalar(this.getToSide(currentVertex,
					containerSize));
		}
		return this;
	}

	/**
	 * Inscribes the shape inside the container by finding the minimum distance
	 * from any vertex to the edge and setting the radius of the polygon
	 * accordingly.
	 * 
	 * @param containerSize
	 * @return itself
	 */
	protected Shape3 inscribe(Vector3 containerSize) {
		// Find the minimum distance to a wall from any vertex
		double minimumRadius = Double.POSITIVE_INFINITY;
		for (int i = 0; i < getVertices().size(); i++) {
			minimumRadius = Math.min(minimumRadius,
					this.getToSide(this.getVertices().get(i), containerSize));
		}
		// Set the radius of the polygon to that minimum distance
		for (int i = 0; i < getVertices().size(); i++) {
			this.getVertices().get(i).multiplyScalar(minimumRadius);
		}
		return this;
	}

	/**
	 * Centers the shape in a container.
	 * 
	 * @param containerSize
	 * @return itself
	 */
	private Shape3 center(Vector3 containerSize) {
		tempV3.set(-0.5).add(this.center).multiply(containerSize);
		for (int i = 0; i < getVertices().size(); i++) {
			getVertices().get(i).add(tempV3);
		}
		return this;
	}

	/**
	 * Gets the distance from a vector to a side of the container.
	 * 
	 * @param vector
	 * @param containerSize
	 * @return
	 */
	public double getToSide(Vector3 vector, Vector3 containerSize) {
		double distanceToXPoint = Double.POSITIVE_INFINITY, distanceToYPoint = Double.POSITIVE_INFINITY, distanceToZPoint = Double.POSITIVE_INFINITY;
		if (vector.getX() != 0) {
			// Find the x distance to the left or right wall.
			double distanceToX = vector.getX() > 0 ? containerSize.getX()
					* (1 - this.center.getX()) : this.center.getX()
					* containerSize.getX();
			// Find the total distance to projected point on the left or right
			// wall.

			distanceToXPoint = distanceToX / Math.abs(vector.getX());
		}
		if (vector.getY() != 0) {
			// Find the y distance to the ceiling or floor.
			double distanceToY = vector.getY() > 0 ? (1 - this.center.getY())
					* containerSize.getY() : this.center.getY()
					* containerSize.getY();
			// Find the total distance to projected point on the ceiling or
			// floor.
			distanceToYPoint = distanceToY / Math.abs(vector.getY());
		}
		if (vector.getZ() != 0) {
			// Find the z distance to the front or back.
			double distanceToZ = vector.getZ() > 0 ? (1 - this.center.getZ())
					* containerSize.getZ() : this.center.getZ()
					* containerSize.getZ();
			// Find the total distance to projected point on the front or back
			// wall.
			distanceToZPoint = distanceToZ / Math.abs(vector.getZ());
		}
		// Return whichever distance is closer, wall or ceiling.
		return Math.min(distanceToXPoint,
				Math.min(distanceToYPoint, distanceToZPoint));
	}

	/**
	 * Advance the rotation of the shape based on the calculated elapsed time.
	 * 
	 * @param deltaTime
	 *            the time that has elapsed since the previous step
	 * @return
	 */
	public Shape3 step(double deltaTime) {
		this.rotation.add(this.rotationSpeed * deltaTime);
		return this;
	}

	/**
	 * @return the vertices
	 */
	public ArrayList<Vector3> get3DVertices() {
		return getVertices();
	}

	/**
	 * @return the rotationSpeed
	 */
	public double getRotationSpeed() {
		return rotationSpeed;
	}

	/**
	 * @param rotationSpeed
	 *            the rotationSpeed to set
	 */
	public void setRotationSpeed(double rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	/**
	 * @return the rotation
	 */
	public Rotation get3DRotation() {
		return this.rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(Rotation rotation) {
		this.rotation.copy(rotation);
	}

	/**
	 * @return the center
	 */
	public Vector3 getCenter() {
		return center;
	}

	/**
	 * @param center
	 *            the center to set
	 */
	public void setCenter(Vector3 center) {
		this.center = center;
	}

	/**
	 * Returns the vertices that form the shape.
	 */
	public String toString() {
		return this.getVertices().toString();
	}

	/**
	 * @return the mode
	 */
	public byte getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(byte mode) {
		this.mode = mode;
	}

	/**
	 * @return the a
	 */
	public int getA() {
		return A;
	}

	/**
	 * @param a
	 *            the a to set
	 */
	public void setA(int a) {
		A = a;
		needsRecomputation = true;
	}

	/**
	 * @return the b
	 */
	public int getB() {
		return B;
	}

	/**
	 * @param b
	 *            the b to set
	 */
	public void setB(int b) {
		B = b;
		needsRecomputation = true;
	}

	/**
	 * @return the c
	 */
	public int getC() {
		return C;
	}

	/**
	 * @param c
	 *            the c to set
	 */
	public void setC(int c) {
		C = c;
		needsRecomputation = true;
	}

	/**
	 * @return the d
	 */
	public int getD() {
		return D;
	}

	/**
	 * @param d
	 *            the d to set
	 */
	public void setD(int d) {
		D = d;
		needsRecomputation = true;
	}

	/**
	 * @return the sides
	 */
	public int getSides() {
		return sides;
	}

	/**
	 * @param sides
	 *            the sides to set
	 */
	public void setSides(int sides) {
		this.sides = sides;
		this.needsRecomputation = true;
	}

}
