package utils;

import java.text.NumberFormat;

public class DataUtils {

    // [Data | Conversion]

    public static String binary(char value) {
        return "";
    }

    public static String binary(byte value) {
        return "";
    }

    public static String binary(int value) {
        return "";
    }

    public static String binary(int value, int digits) {
        return "";
    }

    public static String hex(char value) {
        return "";
    }

    public static String hex(byte value) {
        return "";
    }

    public static String hex(int value) {
        return "";
    }

    public static String hex(int value, int digits) {
        return "";
    }

    // String conversion (primatives)

    public static String str(boolean value) {
        return String.valueOf(value);
    }

    public static String str(byte value) {
        return String.valueOf(value);
    }

    public static String str(char value) {
        return String.valueOf(value);
    }

    public static String str(int value) {
        return String.valueOf(value);
    }

    public static String str(double value) {
        return String.valueOf(value);
    }

    // String conversion (arrays)

    public static String[] str(boolean[] values) {
        String[] s = new String[values.length];
        for (int i = 0; i < values.length; i++) s[i] = str(values[i]);
        return s;
    }

    public static String[] str(byte[] values) {
        String[] s = new String[values.length];
        for (int i = 0; i < values.length; i++) s[i] = str(values[i]);
        return s;
    }

    public static String[] str(char[] values) {
        String[] s = new String[values.length];
        for (int i = 0; i < values.length; i++) s[i] = str(values[i]);
        return s;
    }

    public static String[] str(int[] values) {
        String[] s = new String[values.length];
        for (int i = 0; i < values.length; i++) s[i] = str(values[i]);
        return s;
    }

    public static String[] str(double[] values) {
        String[] s = new String[values.length];
        for (int i = 0; i < values.length; i++) s[i] = str(values[i]);
        return s;
    }

    public static int unbinary(String value) {
        return 0;
    }

    public static int unhex(String value) {
        return 0;
    }

    // [Data | String Functions]

    public static String join(String[] list, char seperator) {
        return "";
    }

    public static String join(String[] list, String seperator) {
        return "";
    }

    public static String[] match(String str, String regexp) {
        return null;
    }

    public static String[][] matchAll(String str, String regexp) {
        return null;
    }

    // [Data | String Functions | nf()]

    static private NumberFormat int_nf;
    static private int int_nf_digits;
    static private boolean int_nf_commas;

    static private NumberFormat float_nf;
    static private int float_nf_left, float_nf_right;
    static private boolean float_nf_commas;

    public static String nf(double num) {
        int inum = (int) num;
        if (num == inum) return str(inum);
        return str(num);
    }

    public static String nf(double num, int digits) {
        return nf(num, 0, digits);
    }

    public static String nf(int num, int digits) {
        if ((int_nf != null) && (int_nf_digits == digits) && !int_nf_commas) {
            return int_nf.format(num);
        }
    
        int_nf = NumberFormat.getInstance();
        int_nf.setGroupingUsed(false); // no commas
        int_nf_commas = false;
        int_nf.setMinimumIntegerDigits(digits);
        int_nf_digits = digits;
        return int_nf.format(num);
    }

    public static String nf(double num, int left, int right) {
        if ((float_nf != null) && (float_nf_left == left) && (float_nf_right == right) && !float_nf_commas) {
            return float_nf.format(num);
        }
    
        float_nf = NumberFormat.getInstance();
        float_nf.setGroupingUsed(false);
        float_nf_commas = false;
    
        if (left != 0) float_nf.setMinimumIntegerDigits(left);
        if (right != 0) {
            float_nf.setMinimumFractionDigits(right);
            float_nf.setMaximumFractionDigits(right);
        }
        float_nf_left = left;
        float_nf_right = right;
        return float_nf.format(num);
    }

    public static String[] nf(double[] nums) {
        String[] outgoing = new String[nums.length];
        for (int i = 0; i < nums.length; i++) outgoing[i] = nf(nums[i]);
        return outgoing;
    }

    public static String[] nf(double[] nums, int digits) {
        return nf(nums, 0, digits);
    }

    public static String[] nf(int[] nums, int digits) {
        String[] formatted = new String[nums.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = nf(nums[i], digits);
        }
        return formatted;
    }

    public static String[] nf(double[] nums, int left, int right) {
        String[] formatted = new String[nums.length];
        for (int i = 0; i < formatted.length; i++) {
            formatted[i] = nf(nums[i], left, right);
        }
        return formatted;
    }

    // [Data | String Functions | nfc()]

    public static String nfc(double num) {
        return "";
    }

    public static String nfc(int num) {
        return "";
    }

    public static String nfc(double num, int right) {
        return "";
    }

    public static String nfc(int num, int right) {
        return "";
    }

    public static String[] nfc(double[] nums) {
        return null;
    }

    public static String[] nfc(int[] nums) {
        return null;
    }

    public static String[] nfc(double[] nums, int right) {
        return null;
    }

    public static String[] nfc(int[] nums, int right) {
        return null;
    }

    // [Data | String Functions | nfp()]

    public static String nfp(double num, int digits) {
        return "";
    }

    public static String nfp(int num, int digits) {
        return "";
    }

    public static String nfp(double num, int left, int right) {
        return "";
    }

    public static String nfp(int num, int left, int right) {
        return "";
    }

    public static String[] nfp(double[] nums, int digits) {
        return null;
    }

    public static String[] nfp(int[] nums, int digits) {
        return null;
    }

    public static String[] nfp(double[] nums, int left, int right) {
        return null;
    }

    public static String[] nfp(int[] nums, int left, int right) {
        return null;
    }

    // [Data | String Functions | nfs()]
    

    public static String nfs(double num, int digits) {
        return "";
    }

    public static String nfs(int num, int digits) {
        return "";
    }

    public static String nfs(double num, int left, int right) {
        return "";
    }

    public static String nfs(int num, int left, int right) {
        return "";
    }

    public static String[] nfs(double[] nums, int digits) {
        return null;
    }

    public static String[] nfs(int[] nums, int digits) {
        return null;
    }

    public static String[] nfs(double[] nums, int left, int right) {
        return null;
    }

    public static String[] nfs(int[] nums, int left, int right) {
        return null;
    }

    public static String[] split(String value, char delim) {
        return null;
    }

    public static String[] split(String value, String delim) {
        return null;
    }

    public static String[] splitTokens(String value) {
        return null;
    }

    public static String[] splitTokens(String value, String delim) {
        return null;
    }

    public static String trim(String str) {
        return "";
    }

    public static String[] trim(String[] array) {
        return null;
    }
    
}