package toolbars;

import static toolbars.Toolbar.createMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

import editor.DefaultEditorPanel.Matrix;
import editor.rpg.GUIMapEditStart;
import editor.tools.GreifTool;
import gui.AAufbau;

public class DialogEinstellungen {
	public static String name = "Einstellungen";
	static {
		final JSpinner spinner1 = new JSpinner(new SpinnerNumberModel(
				Matrix.getRast(), 2, Integer.MAX_VALUE, 1));
		final Object[] raster = { new JLabel("Rasterabstand in Pixel:"),
				spinner1 };

		createMenuItem("Punktfang", "chess_horse", 16, name,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						((JToggleButton) ((GUIMapEditStart) AAufbau.f.CurrendPanel).drawSettings
								.getList().get(0)).doClick();
					}
				}, KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F2)));
		createMenuItem("ORTHOGONAL", "chess_tower", 16, name,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						((JToggleButton) ((GUIMapEditStart) AAufbau.f.CurrendPanel).drawSettings
								.getList().get(1)).doClick();
					}
				}, KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F3)));
		createMenuItem("ORTHOGONAL_OFF_OTHERPOINT", "chess_tower", 16, name,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						((JToggleButton) ((GUIMapEditStart) AAufbau.f.CurrendPanel).drawSettings
								.getList().get(2)).doClick();
					}
				}, KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F4)));
		createMenuItem("EINGABEHILFE", "canvas", 16, name,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						((JToggleButton) ((GUIMapEditStart) AAufbau.f.CurrendPanel).drawSettings
								.getList().get(3)).doClick();
					}
				}, KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F5)));
		createMenuItem("Raster größe", "grid", 16, name, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				((JToggleButton) ((GUIMapEditStart) AAufbau.f.CurrendPanel).drawSettings
						.getList().get(4)).doClick();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F7)));
		createMenuItem("Raster", "grid", 16, name, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Matrix m = ((GUIMapEditStart) AAufbau.f.CurrendPanel)
						.getMatrix();
				GreifTool.RASTER = !GreifTool.RASTER;
				spinner1.setValue(Matrix.getRast());
				String[] o1 = { "OK", "Doch nicht" };
				int i = JOptionPane.showOptionDialog(m.getPanel(), raster,
						"Raster Größe", 0, JOptionPane.QUESTION_MESSAGE, null,
						o1, o1[0]);
				if (i == 0) {
					Matrix.setRast((int) spinner1.getValue());
				}
			}
		}, KeyStroke.getKeyStroke(KeyEvent.getKeyText(' ')));
	}

}
