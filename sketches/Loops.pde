//Loops
//Reeee

void setup() {
  size(200, 200);
}

float x = 0.0;

void draw() {
  background(204);
  x = x + 0.1;
  if (x > width) {
    x = 0;
  }
  line(x, 0, x, height);
  fill(255,0,0);
  rect(mouseX, mouseY, 5, 5);
  fill(0,0,255);
  rect(pmouseX, pmouseY, 5, 5);
}

void mousePressed() {
  noLoop();
}

void mouseReleased() {
  loop();
}