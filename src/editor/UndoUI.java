package editor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.KeyStroke;

import toolbars.Toolbar;
import editor.DefaultEditorPanel.Matrix;
import interfaces.updateable;

public class UndoUI implements ActionListener, updateable {
	static ArrayList<AbstractButton> undo = new ArrayList<>();
	static ArrayList<AbstractButton> redo = new ArrayList<>();

	public UndoUI(String name) {
		undo.add(Toolbar.createMenuItem("Undo", "arrow_undo", 16, name, this,
				KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK)));
		redo.add(Toolbar.createMenuItem("Redo", "arrow_redo", 16, name,
				redoAction,
				KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK)));

		Matrix.addUpdate(this);
	}

	Matrix m;

	protected void setMatirx(Matrix m) {
		this.m = m;
	}

	protected Matrix getMatrix() {
		if (m != null) {
			return m;
		}
		return GUI_Layerbefehle.getMatrix();
	}

	ActionListener redoAction = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			Matrix m = getMatrix();
			m.exeRedo();
			update(m);
		}
	};

	@Override
	public void actionPerformed(ActionEvent e) {
		Matrix m = getMatrix();
		m.exeUndo();
		update(m);
	}

	public void update(Matrix m) {
		setMatirx(m);
		if (m != null && m.undos.size() != 0) {
			for (AbstractButton b : undo) {
				b.setText(getMatrix().undos.get(0).getName());
			}
		} else {
			for (AbstractButton b : undo) {
				b.setText("<html><b>no undo</html>");
			}
		}
		if (m != null && m.redos.size() != 0) {
			for (AbstractButton b : redo) {
				b.setText(getMatrix().redos.get(0).getName());
			}
		} else {
			for (AbstractButton b : redo) {
				b.setText("<html><b>no redo</html>");
			}
		}
	}

	public static void addButton(AbstractButton b) {
		if (b.getActionListeners()[0] instanceof UndoUI) {
			undo.add(b);
		} else {
			redo.add(b);
		}
		b.setPreferredSize(new Dimension(250, 30));
	}
}
