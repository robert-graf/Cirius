package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "gui.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);
	private static LanguageBundle resource;
	static {
		try {
			resource = new LanguageBundle();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Messages() {
	}

	public static String getString(String key) {
		try {
			if (resource.containsKey(key)) {
				return resource.getString(key);
			}
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static void setProperty(String key, String value) {
		// resource
	}

	private static class LanguageBundle extends PropertyResourceBundle {
		private static final File cuntryFile = new File(
				System.getProperty("user.language") + ".properties");
		static {
			if (!cuntryFile.exists()) {
				try {
					cuntryFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public LanguageBundle() throws FileNotFoundException, IOException {
			super(new FileReader(cuntryFile));
			// System.out.println(cuntryFile);
		}
	}
}
