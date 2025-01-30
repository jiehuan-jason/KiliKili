package com.google.zxing;

import javax.microedition.lcdui.Image;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class TextToQRcodeImage {
	private static final int BLACK = 0xFF000000;
	private static final int WHTIE = 0xFFFFFFFF;
	
	static public Image encode(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            int qrWidth = 200;
            int qrHeigth = 200;
            BitMatrix qrBitMatrix = qrCodeWriter.encode(content,
                    BarcodeFormat.QR_CODE, qrWidth, qrHeigth);
            int[] rgb = new int[qrWidth * qrHeigth];
            for (int y = 0; y < qrBitMatrix.getHeight(); y++) {
                for (int x = 0; x < qrWidth; x++) {
                    int offset = y * qrHeigth;
                    rgb[offset + x] = qrBitMatrix.get(x, y) ? BLACK : WHTIE;
                }
            }
            return Image.createRGBImage(rgb, qrWidth, qrHeigth, false);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
