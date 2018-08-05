//package editor.rpg.script;
//
//import java.util.ArrayList;
//
//import javax.swing.text.SimpleAttributeSet;
//import javax.swing.text.StyledDocument;
//
//import de.rubberOo.javascript.GlobalObject;
//import de.rubberOo.javascript.Prototype;
//import de.rubberOo.javascript.gui.Format;
//import de.rubberOo.javascript.gui.scriptPanel.ScriptPanel;
//import de.rubberOo.javascript.syntax.TVariable;
//import de.rubberOo.javascript.syntax.Token;
//import interfaces.HasName;
//import interfaces.NameChangeListener;
//
//public class CObjectVar extends TVariable implements NameChangeListener{
//	HasName name;
//	GlobalObject global;
//	Prototype v;
//	public CObjectVar(Prototype v, HasName name,GlobalObject global) {
//		super(v, name.getName(), 0);
//		this.name = name;
//		this.global = global;
//		this.v = v;
//		name.addNameChangeListener(this);
//		this.input = name.getName();
//	}
//	@Override
//	public String getName() {
//		return name.getName();
//	}
//	@Override
//	public void nameChance(String old, String neu) {
//		global.renameDefaultVariable(old, neu);
//		this.input = name.getName();
//	}
//	@Override
//	public void Format(StyledDocument doc, ArrayList<Token> context) {
//		SimpleAttributeSet s = Format.OBJECT;
//		if(v == null){
//			s = Format.ERROR;
//			setError("Keine Variable übergeben");
//		}else if (isFunction()) {
//			s = Format.FUNKTION;
//		}else if (v.isDefaultVariable()) {
//			s = Format.VARIABLE;
//		} 
//		doc.setCharacterAttributes(getStartLine(), getLength(), s, true);
//		if(ScriptPanel.currendToken  != null && 
//				ScriptPanel.currendToken instanceof TVariable){
//			TVariable v = (TVariable)ScriptPanel.currendToken;
//			if(v.getName().equals(getName())){
//				if(!v.isFunction() || v.getPrototyp().equals(getPrototyp())){
//					doc.setCharacterAttributes(getStartLine(), name.getName().length(), Format.MARKT, false);
//				}
//			}
//		}
//	}
//
//}
