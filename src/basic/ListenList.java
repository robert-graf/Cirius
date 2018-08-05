package basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ListenList<T> implements ListModel<T> {
	Vector<ListDataListener> listener = new Vector<>();
	ArrayList<T> array = new ArrayList<>();

	@Override
	public void addListDataListener(ListDataListener arg0) {
		listener.add(arg0);
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		listener.remove(arg0);
	}

	@Override
	public T getElementAt(int arg0) {
		return array.get(arg0);
	}

	@Override
	public int getSize() {
		return array.size();
	}

	public void set(int arg1, T arg0) {
		array.set(arg1, arg0);
		ListDataEvent event = new ListDataEvent(array,
				ListDataEvent.CONTENTS_CHANGED, arg1, arg1);
		for (ListDataListener l : listener) {
			l.contentsChanged(event);
		}
	}

	public T get(int arg1) {
		return array.get(arg1);
	}

	public void add(int arg1, T arg0) {
		array.add(arg1, arg0);
		ListDataEvent event = new ListDataEvent(array,
				ListDataEvent.INTERVAL_ADDED, arg1, arg1);
		for (ListDataListener l : listener) {
			l.intervalAdded(event);
		}
	}

	public void add(T arg0) {
		array.add(arg0);
		ListDataEvent event = new ListDataEvent(array,
				ListDataEvent.INTERVAL_ADDED, array.size() - 1,
				array.size() - 1);
		for (ListDataListener l : listener) {
			l.intervalAdded(event);
		}
	}

	public void remove(T arg0) {
		int i = array.indexOf(arg0);
		array.remove(i);
		ListDataEvent event = new ListDataEvent(array,
				ListDataEvent.INTERVAL_REMOVED, i, i);
		for (ListDataListener l : listener) {
			l.intervalRemoved(event);
		}
	}

	public void remove(int i) {
		// T arg0 = array.get(i);
		array.remove(i);
		ListDataEvent event = new ListDataEvent(array,
				ListDataEvent.INTERVAL_REMOVED, i, i);
		for (ListDataListener l : listener) {
			l.intervalRemoved(event);
		}
	}

	public ArrayList<T> getArray() {
		return array;
	}

	public void clear() {
		ListDataEvent event = new ListDataEvent(array,
				ListDataEvent.INTERVAL_REMOVED, 0, array.size());
		for (ListDataListener l : listener) {
			l.intervalRemoved(event);
		}
		array.clear();
	}

	public void add(Collection<T> map) {
		for(T t : map){
			add(t);
		}
	}
}
