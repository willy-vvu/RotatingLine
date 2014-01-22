import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Shape.java
 * 
 * A shape that contains and manipulates a
 * 
 * Written Jan 22, 2014.
 * 
 * @author William Wu
 * 
 */
public class Shape {
	private ArrayList<Vector2> vertices = new ArrayList<Vector2>();
	private double rotationSpeed = 0;
	private double rotation = 0;
	private int sides = 2;
	private Vector2 center = new Vector2();
	private Vector2 containerSize = null;

	/**
	 * Creates a new two sided shape, or line.
	 */
	public Shape() {
		// The setter does stuff.
		this.setSides(2);
	}

	/**
	 * Create a new shape with a given number of sides.
	 * 
	 * @param sides
	 */
	public Shape(int sides) {
		this.setSides(sides);
	}

	/**
	 * Positions the points in the array as an n-gon with radius 1.
	 * 
	 * Needs to be called before inflating or inscribing.
	 * 
	 * @return itself
	 */
	public Shape compute() {
		// Find the angle between each vertex and the center.
		double deltaTheta = 2 * Math.PI / this.sides;
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(0).set(1, 0).rotate(this.rotation + i * deltaTheta);
		}
		return this;
	}

	/**
	 * Moves each vertex to the edge of the shape container based on the
	 * respective calculated distance to the edge of each vertex.
	 * 
	 * @return itself
	 */
	public Shape inflate() {
		for (int i = 0; i < vertices.size(); i++) {
			Vector2 currentVertex = this.vertices.get(i);
			currentVertex.multiplyScalar(this.toSide(currentVertex));
		}
		return this;
	}

	/**
	 * Inscribes the shape inside the container by finding the minimum distance
	 * from any vertex to the edge and setting the radius of the polygon
	 * accordingly.
	 * 
	 * @return
	 */
	public Shape inscribe() {
		// Find the minimum distance to a wall from any vertex
		double minimumRadius = Double.POSITIVE_INFINITY;
		for (int i = 0; i < vertices.size(); i++) {
			minimumRadius = Math.min(minimumRadius,
					this.toSide(this.vertices.get(i)));
		}
		// Set the radius of the polygon to that minimum distance
		for (int i = 0; i < vertices.size(); i++) {
			this.vertices.get(i).multiplyScalar(minimumRadius);
		}
		return this;
	}

	/**
	 * Gets the distance from a vector to a side of the container.
	 * 
	 * @param vector
	 * @return
	 */
	public double toSide(Vector2 vector) {
		double distanceToWallPoint = Double.POSITIVE_INFINITY, distanceToFloorPoint = Double.POSITIVE_INFINITY;
		if (vector.x != 0) {
			// Find the x distance to the left or right wall.
			double distanceToWall = vector.x > 0 ? this.containerSize.x
					- this.center.x : this.center.x;
			// Find the total distance to projected point on the left or right
			// wall.
			distanceToWallPoint = Vector2.hypotenuse(distanceToWall, vector.y
					* distanceToWall / vector.x);
		}
		if (vector.y != 0) {
			// Find the y distance to the ceiling or floor.
			double distanceToCeiling = vector.y > 0 ? this.containerSize.y
					- this.center.y : this.center.y;
			// Find the total distance to projected point on the ceiling or
			// floor.
			distanceToFloorPoint = Vector2.hypotenuse(distanceToCeiling,
					vector.x * distanceToCeiling / vector.y);
		}
		// Return whichever distance is closer, wall or ceiling.
		return Math.min(distanceToWallPoint, distanceToFloorPoint);
	}

	/**
	 * Draws the shape in a given graphics context.
	 * 
	 * @param graphics
	 * @return
	 */
	public Shape draw(Graphics2D graphics) {
		return this;
	}

	/**
	 * Advance the rotation of the shape based on the calculated elapsed time.
	 * 
	 * @param deltaTime
	 *            the time that has elapsed since the previous step
	 * @return
	 */
	public Shape step(double deltaTime) {
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
		// Resize the vertex array to match the number of sides in the shape
		while (vertices.size() < this.sides) {
			// Fewer points than sides? No problem.
			vertices.add(new Vector2());
		}
		while (vertices.size() > this.sides) {
			// More vertices than sides? We've got that covered.
			vertices.remove(0);
		}
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
}
