package utils;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static utils.Constants.*;

public class DataUtils {

	////////////////
	// Conversion //
	////////////////

	// binary()

	static final public String binary(byte value) {
		return binary(value, 8);
	}

	static final public String binary(char value) {
		return binary(value, 16);
	}

	static final public String binary(int value) {
		return binary(value, 32);
	}

	static final public String binary(int value, int digits) {
		String stuff = Integer.toBinaryString(value);
		if (digits > 32) digits = 32;
		int length = stuff.length();
		if (length > digits) {
			return stuff.substring(length - digits);
		} else if (length < digits) {
			int offset = 32 - (digits-length);
			return "00000000000000000000000000000000".substring(offset) + stuff;
		}
		return stuff;
	}

	// boolean()

	static final public boolean parseBoolean(int what) {
		return (what != 0);
	}

	static final public boolean parseBoolean(String what) {
		return Boolean.parseBoolean(what);
	}

	static final public boolean[] parseBoolean(int[] what) {
		boolean[] outgoing = new boolean[what.length];
		for (int i = 0; i < what.length; i++) {
			outgoing[i] = (what[i] != 0);
		}
		return outgoing;
	}

	static final public boolean[] parseBoolean(String[] what) {
		boolean[] outgoing = new boolean[what.length];
		for (int i = 0; i < what.length; i++) {
			outgoing[i] = Boolean.parseBoolean(what[i]);
		}
		return outgoing;
	}

	// byte()

	static final public byte parseByte(boolean what) {
		return what ? (byte)1 : 0;
	}

	static final public byte parseByte(char what) {
		return (byte) what;
	}

	static final public byte parseByte(int what) {
		return (byte) what;
	}

	static final public byte parseByte(double what) {
		return (byte) what;
	}

	static final public byte[] parseByte(boolean[] what) {
		byte[] outgoing = new byte[what.length];
		for (int i = 0; i < what.length; i++) {
			outgoing[i] = what[i] ? (byte)1 : 0;
		}
		return outgoing;
	}

	static final public byte[] parseByte(char[] what) {
		byte[] outgoing = new byte[what.length];
		for (int i = 0; i < what.length; i++) {
			outgoing[i] = (byte) what[i];
		}
		return outgoing;
	}

	static final public byte[] parseByte(int[] what) {
		byte[] outgoing = new byte[what.length];
		for (int i = 0; i < what.length; i++) {
			outgoing[i] = (byte) what[i];
		}
		return outgoing;
	}

	static final public byte[] parseByte(double[] what) {
		byte[] outgoing = new byte[what.length];
		for (int i = 0; i < what.length; i++) {
			outgoing[i] = (byte) what[i];
		}
		return outgoing;
	}

	// char()

	static final public char parseChar(byte what) {
		return (char) (what & 0xff);
	}

	static final public char parseChar(int what) {
		return (char) what;
	}

	static final public char[] parseChar(byte[] what) {
		char[] outgoing = new char[what.length];
		for (int i = 0; i < what.length; i++) {
			outgoing[i] = (char) (what[i] & 0xff);
		}
		return outgoing;
	}

	static final public char[] parseChar(int what[]) {
		char outgoing[] = new char[what.length];
		for (int i = 0; i < what.length; i++) {
			outgoing[i] = (char) what[i];
		}
		return outgoing;
	}

	// double()

	static final public double parseDouble(int what) {
		return what;
	}

	static final public double parseDouble(String what) {
		return parseDouble(what, Double.NaN);
	}

	static final public double parseDouble(String what, double otherwise) {
		try {
			return Double.parseDouble(what);
		} catch (NumberFormatException e) { }

		return otherwise;
	}

	static final public double[] parseDouble(byte[] what) {
		double[] doubleies = new double[what.length];
		for (int i = 0; i < what.length; i++) {
			doubleies[i] = what[i];
		}
		return doubleies;
	}

	static final public double[] parseDouble(int[] what) {
		double[] doubleies = new double[what.length];
		for (int i = 0; i < what.length; i++) {
			doubleies[i] = what[i];
		}
		return doubleies;
	}

