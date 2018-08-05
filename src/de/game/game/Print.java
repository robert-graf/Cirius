package de.game.game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Print extends JTextArea {
	public static JFrame frame;
	boolean kill = false;
	PrintStream defaultOut = System.out;
 	PrintStream defaultErr = System.err;
	 
	public Print() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 300);
		frame.add(new JScrollPane(this));
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frame.dispose();
				System.exit(0);
			}
			
		});
		redirectSystemStreams();
	}
	private void updateTextArea(final String text) {
		  SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		      append(text);
		    }
		  });
		}
		 
		private void redirectSystemStreams() {
		  OutputStream out = new OutputStream() {
		    @Override
		    public void write(int b) throws IOException {
		      updateTextArea(String.valueOf((char) b));
		      defaultErr.write(b);
		    }
		 
		    @Override
		    public void write(byte[] b, int off, int len) throws IOException {
		      updateTextArea(new String(b, off, len));
		      defaultErr.write(b,off,len);
		    }
		 
		    @Override
		    public void write(byte[] b) throws IOException {
		      write(b, 0, b.length);
		      defaultErr.write(b);
		    }
		  };
		  System.setOut(new PrintStream(out, true));
		  System.setErr(new PrintStream(out, true));
		}
	@Override
	protected void finalize() throws Throwable {
		System.setOut(defaultOut);
		System.setErr(defaultErr);
		super.finalize();
	}
}
