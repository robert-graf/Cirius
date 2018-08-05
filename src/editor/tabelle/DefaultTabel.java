package editor.tabelle;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import editor.CObject;
import interfaces.FileHolder;

@SuppressWarnings("serial")
public class DefaultTabel extends JTable implements MouseListener {
	public DefaultTableModel model;

	public DefaultTabel(Object[][] o, Object[] o1) {
		model = new DefaultTableModel(o, o1);
		this.setModel(model);
		showVerticalLines = true;
		setSelectionBackground(new Color(200, 200, 255));
		setSelectionMode(0);
		setDefaultRenderer(Object.class, new ColoredTableCellRenderer());
		setRowHeight(24);
		addMouseListener(this);
		getTableHeader().setReorderingAllowed(false);
	}

	public void addRow(Object[] row, int line) {
		if (model.getRowCount() == line) {
			addRow(row);
		} else {
			int cont = 0;
			for (Object o : row) {
				model.setValueAt(o, line, cont);
				cont++;
			}
		}
	}

	public void addRow(Object[] row) {
		model.addRow(row);
	}

	private class ColoredTableCellRenderer extends DefaultTableCellRenderer {
		JCheckBox box = new JCheckBox();
		JLabel listLabel = new JLabel(new ImageIcon(this.getClass()
				.getResource("/img/list.png")));
		JLabel label = new JLabel();
		JButton button = new JButton();

		public ColoredTableCellRenderer() {
			box.setOpaque(true);
			label.setOpaque(true);
			listLabel.setOpaque(true);
			button.setOpaque(true);
		}

		// JButton b = new JButton();
		// Font f = new Font(Font.SANS_SERIF, Font.BOLD, 12);
		Color c1 = new Color(230, 230, 230);
		Color c2 = new Color(240, 240, 240);
		Color c3 = new Color(240, 240, 200);

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int col) {
			Component comp = super.getTableCellRendererComponent(table, value,
					isSelected, hasFocus, row, col);
			if (value instanceof Boolean) {
				box.setSelected((boolean) value);
				comp = box;
			} else if (value instanceof ImageIcon) {
				label.setIcon((Icon) value);
				comp = label; 
			} else if (value instanceof Component) {
				
				comp = (Component) value;
			} else if (value instanceof Collection) {
				listLabel.setText(value.toString());
				listLabel.setHorizontalTextPosition(JLabel.LEFT);
				comp = listLabel;
			} else if (value instanceof CObject) {
				label.setText(((CObject) value).getName());
				if(value instanceof FileHolder){
					label.setText(((FileHolder) value).getFile()+"");
				}
				label.setBackground(comp.getBackground());
				comp = label;
			} else if (value instanceof ActionListener) {
				ActionListener a = (ActionListener) value;
				button.addActionListener(a);
				button.setText(">>-<<");
			}
			if (row == table.getSelectedRow()) {
				comp.setBackground(c3);
				comp.setForeground(Color.BLACK);
			} else if (row % 2 == 0) {
				comp.setBackground(c2);
				comp.setForeground(Color.BLACK);
			} else {
				comp.setBackground(c1);
				comp.setForeground(Color.BLACK);
			}
			return comp;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		try {
			if (!new Rectangle(getSize()).contains(e.getPoint())) {
				getCellEditor().stopCellEditing();
			}
		} catch (Exception e1) {
			// TODO Wirft fehler wenn zeile grade nicht in bearbeitenung.
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
