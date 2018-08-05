package basic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import editor.css.Sp_Dasharray;
import editor.css.SpecialEditObject;
import interfaces.HasName;
import interfaces.NameChangeListener;

public class CSSClasse implements HasName{
	private TreeMap<String, String> bufferString = new TreeMap<>();
	private TreeMap<String, Number> bufferFloat = new TreeMap<>();
	private TreeMap<String, Color> bufferColor = new TreeMap<>();
	private TreeMap<String, SpecialEditObject> bufferObject = new TreeMap<>();
	private static TreeMap<String, Integer> typMap = new TreeMap<>();
	private static TreeMap<String, Class<? extends SpecialEditObject>> typObjectMap = new TreeMap<>();
	static{
		typObjectMap.put("stroke-dasharray",Sp_Dasharray.class);
	}
	public CSSClasse(String name) {
		setName(name);
	}
	
	public CSSClasse(Element cssClass) {
		this(cssClass.getAttribute("id"));
		cssClass.removeAttribute("id");
		setAttributeByElement(cssClass);
	}

	public void addAttribute(String key, String value) {
		int typ = -1;
		if(typMap.containsKey(key)){
			typ = typMap.get(key);
		}
		
		if(typ == STRING || typ == -1){
			if(typ == -1){
				System.out.println("CSSClass: Unbekannter SVG Tag: " + key);
			}
			bufferString.put(key, value);
		}else if(typ == NUMBER){
			bufferFloat.put(key, Float.parseFloat(value));
		}else if(typ == COLOR){
			bufferColor.put(key, decodeColor(value));
		}else if(typ == OBJECT){
			bufferObject.put(key,creatValueAsObject(key,value));
		}
				
	}
	
	/**TODO*/
	/**String*/
	public boolean containsKeyAsString(String key) {
		return typMap.containsKey(key);
	}
	public TreeSet<String> getKeys(){
		TreeSet<String> key = new TreeSet<>();
		key.addAll(bufferString.keySet());
		key.addAll(bufferFloat.keySet());
		key.addAll(bufferColor.keySet());
		key.addAll(bufferObject.keySet());
		return key;
		
	}
	public String getValueAsString(String key) {
		try{
		int typ = typMap.get(key);
		if(typ == STRING || typ == -1){
			return bufferString.get(key);
		}else if(typ == NUMBER){
			return "" + bufferFloat.get(key);
		}else if(typ == COLOR){
			return codeColor(bufferColor.get(key));
		}else if(typ == OBJECT){
			return bufferObject.get(key).toString();
		}else{
			return bufferString.get(key);
		}
		}catch(Exception e){
			System.out.println("Key does not exist");
			System.out.println(key);
			e.printStackTrace();
			return "";
		}
	}
	public void setAttributeString(String key, String value) {
		bufferString.put(key, value);
	}

	/**Color*/
	public boolean containsKeyAsColor(String key) {
		return bufferColor.containsKey(key);
	}

	public Color getValueAsColor(String key) {
		return bufferColor.get(key);
	}
	public void setAttributeColor(String key, Color c) {
		bufferColor.put(key, c);
	}
	/**Number*/
	public boolean containsKeyAsNumber(String key) {
		return bufferFloat.containsKey(key);
	}

	public Number getValueAsNumber(String key) {
		return bufferFloat.get(key);
	}
	public void setAttributeNumber(String key, Number value) {
		bufferFloat.put(key, value);
	}
	/**SpecialEditObject*/
	public boolean containsKeyAsObject(String key) {
		return bufferObject.containsKey(key);
	}

	public SpecialEditObject getValueAsObject(String key) {
		return bufferObject.get(key);
	}
	public SpecialEditObject creatValueAsObject(String key, String value) {
		try {
			SpecialEditObject obj = typObjectMap.get(key).newInstance();
			return obj.set(value);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void setAttributeObject(String key, SpecialEditObject c) {
//		System.out.println(key);
		bufferObject.put(key, c);
	}
	/**STATIC AREA!*/
	/** String in color parsen*/
	private static Pattern pattern = 
			Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
	static private HashMap<String, Color> colorMap;
	static private HashMap<Color, String> colorInvertMap;
	public static String getColorName(Color c) {
		return colorInvertMap.get(c);
	}
	public static Color decodeColor(String input) {
		
		if (input.isEmpty()||input.equals("none")) {
			return null;
		}
		Matcher m = pattern.matcher(input);
		//init
		if(colorMap == null){
			colorMap = new HashMap<>(200);
			colorInvertMap = new HashMap<>(200);
			try {
				Scanner scann = new Scanner(new Object().getClass()
						.getResourceAsStream("/basic/css-color"));
				while(scann.hasNextLine()){
					String[] s = scann.nextLine().split("\t");
					String name = s[0].toLowerCase();
					Color c = decodeColor(s[1]);
					colorMap.put(name, c);
					colorInvertMap.put(c, name);
				}
				scann.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		//check
		if (m.matches()) {
			return new Color(Integer.valueOf(m.group(1)), // r
					Integer.valueOf(m.group(2)), // g
					Integer.valueOf(m.group(3))); // b
		}else if(colorMap.containsKey(input.toLowerCase())){
			return colorMap.get(input.toLowerCase());
		} else {
			try {
				return Color.decode(input);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static String codeColor(Color c) {
		if(c == null){
			return "none";
		}
		if(colorInvertMap.containsKey(c)){
			return colorInvertMap.get(c);
		}
		return "rgb(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue()
				+ ")";
	}
	/**Typ casting*/
	public static final int STRING = 0;
	public static final int NUMBER = 1;
	public static final int COLOR = 2;
	public static final int OBJECT = 3;
	static{
		try {
			Scanner scann = new Scanner(new Object().getClass()
					.getResourceAsStream("/basic/css-typs"));
			while(scann.hasNextLine()){
				String[] s = scann.nextLine().split("\t");
				if(s[0].equals("//ENDE")){
					break;
				}
				int i = STRING;
				if(s[1].equals("String")){
					i = STRING;
				}else if(s[1].equals("Number")){
					i = NUMBER;
				}else if(s[1].equals("Color")){
					i = COLOR;
				}else if(s[1].equals("Object")){
					i = OBJECT;
				}else{
					System.err.println(s[0] +"\t"+ s[1] + "\nCSSClasse: Unbekannter Typ");
				}
				typMap.put(s[0], i);
			}
			scann.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	String name;
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String s) {
		for(NameChangeListener list : nameChangeListeners){
			list.nameChance(this.name, s);
		}
		name = s;
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

	@Override
	public Class<?> getGameClass() {
		return null;
	}

	public void remove(String key) {
		bufferString.remove(key); 
		bufferFloat.remove(key); 
		bufferColor.remove(key); 
		bufferObject.remove(key);
	}

	public CSSClasse clone1() {
		CSSClasse c = new CSSClasse(getName());
		c.bufferColor.putAll(bufferColor);
		c.bufferFloat.putAll(bufferFloat);
		c.bufferString.putAll(bufferString);
		c.bufferObject.putAll(bufferObject);;
		return c;
	}

	//XML Element zu privateclass hinzufügen
		public void setAttributeByElement(Element element) {
			NamedNodeMap map = element.getAttributes();
			for(int i = 0; i != map.getLength(); i+=1){
				org.w3c.dom.Node n = map.item(i);
				addAttribute(n.getNodeName(), n.getNodeValue());
			}
		}
		public void save(Element rootObject) {
			for(String key : getKeys()){
//				if(rootObject.getAttribute(key).isEmpty()){
//					continue;
//				}
				rootObject.setAttribute(key, getValueAsString(key));
			}
		}
}

