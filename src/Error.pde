//Synergetic00
//Error with 3D

void setup() {
    fullScreen();
    background(0);
    fill(255,0,0);
    textAlign(CENTER, CENTER);
    text("Incompatiable function, press any key to exit", width/2, height/2);
}

void keyPressed() {
    exit();
}