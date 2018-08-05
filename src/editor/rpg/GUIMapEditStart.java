package editor.rpg;

import database.MapXML;
import database.XMLCObject;
import editor.CObject;
import editor.DefaultEditorPanel;
import editor.DefaultEditorPanel.Matrix;
import editor.Layer;
import editor.DrawSettingsPanal;
import editor.css.CSSEditor;
import editor.rpg.script.ToolSetCurrendScript;
import editor.tools.GreifTool;
import editor.tools.SingleSelection;
import gui.Drop;
import interfaces.Destroyer;
import interfaces.ExtendsDefaultEditor;
import interfaces.LayerListener;
import interfaces.MatrixListener;
import interfaces.Save;
import interfaces.UndoObject;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;


import basic.CSSClasse;
import basic.ListenTreeList;

@SuppressWarnings("serial")
public class GUIMapEditStart extends JPanel implements ExtendsDefaultEditor,
		Save, Drop, MatrixListener, LayerListener {

	File f;
	public DefaultEditorPanel panel;
	private MapXML xml;
	public DrawSettingsPanal drawSettings = new DrawSettingsPanal();
	public GUIMapEditStart(File f) {
		this.f = f;
		setLayout(new BorderLayout());
		panel = new DefaultEditorPanel();
		Matrix m = panel.m;
		m.passiveTools.add(new SingleSelection());
		m.passiveTools.add(new dataBaseTool(f.getParentFile()));
		m.passiveTools.add(new ToolSetCurrendScript());
		m.addMatrixListener(this);
		
		xml = new MapXML(this);
		xml.read(f);
		xml.addListenerToLayer(this);
		add(panel);
		
		add(drawSettings, BorderLayout.SOUTH);
		
		// m.getLayer(0).add(new CRef(0,0,"res/Projekt/map/LeoVSBlue.rpg",
		// this), false);
		update();
	}

	public Matrix getMatrix() {
		if (panel == null)
			return null;
		return panel.m;
	}

	@Override
	public void destroy() {
		f = null;
		Destroyer.destroy(panel);
		panel = null;
		
	}

	@Override
	public void save() {
		xml.write();
	}

	@Override
	public File getFile() {
		return f;
	}

	@Override
	public boolean dropEvent(File f) {
		Matrix m = getMatrix();
		CObject object = XMLCObject.loadCObjectByFile(f, m.getX(null),
				m.getY(null));
		if (object == null) {
			return true;
		}
		GreifTool.item = object.getGreifItems().get(0);
		m.addCObject(0, object, false);
		m.addUndo(new Undo(object instanceof CTileMap ? Undo.ADDTILEMAP
				: Undo.ADDSPRITE, null, m.getSelectedLayer(), object));
		return false;
	}

	private static class Undo extends UndoObject {
		public static final int ADDSPRITE = -1;
		public static final int ADDTILEMAP = -2;
		private CObject obj;
		private Layer l;

		public Undo(int id, Object old, Layer neu, CObject obj) {
			super(id, old, neu, obj);
			this.obj = obj;
			l = neu;
		}

		@Override
		public void doRedo() {
			if (getID() == ADDSPRITE || getID() == ADDTILEMAP) {
				l.add(0, obj, false);
			}
		}

		@Override
		public void doUndo() {
			if (getID() == ADDSPRITE || getID() == ADDTILEMAP) {
				l.remove(obj, false);
			}
		}

		@Override
		public String getName() {
			if (getID() == ADDSPRITE) {
				return "Sprite einfügen";
			}
			if (getID() == ADDTILEMAP) {
				return "Tilemap einfügen";
			}
			return null;
		}

	}// ende undo
	public ListenTreeList<String, CSSClasse> CSSCList = new ListenTreeList<>();
	
	@Override
	public void update() {
		// TODO NEXUTSPANEL
		// NexusPanel.THAT.setFunktonList(list);
		CSSEditor.THAT.setCSSList(CSSCList);
//		ScriptSettings.THAT.setGlobalObject(global);
		 drawSettings.update();
	}

	@Override
	public void addToMatrixEvent(Matrix m, Layer l) {
		l.addLayerListener(this);
//		global.addDefaultVariable(new CObjectVar(new JavaPrototype(l.getGameClass()), l,global));
	}

	@Override
	public void removedToMatrixEvent(Matrix m, Layer l) {
		l.removeLayerListener(this);
//		if(global != null){
//			global.removeDefaultVariable(l.getName());
//		}
		
	}

	@Override
	public void addToLayerEvent(Layer l, CObject e) {
		l.addLayerListener(this);
//		global.addDefaultVariable(new CObjectVar(e.getScriptPrototyp(), e,global));
	}

	@Override
	public void removedToLayerEvent(Layer l, CObject c) {
		l.removeLayerListener(this);
//		global.removeDefaultVariable(c.getName());
	}

	@Override
	public void selectChacheEvent(Matrix m) {
		// TODO Auto-generated method stub
		
	}

}
