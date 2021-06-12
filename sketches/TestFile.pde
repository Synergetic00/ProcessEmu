//Firstname Surname
//Description of application

int rectX, rectY, sizeW, sizeH, speedX, speedY;
boolean isHoveringBox;

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
}

void draw() {
    frameRate((mouseX/width)*60);
    background(0, 0, 200);
    isHoveringBox = isHoveringBox();
    if (isHoveringBox) {
        fill(255, 0, 0);
    } else {
        fill(255);
    }
    rect(rectX, rectY, sizeW, sizeH);
    rectX += speedX;
    rectY += speedY;

    

    if (rectX > width - sizeW || rectX < 0) speedX *= -1;
    if (rectY > height - sizeH || rectY < 0) speedY *= -1;
}

boolean isHoveringBox() {
    if (mouseX > rectX && mouseX < rectX + sizeW) {
        if (mouseY > rectY && mouseY < rectY + sizeH) {
            return true;
        }
    }
    return false;
}

void mousePressed() {
    if (isHoveringBox) {
        exit();
    }
}