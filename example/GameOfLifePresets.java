import javax.swing.tree.DefaultMutableTreeNode;

/**
 * GameOfLifePresets.java - A class providing several predefined presets for use
 * in the GameOfLifeDisplay class. Presets taken from Wikipedia.
 * 
 * @see GameOfLifeDisplay
 * 
 * @author William Wu
 * 
 */
public class GameOfLifePresets {
	public static final DefaultMutableTreeNode PRESETS;
	public static DefaultMutableTreeNode initialSelection;
	public static DefaultMutableTreeNode initialSelectionCategory;
	// Fill the preset object with the presets
	static {
		PRESETS = new DefaultMutableTreeNode("Presets", true);
		DefaultMutableTreeNode category = initialSelectionCategory = new DefaultMutableTreeNode(
				"Brushes", true);
		category.add(initialSelection = new DefaultMutableTreeNode(new Preset(
				"1 x 1", 1)));
		category.add(new DefaultMutableTreeNode(new Preset("3 x 3", 3)));
		category.add(new DefaultMutableTreeNode(new Preset("5 x 5", 5)));
		category.add(new DefaultMutableTreeNode(new Preset("9 x 9", 9)));
		category.add(new DefaultMutableTreeNode(new Preset("15 x 15", 15)));
		PRESETS.add(category);
		category = new DefaultMutableTreeNode("Still Lifes", true);
		category.add(new DefaultMutableTreeNode(new Preset("Block",
				new int[][] { { 1, 1 }, { 1, 1 } })));
		category.add(new DefaultMutableTreeNode(new Preset("Beehive",
				new int[][] { { 0, 1, 1, 0 }, { 1, 0, 0, 1 }, { 0, 1, 1, 0 } })));
		category.add(new DefaultMutableTreeNode(new Preset("Loaf",
				new int[][] { { 0, 1, 1, 0 }, { 1, 0, 0, 1 }, { 0, 1, 0, 1 },
						{ 0, 0, 1, 0 } })));
		category.add(new DefaultMutableTreeNode(new Preset("Boat", new int[][] {
				{ 1, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } })));
		PRESETS.add(category);
		category = new DefaultMutableTreeNode("Oscillators", true);
		category.add(new DefaultMutableTreeNode(new Preset(
				"Blinker (period 2)", new int[][] { { 1, 1, 1 } })));
		category.add(new DefaultMutableTreeNode(new Preset("Toad (period 2)",
				new int[][] { { 0, 1, 1, 1 }, { 1, 1, 1, 0 } })));
		category.add(new DefaultMutableTreeNode(new Preset("Beacon (period 2)",
				new int[][] { { 1, 1, 0, 0 }, { 1, 0, 0, 0 }, { 0, 0, 0, 1 },
						{ 0, 0, 1, 1 } })));
		category.add(new DefaultMutableTreeNode(new Preset("Pulsar (period 3)",
				new int[][] { { 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
						{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
						{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
						{ 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0 },
						{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
						{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
						{ 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
						{ 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0 }, })));
		PRESETS.add(category);
		category = new DefaultMutableTreeNode("Spaceships", true);
		category.add(new DefaultMutableTreeNode(new Preset("Glider",
				new int[][] { { 0, 1, 0 }, { 0, 0, 1 }, { 1, 1, 1 } })));
		category.add(new DefaultMutableTreeNode(new Preset(
				"Lightweight Spaceship (LWSS)", new int[][] {
						{ 1, 0, 0, 1, 0 }, { 0, 0, 0, 0, 1 },
						{ 1, 0, 0, 0, 1 }, { 0, 1, 1, 1, 1 } })));
		PRESETS.add(category);

		category = new DefaultMutableTreeNode("Methuselahs", true);
		category.add(new DefaultMutableTreeNode(new Preset("R-pentomino",
				new int[][] { { 0, 1, 1 }, { 1, 1, 0 }, { 0, 1, 0 } })));
		category.add(new DefaultMutableTreeNode(
				new Preset("Diehard",
						new int[][] { { 0, 0, 0, 0, 0, 0, 1, 0 },
								{ 1, 1, 0, 0, 0, 0, 0, 0 },
								{ 0, 1, 0, 0, 0, 1, 1, 1 } })));
		category.add(new DefaultMutableTreeNode(new Preset("Acorn",
				new int[][] { { 0, 1, 0, 0, 0, 0, 0 }, { 0, 0, 0, 1, 0, 0, 0 },
						{ 1, 1, 0, 0, 1, 1, 1 } })));
		PRESETS.add(category);

		category = new DefaultMutableTreeNode("Glider Guns", true);
		category.add(new DefaultMutableTreeNode(new Preset("Gosper Glider Gun",
				new int[][] {
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
								0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								1, 1 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0,
								0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								1, 1 },
						{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0,
								0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								0, 0 },
						{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1,
								0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0,
								0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0,
								0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								0, 0 },
						{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0,
								0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
								0, 0 } })));
		PRESETS.add(category);
		category = new DefaultMutableTreeNode("Infinite Growth", true);
		category.add(new DefaultMutableTreeNode(new Preset("Minimal",
				new int[][] { { 0, 0, 0, 0, 0, 0, 1, 0 },
						{ 0, 0, 0, 0, 1, 0, 1, 1 }, { 0, 0, 0, 0, 1, 0, 1, 0 },
						{ 0, 0, 0, 0, 1, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0, 0, 0 },
						{ 1, 0, 1, 0, 0, 0, 0, 0 } })));
		category.add(new DefaultMutableTreeNode(
				new Preset("Square", new int[][] { { 1, 1, 1, 0, 1 },
						{ 1, 0, 0, 0, 0 }, { 0, 0, 0, 1, 1 },
						{ 0, 1, 1, 0, 1 }, { 1, 0, 1, 0, 1 } })));
		category.add(new DefaultMutableTreeNode(new Preset("Strip",
				new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0,
						0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0,
						1, 1, 1, 1, 1 } })));
		PRESETS.add(category);
	}
}

/**
 * A class to represent a single Game of Life preset.
 * 
 * Written 11/24/2013
 * 
 * @author William Wu
 * 
 */
class Preset {
	private String name;
	private int[][] preset;

	/**
	 * Creates a preset given a name and a 2d preset array.
	 * 
	 * @param name
	 * @param preset
	 */
	public Preset(String name, int[][] preset) {
		this.name = name;
		this.preset = preset;
	}

	/**
	 * Creates a square preset of the given size.
	 * 
	 * @param name
	 * @param size
	 */
	public Preset(String name, int size) {
		this(name, new int[size][size]);
		for (int i = 0; i < this.preset.length; i++) {
			for (int j = 0; j < this.preset[i].length; j++) {
				this.preset[i][j] = 1;
			}
		}
	}

	/**
	 * Returns the name of the preset
	 */
	public String toString() {
		return this.name;
	}

	/**
	 * Returns the name of the preset
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the preset itself
	 * 
	 * @return the preset
	 */
	public int[][] getPreset() {
		return preset;
	}
}