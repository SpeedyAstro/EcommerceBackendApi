package in.astro.exceptions;

import in.astro.payloads.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> resourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();

        APIResponse res = new APIResponse(message, false);

        return new ResponseEntity<APIResponse>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e) {
        String message = e.getMessage();

        APIResponse res = new APIResponse(message, false);

        return new ResponseEntity<APIResponse>(res, HttpStatus.BAD_REQUEST);
    }
}
