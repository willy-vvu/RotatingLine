import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
	private Vector2 containerSize = new Vector2();
	private Vector3 boxSize = new Vector3(10, 10, 5);
	private Vector3 view = new Vector3(0.001, 0.001, -30);
	private Vector3 viewVelocity = new Vector3(0, 0, 0);
	private Vector2 mousePosition = new Vector2();
	private boolean mousePressed = false;
	private long mouseLastMoved = 0;
	private Projector projector = new Projector(45);
	private static final Color backgroundColor = new Color(255, 255, 255, 0);
	private static final Color foregroundColor = new Color(0x000000);
	private static final Line2D.Double sharedLine = new Line2D.Double();
	private static final Quaternion tempQ = new Quaternion();
	private static final Quaternion tempQ_2 = new Quaternion();
	private static final Vector3 yAxis = new Vector3(0, 1, 0);
	private static final Vector3 xAxis = new Vector3(1, 0, 0);
	private static final Vector2 tempV2 = new Vector2();
	private long lastTick = 0;
	private static Vector3 tempV3 = new Vector3();
	private CubeGeometry boundingCubeGeo = new CubeGeometry(boxSize);
	private Mesh boundingCube = new Mesh(boundingCubeGeo);

	public Shape3DrawingComponent() {
		Shape3 shape = new Shape3(6, 1);
		shape.getCenter().set(0.5, 0.5, 0.5);
		// shape.get3DRotation().set(1, 1, 1).normalize();
		shape.setMode(Shape3.INSCRIBE);
		shapes.add(shape);

		Shape3 shape2 = new Shape3(Shape3.ICOSAHEDRON, 1);
		shape2.get3DRotation().set(1, -1, -1).normalize();
		shape2.setMode(Shape3.INSCRIBE);
		shapes.add(shape2);

		// Listen for mouse clicks
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				mousePressed = false;
				// Stop the vertical spin when it is shallow.
				if (Math.abs(viewVelocity.getY() * 3) < Math.abs(viewVelocity
						.getX()) && Math.abs(viewVelocity.getY()) < 0.01) {
					viewVelocity.setY(0);
				}
				// Stop the spin if you hold the mouse down or aren't moving the
				// mouse much at all.
				if (new Date().getTime() - mouseLastMoved > 250
						|| viewVelocity.length() < 0.01) {
					viewVelocity.set(0, 0, 0);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mousePressed = true;
				viewVelocity.set(0, 0, 0);
				mousePosition.set(e.getPoint().getX(), e.getPoint().getY());
				mouseLastMoved = new Date().getTime();
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				tempV2.set(e.getX(), e.getY());
				Shape3 selection = null;
				double minDistToSelected = Double.POSITIVE_INFINITY;
				for (int i = 0; i < shapes.size(); i++) {
					Shape3 currentShape = shapes.get(i);
					double currentDist = currentShape.edgeToPoint(tempV2);
					if (currentDist < minDistToSelected) {
						minDistToSelected = currentDist;
						selection = currentShape;
					}
				}
				System.out.println(selection);
			}
		});
		// Listen for and handle mouse motion
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (mousePressed) {
					double newX = e.getPoint().getX(), newY = e.getPoint()
							.getY();
					tempV3.set(newX - mousePosition.getX(),
							mousePosition.getY() - newY).multiplyScalar(
							0.01 * 0.2);
					viewVelocity.multiplyScalar(0.8).add(tempV3);
					view.add(viewVelocity);
					mousePosition.set(newX, newY);
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				this.mouseMoved(e);
			}
		});

		// Listen for mouse scrolling
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				view.setZ(Math.min(Math.max(
						view.getZ() - e.getPreciseWheelRotation() * 2, -50),
						-10));
			}
		});

	}

	public void paintComponent(Graphics graphics) {
		// Extract Graphics2D
		Graphics2D g = (Graphics2D) graphics;
		containerSize.set(this.getWidth(), this.getHeight());

		// Draw background
		g.setColor(backgroundColor);
		g.fill(this.getBounds());
		g.setColor(foregroundColor);

		// Check the time
		long currentTime = new Date().getTime();
		double timeElapsed = (lastTick == 0 ? 0 : (currentTime - lastTick)) * 0.001;
		lastTick = currentTime;

		// Update projector
		if (!mousePressed) {
			tempV3.copy(viewVelocity).multiplyScalar(50 * timeElapsed);
			view.add(tempV3);
		}
		viewVelocity.multiplyScalar(0.999);
		view.setY(Math.min(Math.max(-Math.PI / 2, view.getY()), Math.PI / 2));
		projector.setScreenSize(containerSize);
		tempQ.setAxisAngle(view.getX(), yAxis);
		tempQ_2.setAxisAngle(view.getY(), xAxis);
		tempQ.multiply(tempQ_2);
		projector.getRotation().setFromQuaternion(tempQ);
		projector.getPosition().set(0, 0, view.getZ()).rotate(tempQ);

		// Draw bounding box
		boundingCube.project(projector);
		this.drawLines(g, boundingCube);

		// Compute, draw and advance all shapes
		for (int i = 0; i < shapes.size(); i++) {
			Shape3 currentShape = shapes.get(i);
			currentShape.transform(projector,boxSize);
			this.drawLines(g, currentShape);
			this.drawVertices(g, currentShape);
			currentShape.step(timeElapsed);
		}

		this.repaint();

	}

	/**
	 * Draws an {@link ArrayList} of 3D vertices and an {@link ArrayList} of
	 * {@link Line}s joining them to a given graphics context.
	 * 
	 * @param g
	 * @param mesh
	 */
	private void drawLines(Graphics2D g, Mesh mesh) {
		for (int i = 0; i < mesh.getLines().size(); i++) {
			Line currentLine = mesh.getLines().get(i);
			Vector2 fromVertex = mesh.getProjected().get(currentLine.getA());
			Vector2 toVertex = mesh.getProjected().get(currentLine.getB());
			if (fromVertex.getX() == fromVertex.getX()
					&& toVertex.getX() == toVertex.getX()) {
				sharedLine.setLine(fromVertex.getX(), fromVertex.getY(),
						toVertex.getX(), toVertex.getY());
				g.draw(sharedLine);
			}
		}
	}

	/**
	 * Draws {@link ArrayList} of 3D vertices as points to a given graphics
	 * context.
	 * 
	 * @param g
	 * @param vertices
	 */
	private void drawVertices(Graphics2D g, Mesh mesh) {
		for (int i = 0; i < mesh.getProjected().size(); i++) {
			Vector2 vertex = mesh.getProjected().get(i);
			if (vertex.getX() == vertex.getX()) {
				g.drawOval((int) vertex.getX() - 3, (int) vertex.getY() - 3, 6,
						6);
			}
		}
	}
}
