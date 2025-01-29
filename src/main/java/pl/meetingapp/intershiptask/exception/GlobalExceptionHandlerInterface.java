package pl.meetingapp.intershiptask.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.meetingapp.intershiptask.dto.ErrorResponse;

public interface GlobalExceptionHandlerInterface {

    /**
     * Obsługuje wyjątki walidacyjne (np. błędy walidacji podczas obsługi żądań HTTP).
     * Filtruje komunikaty o błędach, ponieważ Massage dwa razy sie wyświetlał.
     *
     * @param ex wyjątek MethodArgumentNotValidException zawierający informacje o błędach walidacji.
     * @return Odpowiedź HTTP z kodem błędu 400 (Bad Request) oraz szczegółami walidacji.
     */
    ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex);

    /**
     * Obsługuje wyjątki RuntimeException (np. przypadki, gdy nie znaleziono zasobu lub wystąpił problem w logice aplikacji).
     *
     * @param ex wyjątek RuntimeException zawierający szczegóły błędu.
     * @return Odpowiedź HTTP z kodem błędu 500 (Internal Server Error) oraz wiadomością o błędzie.
     */
    ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex);

    /**
     * Obsługuje wyjątki BadRequestException (np. błędne dane wejściowe).
     *
     * @param ex wyjątek BadRequestException zawierający szczegóły błędu.
     * @return Odpowiedź HTTP z kodem błędu 400 (Bad Request) oraz wiadomością o błędzie.
     */
    ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex);

    /**
     * Obsługuje wyjątki IllegalArgumentException (np. gdy żądany zasób, taki jak SWIFT code, nie został znaleziony).
     *
     * @param ex wyjątek IllegalArgumentException zawierający szczegóły błędu.
     * @return Odpowiedź HTTP z kodem błędu 404 (Not Found) oraz wiadomością o błędzie.
     */
    ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex);

    /**
     * Obsługuje wszystkie inne wyjątki, które nie zostały osobno zdefiniowane w tej klasie.
     * Zapewnia domyślną obsługę błędów dla niespodziewanych problemów.
     *
     * @param ex wyjątek Exception zawierający szczegóły błędu.
     * @return Odpowiedź HTTP z kodem błędu 500 (Internal Server Error) oraz ogólnym komunikatem o błędzie.
     */
    ResponseEntity<ErrorResponse> handleGeneralException(Exception ex);
}
