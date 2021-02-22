package event;

public class KeyEvent extends Event {
	static public final int PRESS = 1;
	static public final int RELEASE = 2;
	static public final int TYPE = 3;

	char key;
	int keyCode;
	boolean isAutoRepeat;

	public KeyEvent(Object nativeObject, long millis, int action, int modifiers, char key, int keyCode) {
		super(nativeObject, millis, action, modifiers);
		this.flavor = KEY;
		this.key = key;
		this.keyCode = keyCode;
	}

	public KeyEvent(Object nativeObject, long millis, int action, int modifiers, char key, int keyCode, boolean isAutoRepeat) {
		super(nativeObject, millis, action, modifiers);
		this.flavor = KEY;
		this.key = key;
		this.keyCode = keyCode;
		this.isAutoRepeat = isAutoRepeat;
	}

	public char getKey() {
		return key;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public boolean isAutoRepeat() {
		return isAutoRepeat;
	}
}