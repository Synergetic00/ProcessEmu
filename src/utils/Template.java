package utils;

public class Template {

    // [Structure]

    public void draw() {}

    public void exit() {

    }

    public void loop() {

    }

    public void noLoop() {

    }

    public void pop() {

    }

    public void popStyle() {

    }

    public void push() {

    }

    public void pushStyle() {

    }

    public void redraw() {

    }

    public void setup() {}

    // [Environment]

    public void cursor() {

    }

    public void cursor(int kind) {

    }

    public void cursor(PImage img) {

    }

    public void cursor(PImage img, int x, int y) {
        
    }

    public void delay(int napTime) {
        
    }

    public int displayDensity() {
        return 0;
    }

    public int displayDensity(int display) {
        return 0;
    }
    
    public boolean focused;

    public int frameCount;

    public void frameRate(double fps) {

    }

    public int frameRate;

    public void fullScreen() {

    }

    public void fullScreen(int display) {

    }

    public void fullScreen(String renderer) {

    }

    public void fullScreen(String renderer, int display) {

    }

    public int height;

    public void noCursor() {

    }

    public void noSmooth() {
        
    }

    public void pixelDensity(int density) {
        
    }

    public int pixelHeight;

    public int pixelWidth;

    public void settings() {}

    public void size(int width, int height) {

    }

    public void size(int width, int height, String renderer) {

    }

    public void smooth() {

    }

    public int width;

    // [Data | Conversion]

    public String binary(char value) {
        return "";
    }

    public String binary(byte value) {
        return "";
    }

    public String binary(int value) {
        return "";
    }

    public String binary(int value, int digits) {
        return "";
    }

    public String hex(char value) {
        return "";
    }

    public String hex(byte value) {
        return "";
    }

    public String hex(int value) {
        return "";
    }

    public String hex(int value, int digits) {
        return "";
    }

    public String str(boolean value) {
        return "";
    }

    public String str(byte value) {
        return "";
    }

    public String str(char value) {
        return "";
    }

    public String str(int value) {
        return "";
    }

    public String str(double value) {
        return "";
    }

    public int unbinary(String value) {
        return 0;
    }

    public int unhex(String value) {
        return 0;
    }

    // [Data | String Functions]

    public String join(String[] list, char seperator) {
        return "";
    }

    public String join(String[] list, String seperator) {
        return "";
    }

    public String[] match(String str, String regexp) {
        return null;
    }

    public String[][] matchAll(String str, String regexp) {
        return null;
    }

    // [Data | String Functions | nf()]

    public String nf(double num) {
        return "";
    }

    public String nf(int num) {
        return "";
    }

    public String nf(double num, int digits) {
        return "";
    }

    public String nf(int num, int digits) {
        return "";
    }

    public String nf(double num, int left, int right) {
        return "";
    }

    public String nf(int num, int left, int right) {
        return "";
    }

    public String[] nf(double[] nums) {
        return null;
    }

    public String[] nf(int[] nums) {
        return null;
    }

    public String[] nf(double[] nums, int digits) {
        return null;
    }

    public String[] nf(int[] nums, int digits) {
        return null;
    }

    public String[] nf(double[] nums, int left, int right) {
        return null;
    }

    public String[] nf(int[] nums, int left, int right) {
        return null;
    }

    // [Data | String Functions | nfc()]

    public String nfc(double num) {
        return "";
    }

    public String nfc(int num) {
        return "";
    }

    public String nfc(double num, int right) {
        return "";
    }

    public String nfc(int num, int right) {
        return "";
    }

    public String[] nfc(double[] nums) {
        return null;
    }

    public String[] nfc(int[] nums) {
        return null;
    }

    public String[] nfc(double[] nums, int right) {
        return null;
    }

    public String[] nfc(int[] nums, int right) {
        return null;
    }

    // [Data | String Functions | nfp()]

    public String nfp(double num, int digits) {
        return "";
    }

    public String nfp(int num, int digits) {
        return "";
    }

    public String nfp(double num, int left, int right) {
        return "";
    }

    public String nfp(int num, int left, int right) {
        return "";
    }

    public String[] nfp(double[] nums, int digits) {
        return null;
    }

    public String[] nfp(int[] nums, int digits) {
        return null;
    }

    public String[] nfp(double[] nums, int left, int right) {
        return null;
    }

    public String[] nfp(int[] nums, int left, int right) {
        return null;
    }

    // [Data | String Functions | nfs()]
    

    public String nfs(double num, int digits) {
        return "";
    }

    public String nfs(int num, int digits) {
        return "";
    }

    public String nfs(double num, int left, int right) {
        return "";
    }

    public String nfs(int num, int left, int right) {
        return "";
    }

    public String[] nfs(double[] nums, int digits) {
        return null;
    }

    public String[] nfs(int[] nums, int digits) {
        return null;
    }

    public String[] nfs(double[] nums, int left, int right) {
        return null;
    }

    public String[] nfs(int[] nums, int left, int right) {
        return null;
    }

    public String[] split(String value, char delim) {
        return null;
    }

    public String[] split(String value, String delim) {
        return null;
    }

    public String[] splitTokens(String value) {
        return null;
    }

    public String[] splitTokens(String value, String delim) {
        return null;
    }

    public String trim(String str) {
        return "";
    }

    public String[] trim(String[] array) {
        return null;
    }
    
}