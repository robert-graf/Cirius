package editor.rpg.nexus;

import de.rubberOo.javascript.gui.scriptPanel.ScriptPanel;
import editor.css.CSSEditor;
import gui.AAufbau;
import img.Icons;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import toolbars.Toolbar;
import basic.CSSClasse;
import basic.TreeRenderer;

public enum NexusPanel implements ActionListener {
	THAT();
	JDialog dialog;
	JPanel panel = new JPanel();
	JMenuBar m = new JMenuBar();
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (dialog == null) {
			dialog = new JDialog(AAufbau.f);
			dialog.setLayout(new BorderLayout());
			dialog.add(panel);
			dialog.setSize(200, 500);
			m.add(Toolbar.get(DialogFunktion.name));
			dialog.setJMenuBar(m);
		}
		dialog.setVisible(!dialog.isVisible());
	}

	JTree tree;
	final MainNodeTree<String, CSSClasse> css;
	private NexusPanel() {
		panel.setLayout(new BorderLayout());
		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Nexus", true);
		tree = new JTree(node1);
		final ScriptNode scirpt = new ScriptNode("Scripte", tree);
		node1.add(scirpt);
		css = new MainNodeTree<>("StyleSheet",CSSEditor.THAT.CSSCList, tree,
				Icons.getImage("css", 16),Icons.getImage("css_go", 16));
		node1.add(css);
		tree.getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {

					@Override
					public void valueChanged(TreeSelectionEvent e) {
						TreePath p = e.getPath();
						if (p.getPathCount() == 3) {
							if (scirpt.equals(p.getPathComponent(1))) {
//								int i = ((MutableTreeNode) p.getPathComponent(1))
//										.getIndex((TreeNode)p.getLastPathComponent());
								ScriptPanel.THAT.getGlobal().setCurrendScript(p.getLastPathComponent().toString());
							}
							if (css.equals(p.getPathComponent(1))) {
								CSSEditor.THAT.setCssClasse(p.getLastPathComponent().toString());
							}
						}
					}
				});
		// NODES
		tree.expandRow(0);
		tree.setCellRenderer(new TreeRenderer());
		panel.add(new JScrollPane(tree));
	}
	public void updateCSS() {
		css.setList(CSSEditor.THAT.CSSCList);
	}
}
