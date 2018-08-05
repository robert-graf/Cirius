package de.game.database;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 * Macht schnell aus ein File einen Element (root)
 * 
 * */
public class XMLRoot extends XML{
	File f;
	public XMLRoot(File f) {
		this.f = f;
		read(f);
	}
	Element rootElement;
	public Element getRoot(){
		return rootElement;
	}
	@Override
	public void read(Element rootElement) {
		this.rootElement = rootElement;
	}
	public void write(Element rootElement, Document xmlDoc) {}
	public String getXMLName() {return null;}
	public void fileDoesNotExist() {}

}
