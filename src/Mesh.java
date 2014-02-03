import java.util.ArrayList;

/**
 * Mesh.java
 * 
 * A class that holds an ArrayList of vertices and an ArrayList of lines that
 * constitute a 3D mesh.
 * 
 * Also temporarily holds its own projected points.
 * 
 * Written Jan 30, 2014.
 * 
 * @author William Wu
 * 
 */
public class Mesh {
	private ArrayList<Vector3> vertices = new ArrayList<Vector3>();
	private ArrayList<Line> lines = new ArrayList<Line>();
	private ArrayList<Vector2> projected = new ArrayList<Vector2>();

	/**
	 * Creates an empty mesh.
	 */
	public Mesh() {
	}

	/**
	 * Returns the minimum distance from a given point to any of the shape's
	 * edges, using the formula:
	 * 
	 * dist = |(a-p)-((a-p) . n) n|
	 * 
	 * Where the line is the parametric vector equation a + n t n is a unit
	 * vector defining the line direction p is the point in consideration
	 * 
	 * @param point
	 * @return
	 */
	public double edgeToPoint(Vector2 point) {
		double minDist = Double.POSITIVE_INFINITY;
		for (int i = 0; i < getLines().size(); i++) {
			Vector2 pointA = getProjected().get(getLines().get(i).getA()), pointB = getProjected()
					.get(getLines().get(i).getB());
			// // Compute and save a-p
			// tempV2.copy(pointA).subtract(point);
			// // Compute n
			// tempV2_2.copy(pointB).subtract(pointA).normalize();
			// // Compute ((a-p) . n) n
			// tempV2_2.multiplyScalar(tempV2_2.dot(tempV2));
			// minDistSquared = Math.min(minDistSquared,
			// tempV2.subtract(tempV2_2)
			// .lengthSquared());
			minDist = Math.min(minDist,
					pointA.distanceTo(point) + pointB.distanceTo(point)
							- pointA.distanceTo(pointB));
		}
		return Math.sqrt(minDist);
	}

	/**
	 * Creates a mesh based on a given geometry builder.
	 */
	public Mesh(Geometry geometry) {
		buildFrom(geometry);
	}

	/**
	 * Builds the mesh based on an existing geometry.
	 * 
	 * @param geometry
	 * @return itself
	 */
	public Mesh buildFrom(Geometry geometry) {
		vertices = geometry.getVertices();
		lines = geometry.getLines();
		return this;
	}

	/**
	 * Projects the vertices of the Mesh onto 2D.
	 * 
	 * @return itself
	 */
	public Mesh project(Projector projector) {
		projector.project(vertices, projected);
		return this;
	}

	/**
	 * @return the vertices
	 */
	public ArrayList<Vector3> getVertices() {
		return vertices;
	}

	/**
	 * @param vertices
	 *            the vertices to set
	 */
	public void setVertices(ArrayList<Vector3> vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the lines
	 */
	public ArrayList<Line> getLines() {
		return lines;
	}

	/**
	 * @param lines
	 *            the lines to set
	 */
	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	/**
	 * @return the projected
	 */
	public ArrayList<Vector2> getProjected() {
		return projected;
	}

	/**
	 * @param projected
	 *            the projected to set
	 */
	public void setProjected(ArrayList<Vector2> projected) {
		this.projected = projected;
	}

}
