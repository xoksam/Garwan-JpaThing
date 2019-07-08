package garwan.Project.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends RuntimeException {

    public CustomNotFoundException(Class c, String fieldName, String fieldValue) {
        super(c.getSimpleName() + " with " + fieldName + "='" + fieldValue + "' not found");
    }

    public CustomNotFoundException(String message) {
        super(message);
    }

}
