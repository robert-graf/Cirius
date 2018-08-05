package interfaces;

import editor.DefaultEditorPanel.Matrix;
import editor.GUI_Layerbefehle;

public abstract class UndoObject implements Undo {

	protected int id;
	protected Object old;
	protected Object neu;
	protected Object obj;

	public UndoObject(int id, Object old, Object neu, Object obj) {
		this.id = id;
		this.old = old;
		if (getMatrix() != null && getMatrix().getFirstUndo() != null
				&& getMatrix().getFirstUndo().getID() >= 0
				&& getMatrix().getFirstUndo().getID() == getID()
				&& getMatrix().getFirstUndo() instanceof UndoObject
				&& ((UndoObject) getMatrix().getFirstUndo()).obj.equals(obj)) {
			this.old = ((UndoObject) getMatrix().getFirstUndo()).old;

		}
		this.neu = neu;
		this.obj = obj;
	}

	@Override
	public int getID() {
		return id;
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

	public Object getObject() {
		return obj;
	}
}
