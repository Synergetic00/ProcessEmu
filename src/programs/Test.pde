//John Smith
//Test description

PGraphics pg;

void setup() {
    size(400,400);
    pg = createGraphics(400,400);
    pg.beginDraw();
    pg.background(0,0,200);
    pg.fill(255,0,0);
    pg.rect(200,50,300,300);
    pg.endDraw();
    image(pg,100,100,200,200);
}