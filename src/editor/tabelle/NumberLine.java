package editor.tabelle;

public abstract class NumberLine<T extends Number> extends OneLine<T> {
	Comparable<?> minimum;
	Comparable<?> maximum;
	Number stepSize;

	public NumberLine(String key, T value) {
		this(key, value, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
	}

	public NumberLine(String key, T value, Comparable<?> minimum,
			Comparable<?> maximum, Number stepSize) {
		super(key, value);
		this.minimum = minimum;
		this.maximum = maximum;
		this.stepSize = stepSize;
	}

	public void setMaximum(Comparable<?> maximum) {
		this.maximum = maximum;
	}

	public void setMinimum(Comparable<?> minimum) {
		this.minimum = minimum;
	}

	public void setStepSize(Number stepSize) {
		this.stepSize = stepSize;
	}

	public Comparable<?> getMaximum() {
		return maximum;
	}

	public Comparable<?> getMinimum() {
		return minimum;
	}

	public Number getStepSize() {
		return stepSize;
	}
}
