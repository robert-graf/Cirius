package toolbars;

import static toolbars.Toolbar.createMenuItem;
import static toolbars.Toolbar.createNormComp;
import gui.AAufbau;
import gui.DefaultTree;
import gui.RasterBild;
import gui.SpriteEinstellen;
import img.Icons;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import database.SpriteXML.Animation;

public class DialogSpriteEditor {
	public static String name = "Sprite";
	public static boolean doAnimieren = false;
	// Neue Animation
	static final ActionListener Neue_AniAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			c.data.addAnimation();
			c.updataTable();
		}
	};

	static final ActionListener Neue_SceneAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			c.data.addScene((Integer) Animationindex.getValue(),
					(Integer) Scenenindex.getValue());
			c.updataTable();
		}
	};

	static final ActionListener delete_AniAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			c.data.AnimationList.remove((int) Animationindex.getValue());
			if (c.data.AnimationList.size() == 0) {
				c.data.addAnimation();
			}
			if (0 != (int) Animationindex.getValue()) {
				Animationindex.setValue((int) Animationindex.getValue() - 1);
			}
			c.updataTable();
			setSelectedRow();
		}
	};

	static final ActionListener delete_SceneActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			Animation a = c.data.AnimationList.get((int) Animationindex
					.getValue());
			if (a.size() == 1) {
				delete_Ani.doClick();
			} else {
				a.remove((int) Scenenindex.getValue());
				if (0 != (int) Scenenindex.getValue()) {
					Scenenindex.setValue((int) Scenenindex.getValue() - 1);
				}
				c.updataTable();
				setSelectedRow();
			}
		}
	};

	static final ActionListener saveAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			c.save();
		}
	};

	static final ActionListener rasterbAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (raster == null) {
				raster = new RasterBild(c, c.data,
						"Bild beschneiden. Animation: "
								+ Animationindex.getValue() + " Scene: "
								+ Scenenindex.getValue());
			}
			raster.setVisible(!raster.isVisible());
			raster.setImageSize();
		}
	};

	static final ActionListener treeAction = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			DefaultTree.getDefaultTree(s, c.jlist);
		}
	};
	static final ActionListener animierenAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			doAnimieren = !doAnimieren;
			if (doAnimieren) {
				((AbstractButton) e.getSource()).setIcon(Icons.getImage(
						"check_box", 16));
				Animation.setIcon(Icons.getImage("check_box", 16));
			} else {
				((AbstractButton) e.getSource()).setIcon(Icons.getImage(
						"check_box_empty", 0));
				Animation.setIcon(Icons.getImage("check_box_empty", 0));
			}
			c.animieren.setSelected(doAnimieren);
			c.animieren.setIcon(Animation.getIcon());
			Runnable t = new Runnable() {
				public void run() {
					Scenenindex.setFocusable(false);
					while (doAnimieren) {
						try {
							Animation ani = null;
							ani = c.data.getAnimation((Integer) Animationindex
									.getValue());
							Thread.sleep(ani.get(
									(Integer) Scenenindex.getValue()).getTime());
							if (ani.size() - 1 > (Integer) Scenenindex
									.getValue()) {
								Scenenindex.setValue((Integer) Scenenindex
										.getValue() + 1);
							} else {
								Scenenindex.setValue(0);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					Scenenindex.setFocusable(true);
				}
			};
			new Thread(t).start();
		}
	};
	protected static SpriteEinstellen c;
	static RasterBild raster;

	public static void init() {
		// FIXME
		Animationindex.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (c.data.AnimationList.size() <= (int) Animationindex
						.getValue()) {
					Animationindex.setValue(c.data.AnimationList.size() - 1);
				}
				boolean update = ((Integer) Scenenindex.getValue() == 0);
				Scenenindex.setValue(0);
				if (update) {
					c.updateImage();
					setSelectedRow();
				}
			}
		});
		// FIXME
		Scenenindex.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (c.data.AnimationList.get((int) Animationindex.getValue())
						.size() <= (int) Scenenindex.getValue()) {
					Scenenindex.setValue(c.data.AnimationList.get(
							(int) Animationindex.getValue()).size() - 1);
				}
				new Thread(new Runnable() {
					public void run() {
						c.updateImage();
						setSelectedRow();
						// if(c.wandEdit.isShowing()){c.repaintWall();};
					};
				}).start();
			}
		});
		// TODO Wände
	}

	public static void setSelectedRow() {
		try {
			int i = 0;
			for (int n = 0; n != (int) Animationindex.getValue(); n++) {
				i += c.data.AnimationList.get(n).size();
			}
			i += (int) Scenenindex.getValue();
			c.setSelectedRow(i);
		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			System.out.println(e);
		}
	}

	public static void update() {
		c = (SpriteEinstellen) AAufbau.f.CurrendPanel;
		c.animieren.setSelected(doAnimieren);
		c.animieren.setIcon(Animation.getIcon());
		DefaultTree.setComp(c.jlist);
		DefaultTree.Erlaubt = s;
		if (Animationindex != null) {
			Animationindex.setValue(0);
			Scenenindex.setValue(0);
		}
	}

	public static boolean animieren = false;
	public static JSpinner Animationindex = new JSpinner(
			new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
	public static JSpinner Scenenindex = new JSpinner(new SpinnerNumberModel(0,
			0, Integer.MAX_VALUE, 1));;
	public static final AbstractButton Animation = createMenuItem("Animieren",
			"check_box_empty", 0, name, animierenAction,
			KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK)); // new
																		// JToggleButton("Animieren");
	public static final AbstractButton Neue_Ani = createMenuItem(
			"Neue Animation", "photo_add", 16, name, Neue_AniAction,
			KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));
	public static final AbstractButton Neue_Scene = createMenuItem(
			"Neue Scene", "picture_add", 16, name, Neue_SceneAction,
			KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK));
	public static final AbstractButton delete_Ani = createMenuItem(
			"Animation löschen", "photo_delete", 16, name, delete_AniAction,
			KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
					InputEvent.CTRL_DOWN_MASK));
	public static final AbstractButton delete_Scene = createMenuItem(
			"Scene löschen", "picture_delete", 16, name,
			delete_SceneActionListener,
			KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
	public static final AbstractButton save = createMenuItem("Sichern", "disk",
			16, name, saveAction,
			KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
	public static final AbstractButton rasterb = createMenuItem("Raster",
			"grid", 16, name, rasterbAction,
			KeyStroke.getKeyStroke('1', InputEvent.CTRL_DOWN_MASK));
	public static final AbstractButton tree = createMenuItem("Tree",
			"user_add", 16, name, treeAction,
			KeyStroke.getKeyStroke('2', InputEvent.CTRL_DOWN_MASK));
	private static ArrayList<String> s = new ArrayList<>();
	static {
		// FIXME
		s.add(".png");
		createNormComp(Animationindex);
		createNormComp(Scenenindex);
	}
}
