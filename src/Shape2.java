import java.util.ArrayList;

/**
 * Shape2.java
 * 
 * A 2 dimensional shape (n-gon) that computes and transforms its vertices.
 * 
 * Written Jan 22, 2014.
 * 
 * @author William Wu
 * 
 */
public class Shape2 {
	private ArrayList<Vector2> vertices = new ArrayList<Vector2>();
	private double rotationSpeed = 0;
	private double rotation = 0;
	private int sides = 2;
	// Note that the center is between 0 and 1, relative to the container size.
	private Vector2 center = new Vector2();
	private Vector2 containerSize = null;
	private static Vector2 temp = new Vector2();

	/**
	 * Tests out the Shape2 class and its methods.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Shape2 s = new Shape2(3);
		s.setCenter(new Vector2(0.5, 0.5));
		s.setContainerSize(new Vector2(100, 100));
		s.inflate();
		System.out.println(s);
		s.inscribe();
		System.out.println(s);
	}

	/**
	 * Creates a new two sided shape, or line.
	 */
	public Shape2() {
	}

	/**
	 * Create a new shape with a given number of sides.
	 * 
	 * @param sides
	 */
	public Shape2(int sides) {
		this.sides = sides;
	}

	/**
	 * Create a new shape with a given number of sides and a given center
	 * 
	 * @param sides
	 * @param center
	 */
	public Shape2(int sides, Vector2 center) {
		this(sides);
		this.center.copy(center);
	}

	/**
	 * Positions the points in the array as an n-gon with radius 1.
	 * 
	 * Needs to be called before inflating or inscribing.
	 * 
	 * @return itself
	 */
	private Shape2 compute() {
		// Resize the vertex array to match the number of sides in the shape
		while (vertices.size() < this.sides) {
			// Fewer points than sides? No problem.
			vertices.add(new Vector2());
		}
		while (vertices.size() > this.sides) {
			// More vertices than sides? We've got that covered.
			vertices.remove(0);
		}
		// Find the angle between each vertex and the center.
		double deltaTheta = 2 * Math.PI / this.sides;
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).set(1, 0).rotate(this.rotation + i * deltaTheta);
		}
		return this;
	}

	/**
	 * Moves each vertex to the edge of the shape container based on the
	 * respective calculated distance to the edge of each vertex.
	 * 
	 * @return itself
	 */
	public Shape2 inflate() {
		this.compute();
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
	private Shape2 center() {
		// Get the offset to the center
		Shape2.temp.copy(this.center).multiply(this.containerSize);
		// Add to each vertex
		for (int i = 0; i < vertices.size(); i++) {
			this.vertices.get(i).add(Shape2.temp);
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
	public Shape2 inscribe() {
		this.compute();
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
	public double getToSide(Vector2 vector) {
		double distanceToWallPoint = Double.POSITIVE_INFINITY, distanceToFloorPoint = Double.POSITIVE_INFINITY;
		if (vector.getX() != 0) {
			// Find the x distance to the left or right wall.
			double distanceToWall = vector.getX() > 0 ? this.containerSize
					.getX() * (1 - this.center.getX()) : this.center.getX()
					* this.containerSize.getX();
			// Find the total distance to projected point on the left or right
			// wall.
			distanceToWallPoint = Vector2.hypotenuse(distanceToWall,
					vector.getY() * distanceToWall / vector.getX());
		}
		if (vector.getY() != 0) {
			// Find the y distance to the ceiling or floor.
			double distanceToCeiling = vector.getY() > 0 ? (1 - this.center
					.getY()) * this.containerSize.getY() : this.center.getY()
					* this.containerSize.getY();
			// Find the total distance to projected point on the ceiling or
			// floor.
			distanceToFloorPoint = Vector2.hypotenuse(distanceToCeiling,
					vector.getX() * distanceToCeiling / vector.getY());
		}
		// Return whichever distance is closer, wall or ceiling.
		return Math.min(distanceToWallPoint, distanceToFloorPoint);
	}

	/**
	 * Advance the rotation of the shape based on the calculated elapsed time.
	 * 
	 * @param deltaTime
	 *            the time that has elapsed since the previous step
	 * @return
	 */
	public Shape2 step(double deltaTime) {
		this.rotation += this.rotationSpeed * deltaTime;
		return this;
	}

	/**
	 * @return the vertices
	 */
	public ArrayList<Vector2> getVertices() {
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
	}

	/**
	 * @return the rotation
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
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
	}

	/**
	 * @return the center
	 */
	public Vector2 getCenter() {
		return center;
	}

	/**
	 * @param center
	 *            the center to set
	 */
	public void setCenter(Vector2 center) {
		this.center = center;
	}

	/**
	 * @return the containerSize
	 */
	public Vector2 getContainerSize() {
		return containerSize;
	}

	/**
	 * @param containerSize
	 *            the containerSize to set
	 */
	public void setContainerSize(Vector2 containerSize) {
		this.containerSize = containerSize;
	}

	/**
	 * Returns the vertices that form the shape.
	 */
	public String toString() {
		return this.vertices.toString();
	}
}
