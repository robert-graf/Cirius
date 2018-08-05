package editor.tileMap;

import editor.DefaultEditorPanel.Matrix;

@SuppressWarnings("serial")
public class TileLayer extends editor.Layer {
	public TileLayer(Layer l, Matrix m) {
		super(l.name, m);
		this.l = l;
	}

	Layer l;

	@Override
	public boolean equals(Object o) {
		if(o instanceof TileLayer){
			return l == ((TileLayer) o).l;
		}
		return false;
	}
}
