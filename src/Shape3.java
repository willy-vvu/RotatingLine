import java.util.ArrayList;

/**
 * Shape3.java
 * 
 * A 3 dimensional polyhedra that computes and transforms its vertices.
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
public class Shape3 extends Shape2 {
	private ArrayList<Vector3> computed = new ArrayList<Vector3>(),
			vertices = new ArrayList<Vector3>();
	private ArrayList<Face> faces = new ArrayList<Face>();
	private double rotationSpeed = 0;
	private Rotation rotation = new Rotation();
	private static Quaternion tempQ = new Quaternion();
	private int mode = Shape2.INFLATE;
	private int A = 0, B = 0, C = 0, D = 0;
	public static final int TETRAHEDRON = 0;
	public static final int CUBE = 1;
	public static final int OCTAHEDRON = 2;
	public static final int ICOSAHEDRON = 3;

	// Note that the center is between 0 and 1, relative to the container size.
	private Vector3 center = new Vector3();
	private Vector3 containerSize = null;
	private static Vector3 temp = new Vector3();
	private boolean needsRecomputation = true;

	/**
	 * Tests out the Shape2 class and its methods.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Shape3 s = new Shape3(Shape3.TETRAHEDRON);
		s.setCenter(new Vector3(0.5, 0.5, 0.5));
		s.setContainerSize(new Vector3(100, 100, 100));
		s.compute();
		System.out.println(s.computed);
	}

	/**
	 * Creates a new shape.
	 */
	public Shape3() {
	}

	/**
	 * Create a new shape from a given preset.
	 * 
	 * @param preset
	 */
	public Shape3(int preset) {
		if (preset == Shape3.TETRAHEDRON) {
			this.set(0, 120, 0, 120);
		} else if (preset == Shape3.CUBE) {
			this.set(45, 90, 0, 90);
		} else if (preset == Shape3.OCTAHEDRON) {
			this.set(0, 90, 0, 90);
		} else if (preset == Shape3.ICOSAHEDRON) {
			this.set(0, 60, 36, 72);
		}
	}

	/**
	 * Create a new shape from a given preset and rotation speed.
	 * 
	 * @param preset
	 * @param rotationSpeed
	 */
	public Shape3(int preset, double rotationSpeed) {
		this(preset);
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
		// Calculate the number of vertices to resize the ArrayLists
		int layers = (1 + (180 - A) / B);
		int perLayer = 360 / D;
		int specialLayers = (A == 0 ? 1 : 0) + ((180 - A) % B == 0 ? 1 : 0);
		int numVertices = specialLayers + perLayer * (layers - specialLayers);
		// Resize the vertex array to match the number of vertices in the shape
		while (vertices.size() < numVertices) {
			// Fewer vertices? No problem.
			vertices.add(new Vector3());
		}
		while (vertices.size() > numVertices) {
			// More vertices? We've got that covered.
			vertices.remove(0);
		}
		while (computed.size() < numVertices) {
			computed.add(new Vector3());
		}
		while (computed.size() > numVertices) {
			computed.remove(0);
		}
		faces.clear();
		int vertex = 0;
		int verticesInLastLayer = A == 0 ? 1 : 0;
		for (int i = 0; i < layers; i++) {
			int phiDegrees = A + B * i;
			boolean specialLayer = (phiDegrees == 180 || phiDegrees == 0);
			// Make the vertices for this layer
			int startOfLayer = vertex;
			for (int j = 0; j < perLayer && !specialLayer || (j < 1)
					&& specialLayer; j++) {
				int thetaDegrees = i * C + j * D;
				double c = Math.cos(Math.PI * (90 + phiDegrees) / 180), d = Math
						.cos(Math.PI * (thetaDegrees) / 180);
				computed.get(vertex).set(c * d, c * Math.sqrt(1 - d * d),
						Math.sqrt(1 - c * c));
				vertex++;
			}
			int verticesInLayer = phiDegrees == 180 ? 1 : perLayer;
			// Connect 'em up to form faces!
			for (int j = 0; j < perLayer; j++) {
				int a, b, c;

				if (verticesInLayer > 1 && verticesInLastLayer > 0) {
					// Previous: a *
					// Current:: b c
					a = startOfLayer - verticesInLastLayer + j
							% verticesInLastLayer;
					b = startOfLayer + j % verticesInLayer;
					c = startOfLayer + ((j + 1) % verticesInLayer);
					faces.add(new Face(a, b, c));
				}
				if (verticesInLastLayer > 1) {
					// Previous: a c
					// Current:: * b
					a = startOfLayer - verticesInLastLayer + j
							% verticesInLastLayer;
					b = startOfLayer + j;
					c = startOfLayer - verticesInLastLayer
							+ ((j + 1) % verticesInLastLayer);
					faces.add(new Face(a, b, c));
				}
			}
			verticesInLastLayer = perLayer;
		}
		return this;
	}

	/**
	 * Recalculates the position of each vertex based on a specified rotation.
	 * 
	 * @return
	 */
	private Shape3 rotate() {
		Shape3.tempQ.setFromRotation(this.rotation);
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).copy(computed.get(i)).rotate(Shape3.tempQ);
		}
		return this;
	}

	/**
	 * Transforms (inflates or inscribes) the shape based on its mode
	 * 
	 * @return itself
	 */
	public Shape3 transform() {
		if (this.mode == Shape2.INFLATE) {
			this.inflate();
		} else if (this.mode == Shape2.INSCRIBE) {
			this.inscribe();
		}
		return this;
	}

	/**
	 * Moves each vertex to the edge of the shape container based on the
	 * respective calculated distance to the edge of each vertex.
	 * 
	 * @return itself
	 */
	public Shape3 inflate() {
		if (needsRecomputation) {
			this.compute();
		}
		this.rotate();
		for (int i = 0; i < vertices.size(); i++) {
			Vector2 currentVertex = this.vertices.get(i);
			currentVertex.multiplyScalar(this.getToSide(currentVertex));
		}
		this.center();
		return this;
	}

	/**
	 * Centers the shape in the container.
	 * 
	 * @return itself
	 */
	private Shape3 center() {
		// Get the offset to the center
		Shape3.temp.copy(this.center).multiply(this.containerSize);
		// Add to each vertex
		for (int i = 0; i < vertices.size(); i++) {
			this.vertices.get(i).add(Shape3.temp);
		}
		return this;
	}

	/**
	 * Inscribes the shape inside the container by finding the minimum distance
	 * from any vertex to the edge and setting the radius of the polygon
	 * accordingly.
	 * 
	 * @return itself
	 */
	public Shape3 inscribe() {
		if (needsRecomputation) {
			this.compute();
		}
		this.rotate();
		// Find the minimum distance to a wall from any vertex
		double minimumRadius = Double.POSITIVE_INFINITY;
		for (int i = 0; i < vertices.size(); i++) {
			minimumRadius = Math.min(minimumRadius,
					this.getToSide(this.vertices.get(i)));
		}
		// Set the radius of the polygon to that minimum distance
		for (int i = 0; i < vertices.size(); i++) {
			this.vertices.get(i).multiplyScalar(minimumRadius);
		}
		this.center();
		return this;
	}

	/**
	 * Gets the distance from a vector to a side of the container.
	 * 
	 * @param vector
	 * @return
	 */
	public double getToSide(Vector3 vector) {
		double distanceToXPoint = Double.POSITIVE_INFINITY, distanceToYPoint = Double.POSITIVE_INFINITY, distanceToZPoint = Double.POSITIVE_INFINITY, distanceMultiplier = 0;
		if (vector.getX() != 0) {
			// Find the x distance to the left or right wall.
			double distanceToX = vector.getX() > 0 ? this.containerSize.getX()
					* (1 - this.center.getX()) : this.center.getX()
					* this.containerSize.getX();
			// Find the total distance to projected point on the left or right
			// wall.
			distanceMultiplier = distanceToX / vector.getX();
			distanceToXPoint = Vector3.hypotenuse(distanceToX, vector.getY()
					* distanceMultiplier, vector.getZ() * distanceMultiplier);
		}
		if (vector.getY() != 0) {
			// Find the y distance to the ceiling or floor.
			double distanceToY = vector.getY() > 0 ? (1 - this.center.getY())
					* this.containerSize.getY() : this.center.getY()
					* this.containerSize.getY();
			// Find the total distance to projected point on the ceiling or
			// floor.
			distanceMultiplier = distanceToY / vector.getY();
			distanceToYPoint = Vector3.hypotenuse(distanceToY, vector.getX()
					* distanceMultiplier, vector.getZ() * distanceMultiplier);
		}
		if (vector.getZ() != 0) {
			// Find the z distance to the front or back.
			double distanceToZ = vector.getZ() > 0 ? (1 - this.center.getZ())
					* this.containerSize.getZ() : this.center.getZ()
					* this.containerSize.getZ();
			// Find the total distance to projected point on the front or back
			// wall.
			distanceMultiplier = distanceToZ / vector.getZ();
			distanceToZPoint = Vector3.hypotenuse(distanceToZ, vector.getX()
					* distanceMultiplier, vector.getY() * distanceMultiplier);
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
		return vertices;
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
		needsRecomputation = true;
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
		needsRecomputation = true;
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
		needsRecomputation = true;
	}

	/**
	 * @return the containerSize
	 */
	public Vector3 getContainerSize() {
		return containerSize;
	}

	/**
	 * @param containerSize
	 *            the containerSize to set
	 */
	public void setContainerSize(Vector3 containerSize) {
		this.containerSize = containerSize;
		needsRecomputation = true;
	}

	/**
	 * Returns the vertices that form the shape.
	 */
	public String toString() {
		return this.vertices.toString();
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
		needsRecomputation = true;
	}

	/**
	 * @return the faces
	 */
	public ArrayList<Face> getFaces() {
		return faces;
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
}
