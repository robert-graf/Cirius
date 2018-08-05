package editor;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import editor.DefaultEditorPanel.Matrix;
import gui.AAufbau;
import interfaces.ExtendsDefaultEditor;
import interfaces.HasName;

/**
 * Statische Layerbefehl die über ActionListenerObjekte und Metoden verteilt
 * werden.
 * 
 * @author rubber
 * 
 */
public class GUI_Layerbefehle {
	private static JCheckBox sichtbar = new JCheckBox("Sichtbar");
	private static JCheckBox solid = new JCheckBox("Solid");
	private static JFormattedTextField faktorFeld = new JFormattedTextField(
			new NumberFormatter(new DecimalFormat()));
	private static JLabel faktorLable = new JLabel("Faktor:  ");
	private static TwoComponten faktor = new TwoComponten(faktorLable,
			faktorFeld);
	private static JTextField nameFeld = new JTextField();
	private static JLabel nameLable = new JLabel("Name:  ");
	private static TwoComponten name = new TwoComponten(nameLable, nameFeld);
	private static JLabel einfügePunktLabel = new JLabel("Einfügen über:");
	private static JComboBox<String> einfügePunkt = new JComboBox<String>();

	public static void addLayerDialog(Matrix m) {
		// Dialog
		Layer layer = m.getSelectedLayer();
		if (layer == null) {
			layer = m.getLayer(0);
		}
		// TODO Namen finden
		nameFeld.setText(getUniqueLayerName(layer.getName(), "", getMatrix()));
		sichtbar.setSelected(layer.isVisible());
		solid.setSelected(layer.isSolid());
		faktorFeld.setValue(layer.getFaktor());
		einfügePunkt.removeAllItems();
		for (Layer l : m.getLayerList()) {
			einfügePunkt.addItem(l.getName());
		}
		einfügePunkt.addItem("--Ganz unten--");
		Object[] o = { name, sichtbar, solid, faktor, einfügePunktLabel,
				einfügePunkt };
		String[] o1 = { "OK", "Doch nicht" };
		int i = JOptionPane.showOptionDialog(m.getPanel(), o,
				"Neuer Layer erstellen", 0, JOptionPane.QUESTION_MESSAGE, null,
				o1, o1[0]);
		// Erstellen
		if (i != 0) {
			return;
		}
		Layer neuerLayer = new Layer(nameFeld.getText(),
				((Number) faktorFeld.getValue()).floatValue(),
				sichtbar.isSelected(), solid.isSelected());
		getMatrix().addUndo(
				new Undo(Undo.ADDLAYER, einfügePunkt.getSelectedIndex(),
						einfügePunkt.getSelectedIndex(), neuerLayer));
		m.addLayer(einfügePunkt.getSelectedIndex(), neuerLayer);
	}

	// entferne Layer oder Object
	public static void removeDialog(Matrix m) {
		CObject object = m.getSelectedCObject();
		if (object != null) {
			if (m.getSelectedLayer() == null) {
				return;
			}
			m.getSelectedLayer().remove(object);
			return;
		}
		if (m.getLayerSize() == 1) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(m.getPanel(),
					"Das ist nicht möglich. Es muss immer ein Layer bestehen!",
					"Fehler", JOptionPane.ERROR_MESSAGE, null);
			return;
		}
		int i = m.getSelectedLayerIndex();
		if (i == -1) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(m.getPanel(),
					"Es ist gerade keine Auswahl vorhanden", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (m.getLayer(i).size() != 0) {
			if (JOptionPane.showConfirmDialog(m.getPanel(),
					"Möchtest du wirklich diesen Layer mit "
							+ m.getLayer(i).size() + " Objekten löschen?",
					"Warnung!", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE) != 0) {
				return;
			}
		}
		getMatrix().addUndo(new Undo(Undo.REMOVELAYER, i, i, m.getLayer(i)));
		m.removeLayer(i);
		m.setSelectedCObject(null, null);
		if (i == 0)
			i = 0;
		else
			i--;
		m.setSelectedCObject(null, m.getLayer(i));
	}

	private static ArrayList<String> list = new ArrayList<>();

