float[] distribution = new float[360];

void setup() {
  size(200,100); 
  
  for (int i = 0; i < distribution.length; i++) {
    distribution[i] = int(randomGaussian() * 15);
  }
}

void draw() {
  background(204);

  for (int y = 0; y < 100; y++) {
    float x = randomGaussian() * 15;
    line(50, y, 50 + x, y);
  }
  
  translate(width/2, height/2);

  for (int i = 0; i < distribution.length; i++) {
    rotate(TWO_PI/distribution.length);
    stroke(0);
    float dist = abs(distribution[i]);
    line(0, 0, dist, 0);
  }
}