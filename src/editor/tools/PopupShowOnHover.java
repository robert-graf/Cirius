package editor.tools;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
public enum PopupShowOnHover implements MouseListener {
	That();

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	Color c;
	@Override
	public void mouseEntered(MouseEvent e) {
		((JMenuItem)e.getComponent()).setOpaque(true);
		c = ((JMenuItem)e.getComponent()).getBackground();
		((JMenuItem)e.getComponent()).setBackground(Color.blue);
		e.getComponent().repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		e.getComponent().setBackground(c);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		e.getComponent().setBackground(c);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
