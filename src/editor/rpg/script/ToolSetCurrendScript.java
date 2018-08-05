//package editor.rpg.script;
//
//import java.awt.event.MouseEvent;
//
//import de.rubberOo.javascript.GlobalObject;
//import de.rubberOo.javascript.dialog.NewGameObjectSubscriptDialog;
//import de.rubberOo.javascript.gui.scriptPanel.ScriptPanel;
//import editor.AbstractToolAdapter;
//import editor.DefaultEditorPanel.Matrix;
//
//public class ToolSetCurrendScript extends AbstractToolAdapter{
//	@Override
//	public void mousePressed(Matrix m, MouseEvent ev) {
//		if(ev.getClickCount() == 2 && ev.getButton() == MouseEvent.BUTTON1){
//			GlobalObject g = ScriptPanel.THAT.getGlobal();
//			if(ScriptPanel.THAT.dialog == null ||! ScriptPanel.THAT.dialog.isVisible()){
//				ScriptPanel.THAT.actionPerformed(null);
//			
//			}
//			if(m.getSelectedCObject() != null && 
//					!g.setCurrendScript(m.getSelectedCObject().getName())){
//				NewGameObjectSubscriptDialog dialog = new NewGameObjectSubscriptDialog(m.getSelectedCObject().getName());
//				ScriptPanel.THAT.insertNewSubscriptDialog(dialog);
//			}
//		}
//	}
//}
