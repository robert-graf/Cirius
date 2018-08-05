package editor.cad;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import editor.AbstractTool;
import editor.AbstractToolAdapter;
import editor.DefaultEditorPanel;
import editor.DefaultEditorPanel.Matrix;
import editor.GreifItem;
import editor.tools.GreifTool;
import editor.tools.Pan;
import interfaces.Destroyer;
import interfaces.ExtendsDefaultEditor;

@SuppressWarnings("serial")
public class ShapeEditor extends JPanel implements ExtendsDefaultEditor {
	ArrayList<AbstractTool> tool = new ArrayList<>();
	DefaultEditorPanel ePanel = new DefaultEditorPanel();
	JPanel panel = new JPanel();
	ArrayList<JToggleButton> ToolButtons = new ArrayList<JToggleButton>();
	File f;

	public ShapeEditor(File f) {
		this.f = f;
		tool.add(new SelectByFrame());
		ePanel.m.setMainTool(tool.get(0));
		tool.add(new CRectTool());
		tool.add(new CKreisTool());
		tool.add(new COvalTool());
		tool.add(new CPolyTool());
		// Remove
		tool.add(new SelectByFrame() {
			GreifItem i;

			@Override
			public void mousePressed(Matrix m, MouseEvent ev) {
				if (GreifTool.item != null)
					i = GreifTool.item;
				super.mousePressed(m, ev);
			}

			public void mouseReleased(Matrix m, java.awt.event.MouseEvent ev) {
				super.mouseReleased(m, ev);
				if (ev.getClickCount() == 2) {
					GreifTool.item = i;
					remove();
				}
			};
		});
		tool.add(new Pan());
		setLayout(new BorderLayout());
		add(ePanel);
		add(panel, BorderLayout.NORTH);
		addToggle("Bearbeitungsmodus", new ImageIcon(this.getClass()
				.getResource("/img/PunktEdit.png")));
		ToolButtons.get(0).setSelected(true);
		addToggle("Rechteck",
				new ImageIcon(this.getClass().getResource("/img/Rechteck.png")));
		addToggle("Kreis",
				new ImageIcon(this.getClass().getResource("/img/Kreis.png")));
		addToggle("Oval",
				new ImageIcon(this.getClass().getResource("/img/Oval.png")));
		addToggle("Polygon",
				new ImageIcon(this.getClass().getResource("/img/Poly.png")));
		addToggle("Punkte Löschen",
				new ImageIcon(this.getClass().getResource("/img/x.png")));
		addToggle("Pan",
				new ImageIcon(this.getClass().getResource("/img/Pan.png")));
		getMatrix().passiveTools.add(new AbstractToolAdapter() {
			@Override
			public void keyPressed(Matrix m, KeyEvent ev) {
				if (ev.getKeyCode() == KeyEvent.VK_DELETE)
					remove();
				if (ev.getKeyCode() == KeyEvent.VK_ESCAPE)
					Escape();
			}
		});

	}

	public void addToggle(String name, ImageIcon i) {
		tool.get(ToolButtons.size()).init(getMatrix());
		JToggleButton t = new JToggleButton(i);
		t.setName("" + ToolButtons.size());
		t.setToolTipText(name);
		ToolButtons.add(t);
		panel.add(t);
		t.addActionListener(new Listen(t));

	}

	@Override
	public void destroy() {
		Destroyer.destroy(ePanel);
		ePanel = null;
	}

	@Override
	public Matrix getMatrix() {
		// TODO Auto-generated method stub
		return ePanel.m;
	}

	public class Listen implements ActionListener {
		JToggleButton tb;

		public Listen(JToggleButton tb) {
			this.tb = tb;
		}

		public void actionPerformed(ActionEvent e) {
			for (JToggleButton t : ToolButtons) {
				t.setSelected(false);
			}
			tb.setSelected(true);
			getMatrix().setMainTool(tool.get(Integer.parseInt(tb.getName())));
		}
	}

	public void remove() {
		if (getMatrix().getSelectedCObject() instanceof CPoly) {
			if (GreifTool.item instanceof CPoly.PolygonPoint) {
				CPoly p = (CPoly) getMatrix().getSelectedCObject();
				p.removePoint(GreifTool.item.getID());
				GreifTool.item = null;
				return;
			}
		}
		getMatrix().getSelectedLayer().remove(getMatrix().getSelectedCObject());
		getMatrix().setSelectedCObject(null, getMatrix().getSelectedLayer());
	}

	public void Escape() {
		getMatrix().setSelectedCObject(null, getMatrix().getSelectedLayer());
	}

	@Override
	public File getFile() {
		return f;
	}

	public void setFile(File f) {
		this.f = f;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
}
