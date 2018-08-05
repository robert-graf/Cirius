package editor.css;

import gui.AAufbau;
import img.Icons;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import basic.CSSClasse;
import basic.ListenTreeList;
import basic.TreeListEvent;
import basic.TreeListListener;
import editor.CObject;
import editor.GUI_Layerbefehle;
import editor.Layer;
import editor.rpg.nexus.NexusPanel;

public enum CSSEditor implements ActionListener, TreeListListener<String, CSSClasse> {
	THAT();
	public ListenTreeList<String, CSSClasse> CSSCList;
	JDialog dialog;
	JTabbedPane pane = new JTabbedPane();
	JMenuBar bar = new JMenuBar();
	JMenu menu = new JMenu("Edit");
	JMenu menu2 = new JMenu("Select");
	JMenuItem currendObjectItem = new JMenuItem("Actuelles Object");
	JMenuItem currendLayerItem = new JMenuItem("Actuelles Layer");
	ArrayList<StandartDialog> dialoglist = new ArrayList<>();
	// TODO FIXME
	CSSClasse cssClasse = new CSSClasse("Default");

	private CSSEditor() {
		bar.add(menu);
		JMenuItem item = new JMenuItem("Neu");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String s = JOptionPane.showInputDialog(dialog, "Name eingeben");
				if (s == null)
					return;
				CSSClasse css = new CSSClasse(s);
				CSSCList.put(s, css);
				setCssClasse(css);
			}
		});
		menu.add(item);
		item = new JMenuItem("Bereinigen");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TreeSet<CSSClasse> map = new TreeSet<>();
				for (Layer l : GUI_Layerbefehle.getMatrix().getLayerList()) {
					for (CObject c : l) {
						map.addAll(c.getCSS().getCSSClasses());
					}
				}
				CSSCList.clear();
				for (CSSClasse cl : map) {
					CSSCList.put(cl.getName(), cl);
				}

			}
		});
		menu.add(item);
		currendObjectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CObject o = GUI_Layerbefehle.getMatrix().getSelectedCObject();
				if (o == null){
					JOptionPane.showMessageDialog(CSSEditor.THAT.dialog, "Kein Object markiert.");
					return;
				}
				setCssClasse(o.getCSS().getPrivateCSSClass());
			}
		});
		currendLayerItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Layer o = GUI_Layerbefehle.getMatrix().getSelectedLayer();
				if (o == null){
					JOptionPane.showMessageDialog(CSSEditor.THAT.dialog, "Kein Layer markiert.");
					return;
				}
				setCssClasse(o.getCSS().getPrivateCSSClass());
			}
		});
		initMenu2();
		bar.add(menu2);
	}

	private void initMenu2() {
		menu2.add(currendObjectItem);
		menu2.add(currendLayerItem);
		menu2.addSeparator();
	}

	private void init() {
		//Stoke
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setLayout(gridbag);
		pane.addTab("Stroke", Icons.getImage("linechart", 16),panel);
		new ColorDialog(panel, "stroke", "Linienfarbe");
		new NumberDialog(panel, "stroke-width", "Linienstärke", 1f, 0f, 1000f, 1f,"Linienstärke in Pixel");
		new NumberDialog(panel, "stroke-opacity", "Linientransparenz", 1f, 0f, 1f, .025f, "Linientransparenz");
		new StringChooserDialog(panel, "stroke-linecap", "Enden (Cap)","butt","round","square");
		new StringChooserDialog(panel, "stroke-linejoin", "Ecken (Join)","miter","round","bevel");
		new NumberDialog(panel, "stroke-miterlimit", "miterlimit", 4f, 1f, (float)Integer.MAX_VALUE, 1f,
				"<html>Die Grenze der Größe einer Ecken (Join) mit dem Typ 'miter'.<br>" +
				"Das 'miter limit' steuert, wie lange der Eckpunkt einer 'miter' Ecke (join) sein kann,<br>" +
				"ansonsten wird er automatisch ausgeschaltet und verwandelt sich zu eine 'bevel' Ecke.<br>" +
				"Berechnung des Wertes vom Winkel ausgehen ab wann abgeschnitten wird:<br>" +
				"<b>miter-limit</b> = 1 / ( sin( <b>winkel</b> / 2 ) )</html>"
				);
		new EditDialog<>(panel, "stroke-dasharray", "Stichart");
		new NumberDialog(panel, "stroke-dashoffset", "Stichartverschiebung", 1, 0, Integer.MAX_VALUE, 1, "Verschiebt dern Start der Strichart");
		//TODO stroke-linecap
		//FILL
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setLayout(gridbag);
		pane.addTab("Rendering", Icons.getImage("page_white_paint", 16), panel);
		new ColorDialog(panel, "fill", "Farbe der Füllfläche");
		new NumberDialog(panel, "fill-opacity", "Transparenz", 1.f, 0.f, 1.f,
				.025f,"Transparenz der Füllfläche");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (dialog == null) {
			dialog = new JDialog(AAufbau.f);
			init();
			dialog.add(pane);
			dialog.setJMenuBar(bar);
			dialog.add(pane);
			dialog.setSize(400, 300);
			dialog.setLocationRelativeTo(null);

			dialog.setVisible(false);
		}
		dialog.setVisible(!dialog.isVisible());
	}
	public void setCssClasse(String s){
		setCssClasse(CSSCList.get(s));
	}
	public void setCssClasse(CSSClasse cssClasse) {
		if (cssClasse != null) {
			this.cssClasse = cssClasse;
			for (StandartDialog dia : dialoglist) {
				dia.updateAtt();
			}
			dialog.setTitle(cssClasse.getName());
		}
	}

	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();


	
	
	@Override
	public void listenPut(TreeListEvent<String, CSSClasse> e) {
		final String c = e.getKey();
		JMenuItem item = new JMenuItem(c);
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCssClasse(CSSCList.get(c));
			}
		});
		menu2.add(item);
	}
	@Override
	public void listenClear(TreeListEvent<String, CSSClasse> arg0) {
		menu2.removeAll();
		initMenu2();
	}
	@Override
	public void listenRemove(TreeListEvent<String, CSSClasse> e) {
		for(Component m : menu2.getMenuComponents()){
			if(m instanceof JMenuItem){
				if(((JMenuItem) m).getText().equals(e.getKey())){
					menu2.remove(m);
					return;
				};
			}
		}
	}

	public void setCSSList(ListenTreeList<String, CSSClasse> cSSCList2) {
		if(CSSCList != null){
			CSSCList.removeListDataListener(this);
		};
		CSSCList = cSSCList2;
		CSSCList.addListDataListener(this);
		NexusPanel.THAT.updateCSS();
	}
}
