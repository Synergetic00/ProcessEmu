package utils;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import main.FXApp;

import static utils.Constants.*;
import static utils.MathUtils.*;

import java.util.Calendar;

public class PGraphics {

    GraphicsContext gc;
    FXApp parent;

    public double offsetX, offsetY;
    public double screenW, screenH;

	public PGraphics(GraphicsContext gc, FXApp parent) {
        this.gc = gc;
        this.parent = parent;
        date = new Calendar.Builder().build();
        screenW = gc.getCanvas().getWidth();
        screenH = gc.getCanvas().getHeight();
        defaultSettings();
    }

    private void defaultSettings() {
        colorMode(RGB, 255, 255, 255, 255);
        size(100,100);
        fill(255);
        stroke(0);
    }

    public void setOffset(double x, double y) {
        offsetX = x;
        offsetY = y;
    }

    // [Structure]

    // [Environment]

    public double width, height;

    public void fullScreen() {
        size((int) screenW, (int) screenH);
    }

    public void size(int width, int height) {
        this.width = width;
        this.height = height;
        parent.width = width;
        parent.height = height;
        setOffset(((screenW - width) / 2), ((screenH - height) / 2));

        gc.save();
        gc.setFill(Color.rgb(20,20,20));
        gc.fillRect(0, 0, screenW, screenH);
        background(204);
        gc.restore();
    }

    // [Data]

    // [Control]

    // [Shape]

    public void strokeWeight(int w) {
        gc.setLineWidth(w);
    }

    // [Input]

    //// [Time & Date]

    Calendar date;

    public void updateTime() {
        date.setTimeInMillis(System.currentTimeMillis());
    }

    ////// day()

    public int day() {
        return date.get(Calendar.DAY_OF_MONTH);
    }

    ////// hour()

    public int hour() {
        return date.get(Calendar.HOUR_OF_DAY);
    }

    ////// millis()

    public int millis() {
        return date.get(Calendar.MILLISECOND);
    }

    ////// minute()

    public int minute() {
        return date.get(Calendar.MINUTE);
    }

    ////// month()

    public int month() {
        return date.get(Calendar.MONTH) + 1;
    }

    ////// second()

    public int second() {
        return date.get(Calendar.SECOND);
    }

    ////// year()

    public int year() {
        return date.get(Calendar.YEAR);
    }

    // [Output]

    // [Transform]

    // [Lights, Camera]

    // [Color]

    //// [Setting]

    int colourMode;
    double maxRH;
    double maxGS;
    double maxBB;
    double maxAL;

    boolean hasFill;
    boolean hasStroke;

    ////// background()

    public void background(int gray) {
        background(gray, gray, gray, 255);
    }

    public void background(int gray, int alpha) {
        background(gray, gray, gray, alpha);
    }
    
    public void background(int v1, int v2, int v3) {
        background(v1, v2, v3, 255);
    }
    
    public void background(int v1, int v2, int v3, int alpha) {
        gc.save();
        fill(v1, v2, v3, alpha);
        gc.fillRect(offsetX,offsetY,width,height);
        gc.restore();
    }
    
    ////// colorMode()

    public void colorMode(int mode) {
        colorMode(mode, 255, 255, 255, 255);
    }

    public void colorMode(int mode, double max) {
        colorMode(mode, max, max, max, 255);
    }

    public void colorMode(int mode, double max1, double max2, double max3) {
        colorMode(mode, max1, max2, max3, 255);
    }
    
    public void colorMode(int mode, double max1, double max2, double max3, double maxA) {
        colourMode = mode;
        maxRH = max1;
        maxGS = max2;
        maxBB = max3;
        maxAL = maxA;
    }

    ////// fill()

    public void fill(int gray) {
        fill(gray, gray, gray, (int) maxAL);
    }

    public void fill(int gray, int alpha) {
        fill(gray, gray, gray, alpha);
    }
    
    public void fill(int v1, int v2, int v3) {
        fill(v1, v2, v3, (int) maxAL);
    }
    
    public void fill(int v1, int v2, int v3, int alpha) {
        hasFill = true;
        gc.setFill(getColor(v1, v2, v3, alpha));
    }

    ////// noFill()

    public void noFill() {
        hasFill = false;
    }

    ////// noStroke()

    public void noStroke() {
        hasStroke = false;
    }

    ////// stroke()

    public void stroke(int gray) {
        stroke(gray, gray, gray, (int) maxAL);
    }

