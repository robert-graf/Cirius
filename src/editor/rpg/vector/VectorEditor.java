package editor.rpg.vector;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;

import editor.DefaultEditorPanel;
import editor.DefaultEditorPanel.Matrix;
import interfaces.ExtendsDefaultEditor;

@SuppressWarnings("serial")
public class VectorEditor extends JPanel implements ExtendsDefaultEditor {
	Matrix m;

	public VectorEditor() {
		setLayout(new BorderLayout());
		DefaultEditorPanel panel = new DefaultEditorPanel();
		add(panel);
		m = panel.m;
	}

	@Override
	public void destroy() {

	}

	@Override
	public File getFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix getMatrix() {
		// TODO Auto-generated method stub
		return m;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
