package basic;

import java.util.TreeMap;
import java.util.Vector;

public class ListenTreeList<K,V>{
	Vector<TreeListListener<K,V>> listener = new Vector<>();
	TreeMap<K,V> map = new TreeMap<>();

	public void addListDataListener(TreeListListener<K,V> arg0) {
		listener.add(arg0);
	}

	public void removeListDataListener(TreeListListener<K,V> arg0) {
		listener.remove(arg0);
	}

	public V getElementAt(K arg0) {
		return map.get(arg0);
	}
	
	public int getSize() {
		return map.size();
	}


	public V get(K arg1) {
		return map.get(arg1);
	}

	public void put(K key, V value) {
		map.put(key, value);
		TreeListEvent<K, V> event = new TreeListEvent<K, V>(map, key, value);
		for (TreeListListener<K,V> l : listener) {
			l.listenPut(event);
		}
	}

	public void remove(K key) {
		
		map.remove(key);
		TreeListEvent<K, V> event = new TreeListEvent<K, V>(map, key);
		for (TreeListListener<K,V> l : listener) {
			l.listenRemove(event);
		}
	}

	public TreeMap<K,V> getMap() {
		return map;
	}

	public void clear() {
		TreeListEvent<K, V> event = new TreeListEvent<K, V>(map);
		for (TreeListListener<K,V> l : listener) {
			l.listenClear(event);
		}
		map.clear();
	}
}
