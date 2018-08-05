package editor.tabelle;

import java.awt.Component;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

import gui.AAufbau;
import interfaces.UserZugang;

@SuppressWarnings("serial")
public class ObjectTable extends DefaultTabel {
	Timer t;

	public ObjectTable(Object[][] o, Object[] o1) {
		super(o, o1);
		getColumnModel().getColumn(1).setCellEditor(new AuftragCellEditor());
		t = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
	}

	boolean show = false;
	UserZugang o;

	void setObject(Object o) {
		if (o == null || !(o instanceof UserZugang)) {
			model.setRowCount(0);
			show = false;
			if (t.isRunning()) {
				t.stop();
			}
			return;
		}
		this.o = (UserZugang) o;
		reloudNew();
		if (!t.isRunning()) {
			t.start();
		}
	}

	private void reloudNew() {
		if (o == null || o.getLines() == null) {
			return;
		}
		ArrayList<OneLine<?>> line = o.getLines();
		model.setRowCount(line.size());
//		int cont = 0;
		for (int n = 0; n != line.size(); n++) {
			model.setValueAt(line.get(n).getKey(), n, 0);
			model.setValueAt(line.get(n), n, 1);
//			cont = n;
		}
//		if(o instanceof CShape){
//		for(String s : ((CShape) o).getCSS().getKeys()){
//			model.setValueAt(s, cont, 0);
//			model.setValueAt(((CShape) o).getCSS().getValue(s), cont, 1);
//			cont =+ 1;
//		}
//		}
	}

	public class AuftragCellEditor extends AbstractCellEditor implements
			TableCellEditor, ItemListener, ChangeListener, ActionListener,
			FocusListener {
		JDialog c = new JDialog(AAufbau.f);
		JCheckBox box = new JCheckBox();
		JSpinner spinner = new JSpinner();
		TextField tf = new TextField("null");
		JComboBox<Object> cbox = new JComboBox<Object>();
		JLabel label = new JLabel();
		// Models
		SpinnerNumberModel numberModel = new SpinnerNumberModel(0,0,0,0);

		public AuftragCellEditor() {
			tf.addMouseListener(ObjectTable.this);
			tf.addActionListener(this);
			tf.addFocusListener(this);
			box.addMouseListener(ObjectTable.this);
			box.addItemListener(this);
			c.addMouseListener(ObjectTable.this);
			spinner.addMouseListener(ObjectTable.this);
			spinner.addChangeListener(this);
			spinner.getEditor().setBorder(
					javax.swing.BorderFactory.createEmptyBorder());
			spinner.setModel(numberModel);
			c.setSize(100, 200);
			c.setAlwaysOnTop(true);
			cbox.addItemListener(this);
//			cbox.setRenderer(new ListCellRenderer<Object>() {
//
//				@Override
//				public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
//						boolean isSelected, boolean cellHasFocus) {
//					if(value instanceof JMenuItem){
//						label.setText(((JMenuItem) value).getText());
//						return label;
//							
//					}
//					label.setText(((JComponent) value).toString());
//					return label;
//				}
//			
//			
//			});
			label.setOpaque(true);
		}

		@Override
		public Object getCellEditorValue() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					reloudNew();
				}
			});
			return line;
		}

		OneLine<?> line;

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object line2, boolean isSelected, int row, int column) {
			Object typ = ((OneLine<?>) line2).getValue();
			line = o.getLines().get(row);
			if (!line.isEditable()) {
				label.setText("" + typ);
				return label;
			}
			if (line instanceof ListLine) {
				((ListLine<?>) line).getModel(cbox);
				return cbox;
			}
			if (typ instanceof String) {
				tf.setText((String) typ);
				return tf;
			}
			if (typ instanceof Number) {
				if (line instanceof NumberLine) {
					numberModel.setMinimum(((NumberLine<?>) line).getMinimum());
					numberModel.setMaximum(((NumberLine<?>) line).getMaximum());
					numberModel.setStepSize(((NumberLine<?>) line)
							.getStepSize());
				} else {
					numberModel.setMinimum(Integer.MIN_VALUE);
					numberModel.setMaximum(Integer.MAX_VALUE);
					numberModel.setStepSize(1);
				}
				spinner.setValue(typ);
				return spinner;
			}
			if (typ instanceof Boolean) {
				box.setSelected((boolean) typ);
				return box;
			}
			JOptionPane.showMessageDialog(null, typ.getClass().getName()
					+ " muss erst unterstütz werden ("
					+ this.getClass().getName() + ")");
			return null;
		}

		// JCheckBox
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() instanceof JCheckBox) {
				line.setValueByUser(box.isSelected());
			} else {
				line.setValueByUser(cbox.getSelectedItem());
			}
		}

		// JSpinner
		@Override
		public void stateChanged(ChangeEvent e) {
			line.setValueByUser(spinner.getValue());
		}

		// Textfeld
		@Override
		public void focusGained(FocusEvent arg0) {
		}

		@Override
		public void focusLost(FocusEvent arg0) {
			line.setValueByUser(tf.getText());
		}

		// Textfeld
		@Override
		public void actionPerformed(ActionEvent e) {
			line.setValueByUser(tf.getText());
		}

	}
}
