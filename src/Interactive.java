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

public class Interactive {
	private InteractiveState state;

	public static void main(String[] args) {
		new Interactive();

	}

	public Interactive() {
		state = new InteractiveState();
		// Create frame
		final JFrame frame = new JFrame();
		// Center and size the frame
		frame.setSize(800, 800);
		frame.setLocation(10, 10);
		// Set defaults
		frame.setTitle("Rotating Line Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add the Drawing component
		final ShapeDrawingComponent drawingComponent = new ShapeDrawingComponent(
				state);
		// final Shape3DrawingComponent drawingComponent3D = new
		// Shape3DrawingComponent();
		frame.add(drawingComponent);
		// Make it visible
		frame.setVisible(true);
		// Make the controls
		final JFrame controls = new JFrame();
		// Set title
		controls.setTitle("Rotating Line Controls");
		// Size and position
		controls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controls.setLocation(850, 10);
		controls.setSize(350, 800);

		// creating overall control panel
		final JPanel overallControls = new JPanel();
		overallControls.setLayout(new GridLayout(4, 1));
		JPanel rotationPanel = new JPanel();
		rotationPanel.setLayout(new GridLayout(3, 1));
		JLabel rotationSliderLabel = new JLabel("Rotation Speed", JLabel.CENTER);
		rotationPanel.add(rotationSliderLabel);

		JPanel rotationButtons = new JPanel();
		rotationButtons.setLayout(new GridLayout(1, 2));

		JButton rotationFromView = new JButton("Align rotation to view");
		rotationFromView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				state.alignRotationToView();
			}
		});

		JButton resetRotation = new JButton("Reset Rotation");
		resetRotation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
				state.set2D(state.shapePanel.getSelectedIndex() == 0);
			}
		});

		// creating panel for the buttons
		state.rotationSlider = new JSlider(-600, 600, 0);
		state.rotationSlider.setMajorTickSpacing(200);
		state.rotationSlider.setMinorTickSpacing(50);
		state.rotationSlider.setPaintTicks(true);
		// state.speedSlider.setVisible(true);
		state.rotationSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				state.setSpeed(state.rotationSlider.getValue());
			}
		});

		// creating sideSlider
		state.sideSlider = new JSlider(2, 18, 2);
		state.sideSlider.setMajorTickSpacing(1);
		state.sideSlider.setMinorTickSpacing(1);
		state.sideSlider.setPaintTicks(true);
		state.sideSlider.setPaintLabels(true);
		state.sideSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				state.setSides(state.sideSlider.getValue());
			}
		});
		// creating faceSlider
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

		// state.facesSlider.setVisible(true);
		state.faceSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				state.setPreset(state.faceSlider.getValue());
			}
		});

		state.inscribeButton = new JButton("Inscribe");
		state.inscribeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				state.inscribeButton.setEnabled(false);
				state.inflateButton.setEnabled(true);
				state.selectedShape.setMode((byte) 1);
			}
		});

		state.inflateButton = new JButton("Inflate");
		state.inflateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				state.inscribeButton.setEnabled(true);
				state.inflateButton.setEnabled(false);
				state.selectedShape.setMode((byte) 0);
			}
		});

		state.addShapeButton = new JButton("Add Shape");
		state.addShapeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
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
				state.threeDButton.setEnabled(false);
				state.twoDButton.setEnabled(true);
				state.blendDimensions = 0;
				state.threeDimensions = true;
				if (state.boxSize == null) {
					state.boxSize = new Vector3(state.screenSize.getY());
					state.viewZoom = -state.screenSize.getY() * 2;
				}
			}
		});

		state.twoDButton = new JButton("2D");
		state.twoDButton.setEnabled(false);
		state.twoDButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				state.threeDButton.setEnabled(true);
				state.twoDButton.setEnabled(false);
				state.blendDimensions = 0;
				state.threeDimensions = false;
			}
		});

		JPanel centerX = new JPanel();
		centerX.setLayout(new GridLayout(2, 1));
		JLabel centerXLabel = new JLabel("Shape Center X coordinate",
				JLabel.CENTER);
		centerX.add(centerXLabel);

		state.centerXSlider = new JSlider(0, 100, 50);
		state.centerXSlider.setMajorTickSpacing(25);
		state.centerXSlider.setMinorTickSpacing(5);
		state.centerXSlider.setPaintTicks(true);
		state.centerXSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (state.selectedShape != null) {
					state.selectedShape.getCenter().setX(
							state.centerXSlider.getValue() * 0.01);
				}
			}
		});

		JPanel centerY = new JPanel();
		centerY.setLayout(new GridLayout(2, 1));
		JLabel centerYLabel = new JLabel("Shape Center Y coordinate",
				JLabel.CENTER);
		centerY.add(centerYLabel);

		state.centerYSlider = new JSlider(0, 100, 50);
		state.centerYSlider.setMajorTickSpacing(25);
		state.centerYSlider.setMinorTickSpacing(5);
		state.centerYSlider.setPaintTicks(true);
		state.centerYSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (state.selectedShape != null) {
					state.selectedShape.getCenter().setY(
							state.centerYSlider.getValue() * 0.01);
				}
			}
		});

		JPanel centerZ = new JPanel();
		centerZ.setLayout(new GridLayout(2, 1));
		JLabel centerZLabel = new JLabel("Shape Center Z coordinate",
				JLabel.CENTER);
		centerZ.add(centerZLabel);

		state.centerZSlider = new JSlider(0, 100, 50);
		state.centerZSlider.setMajorTickSpacing(25);
		state.centerZSlider.setMinorTickSpacing(5);
		state.centerZSlider.setPaintTicks(true);
		state.centerZSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (state.selectedShape != null) {
					state.selectedShape.getCenter().setZ(
							state.centerZSlider.getValue() * 0.01);
				}
			}
		});

		centerX.add(state.centerXSlider);
		centerY.add(state.centerYSlider);
		centerZ.add(state.centerZSlider);

		JPanel centerSliders = new JPanel();
		centerSliders.setLayout(new GridLayout(3, 1));
		centerSliders.add(centerX);
		centerSliders.add(centerY);
		centerSliders.add(centerZ);

		rotationButtons.add(rotationFromView);
		rotationButtons.add(resetRotation);

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
		overallControls.add(centerSliders);
		controls.add(overallControls);
		controls.setVisible(true);
		state.selectShape(state.shapes.get(0));

	}
}

