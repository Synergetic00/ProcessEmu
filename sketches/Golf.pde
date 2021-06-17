//Mark - ZepplinGames
//Golf minigame made in processing

float cameraZoom=100;
PVector zoomClamp = new PVector(50, 300); //Min, max
PVector cameraPos = new PVector(0, 0);
int moveSpeed = 3;
PVector cameraMoveTo = new PVector(0, 0);

PVector ballPos = new PVector(0, 0);
PVector holePos = new PVector(0, 0);
float ballScale = 0.1;
float holeScale = 0.2;

float maxPower = 100;
float hitPower = 5;
float hitAngle=0;
boolean canHit=true;


PVector courseCenter = new PVector(0, 0);
int courseComplexity = 3;

int hits=0;
boolean sunkBall=false;

ArrayList<PVector> coursePoints = new ArrayList<PVector>();
ArrayList<Line> courseColliders = new ArrayList<Line>();

void setup() {
  size(500, 500); 
  GenerateGolfCourse();

  PVector spawnPos = new PVector(width/2, height/2);
  cameraPos = spawnPos;
  cameraMoveTo =  spawnPos;
}

void draw() {
  background(15, 195, 215);
  //UpdateMovement();
  UpdateColliders();
  CheckCollisions();
  UpdateCameraPosition();
  DrawCourse();
  DrawBall();
  DrawClub();
  CheckHole();
  DrawUI();
  DebugColliders();
}

void DebugColliders() {
  for (int n =0; n < courseColliders.size(); n++) {
    PVector p1 = courseColliders.get(n).start;
    PVector p2 = courseColliders.get(n).end;

    noFill();
    stroke(255, 0, 0);
    strokeWeight(1);
    line(p1.x, p1.y, p2.x, p2.y);
  }
}

void GenerateGolfCourse() {
  coursePoints = new ArrayList<PVector>();

  int prevPointX= (width/2);
  int prevPointY= (height/2);
  int prevAngle = int(random(360));

  coursePoints.add(new PVector(prevPointX, prevPointY));

  for (int n =0; n < courseComplexity; n++) {
    int angle = int(random(90))  + prevAngle;
    int pathLength = int(random(10));

    int pathPointX = int(cos(angle) *pathLength)  + prevPointX;
    int pathPointY = int(sin(angle) * pathLength) + prevPointY;

    PVector coursePoint = new PVector(pathPointX, pathPointY);
    coursePoints.add(coursePoint);

    prevPointX = pathPointX;
    prevPointY = pathPointY;
    prevAngle = angle;
  }
}

void UpdateColliders() {
  float colDist = (cameraZoom*1.25)/2;

  courseColliders.clear();
  for (int n =0; n < coursePoints.size()-1; n++) {
    PVector p1 = remapPoint(coursePoints.get(n));
    PVector p2 = remapPoint(coursePoints.get(n+1));

    PVector center = new PVector((p1.x + p2.x)/2, (p1.y + p2.y) /2);

    //PVector center = new PVector((p1.x + p2.x)/2, (p1.y+p2.y)/2); 
    float pAngle = getAngle(p1, p2);
    float pAngleInv = getAngle(p2, p1);

    PVector p1L = new PVector((cos(90-pAngle) * colDist) + p1.x, (sin(90-pAngle) * colDist) + p1.y);
    PVector p1R = new PVector((cos(90+pAngle) * colDist) + p1.x, (sin(90+pAngle) * colDist) + p1.y);

    PVector p2L = new PVector((cos(90-pAngleInv) * colDist) + p2.x, (sin(90-pAngleInv) * colDist) + p2.y);
    PVector p2R = new PVector((cos(90+pAngleInv) * colDist) + p2.x, (sin(90+pAngleInv) * colDist) + p2.y);

    if (!circleCollidingLine(center, colDist/2, p1L, p2R)) {
      courseColliders.add(new Line(p1L, p2R));
    } 

    if (!circleCollidingLine(center, colDist/2, p1R, p2L)) {
      courseColliders.add(new Line(p1R, p2L));
    }
  }
}

