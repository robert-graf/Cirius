package toolbars;

import editor.DefaultEditorPanel.Matrix;
import editor.rpg.TiledMapManager;
import gui.AAufbau;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class DialogAbout {
	static ActionListener about = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JEditorPane pane = new JEditorPane();
			pane.setContentType("text/html");
			pane.setText("© RubberOo<br>"
					+ "this product comes with no warranty<br>"
					+ "Icons by FatCow Web Hosting<br>"
					+ "<a href=\"http://www.fatcow.com/\">http://www.fatcow.com/</a><br>"
					+ "for used other license see at license-folder");
			// license
			pane.setPreferredSize(new Dimension(300, 100));
			pane.setEditable(false);
			pane.addHyperlinkListener(new HyperlinkListener() {

				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					try {
						if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
							Desktop.getDesktop().browse(e.getURL().toURI());
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}

				}
			});
			JOptionPane.showMessageDialog(AAufbau.f, pane);
		}
	};
	public static String name = "About";
	public static String nameUIM = "Look and Feel";

	static void init() {
		Toolbar.createMenuItem("About", "info_rhombus", 16, name, about,
				KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F1)));
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			Toolbar.createMenuItem(info.getName(), "", 0, nameUIM,
					new LookAndFeelSetter(info), null);
		}
		JMenu uiManager = Toolbar.get(nameUIM);
		Toolbar.get(name).add(uiManager, 1);
	}

	public static class LookAndFeelSetter implements ActionListener {
		public LookAndFeelSetter(LookAndFeelInfo info) {
			className = info;
		}

		LookAndFeelInfo className;

		@Override
		public void actionPerformed(ActionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						UIManager.setLookAndFeel(className.getClassName());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (UnsupportedLookAndFeelException e) {
						e.printStackTrace();
					}
					SwingUtilities.updateComponentTreeUI(AAufbau.f);
					SwingUtilities
							.updateComponentTreeUI(TiledMapManager.THAT.dialog);
					for (int i = 0; Matrix.getUpdate(i) != null; i++) {
						if (Matrix.getUpdate(i) instanceof Component) {
							SwingUtilities
									.updateComponentTreeUI((Component) Matrix
											.getUpdate(i));
						}
					}
					for (JWindow w : Toolbar.toolbars) {
						SwingUtilities.updateComponentTreeUI(w);
					}
					for (JMenu w : Toolbar.menu.values()) {
						SwingUtilities.updateComponentTreeUI(w);
					}
				}
			});

		}
	}
}
