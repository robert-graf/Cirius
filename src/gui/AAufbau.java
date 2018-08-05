package gui;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import interfaces.FileHolder;

public class AAufbau {

	public static MainFrame f;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 1. Gui laden
		String laf = Messages.getString("AAufbau.LookAndFeel"); //$NON-NLS-1$		
		// 1.1 Look and Feel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				// System.out.println(info.getName());
				if (info.getName().contains(laf)) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
//		try {
//			int i = UIManager.getInstalledLookAndFeels().length;
//			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[new Random().nextInt(i)].getClassName());
//		} catch (Exception e) {
//			try {
//				UIManager.setLookAndFeel(UIManager
//						.getSystemLookAndFeelClassName());
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		}
		f = new MainFrame();
		new DropFile();

	}

	public static void addOpenFile(JComponent p, FileHolder h) {
		f.addOpenFile(p, h);
	}
}
