package utils;

public class PVector {

    float x, y, z;

    public PVector(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public PVector(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }

    public void set(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public void set(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
    }

    public void set(PVector a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public void set(float[] arr) {
        if (arr.length < 2) return;
        this.x = arr[0];
        this.y = arr[1];
        if (arr.length == 3) this.z = arr[2];
    }

    public float[] array() {
        return new float[] {x, y, z};
    }

    /*	
    set()	        Set the components of the vector
    random2D()	    Make a new 2D unit vector with a random direction.
    random3D()	    Make a new 3D unit vector with a random direction.
    fromAngle()	    Make a new 2D unit vector from an angle
    copy()	        Get a copy of the vector
    mag()	        Calculate the magnitude of the vector
    magSq()	        Calculate the magnitude of the vector, squared
    add()	        Adds x, y, and z components to a vector, one vector to another, or two independent vectors
    sub()	        Subtract x, y, and z components from a vector, one vector from another, or two independent vectors
    mult()	        Multiply a vector by a scalar
    div()	        Divide a vector by a scalar
    dist()	        Calculate the distance between two points
    dot()	        Calculate the dot product of two vectors
    cross()	        Calculate and return the cross product
    normalize()	    Normalize the vector to a length of 1
    limit()	        Limit the magnitude of the vector
    setMag()	    Set the magnitude of the vector
    heading()	    Calculate the angle of rotation for this vector
    rotate()	    Rotate the vector by an angle (2D only)
    lerp()	        Linear interpolate the vector to another vector
    angleBetween()	Calculate and return the angle between two vectors
    array()	        Return a representation of the vector as a float array
    */

}