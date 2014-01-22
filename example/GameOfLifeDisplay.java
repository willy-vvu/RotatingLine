/**
 * GameOfLifeDisplay.java - A class for displaying and interacting with John Conway's Game Of Life.
 * 
 * Written 11/4/2013
 * 
 * @author William Wu
 * 
 */
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class GameOfLifeDisplay extends JPanel {
	private static final long serialVersionUID = 1L;
	private GameOfLife game;
	private GameOfLifeDrawingComponent drawingComponent;
	private GameOfLifeState state;

	/**
	 * Visualizes an empty 200 x 200 Game of Life in a JFrame.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		GameOfLifeDisplay display = new GameOfLifeDisplay();
		display.createGame(128, 128);
	}

	/**
	 * Creates and visualizes a GameOfLife.
	 * 
	 * @param width
	 *            the width of the game board
	 * @param height
	 *            the height of the game board
	 */
	public void createGame(int width, int height) {
		this.game = new GameOfLife(width, height);
		initGame();
	}

	/**
	 * Creates and visualizes a GameOfLife.
	 * 
	 * @param board
	 *            the given board
	 */
	public void createGame(int[][] board) {
		this.game = new GameOfLife(board);
		initGame();
	}

	/**
	 * Initializes the visualization of a GameOfLife after it is created.
	 */
	private void initGame() {
		// Initialize a game state
		state = new GameOfLifeState(this.game);
		// Create frame
		JFrame frame = new JFrame();
		// Center and size the frame
		frame.setSize(840, 840);
		frame.setLocation(10, 10);
		// Set defaults
		frame.setTitle("John Conway's Game of Life");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add the Drawing component
		drawingComponent = new GameOfLifeDrawingComponent(this.game);
		drawingComponent.setState(state);
		frame.add(drawingComponent);
		// Make it visible
		frame.setVisible(true);
		// Make the controls
		JFrame controls = new JFrame();
		// Set title
		controls.setTitle("Game of Life Controls");
		// Size and position
		controls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controls.setLocation(850, 10);
		controls.setSize(350, 800);

		JPanel overallControls = new JPanel();
		overallControls.setLayout(new GridLayout(2, 1));
		controls.add(overallControls);

		JPanel topHalfControls = new JPanel();
		topHalfControls.setLayout(new GridLayout(6, 1));
		overallControls.add(topHalfControls);

		// Create a panel to put the buttons in
		// < || >
		JPanel playPauseRewind = new JPanel();
		playPauseRewind.setLayout(new GridLayout(1, 3));
		topHalfControls.add(playPauseRewind);

		state.speedSlider = new JSlider(-100, 100, 0);

		JButton rewindButton = new JButton("Rewind");
		rewindButton
				.setToolTipText("Rewinds the simulation, then increases rewind speed.");
		rewindButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (state.speed > -25) {
					state.setSpeed(-25);
				} else {
					state.setSpeed(Math.max(-100, state.speed - 25));
				}
			}
		});
		playPauseRewind.add(rewindButton);

		JButton pauseButton = new JButton("Pause");
		pauseButton
				.setToolTipText("<html>Pauses the simulation.<br/>Shortcut: Middle Mouse Button</html>");
		pauseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.setSpeed(0);

			}
		});
		playPauseRewind.add(pauseButton);

		JButton playButton = new JButton("Play");
		playButton
				.setToolTipText("<html>Starts the simulation, then increases simulation speed.<br/>Shortcut: Middle Mouse Button</html>");
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (state.speed < 25) {
					state.setSpeed(25);
				} else {
					state.setSpeed(Math.min(100, state.speed + 25));
				}
			}
		});
		playPauseRewind.add(playButton);
		// --|--
		JPanel playbackSpeed = new JPanel();
		playbackSpeed
				.setToolTipText("<html>Left side: High speed rewind.<br/>Middle: Paused.<br/>Right side: High speed fast-forward.</html>");
		playbackSpeed.setLayout(new GridLayout(2, 1));
		topHalfControls.add(playbackSpeed);

		JLabel speedSliderLabel = new JLabel("Playback speed", JLabel.CENTER);
		playbackSpeed.add(speedSliderLabel);

		state.speedSlider.setMajorTickSpacing(50);
		state.speedSlider.setMinorTickSpacing(10);
		state.speedSlider.setPaintTicks(true);
		state.speedSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				state.setSpeed(state.speedSlider.getValue());
			}
		});
		playbackSpeed.add(state.speedSlider);
		// <| |>
		JPanel stepGeneration = new JPanel();
		stepGeneration.setLayout(new GridLayout(1, 3));
		topHalfControls.add(stepGeneration);

		JButton stepBackwards = new JButton("Prev");
		stepBackwards
				.setToolTipText("<html>Steps back one generation, if possible.<br/>Shortcut: Mouse Scroll Up</html>");
		stepGeneration.add(stepBackwards);
		stepBackwards.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.setSpeed(0);
				state.previousGenerationIfPossible();
			}
		});
		JPanel generationCounter = new JPanel();
		generationCounter.setLayout(new GridLayout(2, 1));
		generationCounter
				.setToolTipText("Shows the number of elapsed generations.");
		stepGeneration.add(generationCounter);
		JLabel generationLabel = new JLabel("Generation", JLabel.CENTER);
		generationCounter.add(generationLabel);
		state.generationLabel = new JLabel("0", JLabel.CENTER);
		generationCounter.add(state.generationLabel);

		JButton stepForwards = new JButton("Next");
		stepForwards
				.setToolTipText("<html>Steps forwards one generation.<br/>Shortcut: Mouse Scroll Down</html>");
		stepGeneration.add(stepForwards);
		stepForwards.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.setSpeed(0);
				state.nextGenerationIfPossible();
			}
		});
		// X ^v
		JPanel clearTools = new JPanel();
		clearTools.setLayout(new GridLayout(1, 3));
		topHalfControls.add(clearTools);

		JButton clear = new JButton("Clear");
		clear.setToolTipText("<html>Clears the board.<br/>Use the Prev button to undo.</html>");
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.setSpeed(0);
				state.clearBoard();
			}
		});
		clearTools.add(clear);

		JPanel densityLabels = new JPanel();
		densityLabels.setLayout(new GridLayout(2, 1));
		clearTools.add(densityLabels);

		JLabel randomizeDensityLabel = new JLabel("Density", JLabel.CENTER);
		densityLabels.add(randomizeDensityLabel);
		final JLabel randomizeDensity = new JLabel("10%", JLabel.CENTER);
		densityLabels.add(randomizeDensity);

		JButton randomize = new JButton("Randomize");
		randomize
				.setToolTipText("<html>Randomizes the current board,<br/>with the specified density of living cells.<br/>Use the Prev button to undo.</html>");
		randomize.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.randomize();
			}
		});
		clearTools.add(randomize);

		// -|---
		JPanel randomizeDensitySliderContainer = new JPanel();
		randomizeDensitySliderContainer
				.setToolTipText("<html>Left side: Board is completely dead.<br/>Right side: Board is completely alive.<br/>Use the Randomize button after adjusting.</html>");
		randomizeDensitySliderContainer.setLayout(new GridLayout(2, 1));
		topHalfControls.add(randomizeDensitySliderContainer);

		JLabel randomizeDensitySliderLabel = new JLabel(
				"Randomization Density", JLabel.CENTER);
		randomizeDensitySliderContainer.add(randomizeDensitySliderLabel);

		final JSlider randomizeDensitySlider = new JSlider(0, 100, 10);
		randomizeDensitySlider.setMajorTickSpacing(50);
		randomizeDensitySlider.setMinorTickSpacing(10);
		randomizeDensitySlider.setPaintTicks(true);
		randomizeDensitySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				state.fillFactor = (double) randomizeDensitySlider.getValue()
						/ randomizeDensitySlider.getMaximum();
				randomizeDensity.setText(randomizeDensitySlider.getValue()
						+ "%");
			}
		});
		randomizeDensitySliderContainer.add(randomizeDensitySlider);

		final JPanel contextTools = new JPanel();
		final CardLayout contextToolsLayout = new CardLayout();
		contextTools.setLayout(contextToolsLayout);
		topHalfControls.add(contextTools);
		// | |/] []
		JPanel toolBox = new JPanel();
		toolBox.setLayout(new GridLayout(1, 3));
		contextTools.add(toolBox);
		contextToolsLayout.addLayoutComponent(toolBox, "Paint Tools");

		state.drawTool = new JButton("Draw");
		state.drawTool
				.setToolTipText("<html>In Draw mode:<br/>Left Mouse Button: Draw.<br/>Right Mouse Button: Erase.</html>");
		state.drawTool.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.tool = 1;
				state.updateTool();
			}
		});
		state.drawTool.setEnabled(false);
		toolBox.add(state.drawTool);

		state.toggleTool = new JButton("Toggle");
		state.toggleTool
				.setToolTipText("<html>In Toggle mode:<br/>Left Mouse Button: Temporarily sets mode<br/> to opposite the first cell clicked.<br/>Right Mouse Button: Temporarily sets mode<br/> to the first cell clicked.</html>");
		state.toggleTool.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.tool = 2;
				state.updateTool();
			}
		});
		toolBox.add(state.toggleTool);

		state.eraseTool = new JButton("Erase");
		state.eraseTool
				.setToolTipText("<html>In Erase mode:<br/>Left Mouse Button: Erase.<br/>Right Mouse Button: Draw.</html>");
		state.eraseTool.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.tool = 3;
				state.updateTool();
			}
		});
		toolBox.add(state.eraseTool);

		// <- 0 ->
		JPanel rotationControls = new JPanel();
		rotationControls.setLayout(new GridLayout(1, 3));
		contextTools.add(rotationControls);
		contextToolsLayout.addLayoutComponent(rotationControls,
				"Rotation Tools");
		JButton rotateLeft = new JButton("-90 (L)");
		rotateLeft
				.setToolTipText("Rotates the selected pattern 90 degrees left, or counter-clockwise.");
		rotateLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.rotation = (state.rotation + 3) % 4;
				state.updateRotationLabel();
			}
		});
		rotationControls.add(rotateLeft);

		JPanel rotationLabels = new JPanel();
		rotationLabels.setLayout(new GridLayout(2, 1));
		rotationControls.add(rotationLabels);

		JLabel rotationTitleLabel = new JLabel("Rotation", JLabel.CENTER);
		rotationLabels.add(rotationTitleLabel);
		state.rotationLabel = new JLabel("0", JLabel.CENTER);
		rotationLabels.add(state.rotationLabel);

		JButton rotateRight = new JButton("+90 (R)");
		rotateRight
				.setToolTipText("<html>Rotates the selected pattern 90 degrees right, or counter-clockwise.<br/>Shortcut: Right Mouse Button</html>");
		rotateRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				state.rotation = (state.rotation + 1) % 4;
				state.updateRotationLabel();
			}
		});
		rotationControls.add(rotateRight);
		// ===
		final JTree presetSelection = new JTree(GameOfLifePresets.PRESETS);
		presetSelection.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		presetSelection.setSelectionPath(new TreePath(
				new DefaultMutableTreeNode[] { GameOfLifePresets.PRESETS,
						GameOfLifePresets.initialSelectionCategory,
						GameOfLifePresets.initialSelection }));
		presetSelection.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) presetSelection
						.getLastSelectedPathComponent();
				if (selectedNode != null && selectedNode.isLeaf()) {
					state.selectedPattern = ((Preset) (selectedNode
							.getUserObject())).getPreset();
					state.isBrush = selectedNode.getParent() == GameOfLifePresets.initialSelectionCategory;
					contextToolsLayout.show(contextTools,
							state.isBrush ? "Paint Tools" : "Rotation Tools");
				}
			}
		});
		JScrollPane presetSelectionScroll = new JScrollPane(presetSelection);
		overallControls.add(presetSelectionScroll);
		// Make the entire frame visible
		controls.setVisible(true);
	}
}

