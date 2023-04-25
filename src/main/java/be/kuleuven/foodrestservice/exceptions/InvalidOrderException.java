package be.kuleuven.foodrestservice.exceptions;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super("Invalid order! " + message);
    }
}
