package utils;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.*;
import java.util.zip.*;

import static utils.Constants.*;

import main.FXApp;

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

	

    static public String trim(String str) {
        if (str == null) {
          return null;
        }
        return str.replace('\u00A0', ' ').trim();
      }
    
    
     /**
      * @param array a String array
      */
      static public String[] trim(String[] array) {
        if (array == null) {
          return null;
        }
        String[] outgoing = new String[array.length];
        for (int i = 0; i < array.length; i++) {
          if (array[i] != null) {
            outgoing[i] = trim(array[i]);
          }
        }
        return outgoing;
      }

	static public void arrayCopy(Object src, int srcPosition,
			Object dst, int dstPosition,
			int length) {
		System.arraycopy(src, srcPosition, dst, dstPosition, length);
	}

	/**
	 * Convenience method for arraycopy().
	 * Identical to <CODE>arraycopy(src, 0, dst, 0, length);</CODE>
	 */
	static public void arrayCopy(Object src, Object dst, int length) {
		System.arraycopy(src, 0, dst, 0, length);
	}

	/**
	 * Shortcut to copy the entire contents of
	 * the source into the destination array.
	 * Identical to <CODE>arraycopy(src, 0, dst, 0, src.length);</CODE>
	 */
	static public void arrayCopy(Object src, Object dst) {
		System.arraycopy(src, 0, dst, 0, Array.getLength(src));
	}

	/**
	 * Use arrayCopy() instead.
	 */
	@Deprecated
	static public void arraycopy(Object src, int srcPosition,
			Object dst, int dstPosition,
			int length) {
		System.arraycopy(src, srcPosition, dst, dstPosition, length);
	}

	/**
	 * Use arrayCopy() instead.
	 */
	@Deprecated
	static public void arraycopy(Object src, Object dst, int length) {
		System.arraycopy(src, 0, dst, 0, length);
	}

	/**
	 * Use arrayCopy() instead.
	 */
	@Deprecated
	static public void arraycopy(Object src, Object dst) {
		System.arraycopy(src, 0, dst, 0, Array.getLength(src));
	}

	static final public float parseFloat(int what) {  // also handles byte
		return what;
	}

	static final public float parseFloat(String what) {
		return parseFloat(what, Float.NaN);
	}

	static final public float parseFloat(String what, float otherwise) {
		try {
			return Float.parseFloat(what);
		} catch (NumberFormatException e) { }

		return otherwise;
	}

	static final public float[] parseFloat(byte[] what) {
		float[] floaties = new float[what.length];
		for (int i = 0; i < what.length; i++) {
			floaties[i] = what[i];
		}
		return floaties;
	}

	static final public float[] parseFloat(int[] what) {
		float[] floaties = new float[what.length];
		for (int i = 0; i < what.length; i++) {
			floaties[i] = what[i];
		}
		return floaties;
	}

	static final public float[] parseFloat(String[] what) {
		return parseFloat(what, Float.NaN);
	}

	static final public float[] parseFloat(String[] what, float missing) {
		float[] output = new float[what.length];
		for (int i = 0; i < what.length; i++) {
			try {
				output[i] = Float.parseFloat(what[i]);
			} catch (NumberFormatException e) {
				output[i] = missing;
			}
		}
		return output;
	}

	static public boolean[] subset(boolean[] list, int start) {
		return subset(list, start, list.length - start);
	}


	/**
	 * ( begin auto-generated from subset.xml )
	 *
	 * Extracts an array of elements from an existing array. The <b>array</b>
	 * parameter defines the array from which the elements will be copied and
	 * the <b>offset</b> and <b>length</b> parameters determine which elements
	 * to extract. If no <b>length</b> is given, elements will be extracted
	 * from the <b>offset</b> to the end of the array. When specifying the
	 * <b>offset</b> remember the first array element is 0. This function does
	 * not change the source array.
	 * <br/> <br/>
	 * When using an array of objects, the data returned from the function must
	 * be cast to the object array's data type. For example: <em>SomeClass[]
	 * items = (SomeClass[]) subset(originalArray, 0, 4)</em>.
	 *
	 * ( end auto-generated )
	 * @webref data:array_functions
	 * @param list array to extract from
	 * @param start position to begin
	 * @param count number of values to extract
	 * @see PApplet#splice(boolean[], boolean, int)
	 */
	static public boolean[] subset(boolean[] list, int start, int count) {
		boolean[] output = new boolean[count];
		System.arraycopy(list, start, output, 0, count);
		return output;
	}


	static public byte[] subset(byte[] list, int start) {
		return subset(list, start, list.length - start);
	}


	static public byte[] subset(byte[] list, int start, int count) {
		byte[] output = new byte[count];
		System.arraycopy(list, start, output, 0, count);
		return output;
	}


	static public char[] subset(char[] list, int start) {
		return subset(list, start, list.length - start);
	}


	static public char[] subset(char[] list, int start, int count) {
		char[] output = new char[count];
		System.arraycopy(list, start, output, 0, count);
		return output;
	}


	static public int[] subset(int[] list, int start) {
		return subset(list, start, list.length - start);
	}


	static public int[] subset(int[] list, int start, int count) {
		int[] output = new int[count];
		System.arraycopy(list, start, output, 0, count);
		return output;
	}


	static public long[] subset(long[] list, int start) {
		return subset(list, start, list.length - start);
	}


	static public long[] subset(long[] list, int start, int count) {
		long[] output = new long[count];
		System.arraycopy(list, start, output, 0, count);
		return output;
	}


	static public float[] subset(float[] list, int start) {
		return subset(list, start, list.length - start);
	}


	static public float[] subset(float[] list, int start, int count) {
		float[] output = new float[count];
		System.arraycopy(list, start, output, 0, count);
		return output;
	}


	static public double[] subset(double[] list, int start) {
		return subset(list, start, list.length - start);
	}


	static public double[] subset(double[] list, int start, int count) {
		double[] output = new double[count];
		System.arraycopy(list, start, output, 0, count);
		return output;
	}


	static public String[] subset(String[] list, int start) {
		return subset(list, start, list.length - start);
	}


	static public String[] subset(String[] list, int start, int count) {
		String[] output = new String[count];
		System.arraycopy(list, start, output, 0, count);
		return output;
	}


	static public Object subset(Object list, int start) {
		int length = Array.getLength(list);
		return subset(list, start, length - start);
	}


	static public Object subset(Object list, int start, int count) {
		Class<?> type = list.getClass().getComponentType();
		Object outgoing = Array.newInstance(type, count);
		System.arraycopy(list, start, outgoing, 0, count);
		return outgoing;
	}

	static public boolean[] expand(boolean[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	/**
	 * @param newSize new size for the array
	 */
	static public boolean[] expand(boolean[] list, int newSize) {
		boolean[] temp = new boolean[newSize];
		System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	static public byte[] expand(byte[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	static public byte[] expand(byte[] list, int newSize) {
		byte[] temp = new byte[newSize];
		System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	static public char[] expand(char[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	static public char[] expand(char[] list, int newSize) {
		char[] temp = new char[newSize];
		System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	static public int[] expand(int[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	static public int[] expand(int[] list, int newSize) {
		int[] temp = new int[newSize];
		System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	static public long[] expand(long[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	static public long[] expand(long[] list, int newSize) {
		long[] temp = new long[newSize];
		System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	static public float[] expand(float[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	static public float[] expand(float[] list, int newSize) {
		float[] temp = new float[newSize];
		System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	static public double[] expand(double[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	static public double[] expand(double[] list, int newSize) {
		double[] temp = new double[newSize];
		System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	static public String[] expand(String[] list) {
		return expand(list, list.length > 0 ? list.length << 1 : 1);
	}

	static public String[] expand(String[] list, int newSize) {
		String[] temp = new String[newSize];
		// in case the new size is smaller than list.length
		System.arraycopy(list, 0, temp, 0, Math.min(newSize, list.length));
		return temp;
	}

	/**
	 * @nowebref
	 */
	static public Object expand(Object array) {
		int len = Array.getLength(array);
		return expand(array, len > 0 ? len << 1 : 1);
	}

	static public Object expand(Object list, int newSize) {
		Class<?> type = list.getClass().getComponentType();
		Object temp = Array.newInstance(type, newSize);
		System.arraycopy(list, 0, temp, 0,
				Math.min(Array.getLength(list), newSize));
		return temp;
	}

	static final public int parseInt(boolean what) {
		return what ? 1 : 0;
	}

	/**
	 * Note that parseInt() will un-sign a signed byte value.
	 */
	static final public int parseInt(byte what) {
		return what & 0xff;
	}

	/**
	 * Note that parseInt('5') is unlike String in the sense that it
	 * won't return 5, but the ascii value. This is because ((int) someChar)
	 * returns the ascii value, and parseInt() is just longhand for the cast.
	 */
	static final public int parseInt(char what) {
		return what;
	}

	/**
	 * Same as floor(), or an (int) cast.
	 */
	static final public int parseInt(float what) {
		return (int) what;
	}

	/**
	 * Parse a String into an int value. Returns 0 if the value is bad.
	 */
	static final public int parseInt(String what) {
		return parseInt(what, 0);
	}

	/**
	 * Parse a String to an int, and provide an alternate value that
	 * should be used when the number is invalid.
	 */
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

	// . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .

	static final public int[] parseInt(boolean[] what) {
		int[] list = new int[what.length];
		for (int i = 0; i < what.length; i++) {
			list[i] = what[i] ? 1 : 0;
		}
		return list;
	}

	static final public int[] parseInt(byte[] what) {  // note this unsigns
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

	static public int[] parseInt(float[] what) {
		int[] inties = new int[what.length];
		for (int i = 0; i < what.length; i++) {
			inties[i] = (int)what[i];
		}
		return inties;
	}

	/**
	 * Make an array of int elements from an array of String objects.
	 * If the String can't be parsed as a number, it will be set to zero.
	 *
	 * String s[] = { "1", "300", "44" };
	 * int numbers[] = parseInt(s);
	 *
	 * numbers will contain { 1, 300, 44 }
	 */
	static public int[] parseInt(String[] what) {
		return parseInt(what, 0);
	}

	/**
	 * Make an array of int elements from an array of String objects.
	 * If the String can't be parsed as a number, its entry in the
	 * array will be set to the value of the "missing" parameter.
	 *
	 * String s[] = { "1", "300", "apple", "44" };
	 * int numbers[] = parseInt(s, 9999);
	 *
	 * numbers will contain { 1, 300, 9999, 44 }
	 */
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

	static public String[] loadStrings(File file) {
		if (!file.exists()) {
			System.err.println(file + " does not exist, loadStrings() will return null");
			return null;
		}

		InputStream is = createInput(file);
		if (is != null) {
			String[] outgoing = loadStrings(is);
			try {
				is.close();
			} catch (IOException e) {
				//e.printStackTrace();
			}
			return outgoing;
		}
		return null;
	}

	public String[] loadStrings(String filename) {
		InputStream is = createInput(filename);
		if (is != null) {
			String[] strArr = loadStrings(is);
			try {
				is.close();
			} catch (IOException e) {
				////printStackTrace(e);
			}
			return strArr;
		}

		System.err.println("The file \"" + filename + "\" " +
				"is missing or inaccessible, make sure " +
				"the URL is valid or that the file has been " +
				"added to your sketch and is readable.");
		return null;
	}

	static public String[] loadStrings(InputStream input) {
		try {
			BufferedReader reader =
					new BufferedReader(new InputStreamReader(input, "UTF-8"));
			return loadStrings(reader);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return null;
	}


	static public String[] loadStrings(BufferedReader reader) {
		try {
			String[] lines = new String[100];
			int lineCount = 0;
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (lineCount == lines.length) {
					String[] temp = new String[lineCount << 1];
					System.arraycopy(lines, 0, temp, 0, lineCount);
					lines = temp;
				}
				lines[lineCount++] = line;
			}
			reader.close();

			if (lineCount == lines.length) {
				return lines;
			}

			// resize array to appropriate amount for these lines
			String[] output = new String[lineCount];
			System.arraycopy(lines, 0, output, 0, lineCount);
			return output;

		} catch (IOException e) {
			//e.printStackTrace();
			//throw new RuntimeException("Error inside loadStrings()");
		}
		return null;
	}

	public InputStream createInput(String filename) {
		InputStream input = createInputRaw(filename);
		if (input != null) {
			// if it's gzip-encoded, automatically decode
			final String lower = filename.toLowerCase();
			if (lower.endsWith(".gz") || lower.endsWith(".svgz")) {
				try {
					// buffered has to go *around* the GZ, otherwise 25x slower
					return new BufferedInputStream(new GZIPInputStream(input));

				} catch (IOException e) {
					//printStackTrace(e);
				}
			} else {
				return new BufferedInputStream(input);
			}
		}
		return null;
	}

	public InputStream createInputRaw(String filename) {
		if (filename == null) return null;

		if (sketchPath == null) {
			System.err.println("The sketch path is not set.");
			throw new RuntimeException("Files must be loaded inside setup() or after it has been called.");
		}

		if (filename.length() == 0) {
			// an error will be called by the parent function
			//System.err.println("The filename passed to openStream() was empty.");
			return null;
		}

		// First check whether this looks like a URL
		if (filename.contains(":")) {  // at least smells like URL
			try {
				URL url = new URL(filename);
				URLConnection conn = url.openConnection();

				if (conn instanceof HttpURLConnection) {
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					// Will not handle a protocol change (see below)
					httpConn.setInstanceFollowRedirects(true);
					int response = httpConn.getResponseCode();
					// Default won't follow HTTP -> HTTPS redirects for security reasons
					// http://stackoverflow.com/a/1884427
					if (response >= 300 && response < 400) {
						String newLocation = httpConn.getHeaderField("Location");
						return createInputRaw(newLocation);
					}
					return conn.getInputStream();
				} else if (conn instanceof JarURLConnection) {
					return url.openStream();
				}
			} catch (MalformedURLException mfue) {
				// not a url, that's fine

			} catch (FileNotFoundException fnfe) {
				// Added in 0119 b/c Java 1.5 throws FNFE when URL not available.
				// http://dev.processing.org/bugs/show_bug.cgi?id=403

			} catch (IOException e) {
				// changed for 0117, shouldn't be throwing exception
				//printStackTrace(e);
				//System.err.println("Error downloading from URL " + filename);
				return null;
				//throw new RuntimeException("Error downloading from URL " + filename);
			}
		}

		InputStream stream = null;

		// Moved this earlier than the getResourceAsStream() checks, because
		// calling getResourceAsStream() on a directory lists its contents.
		// http://dev.processing.org/bugs/show_bug.cgi?id=716
		try {
			// First see if it's in a data folder. This may fail by throwing
			// a SecurityException. If so, this whole block will be skipped.
			File file = new File(dataPath(filename));
			if (!file.exists()) {
				// next see if it's just in the sketch folder
				file = sketchFile(filename);
			}

			if (file.isDirectory()) {
				return null;
			}
			if (file.exists()) {
				try {
					// handle case sensitivity check
					String filePath = file.getCanonicalPath();
					String filenameActual = new File(filePath).getName();
					// make sure there isn't a subfolder prepended to the name
					String filenameShort = new File(filename).getName();
					// if the actual filename is the same, but capitalized
					// differently, warn the user.
					//if (filenameActual.equalsIgnoreCase(filenameShort) &&
					//!filenameActual.equals(filenameShort)) {
					if (!filenameActual.equals(filenameShort)) {
						throw new RuntimeException("This file is named " +
								filenameActual + " not " +
								filename + ". Rename the file " +
								"or change your code.");
					}
				} catch (IOException e) { }
			}

			// if this file is ok, may as well just load it
			stream = new FileInputStream(file);
			if (stream != null) return stream;

			// have to break these out because a general Exception might
			// catch the RuntimeException being thrown above
		} catch (IOException ioe) {
		} catch (SecurityException se) { }

		// Using getClassLoader() prevents java from converting dots
		// to slashes or requiring a slash at the beginning.
		// (a slash as a prefix means that it'll load from the root of
		// the jar, rather than trying to dig into the package location)
		ClassLoader cl = getClass().getClassLoader();

		// by default, data files are exported to the root path of the jar.
		// (not the data folder) so check there first.
		stream = cl.getResourceAsStream("data/" + filename);
		if (stream != null) {
			String cn = stream.getClass().getName();
			// this is an irritation of sun's java plug-in, which will return
			// a non-null stream for an object that doesn't exist. like all good
			// things, this is probably introduced in java 1.5. awesome!
			// http://dev.processing.org/bugs/show_bug.cgi?id=359
			if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
				return stream;
			}
		}

		// When used with an online script, also need to check without the
		// data folder, in case it's not in a subfolder called 'data'.
		// http://dev.processing.org/bugs/show_bug.cgi?id=389
		stream = cl.getResourceAsStream(filename);
		if (stream != null) {
			String cn = stream.getClass().getName();
			if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
				return stream;
			}
		}

		try {
			// attempt to load from a local file, used when running as
			// an application, or as a signed applet
			try {  // first try to catch any security exceptions
				try {
					stream = new FileInputStream(dataPath(filename));
					if (stream != null) return stream;
				} catch (IOException e2) { }

				try {
					stream = new FileInputStream(sketchPath(filename));
					if (stream != null) return stream;
				} catch (Exception e) { }  // ignored

				try {
					stream = new FileInputStream(filename);
					if (stream != null) return stream;
				} catch (IOException e1) { }

			} catch (SecurityException se) { }  // online, whups

		} catch (Exception e) {
			//printStackTrace(e);
		}

		return null;
	}


	static public InputStream createInput(File file) {
		if (file == null) {
			throw new IllegalArgumentException("File passed to createInput() was null");
		}
		if (!file.exists()) {
			System.err.println(file + " does not exist, createInput() will return null");
			return null;
		}
		try {
			InputStream input = new FileInputStream(file);
			final String lower = file.getName().toLowerCase();
			if (lower.endsWith(".gz") || lower.endsWith(".svgz")) {
				return new BufferedInputStream(new GZIPInputStream(input));
			}
			return new BufferedInputStream(input);

		} catch (IOException e) {
			System.err.println("Could not createInput() for " + file);
			e.printStackTrace();
			return null;
		}
	}


	public PrintWriter createWriter(String filename) {
		return createWriter(saveFile(filename));
	}


	/**
	 * @nowebref
	 * I want to print lines to a file. I have RSI from typing these
	 * eight lines of code so many times.
	 */
	static public PrintWriter createWriter(File file) {
		if (file == null) {
			throw new RuntimeException("File passed to createWriter() was null");
		}
		try {
			createPath(file);  // make sure in-between folders exist
			OutputStream output = new FileOutputStream(file);
			if (file.getName().toLowerCase().endsWith(".gz")) {
				output = new GZIPOutputStream(output);
			}
			return createWriter(output);

		} catch (Exception e) {
			throw new RuntimeException("Couldn't create a writer for " +
					file.getAbsolutePath(), e);
		}
	}

	public String dataPath(String where) {
		return dataFile(where).getAbsolutePath();
	}

	public File dataFile(String where) {
		// isAbsolute() could throw an access exception, but so will writing
		// to the local disk using the sketch path, so this is safe here.
		File why = new File(where);
		if (why.isAbsolute()) return why;

		URL jarURL = getClass().getProtectionDomain().getCodeSource().getLocation();
		// Decode URL
		String jarPath;
		try {
			jarPath = jarURL.toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
		if (jarPath.contains("Contents/Java/")) {
			File containingFolder = new File(jarPath).getParentFile();
			File dataFolder = new File(containingFolder, "data");
			return new File(dataFolder, where);
		}
		// Windows, Linux, or when not using a Mac OS X .app file
		File workingDirItem =
				new File(sketchPath + File.separator + "data" + File.separator + where);
		//    if (workingDirItem.exists()) {
		return workingDirItem;
		//    }
		//    // In some cases, the current working directory won't be set properly.
	}


	/**
	 * @nowebref
	 * I want to print lines to a file. Why am I always explaining myself?
	 * It's the JavaSoft API engineers who need to explain themselves.
	 */
	static public PrintWriter createWriter(OutputStream output) {
		BufferedOutputStream bos = new BufferedOutputStream(output, 8192);
		OutputStreamWriter osw =
				new OutputStreamWriter(bos, StandardCharsets.UTF_8);
		return new PrintWriter(osw);
	}

	public String savePath(String where) {
		if (where == null) return null;
		String filename = sketchPath(where);
		createPath(filename);
		return filename;
	}


	/**
	 * Identical to savePath(), but returns a File object.
	 */
	public File saveFile(String where) {
		return new File(savePath(where));
	}

	static public void createPath(String path) {
		createPath(new File(path));
	}


	static public void createPath(File file) {
		try {
			String parent = file.getParent();
			if (parent != null) {
				File unit = new File(parent);
				if (!unit.exists()) unit.mkdirs();
			}
		} catch (SecurityException se) {
			System.err.println("You don't have permissions to create " +
					file.getAbsolutePath());
		}
	}

	static protected String calcSketchPath() {
		// try to get the user folder. if running under java web start,
		// this may cause a security exception if the code is not signed.
		// http://processing.org/discourse/yabb_beta/YaBB.cgi?board=Integrate;action=display;num=1159386274
		String folder = null;
		try {
			folder = System.getProperty("user.dir");

			URL jarURL =
					FXApp.class.getProtectionDomain().getCodeSource().getLocation();
			// Decode URL
			String jarPath = jarURL.toURI().getSchemeSpecificPart();

			// Workaround for bug in Java for OS X from Oracle (7u51)
			// https://github.com/processing/processing/issues/2181
			//if (FXApp.platform == MACOSX) {
			if (MACOSX == MACOSX) {
				if (jarPath.contains("Contents/Java/")) {
					String appPath = jarPath.substring(0, jarPath.indexOf(".app") + 4);
					File containingFolder = new File(appPath).getParentFile();
					folder = containingFolder.getAbsolutePath();
				}
			} else {
				// Working directory may not be set properly, try some options
				// https://github.com/processing/processing/issues/2195
				if (jarPath.contains("/lib/")) {
					// Windows or Linux, back up a directory to get the executable
					folder = new File(jarPath, "../..").getCanonicalPath();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folder;
	}

	private String sketchPath;

	public String sketchPath() {
		if (sketchPath == null) {
			sketchPath = calcSketchPath();
		}
		return sketchPath;
	}


	/**
	 * Prepend the sketch folder path to the filename (or path) that is
	 * passed in. External libraries should use this function to save to
	 * the sketch folder.
	 * <p/>
	 * Note that when running as an applet inside a web browser,
	 * the sketchPath will be set to null, because security restrictions
	 * prevent applets from accessing that information.
	 * <p/>
	 * This will also cause an error if the sketch is not inited properly,
	 * meaning that init() was never called on the PApplet when hosted
	 * my some other main() or by other code. For proper use of init(),
	 * see the examples in the main description text for PApplet.
	 */
	public String sketchPath(String where) {
		if (sketchPath() == null) {
			return where;
		}
		// isAbsolute() could throw an access exception, but so will writing
		// to the local disk using the sketch path, so this is safe here.
		// for 0120, added a try/catch anyways.
		try {
			if (new File(where).isAbsolute()) return where;
		} catch (Exception e) { }

		return sketchPath() + File.separator + where;
	}


	public File sketchFile(String where) {
		return new File(sketchPath(where));
	}

    public BufferedReader createReader(String filename) {
        InputStream is = createInput(filename);
        if (is == null) {
          System.err.println("The file \"" + filename + "\" " +
                           "is missing or inaccessible, make sure " +
                           "the URL is valid or that the file has been " +
                           "added to your sketch and is readable.");
          return null;
        }
        return createReader(is);
      }
    
    
      /**
       * @nowebref
       */
      static public BufferedReader createReader(File file) {
        try {
          InputStream is = new FileInputStream(file);
          if (file.getName().toLowerCase().endsWith(".gz")) {
            is = new GZIPInputStream(is);
          }
          return createReader(is);
    
        } catch (IOException e) {
          // Re-wrap rather than forcing novices to learn about exceptions
          throw new RuntimeException(e);
        }
      }
    
    
      /**
       * @nowebref
       * I want to read lines from a stream. If I have to type the
       * following lines any more I'm gonna send Sun my medical bills.
       */
      static public BufferedReader createReader(InputStream input) {
        InputStreamReader isr =
          new InputStreamReader(input, StandardCharsets.UTF_8);
    
        BufferedReader reader = new BufferedReader(isr);
        // consume the Unicode BOM (byte order marker) if present
        try {
          reader.mark(1);
          int c = reader.read();
          // if not the BOM, back up to the beginning again
          if (c != '\uFEFF') {
            reader.reset();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        return reader;
      }

      static public boolean[] concat(boolean[] a, boolean[] b) {
        boolean[] c = new boolean[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
      }
    
      static public byte[] concat(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
      }
    
      static public char[] concat(char[] a, char[] b) {
        char[] c = new char[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
      }
    
      static public int[] concat(int[] a, int[] b) {
        int[] c = new int[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
      }
    
      static public float[] concat(float[] a, float[] b) {
        float[] c = new float[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
      }
    
      static public String[] concat(String[] a, String[] b) {
        String[] c = new String[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
      }
    
      static public Object concat(Object a, Object b) {
        Class<?> type = a.getClass().getComponentType();
        int alength = Array.getLength(a);
        int blength = Array.getLength(b);
        Object outgoing = Array.newInstance(type, alength + blength);
        System.arraycopy(a, 0, outgoing, 0, alength);
        System.arraycopy(b, 0, outgoing, alength, blength);
        return outgoing;
      }
}