package editor.tabelle;

public class ObjectContainerForStringTyping <T,S> {
	
	private String s;
	private T object;
	private S object2;

	public S getObject2() {
		return object2;
	}

	public void setObject2(S object2) {
		this.object2 = object2;
	}

	public String getText() {
		return s;
	}

	public T getObject() {
		return object;
	}

	public void setText(String s) {
		this.s = s;
	}

	public void setObject(T object) {
		this.object = object;
	}
	public  ObjectContainerForStringTyping(String s, T object) {
		this.s = s;
		this.object = object;
	}
	public  ObjectContainerForStringTyping(String s, T object,S object2) {
		this.s = s;
		this.object = object;
		this.object2 = object2;
	}
	@Override
	public String toString() {
		return s;
	}

}
