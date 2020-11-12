//Elise
//A game about infinite haulage

    float pX = 175;
    float pY = 120;
    float s = 4;
    boolean pRight = true;
    boolean stopped = true;
    int counter = 0;
    float bounce = 0;
    float boxX = 430;
    float boxY = 140;
    boolean boxHeld = false;
    float goal = random(150, 350);
    boolean gameWon = false;
    float dist;
    float endS;

    void setup() {

        size(500, 200);
        background(110);
        rectMode(CENTER);
        //frameRate(24); TODO: how to control the frame rate
    }

    void draw() {
        counter ++;
        background(110);
        fill(0);
        rect(width/2, 180, 500, 63);
        if (gameWon) {
            if (counter < 60){
                background(255, 0, 0);
                fill(0);
                rect(width/2, 180, 500, 63);
            } else {
                pX = 101;
                pRight = true;
                gameWon = false;
                boxY = 140;
                boxX = random(70,430);
                goal = random(150, 350);
            }


            drawP();

        } else {
            if (stopped) {
                s = 0;
            } else {
                s = 4;
            }
            if (pX <= width-100 || pX >= 100) {
                if (pRight) {
                    pX += s;
                } else {
                    pX -= s;
                }
            } else {
                s = 0;
                stopped = true;
            }


            if (pX >= width-100 || pX <= 100) {
                stopped = true;
            }
            if (boxHeld) {
                fill(0,255,219);
                noStroke();
                rect(goal, 150, 50, 5);
                stroke(0);
            }

            drawP();
            if (boxHeld == false) {
                fill(103, 72, 47);
                noStroke();
                rect(boxX, boxY, 30, 30);
                stroke(0);
            }
        }
    }

    public void drawP() {
        if (stopped == false) {
            bounce = 0;
        } else {
            if (counter % 12 == 0) {
                if (bounce == 2) {
                    bounce = 0;
                } else {
                    bounce = 2;
                }
            }
        }
        fill(0);
        rect(pX, pY + bounce, 30, 45);
        rect(pX, pY - 15 + bounce, 40, 30);
        rect(pX - 9, pY + 20, 5, 15);
        rect(pX + 9, pY + 20, 5, 15);
        fill(140);
        if (pRight) {
            rect(pX + 10, pY - 15  + bounce, 10, 5);
        } else {
            rect(pX - 10, pY - 15 + bounce, 10, 5);
        }
        if (boxHeld) {
            fill(103, 72, 47);
            noStroke();
            if (pRight) {
                rect(pX + 30, pY + bounce, 30, 30);
            } else {
                rect(pX -30, pY + bounce, 30, 30);
            }
            stroke(0);
        }
    }
    
    void keyTyped() {
        if (key == 'a') {
            if (pX >= 100) {
                pRight = false;
                stopped = false;
            }
        }


        if (key == 'd') {
            if (pX <= width-100) {
                pRight = true;
                stopped = false;
            }
        }
        if (key == 's') {
            stopped = true;
            if (boxHeld == true) {
                boxHeld = false;
                boxY = 140;
                if (pRight) {
                    boxX = pX + 25;
                } else {
                    boxX = pX - 25;
                }
                if (boxX >= goal - 25 && boxX <= goal + 25) {
                    gameWon = true;
                    boxX = -100;
                    boxY = -100;
                    counter = 0;
                    dist = goal - 80;
                }
            }
        }
        if (key == 'w') {
            if (boxHeld == false && pX >= boxX - 30 && pX <= boxX + 30) {
                boxHeld = true;
                stopped = true;
            }
        }
    }
    