    public void stroke(int gray, int alpha) {
        stroke(gray, gray, gray, alpha);
    }
    
    public void stroke(int v1, int v2, int v3) {
        stroke(v1, v2, v3, (int) maxAL);
    }
    
    public void stroke(int v1, int v2, int v3, int alpha) {
        hasStroke = true;
        gc.setStroke(getColor(v1, v2, v3, alpha));
    }

    // [Image]

    // [Rendering]

    // [Typography]

    ////// text()

    public void text(int textVar, double x, double y) { text(textVar+"", x, y); }

    public void text(double textVar, double x, double y) { text(nfs(textVar,3), x, y); }

    public void text(String text, double x, double y) {
        double nx = x;
        double ny = y;

        gc.fillText(text, offsetX+nx, offsetY+ny);
    }

    public String nfs(double num, int right) {
        return String.format("%."+right+"f", num);
    }

    TextAlignment alignH = TextAlignment.LEFT;
    VPos alignV = VPos.BASELINE;
    int alignX = LEFT, alignY = BASELINE;

    ////// textAlign()

    public void textAlign(int newAlignX) {
        alignX = newAlignX;
        switch (newAlignX) {
            case LEFT: {
                alignH = TextAlignment.LEFT;
                break;
            }
            case CENTER: {
                alignH = TextAlignment.CENTER;
                break;
            }
            case RIGHT: {
                alignH = TextAlignment.RIGHT;
                break;
            }
        
            default: { break; }
        }
        gc.setTextAlign(alignH);
    }

    public void textAlign(int newAlignX, int newAlignY) {
        alignX = newAlignX;
        alignY = newAlignY;
        textAlign(newAlignX);
        switch (newAlignY) {
            case TOP: {
                alignV = VPos.TOP;
                break;
            }
            case BOTTOM: {
                alignV = VPos.BOTTOM;
                break;
            }
            case CENTER: {
                alignV = VPos.CENTER;
                break;
            }
            case BASELINE: {
                alignV = VPos.BASELINE;
                break;
            }
        
            default: { break; }
        }
        gc.setTextBaseline(alignV);
    }

    ////// textSize()

    public void textSize(double size) {
        gc.setFont(new Font(size));
    }

































    // Unsorted Shit



    

    public Color getColor(int v1, int v2, int v3, int alpha) {
        switch (colourMode) {
            case RGB: {
                int red = (int) map(v1, 0, maxRH, 0, 255);
                int green = (int) map(v2, 0, maxGS, 0, 255);
                int blue = (int) map(v3, 0, maxBB, 0, 255);
                double opacity = map(alpha, 0, maxAL, 0, 1);
                return Color.rgb(red, green, blue, opacity);
            }

            case HSB: {
                double hue = map(v1, 0, maxRH, 0, 359);
                double saturation = map(v2, 0, maxGS, 0, 1);
                double brightness = map(v3, 0, maxBB, 0, 1);
                double opacity = map(alpha, 0, maxAL, 0, 1);
                return Color.hsb(hue, saturation, brightness, opacity);
            }

            default: {
                return Color.WHITE;
            }
        }
    }

    public void arc(double x, double y, double width, double height, double start, double stop) {
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = x - width/2;
        double ny = y - height/2;

        if (hasFill) gc.fillArc(nx, ny, width, height, degStart, degStop, ArcType.ROUND);
        if (hasStroke) gc.strokeArc(nx, ny, width, height, degStart, degStop, ArcType.OPEN);
    }

    public void arc(double x, double y, double width, double height, double start, double stop, int mode) {
        clamp(mode, OPEN, PIE);
        ArcType arcMode = ArcType.OPEN;
        switch (mode) {
            case OPEN: {
                arcMode = ArcType.OPEN;
                break;
            }
            case CHORD: {
                arcMode = ArcType.CHORD;
                break;
            }
            case PIE: {
                arcMode = ArcType.ROUND;
                break;
            }
        }
        double degStart = -degrees(start);
        double degStop = -degrees(stop);
        degStop -= degStart;

        double nx = x - width/2;
        double ny = y - height/2;

        if (hasFill) gc.fillArc(offsetX+nx, offsetY+ny, width, height, degStart, degStop, arcMode);
        if (hasStroke) gc.strokeArc(offsetX+nx, offsetY+ny, width, height, degStart, degStop, arcMode);
    }

    // circle()

    public void circle(double x, double y, double size) {
        ellipse(x, y, size, size);
    }

