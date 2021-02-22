package types;

public interface PMatrix {

    public void reset();

    public PMatrix get();
    public double[] get(double[] target);

    public void set(PMatrix src);
    public void set(double[] source);
    public void set(double m00, double m01, double m02, double m10, double m11, double m12);
    public void set(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33);

    public void translate(double tx, double ty);
    public void translate(double tx, double ty, double tz);

    public void rotate(double angle);
    public void rotateX(double angle);
    public void rotateY(double angle);
    public void rotateZ(double angle);
    public void rotate(double angle, double v0, double v1, double v2);

    public void scale(double s);
    public void scale(double sx, double sy);
    public void scale(double x, double y, double z);

    public void shearX(double angle);
    public void shearY(double angle);

    public void apply(PMatrix source);
    public void apply(PMatrix2D source);
    public void apply(PMatrix3D source);
    public void apply(double n00, double n01, double n02, double n10, double n11, double n12);
    public void apply(double n00, double n01, double n02, double n03, double n10, double n11, double n12, double n13, double n20, double n21, double n22, double n23, double n30, double n31, double n32, double n33);

    public void preApply(PMatrix left);
    public void preApply(PMatrix2D left);
    public void preApply(PMatrix3D left);
    public void preApply(double n00, double n01, double n02, double n10, double n11, double n12);
    public void preApply(double n00, double n01, double n02, double n03, double n10, double n11, double n12, double n13, double n20, double n21, double n22, double n23, double n30, double n31, double n32, double n33);

    public PVector mult(PVector source, PVector target);
    public double[] mult(double[] source, double[] target);

    public void transpose();
    public boolean invert();
    public double determinant();
    
}