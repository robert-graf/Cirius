package editor.tabelle;

public abstract class OneLine<T> {
	private String key;
	private T value;
	private boolean editable = true;

	public OneLine(String key, T value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public T getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	public void setValueByUser(Object value) {
		if(value instanceof Number){
			if(this.value instanceof Integer){
				valueChanges((T)(Object)((Number)value).intValue());
			}
			else{
				valueChanges((T)(Object)((Number)value).doubleValue());
			}
		}else{
			valueChanges((T)value);
		}
		
	};

	public abstract void valueChanges(T value);

	public void setValue(T value) {
		this.value = (T) value;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditable() {
		return editable;
	}

	@Override
	public String toString() {
		return getValue() + "";
	}
}
