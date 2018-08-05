package editor.tileMap.objects;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * A sheet of sprites that can be drawn individually
 * 
 * @author Orginal: Kevin Glass -- Complett um gebaut
 */
public class SpriteSheet {
	/** The width of a single element in pixels */
	private int tw;
	/** The height of a single element in pixels */
	private int th;
	/** The margin of the image */
	private int margin = 0;
	/** Subimages */
	private Image[][] subImages;
	/** The spacing between tiles */
	private int spacing;
	/** The target image for this sheet */
	BufferedImage bi;

	public SpriteSheet(BufferedImage image, int tw, int th) {
		bi = image;
		this.tw = tw;
		this.th = th;
		initImpl();
	}

	public SpriteSheet(BufferedImage image, int tw, int th, int spacing,
			int margin) {
		bi = image;
		this.tw = tw;
		this.th = th;
		this.spacing = spacing;
		this.margin = margin;
		initImpl();
	}

	public SpriteSheet(BufferedImage image, int tw, int th, int spacing) {
		this(image, tw, th, spacing, 0);
	}

	/**
	 * @see org.newdawn.slick.Image#initImpl()
	 */
	protected void initImpl() {
		if (subImages != null) {
			return;
		}

		int tilesAcross = ((bi.getWidth() - (margin * 2) - tw) / (tw + spacing)) + 1;
		int tilesDown = ((bi.getHeight(null) - (margin * 2) - th) / (th + spacing)) + 1;
		if ((bi.getHeight(null) - th) % (th + spacing) != 0) {
			tilesDown++;
		}

		subImages = new Image[tilesAcross][tilesDown];
		for (int x = 0; x < tilesAcross; x++) {
			for (int y = 0; y < tilesDown; y++) {
				subImages[x][y] = getSprite(x, y);
			}
		}
	}

	protected Image getSprite(int x, int y) {
		try {
			return bi.getSubimage(x * tw, y * th, tw, th);
		} catch (Exception e) {
			// Wenn es auserhalb des Bildes ist.
		}
		return null;
	}

	public Image getSubImage(int x, int y) {
		initImpl();

		if ((x < 0) || (x >= subImages.length)) {
			throw new RuntimeException("SubImage out of sheet bounds: " + x
					+ "," + y);
		}
		if ((y < 0) || (y >= subImages[0].length)) {
			throw new RuntimeException("SubImage out of sheet bounds: " + x
					+ "," + y);
		}

		return subImages[x][y];
	}

	/**
	 * Get the number of sprites across the sheet
	 * 
	 * @return The number of sprites across the sheet
	 */
	public int getHorizontalCount() {
		initImpl();

		return subImages.length;
	}

	public int getVerticalCount() {
		initImpl();

		return subImages[0].length;
	}
	public int getTileHeight(){
		return th;
	}
	public int getTileWight(){
		return tw;
	}
}