	// Layer verschieben
	public static void setLayerDialog(Matrix m) {
		Layer layer = m.getSelectedLayer();
		if (layer == null) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(m.getPanel(),
					"Es ist gerade keine Auswahl vorhanden", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		list.clear();
		for (Layer l : m.getLayerList()) {
			if (!layer.equals(l)) {
				list.add(l.getName());
			}
		}
		list.add("--Ganz unten--");
		Object i = JOptionPane.showInputDialog(m.getPanel(),
				"Verschiebe Layer " + layer.getName() + " über:",
				"Layer verschieben", JOptionPane.QUESTION_MESSAGE, null,
				list.toArray(), m.getSelectedLayerIndex());
		if (i == null)
			return;
		getMatrix().addUndo(
				new Undo(Undo.SETLAYER, m.getIndexOfLayer(layer), list
						.indexOf(i), layer));
		m.removeLayer(layer);
		m.addLayer(list.indexOf(i), layer);
	}

	// Layer verschieben um 1 nach oben
	public static void setLayerOneUp(Matrix m) {
		Layer layer = m.getSelectedLayer();
		if (layer == null) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(m.getPanel(),
					"Es ist gerade keine Auswahl vorhanden", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int i = m.getSelectedLayerIndex() - 1;
		if (i == -1) {
			System.out.println(i);
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		getMatrix().addUndo(
				new Undo(Undo.SETLAYER, m.getSelectedLayerIndex(), i, layer));
		m.removeLayer(layer);
		m.addLayer(i, layer);
	}

	// Object verschieben um 1 nach oben
	public static void setObjectOneUp(Matrix m) {
		Layer layer = m.getSelectedLayer();
		CObject object = m.getSelectedCObject();
		if (object == null) {
			// Layer up, wenn kein Objekt verwendet wird;
			setLayerOneUp(m);
			return;
		}
		int iOld = layer.indexOf(object);
		int i = iOld - 1;
		if (i == -1 && m.getSelectedLayerIndex() == 0) {
			return;
		}
		Layer l = layer;
		// TODO
		layer.remove(object, false);
		if (i != -1) {
			layer.add(i, object, false);
		} else {
			layer = m.getLayer(m.getSelectedLayerIndex() - 1);
			layer.add(object, false);
			i = layer.size() - 1;
		}
		getMatrix().addUndo(
				new UndoObjeckt(UndoObjeckt.ONEUP, l, iOld, layer, i, object));
		m.setSelectedCObject(object, layer);
	}

	// Layer verschieben um 1 nach unten
	public static void setLayerOneDown(Matrix m) {
		Layer layer = m.getSelectedLayer();
		if (layer == null) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(m.getPanel(),
					"Es ist gerade keine Auswahl vorhanden", "Fehler",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		int i = m.getSelectedLayerIndex() + 1;
		if (i >= m.getLayerList().size()) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		getMatrix().addUndo(
				new Undo(Undo.SETLAYER, m.getSelectedLayerIndex(), i, layer));
		m.removeLayer(layer);
		m.addLayer(i, layer);
	}

	// Object verschieben um 1 nach unten
	public static void setObjectOneDown(Matrix m) {
		Layer layer = m.getSelectedLayer();
		Layer l = layer;
		CObject object = m.getSelectedCObject();
		int iold = layer.indexOf(object);
		int i = iold + 1;
		if (object == null) {
			setLayerOneDown(m);
			return;
		}
		if (i >= layer.size()) {
			try {
				layer = m.getLayer(m.getSelectedLayerIndex() + 1);
				i = 0;
			} catch (IndexOutOfBoundsException e) {
				return;
			}
		}
		l.remove(object, false);
		layer.add(i, object, false);
		m.setSelectedCObject(object, layer);
		m.addUndo(new UndoObjeckt(UndoObjeckt.ONDDOWN, l, iold, layer, i,
				object));
	}

	private static ArrayList<Integer> intlist = new ArrayList<>();

	public static String getUniqueLayerName(String nameNeu, String nameAlt,
			Matrix m) {
		boolean nameAltNichtGefunden = true;
		nameNeu.trim();
		intlist.clear();
		if (m == null) {
			return nameNeu;
		}
		Collection<Layer> list = m.getLayerList();
		if (nameNeu.isEmpty()) {
			return "unnamedLayer";
		}
		String s = getStringBase(nameNeu);
		int id = getZahlOfString(nameNeu.replaceFirst(s, ""));
		for (Layer l : list) {
			if (nameAltNichtGefunden && l.getName().equals(nameAlt)) {
				nameAltNichtGefunden = false;
			} else if (l.getName().startsWith(s)) {
				int id2 = getZahlOfString(l.getName().replace(s, ""));
				if (id2 != -1) {
					intlist.add(id2);
				}
			}
		}
		for (Layer layer : list) {
			for (CObject l : layer) {
				if (nameAltNichtGefunden && l.getName().equals(nameAlt)) {
					nameAltNichtGefunden = false;
				} else if (l.getName().startsWith(s)) {
					int id2 = getZahlOfString(l.getName().replace(s, ""));
					if (id2 != -1) {
						intlist.add(id2);
					}
				}
			}
		}
		if (!intlist.contains(id)) {
			return nameNeu;
		}
		for (int n = id; true; n++) {
			if (!intlist.contains(n)) {
				return s + n;
			}
		}

	}

	// gibt einen String ohne Zahl wieder
	private static String getStringBase(String s) {

		while (Character.isDigit(s.charAt(s.length() - 1))) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}

	// gibt die letzte Zahl eines String wieder
	private static int getZahlOfString(String s) {
		if (s.length() == 0) {
			return 0;
		}
		for (int n = 0; n != s.length(); n++) {
			if (!Character.isDigit(s.charAt(n))) {
				return -1;
			}
		}
		return Integer.parseInt(s);
	}

	public static boolean isLayerOrObjectByName(String tupel) {
		if (getLayerOrObjectByName(tupel) != null) {
			return true;
		}
		return false;
	}

	public static HasName getLayerOrObjectByName(String tupel) {
		for (Layer l : getMatrix().getLayerList()) {
			if (l.getName().equals(tupel)) {
				return l;
			}
			for (CObject c : l) {
				if (c.getName().equals(tupel)) {
					return c;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("serial")
	private static class TwoComponten extends JPanel {
		TwoComponten(Component c, Component c1) {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(c);
			add(c1);
		}
	}

	public static Matrix getMatrix() {
		try {
			return ((ExtendsDefaultEditor) AAufbau.f.CurrendPanel).getMatrix();
		} catch (Exception e) {
			return null;
		}
	}

	private static ActionListener addLayerDialog = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			addLayerDialog(getMatrix());
		}
	};

	public static ActionListener ActionAddLayer() {
		return addLayerDialog;
	}

	private static ActionListener addRemoveDialog = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			removeDialog(getMatrix());
		}
	};

	public static ActionListener ActionRemoveLayer() {
		return addRemoveDialog;
	}

	private static ActionListener addSetDialog = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			setLayerDialog(getMatrix());
		}
	};

	public static ActionListener ActionSetLayer() {
		return addSetDialog;
	}

	private static ActionListener addOneUp = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			setObjectOneUp(getMatrix());
		}
	};

