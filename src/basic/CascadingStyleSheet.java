package basic;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import editor.css.Sp_Dasharray;
import editor.css.SpecialEditObject;
import interfaces.HasName;


public class CascadingStyleSheet{
	private CascadingStyleSheet parent = null;
	
	private CSSClasse privateClass = new CSSClasse(""){
		public String getName() {
			return "CSS of " + obj.toString();
		};
	};
	ArrayList<CSSClasse> classes = new ArrayList<>();
	public void addAttribute(String key, String value) {
		privateClass.addAttribute(key, value);
	}
	public void addAttribute(String key, Color c) {
		privateClass.setAttributeColor(key, c);
	}
	public void addAttribute(String key, float g) {
		privateClass.setAttributeNumber(key, g);
	}
	HasName obj;
	public CascadingStyleSheet(HasName obj) {
		this.obj = obj;
	}
//	public void setStyle(String style) {
//		if (style.isEmpty()) {
//			return;
//		}
//		String[] s = style.split(";");
//		for (String tag : s) {
//			try {
//				if (tag.isEmpty()) {
//					continue;
//				}
////				System.out.println(tag);
//				String[] s2 = tag.split(":");
//				styleMap.put(s2[0], s2[1]);
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}
//	}

//	public String getStyle() {
//		String s = "";
//		for (String key : styleMap.keySet()) {
//			s += key + ":" + styleMap.get(key) + ";";
//		}
//		return s;
//	}

	/*
	 * The following properties are shared between CSS2 and SVG. Most of these
	 * properties are also defined in XSL:
	 * 
	 * Font properties: ‘font’ ‘font-family’ ‘font-size’ ‘font-size-adjust’
	 * ‘font-stretch’ ‘font-style’ ‘font-variant’ ‘font-weight’
	 * 
	 * Text properties: ‘direction’ ‘letter-spacing’ ‘text-decoration’
	 * ‘unicode-bidi’ ‘word-spacing’
	 * 
	 * Other properties for visual media: ‘clip’, only applicable to outermost
	 * svg element. ‘color’, used to provide a potential indirect value
	 * (currentColor) for the ‘fill’, ‘stroke’, ‘stop-color’, ‘flood-color’ and
	 * ‘lighting-color’ properties. (The SVG properties which support color
	 * allow a color specification which is extended from CSS2 to accommodate
	 * color definitions in arbitrary color spaces. See Color profile
	 * descriptions.) ‘cursor’ ‘display’ ‘overflow’, only applicable to elements
	 * which establish a new viewport. ‘visibility’ The following SVG properties
	 * are not defined in CSS2. The complete normative definitions for these
	 * properties are found in this specification:
	 * 
	 * Clipping, Masking and Compositing properties: ‘clip-path’ ‘clip-rule’
	 * ‘mask’ ‘opacity’ Filter Effects properties: ‘enable-background’ ‘filter’
	 * ‘flood-color’ ‘flood-opacity’ ‘lighting-color’ Gradient properties:
	 * ‘stop-color’ ‘stop-opacity’ Interactivity properties: ‘pointer-events’
	 * Color and Painting properties: ‘color-interpolation’
	 * ‘color-interpolation-filters’ ‘color-profile’ ‘color-rendering’
	 * ‘fill-opacity’ ‘fill-rule’ ‘image-rendering’ ‘marker’ ‘marker-end’
	 * ‘marker-mid’ ‘marker-start’ ‘shape-rendering’ ‘stroke’ ‘stroke-dasharray’
	 * ‘stroke-dashoffset’ ‘stroke-linecap’ ‘stroke-linejoin’
	 * ‘stroke-miterlimit’ ‘stroke-opacity’ ‘stroke-width’ ‘text-rendering’ Text
	 * properties: ‘alignment-baseline’ ‘baseline-shift’ ‘dominant-baseline’
	 * ‘glyph-orientation-horizontal’ ‘glyph-orientation-vertical’ ‘kerning’
	 * ‘text-anchor’ ‘writing-mode’ A table that lists and summarizes the
	 * styling properties can be found in the Property Index.
	 */

	/** getter und setter */
	public CascadingStyleSheet getParent() {
		return parent;
	}

	public void setParent(CascadingStyleSheet parent) {
		this.parent = parent;
	}

	public String getValue(String key) {
		if (privateClass.containsKeyAsString(key)) {
			return privateClass.getValueAsString(key);
		}
		for(CSSClasse cl : classes){
			if (cl.containsKeyAsString(key)) {
				return cl.getValueAsString(key);
			}
		}
		if (getParent() != null) {
			return getParent().getValue(key);
		}
		return "";
	}

	public Color getValueAsColor(String key, Color defaultColor) {
		if (privateClass.containsKeyAsColor(key)) {
			return privateClass.getValueAsColor(key);
		}
		for(CSSClasse cl : classes){
			if (cl.containsKeyAsColor(key)) {
				return cl.getValueAsColor(key);
			}
		}
		if (getParent() != null) {
			return getParent().getValueAsColor(key, defaultColor);
		}
		return defaultColor;
	}

	public Number getValueAsNumber(String key, float defaultValue) {
		if (privateClass.containsKeyAsNumber(key)) {
			return privateClass.getValueAsNumber(key);
		}
		for(CSSClasse cl : classes){
			if (cl.containsKeyAsNumber(key)) {
				return cl.getValueAsNumber(key);
			}
		}
		if (getParent() != null) {
			return getParent().getValueAsNumber(key, defaultValue);
		}
		return defaultValue;
	}
	private SpecialEditObject getValueAsObject(String key) {
		if (privateClass.containsKeyAsObject(key)) {
			return privateClass.getValueAsObject(key);
		}
		for(CSSClasse cl : classes){
			if (cl.containsKeyAsObject(key)) {
				return cl.getValueAsObject(key);
			}
		}
		if (getParent() != null) {
			return getParent().getValueAsObject(key);
		}
		return null;
	}

