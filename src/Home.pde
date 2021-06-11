//Default Application
//This handles the home screen

int dispNum;
int pageNum;
int pageTtl;
int tailNum;
int calcNum;
int toRender;
int spacing;
double rectH;
double rectX;
int actualIndex;

void setup() {
    fullScreen();

    dispNum = 5;
    pageNum = (int) (Main.appIndex / dispNum);
    pageTtl = (int) Maths.ceil(Main.apps.size() / (double) dispNum);
    tailNum = Main.apps.size() % dispNum;
    calcNum = Main.apps.size() - (pageNum * dispNum);
    toRender = (calcNum == tailNum) ? tailNum : dispNum;
    spacing = 30;
    rectH = (height - (spacing * (dispNum+1)))/dispNum;
    rectX = 600;
}

void draw() {
    background(0);
    textAlign(LEFT, CENTER);

    for (int i = 0; i < toRender; i++) {
        actualIndex = i + (pageNum * dispNum);
        double yPos = i*(rectH+spacing)+spacing;

        fill(0);
        strokeWeight(5);
        if (i == Main.appIndex % dispNum) {
            stroke(54, 205, 255);
        } else {
            stroke(255);
        }
        rect(rectX, yPos, width - rectX - spacing, rectH);
        if (i == Main.appIndex % dispNum) {
            fill(54, 205, 255);
        } else {
            fill(255);
        }
        textSize(30);
        text(Main.apps.get(actualIndex).title, rectX + spacing, yPos+(30));
        textSize(15);
        text(Main.apps.get(actualIndex).authour, rectX + spacing, yPos+(rectH/2)+(3));
        textSize(20);
        text(Main.apps.get(actualIndex).description, rectX + spacing, yPos+(rectH)-(30));
    }

    for (int i = 0; i < pageTtl; i++) {
        stroke(255);
        if (i == pageNum) fill(255);
        else fill(0);
        ellipse((rectX/2)-(25*pageTtl)+(50*i)+25, height - 50, 25, 25);
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
    text("RaspberryPiFX",(rectX/2),100);
    textSize(40);
    text("v4.1.0",(rectX/2),200);
}

void keyPressed() {
    
    if (keyCode == KeyCode.ENTER) {
        Loader.launchProgram(Main.appIndex);
    }
    
    if (keyCode == KeyCode.UP) {
        Main.appIndex--;

        if (Main.appIndex < 0) {
            Main.appIndex = Main.apps.size() - 1;
        }
    }
    
    if (keyCode == KeyCode.DOWN) {
        Main.appIndex++;

        if (Main.appIndex >= Main.apps.size()) {
            Main.appIndex = 0;
        }
    }
}