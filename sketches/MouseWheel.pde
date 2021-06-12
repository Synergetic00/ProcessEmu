//Processing
//Get mouse scroll count

void setup() {
  size(100, 100);
}

void draw() {} 

void mouseWheel(MouseEvent event) {
  float e = event.getCount();
  println(e);
}