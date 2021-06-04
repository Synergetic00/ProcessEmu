//Default Application
//This handles the home screen

void setup() {
    fullScreen();
    background(200,0,0);
    System.out.println("Loaded the home screen!");
}

void draw() {
    
}

void keyPressed() {
    Loader.launchProgram(0);
}