package basic;


public interface TreeListListener<K, V> {
	public void listenPut(TreeListEvent<K, V> arg0);

	public void listenRemove(TreeListEvent<K, V> arg0);
	
	public void listenClear(TreeListEvent<K, V> arg0);
}
