package editor;

import editor.DefaultEditorPanel.Matrix;
import gui.AAufbau;
import interfaces.updateable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

@SuppressWarnings("serial")
public enum GUI_Layer implements updateable, ActionListener {
	THAT();

	public static ActionListener getAction() {
		return THAT;
	}

	protected CObject getObjectByRow(int row) {
		int layerIndex = layerIndexByRow.get(row);
		int n = layerIndex;
		while (layerIndexByRow.get(n) != layerIndex) {
			n++;
		}
		if (m.getLayer(layerIndex).isEmpty() || row == n) {
			return null;
		}
		return m.getLayer(layerIndex).get(row - n - 1);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (dialog == null) {
			dialog = new JDialog(AAufbau.f);
			dialog.setSize(fensterGröße);
			dialog.setLocationRelativeTo(AAufbau.f);
			dialog.add(p);
			dialog.setVisible(false);
		}
		m = GUI_Layerbefehle.getMatrix();
		dialog.setVisible(!dialog.isVisible());
	}

	// variabele
	Object[][] data = { { "", "" } };
	Object[] überschrift = { "", "Layername", "X", "Y", "Faktor", "Sichtbar",
			"Solid" };
	protected Dimension fensterGröße = new Dimension(600, 400);
	protected JTable tabelle;
	protected ColoredTableCellRenderer farbModell;
	protected TabelModel tabelModel;
	// protected ExtendsDefaultEditor c;
	protected Matrix m;
	protected Panel p = new Panel();
	JDialog dialog;

	private GUI_Layer() {

		Matrix.addUpdate(this);

		tabelModel = new TabelModel(data, überschrift);
		tabelle = new JTable(tabelModel);
		farbModell = new ColoredTableCellRenderer();
		// tabelle.getColumn(überschrift[0]).setCellRenderer(new
		// ButtonRenderer());
		tabelle.setDefaultRenderer(Object.class, farbModell);
		tabelle.setRowHeight(22);
		tabelle.getTableHeader().setReorderingAllowed(false);
		// FIXME Aufräumen, aber richtig
		tabelModel = new TabelModel(data, überschrift);
		tabelle.setModel(tabelModel);

		AuftragCellEditor temp = new AuftragCellEditor();
		for (int n = 0; n != tabelle.getColumnCount(); n++) {
			tabelle.getColumnModel().getColumn(n).setCellEditor(temp);
		}
		//
		tabelle.setShowHorizontalLines(false);
		tabelle.setShowVerticalLines(true);
		p.setLayout(new BorderLayout());
		p.add(new JScrollPane(tabelle));
		p.add(new GUI_LayerÄndern(), BorderLayout.SOUTH);
		// Zeilengröße einstellen
		tabelle.getColumn(tabelle.getColumnName(0)).setMinWidth(20);
		tabelle.getColumn(tabelle.getColumnName(0)).setMaxWidth(20);
		tabelle.getColumn(tabelle.getColumnName(1)).setPreferredWidth(200);
	}

	ArrayList<Integer> layerIndexByRow = new ArrayList<>();

