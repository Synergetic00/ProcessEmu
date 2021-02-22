package types;

import static utils.DataUtils.*;
import static utils.MathUtils.*;

public class PMatrix2D implements PMatrix {

	public double m00, m01, m02;
	public double m10, m11, m12;


	/**
	 * Create a new matrix, set to the identity matrix.
	 */
	public PMatrix2D() {
		reset();
	}


	public PMatrix2D(double m00, double m01, double m02,
			double m10, double m11, double m12) {
		set(m00, m01, m02,
				m10, m11, m12);
	}


	public PMatrix2D(PMatrix matrix) {
		set(matrix);
	}


	public void reset() {
		set(1, 0, 0,
				0, 1, 0);
	}


	/**
	 * Returns a copy of this PMatrix.
	 */
	public PMatrix2D get() {
		PMatrix2D outgoing = new PMatrix2D();
		outgoing.set(this);
		return outgoing;
	}


	/**
	 * Copies the matrix contents into a 6 entry double array.
	 * If target is null (or not the correct size), a new array will be created.
	 * Returned in the order {@code {m00, m01, m02, m10, m11, m12}}.
	 */
	public double[] get(double[] target) {
		if ((target == null) || (target.length != 6)) {
			target = new double[6];
		}
		target[0] = m00;
		target[1] = m01;
		target[2] = m02;

		target[3] = m10;
		target[4] = m11;
		target[5] = m12;

		return target;
	}


	/**
	 * If matrix is a PMatrix2D, sets this matrix to be a copy of it.
	 * @throws IllegalArgumentException If <tt>matrix</tt> is not 2D.
	 */
	public void set(PMatrix matrix) {
		if (matrix instanceof PMatrix2D) {
			PMatrix2D src = (PMatrix2D) matrix;
			set(src.m00, src.m01, src.m02,
					src.m10, src.m11, src.m12);
		} else {
			throw new IllegalArgumentException("PMatrix2D.set() only accepts PMatrix2D objects.");
		}
	}


	/**
	 * Unavailable in 2D. Does nothing.
	 */
	public void set(PMatrix3D src) {
	}


	public void set(double[] source) {
		m00 = source[0];
		m01 = source[1];
		m02 = source[2];

		m10 = source[3];
		m11 = source[4];
		m12 = source[5];
	}


	/**
	 * Sets the matrix content.
	 */
	public void set(double m00, double m01, double m02,
			double m10, double m11, double m12) {
		this.m00 = m00; this.m01 = m01; this.m02 = m02;
		this.m10 = m10; this.m11 = m11; this.m12 = m12;
	}


	/**
	 * Unavailable in 2D. Does nothing.
	 */
	public void set(double m00, double m01, double m02, double m03,
			double m10, double m11, double m12, double m13,
			double m20, double m21, double m22, double m23,
			double m30, double m31, double m32, double m33) {

	}


	public void translate(double tx, double ty) {
		m02 = tx*m00 + ty*m01 + m02;
		m12 = tx*m10 + ty*m11 + m12;
	}


	/**
	 * Unavailable in 2D.
	 * @throws IllegalArgumentException
	 */
	public void translate(double x, double y, double z) {
		throw new IllegalArgumentException("Cannot use translate(x, y, z) on a PMatrix2D.");
	}


	// Implementation roughly based on AffineTransform.
	public void rotate(double angle) {
		double s = (double) sin(angle);
		double c = (double) cos(angle);

		double temp1 = m00;
		double temp2 = m01;
		m00 =  c * temp1 + s * temp2;
		m01 = -s * temp1 + c * temp2;
		temp1 = m10;
		temp2 = m11;
		m10 =  c * temp1 + s * temp2;
		m11 = -s * temp1 + c * temp2;
	}


	/**
	 * Unavailable in 2D.
	 * @throws IllegalArgumentException
	 */
	public void rotateX(double angle) {
		throw new IllegalArgumentException("Cannot use rotateX() on a PMatrix2D.");
	}


