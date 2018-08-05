package toolbars;

import static toolbars.Toolbar.createMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import editor.tileMap.TileMapEditor;
import editor.tileMap.objects.TileSet;
import gui.AAufbau;
import gui.DefaultTree;

public class DialogTiledMap {
	public static String name = "TileMap";
	private static ArrayList<String> s = new ArrayList<>();
	public static void init(){
		s.add(TileSet.fileEnding());
		//add TileSet
		createMenuItem("add TileSet","small_tiles", 16, name, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTree.getDefaultTree(s,null);
			}
		});
		//clear TileSet
		createMenuItem("clear unused TileSet","small_tiles_remove", 0, name, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((TileMapEditor)AAufbau.f.CurrendPanel).clearUnusedTileSet();
			}
		});
	}
}