//	public AlphaComposite getValueAsAlphaComposite(String key) {
//		AlphaComposite alpha;
//		if (bufferAlphaCom.containsKey(key)) {
//			return bufferAlphaCom.get(key);
//		}
//		int type = AlphaComposite.SRC_OVER;
//		alpha = AlphaComposite.getInstance(type, getValueAsFloat(key, 1));
//		alpha.
//		bufferAlphaCom.put(key, alpha);
//		return alpha;
//	}

	
	Color black = new Color(255,255,254);
	/** DRAW TODO */
	public boolean doFill(){
		return getValueAsColor("fill", black) != null;
	}
	AlphaComposite alphaFill = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f);
	AlphaComposite alphaLine = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f);
	public void setFillDrawSet(Graphics2D g) {
		g.setColor(getValueAsColor("fill",Color.WHITE));
		float fill_opacity = getValueAsNumber("fill-opacity", 1).floatValue();
		if(alphaFill.getAlpha() != fill_opacity){
			alphaFill = alphaFill.derive(fill_opacity);
		}
		g.setComposite(alphaFill);
	}
	BasicStroke s;
	String currendCap,currendJoin;
	static HashMap<String, Integer> cap = new HashMap<>();
	static HashMap<String, Integer> join = new HashMap<>();
	static{
		cap.put("butt", BasicStroke.CAP_BUTT);
		cap.put("round", BasicStroke.CAP_ROUND);
		cap.put("square", BasicStroke.CAP_SQUARE);
		join.put("miter", BasicStroke.JOIN_MITER);
		join.put("round", BasicStroke.JOIN_ROUND);
		join.put("bevel", BasicStroke.JOIN_BEVEL);
	}
	//new StringChooserDialog(panel, "stroke-linejoin", "Ecken (Join)","miter","round","bevel");
	public boolean doDrawLine(){
		return getValueAsColor("stroke", null) != null;
	}
	public void setLineDrawSet(Graphics2D g) {
		g.setColor(getValueAsColor("stroke",Color.WHITE));
		float line_opacity = getValueAsNumber("stroke-opacity", 1).floatValue();
		if(alphaLine.getAlpha() != line_opacity){
			alphaLine = alphaLine.derive(line_opacity);
		}
		g.setComposite(alphaLine);
//		stroke-opacity
		SpecialEditObject dash = getValueAsObject("stroke-dasharray");
		if(s == null){
			reloudStroke();
		}else if(dash != null && dash.isVerändert()){
			reloudStroke();
		}else if(getValueAsNumber("stroke-width",1).floatValue() != s.getLineWidth()){
			reloudStroke();
		}else if(getValueAsNumber("stroke-miterlimit",4).floatValue() != s.getMiterLimit()){
			reloudStroke();
		}else if(getValueAsNumber("stroke-dashoffset",1).floatValue() != s.getDashPhase()){
			reloudStroke();
		}else if(currendCap != getValue("stroke-linecap")){
			reloudStroke();
		}else if(currendJoin != getValue("stroke-linejoin")){
			reloudStroke();
		}
		g.setStroke(s);

	}