	static final public double[] parseDouble(String[] what) {
		return parseDouble(what, Double.NaN);
	}

	static final public double[] parseDouble(String[] what, double missing) {
		double[] output = new double[what.length];
		for (int i = 0; i < what.length; i++) {
			try {
				output[i] = Double.parseDouble(what[i]);
			} catch (NumberFormatException e) {
				output[i] = missing;
			}
		}
		return output;
	}

	// hex()

	static final public String hex(byte value) {
		return hex(value, 2);
	}

	static final public String hex(char value) {
		return hex(value, 4);
	}

	static final public String hex(int value) {
		return hex(value, 8);
	}

	static final public String hex(int value, int digits) {
		String stuff = Integer.toHexString(value).toUpperCase();
		if (digits > 8) digits = 8;
		int length = stuff.length();
		if (length > digits) {
			return stuff.substring(length - digits);
		} else if (length < digits) {
			return "00000000".substring(8 - (digits-length)) + stuff;
		}
		return stuff;
	}

	// int()

	static final public int parseInt(boolean what) {
		return what ? 1 : 0;
	}

	static final public int parseInt(byte what) {
		return what & 0xff;
	}

	static final public int parseInt(char what) {
		return what;
	}

	static final public int parseInt(double what) {
		return (int) what;
	}

	static final public int parseInt(String what) {
		return parseInt(what, 0);
	}

	static final public int parseInt(String what, int otherwise) {
		try {
			int offset = what.indexOf('.');
			if (offset == -1) {
				return Integer.parseInt(what);
			} else {
				return Integer.parseInt(what.substring(0, offset));
			}
		} catch (NumberFormatException e) { }
		return otherwise;
	}

	static final public int[] parseInt(boolean[] what) {
		int[] list = new int[what.length];
		for (int i = 0; i < what.length; i++) {
			list[i] = what[i] ? 1 : 0;
		}
		return list;
	}

	static final public int[] parseInt(byte[] what) {  // unsigns
		int[] list = new int[what.length];
		for (int i = 0; i < what.length; i++) {
			list[i] = (what[i] & 0xff);
		}
		return list;
	}

	static final public int[] parseInt(char[] what) {
		int[] list = new int[what.length];
		for (int i = 0; i < what.length; i++) {
			list[i] = what[i];
		}
		return list;
	}

	static public int[] parseInt(double[] what) {
		int[] inties = new int[what.length];
		for (int i = 0; i < what.length; i++) {
			inties[i] = (int)what[i];
		}
		return inties;
	}

	static public int[] parseInt(String[] what) {
		return parseInt(what, 0);
	}

	static public int[] parseInt(String[] what, int missing) {
		int[] output = new int[what.length];
		for (int i = 0; i < what.length; i++) {
			try {
				output[i] = Integer.parseInt(what[i]);
			} catch (NumberFormatException e) {
				output[i] = missing;
			}
		}
		return output;
	}

	// str()

	static final public String str(boolean x) {
		return String.valueOf(x);
	}

	static final public String str(byte x) {
		return String.valueOf(x);
	}

	static final public String str(char x) {
		return String.valueOf(x);
	}

	static final public String str(int x) {
		return String.valueOf(x);
	}

	static final public String str(double x) {
		return String.valueOf(x);
	}

	static final public String[] str(boolean[] x) {
		String[] s = new String[x.length];
		for (int i = 0; i < x.length; i++) s[i] = String.valueOf(x[i]);
		return s;
	}

	static final public String[] str(byte[] x) {
		String[] s = new String[x.length];
		for (int i = 0; i < x.length; i++) s[i] = String.valueOf(x[i]);
		return s;
	}

	static final public String[] str(char[] x) {
		String[] s = new String[x.length];
		for (int i = 0; i < x.length; i++) s[i] = String.valueOf(x[i]);
		return s;
	}

	static final public String[] str(int[] x) {
		String[] s = new String[x.length];
		for (int i = 0; i < x.length; i++) s[i] = String.valueOf(x[i]);
		return s;
	}

	static final public String[] str(double[] x) {
		String[] s = new String[x.length];
		for (int i = 0; i < x.length; i++) s[i] = String.valueOf(x[i]);
		return s;
	}