class InteractiveState {

	public JButton addShapeButton = null;
	public JButton removeShapeButton = null;
	public boolean draggingCenter = false;
	public boolean mouseInFrame = false;
	public JSlider rotationSlider = null;
	public JSlider centerXSlider = null;
	public JSlider centerYSlider = null;
	public JSlider centerZSlider = null;
	public JSlider sideSlider = null;
	public JSlider faceSlider = null;
	public JButton inscribeButton = null;
	public JButton inflateButton = null;
	public JButton threeDButton = null;
	public JButton twoDButton = null;
	public JLabel rotationLabel = null;
	public JTabbedPane shapePanel = null;
	public boolean threeDimensions = false;
	public double blendDimensions = 0;
	public Vector3 view = new Vector3(0, 0, -500);
	public Vector3 viewVelocity = new Vector3(0, 0, 0);
	public Vector2 mousePosition = new Vector2();
	public boolean mousePressed = false;
	public long mouseLastMoved = 0;

	public Vector3 currentBoxSize = new Vector3(0, 0, 0);
	public Vector3 boxSize = null;
	public Shape3 selectedShape = null;
	public ArrayList<Shape3> shapes = new ArrayList<Shape3>();
	public Vector3 screenSize = new Vector3();
	public Projector projector = new Projector(45);
	public CubeGeometry boundingCubeGeo = new CubeGeometry(currentBoxSize);
	public Mesh boundingCube = new Mesh(boundingCubeGeo);
	public double viewZoom = 0;

	public void setSpeed(int speed) {
		if (selectedShape != null) {
			selectedShape.setRotationSpeed(Math.max(Math.min(speed * .01, 6),
					-6));
		}
	}

