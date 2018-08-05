package toolbars;

import database.game.DataBaseEditor;
import editor.UndoUI;
import editor.tileMap.TileMapEditor;
import editor.tileSet.TileSetEditor;
import gui.AAufbau;
import gui.DefaultTree;
import gui.SpriteEinstellen;
import img.Icons;
import interfaces.ExtendsDefaultEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.KeyStroke;

public class Toolbar {
	public Toolbar() {
		DialogAbout.init();
		DialogÄndern.init();
		DialogCAD.init();
		DialogDatabase.init();
		DialogDatei.init();
		DialogRPG.init();
		DialogSpriteEditor.init();
		DialogRun.init();
		DialogUndo.init();
		DialogTiledMap.init();
		add(DialogAbout.name);
		add(DialogDatei.name);
		add(DialogRun.name);
		add(DialogUndo.name);
	}

	JComponent old;

	// DialogDatabase ddb = new DialogDatabase();
	// DialogSpriteEditor dse = new DialogSpriteEditor();
	// DialogRPG dm = new DialogRPG();
	// DialogTileMap dtm = new DialogTileMap();
	public void update(JComponent panel) {
		if (panel instanceof ExtendsDefaultEditor) {
			((ExtendsDefaultEditor) panel).update();
		}
		// REMOVE
		if (old instanceof SpriteEinstellen) {
			remove(DialogSpriteEditor.name);
		} else if (old instanceof editor.rpg.GUIMapEditStart) {
			remove(DialogRPG.name);
			remove(DialogCAD.name);
			remove(DialogÄndern.name);
			remove(DialogEinstellungen.name);
		} else if (old instanceof TileSetEditor) {
		} else if (old instanceof TileMapEditor) {
			remove(DialogTiledMap.name);
		} else if (old instanceof DataBaseEditor) {
			remove(DialogDatabase.name);
		} else {
		}
		// Add
		if (panel instanceof SpriteEinstellen) {
			add(DialogSpriteEditor.name);
			DialogSpriteEditor.update();
		} else if (panel instanceof editor.rpg.GUIMapEditStart) {
			add(DialogCAD.name);
			add(DialogÄndern.name);
			add(DialogRPG.name);
			add(DialogEinstellungen.name);
			DialogRPG.update();
		} else if (panel instanceof TileSetEditor) {
			
		} else if (panel instanceof TileMapEditor) {
			add(DialogTiledMap.name);
//			add(DialogRPG.name);
		} else if (panel instanceof DataBaseEditor) {
			add(DialogDatabase.name);
			DialogDatabase.update((DataBaseEditor) panel);
		} else  {
			panel = null;
		}
		if (DefaultTree.update != null) {
			DefaultTree.update.doClick(100);
		}
		old = panel;
		bar.repaint();
		bar.validate();
	}

	public static JMenu get(String s) {
		try {
			return menu.get(s);
		} catch (Exception e) {
			System.err.println(s + " get");
		}
		return null;
	}

	public static void add(String s) {
		try {
			bar.add(menu.get(s), bar.getMenuCount() - 1);
		} catch (Exception e) {
			System.err.println(s + " add");
		}
	}

	public static void remove(String s) {
		try {
			bar.remove(menu.get(s));
		} catch (Exception e) {
			System.err.println(s + " remove");
		}
	}

	public static Dimension miniButt = new Dimension(32, 32);
	public static Dimension maxButt = new Dimension(400, 32);
	public static boolean setText = false, setIcon = true;

	public static void createNormComp(Component c) {
		c.setMinimumSize(miniButt);
		c.setMaximumSize(maxButt);
	}

	public static final JMenuBar bar = new JMenuBar();

	static final HashMap<String, JMenu> menu = new HashMap<String, JMenu>();
	
	public static JMenuItem createCheckBoxMenuItem(String s, String icon, int id,
			String gruppe, ActionListener a,boolean selected) {
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(s);
		item.setSelected(selected);
		item.addActionListener(a);
		item.setIcon(Icons.getImage(icon, id));
		if (menu.containsKey(gruppe)) {
			menu.get(gruppe).add(item,
					menu.get(gruppe).getMenuComponentCount() - 2);
		} else {
			JMenu jmenu = new JMenu(gruppe);
			jmenu.add(item);
			jmenu.addSeparator();
			JMenuItem i = new JMenuItem("Toolbar");
			i.addActionListener(new CreateToolbar(gruppe));
			jmenu.add(i);
			menu.put(gruppe, jmenu);
			// bar.add(jmenu);
		}
//		if (key != null) {
//			item.setAccelerator(key);
//		}
		// KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK);
		return item;
	}
	public static JMenuItem createMenuItem(String s, String icon, int id,
			String gruppe, ActionListener a) {
		System.out.println(s + " hat keine shortcuts");
		// TODO
		return createMenuItem(s, icon, id, gruppe, a, null);
	}

