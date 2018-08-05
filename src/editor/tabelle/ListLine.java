package editor.tabelle;

import java.util.ArrayList;

import javax.swing.JComboBox;

/**
 * Gibt eine JComoBox wieder
 * 
 * */
public abstract class ListLine<T> extends OneLine<T> {
	public ListLine(String key, T value, ArrayList<?> list) {
		super(key, value);
		this.list = list;
	}


	


	private ArrayList<?> list;

	public void setList(ArrayList<?> list) {
		this.list = list;
	}

	public ArrayList<?> getList() {
		return list;
	}

	public JComboBox<Object> getModel(JComboBox<Object> box) {
		box.removeAllItems();
		for (Object o : getList()) {
			box.addItem(o);
		}
		box.setSelectedItem(getValue());
		return box;
	}
}
