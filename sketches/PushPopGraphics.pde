PGraphics pg;
pg = createGraphics(400, 400);

size(600, 600);

pg.beginDraw();
pg.ellipse(0, 200, 132, 132);  // Left circle

pg.pushStyle();  // Start a new style
pg.strokeWeight(40);
pg.fill(204, 153, 0);
pg.ellipse(132, 200, 132, 132);  // Left-middle circle

pg.pushStyle();  // Start another new style
pg.stroke(0, 102, 153);
pg.ellipse(264, 200, 132, 132);  // Right-middle circle
pg.popStyle();  // Restore the previous style

pg.popStyle();  // Restore original style

pg.ellipse(400, 200, 132, 132);  // Right circle
pg.endDraw();

image(pg, 100, 100);