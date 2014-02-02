import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Interactive {
	private InteractiveState state;

	public static void main(String[] args) {
		new Interactive();

	}

	public Interactive() {
		state = new InteractiveState();
		// Create frame
		//TODO changed to final for 3d
		final JFrame frame = new JFrame();
		// Center and size the frame
		frame.setSize(800, 800);
		frame.setLocation(10, 10);
		// Set defaults
		frame.setTitle("Rotating Line Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add the Drawing component
		//TODO also changed to final
		final ShapeDrawingComponent drawingComponent = new ShapeDrawingComponent(
				state);
		//TODO also changed to final
		//final Shape3DrawingComponent drawingComponent3D = new Shape3DrawingComponent();
		frame.add(drawingComponent);
		// Make it visible
		frame.setVisible(true);
		// Make the controls\
		//TODO made final
		final JFrame controls2D = new JFrame();
		// Set title
		controls2D.setTitle("2D Rotating Line Controls");
		// Size and position
		controls2D.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controls2D.setLocation(850, 10);
		controls2D.setSize(350, 800);
		
		final JFrame controls3D = new JFrame();
		// Set title
		controls3D.setTitle("3D Rotating Line Controls");
		// Size and position
		controls3D.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controls3D.setLocation(850, 10);
		controls3D.setSize(350, 800);
		

		// creating overall control panel
		final JPanel overallControls = new JPanel();
		overallControls.setLayout(new GridLayout(5, 1));
		JPanel speedSliderPanel = new JPanel();
		speedSliderPanel.setLayout(new GridLayout(2,1));
		JLabel speedSliderLabel = new JLabel("Rotation Speed", JLabel.CENTER);
		speedSliderPanel.add(speedSliderLabel);
		JPanel dimensionsPanel = new JPanel();
		dimensionsPanel.setLayout(new GridLayout(1,2));
		JPanel shapeOptionsPanel = new JPanel();
		shapeOptionsPanel.setLayout(new GridLayout(1,2));
		JPanel sidesSliderPanel = new JPanel();
		sidesSliderPanel.setLayout(new GridLayout(2,1));
		JLabel sidesSliderLabel = new JLabel("Number of Sides", JLabel.CENTER);
		sidesSliderPanel.add(sidesSliderLabel);

		// creating panel for the buttons
		state.speedSlider = new JSlider(-600, 600, 0);
		state.speedSlider.setMajorTickSpacing(200);
		state.speedSlider.setMinorTickSpacing(50);
		state.speedSlider.setPaintTicks(true);
		//state.speedSlider.setVisible(true);
		state.speedSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				state.setSpeed(state.speedSlider.getValue());
			}
		});


		// creating sidesSlider
		state.sidesSlider = new JSlider(2, 18, 2);
		state.sidesSlider.setMajorTickSpacing(1);
		state.sidesSlider.setMinorTickSpacing(1);
		state.sidesSlider.setPaintTicks(true);
		//state.sidesSlider.setVisible(true);
		state.sidesSlider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				state.setSides(state.sidesSlider.getValue());
			}
		});
		
		state.inscribeButton = new JButton("Inscribe");
		state.inscribeButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				state.inscribeButton.setEnabled(false);
				state.inflateButton.setEnabled(true);
				state.selectedShape.setMode((byte) 1);
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		
	
		
		state.inflateButton = new JButton("Inflate");
		
		state.inflateButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				state.inscribeButton.setEnabled(true);
				state.inflateButton.setEnabled(false);
				state.selectedShape.setMode((byte) 0);
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		
		state.addShapeButton = new JButton("Add Shape");
		state.addShapeButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				Shape3 shape = new Shape3(2);
				shape.getCenter().set(0.5, 0.5);
				state.shapes.add(shape);
				state.selectShape(shape);

			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		
		state.removeShapeButton = new JButton("Delete Shape");
		state.removeShapeButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				state.shapes.remove(state.selectedShape);
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		state.threeDButton = new JButton("3D");
		state.threeDButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				state.threeDButton.setEnabled(false);
				state.twoDButton.setEnabled(true);
				//TODO Enable three dimensions
				frame.remove(drawingComponent);
				//frame.add(drawingComponent3D);
				frame.setVisible(true);
				frame.repaint();
				
				controls2D.setVisible(false);
				controls3D.setVisible(true);
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		
	
		
		state.twoDButton = new JButton("2D");
		
		state.twoDButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				state.threeDButton.setEnabled(true);
				state.twoDButton.setEnabled(false);
				//TODO enable 2 dimensions 
				//frame.remove(drawingComponent3D);
				frame.add(drawingComponent);
				frame.setVisible(true);
				frame.repaint();
				controls3D.setVisible(false);
				controls2D.setVisible(true);
				
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
		
		
		
		
		speedSliderPanel.add(state.speedSlider);
		
		sidesSliderPanel.add(state.sidesSlider);
		JPanel shapeStatePanel = new JPanel();
		shapeStatePanel.setLayout(new GridLayout(1, 2));
		shapeStatePanel.add(state.inscribeButton);
		shapeStatePanel.add(state.inflateButton);
		overallControls.add(shapeStatePanel);
		overallControls.add(speedSliderPanel);
		overallControls.add(sidesSliderPanel);
		shapeOptionsPanel.add(state.addShapeButton);
		shapeOptionsPanel.add(state.removeShapeButton);
		overallControls.add(shapeOptionsPanel);
		dimensionsPanel.add(state.threeDButton);
		dimensionsPanel.add(state.twoDButton);
		overallControls.add(dimensionsPanel);
		
		
		controls2D.add(overallControls);
		controls2D.setVisible(true);
		state.selectShape(state.shapes.get(0));
		
		
	}
}