	// unbinary()
	
	static final public int unbinary(String value) {
		return Integer.parseInt(value, 2);
	}

	// unhex()

	static final public int unhex(String value) {
		return (int) (Long.parseLong(value, 16));
	}

	//////////////////////
	// String Functions //
	//////////////////////

	static private NumberFormat double_nf;
	static private int double_nf_left, double_nf_right;
	static private boolean double_nf_commas;

	static private NumberFormat int_nf;
	static private int int_nf_digits;
	static private boolean int_nf_commas;

	static protected LinkedHashMap<String, Pattern> matchPatterns;

	// join()

	static public String join(String[] list, char separator) {
		return join(list, String.valueOf(separator));
	}

	static public String join(String[] list, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			if (i != 0) sb.append(separator);
			sb.append(list[i]);
		}
		return sb.toString();
	}
	
	// match()

	static public String[] match(String str, String regexp) {
		Pattern p = matchPattern(regexp);
		Matcher m = p.matcher(str);
		if (m.find()) {
			int count = m.groupCount() + 1;
			String[] groups = new String[count];
			for (int i = 0; i < count; i++) {
				groups[i] = m.group(i);
			}
			return groups;
		}
		return null;
	}

	static Pattern matchPattern(String regexp) {
		Pattern p = null;
		if (matchPatterns == null) {
			matchPatterns = new LinkedHashMap<String, Pattern>(16, 0.75f, true) {
				@Override
				protected boolean removeEldestEntry(Map.Entry<String, Pattern> eldest) {
					return size() == 10;
				}
			};
		} else {
			p = matchPatterns.get(regexp);
		}
		if (p == null) {
			p = Pattern.compile(regexp, Pattern.MULTILINE | Pattern.DOTALL);
			matchPatterns.put(regexp, p);
		}
		return p;
	}

	// matchAll()

	static public String[][] matchAll(String str, String regexp) {
		Pattern p = matchPattern(regexp);
		Matcher m = p.matcher(str);
		List<String[]> results = new ArrayList<>();
		int count = m.groupCount() + 1;
		while (m.find()) {
			String[] groups = new String[count];
			for (int i = 0; i < count; i++) groups[i] = m.group(i);
			results.add(groups);
		}
		if (results.isEmpty()) return null;
		String[][] matches = new String[results.size()][count];
		for (int i = 0; i < matches.length; i++) matches[i] = results.get(i);
		return matches;
	}

	// nf()

	static public String nf(double num) {
		int inum = (int) num;
		if (num == inum) return str(inum);
		return str(num);
	}

	static public String[] nf(double[] nums) {
		String[] outgoing = new String[nums.length];
		for (int i = 0; i < nums.length; i++) outgoing[i] = nf(nums[i]);
		return outgoing;
	}

	static public String[] nf(int[] nums, int digits) {
		String[] formatted = new String[nums.length];
		for (int i = 0; i < formatted.length; i++) formatted[i] = nf(nums[i], digits);
		return formatted;
	}

	static public String nf(int num, int digits) {
		if ((int_nf != null) && (int_nf_digits == digits) && !int_nf_commas) {
			return int_nf.format(num);
		}

		int_nf = NumberFormat.getInstance();
		int_nf.setGroupingUsed(false);
		int_nf_commas = false;
		int_nf.setMinimumIntegerDigits(digits);
		int_nf_digits = digits;
		return int_nf.format(num);
	}

	static public String[] nf(double[] nums, int left, int right) {
		String[] formatted = new String[nums.length];
		for (int i = 0; i < formatted.length; i++) {
			formatted[i] = nf(nums[i], left, right);
		}
		return formatted;
	}

	static public String nf(double num, int left, int right) {
		if ((double_nf != null) && (double_nf_left == left) && (double_nf_right == right) && !double_nf_commas) {
			return double_nf.format(num);
		}

		double_nf = NumberFormat.getInstance();
		double_nf.setGroupingUsed(false);
		double_nf_commas = false;

		if (left != 0) double_nf.setMinimumIntegerDigits(left);
		if (right != 0) {
			double_nf.setMinimumFractionDigits(right);
			double_nf.setMaximumFractionDigits(right);
		}
		double_nf_left = left;
		double_nf_right = right;
		return double_nf.format(num);
	}

	// nfc()

	static public String[] nfc(int[] nums) {
		String[] formatted = new String[nums.length];
		for (int i = 0; i < formatted.length; i++) {
			formatted[i] = nfc(nums[i]);
		}
		return formatted;
	}

	static public String nfc(int num) {
		if ((int_nf != null) &&
				(int_nf_digits == 0) &&
				int_nf_commas) {
			return int_nf.format(num);
		}

		int_nf = NumberFormat.getInstance();
		int_nf.setGroupingUsed(true);
		int_nf_commas = true;
		int_nf.setMinimumIntegerDigits(0);
		int_nf_digits = 0;
		return int_nf.format(num);
	}

	static public String[] nfc(double[] nums, int right) {
		String[] formatted = new String[nums.length];
		for (int i = 0; i < formatted.length; i++) {
			formatted[i] = nfc(nums[i], right);
		}
		return formatted;
	}

	static public String nfc(double num, int right) {
		if ((double_nf != null) && (double_nf_left == 0) && (double_nf_right == right) && double_nf_commas) {
			return double_nf.format(num);
		}

		double_nf = NumberFormat.getInstance();
		double_nf.setGroupingUsed(true);
		double_nf_commas = true;

		if (right != 0) {
			double_nf.setMinimumFractionDigits(right);
			double_nf.setMaximumFractionDigits(right);
		}
		double_nf_left = 0;
		double_nf_right = right;
		return double_nf.format(num);
	}

	// nfp()

	static public String nfp(int num, int digits) {
		return (num < 0) ? nf(num, digits) : ('+' + nf(num, digits));
	}

	static public String[] nfp(int[] nums, int digits) {
		String[] formatted = new String[nums.length];
		for (int i = 0; i < formatted.length; i++) {
			formatted[i] = nfp(nums[i], digits);
		}
		return formatted;
	}

	static public String[] nfp(double[] nums, int left, int right) {
		String[] formatted = new String[nums.length];
		for (int i = 0; i < formatted.length; i++) {
			formatted[i] = nfp(nums[i], left, right);
		}
		return formatted;
	}

	static public String nfp(double num, int left, int right) {
		return (num < 0) ? nf(num, left, right) :  ('+' + nf(num, left, right));
	}

	// nfs()

	static public String nfs(int num, int digits) {
		return (num < 0) ? nf(num, digits) : (' ' + nf(num, digits));
	}

	static public String[] nfs(int[] nums, int digits) {
		String[] formatted = new String[nums.length];
		for (int i = 0; i < formatted.length; i++) {
			formatted[i] = nfs(nums[i], digits);
		}
		return formatted;
	}

	static public String[] nfs(double[] nums, int left, int right) {
		String[] formatted = new String[nums.length];
		for (int i = 0; i < formatted.length; i++) {
			formatted[i] = nfs(nums[i], left, right);
		}
		return formatted;
	}

	static public String nfs(double num, int left, int right) {
		return (num < 0) ? nf(num, left, right) :  (' ' + nf(num, left, right));
	}

	// split()

	static public String[] split(String value, char delim) {
		if (value == null) return null;

		char[] chars = value.toCharArray();
		int splitCount = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == delim) splitCount++;
		}

		if (splitCount == 0) {
			String[] splits = new String[1];
			splits[0] = value;
			return splits;
		}

		String[] splits = new String[splitCount + 1];
		int splitIndex = 0;
		int startIndex = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == delim) {
				splits[splitIndex++] =
						new String(chars, startIndex, i-startIndex);
				startIndex = i + 1;
			}
		}
		splits[splitIndex] = new String(chars, startIndex, chars.length-startIndex);
		return splits;
	}


	static public String[] split(String value, String delim) {
		List<String> items = new ArrayList<>();
		int index;
		int offset = 0;
		while ((index = value.indexOf(delim, offset)) != -1) {
			items.add(value.substring(offset, index));
			offset = index + delim.length();
		}
		items.add(value.substring(offset));
		String[] outgoing = new String[items.size()];
		items.toArray(outgoing);
		return outgoing;
	}

	// splitTokens()

	static public String[] splitTokens(String value) {
		return splitTokens(value, WHITESPACE);
	}
	
	static public String[] splitTokens(String value, String delim) {
		StringTokenizer toker = new StringTokenizer(value, delim);
		String[] pieces = new String[toker.countTokens()];

		int index = 0;
		while (toker.hasMoreTokens()) {
			pieces[index++] = toker.nextToken();
		}
		return pieces;
	}

	// trim()

	static public String trim(String str) {
		if (str == null) return null;
		return str.replace('\u00A0', ' ').trim();
	}

	static public String[] trim(String[] array) {
		if (array == null) return null;
		String[] outgoing = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) outgoing[i] = trim(array[i]);
		}
		return outgoing;
	}

	/////////////////////
	// Array Functions //
	/////////////////////

	// append()

	public static final byte[] append(byte[] array, byte value) {
		array = expand(array, array.length + 1);
		array[array.length-1] = value;
		return array;
	}

	public static final char[] append(char[] array, char value) {
		array = expand(array, array.length + 1);
		array[array.length-1] = value;
		return array;
	}
	
	public static final int[] append(int[] array, int value) {
		array = expand(array, array.length + 1);
		array[array.length-1] = value;
		return array;
	}

	public static final double[] append(double[] array, double value) {
		array = expand(array, array.length + 1);
		array[array.length-1] = value;
		return array;
	}

	public static final String[] append(String[] array, String value) {
		array = expand(array, array.length + 1);
		array[array.length-1] = value;
		return array;
	}

	public static final Object append(Object array, Object value) {
		int length = Array.getLength(array);
		array = expand(array, length + 1);
		Array.set(array, length, value);
		return array;
	}

	// arrayCopy()

	public static final void arrayCopy(Object src, int srcPosition, Object dst, int dstPosition, int length) {
		arrayCopy(src, srcPosition, dst, dstPosition, length);
	}

	public static final void arrayCopy(Object src, Object dst, int length) {
		arrayCopy(src, 0, dst, 0, length);
	}

	public static final void arrayCopy(Object src, Object dst) {
		arrayCopy(src, 0, dst, 0, Array.getLength(src));
	}

	// concat()

	public static final boolean[] concat(boolean[] a, boolean[] b) {
		boolean[] c = new boolean[a.length + b.length];
		arrayCopy(a, 0, c, 0, a.length);
		arrayCopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static final byte[] concat(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		arrayCopy(a, 0, c, 0, a.length);
		arrayCopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static final char[] concat(char[] a, char[] b) {
		char[] c = new char[a.length + b.length];
		arrayCopy(a, 0, c, 0, a.length);
		arrayCopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static final int[] concat(int[] a, int[] b) {
		int[] c = new int[a.length + b.length];
		arrayCopy(a, 0, c, 0, a.length);
		arrayCopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static final double[] concat(double[] a, double[] b) {
		double[] c = new double[a.length + b.length];
		arrayCopy(a, 0, c, 0, a.length);
		arrayCopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static final String[] concat(String[] a, String[] b) {
		String[] c = new String[a.length + b.length];
		arrayCopy(a, 0, c, 0, a.length);
		arrayCopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static final Object concat(Object a, Object b) {
		Class<?> type = a.getClass().getComponentType();
		int alength = Array.getLength(a);
		int blength = Array.getLength(b);
		Object outgoing = Array.newInstance(type, alength + blength);
		arrayCopy(a, 0, outgoing, 0, alength);
		arrayCopy(b, 0, outgoing, alength, blength);
		return outgoing;
	}

	// expand()

	public static final boolean[] expand(boolean[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	public static final byte[] expand(byte[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	public static final char[] expand(char[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	public static final int[] expand(int[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	public static final long[] expand(long[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	public static final double[] expand(double[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	public static final String[] expand(String[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	public static final boolean[] expand(boolean[] list, int newSize) {
		boolean[] temp = new boolean[newSize];
		arrayCopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	public static final byte[] expand(byte[] list, int newSize) {
		byte[] temp = new byte[newSize];
		arrayCopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	public static final char[] expand(char[] list, int newSize) {
		char[] temp = new char[newSize];
		arrayCopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	public static final int[] expand(int[] list, int newSize) {
		int[] temp = new int[newSize];
		arrayCopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	public static final long[] expand(long[] list, int newSize) {
		long[] temp = new long[newSize];
		arrayCopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	public static final double[] expand(double[] list, int newSize) {
		double[] temp = new double[newSize];
		arrayCopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	public static final String[] expand(String[] list, int newSize) {
		String[] temp = new String[newSize];
		arrayCopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	public static final Object expand(Object list, int newSize) {
		Class<?> type = list.getClass().getComponentType();
		Object temp = Array.newInstance(type, newSize);
		arrayCopy(list, 0, temp, 0, Math.min(Array.getLength(list), newSize));
		return temp;
	}

	// reverse()

	public static final boolean[] reverse(boolean[] list) {
		boolean[] outgoing = new boolean[list.length];
		int length1 = list.length - 1;
		for (int i = 0; i < list.length; i++) {
			outgoing[i] = list[length1 - i];
		}
		return outgoing;
	}

	public static final byte[] reverse(byte[] list) {
		byte[] outgoing = new byte[list.length];
		int length1 = list.length - 1;
		for (int i = 0; i < list.length; i++) {
			outgoing[i] = list[length1 - i];
		}
		return outgoing;
	}

	public static final char[] reverse(char[] list) {
		char[] outgoing = new char[list.length];
		int length1 = list.length - 1;
		for (int i = 0; i < list.length; i++) {
			outgoing[i] = list[length1 - i];
		}
		return outgoing;
	}

	public static final int[] reverse(int[] list) {
		int[] outgoing = new int[list.length];
		int length1 = list.length - 1;
		for (int i = 0; i < list.length; i++) {
			outgoing[i] = list[length1 - i];
		}
		return outgoing;
	}

	public static final double[] reverse(double[] list) {
		double[] outgoing = new double[list.length];
		int length1 = list.length - 1;
		for (int i = 0; i < list.length; i++) {
			outgoing[i] = list[length1 - i];
		}
		return outgoing;
	}

	public static final String[] reverse(String[] list) {
		String[] outgoing = new String[list.length];
		int length1 = list.length - 1;
		for (int i = 0; i < list.length; i++) {
			outgoing[i] = list[length1 - i];
		}
		return outgoing;
	}

	public static final Object reverse(Object list) {
		Class<?> type = list.getClass().getComponentType();
		int length = Array.getLength(list);
		Object outgoing = Array.newInstance(type, length);
		for (int i = 0; i < length; i++) {
			Array.set(outgoing, i, Array.get(list, (length - 1) - i));
		}
		return outgoing;
	}

	// shorten()

	public static final boolean[] shorten(boolean[] list) {
		return subset(list, 0, list.length-1);
	}

	public static final byte[] shorten(byte[] list) {
		return subset(list, 0, list.length-1);
	}

	public static final char[] shorten(char[] list) {
		return subset(list, 0, list.length-1);
	}

	public static final int[] shorten(int[] list) {
		return subset(list, 0, list.length-1);
	}

	public static final double[] shorten(double[] list) {
		return subset(list, 0, list.length-1);
	}

	public static final String[] shorten(String[] list) {
		return subset(list, 0, list.length-1);
	}

	public static final Object shorten(Object list) {
		int length = Array.getLength(list);
		return subset(list, 0, length - 1);
	}

	// sort()

	public static final byte[] sort(byte[] list) {
		return sort(list, list.length);
	}

	/**
	 * @param count number of elements to sort, starting from 0
	 */
	public static final byte[] sort(byte[] list, int count) {
		byte[] outgoing = new byte[list.length];
		arrayCopy(list, 0, outgoing, 0, list.length);
		Arrays.sort(outgoing, 0, count);
		return outgoing;
	}

	public static final char[] sort(char[] list) {
		return sort(list, list.length);
	}

	public static final char[] sort(char[] list, int count) {
		char[] outgoing = new char[list.length];
		arrayCopy(list, 0, outgoing, 0, list.length);
		Arrays.sort(outgoing, 0, count);
		return outgoing;
	}

	public static final int[] sort(int[] list) {
		return sort(list, list.length);
	}

	public static final int[] sort(int[] list, int count) {
		int[] outgoing = new int[list.length];
		arrayCopy(list, 0, outgoing, 0, list.length);
		Arrays.sort(outgoing, 0, count);
		return outgoing;
	}

	public static final double[] sort(double[] list) {
		return sort(list, list.length);
	}

	public static final double[] sort(double[] list, int count) {
		double[] outgoing = new double[list.length];
		arrayCopy(list, 0, outgoing, 0, list.length);
		Arrays.sort(outgoing, 0, count);
		return outgoing;
	}

	public static final String[] sort(String[] list) {
		return sort(list, list.length);
	}

	public static final String[] sort(String[] list, int count) {
		String[] outgoing = new String[list.length];
		arrayCopy(list, 0, outgoing, 0, list.length);
		Arrays.sort(outgoing, 0, count);
		return outgoing;
	}

	// splice()

	public static final boolean[] splice(boolean[] list, boolean value, int index) {
		boolean[] outgoing = new boolean[list.length + 1];
		arrayCopy(list, 0, outgoing, 0, index);
		outgoing[index] = value;
		arrayCopy(list, index, outgoing, index + 1, list.length - index);
		return outgoing;
	}

	public static final boolean[] splice(boolean[] list, boolean[] value, int index) {
		boolean[] outgoing = new boolean[list.length + value.length];
		arrayCopy(list, 0, outgoing, 0, index);
		arrayCopy(value, 0, outgoing, index, value.length);
		arrayCopy(list, index, outgoing, index + value.length, list.length - index);
		return outgoing;
	}

	public static final byte[] splice(byte[] list, byte value, int index) {
		byte[] outgoing = new byte[list.length + 1];
		arrayCopy(list, 0, outgoing, 0, index);
		outgoing[index] = value;
		arrayCopy(list, index, outgoing, index + 1, list.length - index);
		return outgoing;
	}

	public static final byte[] splice(byte[] list, byte[] value, int index) {
		byte[] outgoing = new byte[list.length + value.length];
		arrayCopy(list, 0, outgoing, 0, index);
		arrayCopy(value, 0, outgoing, index, value.length);
		arrayCopy(list, index, outgoing, index + value.length, list.length - index);
		return outgoing;
	}


	public static final char[] splice(char[] list, char value, int index) {
		char[] outgoing = new char[list.length + 1];
		arrayCopy(list, 0, outgoing, 0, index);
		outgoing[index] = value;
		arrayCopy(list, index, outgoing, index + 1, list.length - index);
		return outgoing;
	}

	public static final char[] splice(char[] list, char[] value, int index) {
		char[] outgoing = new char[list.length + value.length];
		arrayCopy(list, 0, outgoing, 0, index);
		arrayCopy(value, 0, outgoing, index, value.length);
		arrayCopy(list, index, outgoing, index + value.length, list.length - index);
		return outgoing;
	}

	public static final int[] splice(int[] list, int value, int index) {
		int[] outgoing = new int[list.length + 1];
		arrayCopy(list, 0, outgoing, 0, index);
		outgoing[index] = value;
		arrayCopy(list, index, outgoing, index + 1, list.length - index);
		return outgoing;
	}

	public static final int[] splice(int[] list, int[] value, int index) {
		int[] outgoing = new int[list.length + value.length];
		arrayCopy(list, 0, outgoing, 0, index);
		arrayCopy(value, 0, outgoing, index, value.length);
		arrayCopy(list, index, outgoing, index + value.length, list.length - index);
		return outgoing;
	}

	public static final double[] splice(double[] list, double value, int index) {
		double[] outgoing = new double[list.length + 1];
		arrayCopy(list, 0, outgoing, 0, index);
		outgoing[index] = value;
		arrayCopy(list, index, outgoing, index + 1, list.length - index);
		return outgoing;
	}

	public static final double[] splice(double[] list, double[] value, int index) {
		double[] outgoing = new double[list.length + value.length];
		arrayCopy(list, 0, outgoing, 0, index);
		arrayCopy(value, 0, outgoing, index, value.length);
		arrayCopy(list, index, outgoing, index + value.length, list.length - index);
		return outgoing;
	}

	public static final String[] splice(String[] list, String value, int index) {
		String[] outgoing = new String[list.length + 1];
		arrayCopy(list, 0, outgoing, 0, index);
		outgoing[index] = value;
		arrayCopy(list, index, outgoing, index + 1, list.length - index);
		return outgoing;
	}

	public static final String[] splice(String[] list, String[] value, int index) {
		String[] outgoing = new String[list.length + value.length];
		arrayCopy(list, 0, outgoing, 0, index);
		arrayCopy(value, 0, outgoing, index, value.length);
		arrayCopy(list, index, outgoing, index + value.length, list.length - index);
		return outgoing;
	}

	public static final Object splice(Object list, Object value, int index) {
		Class<?> type = list.getClass().getComponentType();
		Object outgoing = null;
		int length = Array.getLength(list);

		if (value.getClass().getName().charAt(0) == '[') {
			int vlength = Array.getLength(value);
			outgoing = Array.newInstance(type, length + vlength);
			arrayCopy(list, 0, outgoing, 0, index);
			arrayCopy(value, 0, outgoing, index, vlength);
			arrayCopy(list, index, outgoing, index + vlength, length - index);
		} else {
			outgoing = Array.newInstance(type, length + 1);
			arrayCopy(list, 0, outgoing, 0, index);
			Array.set(outgoing, index, value);
			arrayCopy(list, index, outgoing, index + 1, length - index);
		}
		return outgoing;
	}

	// subset()

	public static final boolean[] subset(boolean[] list, int start) {
		return subset(list, start, list.length - start);
	}
	
	public static final byte[] subset(byte[] list, int start) {
		return subset(list, start, list.length - start);
	}

	public static final char[] subset(char[] list, int start) {
		return subset(list, start, list.length - start);
	}

	public static final int[] subset(int[] list, int start) {
		return subset(list, start, list.length - start);
	}

	public static final long[] subset(long[] list, int start) {
		return subset(list, start, list.length - start);
	}

	public static final double[] subset(double[] list, int start) {
		return subset(list, start, list.length - start);
	}

	public static final String[] subset(String[] list, int start) {
		return subset(list, start, list.length - start);
	}

	public static final Object subset(Object list, int start) {
		int length = Array.getLength(list);
		return subset(list, start, length - start);
	}

	public static final boolean[] subset(boolean[] list, int start, int count) {
		boolean[] output = new boolean[count];
		arrayCopy(list, start, output, 0, count);
		return output;
	}

	public static final byte[] subset(byte[] list, int start, int count) {
		byte[] output = new byte[count];
		arrayCopy(list, start, output, 0, count);
		return output;
	}

	public static final char[] subset(char[] list, int start, int count) {
		char[] output = new char[count];
		arrayCopy(list, start, output, 0, count);
		return output;
	}

	public static final int[] subset(int[] list, int start, int count) {
		int[] output = new int[count];
		arrayCopy(list, start, output, 0, count);
		return output;
	}

	public static final long[] subset(long[] list, int start, int count) {
		long[] output = new long[count];
		arrayCopy(list, start, output, 0, count);
		return output;
	}

	public static final double[] subset(double[] list, int start, int count) {
		double[] output = new double[count];
		arrayCopy(list, start, output, 0, count);
		return output;
	}

	public static final String[] subset(String[] list, int start, int count) {
		String[] output = new String[count];
		arrayCopy(list, start, output, 0, count);
		return output;
	}

	public static final Object subset(Object list, int start, int count) {
		Class<?> type = list.getClass().getComponentType();
		Object outgoing = Array.newInstance(type, count);
		arrayCopy(list, start, outgoing, 0, count);
		return outgoing;
	}

}