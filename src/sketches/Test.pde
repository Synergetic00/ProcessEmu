//Firstname Surname
//Description of application

int rectX, rectY, sizeW, sizeH, speedX, speedY;

void setup() {
    rectX = width/2;
    rectY = height/2;
    speedX = 5;
    speedY = 2;
    sizeW = 50;
    sizeH = 50;

    size(900,600);
    fill(255);
    noStroke();
    System.out.println("Hello, world!");
}

void draw() {
    background(0,0,200);
    rect(rectX, rectY, sizeW, sizeH);
    rectX += speedX;
    rectY += speedY;

    if (rectX > width - sizeW || rectX < 0) speedX *= -1;
    if (rectY > height - sizeH || rectY < 0) speedY *= -1;
}

void keyPressed() {
    exit();
}