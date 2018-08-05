package interfaces;

import java.util.ArrayList;

import editor.tabelle.OneLine;

public interface UserZugang {
	public ArrayList<OneLine<?>> getLines();

	public Object clone1() throws CloneNotSupportedException;

}
