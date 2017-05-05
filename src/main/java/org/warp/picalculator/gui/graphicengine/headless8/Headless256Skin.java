package org.warp.picalculator.gui.graphicengine.headless8;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.warp.picalculator.gui.graphicengine.GraphicEngine;
import org.warp.picalculator.gui.graphicengine.Skin;
import org.warp.picalculator.gui.graphicengine.cpu.CPUEngine.CPURenderer;

public class Headless256Skin implements Skin {

	public int[] skinData;
	public int[] skinSize;

	Headless256Skin(String file) throws IOException {
		load(file);
	}

	@Override
	public void load(String file) throws IOException {
		final BufferedImage img = ImageIO.read(this.getClass().getResource("/" + file));
		skinData = getMatrixOfImage(img);
		skinSize = new int[] { img.getWidth(), img.getHeight() };
	}

	public static int[] getMatrixOfImage(BufferedImage bufferedImage) {
		BufferedImage after = new BufferedImage(bufferedImage.getWidth(null), bufferedImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(1f/((float)Headless256Engine.C_MUL_X), 1f/((float)Headless256Engine.C_MUL_Y));
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(bufferedImage, after);
		
		
		final int width = after.getWidth(null);
		final int height = after.getHeight(null);
		final int[] pixels = new int[width * height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = after.getRGB(i, j);
				int r = (rgb >> 16) &0xFF;
				int g = (rgb >> 8) &0xFF;
				int b = rgb &0xFF;
				boolean transparent = ((rgb >> 24) & 0xFF) <= 128;
				pixels[i + j * width] = Headless256Renderer.rgbToX256(r, g, b)|(transparent?Headless256Renderer.TRANSPARENT:0);
			}
		}

		return pixels;
	}

	@Override
	public void initialize(GraphicEngine d) {
		
	}

	@Override
	public void use(GraphicEngine d) {
		((Headless256Renderer) d.getRenderer()).currentSkin = this;
	}


}
