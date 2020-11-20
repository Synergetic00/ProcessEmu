//Processing
//A graphics object test

PGraphics pg1;
PGraphics pg2;

void setup() {
  size(600,600);
  //background(10);
  fill(255,0,0);
  pg1 = createGraphics(300,300);
  pg1.beginDraw();
  pg1.fill(0,0,255,40);
  pg1.background(60);
  pg1.rect(100,100,100,100);
  pg1.endDraw();
  pg2 = createGraphics(200,150);
  pg2.beginDraw();
  pg2.fill(0,255,0,100);
  pg2.background(70);
  pg2.rect(75,25,20,80);
  pg2.endDraw();
  image(pg1,75,100);
  image(pg2,275,300);
  rect(50,50,100,100);
}