package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
public class DefaultTree extends JTree {
	File f;
	String suche;
	DefaultMutableTreeNode Stamm;
	File selected;
	public static ArrayList<String> Erlaubt;

	public DefaultTree(File f, ArrayList<String> e) {
		this(f);
		Erlaubt = e;
	}

	public DefaultTree(final File f) {
		super(new MyTreeNode(f));
		Stamm = (DefaultMutableTreeNode) (TreeNode) getModel().getRoot();
		this.f = f;
		f.mkdirs();
		addMouseListener(new DragListener());
		setBackground(new Color(200, 200, 200));
		setBorder(null);
		setCellRenderer(new Tree.Render());
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2 && selected != null) {
					dropit(e);
				}
			}
		});
		getSelectionModel().addTreeSelectionListener(
				new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
						selected = ((MyTreeNode) getSelectionPath()
								.getLastPathComponent()).f;
						// TODO
					}
				});
	}

	public void start() {
		durchsucheOrdner((DefaultMutableTreeNode) Stamm, f.listFiles(), 0);
		int cont = 0;
		while (getRowCount() != cont) {
			expandRow(cont);
			cont++;
		}
	}

	public File getSelectedFile() {
		return selected;
	}

	private void durchsucheOrdner(DefaultMutableTreeNode stamm, File[] files,
			int cont) {
		for (File f : files) {
			if (f.isDirectory()) {
				DefaultMutableTreeNode Ordner = new MyTreeNode(f);
				durchsucheOrdner(Ordner, f.listFiles(), cont + 1);
				stamm.add(Ordner);
			} else {
				if (Erlaubt == null
						|| Erlaubt.contains(f.getName().substring(
								f.getName().lastIndexOf(".")))) {
					stamm.add(new MyTreeNode(f));
				}
			}
		}
	}

	static JScrollPane dialogtreeScroll = new JScrollPane();
	static DefaultTree dialogtree;
	static JDialog d;
	static JPanel p;
	public static JButton update;

	public static void getDefaultTree(ArrayList<String> Erlaubnis,
			JComponent comp) {
		if (d == null) {
			d = new JDialog(AAufbau.f);
			p = new JPanel();
			p.setLayout(new BorderLayout());
			p.add(dialogtreeScroll);
			update = new JButton("Update");
			update.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dialogtree = new DefaultTree(new File("res/"));
					dialogtree.start();
					dialogtreeScroll.getViewport().add(dialogtree);
					p.repaint();
					p.validate();
					d.pack();
				}
			});
			p.add(update, BorderLayout.SOUTH);
			d.add(p);
			setComp(comp);
		}
		dialogtree = new DefaultTree(new File("res/"));
		Erlaubt = Erlaubnis;
		dialogtree.start();
		dialogtreeScroll.getViewport().add(dialogtree);
		d.pack();
		d.setVisible(true);
	}

	private static JScrollPane LastJsp = new JScrollPane();

	public static void setComp(JComponent comp) {
		if (d == null) {
			return;
		}
		if (comp != null) {
			LastJsp.getViewport().removeAll();
			LastJsp.getViewport().add(comp);
			d.add(LastJsp, BorderLayout.EAST);
		}
	}

	public void dropit(MouseEvent e) {
		boolean open = true;
		if (getSelectedFile() == null) {
		} else if (AAufbau.f.CurrendPanel instanceof Drop) {
			open = ((Drop) AAufbau.f.CurrendPanel).dropEvent(getSelectedFile());
		}
		if (open) {
			Öffnen.Open(getSelectedFile(), e);
		}
	}

	static class MyTreeNode extends DefaultMutableTreeNode {
		File f;

		public MyTreeNode(File f) {
			super(f.getName());
			this.f = f;
		}
	}

	class DragListener implements MouseListener {

		boolean b = false;

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			b = false;
		}

		public void mouseExited(MouseEvent e) {
			b = true;
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
			if (b) {
				dropit(e);
			}
		}

	}
}