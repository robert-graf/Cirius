package editor.tileSet;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import editor.DefaultEditorPanel;
import editor.DefaultEditorPanel.Matrix;
import editor.tileMap.objects.TileSet;
import gui.Tree;
import gui.Öffnen;
import interfaces.Destroyer;
import interfaces.ExtendsDefaultEditor;
import interfaces.Save;

@SuppressWarnings("serial")
public class TileSetEditor extends JPanel implements Save, ExtendsDefaultEditor {
	DefaultEditorPanel panel;
	TileSet tileSet;
	File f;
	JCheckBox clipping = new JCheckBox("Zeige Ränder",true);
	TileSetter ts;
	QuickTile qt;
	public TileSetEditor(File f) {
		try {
			if (f.exists() && new FileInputStream(f).available() != 0) {
				loadFile(f);
			} else {
				
				defaultFile(f);
			}
			setLayout(new BorderLayout());
			// add(new JLabel(new ImageIcon(tileMap.getImage())));
			panel = new DefaultEditorPanel();
			add(panel);
			new Thread(){
				public void run() {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pack();
				}
			}.start();
			ts = new TileSetter(this, tileSet);
			if(tileSet.qt == null){
				qt = new QuickTile(this, tileSet);	
			}else{
				qt = new QuickTile(this, tileSet,tileSet.qt);
			}
			
			
			getMatrix().passiveTools.add(ts);
			getMatrix().passiveTools.add(qt);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private void pack() {
		final TileSetEditor that = this;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					ts.pack(panel,that.getWidth()-200,0,200,250);
					qt.pack(panel,that.getWidth()-200,250,200,that.getHeight()-550,that.getWidth(),300);
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		};
	private void loadFile(File f) {
		tileSet = new TileSet(f);
		this.f = f;
	}

	File imagefile;

	private void defaultFile(File f) {
		try {
			if (imagefile == null) {
				imagefile = new File(f.getPath().replace(".tsx", ".png"));

				while (imagefile == null || !imagefile.exists()) {
					imagefile = Öffnen.getImageDialog();
					
				}
				if (f.getName().equals(".tsx")) {
					f = new File(f.getParent() + "/"
							+ imagefile.getName().replace(".png", ".tsx"));
				}
			}
			int i;
			try {
				i = Integer.parseInt(JOptionPane
						.showInputDialog("Tile größe in Pixel? (16,32)"));
			} catch (NumberFormatException e) {
				i = 16;
				e.printStackTrace();
			} catch (HeadlessException e) {
				i = 16;
				e.printStackTrace();
			}
			f.getParentFile().mkdirs();
//			f.createNewFile();
			this.f = f;
			// shauen ob bild bereit in resourc ist.
			File neuBild;
			if(imagefile.getAbsolutePath().startsWith(Tree.THAT.getFile().getAbsolutePath())){
				neuBild = imagefile;
			}else{
				neuBild = new File(f.getParent() + "/" + imagefile.getName());
				Öffnen.BildUmspeichern(imagefile, neuBild);
			}
			
			tileSet = new TileSet(neuBild,imagefile,i,i);
			save();
//			tileSet = new TileSetXML(neuBild, f.getName().replace(".tsx", ""),
//					i, i);
//			tileSet.setZiel(f);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	@Override
	public void save() {
		if(qt!=null)
		tileSet.qt = qt.getQuickTileList();
		
		tileSet.write(f);
		System.out.println("saved " + f);
	}

	@Override
	public Matrix getMatrix() {
		return panel.m;
	}

	@Override
	public void destroy() {
		f = null;
		imagefile = null;
		Destroyer.destroy(panel);
		panel = null;
		Destroyer.destroy(tileSet);
		tileSet = null;

	}

	@Override
	public File getFile() {
		return f;
	}

	@Override
	public void update() {
		
	}

	public boolean isShowClipping() {
		return clipping.isSelected();
	}

}
