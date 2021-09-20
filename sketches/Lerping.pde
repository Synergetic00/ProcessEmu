PVector start;
PVector end;
PVector middle;

void setup() {
    start = new PVector(10.0, 30.0);
    end = new PVector(90.0, 70.0);
    middle = PVector.lerp(start, end, 0.4);
    noStroke();
    fill(255,0,0);
    circle(start.x,start.y,5);
    fill(0,255,0);
    circle(middle.x,middle.y,5);
    fill(0,0,255);
    circle(end.x,end.y,5);
}