//Default Application
//This handles the home screen

int displayNum;
int pageCount;
int pageTotal;
int tailNum;
int calcNum;
int toRender;
int spacing;
double rectH;
double rectX;
int actualIndex;

void setup() {
    fullScreen();

    displayNum = 5;
    spacing = 30;
    rectX = 600;
    rectH = (height - (spacing * (displayNum+1)))/displayNum;

    tailNum = Main.apps.size() % displayNum;
    pageTotal = (int) Maths.ceil(Main.apps.size() / (double) displayNum);
}

void draw() {
    pageCount = (int) (Main.appIndex / displayNum);
    calcNum = Main.apps.size() - (pageCount * displayNum);
    toRender = (calcNum == tailNum) ? tailNum : displayNum;

    background(0);

    for (int i = 0; i < toRender; i++) {
        actualIndex = i + (pageCount * displayNum);
        double yPos = i*(rectH+spacing)+spacing;

        fill(0);
        strokeWeight(5);
        if (i == Main.appIndex % displayNum) {
            stroke(54, 205, 255);
        } else {
            stroke(255);
        }
        rect(rectX, yPos, width - rectX - spacing, rectH);
        if (i == Main.appIndex % displayNum) {
            fill(54, 205, 255);
        } else {
            fill(255);
        }
        textAlign(LEFT, CENTER);
        textSize(30);
        text(Main.apps.get(actualIndex).title(), rectX + spacing, yPos+(30));
        textSize(15);
        text(Main.apps.get(actualIndex).authour(), rectX + spacing, yPos+(rectH/2)+(3));
        textSize(20);
        text(Main.apps.get(actualIndex).description(), rectX + spacing, yPos+(rectH)-(30));
        textAlign(RIGHT, CENTER);
        textSize(15);
        text(Main.apps.get(actualIndex).path(), width - (spacing*2), yPos+(30));
    }

    for (int i = 0; i < pageTotal; i++) {
        stroke(255);
        if (i == pageCount) fill(255);
        else fill(0);
        ellipse((rectX/2)-(25*pageTotal)+(50*i)+25, height - 50, 25, 25);
    }

    String day = String.format("%02d", day());
    String month = String.format("%02d", month());
    String year = String.format("%04d", year());
    String hour = String.format("%02d", hour());
    String minute = String.format("%02d", minute());
    String second = String.format("%02d", second());
    String dateNow = day+"/"+month+"/"+year;
    String timeNow = hour+":"+minute+":"+second;
    fill(255);
    textSize(30);
    textAlign(CENTER, CENTER);
    text(dateNow, (rectX/2), height - 200);
    text(timeNow, (rectX/2), height - 150);
    fill(54, 205, 255);
    textSize(70);
    text(Main.title,(rectX/2),100);
    textSize(40);
    text(Main.version, (rectX/2),200);
}

void keyPressed() {

    if (keyCode == ENTER) {
        Loader.launchProgram(Main.appIndex);
    }

    if (keyCode == UP) {
        Main.appIndex--;

        if (Main.appIndex < 0) {
            Main.appIndex = Main.apps.size() - 1;
        }
    }

    if (keyCode == DOWN) {
        Main.appIndex++;

        if (Main.appIndex >= Main.apps.size()) {
            Main.appIndex = 0;
        }
    }
}