package de.game.database;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XMLStream {
	public abstract void read(Element rootElement);
	public abstract void fileDoesNotExist();
	public abstract void write(Element rootElement, Document xmlDoc);
	public abstract String getXMLName();
}
