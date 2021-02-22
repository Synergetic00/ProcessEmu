package event;


public class Event {
	protected Object nativeObject;

	protected long millis;
	protected int action;

	static public final int SHIFT = 1 << 0;
	static public final int CTRL  = 1 << 1;
	static public final int META  = 1 << 2;
	static public final int ALT   = 1 << 3;
	protected int modifiers;

	static public final int KEY = 1;
	static public final int MOUSE = 2;
	static public final int TOUCH = 3;
	protected int flavor;


	public Event(Object nativeObject, long millis, int action, int modifiers) {
		this.nativeObject = nativeObject;
		this.millis = millis;
		this.action = action;
		this.modifiers = modifiers;
	}

	public int getFlavor() {
		return flavor;
	}

    public Object getNative() {
		return nativeObject;
	}

	public long getMillis() {
		return millis;
	}

	public int getAction() {
		return action;
	}

	public int getModifiers() {
		return modifiers;
	}

	public boolean isShiftDown() {
		return (modifiers & SHIFT) != 0;
	}

	public boolean isControlDown() {
		return (modifiers & CTRL) != 0;
	}

	public boolean isMetaDown() {
		return (modifiers & META) != 0;
	}

	public boolean isAltDown() {
		return (modifiers & ALT) != 0;
	}
}