	public static JMenuItem createMenuItem(String s, String icon, int id,
			String gruppe, ActionListener a, KeyStroke key) {
		JMenuItem item = new JMenuItem(s);
		item.addActionListener(a);
		item.setIcon(Icons.getImage(icon, id));
		if (menu.containsKey(gruppe)) {
			menu.get(gruppe).add(item,
					menu.get(gruppe).getMenuComponentCount() - 2);
		} else {
			JMenu jmenu = new JMenu(gruppe);
			jmenu.add(item);
			jmenu.addSeparator();
			JMenuItem i = new JMenuItem("Toolbar");
			i.addActionListener(new CreateToolbar(gruppe));
			jmenu.add(i);
			menu.put(gruppe, jmenu);
			// bar.add(jmenu);
		}
		if (key != null) {
			item.setAccelerator(key);
		}
		// KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK);
		return item;
	}

	public static AbstractButton createButton(String s, String icon, int id) {
		AbstractButton b = new JButton();
		createNormComp(b);
		if (setText) {
			b.setText(s);
		}
		if (setIcon) {
			b.setIcon(Icons.getImage(icon, id));
		}
		b.setToolTipText(s);
		return b;
	}

	public static AbstractButton createButton(JMenuItem m, String key) {
		AbstractButton b = new JButton();
		createNormComp(b);
		if (setText) {
			b.setText(m.getText());
		}
		if (setIcon) {
			b.setIcon(m.getIcon());
		}
		b.setToolTipText(m.getText());
		for (int n = 0; n != m.getActionListeners().length; n++) {
			b.addActionListener(m.getActionListeners()[n]);
		}
		if (key.equals(DialogUndo.name)) {
			UndoUI.addButton(b);
		}
		return b;
	}

	private static BufferedImage buff, buff2;
	static {
		buff = new BufferedImage(4, 20, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = buff.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 4, 20);

		buff2 = new BufferedImage(20, 4, BufferedImage.TYPE_3BYTE_BGR);
		g = buff2.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 32, 4);
	}
	static public ArrayList<JWindow> toolbars = new ArrayList<JWindow>();

	private static class CreateToolbar implements ActionListener,
			MouseMotionListener, MouseListener {
		String key;
		JButton verschieber;
		JWindow toolBar = null;

		public CreateToolbar(String key) {
			this.key = key;
			verschieber = new JButton("");
			verschieber.addMouseMotionListener(this);
			verschieber.addMouseListener(this);
			verschieber.setBackground(Color.WHITE);

		}

		JPanel panel;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (toolBar == null) {
				toolBar = new JWindow(AAufbau.f);
				toolbars.add(toolBar);
				panel = new JPanel();
				panel.add(verschieber);
				JMenu m = menu.get(key);
				for (int n = 0; n != m.getMenuComponentCount() - 1; n++) {
					if (m.getMenuComponent(n) instanceof JMenuItem) {
						JMenuItem item = (JMenuItem) m.getMenuComponent(n);
						panel.add(createButton(item, key));
					}
				}
				toolBar.add(panel);
				swap();

			}
			toolBar.setVisible(!toolBar.isVisible());
			toolBar.pack();
		}

		Point mouseOnButton;

		@Override
		public void mouseDragged(MouseEvent e) {
			toolBar.setLocation(e.getLocationOnScreen().x - mouseOnButton.x,
					e.getLocationOnScreen().y - mouseOnButton.y);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseOnButton = e.getPoint();
			if (e.isMetaDown()) {
				// toolBar.setVisible(false);
				swap();
			}
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}

		public void swap() {
			int axis = 0;
			if (panel.getLayout() instanceof BoxLayout) {
				axis = ((BoxLayout) panel.getLayout()).getAxis();
			}
			if (axis == BoxLayout.PAGE_AXIS) {
				panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
				verschieber.setPreferredSize(new Dimension(13, 20));
				verschieber.setIcon(new ImageIcon(buff));
			} else {
				panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
				verschieber.setPreferredSize(new Dimension(20, 13));
				verschieber.setIcon(new ImageIcon(buff2));
			}
			((Window) panel.getTopLevelAncestor()).pack();
			panel.validate();
			panel.repaint();
		}

	}
}
