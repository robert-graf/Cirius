package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Formatter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;


import editor.rpg.GUIMapEditStart;
import editor.tileMap.TileMapEditor;
import editor.tileSet.TileSetEditor;
import interfaces.FileHolder;

public class Öffnen {

	static void InitAndOpen(File f) {
		Open(f, null);
	}

	static JLabel vorschau;
	static JPanel p;
	static JFileChooser öffnendialog;

	static public void Open(final File f, MouseEvent e) {

		// SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRITE-SPRIT-
		if (AAufbau.f.OpenFileList.containsKey(f)) {
			AAufbau.f.setSelectedPanel(f);
		} else if (f.getName().toLowerCase().endsWith(".das")) {
			SpriteEinstellen i = new SpriteEinstellen(f);
			i.setName(f.getName());
			AAufbau.addOpenFile(i, i);
		}
		// ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-ORDNER-
		else if (f.isDirectory()) {
			if (e != null && e.isShiftDown()) {
				new Thread(new Runnable() {
					public void run() {
						try {
							Runtime.getRuntime().exec(
									"explorer.exe " + f.getPath());
						} catch (IOException e) {
						}
					}
				}).start();
			}
		}
		// BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-BILD-
		else if (f.getName().toLowerCase().endsWith(".png") && !f.exists()) {
			// FIXME
			File pfad = getImageDialog();
			if (pfad == null) {
				return;
			}
			BildUmspeichern(pfad, f);
		} else if (f.getName().toLowerCase().endsWith(".png")) {
			JPanel i = new JPanel();
			i.setName(f.getName());
			JLabel jl = new JLabel();
			jl.setIcon(new ImageIcon(f.getAbsolutePath()));
			i.setLayout(new BorderLayout());
			i.add(new JScrollPane(jl));
			AAufbau.addOpenFile(i, new FileHolder() {
				public File getFile() {
					return f;
				}
			});
		}
		// -RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--RPG--
		else if (f.getName().toLowerCase().endsWith("rpg")) {
			GUIMapEditStart i = new GUIMapEditStart(f);
			i.setName(f.getName());
			AAufbau.addOpenFile(i, i);
		}
		// -TSX-Tileset-TSX-Tileset-TSX-Tileset-TSX-Tileset-TSX-Tileset-TSX-Tileset-TSX-Tileset-TSX-Tileset-TSX-Tileset-TSX-
		else if (f.getName().toLowerCase().endsWith("xts")) {
			TileSetEditor i = new TileSetEditor(f);
			i.setName(f.getName());
			AAufbau.addOpenFile(i, i);
			// -TMX-Tilemap-TMX-Tilemap-TMX-Tilemap-TMX-Tilemap-TMX-Tilemap-TMX-Tilemap-TMX-Tilemap-TMX-Tilemap-TMX-Tilemap
		} else if (f.getName().toLowerCase().endsWith("tmx")) {
			TileMapEditor i = new TileMapEditor(f);
			i.setName(f.getName());
			AAufbau.addOpenFile(i, i);
			// -database-database-database-database-database-database-database-database-database-database-database-database
		} else if (f.getName().toLowerCase().endsWith(".database")) {
			database.game.DataBaseEditor i = new database.game.DataBaseEditor(f);
			i.setName("Globale Datenbank");
			AAufbau.addOpenFile(i, i);
			// -fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt-fnt
		} else if (f.getName().toLowerCase().endsWith(".fnt")) {
			try {
				File open = new File("open.tmp");
				Formatter formatter = new Formatter(open);
				formatter.format(f.getAbsolutePath() + "\n \n");
				formatter.close();
				open.deleteOnExit();
				Runtime.getRuntime().exec(
						"java -jar lib\\jars\\Hiero.jar");//+f.getAbsoluteFile());// -classpath slick.jar
			} catch (IOException ex) {
				ex.printStackTrace();
				//java -jar Hiero.jar -classpath slick.jar
			}
		
		}else if(f.getName().toLowerCase().endsWith(".pesx")){
			JOptionPane.showMessageDialog(null, "Particlesystemes are not Supportet");			
		}
		else if (!f.exists()) {
			JOptionPane.showMessageDialog(null, "Datei existiert nicht.");
		} else {
			JOptionPane.showMessageDialog(null, f.getName()
					+ " hat einen nicht unterstützten Sufix");
		}
	}// end

	static File backup;

	public static File getImageDialog() {
		if (öffnendialog == null) {
			initImageDialog();
		}
		öffnendialog.setSelectedFile(Tree.selected);
		if (öffnendialog.showSaveDialog(öffnendialog) == JFileChooser.APPROVE_OPTION) {
			return öffnendialog.getSelectedFile();
		}
		System.out.println(öffnendialog.getSelectedFile());
		return null;
	}

	private static void initImageDialog() {
		öffnendialog = new JFileChooser();
		
		öffnendialog.setPreferredSize(GraphicsEnvironment
				.getLocalGraphicsEnvironment().getMaximumWindowBounds()
				.getSize());
		öffnendialog.setPreferredSize(new Dimension(öffnendialog
				.getPreferredSize().width - 50,
				öffnendialog.getPreferredSize().height - 50));
		// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		öffnendialog.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory()
						|| f.getName().toLowerCase().endsWith(".png")
						|| f.getName().toLowerCase().endsWith(".gif")
						|| f.getName().toLowerCase().endsWith(".jpg");
			}

			public String getDescription() {
				return "Bilder (*.png , *.jpg, *.gif) .png empfolen!";
			}
		});
		// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		p = new JPanel();
		vorschau = new JLabel("NO IMAGE!");
		öffnendialog.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				try {
					vorschau.setIcon(new ImageIcon(e.getNewValue().toString()));
					vorschau.setText("");
					if (vorschau.getIcon().getIconWidth() > öffnendialog
							.getWidth() / 2) {
						p.setPreferredSize(new Dimension(öffnendialog
								.getWidth() - 450, 40));
					} else {
						p.setPreferredSize(new Dimension(vorschau.getIcon()
								.getIconWidth(), 40));
					}
				} catch (Exception ex) {
					vorschau.setIcon(null);
					vorschau.setText("NO IMAGE!");
					p.setPreferredSize(new Dimension(75, 75));
				}
			}
		});
		p.add(vorschau);
		öffnendialog.setAccessory(p);
		
	}

	public static void BildUmspeichern(File pfad, File f) {
		if (pfad == null) {
			return;
		}
		// Bild umspeichern
		BufferedImage Temp = null;
		try {
			Temp = ImageIO.read(pfad);
			if (f.getName().equals(".png")) {
				f = new File(f.getPath().replace("\\.png", "") + "\\"
						+ pfad.getName());
			}
			ImageIO.write(Temp, "png", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}// class end

