package img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Icons {
	private static HashMap<String, ImageIcon> imageIcon = new HashMap<>();
	static public boolean debugg = true;
	final static String ziel = "/used_icons/";
	static {
		if (new File("editor_lib/").exists()) {
			debugg = true;
		}
		if (debugg) {
			System.out.println("Icons.java ist noch im debugg modus");
		}
	}

	public static ImageIcon getImage(String s, int id) {
		if (s.equals("")) {
			return null;
		}
		if (imageIcon.containsKey(s)) {
			return imageIcon.get(s + id);
		} else {
			ImageIcon i = null;
			try {
//				 i = new ImageIcon(new
//				 Object().getClass().getResource("/img/Farm-fresh/"+id+"/" + s
//				 +".png"));
				i = new ImageIcon(ImageIO.read(new File("icons/Farm-fresh/"
						+ id + "/" + s + ".png")));
//				i = new ImageIcon(ImageIO.read(new File("Cirisus_lib/"
//						+ id + "/" + s + ".png")));
				
				if (debugg) {
					try {
						File file = new File(ziel + id + "/" + s + ".png");
						if (!file.exists()) {
							BufferedImage image = new BufferedImage(
									i.getIconWidth(), i.getIconHeight(),
									BufferedImage.TYPE_4BYTE_ABGR);
							image.getGraphics().drawImage(i.getImage(), 0, 0,
									null);
							ImageIO.write(image, "png", file);
							System.err.println(file + " existiert noch nicht");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} catch (Exception e) {
				System.out.println(s + "   " + id);
				e.printStackTrace();
//				System.out.println(e);
			}
			imageIcon.put(s + id, i);
			return i;
		}
	}
}
