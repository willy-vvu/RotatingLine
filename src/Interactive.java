/*
 * Interactive.java: A class to create an Interactive object
 * for displaying and interacting with a Rotating Line (and
 * other shapes).
 * 
 * Written by Liam Smith
 * Period 5
 * 1/23/2014
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import com.leapmotion.leap.CircleGesture;
//import com.leapmotion.leap.Controller;
//import com.leapmotion.leap.Frame;
//import com.leapmotion.leap.Gesture;
//import com.leapmotion.leap.Hand;
//import com.leapmotion.leap.Listener;
//import com.leapmotion.leap.Pointable;
//import com.leapmotion.leap.ScreenTapGesture;
//import com.leapmotion.leap.Vector;



public class Interactive {
	private InteractiveState state;

	public static void main(String[] args) {
		new Interactive();

	}

	/*
	 * Default constructor for an Interactive object
	 */
	public Interactive() {
		state = new InteractiveState();
		
		// Create frame
		final JFrame frame = new JFrame();
		state.frame = frame;
		frame.setSize(800, 800);
		frame.setLocation(10, 10);
		frame.setTitle("Rotating Line Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Add the Drawing component
		final ShapeDrawingComponent drawingComponent = new ShapeDrawingComponent(
				state);
		frame.add(drawingComponent);
		frame.setVisible(true);
		
		// Make the controls
		final JFrame controls = new JFrame();
		controls.setTitle("Rotating Line Controls");
		controls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controls.setLocation(850, 10);
		controls.setSize(350, 800);

		// creating panels for controls
		final JPanel overallControls = new JPanel();
		overallControls.setLayout(new GridLayout(3, 1));
		JPanel rotationPanel = new JPanel();
		rotationPanel.setLayout(new GridLayout(3, 1));
		JLabel rotationSliderLabel = new JLabel("Rotation Speed", JLabel.CENTER);
		rotationPanel.add(rotationSliderLabel);
		JPanel rotationButtons = new JPanel();
		rotationButtons.setLayout(new GridLayout(1, 2));
		
		//defining buttons and sliders
		state.rotationFromView = new JButton("Align rotation to view");
		state.rotationFromView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//aligns the rotation the the user's current perspective
				state.alignRotationToView();
			}
		});

		state.resetRotation = new JButton("Reset Rotation");
		state.resetRotation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//sets the rotation to the default rotation perspective
				state.resetRotation();
			}
		});

		JPanel dimensionsPanel = new JPanel();
		dimensionsPanel.setLayout(new GridLayout(1, 2));
		JPanel shapeOptionsPanel = new JPanel();
		shapeOptionsPanel.setLayout(new GridLayout(1, 2));

		JPanel sidesSliderPanel = new JPanel();
		sidesSliderPanel.setLayout(new GridLayout(2, 1));
		JLabel sidesSliderLabel = new JLabel("Number of Sides", JLabel.CENTER);
		sidesSliderPanel.add(sidesSliderLabel);
		
		JPanel facesSliderPanel = new JPanel();
		facesSliderPanel.setLayout(new GridLayout(2, 1));
		JLabel facesSliderLabel = new JLabel("Number of Faces", JLabel.CENTER);
		facesSliderPanel.add(facesSliderLabel);
		
		state.shapePanel = new JTabbedPane();
		state.shapePanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				//determines the dimensions based on what tab is selected
				state.set2D(state.shapePanel.getSelectedIndex() == 0);
			}
		});

		state.rotationSlider = new JSlider(-600, 600, 0);
		state.rotationSlider.setMajorTickSpacing(200);
		state.rotationSlider.setMinorTickSpacing(50);
		state.rotationSlider.setPaintTicks(true);
		state.rotationSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				//sets the speed to the value of the slider
				state.setSpeed(state.rotationSlider.getValue());
			}
		});

		state.sideSlider = new JSlider(2, 18, 2);
		state.sideSlider.setMajorTickSpacing(1);
		state.sideSlider.setMinorTickSpacing(1);
		state.sideSlider.setPaintTicks(true);
		state.sideSlider.setPaintLabels(true);
		state.sideSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				//sets the number of sides to the value of the slider
				state.setSides(state.sideSlider.getValue());
			}
		});
		
		state.faceSlider = new JSlider(1, 4, 2);
		state.faceSlider.setMajorTickSpacing(1);
		state.faceSlider.setMinorTickSpacing(1);
		state.faceSlider.setPaintTicks(true);
		Hashtable<Integer, JLabel> faceLabels = new Hashtable<Integer, JLabel>();
		faceLabels.put(1, new JLabel("Tetrahedron"));
		faceLabels.put(2, new JLabel("Hexahedron"));
		faceLabels.put(3, new JLabel("Octahedron"));
		faceLabels.put(4, new JLabel("Icosahedron"));
		state.faceSlider.setLabelTable(faceLabels);
		state.faceSlider.setPaintLabels(true);
		state.faceSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				//sets the 3D shape to the shape on the slider
				state.setPreset(state.faceSlider.getValue());
			}
		});

		state.inscribeButton = new JButton("Inscribe");
		state.inscribeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//sets the shape to the inscribed mode
				state.inscribeButton.setEnabled(false);
				state.inflateButton.setEnabled(true);
				state.selectedShape.setMode((byte) 1);
			}
		});

		state.inflateButton = new JButton("Inflate");
		state.inflateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//sets the shape to the inflated mode
				state.inscribeButton.setEnabled(true);
				state.inflateButton.setEnabled(false);
				state.selectedShape.setMode((byte) 0);
			}
		});

		state.addShapeButton = new JButton("Add Shape");
		state.addShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//adds a new shape
				Shape3 shape = new Shape3(2);
				shape.getCenter().set(0.5, 0.5);
				state.shapes.add(shape);
				state.selectShape(shape);
				state.removeShapeButton.setEnabled(true);
			}

		});

		state.removeShapeButton = new JButton("Delete Shape");
		state.removeShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//deletes a shape if applicable
				if (state.selectedShape != null) {
					state.shapes.remove(state.selectedShape);
					if (state.shapes.size() == 0) {
						state.removeShapeButton.setEnabled(false);
					}
				}
			}
		});
		
		state.threeDButton = new JButton("3D");
		state.threeDButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//sets the view to three dimensions
				state.threeDButton.setEnabled(false);
				state.twoDButton.setEnabled(true);
				state.blendDimensions = 0;
				state.threeDimensions = true;
			}
		});

		state.twoDButton = new JButton("2D");
		state.twoDButton.setEnabled(false);
		state.twoDButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//sets the view to two dimensions
				state.threeDButton.setEnabled(true);
				state.twoDButton.setEnabled(false);
				state.blendDimensions = 0;
				state.threeDimensions = false;
			}
		});

		//adding buttons and Panels to the frame
		rotationButtons.add(state.rotationFromView);
		rotationButtons.add(state.resetRotation);

		rotationPanel.add(state.rotationSlider);
		rotationPanel.add(rotationButtons);
		sidesSliderPanel.add(state.sideSlider);
		facesSliderPanel.add(state.faceSlider);

		JPanel inflateInscribePanel = new JPanel();
		inflateInscribePanel.setLayout(new GridLayout(1, 2));
		inflateInscribePanel.add(state.inscribeButton);
		inflateInscribePanel.add(state.inflateButton);

		state.shapePanel.addTab("2D shape", sidesSliderPanel);
		state.shapePanel.addTab("3D polyhedron", facesSliderPanel);
		dimensionsPanel.add(state.threeDButton);
		shapeOptionsPanel.add(state.addShapeButton);
		shapeOptionsPanel.add(state.removeShapeButton);
		dimensionsPanel.add(state.twoDButton);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(3, 1));
		buttonPanel.add(dimensionsPanel);
		buttonPanel.add(shapeOptionsPanel);
		buttonPanel.add(inflateInscribePanel);

		overallControls.add(buttonPanel);
		overallControls.add(state.shapePanel);
		overallControls.add(rotationPanel);
		controls.add(overallControls);
		controls.setVisible(true);
		state.selectShape(state.shapes.get(0));

	}
}

