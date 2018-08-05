package interfaces;

public interface Undo {
	public void doRedo();

	public void doUndo();

	public String getName();

	public int getID();

	public Object getObject();
}
