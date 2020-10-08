// Move the mouse quickly to see the difference 
// between the current and previous position 
void setup() {
    size(800,800);
}

void draw() { 
  background(204); 
  fill(0,0,255);
  rect(pmouseX, pmouseY, 5,5);
  fill(255,0,0);
  rect(mouseX, mouseY, 5,5);
  fill(value);
  rect(25, 25, 50, 50);
} 

int value = 0;

void mouseDragged() 
{
  value = value + 5;
  if (value > 255) {
    value = 0;
  }
}