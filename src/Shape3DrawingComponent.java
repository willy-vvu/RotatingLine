import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Shape3DrawingComponent.java
 * 
 * A component to draw 3D shapes
 * 
 * Written Jan 24, 2014.
 * 
 * @author William Wu
 * 
 */
public class Shape3DrawingComponent extends JComponent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create frame
		JFrame frame = new JFrame();
		// Center and size the frame
		frame.setSize(800, 800);
		frame.setLocation(10, 10);
		// Set defaults
		frame.setTitle("Rotating Line Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add the Drawing component
		JComponent drawingComponent = new Shape3DrawingComponent();
		frame.add(drawingComponent);
		// Make it visible
		frame.setVisible(true);
	}

	private static final long serialVersionUID = 1L;
	private ArrayList<Shape3> shapes = new ArrayList<Shape3>();
	private static ArrayList<Vector2> projected = new ArrayList<Vector2>();
	private Vector2 containerSize = new Vector2();
	private Vector3 boxSize = new Vector3(10, 10, 10);
	private Projector projector = new Projector(45, new Vector3(0, 0, -30));
	private static final Color backgroundColor = new Color(255, 255, 255, 0);
	private static final Color foregroundColor = new Color(0x000000);
	public static Line2D.Double sharedLine = new Line2D.Double();
	private long lastTick = 0;

	public Shape3DrawingComponent() {
		Shape3 shape = new Shape3(Shape3.ICOSAHEDRON, 1);
		shape.get3DRotation().set(1, 1, 1).normalize();
		shape.setMode(Shape3.INSCRIBE);
		shapes.add(shape);
	}

	public void paintComponent(Graphics graphics) {
		// Extract Graphics2D
		Graphics2D g = (Graphics2D) graphics;
		containerSize.set(this.getWidth(), this.getHeight());
		// Draw background
		g.setColor(backgroundColor);
		g.fill(this.getBounds());
		g.setColor(foregroundColor);

		long currentTime = new Date().getTime();
		double timeElapsed = (lastTick == 0 ? 0 : (currentTime - lastTick)) * 0.001;
		lastTick = currentTime;
		projector.setScreenSize(containerSize);
		// Compute, draw and advance all shapes
		for (int i = 0; i < shapes.size(); i++) {
			Shape3 currentShape = shapes.get(i);
			currentShape.setContainerSize(boxSize);
			currentShape.transform();
			this.drawShape(g, currentShape);
			currentShape.step(timeElapsed);
		}

		this.repaint();

	}

	/**
	 * draws a single shape to a given graphics context
	 * 
	 * @param g
	 * @param shape
	 */
	private void drawShape(Graphics2D g, Shape3 shape) {
		projector.project(shape.get3DVertices(), projected);
		//System.out.println(shape.get3DVertices());
		// ArrayList<Face> faces = shape.getFaces();
		// for (int i = 0; i < faces.size(); i++) {
		// for (int j = 0; j < 3; j++) {
		// Vector2 fromVertex = projected.get(faces.get(i).getAt(j));
		// Vector2 toVertex = projected.get(faces.get(i)
		// .getAt((j + 1) % 3));
		// sharedLine.setLine(fromVertex.getX(), fromVertex.getY(),
		// toVertex.getX(), toVertex.getY());
		// g.draw(sharedLine);
		// }
		// }
		for (int i = 0; i < projected.size(); i++) {
			g.drawOval((int) projected.get(i).getX(), (int) projected.get(i)
					.getY(), 5, 5);
		}
	}
}
