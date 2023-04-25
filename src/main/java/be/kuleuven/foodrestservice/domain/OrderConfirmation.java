package be.kuleuven.foodrestservice.domain;

import java.time.LocalDateTime;
import java.util.List;

public record OrderConfirmation(
        int id,
        String message,
        String address,
        List<Meal> meals,
        Double total,
        LocalDateTime deliveryTime) {
}
