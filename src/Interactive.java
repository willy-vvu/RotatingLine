import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Interactive {

	private InteractiveState state = new InteractiveState();
	public static void main(String[] args) {
		new Interactive();

	}

	public Interactive() {
		// Create frame
		JFrame frame = new JFrame();
		// Center and size the frame
		frame.setSize(800, 800);
		frame.setLocation(10, 10);
		// Set defaults
		frame.setTitle("Rotating Line Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add the Drawing component
		ShapeDrawingComponent drawingComponent = new ShapeDrawingComponent();
		frame.add(drawingComponent);
		// Make it visible
		frame.setVisible(true);
		// Make the controls
		JFrame controls = new JFrame();
		// Set title
		controls.setTitle("Rotating Line Controls");
		// Size and position
		controls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controls.setLocation(850, 10);
		controls.setSize(350, 800);
		controls.setVisible(true);
		
		//creating overall control panel
		JPanel overallControls = new JPanel();
		overallControls.setLayout(new GridLayout(2, 1));
		
		
		//creating panel for the buttons
		state.speedSlider = new JSlider(0, 100, 10);
		state.speedSlider.setMajorTickSpacing(50);
		state.speedSlider.setMinorTickSpacing(10);
		state.speedSlider.setPaintTicks(true);
		state.speedSlider.setVisible(true);
		state.speedSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) 
			{
				state.setSpeed(state.speedSlider.getValue());
			}
		});

		overallControls.add(state.speedSlider);
		overallControls.setVisible(true);
		
		

	}
}
class InteractiveState{
	public boolean mouseDown = false;
	public boolean rightMouseDown = false;
	public boolean mouseInFrame = false;
	public JSlider speedSlider = null;
	public JButton toggleTool = null;
	public JLabel rotationLabel = null;
	public int speed = 0;
	
	public void setSpeed(int speed){
		this.speed = (int) Math.max(Math.min(speed, 6), -6);
		this.speedSlider.setValue(this.speed);
	}
}

class ShapeDrawingComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	private ArrayList<Shape2> shapes = new ArrayList<Shape2>();
	private Vector2 containerSize = new Vector2();
	private static final Color backgroundColor = new Color(255, 255, 255, 0);
	private static final Color foregroundColor = new Color(0x000000);
	private static Line2D.Double sharedLine = new Line2D.Double();
	private long lastTick = 0;

	public ShapeDrawingComponent() {
		shapes.add(new Shape2(5, new Vector2(0.5, 0.5), 1, Shape2.INFLATE));
		shapes.add(new Shape2(3, new Vector2(0.2, 0.5), -0.4, Shape2.INSCRIBE));
		shapes.add(new Shape2(2, new Vector2(0.5, 0.3), .3, Shape2.INFLATE));
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
		// Compute, draw and advance all shapes
		for (int i = 0; i < shapes.size(); i++) {
			Shape2 currentShape = shapes.get(i);

			// lets the shape know how big the window is
			currentShape.setContainerSize(containerSize);
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
	private void drawShape(Graphics2D g, Shape2 shape) {
		ArrayList<Vector2> vertices = shape.getVertices();
		for (int i = 0; i < vertices.size(); i++) {
			Vector2 fromVertex = vertices.get(i);
			Vector2 toVertex = vertices.get((i + 1) % vertices.size());
			sharedLine.setLine(fromVertex.getX(), fromVertex.getY(),
					toVertex.getX(), toVertex.getY());
			g.draw(sharedLine);
		}
	}
}
	