/**
 * A class defining the current state of the Game Of Life object including UI
 * elements, selected tool, speed, and more. Also provides utility methods to
 * clear or reset the board.
 * 
 * Written 11/4/2013
 * 
 * @see GameOfLifeDisplay
 * @see GameOfLifeDrawingComponent
 * 
 * @author William Wu
 * 
 */
class GameOfLifeState {
	public long generationCount = 0;
	public boolean mouseDown = false;
	public boolean rightMouseDown = false;
	public boolean mouseInFrame = false;
	public double fillFactor = 0.1;
	public Point mousePosition;
	public int tool = 1;// 1 - Draw, 2 - Toggle, 3 - Erase
	public int temptool = 0;
	public JSlider speedSlider = null;
	public JLabel generationLabel = null;
	public JButton drawTool = null;
	public JButton toggleTool = null;
	public JButton eraseTool = null;
	public JLabel rotationLabel = null;
	public int speed = 0;
	public int time = 0;
	public final int MIN_SPEED = 5000;// In 10ths of seconds
	public byte currentBuffer = GameOfLife.BUFFERS;
	public long lastTick = 0;
	public int[][] selectedPattern = ((Preset) (GameOfLifePresets.initialSelection
			.getUserObject())).getPreset();
	public boolean isBrush = true;
	public boolean drawMore = false;
	public int rotation = 0;
	private GameOfLife game;

