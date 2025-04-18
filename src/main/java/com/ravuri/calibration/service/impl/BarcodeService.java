package com.ravuri.calibration.service.impl;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.ravuri.calibration.exception.BarcodeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ravuri.calibration.constant.CalibrationConstants.HEIGHT;
import static com.ravuri.calibration.constant.CalibrationConstants.WIDTH;

@Service
public class BarcodeService {

    private static final Logger LOGGER = LogManager.getLogger(BarcodeService.class);
    public Map<String, String> readBarcode(MultipartFile file) {
        LOGGER.info("readBarCode");
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new BarcodeException("Could not read image file");
            }

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);

            Map<String, String> response = new HashMap<>();
            response.put("format", result.getBarcodeFormat().toString());
            response.put("text", result.getText());

            return response;
        } catch (NotFoundException e) {
            throw new BarcodeException("No barcode found in the image");
        } catch (IOException e) {
            throw new BarcodeException("Error reading image file: " + e.getMessage());
        } catch (Exception e) {
            throw new BarcodeException("Error processing barcode: " + e.getMessage());
        }
    }

    public Map<String, String> readBarcodeWithFormat(MultipartFile file, String format) {
        LOGGER.info("readBarCodeWithFormat");
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new BarcodeException("Could not read image file");
            }

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.POSSIBLE_FORMATS, getBarcodeFormat(format));

            Reader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap, hints);

            Map<String, String> response = new HashMap<>();
            response.put("format", result.getBarcodeFormat().toString());
            response.put("text", result.getText());

            return response;
        } catch (NotFoundException e) {
            throw new BarcodeException("No barcode found in the image");
        } catch (IOException e) {
            throw new BarcodeException("Error reading image file: " + e.getMessage());
        } catch (Exception e) {
            throw new BarcodeException("Error processing barcode: " + e.getMessage());
        }
    }

    private BarcodeFormat getBarcodeFormat(String format) {
        LOGGER.info("getBarcodeFormat");
        try {
            return BarcodeFormat.valueOf(format.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BarcodeException("Invalid barcode format: " + format);
        }
    }

    public byte[] generateBarcode(String instrumentId) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    instrumentId,
                    BarcodeFormat.CODE_128,
                    WIDTH,
                    HEIGHT
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            LOGGER.info("Generated barcode for instrument: {}", instrumentId);
            return outputStream.toByteArray();

        } catch (WriterException e) {
            LOGGER.error("Error generating barcode for instrument {}: {}", instrumentId, e.getMessage());
            throw new RuntimeException("Failed to generate barcode: " + e.getMessage());
        } catch (IOException e) {
            LOGGER.error("Error writing barcode image for instrument {}: {}", instrumentId, e.getMessage());
            throw new RuntimeException("Failed to write barcode image: " + e.getMessage());
        }
    }
}
