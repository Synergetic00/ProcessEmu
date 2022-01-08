size(1000, 1000);

int num = 10;
int size = width / num;

colorMode(HSB, num, num, num);

for (int i = 0; i < num; i++) {
    for (int j = 0; j < num; j++) {
        fill(i, j, num);
        rect(i*size, j*size, size, size);
    }
}