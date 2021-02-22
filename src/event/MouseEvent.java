package event;

public class MouseEvent extends Event {
	static public final int PRESS = 1;
	static public final int RELEASE = 2;
	static public final int CLICK = 3;
	static public final int DRAG = 4;
	static public final int MOVE = 5;
	static public final int ENTER = 6;
	static public final int EXIT = 7;
	static public final int WHEEL = 8;

	protected int x, y;
	protected int button;
	protected int count;

	public MouseEvent(Object nativeObject, long millis, int action, int modifiers, int x, int y, int button, int count) {
		super(nativeObject, millis, action, modifiers);
		this.flavor = MOUSE;
		this.x = x;
		this.y = y;
		this.button = button;
		this.count = count;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

    public int getButton() {
		return button;
	}

    @Deprecated
	public int getClickCount() {
		//return (int) amount; //clickCount;
		return count;
	}

    @Deprecated
	public float getAmount() {
		//return amount;
		return count;
	}

    public int getCount() {
		return count;
	}

    private String actionString() {
		switch (action) {
		default:
			return "UNKNOWN";
		case CLICK:
			return "CLICK";
		case DRAG:
			return "DRAG";
		case ENTER:
			return "ENTER";
		case EXIT:
			return "EXIT";
		case MOVE:
			return "MOVE";
		case PRESS:
			return "PRESS";
		case RELEASE:
			return "RELEASE";
		case WHEEL:
			return "WHEEL";
		}
	}

	@Override
	public String toString() {
		return String.format("<MouseEvent %s@%d,%d count:%d button:%d>",
				actionString(), x, y, count, button);
	}
}