void DrawCourse() {
  ArrayList<PVector> drawPoints = new ArrayList<PVector>();
  PVector[] arrayPoints = new PVector[coursePoints.size()];
  courseCenter = centroid(coursePoints.toArray(arrayPoints));

  for (int n =0; n < coursePoints.size(); n++) {
    PVector p = coursePoints.get(n);
    PVector newPos = remapPoint(p);
    drawPoints.add(newPos);
  }

  strokeJoin(ROUND);
  noFill();

  strokeWeight(cameraZoom*1.25);
  stroke(230, 220, 210);
  beginShape();
  for (int n=0; n < drawPoints.size()-1; n++) {
    PVector p1 = drawPoints.get(n);
    PVector p2 = drawPoints.get(n+1);

    vertex(p1.x, p1.y);
    vertex(p2.x, p2.y);
  }
  endShape();

  strokeWeight(cameraZoom);
  stroke(130, 195, 72);
  beginShape();
  for (int n=0; n < drawPoints.size()-1; n++) {
    PVector p1 = drawPoints.get(n);
    PVector p2 = drawPoints.get(n+1);

    vertex(p1.x, p1.y);
    vertex(p2.x, p2.y);
  }
  endShape();

  //Draw hole
  noStroke();
  fill(127);
  PVector lPoint = drawPoints.get(drawPoints.size()-1);
  holePos = lPoint;
  circle(lPoint.x, lPoint.y, cameraZoom * holeScale);
}

/*
int moveX =0;
 int moveY =0;
 boolean keyUp, keyDown, keyLeft, keyRight;
 
 void UpdateMovement() {
 moveX = 0;
 moveY =0;
 if (keyUp) {
 moveY += 1;
 }
 if (keyDown) {
 moveY += -1;
 }
 if (keyLeft) {
 moveX += 1;
 }
 if (keyRight) {
 moveX += -1;
 }
 
 if (moveX !=0) {
 moveX = moveX > 0 ? 1 : -1;
 }
 if (moveY !=0) {
 moveY = moveY > 0 ? 1 : -1;
 }
 } */

void CheckCollisions() {
  for (int  n =0; n < courseColliders.size(); n++) {
    Line l = courseColliders.get(n);
    if (circleCollidingLine(new PVector(width/2, height/2), cameraZoom*ballScale, l.start, l.end)) {
      //Get direction of collision
      //Bounce, move opposite direction
      PVector start = l.start.y > l.end.y ? l.start : l.end;
      PVector end = start == l.start ? l.end : l.start;

      float wallAngle = getAngle(start, end);
      float exitAngle = -(hitAngle + wallAngle);

      float rPower = hitPower;

      float dist = dist(cameraPos.x, cameraPos.y, cameraMoveTo.x, cameraMoveTo.y);

      PVector bouncePos = new PVector(cos((exitAngle)) * rPower, sin((exitAngle)) * rPower);
      cameraMoveTo = bouncePos;
    }
  }
}

float movePercent=0;
void UpdateCameraPosition() {
  cameraPos = cameraPos.lerp(cameraMoveTo, movePercent);
  movePercent += 0.001;
  if (movePercent >= 0.1 ) {
    canHit=true;
    println(cameraPos);
  }
}

void DrawBall() {
  noStroke();
  fill(255);
  circle(width/2, height/2, cameraZoom * ballScale);
}

void CheckHole() {
  float dist = dist(width/2, height/2, holePos.x, holePos.y);
  if (dist < (cameraZoom * holeScale)/2) {
    println("Sunk");
    println(holePos +  " : " + cameraPos);
    sunkBall=true;

    cameraMoveTo = cameraPos;
    movePercent =1;
  }
}

