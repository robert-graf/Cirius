package database.game;

import static database.XMLCObject.loadCObject;
import static database.XMLCObject.saveCObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import database.XML;
import editor.CObject;

/**
 * import static database.XMLCObject.*;
 */
public class GameDatabase extends XML {
	// Integer
	int version = 1;
	String keyString = "k";
	String valueString = "v";
	HashMap<String, Integer> integerList = new HashMap<>();
	HashMap<String, Float> floatList = new HashMap<>();
	HashMap<String, Boolean> boolList = new HashMap<>();
	HashMap<String, CObject> spirteList = new HashMap<>();
	HashMap<String,Object> settings = new HashMap<>();
	ArrayList<OldReferenz> oldRefList = new ArrayList<OldReferenz>();
	public GameDatabase(File f) {
		read(f);
	}

	@Override
	public void fileDoesNotExist() {
		setName("Unnamed");
		setWidht("800");
		setHeight("600");
		setFps("90");
		setFullscreen("" + false);
		setVsync("" + true);
		setClearEachFrame("" + true);
	}

	public Float getFloat(String s) {
		return floatList.get(s);
	}

	public HashMap<String, Float> getFloatList() {
		return floatList;
	}

	public void setFloat(String key, float f) {
		floatList.put(key, f);
	}

	public Boolean getBoolean(String s) {
		return boolList.get(s);
	}

	public HashMap<String, Boolean> getBooleanList() {
		return boolList;
	}

	public void setBoolean(String key, boolean f) {
		boolList.put(key, f);
	}

	public Integer getInteger(String s) {
		return integerList.get(s);
	}

	public void setInteger(String key, int i) {
		integerList.put(key, i);
	}

	public HashMap<String, Integer> getIntegerList() {
		return integerList;
	}

	public CObject getObject(String s) {
		return spirteList.get(s);
	}

	public HashMap<String, CObject> getSpirteList() {
		return spirteList;
	}

	public void setCObject(String key, CObject i) {
		spirteList.put(key, i);
	}

	@Override
	public String getXMLName() {
		return getXMLNameStatic();
	}
	public static String getXMLNameStatic() {
		return "database";
	}
	
	public String getGameName() {
		return (String) settings.get("name");
	}

	public int getWidht() {
		return (Integer) settings.get("widht");
	}

	public int getHeight() {
		return (Integer) settings.get("height");
	}

	public int getFps() {
		return (Integer) settings.get("fps");
	}

	public boolean isFullscreen() {
		return (Boolean) settings.get("fullscreen");
	}

	public boolean isVsync() {
		return (Boolean) settings.get("vsync");
	}

	public boolean isClearEachFrame() {
		return (Boolean) settings.get("clearEachFrame");
	}

	public void setName(String name) {
		settings.put("name", name);
	}

	public void setWidht(String widht) {
		settings.put("widht", new Integer(widht));
	}

	public void setHeight(String height) {
		settings.put("height", new Integer(height));
	}

	public void setFps(String fps) {
		settings.put("fps", new Integer(fps));
	}

	public void setFullscreen(String fullscreen) {
		settings.put("fullscreen", new Boolean(fullscreen));
	}

	public void setVsync(String string) {
		settings.put("vsync", new Boolean(string));
	}

	public void setClearEachFrame(String clearEachFrame) {
		settings.put("clearEachFrame", new Boolean(clearEachFrame));
		
	}
	String[] ignorList = { "Sprite" };
	

	@Override
	public void read(Element rootElement) {
		try {
			setName(rootElement.getAttribute("gameName"));
			setWidht(rootElement.getAttribute("widht"));
			setHeight(rootElement.getAttribute("height"));
			setFps(rootElement.getAttribute("fps"));
			setFullscreen(rootElement.getAttribute("fullscreen"));
			setVsync(rootElement.getAttribute("vsync"));
			setClearEachFrame(rootElement.getAttribute("clearEachFrame"));
			version = Integer.parseInt(rootElement.getAttribute("version"));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		}
		NodeList n = rootElement.getElementsByTagName("*");
		for (int i = 0; i < n.getLength(); i++) {
			Element e = (Element) n.item(i);
			if (!e.hasAttribute(keyString) && !e.getNodeName().equals("oldRef")) {
				if (e.getNodeName().equals("Sprite")) {
					continue;
				}

				System.out.println(e.getNodeName()
						+ " ist GameDatabase unbekannt.");
				continue;
			}
			// int
			if (e.getNodeName().endsWith("int")) {
				Integer value = Integer.parseInt(e.getAttribute(valueString));
				String key = e.getAttribute(keyString);
				integerList.put(key, value);
				continue;
			}
			if (e.getNodeName().endsWith("float")) {
				Float value = Float.parseFloat(e.getAttribute(valueString));
				String key = e.getAttribute(keyString);
				floatList.put(key, value);
				continue;
			}
			if (e.getNodeName().endsWith("bool")) {
				Boolean value = e.getAttribute(valueString).equals("true");
				String key = e.getAttribute(keyString);
				boolList.put(key, value);
				continue;
			}
			if (e.getNodeName().endsWith("oldRef")) {
				String key = e.getAttribute("key");
				String keyNeu = e.getAttribute("keyNeu");
				int typ = Integer.parseInt(e.getAttribute("typ"));
				float version = Float.parseFloat(e.getAttribute("version"));
				oldRefList.add(new OldReferenz(key, keyNeu, typ, version));
				continue;
			}
			if (e.getNodeName().endsWith("obj")) {
				NodeList list = e.getElementsByTagName("*");
				for (int j = 0; j < list.getLength(); j++) {
					Element object = (Element) list.item(j);
					CObject c = loadCObject(object, null,null,null);
					String key = object.getAttribute("o-key");
					spirteList.put(key, c);
				}
				continue;
			}
			System.out.println(e.getNodeName());
			// erweitern
		}
	}

