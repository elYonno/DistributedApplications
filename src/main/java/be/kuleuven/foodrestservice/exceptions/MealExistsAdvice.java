package be.kuleuven.foodrestservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class MealExistsAdvice {

    @ResponseBody
    @ExceptionHandler(MealExistsException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    String mealExistsHandler(MealExistsException ex) {
        return ex.getMessage();
    }
}