//	new EditDialog<>(panel, "stroke-dasharray", "Stichart");
//	new NumberDialog(panel, "stroke-dashoffset", "Stichartverschiebung", 1, Integer.MIN_VALUE, Integer.MAX_VALUE, 1, "Verschiebt dern Start der Strichart");
	float[] f = {0f};
	private void reloudStroke() {
		float width = getValueAsNumber("stroke-width",1).floatValue();
		float miterlimit = getValueAsNumber("stroke-miterlimit", 4).floatValue();
		float dashoffset = getValueAsNumber("stroke-dashoffset",1).floatValue();
		Sp_Dasharray dash = (Sp_Dasharray)getValueAsObject("stroke-dasharray");
		currendCap = getValue("stroke-linecap");
		currendJoin = getValue("stroke-linejoin");
		int capint = BasicStroke.CAP_BUTT;
		int joinint = BasicStroke.JOIN_MITER;
		if(currendCap != null){
			capint = cap.get(currendCap);
		}
		if(currendJoin != null){
			joinint = join.get(currendJoin);
		}
		float[] f;
		if(dash == null){
			f = null;
		}else{
			f = dash.getValue();
			dash.setVerändert(false);
		}
		s = new BasicStroke(width,capint,joinint,miterlimit,f,dashoffset);
		
	}
	static AlphaComposite $alphaDefault = AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER, 1);

	public void restore(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setComposite($alphaDefault);
	}
	//XML Element zu privateclass hinzufügen
	public void setAttributeByElement(Element element) {
		NamedNodeMap map = element.getAttributes();
		for(int i = 0; i != map.getLength(); i+=1){
			org.w3c.dom.Node n = map.item(i);
			if(n.getNodeName().equals("o-key")){continue;}
			addAttribute(n.getNodeName(), n.getNodeValue());
		}
	}
	public boolean addCSSClasses(CSSClasse c) {
		return classes.add(c);
	}
	public ArrayList<CSSClasse> getCSSClasses() {
		return classes;
	}

	public CSSClasse getPrivateCSSClass() {
		return privateClass;
	}
	public void save(Element rootObject, Document xmlDoc) {
		if(!classes.isEmpty()){
			String s = "";
			for(CSSClasse cl : classes){
				s += cl.getName() + " ";
			}
			rootObject.setAttribute("class", s);
		}
		for(String key : getPrivateCSSClass().getKeys()){
			rootObject.setAttribute(key, getPrivateCSSClass().getValueAsString(key));
		}
	}
	public CascadingStyleSheet clone1(HasName name) {
		CascadingStyleSheet css = new CascadingStyleSheet(name);
		css.privateClass = privateClass.clone1();
		for(CSSClasse c : classes){
			css.addCSSClasses(c);
		}
		css.setParent(parent);
		return css;
	}
	
}
