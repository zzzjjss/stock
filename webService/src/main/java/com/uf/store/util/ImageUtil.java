package com.uf.store.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

public class ImageUtil {
	public static boolean resize(File sourceFile, File targetFile, int targetW, int targetH) {
		try {
			String fileType=FilenameUtils.getExtension(targetFile.getName());
			BufferedImage source = ImageIO.read(sourceFile);
			int type = source.getType();
			BufferedImage target = null;
			double sx = (double) targetW / source.getWidth();
			double sy = (double) targetH / source.getHeight();

			if (type == BufferedImage.TYPE_CUSTOM) {
				ColorModel cm = source.getColorModel();
				WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
				boolean alphaPremultiplied = cm.isAlphaPremultiplied();
				target = new BufferedImage(cm, raster, alphaPremultiplied, null);
			} else {
				target = new BufferedImage(targetW, targetH, type);
			}
			Graphics2D g = target.createGraphics();
			// smoother than exlax:
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
			g.dispose();
			if (!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			ImageIO.write(target,fileType ,targetFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
