package editor.rpg.nexus;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;

import basic.ListenTreeList;
import basic.MyTreeNode;
import basic.TreeListEvent;
import basic.TreeListListener;

@SuppressWarnings("serial")
public class MainNodeTree<K,V> extends MyTreeNode<String> implements TreeListListener<K, V> {
	private JTree tree;
	private ListenTreeList<K,V> list;
	Icon i, i2;
	public MainNodeTree(String s,ListenTreeList<K, V> list , JTree tree,Icon i,Icon i2) {
		super(s, true);
		this.tree = tree;
		this.i = i;
		this.i2= i2;
		setList(list);
		setIcon(i);
	}

	public void setList(ListenTreeList<K,V> list) {
		if (this.list != null) {
			this.list.removeListDataListener(this);
		}
		if(list == null){
			this.list = null;
			return;
		}
		list.addListDataListener(this);
		this.list = list;
		this.removeAllChildren();
		for (K f : list.getMap().keySet()) {
			this.add(new MyTreeNode<K>(f).setIcon(i2));
		}
		update();
	}

	public ListenTreeList<K,V> getList() {
		return list;
	}
	@Override
	public void listenRemove(TreeListEvent<K, V> e) {
		this.children.remove(e.getKey());
		update();
	}
	@Override
	public void listenPut(TreeListEvent<K, V> e) {
		this.add(new MyTreeNode<K>(e.getKey()).setIcon(i2));
		update();
	}
	@Override
	public void listenClear(TreeListEvent<K, V> arg0) {
		this.children.clear();
		update();
	}
	void update() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((DefaultTreeModel) tree.getModel())
						.nodeStructureChanged(MainNodeTree.this);
			}
		});
	}
}
