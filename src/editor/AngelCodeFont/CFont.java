package editor.AngelCodeFont;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;

import editor.DefaultEditorPanel.Matrix;
import editor.GreifItem;
import editor.rpg.CImageObject;
import editor.tabelle.NumberLine;
import editor.tabelle.OneLine;
public class CFont extends CImageObject implements DocumentListener{
	AngelCodeFont font;// = new AngelCodeFont("res/Projekt/fonts/game_over_font.fnt", "res/Projekt/fonts/game_over_font.png");
	private Document text = new DefaultStyledDocument();
	private boolean relativ;
	private int Linereset = 200;
	private int maxCharCont = -1;
	private ArrayList<String> nextLine = new ArrayList<>();
	OneLine<String> lineName = new OneLine<String>("Text", "") {
		public String getValue() {
			try {
				return text.getText(0, text.getLength());
			} catch (BadLocationException e) {
				e.printStackTrace();
				return "";
				
			}
		};
		@Override
		public void valueChanges(String value) {
		
			setText(value);
		}
	};
	OneLine<Boolean> lineRelativ = new OneLine<Boolean>("Relativ",false) {
		
		@Override
		public void valueChanges(Boolean value) {
			setRelativ(value);
		}
	};
	NumberLine<Integer> newLine = new NumberLine<Integer>("Linereset",Linereset) {
		
		@Override
		public void valueChanges(Integer value) {
			setLinereset(value);
		}
	};
	
	
	NumberLine<Integer> maxCharContLine = new NumberLine<Integer>("MaxCharCont", maxCharCont) {
		public Integer getValue() {
			return maxCharCont;
		};
		@Override
		public void valueChanges(Integer value) {
			setMaxCharCont(value);
		}
	};
	public CFont(double x, double y, File f) {
		setFile(f);
		init(x, y);
		font = new AngelCodeFont(f, new File(f.getAbsolutePath().replace(".fnt", ".png")));
		setText("Test!10");
	}
	public CFont(double x, double y, File f,String s,int width, String name, int maxCharCont2,boolean relativ) {
		setFile(f);
		init(x, y);
		font = new AngelCodeFont(f, new File(f.getAbsolutePath().replace(".fnt", ".png")));
		setText(s);
		setLinereset(width);
		setName(name);
		setMaxCharCont(maxCharCont2);
		setRelativ(relativ);
	}
	public void init(int x,int y){
		super.init(x, y);
		addLine(0,lineName);
		addLine(1,lineRelativ);
		addLine(2,newLine);
		addLine(3,maxCharContLine);
		getGreifItems().clear();
		getGreifItems().add(new VerschiebeGriff());
		getGreifItems().add(new RestLineGriff());
		text.addDocumentListener(this);
	}
	
