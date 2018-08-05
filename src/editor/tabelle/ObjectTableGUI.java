package editor.tabelle;

import gui.AAufbau;
import interfaces.ExtendsDefaultEditor;
import interfaces.HasPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public enum ObjectTableGUI implements ActionListener {
	THAT();
	private JPanel d;
	private JButton export = new JButton("Zu Fenster");
	private ObjectTable table;
	private JDialog dialog;

	public void show() {
		if (d == null) {
			init();
		}
		d.setVisible(!d.isVisible());
		if (dialog != null && dialog.isVisible()) {
			dialog.setVisible(true);
			export.doClick();
		}
	}

	public void show(Object o) {
		if (d == null) {
			init();
		}
		show();
		table.setObject(o);
	}

	JPanel old;

	public void update(Object o) {
		if (d == null) {
			return;
		}
		if (table != null) {
			table.setObject(o);
		}
		if (old != null) {
			d.remove(old);
			old = null;
			validate();
		}
		if (o instanceof HasPanel) {
			old = ((HasPanel) o).getPanel();
			d.add(old, BorderLayout.NORTH);
			validate();
		}
	}

	private void validate() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				d.validate();
				d.repaint();
			}
		});
	}

	private static Object[][] o = new Object[2][2];
	private static Object[] o1 = { "Schlüssel", "Wert" };

	private void init() {
		d = new JPanel();
		d.setLayout(new BorderLayout());
		table = new ObjectTable(o, o1);
		table.setPreferredScrollableViewportSize(new Dimension(200, 500));
		d.add(new JScrollPane(table));
		AAufbau.f.add(d, BorderLayout.EAST);
		AAufbau.f.validate();
		d.setVisible(false);
		export.addActionListener(new ActionListener() {
			boolean b = true;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (b) {
					if (dialog == null) {
						dialog = new JDialog(AAufbau.f);
						dialog.add(d);
						dialog.pack();
						dialog.setPreferredSize(null);
					} else {
						dialog.add(d);
					}
					b = false;
					AAufbau.f.validate();
					dialog.setVisible(true);
					((AbstractButton) e.getSource()).setText("Einbinden");
				} else {
					dialog.setVisible(false);
					AAufbau.f.add(d, BorderLayout.EAST);
					AAufbau.f.validate();
					b = true;
				}
			}
		});
		d.add(export, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent arg0) {
		THAT.show();
		if (AAufbau.f.CurrendPanel instanceof ExtendsDefaultEditor) {
			ExtendsDefaultEditor ede = (ExtendsDefaultEditor) AAufbau.f.CurrendPanel;
			THAT.update(ede.getMatrix().getSelectedCObject());
		}
	}
}
