package utils;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class StyleState {
    public Paint fill;
    public Paint stroke;
    public double linewidth;
    public StrokeLineCap linecap;
    public StrokeLineJoin linejoin;
    public double miterlimit;
    public double dashes[];
    public double dashOffset;
    public Font font;
    public TextAlignment textalign;
    public VPos textbaseline;

    public StyleState(GraphicsContext gc) {
        this.fill = gc.getFill();
        this.stroke = gc.getStroke();
        this.linewidth = gc.getLineWidth();
        this.linecap = gc.getLineCap();
        this.linejoin = gc.getLineJoin();
        this.miterlimit = gc.getMiterLimit();
        this.dashes = gc.getLineDashes();
        this.dashOffset = gc.getLineDashOffset();
        this.font = gc.getFont();
        this.textalign = gc.getTextAlign();
        this.textbaseline = gc.getTextBaseline();
    }
}