	public static ActionListener ActionOneUp() {
		return addOneUp;
	}

	private static ActionListener addOneDown = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			setObjectOneDown(getMatrix());
		}
	};

	public static ActionListener ActionOneDown() {
		return addOneDown;
	}

	private static class UndoObjeckt extends interfaces.UndoObject {
		public static final int ONEUP = -1;
		public static final int ONDDOWN = -1;
		int index, indexNeu;
		Layer old, neu;

		public UndoObjeckt(int id, Layer old, int index, Layer neu, int index2,
				CObject obj) {
			super(id, old, neu, obj);
			this.old = old;
			this.neu = neu;
			this.index = index;
			this.indexNeu = index2;
		}

		@Override
		public void doRedo() {
			old.remove(getObject(), false);
			neu.add(indexNeu, getObject(), false);
			getMatrix().setSelectedCObject(getObject(), neu);
		}

		@Override
		public void doUndo() {
			neu.remove(getObject(), false);
			old.add(index, getObject(), false);
			getMatrix().setSelectedCObject(getObject(), old);
		}

		@Override
		public String getName() {
			if (getID() == ONEUP) {
				return "Nach oben " + getObject().getName();
			} else if (getID() == ONDDOWN) {
				return "Nach unten " + getObject().getName();
			}
			return null;
		}

		@Override
		public CObject getObject() {
			return (CObject) super.getObject();
		}
	}

	private static class Undo extends interfaces.UndoObject {
		Layer obj;
		public static final int ADDLAYER = -1;
		public static final int REMOVELAYER = -2;
		public static final int SETLAYER = -3;

		public Undo(int id, Object old, Object neu, Layer obj) {
			super(id, old, neu, obj);
			this.obj = obj;
		}

		@Override
		public void doRedo() {
			if (getID() == ADDLAYER) {
				getMatrix().addLayer((int) neu, obj);
			} else if (getID() == REMOVELAYER) {
				getMatrix().removeLayer(obj);
			} else if (getID() == SETLAYER) {
				getMatrix().removeLayer(obj);
				getMatrix().addLayer((int) neu, obj);
			}
		}

		@Override
		public void doUndo() {
			if (getID() == ADDLAYER) {
				getMatrix().removeLayer(obj);
			} else if (getID() == REMOVELAYER) {
				getMatrix().addLayer((int) neu, obj);
			} else if (getID() == SETLAYER) {
				getMatrix().removeLayer(obj);
				getMatrix().addLayer((int) old, obj);
			}
		}

		@Override
		public String getName() {
			if (getID() == ADDLAYER) {
				return "Layer hinzufügen";
			} else if (getID() == REMOVELAYER) {
				return "Layer entfernen";
			} else if (getID() == SETLAYER) {
				return "Layer verschieben";
			}
			return null;
		}

	}
}
