package de.game.settings;

import static game.database.XMLCObject.*;

import java.io.File;
import java.util.HashMap;

import javax.swing.JOptionPane;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.game.objects.CObject;
import game.database.XML;

/**
 * import static database.XMLCObject.*;
*/
public class GameDatabase extends XML {
	//Integer
	String keyString = "k";
	String valueString = "v";
	HashMap<String, Integer> integerList = new HashMap<>();
	HashMap<String, Float> floatList = new HashMap<>();
	HashMap<String, Boolean> boolList = new HashMap<>();
	HashMap<String, CObject> spirteList = new HashMap<>();
	public GameDatabase(File f) {
		read(f);
	}

	@Override
	public void fileDoesNotExist() {
		int i = JOptionPane.showConfirmDialog(null,
				"Wollen sie eine wirklich eine neue Datenbank erstellen");
		if(i == 1){
			//Construktor
		}
	}

	public Float getFloat(String s) {
		return floatList.get(s);
	}
	public HashMap<String, Float> getFloatList() {
		return floatList;
	}
	public void setFloat(String key, float f){
			floatList.put(key, f);
	}
	public Boolean getBoolean(String s) {
		return boolList.get(s);
	}
	public HashMap<String, Boolean> getBooleanList() {
		return boolList;
	}
	public void setBoolean(String key, boolean f){
			boolList.put(key, f);
	}
	public Integer getInteger(String s) {
		return integerList.get(s);
	}
	public void setInteger(String key, int i){
		integerList.put(key, i);
	}

	public HashMap<String, Integer> getIntegerList() {
		return integerList;
	}

	public CObject getSpirte(String s) {
		return spirteList.get(s);
	}

	public HashMap<String, CObject> getSpirteList() {
		return spirteList;
	}
	public void setCObject(String key, CObject i){
		spirteList.put(key, i);
	}
	@Override
	public String getXMLName() {
		return "database";
	}
	@Override
	public void read(Element rootElement) {
		try {
			DisplayMode.setName(rootElement.getAttribute("gameName"));
			DisplayMode.setWidht(new Integer(rootElement.getAttribute("widht")));
			DisplayMode.setHeight(new Integer(rootElement.getAttribute("height")));
			DisplayMode.setFPS(new Integer(rootElement.getAttribute("fps")));
			DisplayMode.setFullscreen(new Boolean(rootElement.getAttribute("fullscreen")));
			DisplayMode.setVsync(new Boolean(rootElement.getAttribute("vsync")));
			DisplayMode.setClearEachFrame(new Boolean(rootElement.getAttribute("clearEachFrame")));
//			DisplayMode.version = Integer.parseInt(rootElement.getAttribute("version"));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		}
		NodeList n = rootElement.getElementsByTagName("*");
		for (int i = 0; i < n.getLength(); i++) {
			Element e = (Element) n.item(i);
			if(!e.hasAttribute(keyString)){
				continue;
			}
			//int
			if(e.getNodeName().endsWith("int")){
				Integer value = Integer.parseInt(e.getAttribute(valueString));
				String key = e.getAttribute(keyString);
				integerList.put(key, value);
				continue;
			}
			if(e.getNodeName().endsWith("float")){
				Float value = Float.parseFloat(e.getAttribute(valueString));
				String key = e.getAttribute(keyString);
				floatList.put(key, value);
				continue;
			}
			if(e.getNodeName().endsWith("bool")){
				Boolean value = e.getAttribute(valueString).equals("true");
				String key = e.getAttribute(keyString);
				boolList.put(key, value);
				continue;
			}
			if(e.getNodeName().endsWith("obj")){
				NodeList list = e.getElementsByTagName("*");
				for(int j = 0; j < list.getLength(); j++){
				Element object = (Element) list.item(j);
				CObject c = loadCObject(object,null);
				String key = object.getAttribute("o-key");
				spirteList.put(key,c);
				}
				continue;
			}
			//erweitern
		}
	}

	public void saveList(Element rootElement, Document xmlDoc,HashMap<String,?> list,String name){
		for(String i : list.keySet()){
			Element e = xmlDoc.createElement(name);
			e.setAttribute(keyString, i);
			e.setAttribute(valueString,list.get(i)+"");
			rootElement.appendChild(e);
		}
	}

	@Override
	public void write(Element rootElement, Document xmlDoc) {
//		//int
//		saveList(rootElement, xmlDoc, integerList, "int");
//		//float
//		saveList(rootElement, xmlDoc, floatList, "float");
//		//boolean
//		saveList(rootElement, xmlDoc, boolList, "bool");
//		//Object
//		Element e = xmlDoc.createElement("obj");
//		e.setAttribute(keyString, "Default");
//		for(String i : spirteList.keySet()){
//			saveCObject(e, xmlDoc, spirteList.get(i)).setAttribute("o-key", i);
//		}
//		rootElement.appendChild(e);
	}
	
}