void DrawClub() {
  if (clickedBall) {
    stroke(255, 0, 0);
    noFill();
    strokeWeight(ballScale * 2);

    PVector mouse = new PVector(mouseX, mouseY);
    PVector hWH = new PVector(width/2, height/2);

    circle(hWH.x, hWH.y, cameraZoom * ballScale * 1.25);

    float powerDist = dist(hWH.x, hWH.y, mouse.x, mouse.y);
    powerDist = powerDist > maxPower ? maxPower : powerDist;

    float angle = getAngle(mouse, hWH);
    PVector powerLine = new PVector(cos(angle)*powerDist + hWH.x, sin(angle)*powerDist + hWH.y);

    line(hWH.x, hWH.y, powerLine.x, powerLine.y);
  }
}

/*void keyPressed() {
 if (key == 'w' || keyCode == UP) {
 keyUp=true;
 }
 if (key == 's' || keyCode == DOWN) {
 keyDown=true;
 }
 
 if (key == 'a' || keyCode == LEFT) {
 keyLeft=true;
 }
 
 if (key == 'd' || keyCode == RIGHT) {
 keyRight=true;
 }
 }
 
 void keyReleased() {
 if (key == 'w' || keyCode == UP) {
 keyUp=false;
 }
 if (key == 's' || keyCode == DOWN) {
 keyDown=false;
 }
 
 if (key == 'a' || keyCode == LEFT) {
 keyLeft=false;
 }
 
 if (key == 'd' || keyCode == RIGHT) {
 keyRight=false;
 }
 } */

void mouseWheel(MouseEvent event) {
  float e = event.getCount();

  //cameraZoom += e *10;

  cameraZoom = cameraZoom < zoomClamp.x ? zoomClamp.x : cameraZoom;
  cameraZoom = cameraZoom > zoomClamp.y ? zoomClamp.y : cameraZoom;
}

boolean clickedBall =false;
void mousePressed(MouseEvent event) {
  if (mouseButton == LEFT) {
    if (canHit) {
      float mouseDist= dist(mouseX, mouseY, width/2, height/2);
      if (mouseDist < cameraZoom * ballScale) { 
        clickedBall =true;
        println("clicked");
      }
    }
  }
}

void mouseReleased() {
  if (clickedBall) {
    movePercent=0;
    hits++;
    clickedBall =  false;
    canHit=false;

    PVector mouse = new PVector(mouseX, mouseY);
    PVector hWH = new PVector(width/2, height/2);

    float powerDist = dist(hWH.x, hWH.y, mouse.x, mouse.y);
    powerDist = powerDist > maxPower ? maxPower : powerDist;
    powerDist *= hitPower;

    float angle = getAngle(mouse, hWH);
    hitAngle = angle;

    println("Hit with power " + powerDist  + " at angle " + degrees(angle));

    PVector moveTo = new PVector(cos(angle) * powerDist + cameraPos.x, sin(angle) * powerDist + cameraPos.y);
    cameraMoveTo = moveTo;
  }
}

void DrawUI() {
  text("HITS: " + hits, 50, 50);
}

PVector centroid(PVector[] points) {
  PVector center= new PVector(0, 0);
  for (int  n =0; n < points.length; n++) {
    center  = new PVector(center.x + points[n].x, center.y  + points[n].y);
  }
  center = new PVector(center.x / points.length, center.y / points.length);

  return center;
}

PVector remapPoint(PVector p) {
  PVector zoomPoint = new PVector(width/2, height/2);

  float dist = dist(p.x, p.y, zoomPoint.x, zoomPoint.y);
  if (dist < 0.01) {
    p = new PVector(p.x+0.01, p.y+0.01);
  }

  float angle = getAngle(p, zoomPoint);

  float newDist = dist*cameraZoom;

  float pPosX = (cos(angle) * newDist) + cameraPos.x;
  float pPosY = (sin(angle) * newDist) + cameraPos.y;

  PVector remappedPoint = new PVector(pPosX, pPosY);
  return remappedPoint;
}

