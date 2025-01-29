package pl.meetingapp.intershiptask.exception.impl;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import pl.meetingapp.intershiptask.dto.ErrorResponse;
import pl.meetingapp.intershiptask.exception.GlobalExceptionHandlerInterface;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler implements GlobalExceptionHandlerInterface {

    /**
     * Obsługuje wyjątki walidacyjne (np. błędy walidacji podczas obsługi żądań HTTP).
     * Filtruje komunikaty o błędach, ponieważ Massage dwa razy sie wyświetlał.
     *
     * @param ex wyjątek MethodArgumentNotValidException zawierający informacje o błędach walidacji.
     * @return Odpowiedź HTTP z kodem błędu 400 (Bad Request) oraz szczegółami walidacji.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");

        List<String> customMessages = bindingResult.getFieldErrors().stream()
                .filter(fieldError -> fieldError.getDefaultMessage() != null && !fieldError.getDefaultMessage().contains("must not be blank"))
                .map(fieldError -> fieldError.getField() + " - " + fieldError.getDefaultMessage())
                .distinct()
                .toList();

        for (String message : customMessages) {
            errorMessage.append(message).append("; ");
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_ERROR",
                errorMessage.toString().trim()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątki RuntimeException (np. przypadki, gdy nie znaleziono zasobu lub wystąpił problem w logice aplikacji).
     *
     * @param ex wyjątek RuntimeException zawierający szczegóły błędu.
     * @return Odpowiedź HTTP z kodem błędu 500 (Internal Server Error) oraz wiadomością o błędzie.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Sprawdzic
    /**
     * Obsługuje wyjątki BadRequestException (np. błędne dane wejściowe).
     *
     * @param ex wyjątek BadRequestException zawierający szczegóły błędu.
     * @return Odpowiedź HTTP z kodem błędu 400 (Bad Request) oraz wiadomością o błędzie.
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "BAD_REQUEST",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Obsługuje wyjątki IllegalArgumentException (np. gdy żądany zasób, taki jak SWIFT code, nie został znaleziony).
     *
     * @param ex wyjątek IllegalArgumentException zawierający szczegóły błędu.
     * @return Odpowiedź HTTP z kodem błędu 404 (Not Found) oraz wiadomością o błędzie.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "NOT_FOUND",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Obsługuje wszystkie inne wyjątki, które nie zostały osobno zdefiniowane w tej klasie.
     * Zapewnia domyślną obsługę błędów dla niespodziewanych problemów.
     *
     * @param ex wyjątek Exception zawierający szczegóły błędu.
     * @return Odpowiedź HTTP z kodem błędu 500 (Internal Server Error) oraz ogólnym komunikatem o błędzie.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "ERROR",
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