class InteractiveState {
	
	public JButton addShapeButton=null;
	public JButton removeShapeButton=null;
	public boolean mouseDown = false;
	public boolean rightMouseDown = false;
	public boolean mouseInFrame = false;
	public JSlider speedSlider = null;
	public JSlider sidesSlider = null;
	public JButton inscribeButton = null;
	public JButton inflateButton = null;
	public JButton threeDButton = null;
	public JButton twoDButton = null;
	public JLabel rotationLabel = null;
	
	public int speed = 0;
	public Shape3 selectedShape = null;
	public ArrayList<Shape3> shapes = new ArrayList<Shape3>();

	public void setSpeed(int speed) {
		if (selectedShape != null) {
			selectedShape.setRotationSpeed(Math.max(Math.min(speed * .01, 6),
					-6));
		}
	}

	public void setSides(int value) {
		if (selectedShape != null) {
			selectedShape.setSides(Math.max(Math.min(value, 18),
					2));
		}
	}

	public void selectShape(Shape3 shape) {
		selectedShape = shape;
		speedSlider.setValue((int) (selectedShape.getRotationSpeed() * 100));
		sidesSlider.setValue(selectedShape.getSides());
		if(selectedShape.getMode()==0){
			this.inscribeButton.setEnabled(true);
			this.inflateButton.setEnabled(false);
		}
		else if(selectedShape.getMode()==1){
			this.inscribeButton.setEnabled(false);
			this.inflateButton.setEnabled(true);
		}
		
	}
}

class ShapeDrawingComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	private Vector3 containerSize = new Vector3();
	private static final Color backgroundColor = new Color(255, 255, 255, 0);
	private static final Color foregroundColor = new Color(0x000000);
	private static final Color selectedColor = new Color(0,0,255);
	private static final BasicStroke thickLine = new BasicStroke(5);
	private static final BasicStroke thinLine = new BasicStroke(1);
	private static Line2D.Double sharedLine = new Line2D.Double();
	private static Ellipse2D.Double center = new Ellipse2D.Double();
	private long lastTick = 0;
	private InteractiveState state;
	//TODO figure out what this does
	private Projector projector = new Projector(45);

	public ShapeDrawingComponent(final InteractiveState state) {
		this.state = state;
		state.shapes.add(new Shape3(3));
				//.add(new Shape3(5, new Vector2(0.5, 0.5), 1, (int)(Shape3.INFLATE)));
		state.shapes.add(new Shape3(5));
				//.add(new Shape3(3, new Vector2(0.2, 0.5), -0.4, Shape3.INSCRIBE));
		state.shapes.add(new Shape3(7));
				//new Shape3(2, new Vector2(0.5, 0.3), .3,Shape3.INFLATE));
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				for (int i = 0; i < state.shapes.size(); i++) {
					if(state.shapes.get(i) == state.selectedShape){
						double marginOfError = 50;
						double x = state.shapes.get(i).getCenter().getX() * containerSize.getX();
						double y =state.shapes.get(i).getCenter().getY() * containerSize.getY();
						if ((x <= e.getLocationOnScreen().x + marginOfError && x >= e.getLocationOnScreen().x - marginOfError)
									&& (y <= e.getLocationOnScreen().y + marginOfError 
										&& y >= e.getLocationOnScreen().y - marginOfError))
						{
							//TODO figure out why you have to adjust when moving the center
							state.shapes.get(i).setCenter(new Vector3((e.getLocationOnScreen().x -17)/ containerSize.getX(), 
									(e.getLocationOnScreen().y-42)/containerSize.getY(),0));
						}
					}
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
		});
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO more stuff
				for (int i = 0; i < state.shapes.size(); i++) {
					double fudgeFactor = 50;
					double x = state.shapes.get(i).getCenter().getX() * containerSize.getX();
					double y =state.shapes.get(i).getCenter().getY() * containerSize.getY();
					if ((x <= e.getLocationOnScreen().x + fudgeFactor && x >= e.getLocationOnScreen().x - fudgeFactor)
								&& (y <= e.getLocationOnScreen().y + fudgeFactor 
									&& y >= e.getLocationOnScreen().y - fudgeFactor))
					{
						state.selectShape(state.shapes.get(i));
						
					}
				}
				
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

		long currentTime = new Date().getTime();
		double timeElapsed = (lastTick == 0 ? 0 : (currentTime - lastTick)) * 0.001;
		lastTick = currentTime;
		// Compute, draw and advance all shapes
		for (int i = 0; i < state.shapes.size(); i++) {
			Shape3 currentShape = state.shapes.get(i);

			// lets the shape know how big the window is
			currentShape.setContainerSize(containerSize);
			currentShape.transform(projector);
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
		if(state.selectedShape== shape){
			g.setColor(selectedColor);
			g.setStroke(thickLine);
			center.setFrame(shape.getCenter().getX() * this.containerSize.getX() -2.5 , shape.getCenter().getY() * this.containerSize.getY() -2.5 , 5, 5);
			g.draw(center);
		} 
		else{
			g.setColor(foregroundColor);
			g.setStroke(thinLine);
			center.setFrame(shape.getCenter().getX() * this.containerSize.getX() -2.5 , shape.getCenter().getY() * this.containerSize.getY() -2.5 , 5, 5);
			g.draw(center);
		}
		ArrayList<Vector3> vertices = shape.getVertices();
		for (int i = 0; i < vertices.size(); i++) {
			Vector2 fromVertex = vertices.get(i);
			Vector2 toVertex = vertices.get((i + 1) % vertices.size());
			sharedLine.setLine(fromVertex.getX(), fromVertex.getY(),
					toVertex.getX(), toVertex.getY());
			g.draw(sharedLine);
		}
	}
}
