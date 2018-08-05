package basic;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("serial")
public class MyTreeNode<T> extends DefaultMutableTreeNode {
	T o;

	public MyTreeNode(T f, boolean b) {
		super(f, b);
		this.o = f;
	}

	public MyTreeNode(T f) {
		super(f);
		this.o = f;
	}

	public MyTreeNode() {
		super();
	}

	public T getObject() {
		return o;
	}

	@SuppressWarnings("unchecked")
	public void set(int index, DefaultMutableTreeNode t) {
		t.setParent(this);
		children.set(index, t);
	}

	@SuppressWarnings("unchecked")
	void add(int index, DefaultMutableTreeNode t) {
		t.setParent(this);
		children.add(index, t);
	}

	@SuppressWarnings("unchecked")
	void add(DefaultMutableTreeNode t) {
		t.setParent(this);
		children.add(t);
	}
	Icon i;// = Tree.Render.Ordner;
	public MyTreeNode<T> setIcon(Icon i){
		this.i = i;
		return this;
	}
	public Icon getIcon() {
		return i;
	}
}
