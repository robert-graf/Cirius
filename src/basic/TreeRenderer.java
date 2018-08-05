package basic;

import java.awt.Component;

import javax.swing.JTree;

@SuppressWarnings("serial")
public class TreeRenderer extends javax.swing.tree.DefaultTreeCellRenderer{
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		if(value instanceof MyTreeNode<?>){
			setText(value.toString());
			setIcon(((MyTreeNode<?>) value).getIcon());
			return this;
		}
		
		return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
	}
}
