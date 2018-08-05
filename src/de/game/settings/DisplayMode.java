package de.game.settings;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class DisplayMode {
	private static String	name = 			"#Demo#";
	private static int		widht = 		800;
	private static int 		height = 		600;
	private static int 		fps = 			90;
	private static boolean 	fullscreen = 	false;
	private static boolean 	vsync = 		true;
	private static boolean 	ClearEachFrame= true;
	private static AppGameContainer c;
	public static void init(AppGameContainer c){
		DisplayMode.c = c;
		
		c.setTargetFrameRate(DisplayMode.getFPS());
		c.setVSync(DisplayMode.isVsync());
		c.setClearEachFrame(DisplayMode.isClearEachFrame());
		updateSize();
		System.err.println("Die DisplayModeClass muss noch erstellt werden");
	}
	public static String getName() {
		return name;
	}
	public static void setName(String name1) {
		if(c!=null)c.setTitle(name);
		name = name1;
	}
	public static int getWidht() {
		return widht;
	}
	public static void setWidht(int widht) {
		
		DisplayMode.widht = widht;
		if(c!=null)updateSize();
	}
	public static int getHeight() {
		
		return height;
	}
	public static int getFPS() {
		return fps;
	}
	public static boolean isFullscreen() {
		return fullscreen;
	}
	public static boolean isVsync() {
		return vsync;
	}
	public static void setHeight(int height) {
		
		DisplayMode.height = height;
		if(c!=null)updateSize();
	}
	public static void setFPS(int fps) {
		if(c!=null)c.setTargetFrameRate(fps);
		DisplayMode.fps = fps;
	}
	public static void setFullscreen(boolean fullscreen) {
		
		DisplayMode.fullscreen = fullscreen;
		if(c!=null)updateSize();
	}
	public static void setDisplayMode(int w,int h , boolean full){
		DisplayMode.height = h;
		DisplayMode.widht = w;
		DisplayMode.fullscreen = full;	
		if(c!=null)updateSize();
	}
	private static void updateSize() {
		try {
			c.setDisplayMode(DisplayMode.getWidht(),DisplayMode.getHeight(), DisplayMode.isFullscreen());
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void setVsync(boolean vsync) {
		if(c!=null)c.setVSync(vsync);
		DisplayMode.vsync = vsync;
	}
	public static boolean isClearEachFrame() {
		return ClearEachFrame;
	}
	public static void setClearEachFrame(boolean clearEachFrame) {
		if(c!=null)c.setClearEachFrame(clearEachFrame);
		ClearEachFrame = clearEachFrame;
	}
}