	public void update(Matrix m) {
		// this.c = c;
		// tabelModel.setRowCount(0);
		int i = 0;
		if (m == null) {
			tabelModel.setRowCount(0);
			return;
		}
		this.m = m;
		int cont = 0;
		try {
			for (Layer l : m.getLayerList()) {
				addRow("•", l, null, null, l.getFaktor(), l.isVisible(),
						l.isSolid(), i);
				if (layerIndexByRow.size() == i) {
					layerIndexByRow.add(cont);
				}
				layerIndexByRow.set(i, cont);
				for (CObject obj : l) {
					i++;
					addRow("", obj.getName(), obj.getX(), obj.getY(), null,
							null, null, i);
					if (layerIndexByRow.size() == i) {
						layerIndexByRow.add(cont);
					}
					layerIndexByRow.set(i, cont);
				}
				cont++;
				i++;
			}
		} catch (ConcurrentModificationException e) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println("Noch ein versuch:" + this.getClass().getName());
			update(m);
			return;
		}
		tabelModel.setRowCount(i);
	}

	private void addRow(Object o1, Object o2, Object o3, Object o4,
			Object faktor, Object visible, Object solid, int i) {
		// TODO hier Spalten einfügen
		Object[] st = { o1, o2, o3, o4, faktor, visible, solid };
		// fensterGröße.height = tabelle.getRowHeight() *
		// tabelModel.getRowCount();
		try {
			if (tabelle != null && i != tabelle.getRowCount()) {
				for (int n = 0; n != st.length; n++) {
					tabelModel.setValueAt(st[n], i, n);
				}
			} else {
				tabelModel.addRow(st);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class TabelModel extends DefaultTableModel {
		public TabelModel(Object[][] data, Object[] überschrift) {
			super(data, überschrift);
		}

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				Layer l = m.getLayer(layerIndexByRow.get(rowIndex));
				m.setSelectedCObject(getObjectByRow(rowIndex), l);
				return false;
			}
			return tabelle.getValueAt(rowIndex, columnIndex) != null;
		}
	}

	private class ColoredTableCellRenderer extends DefaultTableCellRenderer {
		Color c1 = new Color(230, 230, 230);
		Color c2 = new Color(240, 240, 240);
		Color c3 = new Color(240, 240, 200);
		int rowPointer = -1;
		JCheckBox box = new JCheckBox();
		JLabel listLabel = new JLabel(new ImageIcon(this.getClass()
				.getResource("/img/list.png")));
		JLabel label = new JLabel();
		// JButton b = new JButton();
		Font f = new Font(Font.SANS_SERIF, Font.BOLD, 12);

		public ColoredTableCellRenderer() {
			box.setOpaque(true);
			listLabel.setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int col) {
			Component comp = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, col);
			if (value instanceof Boolean) {
				box.setSelected((boolean) value);
				comp = box;
			}
			if (value instanceof ImageIcon) {
				label.setIcon((Icon) value);
				comp = label;
			}
			if (value instanceof Component) {
				return (Component) value;
			}
			if (value instanceof Collection) {
				listLabel.setText(value.toString());
				listLabel.setHorizontalTextPosition(JLabel.LEFT);
				comp = listLabel;
			}
			if (value != null && value.toString().contains("•")) {
				rowPointer = row;
			}
			if (row == rowPointer) {
				comp.setFont(f);
			}
			if (layerIndexByRow.get(row) % 2 == 0) {
				comp.setBackground(c2);
				comp.setForeground(Color.BLACK);
			} else {
				comp.setBackground(c1);
				comp.setForeground(Color.BLACK);
			}
			if (layerIndexByRow.get(row) == m.getSelectedLayerIndex()) {
				comp.setBackground(c3);
				comp.setForeground(Color.BLACK);
			}
			if (m.getLayer(layerIndexByRow.get(row)) == null) {
				return comp;
			}
			if (col == 0
					&& m.getLayer(layerIndexByRow.get(row)).equals(
							m.getSelectedLayer())) {
				if (m.getSelectedCObject() != null
						&& m.getSelectedCObject().getName()
								.equals(table.getValueAt(row, 1) + "")) {
					comp.setBackground(Color.red);
				}
			}
			return comp;
		}
	}

	public class AuftragCellEditor extends AbstractCellEditor implements
			TableCellEditor, ChangeListener {
		Layer l;
		JCheckBox box = new JCheckBox();
		JSpinner spinnerFloat = new JSpinner(new SpinnerNumberModel(0f, -100f,
				100.1f, 0.1f));
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(0,
				Integer.MIN_VALUE, Integer.MAX_VALUE, 10));
		TextField tf = new TextField("GUI_Layer_All_Test");
		Object value1;
		int column, row;

		public AuftragCellEditor() {
			box.addChangeListener(this);
			box.setOpaque(true);
		}

		@Override
		public Object getCellEditorValue() {

			if (column == 5 || column == 6) {
				return box.isSelected();
			} else if (column == 4) {
				l.setFaktor(((Number) spinnerFloat.getValue()).floatValue());
				return spinnerFloat.getValue();
			} else if (column == 3) {
				getObjectByRow(row).setY((int) spinner.getValue());
				return spinner.getValue();
			} else if (column == 2) {
				getObjectByRow(row).setX((int) spinner.getValue());
				return spinner.getValue();
			} else if (column == 1) {
				if (l != null) {
					l.setName(tf.getText());
				} else {
					getObjectByRow(row).setName(tf.getText());
				}
				return tf.getText();
			}
			return value1;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			// TODO Auto-generated method stub
			// Layer
			if (value == null) {
				return null;
			}
			// if(value instanceof String && ((String) value).isEmpty()){return
			// null;}
			this.column = column;
			this.row = row;
			if (table.getValueAt(row, 1) instanceof Layer) {
				l = (Layer) table.getValueAt(row, 1);
			} else {
				l = null;
			}
			if (column == 5 || column == 6) {
				box.setSelected((boolean) value);
				return box;
			} else if (column == 4) {
				spinnerFloat.setValue(value);
				return spinnerFloat;
			} else if (column == 3 || column == 2) {

				spinner.setValue(value);
				return spinner;
			}
			if (column == 1) {
				tf.setText(value + "");
				return tf;
			}
			value1 = value;
			return new JLabel(value.toString());
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (l == null) {

			} else if (column == 5) {
				l.setVisible(box.isSelected());
			} else if (column == 6) {
				l.setSolid(box.isSelected());
			}
		}
	}

}
