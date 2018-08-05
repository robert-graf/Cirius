package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public enum NeueDatei {
	THAT();
	// TODO outsorcen
	String workspace = Messages.getString("NeueDatei.workspace"); //$NON-NLS-1$

	private JTextField pfad;
	private JTabbedPane pane = new JTabbedPane();
	private final ArrayList<DateiTyp> typeList = new ArrayList<NeueDatei.DateiTyp>();
	public final DateiTyp sprite = new DateiTyp(
			Messages.getString("NeueDatei.Sprite"), //$NON-NLS-1$
			Messages.getString("NeueDatei.Sprite.Explain"), Messages.getString("NeueDatei.Sprite.Pfad"), Messages.getString("NeueDatei.Sprite.Sufix")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public final DateiTyp bild = new DateiTyp(
			Messages.getString("NeueDatei.Pic"), //$NON-NLS-1$
			Messages.getString("NeueDatei.Pic.Explain"), Messages.getString("NeueDatei.Pic.Pfad"), Messages.getString("NeueDatei.Pic.Sufix")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public final DateiTyp rpg = new DateiTyp(
			Messages.getString("NeueDatei.RPG"), //$NON-NLS-1$
			Messages.getString("NeueDatei.RPG.Explain"), Messages.getString("NeueDatei.RPG.Pfad"), Messages.getString("NeueDatei.RPG.Sufix")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public final DateiTyp tsx = new DateiTyp(
			Messages.getString("NeueDatei.TSX"), //$NON-NLS-1$
			Messages.getString("NeueDatei.TSX.Explain"), Messages.getString("NeueDatei.TSX.Pfad"), Messages.getString("NeueDatei.TSX.Sufix")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	public final DateiTyp tmx = new DateiTyp(
			Messages.getString("NeueDatei.TMX"), //$NON-NLS-1$
			Messages.getString("NeueDatei.TMX.Explain"), Messages.getString("NeueDatei.TMX.Pfad"), Messages.getString("NeueDatei.TMX.Sufix"));
	public final DateiTyp  particel = new DateiTyp("Particel", "Erstelle ein neu Particel anordung", "%s\\%s\\particel", ".pesx");
//			NeueDatei.RPG=Karte 
//			NeueDatei.RPG.Explain=Karten sind Einzelabschnitte im Spiel 
//			NeueDatei.RPG.Pfad=%s\\%s\\map
//			NeueDatei.RPG.Sufix=.rpg
	private JDialog dialog;
	private File f;
	private String name, Project;

	private void init() {
		dialog = new JDialog(AAufbau.f);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		pfad = new JTextField();
		p.add(pfad, BorderLayout.NORTH);
		p.add(pane);
		JButton button = new JButton(Messages.getString("NeueDatei.Create")); //$NON-NLS-1$
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				start();
			}
		});
		p.add(button, BorderLayout.SOUTH);
		dialog.add(p);

		pane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				NeueDatei.THAT.Dialog(((JTabbedPane) e.getSource())
						.getSelectedComponent().getName(), Project);
			}
		});
	}

	public void Neu(String name, TreePath leadSelectionPath) {
		// PFAD
		DefaultMutableTreeNode node = null;
		DefaultTreeModel model = (DefaultTreeModel) Tree.THAT.tree
				.getModel();
		if (leadSelectionPath != null) {
			f = new File(leadSelectionPath.toString()
					.replace("[", "").replace("]", "").replace(", ", "\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			node = (DefaultMutableTreeNode) leadSelectionPath
					.getLastPathComponent();
		} else {
			f = new File(model.getRoot().toString());
			node = (DefaultMutableTreeNode) model.getRoot();
		}

		if (name.equals(Messages.getString("NeueDatei.Folder"))) {neuerOrdner(model, node, f);} //$NON-NLS-1$
		else {
			if (leadSelectionPath == null) {
				Dialog(name, model.getChild(model.getRoot(), 0).toString());
				return;
			}
			Object[] o = leadSelectionPath.getPath();
			if (o.length == 0) {
				Dialog(name, model.getChild(model.getRoot(), 0).toString());
				return;
			}
			Dialog(name, o[1].toString());
		}
	}

	private void Dialog(String name, String project) {
		if (dialog == null) {
			init();
		}
		if (!dialog.isVisible()) {
			pfad.setText(f.getPath());
			dialog.setSize(400, 400);
			dialog.setLocationRelativeTo(AAufbau.f);
			dialog.setVisible(true);
		}
		this.name = name;
		this.Project = project;
		if (f == null) {
			f = new File(pfad.getText());
		}
		if (f.getParent() == null) {
			f = new File(workspace + project + '\\');
		}
		for (DateiTyp typ : typeList) {
			if (name.equals(typ.getName())) {
				if (!f.getParent().startsWith(typ.getFolder(project))) {
					String s = typ.getSufix();
					if (f.isFile()) {
						s = f.getName() + s;
					}
					f = new File(typ.getFolder(project) + '\\' + s);
					pfad.setText(f.getPath());
					pane.setSelectedIndex(typ.getId());
				}
				return;
			}
		}
		System.out
				.println(Messages.getString("NeueDatei.Error.NO_Support") + name);//$NON-NLS-1$
	}

	private void start() {
		try {
			File file = null;
			file = new File(pfad.getText());
			// SUFIX kontolle; Ist Leer Kontrolle
			for (DateiTyp typ : typeList) {
				if (name.equals(typ.getName())) {
					if (!file.getName().endsWith(typ.getSufix())) {
						file = new File(file.getPath() + typ.getSufix());
					}
					if (file.getName().startsWith(".") //$NON-NLS-1$
							|| file.getName().isEmpty()) {
						JOptionPane
								.showMessageDialog(
										AAufbau.f,
										Messages.getString("NeueDatei.Error.No_Filename_error") + file.getName()); //$NON-NLS-1$
						return;
					}
				}
			}
			if (name.equals(Messages.getString("NeueDatei.Sprite")) || name.equals(Messages.getString("NeueDatei.RPG"))) { //$NON-NLS-1$ //$NON-NLS-2$
				file.createNewFile();
			}
			Öffnen.InitAndOpen(file);
			
		} catch (Exception ex) {
			
			
			JOptionPane
					.showMessageDialog(
							AAufbau.f,
							Messages.getString("NeueDatei.Error") + pane.getSelectedComponent().getName()+"\n"+ex.getMessage()); //$NON-NLS-1$
			ex.printStackTrace();
		}
		
		// pfad
	}

	private void neuerOrdner(DefaultTreeModel model,
			DefaultMutableTreeNode node, File f) {
		String s = JOptionPane.showInputDialog(Messages
				.getString("NeueDatei.NewFolder")); //$NON-NLS-1$
		if (s == null) {
			return;
		}
		model.insertNodeInto(new DefaultMutableTreeNode(s), node,
				node.getChildCount());
		new File(f.getPath() + '\\' + s).mkdirs(); //$NON-NLS-1$
	}

	static int i = 0;

	public class DateiTyp extends JPanel {
		private String name, pfad, sufix;

		int id = i++;

		public DateiTyp(String name, String text, String pfad, String sufix) {
			this.name = name;
			this.pfad = pfad;
			this.sufix = sufix;
			pane.add(name, this);
			this.setName(name);
			this.setLayout(new BorderLayout());
			JEditorPane ep = new JEditorPane();
			ep.setContentType(Messages.getString("NeueDatei.TEXTTYP")); //$NON-NLS-1$
			ep.setText(text);
			this.add(ep);
			ep.setEditable(false);
			typeList.add(this);
		}

		public String getText() {
			return name;
		}

		public String getFolder(String project) {
			return String.format(pfad, workspace, project);
		}

		public int getId() {
			return id;
		}

		public String getSufix() {
			return sufix;
		}
	}

}
