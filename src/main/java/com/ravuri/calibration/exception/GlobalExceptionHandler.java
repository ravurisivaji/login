//package com.ravuri.calibration.exception;
//
//import jakarta.validation.ConstraintViolationException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.multipart.MaxUploadSizeExceededException;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestControllerAdvice
//
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
////    @ExceptionHandler(Exception.class)
////    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
////        logger.error("Internal Server Error: {}", ex.getMessage(), ex);
////
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////
////        String body = String.format(
////                "{\"type\": \"about:blank\", \"title\": \"Internal Server Error\", \"status\": 500, \"detail\": \"%s\", \"instance\": \"%s\"}",
////                ex.getMessage(), request.getDescription(false)
////        );
////        return ResponseEntity.status(500).headers(headers).body(body);
////    }
////
////    @ExceptionHandler(OfficeNotFoundException.class)
////    public ResponseEntity<ErrorResponse> handleOfficeNotFoundException(
////            OfficeNotFoundException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.NOT_FOUND.value(),
////                "Not Found",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
////    }
////
////    @ExceptionHandler(DuplicateOfficeException.class)
////    public ResponseEntity<ErrorResponse> handleDuplicateOfficeException(
////            DuplicateOfficeException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.CONFLICT.value(),
////                "Conflict",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
////    }
////
////    @ExceptionHandler(ValidationException.class)
////    public ResponseEntity<ErrorResponse> handleValidationException(
////            ValidationException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.BAD_REQUEST.value(),
////                "Bad Request",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
////    }
////
////    @ExceptionHandler(MethodArgumentNotValidException.class)
////    public ResponseEntity<Map<String, Object>> handleValidationErrors(
////            MethodArgumentNotValidException ex,
////            WebRequest request) {
////        Map<String, Object> response = new HashMap<>();
////        Map<String, String> errors = new HashMap<>();
////
////        ex.getBindingResult().getFieldErrors().forEach(error ->
////                errors.put(error.getField(), error.getDefaultMessage())
////        );
////
////        response.put("timestamp", LocalDateTime.now());
////        response.put("status", HttpStatus.BAD_REQUEST.value());
////        response.put("error", "Validation Failed");
////        response.put("errors", errors);
////        response.put("path", request.getDescription(false));
////
////        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
////    }
////
////    @ExceptionHandler(ConstraintViolationException.class)
////    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
////            ConstraintViolationException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.BAD_REQUEST.value(),
////                "Validation Failed",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
////    }
////
////    @ExceptionHandler(Exception.class)
////    public ResponseEntity<ErrorResponse> handleGlobalException(
////            Exception ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.INTERNAL_SERVER_ERROR.value(),
////                "Internal Server Error",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
////    }
////
////    @ExceptionHandler(InstrumentNotFoundException.class)
////    public ResponseEntity<ErrorResponse> handleInstrumentNotFoundException(
////            InstrumentNotFoundException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.NOT_FOUND.value(),
////                "Not Found",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
////    }
////
////    @ExceptionHandler(TechniqueNotFoundException.class)
////    public ResponseEntity<ErrorResponse> handleTechniqueNotFoundException(
////            TechniqueNotFoundException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.NOT_FOUND.value(),
////                "Not Found",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
////    }
////
////    @ExceptionHandler(DuplicateInstrumentException.class)
////    public ResponseEntity<ErrorResponse> handleDuplicateInstrumentException(
////            DuplicateInstrumentException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.CONFLICT.value(),
////                "Conflict",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
////    }
////
////    @ExceptionHandler(DuplicateTechniqueException.class)
////    public ResponseEntity<ErrorResponse> handleDuplicateTechniqueException(
////            DuplicateTechniqueException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.CONFLICT.value(),
////                "Conflict",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
////    }
////
////    @ExceptionHandler(BarcodeException.class)
////    public ResponseEntity<ErrorResponse> handleBarcodeException(
////            BarcodeException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.BAD_REQUEST.value(),
////                "Barcode Error",
////                ex.getMessage(),
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
////    }
////
////    @ExceptionHandler(MaxUploadSizeExceededException.class)
////    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
////            MaxUploadSizeExceededException ex,
////            WebRequest request) {
////        ErrorResponse error = new ErrorResponse(
////                LocalDateTime.now(),
////                HttpStatus.BAD_REQUEST.value(),
////                "File Size Error",
////                "File size exceeds maximum allowed size",
////                request.getDescription(false)
////        );
////        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
////    }
////
//@ExceptionHandler(InstrumentNotFoundException.class)
//public ResponseEntity<ErrorResponse> handleInstrumentNotFoundException(
//        InstrumentNotFoundException ex,
//        WebRequest request) {
//    ErrorResponse error = new ErrorResponse(
//            LocalDateTime.now(),
//            HttpStatus.NOT_FOUND.value(),
//            "Not Found",
//            ex.getMessage(),
//            request.getDescription(false)
//    );
//    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//}
//
//    @ExceptionHandler(TechniqueNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleTechniqueNotFoundException(
//            TechniqueNotFoundException ex,
//            WebRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.NOT_FOUND.value(),
//                "Not Found",
//                ex.getMessage(),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(DuplicateInstrumentException.class)
//    public ResponseEntity<ErrorResponse> handleDuplicateInstrumentException(
//            DuplicateInstrumentException ex,
//            WebRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//                "Conflict",
//                ex.getMessage(),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(DuplicateTechniqueException.class)
//    public ResponseEntity<ErrorResponse> handleDuplicateTechniqueException(
//            DuplicateTechniqueException ex,
//            WebRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.CONFLICT.value(),
//                "Conflict",
//                ex.getMessage(),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//    }
//
//    @ExceptionHandler(ValidationException.class)
//    public ResponseEntity<ErrorResponse> handleValidationException(
//            ValidationException ex,
//            WebRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                "Bad Request",
//                ex.getMessage(),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
//            MethodArgumentNotValidException ex,
//            WebRequest request) {
//        Map<String, Object> response = new HashMap<>();
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage())
//        );
//
//        response.put("timestamp", LocalDateTime.now());
//        response.put("status", HttpStatus.BAD_REQUEST.value());
//        response.put("error", "Validation Failed");
//        response.put("errors", errors);
//        response.put("path", request.getDescription(false));
//
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
//            ConstraintViolationException ex,
//            WebRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                "Validation Failed",
//                ex.getMessage(),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleAllExceptions(
//            Exception ex,
//            WebRequest request) {
//        ErrorResponse error = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "Internal Server Error",
//                ex.getMessage(),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//}
