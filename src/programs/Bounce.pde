//Processing
//A ball bouncing like the DVD screen

int rad = 60;
double xpos, ypos;
double xspeed = 2.8;
double yspeed = 2.2;
int xdirection = 1;
int ydirection = 1;

void setup() {
    size(640, 360);
    noStroke();
    ellipseMode(RADIUS);
    xpos = width/2;
    ypos = height/2;
}

void draw() {
    background(102);

    xpos = xpos + ( xspeed * xdirection );
    ypos = ypos + ( yspeed * ydirection );

    if (xpos > width-rad || xpos < rad) {
        xdirection *= -1;
    }

    if (ypos > height-rad || ypos < rad) {
        ydirection *= -1;
    }

    ellipse(xpos, ypos, rad, rad);
}
