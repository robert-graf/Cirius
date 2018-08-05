package toolbars;

import editor.UndoUI;

public class DialogUndo {
	static final String name = "Undo";

	/** @see UndoUI */
	public static void init() {
		new UndoUI(name);
	}
}