    // ellipse()
    
    public void ellipse(double x, double y, double width, double height) {
        double nx = x, ny = y, nwidth = width, nheight = height;
        switch (ellipseMode) {
            case CORNER: {
                nwidth *= 2;
                nheight *= 2;
                break;
            }
            case CORNERS: {
                break;
            }
            case RADIUS: {
                nwidth *= 2;
                nheight *= 2;
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
            case CENTER: {
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
        }
        if (hasFill) gc.fillOval(offsetX+nx, offsetY+ny, nwidth, nheight);
        if (hasStroke) gc.strokeOval(offsetX+nx, offsetY+ny, nwidth, nheight);
    }

    // line()
    
    public void line(double x1, double y1, double x2, double y2) {
        gc.strokeLine(offsetX+x1, offsetY+y1, offsetX+x2, offsetY+y2);
    }

    // point() - maybe drawing the point using either circle() or square()
    public void point(double x, double y) {
        line(x, y, x, y);
    }

    // quad()

    public void quad(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] xPoints = {offsetX+x1, offsetX+x2, offsetX+x3, offsetX+x4};
        double[] yPoints = {offsetY+y1, offsetY+y2, offsetY+y3, offsetY+y4};
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 4);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 4);
    }

    // rect()

    public void rect(double x, double y, double width, double height) {
        double nx = x, ny = y, nwidth = width, nheight = height;
        switch (rectMode) {
            case CORNER: {
                break;
            }
            case CORNERS: {
                nwidth /= 2;
                nheight /= 2;
                break;
            }
            case RADIUS: {
                nwidth *= 2;
                nheight *= 2;
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
            case CENTER: {
                nx -= nwidth / 2;
                ny -= nheight / 2;
                break;
            }
        }
        if (hasFill) gc.fillRect(offsetX+nx, offsetY+ny, nwidth, nheight);
        if (hasStroke) gc.strokeRect(offsetX+nx, offsetY+ny, nwidth, nheight);
    }

    // square()

    public void square(double x, double y, double size) {
        rect(x, y, size, size);
    }

    // triangle()

    public void triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] xPoints = {offsetX+x1, offsetX+x2, offsetX+x3};
        double[] yPoints = {offsetY+y1, offsetY+y2, offsetY+y3};
        if (hasFill) gc.fillPolygon(xPoints, yPoints, 3);
        if (hasStroke) gc.strokePolygon(xPoints, yPoints, 3);
    }

    public int rectMode = CORNER, ellipseMode = CENTER;

    public void rectMode(int newMode) {
        rectMode = clamp(newMode, CORNER, CENTER);
    }

    public void ellipseMode(int newMode) {
        ellipseMode = clamp(newMode, CORNER, CENTER);
    }

    public int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
    
    // [Transform]

    final int MATRIX_STACK_DEPTH = 32;
    int transformCount;
    Affine[] transformStack = new Affine[MATRIX_STACK_DEPTH];

    public void applyMatrix() {
        // Not yet implemented
    }

    public void popMatrix() {
        if (transformCount == 0) {
            throw new RuntimeException("popMatrix() needs corresponding pushMatrix() statement");
        } else {
            transformCount--;
            gc.setTransform(transformStack[transformCount]);
        }
    }

    public void printMatrix() {
        // Not yet implemented
    }

    public void pushMatrix() {
        if (transformCount == transformStack.length) {
            throw new RuntimeException("StackOverflow: Reached the maximum amount of pushed matrixes");
        } else {
            transformStack[transformCount] = gc.getTransform(transformStack[transformCount]);
            transformCount++;
        }
    }

    public void resetMatrix() {
        gc.setTransform(new Affine());
    }

    public void rotate(double angle) {
        gc.rotate(degrees(angle));
    }

    public void rotateX() {
        // Not yet implemented
    }

    public void rotateY() {
        // Not yet implemented
    }

    public void rotateZ() {
        // Not yet implemented
    }

    public void scale(double s) {
        scale(s, s);
    }

    public void scale(double x, double y) {
        gc.scale(x, y);
    }

    public void shearX(double angle) {
        Affine temp = new Affine();
        temp.appendShear(tan(angle), 0);
        gc.transform(temp);

    }

    public void shearY(double angle) {
        Affine temp = new Affine();
        temp.appendShear(0, tan(angle));
        gc.transform(temp);

    }

    public void translate(double x, double y) {
        gc.translate(x, y);
    }

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

}