/*
 * InteractiveState.java: a class that holds all the data
 * and methods necessary for creating an Interactive object
 * 
 * Written by Liam Smith
 * Period 5
 * 1/23/2014
 */

class InteractiveState {
	
	protected Vector3 leapPointer = new Vector3();
	protected int leapPointing = 0;
	protected Shape3 leapGrab = null;
	protected float leapToTouch = 0;

	protected JFrame frame = null;
	protected JButton addShapeButton = null;
	protected JButton removeShapeButton = null;
	protected JButton rotationFromView;
	protected JButton resetRotation;
	protected Shape3 draggingCenter = null;
	protected JSlider rotationSlider = null;
	protected JSlider sideSlider = null;
	protected JSlider faceSlider = null;
	protected JButton inscribeButton = null;
	protected JButton inflateButton = null;
	protected JButton threeDButton = null;
	protected JButton twoDButton = null;
	protected JLabel rotationLabel = null;
	protected JTabbedPane shapePanel = null;
	protected boolean threeDimensions = false;
	protected double blendDimensions = 0;
	protected Vector3 view = new Vector3(0, 0, 0);
	protected Vector3 viewVelocity = new Vector3(0, 0, 0);
	protected static final Vector3 viewFriction = new Vector3(0.999, 0.999,
			0.99);
	protected static final double POINT_SELECT_THRESHOLD = 20;
	protected static final double EDGE_SELECT_THRESHOLD = 20;
	protected Vector2 mousePosition = new Vector2();
	protected boolean mousePressed = false;
	protected long mouseLastMoved = 0;
	protected Shape3 selectedShape = null;

