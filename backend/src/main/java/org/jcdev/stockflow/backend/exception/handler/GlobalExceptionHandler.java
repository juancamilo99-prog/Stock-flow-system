package org.jcdev.stockflow.backend.exception.handler;

import org.jcdev.stockflow.backend.dto.responses.ExceptionResponse;
import org.jcdev.stockflow.backend.exception.CambioNoDetectadoException;
import org.jcdev.stockflow.backend.exception.RecursoDuplicadoException;
import org.jcdev.stockflow.backend.exception.RecursoNoEncontradoException;
import org.jcdev.stockflow.backend.exception.TransicionEstadoInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFound(final RecursoNoEncontradoException recursoNoEncontradoException) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.NOT_FOUND.value(), "RECURSO_NO_ENCONTRADO", recursoNoEncontradoException.getMessage()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ExceptionResponse> handleResourcesDuplicated(final RecursoDuplicadoException recursoDuplicadoException) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.CONFLICT.value(), "RECURSO_DUPLICADO", recursoDuplicadoException.getMessage()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CambioNoDetectadoException.class)
    public ResponseEntity<ExceptionResponse> changeNotDetected(final CambioNoDetectadoException cambioNoDetectadoException) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.UNPROCESSABLE_CONTENT.value(), "CAMBIO_NO_DETECTADO", cambioNoDetectadoException.getMessage()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    public ResponseEntity<ExceptionResponse> invalidStateTransition(final TransicionEstadoInvalidaException transicionEstadoInvalidaException) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),  "TRANSICION_INVALIDA", transicionEstadoInvalidaException.getMessage()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNPROCESSABLE_CONTENT);
    }
}
