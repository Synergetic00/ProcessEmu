//Jack
//Loop rotate text

void setup() {
    size(300,300);
    rectMode(CENTER);
    fill(255,0,0);
    for(int i = 0; i < 360; i++) {
        float x = cos((float)i/360.0f) * 250.0f;
        float y = sin((float)i/360.0f) * 250.0f;
        rect(x,y,15,15);
    }
}