	/**
	 * Unavailable in 2D.
	 * @throws IllegalArgumentException
	 */
	public void rotateY(double angle) {
		throw new IllegalArgumentException("Cannot use rotateY() on a PMatrix2D.");
	}


	public void rotateZ(double angle) {
		rotate(angle);
	}


	/**
	 * Unavailable in 2D.
	 * @throws IllegalArgumentException
	 */
	public void rotate(double angle, double v0, double v1, double v2) {
		throw new IllegalArgumentException("Cannot use this version of rotate() on a PMatrix2D.");
	}


	public void scale(double s) {
		scale(s, s);
	}


	public void scale(double sx, double sy) {
		m00 *= sx;  m01 *= sy;
		m10 *= sx;  m11 *= sy;
	}


	/**
	 * Unavailable in 2D.
	 * @throws IllegalArgumentException
	 */
	public void scale(double x, double y, double z) {
		throw new IllegalArgumentException("Cannot use this version of scale() on a PMatrix2D.");
	}


	public void shearX(double angle) {
		apply(1, 0, 1, (double) tan(angle), 0, 0);
	}


	public void shearY(double angle) {
		apply(1, 0, 1, 0, (double) tan(angle), 0);
	}


	public void apply(PMatrix source) {
		if (source instanceof PMatrix2D) {
			apply((PMatrix2D) source);
		} else if (source instanceof PMatrix3D) {
			apply((PMatrix3D) source);
		}
	}


	public void apply(PMatrix2D source) {
		apply(source.m00, source.m01, source.m02,
				source.m10, source.m11, source.m12);
	}


	/**
	 * Unavailable in 2D.
	 * @throws IllegalArgumentException
	 */
	public void apply(PMatrix3D source) {
		throw new IllegalArgumentException("Cannot use apply(PMatrix3D) on a PMatrix2D.");
	}


	public void apply(double n00, double n01, double n02,
			double n10, double n11, double n12) {
		double t0 = m00;
		double t1 = m01;
		m00  = n00 * t0 + n10 * t1;
		m01  = n01 * t0 + n11 * t1;
		m02 += n02 * t0 + n12 * t1;

		t0 = m10;
		t1 = m11;
		m10  = n00 * t0 + n10 * t1;
		m11  = n01 * t0 + n11 * t1;
		m12 += n02 * t0 + n12 * t1;
	}


	/**
	 * Unavailable in 2D.
	 * @throws IllegalArgumentException
	 */
	public void apply(double n00, double n01, double n02, double n03,
			double n10, double n11, double n12, double n13,
			double n20, double n21, double n22, double n23,
			double n30, double n31, double n32, double n33) {
		throw new IllegalArgumentException("Cannot use this version of apply() on a PMatrix2D.");
	}


	/**
	 * Apply another matrix to the left of this one.
	 */
	public void preApply(PMatrix source) {
		if (source instanceof PMatrix2D) {
			preApply((PMatrix2D) source);
		} else if (source instanceof PMatrix3D) {
			preApply((PMatrix3D) source);
		}
	}


	public void preApply(PMatrix2D left) {
		preApply(left.m00, left.m01, left.m02,
				left.m10, left.m11, left.m12);
	}


	/**
	 * Unavailable in 2D.
	 * @throws IllegalArgumentException
	 */
	public void preApply(PMatrix3D left) {
		throw new IllegalArgumentException("Cannot use preApply(PMatrix3D) on a PMatrix2D.");
	}


	public void preApply(double n00, double n01, double n02,
			double n10, double n11, double n12) {
		double t0 = m02;
		double t1 = m12;
		n02 += t0 * n00 + t1 * n01;
		n12 += t0 * n10 + t1 * n11;

		m02 = n02;
		m12 = n12;

		t0 = m00;
		t1 = m10;
		m00 = t0 * n00 + t1 * n01;
		m10 = t0 * n10 + t1 * n11;

		t0 = m01;
		t1 = m11;
		m01 = t0 * n00 + t1 * n01;
		m11 = t0 * n10 + t1 * n11;
	}