	@Override
	public Class<?> getGameClass() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected CImageObject clone2() throws CloneNotSupportedException {
		return new CFont(getX(),getY(),getFile(),getText(),getLinereset(),getName(),getMaxCharCont(),isRelativ());
	}
	@Override
	public void drawRenderer(Graphics2D g, Graphics2D g2, Matrix m,
			MouseEvent ev, int x, int y) {
		if(isRelativ()){
			g = g2;
		}
		int length = getMaxCharCont();
		
		for(String s : nextLine){
			if(length != -1){
				length-=s.length();
				if(length <= 0){
					font.drawString(s,0,s.length()+length-1, g,x, y);
					break;
				}
				
			}
			font.drawString(s,0,s.length(), g,x, y);
			y+=font.getHeight(s);
		}
		
		
	}
	@Override
	public void updateRenderer(Matrix m, int timePassed, int conter) {
		
	}
	Rectangle r = new Rectangle();
	@Override
	public Shape getSingelShape() {
		if(isRelativ()){
			Matrix m = getMatrix();
			r.setBounds((int) ((CFont.this.getX()) / m.getScale() + m.getTransX()),  
					(int) ((CFont.this.getY()) / m.getScale() + m.getTransY()), 
					(int)(getLinereset()/m.getScale()),
					(int)(font.getHeight(lineName.getValue())/m.getScale())*nextLine.size());
		}else{
			r.setBounds((int)getX(),(int)getY(), getLinereset(),(int)  font.getHeight(lineName.getValue())*nextLine.size());
		}
		return r;
	}
	@Override
	public void reload() {
		font = new AngelCodeFont(getFile(), 
				new File(getFile().getAbsolutePath().replace(".fnt", ".png")));
		
	}
	@Override
	public String getXMLName() {
		return "Font";
	}
	/**
	 * @return the text
	 */
	public String getText() {
		try {
			return text.getText(0, text.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return "";
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
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		lineName.setValue(text);
		try {
			if(!getText().isEmpty()){
				this.text.remove(0, text.length());
				
			}
			this.text.insertString(0, text, null);
		} catch (BadLocationException e) {
			System.out.println("Text length is " + text.length());
			e.printStackTrace();
		}
		
		
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
		lineRelativ.setValue(relativ);
		
		this.relativ = relativ;
	
	}
	/**
	 * @return the linereset
	 */
	public int getLinereset() {
		return Linereset;
	}
	/**
	 * @param x the linereset to set
	 */
	public void setLinereset(int x) {
		newLine.setValue(x);
		Linereset = x;
		resetLine();
	}
	private class VerschiebeGriff extends GreifItem {
		Rectangle r = new Rectangle();

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			g.setColor(Color.WHITE);
			int i = 10;
			// if(i == 0){i = 1;}
			r.setBounds((int)m.getRealX(getX()) - i / 2,(int) m.getRealY(getY()) - i / 2,
					i, i);
			g2.setColor(transformGriffColor);
			g2.fill(r);
			g2.setColor(Color.WHITE);
			g2.draw(r);
		}

		@Override
		public Rectangle getBounds() {
			return r;
		}

		public int getID() {
			return 0;
		}

		public double getX() {
			if(isRelativ()){
				Matrix m = getMatrix();
				return (int) ((CFont.this.getX()) / m.getScale() + m.getTransX());
			}
			return CFont.this.getX();
		}

		public double getY() {
			Matrix m = getMatrix();
			if(isRelativ()){
				return (int) ((CFont.this.getY()) / m.getScale() + m.getTransY());
			}
			return CFont.this.getY();
		}

		@Override
		public void setLocation(double x, double y) {
			if(isRelativ()){
				Matrix m = getMatrix();
				CFont.this.setX(m.getRealX(x));
				CFont.this.setY(m.getRealY(y));
//				return (int) ((CFont.this.getY()) / m.getScale() + m.getTransY());
			}else{
				CFont.this.setX(x);
				CFont.this.setY(y);
			}
		}

		@Override
		public boolean remove() {
			return true;
		}

		@Override
		public JPopupMenu showContext() {
			return getMenu();
		}
		
	}
	private class RestLineGriff extends GreifItem {
		Rectangle r = new Rectangle();

		public void draw(Graphics2D g, Graphics2D g2, Matrix m) {
			g.setColor(Color.WHITE);
			int i = 10;
			// if(i == 0){i = 1;}
			r.setBounds((int)m.getRealX(getX()) - i / 2,(int) m.getRealY(getY()) - i / 2,
					i, i);
			g2.setColor(transformGriffColor);
			g2.fill(r);
			g2.setColor(Color.WHITE);
			g2.draw(r);
		}

		@Override
		public Rectangle getBounds() {
			return r;
		}

		public int getID() {
			return 0;
		}

		public double getX() {
			if(isRelativ()){
				Matrix m = getMatrix();
				return (int) ((CFont.this.getX()+getLinereset()) / m.getScale() + m.getTransX());
			}
			return CFont.this.getX() +getLinereset();
		}

		public double getY() {
			Matrix m = getMatrix();
			if(isRelativ()){
				return (int) ((CFont.this.getY()) / m.getScale() + m.getTransY());
			}
			return CFont.this.getY();
		}

		@Override
		public void setLocation(double x, double y) {
			if(isRelativ()){
				Matrix m = getMatrix();
				setLinereset((int)(m.getRealX(x)-CFont.this.getX()));
				
//				return (int) ((CFont.this.getY()) / m.getScale() + m.getTransY());
			}else{
				setLinereset((int)(x-CFont.this.getX()));
				
			}
		}

		@Override
		public boolean remove() {
			return true;
		}

		@Override
		public JPopupMenu showContext() {
			return getMenu();
		}
		
}
	public Document getDoc() {
		return text;
	}
	public int getMaxCharCont() {
		return maxCharCont;
	}
	public void setMaxCharCont(int maxCharCont) {
		if(maxCharCont<-1){
			this.maxCharCont=-1;
			return;
		}
		this.maxCharCont = maxCharCont;
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		resetLine();
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		resetLine();
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		resetLine();
	}
	
}