	/**
	 * Creates a State object based on a given game.
	 * 
	 * @param game
	 *            the given game.
	 */
	public GameOfLifeState(GameOfLife game) {
		this.game = game;
	}

	/**
	 * Updates the label of the rotation.
	 */
	public void updateRotationLabel() {
		switch (this.rotation) {
		case 1:
			this.rotationLabel.setText("90");
			break;
		case 2:
			this.rotationLabel.setText("180");
			break;
		case 3:
			this.rotationLabel.setText("270");
			break;
		default:
			this.rotationLabel.setText("0");
			break;
		}

	}

	/**
	 * Sets the speed of the simulation, with constraints.
	 * 
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = (int) Math.max(Math.min(speed, 100), -100);
		this.time = 0;
		this.speedSlider.setValue(this.speed);
	}

	/**
	 * Sets the game object reference.
	 * 
	 * @param game
	 *            the game to set
	 */
	public void setGame(GameOfLife game) {
		this.game = game;
	}

	/**
	 * Steps forward to the next generation, keeping the buffer in bounds and
	 * simulating only if necessary.
	 */
	public void nextGenerationIfPossible() {
		if (this.currentBuffer < GameOfLife.BUFFERS) {
			this.currentBuffer++;
		} else {
			this.game.nextGeneration();
		}
		this.generationCount++;
		this.generationLabel.setText(this.generationCount + "");
	}