	protected Vector3 currentBoxSize = new Vector3(0, 0, 0);
	protected Vector3 boxSize = null;
	protected ArrayList<Shape3> shapes = new ArrayList<Shape3>();
	protected Vector3 screenSize = new Vector3();
	protected Projector projector = new Projector(45);
	protected CubeGeometry boundingCubeGeo = new CubeGeometry(currentBoxSize);
	protected Mesh boundingCube = new Mesh(boundingCubeGeo);
	protected Shape3 highlight = null;
//	protected Controller controller;
//	protected Listener listener;

	protected static final Vector2 tempV2 = new Vector2();
	protected static final Vector3 tempV3 = new Vector3();

	/*
	 * Sets the speed of the currently selected shape
	 * given a speed
	 * Parameters: int speed- the speed of the shape
	 * Returns: none
	 */
	protected void setSpeed(int speed) {
		if (selectedShape != null) {
			selectedShape.setRotationSpeed(Math.max(Math.min(speed * .01, 6),
					-6));
		}
	}

	/*
	 * Sets the rotation axis of the selected shape to the projected view axis
	 * Parameters: none
	 * Returns: none
	 */
	protected void alignRotationToView() {
		if (selectedShape != null) {
			selectedShape.getRotationAxis().set(0, 0, 1)
					.rotate(projector.getRotation());
		}
	}

	/*
	 * Resets the rotation axis of the selected shape
	 * Parameters: none
	 * Returns: none
	 */
	protected void resetRotation() {
		if (selectedShape != null) {
			selectedShape.get3DRotation().set(0, 0, 1, 0);
			selectedShape.getRotationAxis().set(0, 0, 1);
		}
	}

	/*
	 * Sets whether or not the shape is 2D or 3D
	 * Parameters: boolean is2D- whether or not the 
	 * 							 shape is 2D
	 * Returns: none
	 */
	protected void set2D(boolean is2D) {
		if (selectedShape != null) {
			selectedShape.set2D(is2D);
		}
	}

	/*
	 * Sets the selected shape to the shape of a given
	 * preset value.
	 * Parameters: int value - the shape's preset value
	 * Returns: none
	 */
	protected void setPreset(int value) {
		if (selectedShape != null) {
			switch (value) {
			case 1:
				selectedShape.setPreset(Shape3.TETRAHEDRON);
				break;
			case 2:
				selectedShape.setPreset(Shape3.HEXAHEDRON);
				break;
			case 3:
				selectedShape.setPreset(Shape3.OCTAHEDRON);
				break;
			case 4:
				selectedShape.setPreset(Shape3.ICOSAHEDRON);
				break;
			}
		}
	}

	/*
	 * Sets the number of sides of the selected shape
	 * Parameters: int value - the number of sides
	 * Returns: none
	 */
	protected void setSides(int value) {
		if (selectedShape != null) {
			selectedShape.setSides(Math.max(Math.min(value, 18), 2));
		}
	}

	/*
	 * Selects a given shape
	 * Parameters: shape- the shape to be selected
	 * Returns: none
	 */
	protected void selectShape(Shape3 shape) {
		selectedShape = shape;
		updateUI();
	}

	/*
	 * Updates the buttons and sliders to reflect the selected shape
	 * Parameters: none
	 * Returns: none
	 */
	protected void updateUI() {
		threeDButton.setEnabled(!threeDimensions);
		twoDButton.setEnabled(threeDimensions);
		if (selectedShape != null) {
			rotationSlider
					.setValue((int) (selectedShape.getRotationSpeed() * 100));
			sideSlider.setValue(selectedShape.getSides());
			if (selectedShape.isPreset(Shape3.TETRAHEDRON)) {
				faceSlider.setValue(1);
			} else if (selectedShape.isPreset(Shape3.HEXAHEDRON)) {
				faceSlider.setValue(2);
			} else if (selectedShape.isPreset(Shape3.OCTAHEDRON)) {
				faceSlider.setValue(3);
			} else if (selectedShape.isPreset(Shape3.ICOSAHEDRON)) {
				faceSlider.setValue(4);
			}
			if (selectedShape.getMode() == Shape3.INFLATE) {
				this.inscribeButton.setEnabled(true);
				this.inflateButton.setEnabled(false);
			} else if (selectedShape.getMode() == 1) {
				this.inscribeButton.setEnabled(false);
				this.inflateButton.setEnabled(true);
			}
			shapePanel.setSelectedIndex(selectedShape.isIs2D() ? 0 : 1);
		}
	}

