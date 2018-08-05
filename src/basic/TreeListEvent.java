package basic;

import java.util.TreeMap;

public class TreeListEvent<K,V> {
	
	private TreeMap<K, V> map;
	private K key;
	private V value;
	
	public TreeListEvent(TreeMap<K,V> map,K key,V value) {
		this.map = map;
		this.key = key;
		this.value = value;
	}
	public TreeListEvent(TreeMap<K,V> map,K key) {
		this.map = map;
		this.key = key;
	}
	public TreeListEvent(TreeMap<K,V> map) {
		this.map = map;
	}
	public TreeMap<K, V> getMap() {
		return map;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		if(value != null){
		return value;
		}
		return map.get(key);
	}
}
