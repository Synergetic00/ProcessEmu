//Processing
//A graphics object test

PGraphics pg;

void setup() {
  System.out.println("Hello, world!");
  pg = createGraphics(100,100);
  hello("From main");
  pg.hello("From obj");
}

void draw() {
  
}