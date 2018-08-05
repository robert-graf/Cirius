package database;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class XML {
	private File file;

	// List neue Datei, Erstellt rootElemtn und schickt es weiter
	@SuppressWarnings("resource")
	public void read(File f) {
		setFile(f);
		try {
			if (!f.exists() || new FileInputStream(f).available() == 0) {
				fileDoesNotExist();
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		try {
			// DocumenBuilderFactory
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			// DocumentBuilder
			DocumentBuilder builder = factory.newDocumentBuilder();
			// Document
			Document doc = builder.parse(f);
			// rootElement
			Element rootElement = doc.getDocumentElement();
			read(rootElement);
		} catch (Exception ex) {
			Toolkit.getDefaultToolkit().beep();
			ex.printStackTrace();
		}
	}

	// List Elemente und Kinder von Datei-Root oder von Andere XML-Klasse
	public abstract void read(Element rootElement);

	// Geladene Datei überspeichern
	public void write() {
		write(getFile());
	};

	// Datei überspeichen
	public void write(File f) {
		try {
			/** START */
			// DocumenBuilderFactory
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			// DocumentBuilder
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			// Document
			Document xmlDoc = docBuilder.newDocument();
			// TileSet bekommen
			Element rootElement = xmlDoc.createElement(getXMLName());
			/** CUSTOM */
			write(rootElement, xmlDoc);
			/** END */
			// Document anhängen
			xmlDoc.appendChild(rootElement);
			// Set OutputFormen
			OutputFormat out = new OutputFormat(xmlDoc);
			out.setIndenting(true);
			// Declare the file
			File xmlFile = f;
			// Declari the FileOutputStream
			FileOutputStream outStream = new FileOutputStream(xmlFile);
			// XMLSerializer
			XMLSerializer serializer = new XMLSerializer(outStream, out);
			// the secified Output Format
			serializer.serialize(xmlDoc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	};

	// RootElement von Datei oder Element von anderem XML Document
	public abstract void write(Element rootElement, Document xmlDoc);

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public abstract String getXMLName();

	public abstract void fileDoesNotExist();
}
