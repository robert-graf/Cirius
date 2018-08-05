package editor.rpg.nexus;

import toolbars.Toolbar;
import de.rubberOo.javascript.gui.scriptPanel.ScriptPanel;
import editor.css.CSSEditor;

public final class DialogFunktion {
	static String name = "Fenster ";
	
	static {
		Toolbar.createMenuItem("Scriptpanel", "script_code_red", 16, name,
				ScriptPanel.THAT);
		Toolbar.createMenuItem("CSS", "css", 16, name,
				CSSEditor.THAT);
	}
}
