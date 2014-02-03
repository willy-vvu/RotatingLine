
/**
 * Geometry.java
 * 
 * An abstract class to define a self-building mesh.
 * 
 * Written Jan 30, 2014.
 * 
 * @author William Wu
 * 
 */
public abstract class Geometry extends Mesh{
	/**
	 * Rebuilds the vertices and/or lines.
	 */
	public abstract void rebuild();
}
