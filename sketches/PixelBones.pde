HashMap<PVector, Integer> allPixels = new HashMap<PVector, Integer>();
PVector artSize = new PVector(64,64);
color currDrawColour = color(0);

PVector cameraPos = new PVector(0, 0);

int scaling = 32;
PVector scaleBounds = new PVector(8, 128); //Min, max

Mode currMode = Mode.DRAWING;
enum Mode {
  DRAWING, RIGGING, POSING
}

void setup() {
  size(1024, 1024);
  surface.setTitle("PixelBones");
}

void draw() {
  background(0);
  DrawGrid(scaling);

  switch(currMode) {
  case DRAWING:
    //Change colours
    //Pick brushes
    //Draw on canvas
    strokeWeight(2);
    stroke(255);

    fill(currDrawColour);
    square(width-50, height-50, 25);

    PlotPixel();
    DrawPixels();
    //Swap layers
    break;
  case RIGGING:
    //Add and connect bones
    //Select and assign groups of pixels for each bone
    break;
  case POSING:
    //Move bones (and transform pixels) to pose model
    break;
  }
}

void DrawGrid(int gridScale) {
  noStroke();

  color gridCol = color(110);
  color gridColAlt = color(127);

  for (int x =0; x < (width*scaling) / artSize.x; x+= gridScale) {
    for (int y =0; y < (height*scaling) / artSize.y; y += gridScale) {
      boolean xGrid = (x/gridScale) % 2 == 0;
      boolean yGrid = (y/gridScale) % 2 == 0;

      fill(xGrid != yGrid ? gridCol : gridColAlt);

      square(x +cameraPos.x, y+cameraPos.y, gridScale);
    }
  }
}

void PlotPixel() {
  if (mousePressed) {
    if (mouseButton == LEFT) {
      PVector pixelPos = new PVector(int((mouseX - cameraPos.x) / scaling), int((mouseY - cameraPos.y) / scaling));
      if (!allPixels.containsKey(pixelPos)) {
        //If pixel doesnt exist at location, add one
        allPixels.put(pixelPos, currDrawColour);
      } else {
        //Pixel does exist at location
        //See if colour is different
        if (currDrawColour != allPixels.get(pixelPos)) {
          //Colour is different. Update pixel color
          allPixels.replace(pixelPos, currDrawColour);
        }
      }
    }
  }
}

void DrawPixels() {
  noStroke();
  for (Map.Entry<PVector, Integer> p : allPixels.entrySet()) {
    PVector drawPos = new PVector(p.getKey().x * scaling, p.getKey().y * scaling);
    fill(p.getValue());
    square(drawPos.x + cameraPos.x, drawPos.y + cameraPos.y, scaling);
  }
}

PVector lastDragPos = new PVector(0, 0);
boolean dragging = false;
PVector unscaledCameraPos = new PVector(0, 0);
void mouseDragged() {
  if (mouseButton == CENTER) {
    if (!dragging) {
      lastDragPos = new PVector(mouseX, mouseY);
      dragging = true;
    }
  }

  if (dragging) {
    unscaledCameraPos = new PVector(unscaledCameraPos.x  + (mouseX - lastDragPos.x), unscaledCameraPos.y + ( mouseY - lastDragPos.y));
    cameraPos = new PVector(int(unscaledCameraPos.x / scaling) * scaling, int(unscaledCameraPos.y / scaling) * scaling);
    lastDragPos = new PVector(mouseX, mouseY);
  }
}

void mouseReleased() {
  if (mouseButton == CENTER) {
    dragging=false;
  }
}

void mouseWheel(MouseEvent event) {
  float e  = event.getCount();

  scaling += (e * 8); 
  scaling  = scaling < int(scaleBounds.x) ? int(scaleBounds.x) : scaling;
  scaling = scaling > int(scaleBounds.y) ? int(scaleBounds.y) : scaling;

  println(scaling);
}