	/**
	 * Sets the rotation axis of the selected shape to the projected view axis
	 */
	public void alignRotationToView() {
		if (selectedShape != null) {
			selectedShape.getRotationAxis().set(0, 0, 1)
					.rotate(projector.getRotation());
		}
	}

	/**
	 * Resets the rotation axis of the selected shape
	 */
	public void resetRotation() {
		if (selectedShape != null) {
			selectedShape.get3DRotation().set(0, 0, 1, 0);
			selectedShape.getRotationAxis().set(0, 0, 1);
		}
	}

	/**
	 * Sets whether or not the shape is 2D or 3D
	 * 
	 * @param b
	 */
	public void set2D(boolean is2D) {
		if (selectedShape != null) {
			selectedShape.set2D(is2D);
		}
	}

	/**
	 * Sets the selected shape's preset.
	 * 
	 * @param value
	 */
	public void setPreset(int value) {
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

	public void setSides(int value) {
		if (selectedShape != null) {
			selectedShape.setSides(Math.max(Math.min(value, 18), 2));
		}
	}

	public void selectShape(Shape3 shape) {
		selectedShape = shape;
		rotationSlider.setValue((int) (selectedShape.getRotationSpeed() * 100));
		centerXSlider.setValue((int) (selectedShape.getCenter().getX() * 100));
		centerYSlider.setValue((int) (selectedShape.getCenter().getY() * 100));
		centerZSlider.setValue((int) (selectedShape.getCenter().getZ() * 100));
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

class ShapeDrawingComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	private static final Color backgroundColor = new Color(255, 255, 255, 0);
	private static final Color foregroundColor = new Color(0x000000);
	private static final Color selectedColor = new Color(0, 0, 255);
	private static final BasicStroke thickLine = new BasicStroke(5);
	private static final BasicStroke thinLine = new BasicStroke(1);
	private static Line2D.Double sharedLine = new Line2D.Double();
	private static Ellipse2D.Double sharedEllipse = new Ellipse2D.Double();
	private static final Vector2 tempV2 = new Vector2();
	private static final Vector2 tempV2_2 = new Vector2();
	private static final Vector3 tempV3 = new Vector3();
	private long lastTick = 0;
	private InteractiveState state;

	private static final Quaternion tempQ = new Quaternion();
	private static final Quaternion tempQ_2 = new Quaternion();
	private static final Vector3 yAxis = new Vector3(0, 1, 0);
	private static final Vector3 xAxis = new Vector3(1, 0, 0);

	public ShapeDrawingComponent(final InteractiveState state) {
		this.state = state;

		state.shapes.add(new Shape3(3));
		// .add(new Shape3(5, new Vector2(0.5, 0.5), 1, (int)(Shape3.INFLATE)));
		state.shapes.add(new Shape3(5));
		// .add(new Shape3(3, new Vector2(0.2, 0.5), -0.4, Shape3.INSCRIBE));
		state.shapes.add(new Shape3(7));
		// new Shape3(2, new Vector2(0.5, 0.3), .3,Shape3.INFLATE));
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				this.mouseMoved(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if (state.threeDimensions) {
					if (state.mousePressed) {
						double newX = e.getPoint().getX(), newY = e.getPoint()
								.getY();
						tempV3.set(newX - state.mousePosition.getX(),
								state.mousePosition.getY() - newY)
								.multiplyScalar(0.01 * 0.2);
						state.viewVelocity.multiplyScalar(0.8).add(tempV3);
						state.view.add(state.viewVelocity);
						state.mousePosition.set(newX, newY);
					}
				}
			}

		});
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				state.mousePressed = false;
				if (state.threeDimensions) {
					// Stop the vertical spin when it is shallow.
					if (Math.abs(state.viewVelocity.getY() * 3) < Math
							.abs(state.viewVelocity.getX())
							&& Math.abs(state.viewVelocity.getY()) < 0.01) {
						state.viewVelocity.setY(0);
					}
					// Stop the spin if you hold the mouse down or aren't moving
					// the
					// mouse much at all.
					if (new Date().getTime() - state.mouseLastMoved > 250
							|| state.viewVelocity.length() < 0.01) {
						state.viewVelocity.set(0, 0, 0);
					}
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {
				state.mousePressed = true;
				if (state.threeDimensions) {
					state.viewVelocity.set(0, 0, 0);
					state.mousePosition.set(e.getPoint().getX(), e.getPoint()
							.getY());
					state.mouseLastMoved = new Date().getTime();
				}
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
				tempV2.set(e.getX(), e.getY());
				Shape3 selection = null;
				double minDistToSelected = Double.POSITIVE_INFINITY;
				for (int i = 0; i < state.shapes.size(); i++) {
					Shape3 currentShape = state.shapes.get(i);
					// Check distance to shape edges
					double currentDist = currentShape.edgeToPoint(tempV2);
					// Check distance to shape center
					currentShape.getCenterInBox(state.currentBoxSize, tempV3);
					state.projector.project(tempV3, tempV2_2);
					currentDist = Math.min(currentDist,
							tempV2_2.distanceTo(tempV2));
					if (currentDist < minDistToSelected) {
						minDistToSelected = currentDist;
						selection = currentShape;
					}
				}
				state.selectShape(selection);
			}
		});

