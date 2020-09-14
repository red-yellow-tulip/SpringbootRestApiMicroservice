package base.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HandlerExceptionNotFound extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResponseEntity<Object> exception(HandlerExceptionNotFound exception) {
        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

}
