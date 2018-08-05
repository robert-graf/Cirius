package toolbars;

import static toolbars.Toolbar.createMenuItem;
import editor.tools.Verschieben;
import editor.tools.setMainTool;

public class DialogÄndern {
	static String name = "Ändern";

	public static void init() {
		createMenuItem("Kopieren", "copying_and_distribution", 16, name,
				new setMainTool(null));
		createMenuItem("Verschieben", "transform_move", 16, name,
				new setMainTool(new Verschieben()));
	}
}
