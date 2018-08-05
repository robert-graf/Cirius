package editor.rpg.nexus;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.tree.DefaultTreeModel;

import basic.ListenList;
import basic.MyTreeNode;

@SuppressWarnings("serial")
public class MainNode<T> extends MyTreeNode<String> implements ListDataListener {
	private JTree tree;
	private ListenList<T> list;

	public MainNode(String s, JTree tree) {
		super(s, true);
		this.tree = tree;
	}

	public void setList(ListenList<T> list) {
		if (list != null) {
			list.removeListDataListener(this);
		}
		list.addListDataListener(this);
		this.list = list;
		this.removeAllChildren();
		for (T f : list.getArray()) {
			this.add(new MyTreeNode<T>(f));
		}
		update();
	}

	public ListenList<T> getList() {
		return list;
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		this.remove(e.getIndex0());
		update();
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
		ArrayList<?> list = (ArrayList<?>) e.getSource();
		this.add(new MyTreeNode<Object>(list.get(e.getIndex0())));
		update();
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		ArrayList<?> list = (ArrayList<?>) e.getSource();
		this.set(e.getIndex0(), new MyTreeNode<Object>(list.get(e.getIndex0())));
		update();
	}

	void update() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((DefaultTreeModel) tree.getModel())
						.nodeStructureChanged(MainNode.this);
			}
		});
	}
}
