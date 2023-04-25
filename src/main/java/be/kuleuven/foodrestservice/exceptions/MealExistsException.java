package be.kuleuven.foodrestservice.exceptions;

public class MealExistsException extends RuntimeException {
    public MealExistsException(String id) {
        super("Meal " + id + " already exists, use the update method.");
    }
}