	public void renameKey(String alt, String neu, int typ) {
		HashMap<String, Object> map = getMap(typ);
		Object object = map.get(alt);
		map.remove(alt);
		map.put(neu, object);
		oldRefList.add(new OldReferenz(alt, neu, typ, getVersion()));
		version += 1;
	}

	@Override
	public void write(Element rootElement, Document xmlDoc) {
		rootElement.setAttribute("version", version + "");
		rootElement.setAttribute("gameName",  getGameName() + "");
		rootElement.setAttribute("widht", getWidht()+"");
		rootElement.setAttribute("height",getHeight() + "");
		rootElement.setAttribute("fps",getFps()+ "");
		rootElement.setAttribute("fullscreen",isFullscreen()+ "");
		rootElement.setAttribute("vsync",isVsync()+ "");
		rootElement.setAttribute("clearEachFrame",isClearEachFrame()+ "");
		// int
		saveList(rootElement, xmlDoc, integerList, "int");
		// float
		saveList(rootElement, xmlDoc, floatList, "float");
		// boolean
		saveList(rootElement, xmlDoc, boolList, "bool");
		// Link (alte keys)
		kickOldReferenz();
		for (OldReferenz link : oldRefList) {
			if (getMap(link.typ).containsKey(link.key)) {
				continue;
			}

			Element e = xmlDoc.createElement("oldRef");
			e.setAttribute("key", link.key);
			e.setAttribute("keyNeu", link.keyNeu);
			e.setAttribute("version", link.version + "");
			e.setAttribute("typ", link.typ + "");
			rootElement.appendChild(e);
		}
		// Object
		Element e = xmlDoc.createElement("obj");
		e.setAttribute(keyString, "Default");
		for (String i : spirteList.keySet()) {
			saveCObject(e, xmlDoc, spirteList.get(i),false).setAttribute("o-key", i);
		}
		rootElement.appendChild(e);
	}

	private void kickOldReferenz() {
		ArrayList<OldReferenz> kick = new ArrayList<>();
		for (OldReferenz link : oldRefList) {
			for (OldReferenz link2 : oldRefList) {
				if (link.key.equals(link2.key)) {
					if (link.version < link2.version) {
						kick.add(link);
					} else if (link.version > link2.version) {
						kick.add(link2);
					}
				}
			}
		}
		oldRefList.removeAll(kick);
	}

	public void saveList(Element rootElement, Document xmlDoc,
			HashMap<String, ?> list, String name) {
		for (String i : list.keySet()) {
			Element e = xmlDoc.createElement(name);
			e.setAttribute(keyString, i);
			e.setAttribute(valueString, list.get(i) + "");
			rootElement.appendChild(e);
		}
	}

	public int getVersion() {
		return version;
	}

	public final static int SPRITE = 0, INT = 1, FLOAT = 2, BOOL = 3;

	public boolean isKeyOld(String key, int typ, float version) {
		for (OldReferenz link : oldRefList) {
			if (link.typ == typ && link.key.equals(key)
			/* && link.version < version */) {

				return true;
			}
		}
		return false;
	}

	public String getNewKey(String key, int typ, float version) {
		for (OldReferenz link : oldRefList) {
			if (link.typ == typ && link.key.equals(key)
			/* && link.version < version */) {
				return link.keyNeu;
			}
		}
		return null;
	}

	private class OldReferenz {
		String key;
		String keyNeu;
		int typ;
		float version;

		public OldReferenz(String key, String keyNeu, int typ, float version) {
			this.key = key;
			this.keyNeu = keyNeu;
			this.typ = typ;
			this.version = version;
		}
	}

	public int getTyp(HashMap<String, Object> map) {
		if (map.equals(getSpirteList())) {
			return SPRITE;
		}
		if (map.equals(getBooleanList())) {
			return BOOL;
		}
		if (map.equals(getIntegerList())) {
			return INT;
		}
		if (map.equals(getFloatList())) {
			return FLOAT;
		}
		return -1;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap<String, Object> getMap(int typ) {
		if (typ == SPRITE) {
			return (HashMap) getSpirteList();
		}
		if (typ == BOOL) {
			return (HashMap) getBooleanList();
		}
		if (typ == INT) {
			return (HashMap) getIntegerList();
		}
		if (typ == FLOAT) {
			return (HashMap) getFloatList();
		}
		return null;
	}

	public void bereinigen() {
		oldRefList.clear();
	}
}
