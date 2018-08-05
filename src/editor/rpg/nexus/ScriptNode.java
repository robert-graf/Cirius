package editor.rpg.nexus;

import img.Icons;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;

import basic.MyTreeNode;
import de.rubberOo.javascript.GlobalObject;
import de.rubberOo.javascript.ScriptListener;

@SuppressWarnings("serial")
public class ScriptNode extends MyTreeNode<String> implements ScriptListener {
	private JTree tree;
	
	public ScriptNode(String s, JTree tree) {
		super(s, true);
		this.tree = tree;
		GlobalObject.addScriptListener(this);
		setIcon(Icons.getImage("script_code_red", 16));
	}
	@Override
	public void scriptRemoved(int i, String arg1) {
		this.remove(i);
		update();
	}
	@Override
	public void scriptCreated(String s, String ini) {
		this.add(new MyTreeNode<Object>(s).setIcon(Icons.getImage("script_code", 16)));
		update();
	}
	void update() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				((DefaultTreeModel) tree.getModel())
						.nodeStructureChanged(ScriptNode.this);
			}
		});
	}
	@Override
	public void currenGlobal(GlobalObject g) {
		this.removeAllChildren();
		for(int i = 0; i != g.size(); i+=1){
			this.add(new MyTreeNode<Object>(g.getName(i)).setIcon(Icons.getImage("script_code", 16)));
		}
		update();
	}
	
}
