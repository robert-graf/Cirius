package de.game.objects;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

public class CFont extends CObject{
	AngelCodeFont font;
	private String text;
	private boolean relativ;
	private int maxCharCont = -1;
	private int Linereset = 200;
	private ArrayList<String> nextLine = new ArrayList<>();
	File f;
	public CFont(int x, int y, File f,String s,int width, String name, int maxCharCont2,boolean relativ) {
		setX(x);
		setY(y);
		
		this.f = f;
//		setFile(f);		
		text = s;
		Linereset = width;
		setName(name);
		setMaxCharCont(maxCharCont2);
		setRelativ(relativ);
		
	}

	@Override
	public void draw(int x, int y, Graphics g) {
		if(isRelativ()){
			//TODO Scale?
			g.resetTransform();
		}
		int length = getMaxCharCont();
		
		for(String s : nextLine){
			if(length != -1){
				length-=s.length();
				if(length <= 0){
					font.drawString(x, y, s, Color.white, 0, s.length()+length-1);
					break;
				}
				
			}
			font.drawString(x, y,s, Color.white,0,s.length());
			y+=font.getHeight(s);
		}
		if(isRelativ()){
			g.translate(CObject.getVerschiebungX(), CObject.getVerschiebungY());
		}
	}

	@Override
	protected void update(int delta, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		try {
			Image i = new Image(f.getPath().replace(".fnt", ".png"));
			font = new AngelCodeFont(f.getPath(),i);
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
		resetLine();
	}

	@Override
	public String getXMLName() {
		return "Font";
	}
	
	@Override
	public Shape getShape() {
		return null;
	}
	private void resetLine(){
		nextLine.clear();
		if(text == null || getText().isEmpty())return;
		int endLast= 0;
		int poss = 0;
		while(getText().indexOf(' ',poss) != -1){
			int end = getText().indexOf(' ',poss);
			int lineStart = getText().indexOf('\n',poss) ;
			if(lineStart< endLast && lineStart != -1){
				
			}else{
				lineStart = endLast;
			}
			if(font.getWidth(getText().substring(lineStart, end)) >= getLinereset()&&poss != 0){
				nextLine.add(getText().substring(endLast, poss));
				endLast = poss;
				
			}
			poss=end+1;
		}//wiederholung
		if(font.getWidth(getText().substring(endLast)) >= getLinereset()&&poss != 0){
			nextLine.add(getText().substring(endLast, getText().lastIndexOf(' ')));
			endLast = poss;
		}
		nextLine.add(getText().substring(endLast));
//		System.out.println(nextLine);
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
		resetLine();
		
	}

	/**
	 * @return the linereset
	 */
	public int getLinereset() {
		return Linereset;
	}

	/**
	 * @param linereset the linereset to set
	 */
	public void setLinereset(int linereset) {
		Linereset = linereset;
		resetLine();
	}

	/**
	 * @return the maxCharCont
	 */
	public int getMaxCharCont() {
		return maxCharCont;
	}

	/**
	 * @param maxCharCont the maxCharCont to set
	 */
	public void setMaxCharCont(int maxCharCont) {
		this.maxCharCont = maxCharCont;
	}

	/**
	 * @return the relativ
	 */
	public boolean isRelativ() {
		return relativ;
	}

	/**
	 * @param relativ the relativ to set
	 */
	public void setRelativ(boolean relativ) {
		this.relativ = relativ;
	}

}
