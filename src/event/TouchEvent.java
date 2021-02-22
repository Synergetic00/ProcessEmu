package event;

public class TouchEvent extends Event {

	public TouchEvent(Object nativeObject, long millis, int action, int modifiers) {
		super(nativeObject, millis, action, modifiers);
		this.flavor = TOUCH;
	}
}