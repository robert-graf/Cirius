package editor;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import editor.DefaultEditorPanel.Matrix;

public class AbstractToolAdapter implements AbstractTool {

	// Key
	public void keyTyped(Matrix m, KeyEvent ev) {
	};

	public void keyReleased(Matrix m, KeyEvent ev) {
	};

	public void keyPressed(Matrix m, KeyEvent ev) {
	};

	//
	// Mouse
	public void mouseReleased(Matrix m, MouseEvent ev) {
	};

	public void mousePressed(Matrix m, MouseEvent ev) {
	};

	public void mouseExited(Matrix m, MouseEvent ev) {
	};

	public void mouseEntered(Matrix m, MouseEvent ev) {
	};

	public void mouseClicked(Matrix m, MouseEvent ev) {
	};

	//
	// mouseMotion
	public void mouseMoved(Matrix m, MouseEvent ev) {
	};

	public void mouseDragged(Matrix m, MouseEvent ev) {
	};

	//
	// mouseWheel
	public void mouseWheelMoved(Matrix m, MouseWheelEvent ev) {
	};

	// custom
	public void init(Matrix m) {
	};

	public void lost(Matrix m, MouseEvent ev) {
	};

	public void draw(Graphics2D g1, Graphics2D g2, Matrix m, MouseEvent ev) {
	}

	@Override
	public void destroy() {
	};

}
