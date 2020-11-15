//Processing
//A graphics object test

PGraphics pg;

void setup() {
  size(600,600);
  background(10);
  fill(255,0,0);
  rect(50,50,100,100);
  pg = createGraphics(400,400);
  pg.beginDraw();
  pg.fill(0,0,255);
  pg.background(60);
  pg.rect(100,100,100,100);
  pg.endDraw();
  image(pg,100,100);
}