package apap.ti.appointment2206082612.restcontroller;

import apap.ti.appointment2206082612.restdto.response.BaseResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import java.util.Date;

@RestControllerAdvice
public class BaseRestController {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(final NoResourceFoundException ex) {
        var baseResponseDTO = new BaseResponseDTO<Void>();
        baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
        baseResponseDTO.setMessage("Path URI tidak ditemukan");
        baseResponseDTO.setData(null);
        baseResponseDTO.setTimestamp(new Date());

        ex.printStackTrace();

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        var baseResponseDTO = new BaseResponseDTO<Void>();
        baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponseDTO.setMessage(String.format("Terjadi error saat membaca request body. Error: %s", ex.getMessage()));
        baseResponseDTO.setData(null);
        baseResponseDTO.setTimestamp(new Date());

        ex.printStackTrace();

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(final Exception ex) {
        var baseResponseDTO = new BaseResponseDTO<Void>();
        baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        baseResponseDTO.setMessage(String.format("Terjadi error pada server. Error: %s", ex.getMessage()));
        baseResponseDTO.setData(null);
        baseResponseDTO.setTimestamp(new Date());

        ex.printStackTrace();

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
