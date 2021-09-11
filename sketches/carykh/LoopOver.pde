//E
//E

String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

final int WIDTH = 1000;

int BOARD_WIDTH = 5;
int BOARD_HEIGHT = 5;
float TILE_SIZE = 200;

final int GAME_MODE = 0;

final int SOLVES_IN_AVERAGE = 5;
int[][] board;
float[] shades;
float[][] transitions;
PFont font;
int highlightX = 2;
int highlightY = 2;
boolean horizontalLast = false;
int UI_SIZE = 300;
int startMillis = -1;
int endMillis = -1;
int correctTiles = 0;
int[] times = new int[SOLVES_IN_AVERAGE];
int average = -1;
int minIndex = -1;
int maxIndex = -1;
boolean isScrambled = false;
int centerTileX = BOARD_WIDTH/2;
int centerTileY = BOARD_HEIGHT/2;

int timeSinceLastWASD = -9999;

int moveCount = 0;
int resizeDelay = 0;

float TRANSITION_FACTOR = 0.666667;
boolean lastMoused = true;
boolean isSpaceDown = false;
boolean isShiftDown = false;
boolean isMouseDown = false;

void setup(){
  frameRate(60);
  font = loadFont("ArialMT-48.vlw");
  
  resetBoard(5);
  //size(BOARD_WIDTH*TILE_SIZE+UI_SIZE,BOARD_HEIGHT*TILE_SIZE);
  size(1300,1000);
  noSmooth();
  noStroke();
}
void resetBoard(int inputSize){
  horizontalLast = false;
  isScrambled = false;
  
  BOARD_HEIGHT = inputSize;
  BOARD_WIDTH = inputSize;
  
  highlightX = centerTileX = (int)(BOARD_WIDTH/2);
  highlightY = centerTileY = (int)(BOARD_HEIGHT/2);

  TILE_SIZE = ((float)1000.0)/inputSize;
  
  board = new int[BOARD_HEIGHT][BOARD_WIDTH];
  shades = new float[BOARD_HEIGHT*BOARD_WIDTH];
  transitions = new float[2][max(BOARD_WIDTH,BOARD_HEIGHT)];
  
  for(int y = 0; y < BOARD_HEIGHT; y++){
    for(int x = 0; x < BOARD_WIDTH; x++){
      board[y][x] = y*BOARD_WIDTH+x;
      shades[y*BOARD_WIDTH+x] = random(0.5,1.0);
    }
  }
  for(int i = 0; i < 2; i++){
    for(int j = 0; j < max(BOARD_HEIGHT,BOARD_WIDTH); j++){
      transitions[i][j] = 0;
    }
  }
  for(int i = 0; i < SOLVES_IN_AVERAGE; i++){
    times[i] = -1;
  }
  
  startMillis = -1;
  endMillis = -1;
  
  correctTiles = BOARD_WIDTH*BOARD_HEIGHT;
  
  moveCount = 0;
}
void draw(){
  if(isMouseDown && endMillis == -1){
    lastMoused = true;
    int newX = floor((float)mouseX/TILE_SIZE+BOARD_WIDTH)%BOARD_WIDTH;
    int newY = floor((float)mouseY/TILE_SIZE+BOARD_HEIGHT)%BOARD_HEIGHT;
    while(hasDiff(newX,highlightX,true) && endMillis == -1 && isMoveableTile(true)){
      move(0,highlightX,highlightY, false);
    }
    while(hasDiff(newX,highlightX,false) && endMillis == -1 && isMoveableTile(true)){
      move(1,highlightX,highlightY, false);
    }
    while(hasDiff(newY,highlightY,true) && endMillis == -1 && isMoveableTile(false)){
      move(2,highlightX,highlightY, false);
    }
    while(hasDiff(newY,highlightY,false) && endMillis == -1 && isMoveableTile(false)){
      move(3,highlightX,highlightY, false);
    }
  }
  for(int i = 0; i < 2; i++){
    for(int j = 0; j < max(BOARD_HEIGHT,BOARD_WIDTH); j++){
      if(i == 0){
        if(transitions[i][j] > ((float)BOARD_WIDTH)/2){
          transitions[i][j] -= BOARD_WIDTH;
        }else if(transitions[i][j] < -((float)BOARD_WIDTH)/2){
          transitions[i][j] += BOARD_WIDTH;
        }
      }else if(i == 1){
        if(transitions[i][j] > ((float)BOARD_HEIGHT)/2){
          transitions[i][j] -= BOARD_HEIGHT;
        }else if(transitions[i][j] < -((float)BOARD_HEIGHT)/2){
          transitions[i][j] += BOARD_HEIGHT;
        }
      }
      transitions[i][j] *= TRANSITION_FACTOR;
    }
  }
  background(160,200,250);
  setFont(0);
  for(int y = 0; y < BOARD_HEIGHT; y++){
    for(int x = 0; x < BOARD_WIDTH; x++){
      float ax = x;
      if(horizontalLast){
        ax += transitions[0][y];
      }
      ax = (ax+BOARD_WIDTH)%BOARD_WIDTH;
      float ay = y;
      if(!horizontalLast){
        ay += transitions[1][x];
      }
      ay = (ay+BOARD_HEIGHT)%BOARD_HEIGHT;
      int i = board[y][x];
      while(ay > -1){
        float aax = ax;
        while(aax > -1){
          //fill(255*shades[i],190*shades[i],150*shades[i]);
          if(GAME_MODE == 1 && x == centerTileX && y == centerTileY){
            fill(255,255,255);
          }else{
            float colorX = (float)(i%BOARD_WIDTH)/(BOARD_WIDTH-1);
            float colorY = (float)(i/BOARD_WIDTH)/(BOARD_HEIGHT-1);
            
            /*float xDist = abs((i%BOARD_WIDTH)-x);
            if(xDist >= 3) xDist = 5-xDist;
            
            float yDist = abs((i/BOARD_WIDTH)-y);
            if(yDist >= 3) yDist = 5-yDist;
            float relDist = ((float)(xDist+yDist))/5.0;
            colorX = 1-relDist;
            fill(colorX*155+100);*/
            fill((1-colorX)*255,colorY*255,colorX*255);
          }
          rect(aax*TILE_SIZE,ay*TILE_SIZE,TILE_SIZE,TILE_SIZE);
          fill(0,0,0);
          if(BOARD_WIDTH*BOARD_HEIGHT > 26){
            int py = floor(i/BOARD_WIDTH);
            int px = i%BOARD_WIDTH;
            text((i+1),(aax+0.5)*TILE_SIZE,(ay+0.7)*TILE_SIZE);
          }else{
            text(letters[i],(aax+0.5)*TILE_SIZE,(ay+0.82)*TILE_SIZE);
          }
          if(!lastMoused && x == highlightX && y == highlightY){
            noFill();
            float f = TILE_SIZE*0.038;
            strokeWeight(f*2);
            stroke(abs((frameCount*20)%510-255));
            if(isSpaceDown || millis()-timeSinceLastWASD < 500){
              rect(aax*TILE_SIZE+f*2,ay*TILE_SIZE+f*2,TILE_SIZE-f*4,TILE_SIZE-f*4);
              line(aax*TILE_SIZE+f*2,ay*TILE_SIZE+f*2,aax*TILE_SIZE+f*6,ay*TILE_SIZE+f*6);
              line((aax+1)*TILE_SIZE-f*2,ay*TILE_SIZE+f*2,(aax+1)*TILE_SIZE-f*6,ay*TILE_SIZE+f*6);
              line(aax*TILE_SIZE+f*2,(ay+1)*TILE_SIZE-f*2,aax*TILE_SIZE+f*6,(ay+1)*TILE_SIZE-f*6);
              line((aax+1)*TILE_SIZE-f*2,(ay+1)*TILE_SIZE-f*2,(aax+1)*TILE_SIZE-f*6,(ay+1)*TILE_SIZE-f*6);
            }else{
              rect(aax*TILE_SIZE+f,ay*TILE_SIZE+f,TILE_SIZE-f*2,TILE_SIZE-f*2);
            }
            noStroke();
          }
          aax -= BOARD_WIDTH;
        }
        ay -= BOARD_HEIGHT;
      }
    }
  }
  fill(60,60,60);
  rect(BOARD_WIDTH*TILE_SIZE,0,UI_SIZE,BOARD_HEIGHT*TILE_SIZE);
  fill(255,255,255);
  String s = "";
  if(startMillis >= 0 && endMillis >= 0){
    fill(0,255,0);
    s = timeToString(endMillis-startMillis);
  }else if(startMillis >= 0 && correctTiles < BOARD_WIDTH*BOARD_HEIGHT){
    s = timeToString(millis()-startMillis);
  }else if(isScrambled){
    s = "Scrambled!";
  }
  
  setFont(1);
  text(s,width-10,150);
  textFont(font,28);
  if(startMillis >= 0){
    String details = "";
    float mps = 0;
    if(moveCount >= 2){
      if(endMillis >= 0){
        mps = ((float)1000.0)*(moveCount-1)/(endMillis-startMillis);
      }else{
        mps = ((float)1000.0)*(moveCount-1)/(millis()-startMillis);
      }
    }
    details = moveCount+" mov,  "+nf(mps,0,2)+" mps";
    text(details,width-10,208);
    text("1st move not counted\nin mps calculation",width-10,240);
  }
  setFont(1);
  for(int i = 0; i < SOLVES_IN_AVERAGE; i++){
    String str = "-";
    if(times[i] >= 0){
      str = timeToString(times[i]);
    }
    fill(255,255,255);
    if(i == minIndex || i == maxIndex){
      str = "("+str+")";
      fill(160);
    }
    text(str,width-10,height-450-i*50);
  }
  fill(255,255,255);
  s = "-";
  if(average >= 0){
    s = timeToString(average);
  }
  text("= "+s,width-10,height-350);
  
  textFont(font,36);
  String[] names = {"Controls","Reset with\nbigger board","Reset with\nsmaller board"};
  for(int i = 0; i < 3; i++){
    fill(100);
    rect(1020,height-330+110*i,260,90);
    fill(255);
    textAlign(CENTER);
    text(names[i],1150,height-295+110*i);
  }
  if(abs(mouseX-1150) <= 150 && abs(mouseY-(height-285)) <= 45){
    textFont(font,28);
    fill(100);
    rect(1020,height-740,260,720);
    fill(255);
    textAlign(CENTER);
    text("CONTROLS\n(not all necessary)\n\nMouse/touch screen:\ndrag tiles\n\nIJKL / arrow keys:\nmove cursor\n\nHold space:\ncling cursor to grid\n\nWASD:\ncling & move cursor\n\nShift + letter:\nTeleport to that tile\n\nEnter:\ninsta-click\nupper-right button",1150,height-705);
  }
  textFont(font,40);
  fill(100);
  rect(1020,10,260,80);
  fill(255);
  String ss = "[SCRAMBLE]";
  if(startMillis >= 0 && endMillis >= 0){
    ss = "[UNLOCK]";
  }else if(startMillis >= 0 && correctTiles < BOARD_WIDTH*BOARD_HEIGHT){
    ss = "[RESET]";
  }else if(isScrambled){
    ss = "[RESET]";
  }
  text(ss,1150,62);
  textAlign(RIGHT);
  
  resizeDelay = max(0,resizeDelay-1);
}
boolean hasDiff(int newn, int old, boolean isLower){
  int diff = (newn+BOARD_WIDTH-old)%BOARD_WIDTH;
  if(isLower){
    return (diff >= 1 && diff <= BOARD_WIDTH/2);
  }else{
    return (diff >= (BOARD_WIDTH+1)/2 && diff <= BOARD_WIDTH-1);
  }
}
String toChar(int i){
  if(i < 10){
    return i+"";
  }else{
    return letters[i-10];
  }
}
boolean isMoveableTile(boolean isHorizontal){
  return (GAME_MODE != 1 || 
  ((highlightX == centerTileX && !isHorizontal) || 
  (highlightY == centerTileY && isHorizontal)));
}
String timeToString(int mil){
  int sec = mil%60000;
  int min = floor(mil/60000);
  if(min == 0){
    return nf((float)(sec)/1000,0,3);
  }else if(sec < 10000){
    return min+":0"+nf((float)(sec)/1000,0,3);
  }else{
    return min+":"+nf((float)(sec)/1000,0,3);
  }
}
void setFont(int n){
  if(n == 0){
    if(BOARD_WIDTH*BOARD_HEIGHT >= 26){
      textFont(font,TILE_SIZE*0.53);
    }else{
      textFont(font,TILE_SIZE*0.9);
    }
    textAlign(CENTER);
  }else if(n == 1){
    textAlign(RIGHT);
    textFont(font,50);
  }
}
void mousePressed(){
  if(mouseX >= 1000){
    for(int i = 0; i < 2; i++){
      float xDiff = abs(mouseX-1150);
      float yDiff = abs(mouseY-(height-185+110*i));
      if(xDiff <= 150 && yDiff <= 45){
        if(resizeDelay <= 0){
          resetBoard(min(max(BOARD_WIDTH+(1-2*i),2),20));
          resizeDelay = 10;
        }
      }
    }
    if(mouseX >= 1020 && mouseY < 100){
      if(resizeDelay <= 0){
        dealWithEnter();
        resizeDelay = 10;
      }
    }
  }else{
    isMouseDown = true;
    highlightX = floor(mouseX/TILE_SIZE%BOARD_WIDTH)%BOARD_WIDTH;
    highlightY = floor(mouseY/TILE_SIZE%BOARD_HEIGHT)%BOARD_HEIGHT;
  }
}
void mouseReleased(){
  isMouseDown = false;
}
void keyPressed(){
  if(key == '\n'){
    dealWithEnter();
  }else if(endMillis == -1){
    lastMoused = false;
  }
  if(key == ' '){
    isSpaceDown = true;
  }
  if(key == CODED && keyCode == SHIFT){
    isShiftDown = true;
  }
  /*if(key == 'd'){
    move(0,highlightX,highlightY, false);
  }else if(key == 'a'){
    move(1,highlightX,highlightY, false);
  }else if(key == 's'){
    move(2,highlightX,highlightY, false);
  }else if(key == 'w'){
    move(3,highlightX,highlightY, false);
  }else if(key == 'l'){
    highlightX = (highlightX+1)%BOARD_WIDTH;
  }else if(key == 'j'){
    highlightX = (highlightX+BOARD_WIDTH-1)%BOARD_WIDTH;
  }else if(key == 'k'){
    highlightY = (highlightY+1)%BOARD_HEIGHT;
  }else if(key == 'i'){
    highlightY = (highlightY+BOARD_HEIGHT-1)%BOARD_HEIGHT;
  }*/
  if(isShiftDown && BOARD_WIDTH*BOARD_HEIGHT <= 26){
    int theKey = (int)(key);
    if(theKey <= 64+26){
      theKey -= 65;
    }else{
      theKey -= 97;
    }
    if(theKey >= 0 && theKey < 26){
      for(int x = 0; x < BOARD_WIDTH; x++){
        for(int y = 0; y < BOARD_HEIGHT; y++){
          if(board[y][x] == theKey){
            highlightX = x;
            highlightY = y;
          }
        }
      }
    }
  }else{
    if(endMillis == -1){
      if(key == 'd' || key == 'D' || key == 'a' || key == 'A' || 
      key == 's' || key == 'S' || key == 'w' || key == 'W'){
        timeSinceLastWASD = millis();
      }
      if((rightPressed() && isSpaceDown) || key == 'd' || key == 'D'){
        move(0,highlightX,highlightY, false);
      }else if((leftPressed() && isSpaceDown) || key == 'a' || key == 'A'){
        move(1,highlightX,highlightY, false);
      }else if((downPressed() && isSpaceDown) || key == 's' || key == 'S'){
        move(2,highlightX,highlightY, false);
      }else if((upPressed() && isSpaceDown) || key == 'w' || key == 'W'){
        move(3,highlightX,highlightY, false);
      }
    }
    if(!isSpaceDown){
      if(rightPressed()){
        highlightX = (highlightX+1)%BOARD_WIDTH;
      }else if(leftPressed()){
        highlightX = (highlightX+BOARD_WIDTH-1)%BOARD_WIDTH;
      }else if(downPressed()){
        highlightY = (highlightY+1)%BOARD_HEIGHT;
      }else if(upPressed()){
        highlightY = (highlightY+BOARD_HEIGHT-1)%BOARD_HEIGHT;
      }
    }
  }
}
boolean rightPressed(){
  return ((key == CODED && keyCode == RIGHT) || key == 'l' || key == 'L');
}
boolean leftPressed(){
  return ((key == CODED && keyCode == LEFT) || key == 'j' || key == 'J');
}
boolean downPressed(){
  return ((key == CODED && keyCode == DOWN)|| key == 'k' || key == 'K');
}
boolean upPressed(){
  return ((key == CODED && keyCode == UP) || key == 'i' || key == 'I');
}
void keyReleased(){
  if(key == ' '){
    isSpaceDown = false;
  }
  if(key == CODED && keyCode == SHIFT){
    isShiftDown = false;
  }
}
void dealWithEnter(){
  if(endMillis >= 0){
    endMillis = -1;
    startMillis = -1;
  }else if(!isScrambled && startMillis == -1){
    float low = pow(max(BOARD_WIDTH,BOARD_HEIGHT),1.4)*60;
    float high = pow(max(BOARD_WIDTH,BOARD_HEIGHT),1.5)*60+30;
    scramble((int)(random(low, high)));
  }else{
    resetBoard(BOARD_WIDTH);
  }
}
void move(int moveDirection, int hx, int hy, boolean scrambleMove){
  if(moveDirection == 0){
    int ph = board[hy][BOARD_WIDTH-1];
    for(int x = BOARD_WIDTH-1; x >= 1; x--){
      setBoardTile(x,hy,board[hy][x-1]);
    }
    setBoardTile(0,hy,ph);
    if(highlightY == hy) highlightX++;
    transitions[0][hy]--;
    horizontalLast = true;
  }else if(moveDirection == 1){
    int ph = board[hy][0];
    for(int x = 0; x < BOARD_WIDTH-1; x++){
      setBoardTile(x,hy,board[hy][x+1]);
    }
    setBoardTile(BOARD_WIDTH-1,hy,ph);
    if(highlightY == hy) highlightX--;
    transitions[0][hy]++;
    horizontalLast = true;
  }else if(moveDirection == 2){
    int ph = board[BOARD_HEIGHT-1][hx];
    for(int y = BOARD_HEIGHT-1; y >= 1; y--){
      setBoardTile(hx,y,board[y-1][hx]);
    }
    setBoardTile(hx,0,ph);
    if(highlightX == hx) highlightY++;
    transitions[1][hx]--;
    horizontalLast = false;
  }else{
    int ph = board[0][hx];
    for(int y = 0; y < BOARD_HEIGHT-1; y++){
      setBoardTile(hx,y,board[y+1][hx]);
    }
    setBoardTile(hx,BOARD_HEIGHT-1,ph);
    if(highlightX == hx) highlightY--;
    transitions[1][hx]++;
    horizontalLast = false;
  }
  highlightX = (highlightX+BOARD_WIDTH)%BOARD_WIDTH;
  highlightY = (highlightY+BOARD_HEIGHT)%BOARD_HEIGHT;
  if(scrambleMove){
    startMillis = -1;
    isScrambled = true;
  }else{
    if(startMillis == -1 && isScrambled){
      startMillis = millis();
    }
    isScrambled = false;
    moveCount++;
  }
  if(correctTiles == BOARD_WIDTH*BOARD_HEIGHT){
    if(startMillis >= 0){
      solvedStuff();
    }
  }else if(endMillis >= 0){
    startMillis = -1;
    endMillis = -1;
  }
}
void solvedStuff(){
  endMillis = millis();
  lastMoused = true;
  for(int i = SOLVES_IN_AVERAGE-1; i >= 1; i--){
    times[i] = times[i-1];
  }
  times[0] = endMillis-startMillis;
  average = -1;
  if(times[SOLVES_IN_AVERAGE-1] >= 0){
    int sum = 0;
    int min = 2000000000;
    int max = -1;
    for(int i = 0; i < SOLVES_IN_AVERAGE; i++){
      sum += times[i];
      if(times[i] > max){
        max = times[i];
        maxIndex = i;
      }
      if(times[i] < min){
        min = times[i];
        minIndex = i;
      }
    }
    sum -= max+min;
    average = sum/3;
  }
}
void setBoardTile(int x, int y, int value){
  if(isCorrect(x,y)) correctTiles--;
  board[y][x] = value;
  if(isCorrect(x,y)) correctTiles++;
  if(value == BOARD_WIDTH*(BOARD_HEIGHT/2)+BOARD_WIDTH/2){
    centerTileX = x;
    centerTileY = y;
  }
}
boolean isCorrect(int x, int y){
  return (board[y][x] == y*BOARD_WIDTH+x);
}
void scramble(int scrambleMoveCount){
  int currentMove = -1;
  int i = 0;
  while(i < scrambleMoveCount || correctTiles == BOARD_WIDTH*BOARD_HEIGHT){
    currentMove = getNextRandomMove(currentMove);
    if(GAME_MODE == 1){
      move(currentMove,centerTileX,centerTileY,true);
    }else{
      move(currentMove,floor(random(0,BOARD_WIDTH)),floor(random(0,BOARD_HEIGHT)),true);
    }
    i++;
  }
  moveCount = 0;
}
int getNextRandomMove(int prev){
  if(prev == -1){
    return floor(random(0,4));
  }else if(prev <= 1){
    return floor(random(2,4));
  }else{
    return floor(random(0,2));
  }
}