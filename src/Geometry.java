import java.util.ArrayList;

/**
 * Geometry.java
 * 
 * An abstract class to define a shape's geometry.
 * 
 * Written Jan 30, 2014.
 * 
 * @author William Wu
 * 
 */
public abstract class Geometry {
	private ArrayList<Vector3> vertices = new ArrayList<Vector3>();
	private ArrayList<Line> lines = new ArrayList<Line>();

	/**
	 * Rebuilds the vertices and/or lines.
	 */
	public abstract void rebuild();

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
}
