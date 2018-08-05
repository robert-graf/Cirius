package interfaces;

import editor.DefaultEditorPanel.Matrix;

public interface ExtendsDefaultEditor extends DestroyAble, FileHolder {
	public Matrix getMatrix();

	public void update();

}