PVector inverseRemapPoint(PVector p) {
  //float dist = dist(p.x, p.y, cameraPos.x, cameraPos.y);
  float dx = p.x - cameraPos.x;
  float dy = p.y - cameraPos.y;

  return new PVector(-dx, -dy);
}

float getAngle(PVector of, PVector from) {
  PVector p1 = of;
  PVector p2 = from;

  float dX = p1.x - p2.x;
  float dY = p1.y - p2.y;

  float angle = atan2(dY, dX);

  return angle;
}

PVector slerp(PVector curr, PVector end, float percent) {
  float dot = curr.dot(end);
  dot = dot > 1 ? 1 :dot;
  dot = dot < -1 ? -1 : dot;

  float t = acos(dot)*percent;
  PVector relVec = new PVector((end.x - curr.x)*dot, (end.y - curr.y) * dot);
  relVec = relVec.normalize();

  PVector currT = new PVector(curr.x * cos(t), curr.y * cos(t));
  PVector relT = new PVector(relVec.x * sin(t), relVec.y  *sin(t));

  return new PVector(currT.x + relT.x, currT.y + relT.y);
}

boolean circleCollidingLine(PVector cPos, float cRad, PVector lP1, PVector lP2) {
  float dist = dist(cPos.x, cPos.y, lP1.x, lP1.y);
  float angle = getAngle(lP1, lP2);

  PVector closestPoint = new PVector((cos(angle) * -dist) + lP1.x, (sin(angle)* -dist) + lP1.y);
  float cDist = dist(cPos.x, cPos.y, closestPoint.x, closestPoint.y);

  boolean colliding=false;
  if (cDist < cRad) {
    colliding=true;
  }
  return colliding;
}

PVector lineIntersection(Line l1, Line l2) { 
  PVector lineIntersectPoint = null;

  if (lineIntersects(l1, l2)) {
    float mL1 = (l1.end.y - l1.start.y)  /  (l1.end.x - l1.start.x);
    float mL2 = (l2.end.y - l2.start.y)  /  (l2.end.x - l2.start.x);

    float cL1 = (l1.start.y / mL1) + l1.start.x;
    float cL2 = (l2.start.y / mL2) + l2.start.x;

    float cDiff = cL1 - cL2;
    float mDiff = mL1 - mL2;

    float x = cDiff / mDiff;

    float y = (mL1*x) + cL1;

    lineIntersectPoint = new PVector(x, y);
  }

  return lineIntersectPoint;
}

boolean lineIntersects(Line l1, Line l2) {
  Line p1 = l1;

  float l1p1SX = l1.start.x < l1.end.x ? l1.start.x : l1.end.x;
  float l1p1SY = l1.start.y < l1.end.y ? l1.start.y : l1.end.y;

  float l1p2SX = l1p1SX == l1.start.x ? l1.end.x : l1.start.x;
  float l1p2SY = l1p1SY == l1.start.y ? l1.end.y : l1.end.y;
  p1 = new Line(new PVector(l1p1SX, l1p1SY), new PVector(l1p2SX, l1p2SY));

  Line p2 = l2;
  float l2p1SX = l2.start.x < l2.end.x ? l2.start.x : l2.end.x;
  float l2p1SY = l2.start.y < l2.end.y ? l2.start.y : l2.end.y;

  float l2p2SX = l2p1SX == l2.start.x ? l2.end.x : l1.start.x;
  float l2p2SY = l2p1SY == l2.start.y ? l2.end.y : l2.start.y; 

  boolean colliding = false;
  if (p1.start.x < p2.end.x) {
    if (p1.end.x > p2.start.x) {
      if (p1.start.y < p2.start.y) {
        if (p1.start.y > p2.end.y) {
          colliding =true;
        }
      }
    }
  }

  return colliding;
}

class Line {
  PVector start;
  PVector end;

  Line(PVector start, PVector end) {
    this.start = start;
    this.end = end;
  }
}