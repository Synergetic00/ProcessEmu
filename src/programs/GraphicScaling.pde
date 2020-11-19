//Test
//Test

PGraphics pg1;

void setup() {
  size(600,600);
  background(10);
  fill(255,0,0);
  pg1 = createGraphics(300,300);
  pg1.beginDraw();
  pg1.background(60);
  pg1.fill(0,0,255);
  pg1.rect(100,100,100,100);
  pg1.fill(255);
  pg1.textSize(60);
  pg1.text("Hello", 50, 50);
  pg1.endDraw();
  image(pg1,75,100,100,200);
}