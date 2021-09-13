float y = 180;
 
// The statements in the setup() function 
// run once when the program begins
void setup() {
  size(640, 360);  // Size should be the first statement
  stroke(255);     // Set stroke color to white
  noLoop();
}

void draw() { 
  background(0);  // Set the background to black
  line(0, y, width, y);  
  y = y - 1; 
  if (y < 0) { 
    y = height; 
  } 
} 

void mousePressed() {
  loop();
}