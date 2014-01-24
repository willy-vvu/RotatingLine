/**
 * Matrix4.java
 * 
 * A class to create and manipulate 4x4 matrices
 * 
 * Written Jan 23, 2014.
 * 
 * @author William Wu
 * 
 */
public class Matrix4 {
	private double[][] matrix = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 },
			{ 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
	private static final double[][] temp = new double[4][4];

	/**
	 * Creates a new identity matrix.
	 */
	public Matrix4() {

	}

	/**
	 * Creates a new matrix from an existing double matrix.
	 * 
	 * @param matrix
	 */
	public Matrix4(double[][] matrix) {
		this.copy(matrix);
	}

	/**
	 * Copies the values from another matrix into itself
	 * 
	 * @param matrix
	 * @return itself
	 */
	public Matrix4 copy(double[][] matrix) {
		for (int i = 0; i < matrix.length && i < 4; i++) {
			for (int j = 0; j < matrix.length && j < 4; j++) {
				this.matrix[i][j] = matrix[i][j];
			}
		}
		return this;
	}

	/**
	 * Multiply (this matrix) x (another matrix) into itself.
	 * 
	 * @return itself
	 */
	public Matrix4 multiplyBeforeMatrix(Matrix4 matrix) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				double sum = 0;
				for (int k = 0; k < 4; k++) {
					sum += this.getAt(i, k) * matrix.getAt(k, j);
				}
				Matrix4.temp[i][j] = sum;
			}
		}
		return this.copy(Matrix4.temp);
	}

	/**
	 * Multiply (another matrix) x (this matrix) into itself.
	 * 
	 * @return itself
	 */
	public Matrix4 multiplyAfterMatrix(Matrix4 matrix) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				double sum = 0;
				for (int k = 0; k < 4; k++) {
					sum += matrix.getAt(i, k) * this.getAt(k, j);
				}
				Matrix4.temp[i][j] = sum;
			}
		}
		return this.copy(Matrix4.temp);
	}

	/**
	 * Resets a modified matrix back to an identity matrix.
	 * 
	 * @return itself
	 */
	public Matrix4 makeIdentity() {
		this.set(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
		return this;
	}

	/**
	 * Sets the rotation portion of the matrix to a specified rotation around a
	 * unit vector defined axis.
	 * 
	 * See http://en.wikipedia.org/wiki/Rotation_matrix#
	 * Rotation_matrix_from_axis_and_angle
	 * 
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 * @return itself
	 */
	public Matrix4 setRotation(double angle, double x, double y, double z) {
		double c = Math.cos(angle), d = 1 - c, s = Math.sqrt(1 - c * c);
		this.set(c + x * d, x * y * d - z * s, x * z * d + y * s, y * x * d + z
				* s, c + y * d, y * z * d - x * s, z * x * d - y * s, z * y * d
				+ x * s, c + z * d);
		return this;
	}

	/**
	 * Sets the rotation portion of the matrix to a specified rotation around a
	 * unit vector defined axis.
	 * 
	 * @param angle
	 * @param vector
	 * @return itself
	 */
	public Matrix4 setRotation(double angle, Vector3 vector) {
		return this.setRotation(vector.getX(), vector.getY(), vector.getZ(),
				angle);
	}

	/**
	 * Sets the rotation portion of the matrix to a Rotation object
	 * 
	 * @param rotation
	 * @return itself
	 */
	public Matrix4 setRotation(Rotation rotation) {
		return this.setRotation(rotation.getAngle(), rotation);
	}

	/**
	 * Sets the rotation portion of the matrix to a specified rotation around
	 * the x axis.
	 * 
	 * @param angle
	 * @return itself
	 */
	public Matrix4 setRotationX(double angle) {
		return this.setRotation(1, 0, 0, angle);
	}

	/**
	 * Sets the rotation portion of the matrix to a specified rotation around
	 * the y axis.
	 * 
	 * @param angle
	 * @return itself
	 */
	public Matrix4 setRotationY(double angle) {
		return this.setRotation(0, 1, 0, angle);
	}

	/**
	 * Sets the rotation portion of the matrix to a specified rotation around
	 * the z axis.
	 * 
	 * @param angle
	 * @return itself
	 */
	public Matrix4 setRotationZ(double angle) {
		return this.setRotation(0, 0, 1, angle);
	}

	/**
	 * Sets the translation portion of the matrix to a specified translation
	 * given by a vector.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Matrix4 setTranslation(double x, double y, double z) {
		this.setTranslationX(x);
		this.setTranslationY(y);
		this.setTranslationZ(z);
		return this;
	}

	/**
	 * Sets the translation portion of the matrix to a specified translation
	 * given by a vector.
	 * 
	 * @param vector
	 * @return itself
	 */
	public Matrix4 setTranslation(Vector3 vector) {
		return this.setTranslation(vector.getX(), vector.getY(), vector.getZ());
	}

	/**
	 * Sets the x translation portion of the matrix.
	 * 
	 * @param x
	 * @return itself
	 */
	public Matrix4 setTranslationX(double x) {
		this.setAt(0, 3, x);
		return this;
	}

	/**
	 * Sets the y translation portion of the matrix.
	 * 
	 * @param y
	 * @return itself
	 */
	public Matrix4 setTranslationY(double y) {
		this.setAt(1, 3, y);
		return this;
	}

	/**
	 * Sets the z translation portion of the matrix.
	 * 
	 * @param z
	 * @return itself
	 */
	public Matrix4 setTranslationZ(double z) {
		this.setAt(2, 3, z);
		return this;
	}

	/**
	 * Returns whether or not a specified position in the matrix is valid.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isValid(int row, int col) {
		return -1 < row && row < 5 && -1 < col && col < 5;
	}

	/**
	 * Gets the value of the matrix at a speficied row and column.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public double getAt(int row, int col) {
		return isValid(row, col) ? this.matrix[row][col] : 0;
	}

	/**
	 * Sets a value of the matrix at a specified row and column.
	 * 
	 * @param row
	 * @param col
	 * @param value
	 */
	public void setAt(int row, int col, double value) {
		if (isValid(row, col)) {
			this.matrix[row][col] = value;
		}
	}

	/**
	 * Set multiple values of the matrix at once.
	 * 
	 * Sets the upper left 3x3 submatrix in the following order:
	 * 
	 * n1, n2, n3
	 * 
	 * n4, n5, n6
	 * 
	 * n7, n8, n9
	 * 
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param n4
	 * @param n5
	 * @param n6
	 * @param n7
	 * @param n8
	 * @param n9
	 */
	public void set(double n1, double n2, double n3, double n4, double n5,
			double n6, double n7, double n8, double n9) {
		this.setAt(0, 0, n1);
		this.setAt(0, 1, n2);
		this.setAt(0, 2, n3);
		this.setAt(1, 0, n4);
		this.setAt(1, 1, n5);
		this.setAt(1, 2, n6);
		this.setAt(2, 0, n7);
		this.setAt(2, 1, n8);
		this.setAt(2, 2, n9);
	}

	/**
	 * Set multiple values of the matrix at once.
	 * 
	 * Sets the entire matrix in the following order:
	 * 
	 * n1, n2, n3, n4
	 * 
	 * n5, n6, n7, n8
	 * 
	 * n9, n10, n11, n12
	 * 
	 * n13, n14, n15, n16
	 * 
	 * @param n1
	 * @param n2
	 * @param n3
	 * @param n4
	 * @param n5
	 * @param n6
	 * @param n7
	 * @param n8
	 * @param n9
	 */
	public void set(double n1, double n2, double n3, double n4, double n5,
			double n6, double n7, double n8, double n9, double n10, double n11,
			double n12, double n13, double n14, double n15, double n16) {
		this.set(n1, n2, n3, n5, n6, n7, n9, n10, n11);
		this.setAt(0, 3, n4);
		this.setAt(1, 3, n8);
		this.setAt(2, 3, n12);
		this.setAt(3, 0, n13);
		this.setAt(3, 1, n14);
		this.setAt(3, 2, n15);
		this.setAt(3, 3, n16);
	}
}
