package editor.rpg;

import img.Icons;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import editor.CObject;
import editor.DefaultEditorPanel.Matrix;
import editor.GUI_Layerbefehle;
import editor.Layer;
import editor.rpg.TiledMapEditor.Teilbar;
import gui.AAufbau;

public enum TiledMapManager implements ActionListener, ItemListener {
	THAT();
	HashMap<File, Teilbar> hmTileMap = new HashMap<>();
	public JDialog dialog;
	JComboBox<File> box = new JComboBox<>();
	JPanel panel = new JPanel();
	JButton selectAll = new JButton();

	@Override
	public void actionPerformed(ActionEvent e) {
		if (dialog == null) {
			init();
		}
		updateBox();

		tme.init();
		dialog.setVisible(!dialog.isVisible());
	}

	TiledMapEditor tme;

	private void init() {
		dialog = new JDialog(AAufbau.f);
		dialog.setSize(300, 300);
		dialog.setLocation(200, 200);
		dialog.add(panel);
		panel.setLayout(new BorderLayout());
		panel.add(box, BorderLayout.NORTH);
		selectAll.setText("Alle TiledMap markieren");
		selectAll.setIcon(Icons.getImage("table_select_all", 16));
		selectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Matrix m = GUI_Layerbefehle.getMatrix();
				m.getSelectedList().clear();
				for (Layer l : m.getLayerList()) {
					for (CObject c : l) {
						if (c instanceof CTileMap) {
							m.addSelectedCObject(c);
						}
					}
				}
			}
		});
		panel.add(selectAll, BorderLayout.SOUTH);
		box.addItemListener(this);
		tme = new TiledMapEditor();
		panel.add(tme);
		update1();

	}

	private void update1() {
		// Tilemaps
		for (String map : CTileMap.hmTileMap.keySet()) {
			File f = new File(map);
			label: if (!hmTileMap.containsKey(f)) {
				for (Layer l : getMatrix().getLayerList()) {
					for (CObject c : l) {
						if (c instanceof CTileMap) {
							if (((CTileMap) c).getFile().equals(f)) {
								hmTileMap.put(f, (Teilbar) c);
								break label;
							}
						}
					}
				}
			}
		}
		// CRef
		for (File f : CRef.hmRefMap.keySet()) {
			label: if (!hmTileMap.containsKey(f)) {
				for (Layer l : getMatrix().getLayerList()) {
					for (CObject c : l) {
						if (c instanceof CRef) {
							if (((CRef) c).getFile().equals(f)) {
								hmTileMap.put(f, (Teilbar) c);
								break label;
							}
						}
					}
				}
			}
		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		tme.init();
		tme.repaint(100);
	}

	public void update() {
		if (dialog != null && dialog.isVisible()) {
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					update1();
					updateBox();
					tme.init();
				}
			}.start();
		}
	}

	private Matrix getMatrix() {
		return GUI_Layerbefehle.getMatrix();
	}

	private void updateBox() {
		Object str = box.getSelectedItem();
		box.removeAllItems();
		for (File s : hmTileMap.keySet()) {
			box.addItem(s);
		}
		if (str != null)
			box.setSelectedItem(str);
	}
}
