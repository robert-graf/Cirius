package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import toolbars.DialogSpriteEditor;
import database.SpriteXML;
import database.SpriteXML.Animation;
import database.SpriteXML.Scene;
import editor.cad.ShapeEditor;
import editor.tabelle.DefaultTabel;
import interfaces.FileHolder;
import interfaces.Save;

////<html><font color=164914><html>
@SuppressWarnings("serial")
public class SpriteEinstellen extends JTabbedPane implements Drop, Save,
		MouseListener, FileHolder {
	float scale = 1;
	JPanel spriteEdit;
	public SpriteXML data;
	public SubToggleButton animieren;
	DefaultTabel table;
	String[] s = { "Animation", "Zeit", "Image", "X", "Y", "Breite", "Höhe",
			"Wand" };
	Object[][] o = new Object[0][s.length];
	JPanel Buttons = new JPanel();
	public JList<Object> jlist = new JList<>();
	File f;

	public SpriteEinstellen(File f) {
		this.f = f;
		String[] s0 = { "                NOT JET STARTED             " };
		jlist.setListData(s0);
		// setTabPlacement(JTabbedPane.LEFT);
		data = new SpriteXML(f, this);
		spriteEdit = new JPanel();
		spriteEdit.setLayout(new BorderLayout());
		JSplitPane sp = new JSplitPane(1, spriteEdit, getSide());
		sp.setDividerLocation(900);
		this.add("SpirteEdit", sp);
		table = new DefaultTabel(o, s);
		spriteEdit.add(new JScrollPane(table));
		updataTable();
		AuftragCellEditor editor = new AuftragCellEditor();
		for (int n = 0; n < table.getColumnCount(); n++) {
			table.getColumn(table.getColumnName(n)).setCellEditor(editor);
		}
		table.addMouseListener(this);
		Buttons.setLayout(new GridLayout(3, 6));
		JSpinner spinner = new JSpinner(new SpinnerNumberModel() {
			public void setValue(Object arg0) {
				DialogSpriteEditor.Animationindex.setValue(arg0);
			}

			public void removeChangeListener(ChangeListener arg0) {
				DialogSpriteEditor.Animationindex.removeChangeListener(arg0);
			}

			public Object getValue() {
				return DialogSpriteEditor.Animationindex.getValue();
			}

			public Object getPreviousValue() {
				return DialogSpriteEditor.Animationindex.getPreviousValue();
			}

			public Object getNextValue() {
				return DialogSpriteEditor.Animationindex.getNextValue();
			}

			public void addChangeListener(ChangeListener arg0) {
				DialogSpriteEditor.Animationindex.addChangeListener(arg0);
			}
		});
		Buttons.add(spinner);
		spinner = new JSpinner(new SpinnerNumberModel() {
			public void setValue(Object arg0) {
				DialogSpriteEditor.Scenenindex.setValue(arg0);
			}

			public void removeChangeListener(ChangeListener arg0) {
				DialogSpriteEditor.Scenenindex.removeChangeListener(arg0);
			}

			public Object getValue() {
				return DialogSpriteEditor.Scenenindex.getValue();
			}

			public Object getPreviousValue() {
				return DialogSpriteEditor.Scenenindex.getPreviousValue();
			}

			public Object getNextValue() {
				return DialogSpriteEditor.Scenenindex.getNextValue();
			}

			public void addChangeListener(ChangeListener arg0) {
				DialogSpriteEditor.Scenenindex.addChangeListener(arg0);
			}
		});
		Buttons.add(spinner);
		Buttons.add(new SubButton("Neue Animation hinzufügen",
				DialogSpriteEditor.Neue_Ani));
		Buttons.add(new SubButton("Neue Scene", DialogSpriteEditor.Neue_Scene));
		Buttons.add(new SubButton("Animation löschen",
				DialogSpriteEditor.delete_Ani));
		Buttons.add(new SubButton("Scene löschen",
				DialogSpriteEditor.delete_Scene));
		spriteEdit.add(Buttons, BorderLayout.SOUTH);

		// TODO Repaint Wall
		// TODO AddImage
		/**
		 * addChangeListener(new ChangeListener() { //// public void
		 * stateChanged(ChangeEvent e) { //// repaintWall(); //// }});
		 * 
		 * try { wandEdit.dpanel.i = ((ImageIcon) image.getIcon()).getImage(); }
		 * catch (Exception e) {wandEdit.dpanel.i = null;} wandEdit.validate();
		 * wandEdit.repaint();
		 * 
		 * // GStart wandEdit = new GStart(data); // add("Wände", wandEdit);
		 */
		add("Wände", new ShapeEditor(f));

		setSelectedRow(0);
	}

	public void setSelectedRow(int i) {
		table.setRowSelectionInterval(i, i);
	}

	public void updataTable() {
		int cont = 0, cont1 = 0;
		for (Animation a : data.AnimationList) {
			for (Scene s : a) {
				Object[] row = { cont, s.getTime(),
						new File(s.getImagePfad()).getName(), s.getX(),
						s.getY(), s.getWidht(), s.getHeight(), "TODO" };
				table.addRow(row, cont1);
				cont1++;
			}
			cont++;
			((DefaultTableModel) table.getModel()).setRowCount(cont1);
			table.getColumn(table.getColumnName(2)).setPreferredWidth(300);
		}
	}

	public class AuftragCellEditor extends AbstractCellEditor implements
			TableCellEditor, ChangeListener {
		JDialog c = new JDialog(AAufbau.f);
		JSpinner spinner = new JSpinner();
		TextField tf = new TextField("SpriteEinstellenTest");
		JComboBox<String> comboBox = new JComboBox<>();

		public AuftragCellEditor() {
			tf.addMouseListener(table);
			c.addMouseListener(table);
			spinner.addMouseListener(table);
			spinner.addChangeListener(this);
			c.setSize(100, 200);
			c.setAlwaysOnTop(true);
		}

		@Override
		public Object getCellEditorValue() {
			table.setRowHeight(row, table.getRowHeight());
			Object o = getCellEditorValue2();
			updateImage();
			return o;
		}

		public Object getCellEditorValue2() {
			// TODO Animation
			if (column == 0) {
				return value;
			}
			if (column == 1) {
				data.sucheReihe(row).setTime((Integer) spinner.getValue());
				return spinner.getValue();
			}
			if (column == 2) {
				data.sucheReihe(row).setImagePfad(
						(String) comboBox.getSelectedItem());
				if (comboBox.getSelectedItem() == null) {
					return "";
				}
				return new File((String) comboBox.getSelectedItem()).getName();
			}
			if (column == 3) {
				data.sucheReihe(row).setX((Integer) spinner.getValue());
				return spinner.getValue();
			}
			if (column == 4) {
				data.sucheReihe(row).setY((Integer) spinner.getValue());
				return spinner.getValue();
			}
			if (column == 5) {
				data.sucheReihe(row).setWidht((Integer) spinner.getValue());
				return spinner.getValue();
			}
			if (column == 6) {
				data.sucheReihe(row).setHeight((Integer) spinner.getValue());
				return spinner.getValue();
			}
			return value;
		}

		Object value;
		int row, column;

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			this.row = row;
			this.column = column;
			this.value = value;
			if (column == 0) {
				return null;
			}
			if (column == 2) {
				comboBox.removeAllItems();
				jlist.setListData(imageHash.keySet().toArray());
				for (String key : imageHash.keySet()) {
					comboBox.addItem(key);
				}
				comboBox.setSelectedItem(value);
				return comboBox;
			}
			if (value instanceof String) {
				tf.setText((String) value);
				return tf;
			}
			if (value instanceof Number) {
				// TIME
				if (column == 1) {
					spinner.setModel(new SpinnerNumberModel(0, 0,
							Integer.MAX_VALUE, 10));
				}// x
				else if (column == 3) {
					spinner.setModel(new SpinnerNumberModel(0, 0, imageHash
							.get(data.sucheReihe(row).getImagePfad())
							.getWidth() - 1, 1));
				}// Y
				else if (column == 4) {
					spinner.setModel(new SpinnerNumberModel(0, 0, imageHash
							.get(data.sucheReihe(row).getImagePfad())
							.getHeight() - 1, 1));
				}// Width
				else if (column == 5) {
					spinner.setModel(new SpinnerNumberModel(0, 0, imageHash
							.get(data.sucheReihe(row).getImagePfad())
							.getWidth(), 1));
				}// Height
				else if (column == 6) {
					spinner.setModel(new SpinnerNumberModel(0, 0, imageHash
							.get(data.sucheReihe(row).getImagePfad())
							.getHeight(), 1));
				}
				table.setRowHeight(row, 22);
				spinner.setValue(value);
				return spinner;
			}
			if (value instanceof Component) {
				return (Component) value;
			}
			if (value instanceof ImageIcon) {
				if (!c.isVisible()) {
					c.setLocationRelativeTo(null);
				}
				c.pack();
				c.setVisible(true);
				c.add(new JScrollPane(new JLabel((ImageIcon) value)));
				return new JLabel(value + "");
			}
			JOptionPane.showMessageDialog(null, value.getClass().getName()
					+ " muss erst unterstütz werden ("
					+ this.getClass().getName() + ")");
			return null;
		}

		// JSpinner
		@Override
		public void stateChanged(ChangeEvent e) {
			if (value instanceof Number) {
				getCellEditorValue2();
				updateImage();

			}
		}
	}

	JLabel image;

	public JPanel getSide() {
		image = new JLabel("Muss initialiesiert werden.");
		image.setHorizontalAlignment(JLabel.CENTER);
		updateImage();
		JPanel p = new JPanel();
		
		p.setLayout(new BorderLayout());
		JScrollPane sc = new JScrollPane(image);
				sc.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mousePressed(MouseEvent ev) {
						updateImage();
						if(ev.getButton()==1){
							if(scale<1)scale*=2;
							else scale+=1;
						}else if(ev.getButton()==3){
							if(scale<=1)scale/=2;
							else scale-=1;
						}
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
		p.add(sc);
		// Unten
		JPanel panel = new JPanel(new GridLayout(3, 1));
		animieren = new SubToggleButton("Animieren",
				DialogSpriteEditor.Animation);
		panel.add(animieren);
		panel.add(new SubButton("Raster", DialogSpriteEditor.rasterb));
		panel.add(new SubButton("Tree", DialogSpriteEditor.tree));
		p.add(panel, BorderLayout.SOUTH);
		return p;
	}

	public class SubToggleButton extends JToggleButton implements
			ActionListener {
		AbstractButton b;

		public SubToggleButton(String s, AbstractButton b) {
			super(s);
			this.b = b;
			setSelected(DialogSpriteEditor.doAnimieren);
			setIcon(b.getIcon());
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			b.doClick();
			b.repaint();
		}
	}

	private class SubButton extends JButton implements ActionListener {
		AbstractButton b;

		public SubButton(String s, AbstractButton b) {
			super(s);
			this.b = b;
			setIcon(b.getIcon());
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			b.doClick();
		}
	}

	public HashMap<String, BufferedImage> imageHash = new HashMap<>();

	public void updateImage() {
		try {
			Scene s = data.getSceneByToolBar();
			if (s == null || s.getImagePfad() == null
					|| s.getImagePfad().isEmpty()
					|| !new File(s.getImagePfad()).exists()) {
				image.setIcon(null);
				image.setText("<html>Für diesen Eintrag gibt es keine Bild.<br> Fügen sie ein Bild hinzu. "
						+ "<br> Mit Drag´n´Drop können sie neue Bilder aus dem Baum hinzufügen.<html>");
				return;
			}
			if (!imageHash.containsKey(s.getImagePfad())) {

				imageHash.put(s.getImagePfad(),
						ImageIO.read(new File(s.getImagePfad())));
			}
			if (s.getX() > imageHash.get(s.getImagePfad()).getWidth()) {
				s.setX(imageHash.get(s.getImagePfad()).getWidth() - 2);
				s.setWidht(1);
			} else if (s.getY() > imageHash.get(s.getImagePfad()).getHeight()) {
				s.setY(imageHash.get(s.getImagePfad()).getHeight() - 2);
				s.setHeight(1);
			} else if (s.getX() + s.getWidht() > imageHash
					.get(s.getImagePfad()).getWidth()) {
				s.setWidht(imageHash.get(s.getImagePfad()).getWidth()
						- s.getX());
				updataTable();
			} else if (s.getY() + s.getHeight() > imageHash.get(
					s.getImagePfad()).getHeight()) {

				s.setHeight(imageHash.get(s.getImagePfad()).getHeight()
						- s.getY());
				updataTable();
			}
			Image i = imageHash.get(s.getImagePfad()).getSubimage(s.getX(), s.getY(), s.getWidht(),s.getHeight());
			if(scale != 1){
				if(Math.min(s.getWidht()*scale, s.getHeight()*scale)<4){
					scale*=2;
				}
				i = i.getScaledInstance((int)(s.getWidht()*scale), (int)(s.getHeight()*scale), Image.SCALE_FAST);
			}
			image.setIcon(new ImageIcon(i));
			image.setText("");
		} catch (Exception e) {
			image.setText("Ein unbehandelter Fehler ist aufgetreten. "
					+ "Geben sie keine \"Null\" oder \"Out of Range\" ein.");
		}
	}

	@Override
	public void save() {
		data.save();
	}

	@Override
	public boolean dropEvent(File f) {
		if (f.getName().endsWith("png")) {
			if (data.AnimationList.size() == 0) {
				data.addAnimation();
			}
			try {
				imageHash.put(f.getPath().replace("\\", "/"), ImageIO.read(f));
				jlist.setListData(imageHash.keySet().toArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		Point p = data.getAnimationAndSceneByRow(table.getSelectedRow());
		DialogSpriteEditor.Animationindex.setValue(p.x);
		DialogSpriteEditor.Scenenindex.setValue(p.y);
	}

	public void mouseReleased(MouseEvent e) {
		Point p = data.getAnimationAndSceneByRow(table.getSelectedRow());
		DialogSpriteEditor.Animationindex.setValue(p.x);
		DialogSpriteEditor.Scenenindex.setValue(p.y);
	}

	@Override
	public File getFile() {
		return f;
	}

}