	/**
	 * Unavailable in 2D.
	 * @throws IllegalArgumentException
	 */
	public void preApply(double n00, double n01, double n02, double n03,
			double n10, double n11, double n12, double n13,
			double n20, double n21, double n22, double n23,
			double n30, double n31, double n32, double n33) {
		throw new IllegalArgumentException("Cannot use this version of preApply() on a PMatrix2D.");
	}


	//////////////////////////////////////////////////////////////


	/**
	 * {@inheritDoc}
	 * Ignores any z component.
	 */
	public PVector mult(PVector source, PVector target) {
		if (target == null) {
			target = new PVector();
		}
		target.x = m00*source.x + m01*source.y + m02;
		target.y = m10*source.x + m11*source.y + m12;
		return target;
	}


	/**
	 * Multiply a two element vector against this matrix.
	 * If out is null or not length four, a new double array will be returned.
	 * The values for vec and out can be the same (though that's less efficient).
	 */
	public double[] mult(double[] vec, double[] out) {
		if (out == null || out.length != 2) {
			out = new double[2];
		}

		if (vec == out) {
			double tx = m00*vec[0] + m01*vec[1] + m02;
			double ty = m10*vec[0] + m11*vec[1] + m12;

			out[0] = tx;
			out[1] = ty;

		} else {
			out[0] = m00*vec[0] + m01*vec[1] + m02;
			out[1] = m10*vec[0] + m11*vec[1] + m12;
		}

		return out;
	}


	/**
	 * Returns the x-coordinate of the result of multiplying the point (x, y)
	 * by this matrix.
	 */
	public double multX(double x, double y) {
		return m00*x + m01*y + m02;
	}


	/**
	 * Returns the y-coordinate of the result of multiplying the point (x, y)
	 * by this matrix.
	 */
	public double multY(double x, double y) {
		return m10*x + m11*y + m12;
	}



	/**
	 * Unavailable in 2D. Does nothing.
	 */
	public void transpose() {
	}


	/*
	 * Implementation stolen from OpenJDK.
	 */
	public boolean invert() {
		double determinant = determinant();
		if (Math.abs(determinant) <= Double.MIN_VALUE) {
			return false;
		}

		double t00 = m00;
		double t01 = m01;
		double t02 = m02;
		double t10 = m10;
		double t11 = m11;
		double t12 = m12;

		m00 =  t11 / determinant;
		m10 = -t10 / determinant;
		m01 = -t01 / determinant;
		m11 =  t00 / determinant;
		m02 = (t01 * t12 - t11 * t02) / determinant;
		m12 = (t10 * t02 - t00 * t12) / determinant;

		return true;
	}


	/**
	 * @return the determinant of the matrix
	 */
	public double determinant() {
		return m00 * m11 - m01 * m10;
	}


	//////////////////////////////////////////////////////////////


	public void print() {
		int big = (int) abs(max(max(abs(m00), abs(m01), abs(m02)),
				max(abs(m10), abs(m11), abs(m12))));

		int digits = 1;
		if (Double.isNaN(big) || Double.isInfinite(big)) {  // avoid infinite loop
			digits = 5;
		} else {
			while ((big /= 10) != 0) digits++;  // cheap log()
		}

		System.out.println(nfs(m00, digits, 4) + " " +
				nfs(m01, digits, 4) + " " +
				nfs(m02, digits, 4));

		System.out.println(nfs(m10, digits, 4) + " " +
				nfs(m11, digits, 4) + " " +
				nfs(m12, digits, 4));

		System.out.println();
	}


	//////////////////////////////////////////////////////////////


	// implementation needs to be improved first. (e.g. actually keeping track
	// of whether the matrix is in fact identity internally.)


	protected boolean isIdentity() {
		return ((m00 == 1) && (m01 == 0) && (m02 == 0) &&
				(m10 == 0) && (m11 == 1) && (m12 == 0));
	}



	protected boolean isWarped() {
		return ((m00 != 1) || (m01 != 0) &&
				(m10 != 0) || (m11 != 1));
	}
}