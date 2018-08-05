//package editor.rpg.script;
//
//import gui.AAufbau;
//import interfaces.updateable;
//import de.rubberOo.javascript.GlobalObject;
//import de.rubberOo.javascript.dialog.NewGameObjectSubscriptDialog;
//import de.rubberOo.javascript.gui.scriptPanel.ScriptPanel;
//import editor.DefaultEditorPanel.Matrix;
//
//public enum ScriptSettings implements updateable {
//	THAT();
//	ScriptSettings() {
//		ScriptPanel.THAT.setWindow(AAufbau.f);
//		// ScriptPanel.THAT.actionPerformed(null);
//		Matrix.addUpdate(this);
//	}
//
//	public void setGlobalObject(GlobalObject global) {
//		ScriptPanel.THAT.setGlobal(global);
//	}
//
//	@Override
//	public void update(Matrix m) {
//		if (m.getSelectedCObject() != null) {
//			if (ScriptPanel.THAT.getGlobal().getCurrendScriptName().equals(m.getSelectedCObject().getName())) {
//				boolean b = ScriptPanel.THAT.getGlobal().setCurrendScript(m.getSelectedCObject().getName());
//				if(!b){
//					ScriptPanel.THAT.insertNewSubscriptDialog(new NewGameObjectSubscriptDialog(m.getSelectedCObject().getName()));
//				}
//			}
//		}
//	}
//}
