package types;

import static utils.Constants.*;
import static utils.MathUtils.*;
import utils.MathUtils;

public class PVector {

    public double x, y, z;

    public PVector(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
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

    public static PVector random2D() {
        PVector output = fromAngle(random(TAU));
        output.normalize();
        return output;
    }

    public static PVector random2D(PVector target) {
        PVector output = fromAngle(random(TAU), target);
        output.normalize();
        return output;
    }

    public static PVector random3D() {
        double angle = random(TAU);
        double vz = random(-1,1);
        double vy = sqrt(1-vz*vz) * sin(angle);
        double vx = sqrt(1-vz*vz) * cos(angle);
        PVector output = new PVector(vx, vy, vz);
        output.normalize();
        return output;
    }

    public static PVector random3D(PVector target) {
        target.set(random3D());
        target.normalize();
        return target;
    }

    public static PVector fromAngle(double angle) {
        return new PVector(cos(angle), sin(angle));
    }

    public static PVector fromAngle(double angle, PVector target) {
        target.set(cos(angle), sin(angle));
        return target;
    }

    public PVector copy() {
        return new PVector(x, y, z);
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

    public static PVector add(PVector v1, PVector v2, PVector target) {
        target.x = v1.x + v2.x;
        target.y = v1.y + v2.y;
        target.z = v1.z + v2.z;
        return target;
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

    public static PVector sub(PVector v1, PVector v2, PVector target) {
        target.x = v1.x - v2.x;
        target.y = v1.y - v2.y;
        target.z = v1.z - v2.z;
        return target;
    }

    public void mult(double n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
    }

    public static PVector mult(PVector v, double n) {
        return new PVector(v.x * n, v.y * n, v.z * n);
    }

    public static PVector mult(PVector v, double n, PVector target) {
        target.x = v.x * n;
        target.y = v.y * n;
        target.z = v.z * n;
        return target;
    }

    public void div(double n) {
        this.x /= n;
        this.y /= n;
        this.z /= n;
    }

    public static PVector div(PVector v, double n) {
        return new PVector(v.x / n, v.y / n, v.z / n);
    }

    public static PVector div(PVector v, double n, PVector target) {
        target.x = v.x / n;
        target.y = v.y / n;
        target.z = v.z / n;
        return target;
    }

    public double dist(PVector v) {
        return dist(new PVector(0,0,0), v);
    }

    public double dist(PVector v1, PVector v2) {
        return MathUtils.dist(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
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

    public static double dot(PVector v1, PVector v2) {
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

    public static PVector cross(PVector v1, PVector v2, PVector target) {
        target.x = v1.y * v2.z - v1.z * v2.y; 
        target.y = v1.z * v2.x - v1.x * v2.z; 
        target.z = v1.x * v2.y - v1.y * v2.x;
        return target;
    }

    public PVector normalize() {
        double length = MathUtils.dist(0, 0, 0, x, y, z);
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
        return new PVector(x,y,z);
    }

    /*public void normalize() {
        double length = dist(0, 0, 0, x, y, z);
        if (length != 0) {
            x /= length;
            y /= length;
            z /= length;
        }
    }*/

    public void normalize(PVector target) {
        double length = MathUtils.dist(0, 0, 0, x, y, z);
        if (length != 0) {
            target.x = x /= length;
            target.y = y /= length;
            target.z = z /= length;
        }
    }

    public void limit(double max) {
        if (this.mag() > max) {
            this.normalize();
            this.mult(max);
        }
    }

    public void setMag(double len) {
        this.normalize();
        this.mult(len);
    }

    public void setMag(PVector target, double len) {
        target = this.copy();
        target.normalize();
        target.mult(len);
    }

    public double heading() {
        return atan2(y, x);
    }

    public void rotate(double theta) {
        double tmp = x;
        x = x * cos(theta) - y * sin(theta);
        y = tmp * sin(theta) + y * cos(theta);
    }

    public void lerp(PVector v, float amt) {
        x = MathUtils.lerp(x, v.x, amt);
        y = MathUtils.lerp(y, v.y, amt);
        z = MathUtils.lerp(z, v.z, amt);
    }

    public static PVector lerp(PVector v1, PVector v2, float amt) {
        PVector v = v1.copy();
        v.lerp(v2, amt);
        return v;
    }

    public void lerp(float x, float y, float z, float amt) {
        this.x = MathUtils.lerp(this.x, x, amt);
        this.y = MathUtils.lerp(this.y, y, amt);
        this.z = MathUtils.lerp(this.z, z, amt);
    }

    public double[] array() {
        return new double[] {x, y, z};
    }

    public static void print(PVector v) {
        System.out.println("["+v.x+", "+v.y+", "+v.z+"]");
    }

    public static double angleBetween(PVector v1, PVector v2) {
        if (v1.x == 0 && v1.y == 0 && v1.z == 0) return 0;
        if (v2.x == 0 && v2.y == 0 && v2.z == 0) return 0;

        double dot = dot(v1, v2);
        double amt = dot / (v1.mag() * v2.mag());
        
        if (amt <= -1) return PI;
        else if (amt >= 1) return 0;
        return (float) Math.acos(amt);
    }

}