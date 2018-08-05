package interfaces;

import editor.DefaultEditorPanel.Matrix;
import editor.Layer;

public interface MatrixListener {
	public void addToMatrixEvent(Matrix m ,Layer l);
	public void removedToMatrixEvent(Matrix m ,Layer l);
	public void selectChacheEvent(Matrix m);
}
