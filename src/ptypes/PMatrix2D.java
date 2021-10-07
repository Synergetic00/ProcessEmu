package ptypes;

import static utils.Maths.*;
import static utils.Data.*;

public class PMatrix2D {

    public double m00, m01, m02;
    public double m10, m11, m12;

    public PMatrix2D(double m00, double m01, double m02, double m10, double m11, double m12) {
        set(m00, m01, m02, m10, m11, m12);
    }

    public void set(double m00, double m01, double m02, double m10, double m11, double m12) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
    }

    public String toString() {
        int big = (int) abs(max(max(abs(m00), abs(m01), abs(m02)), max(abs(m10), abs(m11), abs(m12))));
        int digits = 1;
        if (Double.isNaN(big) || Double.isInfinite(big)) digits = 5;
        else while ((big /= 10) != 0) digits++;
        String output = nfs(m00, digits, 4) + " " + nfs(m01, digits, 4) + " " + nfs(m02, digits, 4);
        output += "\n" + nfs(m10, digits, 4) + " " + nfs(m11, digits, 4) + " " + nfs(m12, digits, 4);
        return output;
    }
    
}