	/**
	 * Rewinds to the previous generation, keeping the buffer in bounds.
	 */
	public void previousGenerationIfPossible() {
		if (this.currentBuffer > 0 && this.generationCount > 0) {
			this.currentBuffer--;
			this.generationCount--;
			this.generationLabel.setText(this.generationCount + "");
		}
	}

	/**
	 * Clears the history after the current frame, resetting the board to the
	 * present.
	 */
	public void toPresent() {
		if (this.currentBuffer < GameOfLife.BUFFERS) {
			this.game.bitShiftLeft(GameOfLife.BUFFERS - this.currentBuffer);
			this.currentBuffer = GameOfLife.BUFFERS;
		}
	}

	/**
	 * Clears the board.
	 */
	public void clearBoard() {
		this.toPresent();
		this.game.clearBoard();
	}

	/**
	 * Randomizes the board with the stored density.
	 */
	public void randomize() {
		this.toPresent();
		this.game.randomize(this.fillFactor);
	}

	/**
	 * Updates the tools in the UI.
	 */
	public void updateTool() {
		int currentTool = (this.temptool > 0 ? this.temptool : this.tool);
		if (this.rightMouseDown) {
			currentTool = 4 - currentTool;
		}
		this.drawTool.setEnabled(currentTool != 1);
		this.toggleTool.setEnabled(currentTool != 2);
		this.eraseTool.setEnabled(currentTool != 3);
	}

	/**
	 * Gets the rotated coordinate from the selected pattern
	 * 
	 * @param row
	 *            the row coordinate
	 * @param col
	 *            the column coordinate
	 * @return the value from the rotated coordinates
	 */
	public int getRotated(int row, int col) {
		int patternWidth = this.selectedPattern[0].length, patternHeight = this.selectedPattern.length;
		switch (this.rotation) {
		case 1:
			return this.selectedPattern[patternHeight - 1 - col][row];
		case 2:
			return this.selectedPattern[patternHeight - 1 - row][patternWidth
					- 1 - col];
		case 3:
			return this.selectedPattern[col][patternWidth - 1 - row];
		default:
			return this.selectedPattern[row][col];
		}
	}
}

/**
 * The component that draws the current board state, as well as handles timing,
 * mouse interaction, and generation progression.
 * 
 * Written 11/4/2013
 * 
 * @see GameOfLifeDisplay
 * 
 * @author William Wu
 * 
 */
class GameOfLifeDrawingComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	private GameOfLife game;
	private GameOfLifeState state;
	private static final Color backgroundColor = new Color(255, 255, 255, 0);
	private static final Color foregroundColor = new Color(0x000000);
	private static final Color secondaryColor = new Color(0x88CCFF);
	private double cellWidth, cellHeight, fromTop, fromLeft;
	private boolean showGrid;
	private Rectangle2D sharedRectangle = new Rectangle2D.Double();
	private Line2D sharedLine = new Line2D.Double();

	/**
	 * Creates a new Drawing Component based on a given game.
	 * 
	 * @param game
	 */
	public GameOfLifeDrawingComponent(final GameOfLife game) {
		this.game = game;
		// Make it resize properly
		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				((GameOfLifeDrawingComponent) e.getComponent())
						.recalculateLayout();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					state.rightMouseDown = false;
					if (!state.mouseDown) {
						state.temptool = 0;
					}
				} else if (e.getButton() == MouseEvent.BUTTON2) {
					state.setSpeed(0);
				} else {
					if (!state.rightMouseDown) {
						state.temptool = 0;
					}
					state.mouseDown = false;
				}
				state.updateTool();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					if (!state.isBrush) {
						state.rotation = (state.rotation + 1) % 4;
						state.updateRotationLabel();
					}
					state.rightMouseDown = true;
				} else if (e.getButton() == MouseEvent.BUTTON2) {
					if (state.speed == 0) {
						state.setSpeed(75);
					} else {
						state.setSpeed(0);
					}
				} else {
					state.mouseDown = true;
				}
				if (e.getButton() != MouseEvent.BUTTON2) {
					state.drawMore = true;
					int mouseCol = (int) ((state.mousePosition.x - fromLeft) / cellWidth);
					int mouseRow = (int) ((state.mousePosition.y - fromTop) / cellHeight);
					if (state.tool == 2 && state.temptool == 0) {
						state.temptool = game
								.getCurrentCell(mouseRow, mouseCol) == 0 ? 1
								: 3;
					}
				}
				state.updateTool();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				state.mouseInFrame = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				state.mouseInFrame = true;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				state.mousePosition = e.getPoint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				state.mousePosition = e.getPoint();
			}
		});
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				state.setSpeed(0);
				if (e.getPreciseWheelRotation() > 0) {
					state.nextGenerationIfPossible();
				} else {
					state.previousGenerationIfPossible();
				}
			}
		});
	}

	/**
	 * Draws the game of life.
	 */
	public void paintComponent(Graphics graphics) {
		if (cellWidth == 0) {
			recalculateLayout();
		}
		// Extract Graphics2D
		Graphics2D g = (Graphics2D) graphics;
		// Draw background
		g.setColor(backgroundColor);
		g.fill(this.getBounds());
		g.setColor(foregroundColor);
		// Mouse stuff
		int mouseRow = 0, mouseCol = 0;
		if (state.mouseInFrame) {
			mouseCol = (int) ((state.mousePosition.x - fromLeft) / cellWidth);
			mouseRow = (int) ((state.mousePosition.y - fromTop) / cellHeight);
		}
		// Draw Crosshair
		if (state.mouseInFrame) {
			double cellTopLeftCol = mouseCol * cellWidth + fromLeft, cellTopLeftRow = mouseRow
					* cellHeight + fromTop;
			int patternWidth = state.rotation % 2 == 0 ? state.selectedPattern[0].length
					: state.selectedPattern.length, patternHeight = state.rotation % 2 == 0 ? state.selectedPattern.length
					: state.selectedPattern[0].length, patternCol = (int) ((state.mousePosition.x - fromLeft)
					/ cellWidth - 0.5 * patternWidth + 0.5), patternRow = (int) ((state.mousePosition.y - fromTop)
					/ cellHeight - 0.5 * patternHeight + 0.5);
			g.setColor(secondaryColor);
			for (int row = 0; row < patternHeight; row++) {
				for (int col = 0; col < patternWidth; col++) {
					if (state.getRotated(row, col) != 0) {
						sharedRectangle.setRect((patternCol + col) * cellWidth
								+ fromLeft, (patternRow + row) * cellHeight
								+ fromTop, cellWidth, cellHeight);
						g.fill(sharedRectangle);

					}
				}
			}
			g.setColor(foregroundColor);
			if (state.isBrush) {
				sharedLine.setLine(fromLeft, cellTopLeftRow + 0.5 * cellWidth,
						fromLeft + this.game.getWidth() * cellWidth,
						cellTopLeftRow + 0.5 * cellWidth);
				g.draw(sharedLine);
				sharedLine.setLine(cellTopLeftCol + 0.5 * cellHeight, 0,
						cellTopLeftCol + 0.5 * cellHeight,
						fromTop + this.game.getHeight() * cellWidth);
				g.draw(sharedLine);
			}
			// Consider mouse down, adding/removing cells
			if (state.mouseDown || state.rightMouseDown) {
				this.state.toPresent();
				if (this.state.speed < 0) {
					this.state.setSpeed(0);
				}
				if (state.isBrush || !state.isBrush && state.drawMore
						&& !state.rightMouseDown) {
					for (int row = 0; row < patternHeight; row++) {
						for (int col = 0; col < patternWidth; col++) {
							if (state.getRotated(row, col) != 0) {
								game.setCurrentCell(
										patternRow + row,
										patternCol + col,
										!state.isBrush ? 1
												: (state.temptool > 0 ? state.temptool
														: state.tool) == (state.rightMouseDown ? 3
														: 1) ? 1 : 0);
							}
						}
					}
				}
			} else if (!state.rightMouseDown) {
				state.temptool = 0;
			}
			state.drawMore = false;
		}

		for (int row = 0; row < this.game.getHeight(); row++) {
			for (int col = 0; col < this.game.getWidth(); col++) {
				double cellTopLeftCol = col * cellWidth + fromLeft;
				double cellTopLeftRow = row * cellHeight + fromTop;
				// g.setColor(new Color((int) (0xFFFFFF*Math.random())));
				int buffer = (int) state.currentBuffer;
				for (; buffer >= 0; buffer--) {
					if (this.game.getCell(row, col, buffer) != 0) {
						if (buffer == state.currentBuffer) {
							sharedRectangle.setRect(cellTopLeftCol,
									cellTopLeftRow, cellWidth, cellHeight);
							g.fill(sharedRectangle);
						} else {
							double size = (double) buffer
									/ (state.currentBuffer + 1);
							sharedRectangle.setRect(cellTopLeftCol
									+ (1.0 - size) * cellWidth * 0.5,
									cellTopLeftRow + (1.0 - size) * cellHeight
											* 0.5, cellWidth * size, cellHeight
											* size);
							g.draw(sharedRectangle);
						}
						break;
					}
				}

				if (buffer < 0
						&& this.showGrid
						&& (!state.isBrush || !this.state.mouseInFrame || (row != mouseRow && col != mouseCol))) {
					double pointRadius = 0.5;
					sharedRectangle
							.setRect(cellTopLeftCol + 0.5 * cellWidth
									- pointRadius, cellTopLeftRow + 0.5
									* cellHeight - pointRadius,
									pointRadius * 2.0, pointRadius * 2.0);
					g.fill(sharedRectangle);
				}

			}
		}
		long currentTime = new Date().getTime();
		boolean clock = false;
		if (this.state.speed != 0
				&& currentTime - state.lastTick > this.state.MIN_SPEED
						/ (Math.abs(this.state.speed)) - this.state.MIN_SPEED
						/ 100) {
			clock = true;
		}
		if (clock) {
			this.state.lastTick = currentTime;
		}
		// Handle rewind
		if (this.state.speed < 0) {
			this.state.time++;
			if (clock) {
				this.state.previousGenerationIfPossible();
			}
		}
		// Handle forwards speed
		else if (this.state.speed > 0) {
			this.state.time++;
			if (clock) {
				this.state.nextGenerationIfPossible();
			}
		}
		this.repaint();
	}

	/**
	 * Recalculates the current layout for the game board, taking into account
	 * the width and height of the window.
	 * 
	 * @param width
	 *            the specified width of the window
	 * @param height
	 *            the specified height of the window
	 */
	private void recalculateLayout() {
		double cellSide = Math.min(
				(double) this.getWidth() / this.game.getWidth(),
				(double) this.getHeight() / this.game.getHeight());
		this.cellWidth = cellSide;
		this.cellHeight = cellSide;
		this.fromLeft = Math.max(this.getWidth() - this.getHeight(), 0) / 2;
		this.fromTop = Math.max(this.getHeight() - this.getWidth(), 0) / 2;
		this.showGrid = !(this.getWidth() < 200 || this.getHeight() < 200);
	}

	/**
	 * Sets the state of the component
	 * 
	 * @param state
	 *            the state to set
	 */
	public void setState(GameOfLifeState state) {
		this.state = state;
	}
}