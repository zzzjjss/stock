package com.uf.wechat;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;



public class QRcodeGenerator {
  private static final int WHITE = 0xFFFFFFFF;
  private static final int BLACK = 0xFF000000;

  public File generateQRcodeImageFile(String content, File folder) {
    String uuid = UUID.randomUUID().toString();
    if (!folder.exists()) {
      folder.mkdirs();
    }
    File file = new File(folder, uuid + ".jpg");
    try {
      MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
      Map hints = new HashMap();
      hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
      BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400, hints);
      writeToFile(bitMatrix, "jpg", file);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (file.exists()) {
      return file;
    }
    return null;
  }


  public static BufferedImage toBufferedImage(BitMatrix matrix) {
    int width = matrix.getWidth();
    int height = matrix.getHeight();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
      }
    }
    return image;
  }


  public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
    BufferedImage image = toBufferedImage(matrix);
    if (!ImageIO.write(image, format, file)) {
      throw new IOException("Could not write an image of format " + format + " to " + file);
    }
  }


  public static void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
    BufferedImage image = toBufferedImage(matrix);
    if (!ImageIO.write(image, format, stream)) {
      throw new IOException("Could not write an image of format " + format);
    }
  }
}
