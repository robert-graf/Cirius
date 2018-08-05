package editor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;

import basic.CascadingStyleSheet;
import editor.DefaultEditorPanel.Matrix;
import interfaces.HasName;
import interfaces.LayerListener;
import interfaces.NameChangeListener;

@SuppressWarnings("serial")
public class Layer extends ArrayList<CObject> implements HasName {
	private static final Color fillDefault = new Color(100, 100, 100);
	private Matrix m;
	private String name = "NO NAME???";
	private boolean visible = true;
	private boolean solid = true;
	private float Faktor = 1;
	private CascadingStyleSheet css = new CascadingStyleSheet(this);

	// construktoren
	public Layer(String name, Matrix m) {
		this(name,1,true,true);
		setMatrix(m);
	}

	public Layer(String name, boolean visible, Matrix m) {
		this(name,1,visible,true);
		setMatrix(m);
	}

	public Layer(String name, float faktor, boolean visible, boolean solid) {
		setName(name);
		setFaktor(faktor);
		setVisible(visible);
		setSolid(solid);
		setMatrix(m);
		css.addAttribute("fill",fillDefault);
//		css.addAttribute("fill-opacity",0.4f);
		css.addAttribute("stroke", "Black");
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		for(NameChangeListener list : nameChangeListeners){
			list.nameChance(this.name, name);
		}
		if (m == null) {
			this.name = name;
			return;
		}
		this.name = GUI_Layerbefehle.getUniqueLayerName(name, this.name, m);
	}
	Vector<NameChangeListener> nameChangeListeners = new Vector<>();
	@Override
	public void addNameChangeListener(NameChangeListener list) {
		nameChangeListeners.add(list);
	}

	@Override
	public void removeNameChangeListener(NameChangeListener list) {
		nameChangeListeners.remove(list);
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public float getFaktor() {
		return Faktor;
	}

	public void setFaktor(float f) {
		Faktor = f;
	}

	/** OVERRIDE */
	@Override
	public boolean add(CObject e) {
		return add(e, true);
	}

	@Override
	public void add(int index, CObject e) {
		add(index, e, true);
	}

	public boolean add(CObject e, boolean undo) {
		
		if (undo) {
			getMatrix().addUndo(
					new UndoEdit(UndoEdit.HINZUFÜGEN, size(), this, e));
		}
		e.setMatrix(m);
		for(LayerListener listen : listener){
			listen.addToLayerEvent(this, e);
		}
		return super.add(e);
	}

	public void add(int index, CObject e, boolean undo) {
		
		if (undo) {
			getMatrix().addUndo(
					new UndoEdit(UndoEdit.HINZUFÜGEN, index, this, e));
		}
		e.setMatrix(m);
		super.add(index, e);
		for(LayerListener listen : listener){
			listen.addToLayerEvent(this, e);
		}
	}

	@Override
	public CObject remove(int index) {
		if(index < size()&& index != -1){
		getMatrix().addUndo(
				new UndoEdit(UndoEdit.ENTFERNEN, index, this, get(index)));
		for(LayerListener listen : listener){
			listen.removedToLayerEvent(this, super.get(index));
		}
		return super.remove(index);
		}
		return null;
	}

	public boolean remove(Object o, boolean b) {
		if (b) {
			getMatrix().addUndo(
					new UndoEdit(UndoEdit.ENTFERNEN, indexOf(o), this,
							(CObject) o));
		}
		for(LayerListener listen : listener){
			listen.removedToLayerEvent(this,(CObject) o);
		}
		return super.remove(o);
	}

	@Override
	public boolean remove(Object o) {
		return remove(o, true);
	}

	public Matrix getMatrix() {
		if (m == null) {
			m = GUI_Layerbefehle.getMatrix();
		}
		return m;
	}

	public void setMatrix(Matrix m) {
		this.m = m;
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Class<?> getGameClass() {
		return de.game.objects.Layer.class;
	}

	public CascadingStyleSheet getCSS() {
		return css;
	}

	public void setCss(CascadingStyleSheet css) {
		this.css = css;
	}
	Vector<LayerListener> listener = new Vector<>();
	public void addLayerListener(LayerListener lister){
		if(listener.contains(lister)){
			return;
		}
		listener.add(lister);
	}
	public void removeLayerListener(LayerListener lister){
		listener.remove(lister);
	}
	private static class UndoEdit extends interfaces.UndoObject {
		// TODO
		public static final int ENTFERNEN = Matrix.undoID++;
		public static final int HINZUFÜGEN = Matrix.undoID++;
		CObject o;
		Layer l;
		int index;

		public UndoEdit(int id, int index, Layer neu, CObject obj) {
			super(id, null, neu, obj);
			o = obj;
			l = neu;
			this.index = index;
		}

		@Override
		public void doRedo() {
			if (getID() == ENTFERNEN) {
				l.remove(o, false);
			} else if (getID() == HINZUFÜGEN) {
				l.add(index, o, false);
			}
		}

		@Override
		public void doUndo() {
			if (getID() == ENTFERNEN) {
				l.add(index, o, false);
			} else if (getID() == HINZUFÜGEN) {
				l.remove(o, false);
			}
		}

		@Override
		public String getName() {
			if (getID() == ENTFERNEN) {
				return "Entfernen";
			} else if (getID() == HINZUFÜGEN) {
				return "Hinzufügen " + obj.getClass().getSimpleName();
			}
			return null;
		}
	}
}
