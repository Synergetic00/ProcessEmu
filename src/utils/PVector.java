package utils;

import static utils.FXUtils.*;

public class PVector {

    double x, y, z;

    public PVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(PVector a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public void set(double[] arr) {
        if (arr.length < 2) return;
        this.x = arr[0];
        this.y = arr[1];
        if (arr.length == 3) this.z = arr[2];
    }

    public double mag() {
        return sqrt(x*x + y*y + z*z);
    }

    public double magSq() {
        return (x*x + y*y + z*z);
    }

    public void add(PVector v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    public static PVector add(PVector v1, PVector v2) {
        return new PVector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public static void add(PVector v1, PVector v2, PVector target) {
        target.x = v1.x + v2.x;
        target.y = v1.y + v2.y;
        target.z = v1.z + v2.z;
    }

    public void sub(PVector v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }

    public void sub(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public void sub(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
    }

    public static PVector sub(PVector v1, PVector v2) {
        return new PVector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static void sub(PVector v1, PVector v2, PVector target) {
        target.x = v1.x - v2.x;
        target.y = v1.y - v2.y;
        target.z = v1.z - v2.z;
    }

    public void mult(double n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }

    public static PVector mult(PVector v, double n) {
        return new PVector(v.x * n, v.y * n, v.z * n);
    }

    public static void mult(PVector v, double n, PVector target) {
        target.x = v.x * n;
        target.y = v.y * n;
        target.z = v.z * n;
    }

    public void div(double n) {
        this.x /= n;
        this.y /= n;
        this.z /= n;
    }

    public static PVector div(PVector v, double n) {
        return new PVector(v.x / n, v.y / n, v.z / n);
    }

    public static void div(PVector v, double n, PVector target) {
        target.x = v.x / n;
        target.y = v.y / n;
        target.z = v.z / n;
    }

    public double dist(PVector v) {
        return dist(new PVector(0,0,0), v);
    }

    public double dist(PVector v1, PVector v2) {
        return FXUtils.dist(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
    }

    public double dot(PVector v) {
        double dotProd = 0;
        dotProd += x * v.x;
        dotProd += y * v.y;
        dotProd += z * v.z;
        return dotProd;
    }

    public double dot(double x, double y, double z) {
        double dotProd = 0;
        dotProd += x * this.x;
        dotProd += y * this.y;
        dotProd += z * this.z;
        return dotProd;
    }

    public double dot(PVector v1, PVector v2) {
        double dotProd = 0;
        dotProd += v1.x * v2.x;
        dotProd += v1.y * v2.y;
        dotProd += v1.z * v2.z;
        return dotProd;
    }

    public PVector cross(PVector v) {
        double crossX = y * v.z - z * v.y; 
        double crossY = z * v.x - x * v.z; 
        double crossZ = x * v.y - y * v.x; 
        return new PVector(crossX, crossY, crossZ);
    }

    public PVector cross(PVector v, PVector target) {
        target.x = y * v.z - z * v.y; 
        target.y = z * v.x - x * v.z; 
        target.z = x * v.y - y * v.x; 
        return target;
    }

    public static void cross(PVector v1, PVector v2, PVector target) {
        target.x = v1.y * v2.z - v1.z * v2.y; 
        target.y = v1.z * v2.x - v1.x * v2.z; 
        target.z = v1.x * v2.y - v1.y * v2.x;  
    }

    public void normalize() {
        double length = FXUtils.dist(0, 0, 0, x, y, z);
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
    }

    public void normalize(PVector target) {
        double length = FXUtils.dist(0, 0, 0, x, y, z);
        if (length != 0) {
            target.x = x /= length;
            target.y = y /= length;
            target.z = z /= length;
        }
    }

    public double[] array() {
        return new double[] {x, y, z};
    }

    /*	
    ✔    set()	        Set the components of the vector
        random2D()	    Make a new 2D unit vector with a random direction.
        random3D()	    Make a new 3D unit vector with a random direction.
        fromAngle()	    Make a new 2D unit vector from an angle
        copy()	        Get a copy of the vector
    ✔    mag()	        Calculate the magnitude of the vector
    ✔    magSq()	    Calculate the magnitude of the vector, squared
    ✔    add()	        Adds x, y, and z components to a vector, one vector to another, or two independent vectors
    ✔    sub()	        Subtract x, y, and z components from a vector, one vector from another, or two independent vectors
    ✔    mult()	        Multiply a vector by a scalar
    ✔    div()	        Divide a vector by a scalar
    ✔    dist()	        Calculate the distance between two points
    ✔    dot()	        Calculate the dot product of two vectors
    ✔    cross()	    Calculate and return the cross product
    ✔    normalize()	Normalize the vector to a length of 1
        limit()	        Limit the magnitude of the vector
        setMag()	    Set the magnitude of the vector
        heading()	    Calculate the angle of rotation for this vector
        rotate()	    Rotate the vector by an angle (2D only)
        lerp()	        Linear interpolate the vector to another vector
        angleBetween()	Calculate and return the angle between two vectors
    ✔    array()	        Return a representation of the vector as a double array
    */

}