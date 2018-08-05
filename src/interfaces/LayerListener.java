package interfaces;


import editor.CObject;
import editor.Layer;

public interface LayerListener {
	public void addToLayerEvent(Layer l, CObject e);
	public void removedToLayerEvent(Layer l, CObject c);
}
