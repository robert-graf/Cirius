package interfaces;

public interface HasName {
	public String getName();

	public void setName(String s);

	public abstract Class<?> getGameClass();
	
	public void addNameChangeListener(NameChangeListener list);
	
	public void removeNameChangeListener(NameChangeListener list);
}