	/*
	 * Gets the closest selectable shape to the cursor, including the center,
	 * within default thresholds.
	 * 
	 * Parameters: Vector2 vector - the cursor position
	 * Returns: the selection (if any)
	 */
	protected Shape3 getClosestToPoint(Vector2 vector) {
		return getClosestToPoint(vector, EDGE_SELECT_THRESHOLD,
				POINT_SELECT_THRESHOLD);
	}

	/*
	 * Gets the closest selectable shape to the cursor, including the center,
	 * within a threshold.
	 * 
	 * Parameters: 	Vector2 vector - the cursor position
	 * 				double edgeThreshold - the edge threshold
	 * 	 			double pointThreshold - the point threshold
	 * Returns: the selection (if any)
	 */
	protected Shape3 getClosestToPoint(Vector2 vector, double edgeThreshold,
			double pointThreshold) {
		Shape3 selection = getClosestCenterToPoint(vector, pointThreshold);
		if (selection != null) {
			return selection;
		}
		double minDist = Double.POSITIVE_INFINITY;
		for (int i = 0; i < shapes.size(); i++) {
			Shape3 currentShape = shapes.get(i);
			// Check distance to shape edges
			double currentDist = currentShape.edgeToPoint(vector);
			if (currentDist < edgeThreshold && currentDist < minDist) {
				minDist = currentDist;
				selection = currentShape;
			}
		}
		return selection;
	}

	/*
	 * Gets the closest selectable shape center to the cursor, within the
	 * default threshold.
	 * 
	 * Parameters: Vector2 vector - the cursor position
	 * Returns: the selection (if any)
	 */
	protected Shape3 getClosestCenterToPoint(Vector2 vector) {
		return getClosestCenterToPoint(vector,
				InteractiveState.POINT_SELECT_THRESHOLD);
	}

	/*
	 * Gets the closest selectable shape center to the cursor, within a given
	 * threshold.
	 * 
	 * Parameters: 	Vector 2 vector - the cursor position
	 *				double threshold - the threshold for selection
	 * Returns: the selection (if any)
	 */
	protected Shape3 getClosestCenterToPoint(Vector2 vector, double threshold) {
		Shape3 selection = null;
		double minDist = Double.POSITIVE_INFINITY;
		for (int i = 0; i < shapes.size(); i++) {
			Shape3 currentShape = shapes.get(i);
			// Check distance to shape center
			currentShape.getCenterInBox(currentBoxSize, tempV3);
			projector.project(tempV3, tempV2);
			double currentDist = tempV2.distanceTo(vector);
			if (currentDist < threshold && currentDist < minDist) {
				minDist = currentDist;
				selection = currentShape;
			}
		}
		return selection;
	}
}

/*
 * ShapeDrawingComponent.java: The component that draws the current shape, handels
 * mouse interaction and controller interaction
 * 
 * Written by Liam Smith
 * Period 5
 * 1/23/2014
 */
class ShapeDrawingComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	private static final Color BACKGROUND_COLOR = new Color(255, 255, 255, 0);
	private static final Color FOREGROUND_COLOR = new Color(0, 0, 0);
	private static final Color BOUNDING_BOX_COLOR = new Color(0, 0, 0, 100);
	private static final Color LEAP_POINTER_COLOR = new Color(0, 200, 0, 255);
	private static final Color SELECTED_COLOR = new Color(0, 0, 255);
	private static final Color HIGHLIGHT_COLOR = new Color(100, 100, 255);
	private static final BasicStroke THICK_STROKE = new BasicStroke(4);
	private static final BasicStroke MEDIUM_STROKE = new BasicStroke(2);
	private static final BasicStroke THIN_STROKE = new BasicStroke(1);
	private static Line2D.Double sharedLine = new Line2D.Double();
	private static Ellipse2D.Double sharedEllipse = new Ellipse2D.Double();
	private long lastTick = 0;
	private InteractiveState state;

	private static final Quaternion tempQ = new Quaternion();
	private static final Quaternion tempQ_2 = new Quaternion();
	private static final Vector3 yAxis = new Vector3(0, 1, 0);
	private static final Vector3 xAxis = new Vector3(1, 0, 0);

	private static final int FLINGER_SPEED = 50;
	private static final int VERTEX_RADIUS = 3;
	private static final int CENTER_RADIUS = 6;
	private static final int BORDER_2D = 10;
	private static final int SCROLL_SPEED = 25;
	private static final int LEAP_POINTER_RADIUS = 20;

	public static final double FLINGER_STOP_THRESHOLD = 0.01;

	public ShapeDrawingComponent(final InteractiveState state) {
		this.state = state;
		//Adds a few default shapes
		Shape3 shapeToAdd = new Shape3(3);
		shapeToAdd.getCenter().set(0.7, 0.4, 0.9);
		state.shapes.add(shapeToAdd);

		shapeToAdd = new Shape3(5);
		shapeToAdd.getCenter().set(0.3, 0.6, 0.7);
		shapeToAdd.setMode(Shape3.INSCRIBE);
		state.shapes.add(shapeToAdd);

		shapeToAdd = new Shape3(7);
		shapeToAdd.getCenter().set(0.5, 0.5, 0.2);
		state.shapes.add(shapeToAdd);

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				state.mousePressed = false;
				state.draggingCenter = null;
				if (state.threeDimensions) {
					// Stop the vertical spin when it is shallow.
					if (Math.abs(state.viewVelocity.getY() * 3) < Math
							.abs(state.viewVelocity.getX())
							&& Math.abs(state.viewVelocity.getY()) < 0.01) {
						state.viewVelocity.setY(0);
					}
					// Stop the spin if you hold the mouse down or aren't moving
					// the mouse much at all.
					if (new Date().getTime() - state.mouseLastMoved > 250
							|| state.viewVelocity.length() < FLINGER_STOP_THRESHOLD) {
						state.viewVelocity.set(0, 0, 0);
					}
				}
				state.mousePosition.set(e.getX(), e.getY());
				state.highlight = state.getClosestToPoint(state.mousePosition);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				state.mousePressed = true;
				state.highlight = null;
				if (state.threeDimensions) {
					state.viewVelocity.set(0, 0, 0);
					state.mouseLastMoved = new Date().getTime();
				}
				state.mousePosition.set(e.getX(), e.getY());
				// Check to see if user is dragging shape center
				state.draggingCenter = state
						.getClosestCenterToPoint(state.mousePosition);
			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// User wants to select a shape
				state.mousePosition.set(e.getX(), e.getY());
				Shape3 selection = state.getClosestToPoint(state.mousePosition);
				if (selection != null) {
					state.selectShape(selection);
				}
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				this.mouseMoved(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				double newX = e.getX(), newY = e.getY();
				if (state.mousePressed) {
					state.highlight = null;
					if (state.draggingCenter != null) {
						InteractiveState.tempV2.set(
								newX - state.mousePosition.getX(), newY
										- state.mousePosition.getY());
						state.draggingCenter.getCenterInBox(
								state.currentBoxSize, InteractiveState.tempV3);
						state.projector.transform(InteractiveState.tempV3,
								InteractiveState.tempV3);
						double depth = InteractiveState.tempV3.getZ();
						state.projector.perspective(InteractiveState.tempV3);
						InteractiveState.tempV3.add(InteractiveState.tempV2);
						state.projector.inversePrespective(
								InteractiveState.tempV3, depth);
						state.projector.inverseTransform(
								InteractiveState.tempV3,
								InteractiveState.tempV3);
						state.draggingCenter.setCenterInBox(
								state.currentBoxSize, InteractiveState.tempV3);
						// Keep the center in bounds
						Vector3 center = state.draggingCenter.getCenter();
						center.setX(Math.min(Math.max(center.getX(), 0), 1));
						center.setY(Math.min(Math.max(center.getY(), 0), 1));
						center.setZ(Math.min(Math.max(center.getZ(), 0), 1));
					} else if (state.threeDimensions) {
						InteractiveState.tempV3.set(
								newX - state.mousePosition.getX(),
								state.mousePosition.getY() - newY)
								.multiplyScalar(0.01 * 0.2);
						state.viewVelocity.multiplyScalar(0.8).add(
								InteractiveState.tempV3);
						state.view.add(state.viewVelocity);
					}
					state.mousePosition.set(newX, newY);
				} else {
					state.mousePosition.set(newX, newY);
					state.highlight = state
							.getClosestToPoint(state.mousePosition);
				}
			}
		});
		// Listen for mouse scrolling
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (state.threeDimensions) {
					state.viewVelocity.setZ(state.viewVelocity.getZ()
							- SCROLL_SPEED * e.getPreciseWheelRotation());
				}
			}
		});
		
		//Some crazy controller stuff
		// state.controller = new Controller();
		// state.listener = new Listener() {
		//
		// public void onConnect(Controller controller) {
		// controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		// controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		// }
		//
		// public void onFrame(Controller controller) {
		// Frame frame = controller.frame();
		// state.highlight = null;
		// state.leapPointing = 0;
		// if (!frame.hands().isEmpty()) {
		// Hand hand = frame.hands().get(0);
		// if (frame.hands().count() > 1 && hand.fingers().count() > 2
		// && frame.hands().get(1).fingers().count() > 2
		// && controller.frame(1).hands().count() > 1) {
		// state.leapGrab = null;
		// state.leapPointing = -1;
		// state.viewVelocity.multiplyScalar(0.5);
		// // Two hands. Check for 2D to 3D transition
		// Hand secondHand = frame.hands().get(1);
		// double deltaDistance = hand
		// .stabilizedPalmPosition()
		// .distanceTo(secondHand.stabilizedPalmPosition())
		// - controller
		// .frame(1)
		// .hand(hand.id())
		// .stabilizedPalmPosition()
		// .distanceTo(
		// controller
		// .frame(1)
		// .hand(secondHand.id())
		// .stabilizedPalmPosition());
		// if (deltaDistance > 5 && state.threeDimensions) {
		// state.blendDimensions = 0;
		// state.threeDimensions = false;
		// state.updateUI();
		// }
		// if (deltaDistance < -5 && !state.threeDimensions) {
		// state.blendDimensions = 0;
		// state.threeDimensions = true;
		// state.updateUI();
		// }
		// } else if (hand.fingers().count() > 1) {
		// // One hand. Rotate the scene
		// Vector palmVelocity = hand.palmVelocity();
		// if (state.leapGrab != null) {
		// // This if body caused many issues that I think had
		// // to do with the static temporary variables.
		// // I fixed it by temporarily creating a new
		// // temporary object like so:
		// Vector3 tempV3 = new Vector3();
		// state.leapPointing = 2;
		// Pointable pointer = frame.pointables().frontmost();
		// Vector position = frame.interactionBox()
		// .normalizePoint(
		// pointer.stabilizedTipPosition(),
		// false);
		// state.leapToTouch = pointer.touchDistance();
		// state.leapPointer
		// .set(position.getX(), 1 - position.getY(),
		// 0).multiply(state.screenSize)
		// .setZ(position.getZ());
		// state.leapGrab.getCenterInBox(state.currentBoxSize,
		// tempV3);
		// state.projector.transform(tempV3, tempV3);
		// double depth = Math.max(tempV3.getZ() - 0.1
		// * palmVelocity.getZ(), 1);
		// state.projector.perspective(tempV3);
		// double depth2 = tempV3.getZ();
		// tempV3.copy(state.leapPointer).setZ(depth2);
		// state.projector.inversePrespective(tempV3, depth);
		// state.projector.inverseTransform(tempV3, tempV3);
		// state.leapGrab.setCenterInBox(state.currentBoxSize,
		// tempV3);
		// // Keep the center in bounds
		// Vector3 center = state.leapGrab.getCenter();
		// center.setX(Math.min(Math.max(center.getX(), 0), 1));
		// center.setY(Math.min(Math.max(center.getY(), 0), 1));
		// center.setZ(Math.min(Math.max(center.getZ(), 0), 1));
		// } else {
		// if (state.threeDimensions) {
		// state.viewVelocity.set(
		// palmVelocity.getX() * 0.0005,
		// palmVelocity.getY() * 0.0002,
		// palmVelocity.getZ() * 0.1);
		// }
		// }
		// } else {
		// // One hand with closed fist/finger
		// state.viewVelocity.multiplyScalar(0.9);
		// if (frame.pointables().count() > 0) {
		// state.leapPointing = 1;
		// // One finger. Select shapes
		// Pointable pointer = frame.pointables().frontmost();
		// Vector position = frame.interactionBox()
		// .normalizePoint(
		// pointer.stabilizedTipPosition(),
		// true);
		// state.leapToTouch = pointer.touchDistance();
		// state.leapPointer
		// .set(position.getX(), 1 - position.getY(),
		// 0).multiply(state.screenSize)
		// .setZ(position.getZ());
		// state.highlight = state.getClosestToPoint(
		// state.leapPointer,
		// Double.POSITIVE_INFINITY, 20);
		// state.leapGrab = state.getClosestCenterToPoint(
		// state.leapPointer, 20);
		// if (!frame.gestures().isEmpty()) {
		// for (int i = 0; i < frame.gestures().count(); i++) {
		// Gesture gesture = frame.gestures().get(i);
		// if (gesture.type() == Gesture.Type.TYPE_SCREEN_TAP) {
		// ScreenTapGesture tap = new ScreenTapGesture(
		// gesture);
		// if (!tap.pointable().equals(pointer)) {
		// continue;
		// }
		// if (state.highlight != null) {
		// state.selectShape(state.highlight);
		// }
		// break;
		// } else if (gesture.type() == Gesture.Type.TYPE_CIRCLE) {
		// CircleGesture circle = new CircleGesture(
		// gesture);
		// if (!circle.pointable().equals(pointer)) {
		// continue;
		// }
		// CircleGesture previous = new CircleGesture(
		// controller.frame(1).gesture(
		// circle.id()));
		// int clockwise = -1;
		// if (circle.pointable().direction()
		// .angleTo(circle.normal()) <= Math.PI / 4) {
		// clockwise = 1;
		// }
		// double angle = (circle.progress() - previous
		// .progress())
		// * 2
		// * Math.PI
		// * 0.2;
		// if (state.selectedShape != null) {
		// state.selectedShape
		// .setRotationSpeed(Math.min(
		// Math.max(
		// state.selectedShape
		// .getRotationSpeed()
		// + clockwise
		// * angle,
		// -6), 6));
		// state.updateUI();
		// }
		// break;
		// }
		// }
		// }
		// } else {
		// // Closed fist
		// state.leapGrab = null;
		// }
		// }
		// }
		// }
		// };
		// state.controller.addListener(state.listener);
		// state.frame.addWindowListener(new java.awt.event.WindowAdapter() {
		// public void windowClosing(java.awt.event.WindowEvent evt) {
		// state.controller.removeListener(state.listener);
		// }
		// });
	}

	/*
	 * Paints the shape to a given graphics context
	 * Parameters: Graphics graphics - the graphics context
	 * Returns: none
	 */
	public void paintComponent(Graphics graphics) {
		// Extract Graphics2D
		Graphics2D g = (Graphics2D) graphics;
		//adds some anti-Aliasing
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// Draw background
		g.setColor(BACKGROUND_COLOR);
		g.fill(this.getBounds());
		g.setColor(FOREGROUND_COLOR);

		long currentTime = new Date().getTime();
		double timeElapsed = (lastTick == 0 ? 0 : (currentTime - lastTick)) * 0.001;
		lastTick = currentTime;

		// Handle the view
		state.screenSize.set(this.getWidth(), this.getHeight());
		state.projector.setScreenSize(state.screenSize);
		if (state.boxSize == null) {
			state.boxSize = new Vector3(state.screenSize.getY());
			state.view.setZ(-2 * state.screenSize.getY());
		}
		if (state.threeDimensions) {
			if (!state.mousePressed) {
				InteractiveState.tempV3.copy(state.viewVelocity)
						.multiplyScalar(FLINGER_SPEED * timeElapsed);
				state.view.add(InteractiveState.tempV3);
			}
			state.viewVelocity.multiply(InteractiveState.viewFriction);
			state.projector.setOrthographic(state.projector.getOrthographic()
					* (1 - state.blendDimensions));
		} else {
			// Spin consistently
			double rotation = state.view.getX();
			rotation %= (2 * Math.PI);
			if (rotation < 0) {
				rotation += 2 * Math.PI;
			}
			if (rotation > Math.PI) {
				rotation -= 2 * Math.PI;
			}
			state.view.setX(rotation);
			InteractiveState.tempV3.set(0, 0, -2 * state.screenSize.getY());
			state.view.lerp(InteractiveState.tempV3, state.blendDimensions);
			state.projector.setOrthographic(state.projector.getOrthographic()
					* (1 - state.blendDimensions) + state.blendDimensions);
		}
		state.view.setY(Math.min(Math.max(-Math.PI / 2, state.view.getY()),
				Math.PI / 2));
		state.view.setZ(Math.min(state.view.getZ(), -1));
		tempQ.setAxisAngle(yAxis, state.view.getX());
		tempQ_2.setAxisAngle(xAxis, state.view.getY());
		tempQ.multiply(tempQ_2);
		state.projector.getRotation().copy(tempQ);
		state.projector.getPosition().set(0, 0, state.view.getZ())
				.rotate(tempQ);

		// Draw bounding box
		if (state.threeDimensions) {
			state.currentBoxSize.lerp(state.boxSize, state.blendDimensions);
		} else {
			InteractiveState.tempV3.set(state.screenSize.getX() - BORDER_2D,
					state.screenSize.getY() - BORDER_2D, state.boxSize.getZ());
			state.currentBoxSize.lerp(InteractiveState.tempV3,
					state.blendDimensions);
		}
		state.boundingCubeGeo.setSize(state.currentBoxSize);
		state.boundingCubeGeo.rebuild();
		state.boundingCube.project(state.projector);
		if (state.leapPointing == -1) {
			g.setColor(LEAP_POINTER_COLOR);
			g.setStroke(MEDIUM_STROKE);
		} else {
			g.setColor(BOUNDING_BOX_COLOR);
			g.setStroke(THIN_STROKE);
		}
		this.drawLines(g, state.boundingCube);

		// Compute, draw and advance all shapes
		for (int i = 0; i < state.shapes.size(); i++) {
			Shape3 currentShape = state.shapes.get(i);

			// lets the shape know how big the window is
			currentShape.transform(state.projector, state.currentBoxSize);
			this.drawShape(g, currentShape);
			currentShape.step(timeElapsed);
		}
		// Draw the pointer from the leap motion
		if (state.leapPointing > 0) {
			g.setColor(LEAP_POINTER_COLOR);
			if (state.leapPointing == 2) {
				g.setStroke(THICK_STROKE);
			} else if (state.leapPointing == 1 && state.leapGrab != null) {
				g.setStroke(MEDIUM_STROKE);
			} else {
				g.setStroke(THIN_STROKE);
			}
			double realRadius = LEAP_POINTER_RADIUS * (state.leapToTouch + 1);
			sharedEllipse.setFrame(state.leapPointer.getX() - realRadius,
					state.leapPointer.getY() - realRadius, 2 * realRadius,
					2 * realRadius);
			g.draw(sharedEllipse);
		}

		// Blend the dimensions if we are shifting
		state.blendDimensions = Math.min(state.blendDimensions + 0.05
				* timeElapsed, 1);
		if (state.blendDimensions > 0.05) {
			state.blendDimensions = 1;
		}
		this.repaint();

	}

	/*
	 * draws a single shape to a given graphics context
	 * 
	 * Parameters: 	Graphics 2D g - the graphics context
	 * 				Shape 3 shape - the shape to be drawn
	 * Returns: none
	 */
	private void drawShape(Graphics2D g, Shape3 shape) {
		// Draw the center
		if (state.selectedShape == shape) {
			g.setColor(SELECTED_COLOR);
			g.setStroke(THICK_STROKE);
		} else if (state.highlight == shape) {
			g.setColor(HIGHLIGHT_COLOR);
			g.setStroke(MEDIUM_STROKE);
		} else {
			g.setColor(FOREGROUND_COLOR);
			g.setStroke(THIN_STROKE);
		}
		// Draw the shape
		drawLines(g, shape);

		// Draw the center and other vertices
		g.setStroke(THIN_STROKE);
		shape.getCenterInBox(state.currentBoxSize, InteractiveState.tempV3);
		state.projector.project(InteractiveState.tempV3,
				InteractiveState.tempV2);
		sharedEllipse.setFrame(InteractiveState.tempV2.getX() - CENTER_RADIUS,
				InteractiveState.tempV2.getY() - CENTER_RADIUS,
				2 * CENTER_RADIUS, 2 * CENTER_RADIUS);
		if (state.selectedShape == shape) {
			g.fill(sharedEllipse);
		} else {
			g.draw(sharedEllipse);
		}
		drawVertices(g, shape);
	}

	/*
	 * Draws an ArrayList of 3D vertices and an ArrayList of
	 * Lines joining them to a given graphics context.
	 * 
	 * Parameters:	Graphics 2D g - the graphics context
	 * 				Mesh mesh - the mesh containing the Line
	 * 							Vertices
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

	/*
	 * Draws an ArrayList of 3D vertices as points to a given graphics
	 * context.
	 * 
	 * Parameters:	Graphics 2D g - the graphics context
	 *				Mesh mesh - the mesh containing the Line
	 * 							Vertices
	 */
	private void drawVertices(Graphics2D g, Mesh mesh) {
		for (int i = 0; i < mesh.getProjected().size(); i++) {
			Vector2 vertex = mesh.getProjected().get(i);
			if (vertex.getX() == vertex.getX()) {
				sharedEllipse.setFrame(vertex.getX() - VERTEX_RADIUS,
						vertex.getY() - VERTEX_RADIUS, 2 * VERTEX_RADIUS,
						2 * VERTEX_RADIUS);
				g.fill(sharedEllipse);
			}
		}
	}
}
