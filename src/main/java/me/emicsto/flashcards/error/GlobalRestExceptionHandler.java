package me.emicsto.flashcards.error;

import me.emicsto.flashcards.deck.DeckNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalRestExceptionHandler {

    @ExceptionHandler(DeckNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleDeckNotFoundException(DeckNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage()));
    }
}
