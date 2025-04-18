package com.ravuri.calibration.controller;
import com.ravuri.calibration.service.impl.BarcodeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/barcode")
public class BarcodeController {

    private static final Logger LOGGER = LogManager.getLogger(BarcodeController.class);
    @Autowired
    private BarcodeService barcodeService;

    @PostMapping(value = "/read", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> readBarcode(@RequestParam("file") MultipartFile file) {
        LOGGER.info("readBarcode");
        return ResponseEntity.ok(barcodeService.readBarcode(file));
    }

    @PostMapping(value = "/read/{format}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> readBarcodeWithFormat(
            @RequestParam("file") MultipartFile file,
            @PathVariable String format) {
        LOGGER.info("ReadBarCodeWithFormat");
        return ResponseEntity.ok(barcodeService.readBarcodeWithFormat(file, format));
    }

    @GetMapping("/{instrumentId}")
    public ResponseEntity<byte[]> generateBarcode(@PathVariable String instrumentId) {
        LOGGER.info("Generating barcode for instrument: {}", instrumentId);

        byte[] barcodeImage = barcodeService.generateBarcode(instrumentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDispositionFormData("attachment", "barcode-" + instrumentId + ".png");

        return ResponseEntity.ok()
                .headers(headers)
                .body(barcodeImage);
    }

}