		// Listen for mouse scrolling
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (state.threeDimensions) {
					state.viewZoom = (Math.min(
							state.viewZoom - e.getPreciseWheelRotation() * 100,
							-1));
				}
			}
		});
	}

	public void paintComponent(Graphics graphics) {
		// Extract Graphics2D
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// Draw background
		g.setColor(backgroundColor);
		g.fill(this.getBounds());
		g.setColor(foregroundColor);

		long currentTime = new Date().getTime();
		double timeElapsed = (lastTick == 0 ? 0 : (currentTime - lastTick)) * 0.001;
		lastTick = currentTime;

		// Handle the view
		state.screenSize.set(this.getWidth(), this.getHeight(), 0);
		state.projector.setScreenSize(state.screenSize);
		state.projector.getPosition().set(0, 0, -30);
		if (state.threeDimensions) {
			if (!state.mousePressed) {
				tempV3.copy(state.viewVelocity)
						.multiplyScalar(50 * timeElapsed);
				state.view.add(tempV3);
			}
			// Animate the zoom
			state.view.setZ(state.view.getZ() * (1 - state.blendDimensions)
					+ state.viewZoom * state.blendDimensions);
			state.viewVelocity.multiplyScalar(0.999);
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
			tempV3.set(0, 0, -state.screenSize.getY() / 0.82);
			state.view.lerp(tempV3, state.blendDimensions);
		}
		state.view.setY(Math.min(Math.max(-Math.PI / 2, state.view.getY()),
				Math.PI / 2));
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
			state.currentBoxSize.lerp(state.screenSize, state.blendDimensions);
		}
		state.boundingCubeGeo.setSize(state.currentBoxSize);
		state.boundingCubeGeo.rebuild();
		state.boundingCube.project(state.projector);
		this.drawLines(g, state.boundingCube);

		// Compute, draw and advance all shapes
		for (int i = 0; i < state.shapes.size(); i++) {
			Shape3 currentShape = state.shapes.get(i);

			// lets the shape know how big the window is
			currentShape.transform(state.projector, state.currentBoxSize);
			this.drawShape(g, currentShape);
			currentShape.step(timeElapsed);
		}
		// Blend the dimensions if we are shifting
		state.blendDimensions = Math.min(state.blendDimensions + 0.05
				* timeElapsed, 1);
		this.repaint();

	}

	/**
	 * draws a single shape to a given graphics context
	 * 
	 * @param g
	 * @param shape
	 */
	private void drawShape(Graphics2D g, Shape3 shape) {
		// Draw the center
		if (state.selectedShape == shape) {
			g.setColor(selectedColor);
			g.setStroke(thickLine);
		} else {
			g.setColor(foregroundColor);
			g.setStroke(thinLine);
		}
		shape.getCenterInBox(state.currentBoxSize, tempV3);
		state.projector.project(tempV3, tempV2);
		sharedEllipse.setFrame(tempV2.getX() - 2.5, tempV2.getY() - 2.5, 5, 5);
		g.draw(sharedEllipse);
		// Draw the shape
		drawLines(g, shape);
		drawVertices(g, shape);
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
