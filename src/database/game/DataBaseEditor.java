package database.game;

import database.XMLCObject;
import editor.CObject;
import editor.tabelle.DefaultTabel;
import editor.tabelle.ObjectTableGUI;
import gui.AAufbau;
import gui.Drop;
import img.Icons;
import interfaces.FileHolder;
import interfaces.Save;
import interfaces.UserZugang;

import java.awt.Component;
import java.awt.TextField;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("serial")
public class DataBaseEditor extends JTabbedPane implements Save, Drop,
		ChangeListener, FileHolder {
	GameDatabase database;
	Table currendTable;
	Table gameSettings,objectTabelle, integerTabelle, floatTabelle, booleanTabelle;
	DataBaseEditor selbstbezug = this;

	public DataBaseEditor(File f) {
		database = new GameDatabase(f);
		gameSettings = new Table(database.settings, database);
		objectTabelle = new Table(database.spirteList, database);
		integerTabelle = new Table(database.integerList, database);
		floatTabelle = new Table(database.floatList, database);
		booleanTabelle = new Table(database.boolList, database);
		objectTabelle.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						UserZugang z = (UserZugang) objectTabelle.getValueAt(
								objectTabelle.getSelectedRow(), 1);
						ObjectTableGUI.THAT.update(z);
					}
				});
		add("Gamesettings",new JScrollPane(gameSettings));
		add("Objecte", new JScrollPane(objectTabelle));
		add("Ganze Zahlen", new JScrollPane(integerTabelle));
		add("Brüche", new JScrollPane(floatTabelle));
		add("Switch", new JScrollPane(booleanTabelle));
		int i = 0;
		
		setTabComponentAt(i++, new JLabel("Gamesettings",Icons.getImage("setting_tools", 16), 0));
		setTabComponentAt(i++, new JLabel("Objecte",
				Icons.getImage("ballon", 16), 0));
		setTabComponentAt(
				i++,
				new JLabel("Ganze Zahlen", Icons.getImage("calendar_view_day",
						16), 0));
		setTabComponentAt(i++,
				new JLabel("Brüche", Icons.getImage("calculator_black", 16), 0));
		setTabComponentAt(i++, new JLabel("Switch", Icons.getImage("switch", 16),
				0));

		currendTable = objectTabelle;
		addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		try {
			currendTable = (Table) ((JScrollPane) getSelectedComponent())
					.getViewport().getComponent(0);
		} catch (Exception e1) {
			currendTable = null;
			if (e1 instanceof NullPointerException) {
				return;
			}
			e1.printStackTrace();
		}
	}

	@Override
	public boolean dropEvent(File f) {
		CObject c = XMLCObject.loadCObjectByFile(f, 0, 0);
		if (c == null) {
			return true;
		}
		String key = null;
		if (objectTabelle.getSelectedRow() != -1) {
			int i = JOptionPane.showConfirmDialog(
					null,
					"Möchten Sie \""
							+ objectTabelle.getValueAt(
									objectTabelle.getSelectedRow(), 0)
							+ "\" überschreiben.");
			if (0 == i) {
				key = objectTabelle.getValueAt(objectTabelle.getSelectedRow(),
						0) + "";
			} else if (1 != i) {
				return false;
			}
		}
		if (key == null) {
			key = JOptionPane.showInputDialog(AAufbau.f,
					"Gebenen Sie den Namen des Object ein", c.getName());
		}
		if (key == null) {
			return false;
		}
		database.setCObject(key, c);
		update();
		return false;
	}

	@Override
	public void save() {
		database.write();
	}

	// TODO wird nie aufgerufen!
	public void update() {
		objectTabelle.update();
		integerTabelle.update();
		floatTabelle.update();
		booleanTabelle.update();
	}

	private static class Table extends DefaultTabel {
		static final String[] o1 = { "Name", "Value" };
		static final String[][] o = { { "Name", "Value" } };
		HashMap<String, Object> map;
		private GameDatabase data;

		@SuppressWarnings("unchecked")
		public Table(HashMap<String, ?> map, GameDatabase data) {
			super(o, o1);
			this.map = (HashMap<String, Object>) map;
			this.data = data;
			update();
			AuftragCellEditor temp = new AuftragCellEditor();
			for (int n = 0; n != getColumnCount(); n++) {
				getColumnModel().getColumn(n).setCellEditor(temp);
			}
		}

		public void update() {
			if (map == null) {
				return;
			}
			model.setRowCount(map.size());
			int n = 0;
			ArrayList<String> list = new ArrayList<>(map.keySet());
			Collections.sort(list);
			for (String s : list) {
				model.setValueAt(s, n, 0);
				model.setValueAt(map.get(s), n, 1);
				n++;
			}
		}

		HashMap<String, Object> getList() {
			return map;
		}

		class AuftragCellEditor extends AbstractCellEditor implements
				TableCellEditor {
			TextField tf = new TextField("DataBaseTextField");
			JSpinner s = new JSpinner();
			JCheckBox box = new JCheckBox();
			JSpinner spinnerFloat = new JSpinner(new SpinnerNumberModel(0f,
					(float) Integer.MIN_VALUE, (float) Integer.MAX_VALUE, 0.1f));
			Object value1;
			int column, row;

			@Override
			public Object getCellEditorValue() {
				if (column == 0) {
					rename(value1 + "", tf.getText(), map, data);
					return tf.getText();
				}
				String key = getValueAt(row, 0) + "";
				if (value1 instanceof Boolean) {
					map.put(key, box.isSelected());
					return box.isSelected();
				}
				if (value1 instanceof Float || value1 instanceof Double) {
					map.put(key,
							((Number) spinnerFloat.getValue()).floatValue());
					return ((Number) spinnerFloat.getValue()).floatValue();
				}
				if (value1 instanceof Integer) {
					map.put(key, s.getValue());
					return s.getValue();
				}
				System.out.println(value1.getClass().getName());
				return value1;
			}

			@Override
			public Component getTableCellEditorComponent(JTable table,
					Object value, boolean isSelected, int row, int column) {
				this.column = column;
				value1 = value;
				this.row = row;
				if (column == 1 && map.values().toArray()[0] instanceof CObject) {
					return null;
				}
				if (column == 1) {
					if (value instanceof Boolean) {
						box.setOpaque(true);
						box.setSelected((boolean) value);
						return box;
					}
					if (value instanceof Float || value instanceof Double) {
						spinnerFloat.setValue(value);
						return spinnerFloat;
					}
					s.setValue(value);
					return s;
				}
				tf.setText(value + "");
				return tf;

			}
		}
	}

	public void removeSelected() {
		if (currendTable.getSelectedRow() == -1) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			return;
		}
		String s = currendTable.getValueAt(currendTable.getSelectedRow(), 0)
				+ "";
		currendTable.getList().remove(s);
		currendTable.update();
	}

	public void editSprite() {
		if (currendTable.getSelectedRow() == -1) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			return;
		}
		// TODO
		JOptionPane.showMessageDialog(null, "Muss erst erstellt werden");
	}

	public void rename() {
		if (currendTable.getSelectedRow() == -1) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			return;
		}
		String s = currendTable.getValueAt(currendTable.getSelectedRow(), 0)
				+ "";
		rename(s, JOptionPane.showInputDialog(AAufbau.f,
				"Neuer Name eingeben:", s), currendTable.getList(), database);
		update();
	}

	private static void rename(String alt, String neu,
			HashMap<String, Object> map, GameDatabase data) {
		if (neu == null || neu.equals("") || neu.equals(alt)) {
			return;
		}
		data.renameKey(alt, neu, data.getTyp(map));
		// TODO LINK
	}

	public boolean add() {
		if (currendTable.equals(objectTabelle)) {
			return false;
		}
		Component c = new JSpinner();
		JTextField tf = new JTextField();
		String s = "Unbekannt";
		Object[] list2 = { "Einfügen", "Abbrechen" };
		if (currendTable.equals(integerTabelle)) {
			c = new JSpinner();
			tf.setText("Zahl");
			s = "Zahl";
		} else if (currendTable.equals(floatTabelle)) {
			c = new JSpinner(new SpinnerNumberModel(0f,
					(float) Integer.MIN_VALUE, (float) Integer.MAX_VALUE, 0.1f));
			tf.setText("KommaZahl");
			s = "Bruchzahl";
		} else if (currendTable.equals(booleanTabelle)) {
			c = new JCheckBox("an/aus", true);
			tf.setText("Wahrheitswert");
			s = "Switch";
		}
		Object[] list = { tf, c };
		int i = JOptionPane.showOptionDialog(AAufbau.f, list, "Einfügen " + s,
				0, JOptionPane.QUESTION_MESSAGE, null, list2, list2[0]);

		if (i == 0 && !tf.getText().isEmpty()) {
			if (Overridde(currendTable.map, tf.getText())) {
				return true;
			}
			if (currendTable.equals(integerTabelle)) {
				currendTable.map.put(tf.getText(), ((JSpinner) c).getValue());
			} else if (currendTable.equals(floatTabelle)) {
				currendTable.map.put(tf.getText(),
						((Double) ((JSpinner) c).getValue()).floatValue());
			} else if (currendTable.equals(booleanTabelle)) {
				currendTable.map
						.put(tf.getText(), ((JCheckBox) c).isSelected());
			}
		}
		update();
		return true;
	}

	private boolean Overridde(HashMap<String, ?> map, String key) {
		if (integerTabelle.map.containsKey(key)
				&& JOptionPane.showConfirmDialog(AAufbau.f, key
						+ " existiert bereits. Möchtest du überschreiben?") != 0) {
			return true;
		}
		return false;
	}

	public void bereinigen() {
		int i = JOptionPane
				.showConfirmDialog(
						AAufbau.f,
						"Wollen Sie wirklich die Datenbank bereinigen? \n"
								+ "Alte Referenzen würden gelöscht (z. B. nach Umbennenungen) "
								+ "und könnten nicht mehr automatisch Aktualisiert werden. \n"
								+ "Es sind aktuell "
								+ database.oldRefList.size()
								+ " Referenzen vorhanden. \n"
								+ "Dies spart natürlich Speicherplatz und "
								+ "sollte vor einem endgültigen Export gemacht werden.");
		if (i == 0) {
			database.bereinigen();
		}
	}

	public String getVersion() {
		return "" + database.getVersion();
	}

	@Override
	public File getFile() {
		return database.getFile();
	}
}
