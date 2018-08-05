package editor.tileMap;

import editor.DefaultEditorPanel;
import editor.DefaultEditorPanel.Matrix;
import editor.tileMap.objects.TileSet;
import editor.tileMap.objects.TiledMap;
import editor.tileMap.objects.TiledMap.SecretAccess;
import gui.Drop;
import interfaces.Destroyer;
import interfaces.ExtendsDefaultEditor;
import interfaces.Save;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class TileMapEditor extends JPanel implements ExtendsDefaultEditor,Save,Drop {

	DefaultEditorPanel panel;
	TiledMap tile;
	File f;
	public TileSetPanel tileSet;
	public TileLayerPanel tileLayer;
	public TileMapEditor(File f) {
		this.f = f;
		panel = new DefaultEditorPanel();
		SecretAccess[] a = new SecretAccess[1];
		tile = new TiledMap(f,a);

		setLayout(new BorderLayout());
		tileSet = new TileSetPanel(tile);
		TileRenderer render = new TileRenderer(tile, panel.m);
		tileLayer = new TileLayerPanel(tile,a[0], render, panel.m);
		getMatrix().setMainTool(render);
		getMatrix().passiveTools.add(new TileChanceTool(tile, a[0],tileSet));
		panel.setShowFPS(false);
		new Thread(){
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pack();
			};
		}.start();
		panel.add(tileSet);
		panel.add(tileLayer);
		add(panel);
	}
	public void pack(){
		int widht = 300;
		tileSet.setSize(widht, getHeight());
		tileSet.setLocation(getWidth()-widht, 0);
		tileSet.setVisible(true);
//		tileLayer.pack();
		tileLayer.setSize(175, 400);
		tileLayer.setLocation(0, 0);
		tileLayer.setVisible(true);
	}
	@Override
	public void destroy() {
		Destroyer.destroyObject(panel);
		panel = null;
	}

	@Override
	public Matrix getMatrix() {
		return panel.m;
	}

	@Override
	public File getFile() {
		return f;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
	@Override
	public void save() {
		tile.save();
	}
	@Override
	public boolean dropEvent(File f) {
		if(f.exists()){
			if(f.getName().toLowerCase().endsWith(TileSet.fileEnding())){
				
				tileSet.addTileSet(f);
				return false;
			}
		}
		return true;
	}
	public void clearUnusedTileSet() {
		tile.clearUnusedTileSet(tileSet);
	}
}
