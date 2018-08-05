package editor.tabelle;

public class NumberLineAdapter<T extends Number> extends NumberLine<T> {
	public NumberLineAdapter(String key, T value, Comparable<?> minimum,
			Comparable<?> maximum, Number stepSize) {
		super(key, value, minimum, maximum, stepSize);
	}

	public void valueChanges(T value) {
		setValue(value);
	};
}
