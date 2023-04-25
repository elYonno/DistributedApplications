package be.kuleuven.foodrestservice.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record OrderConfirmation(
        String message,
        String address,
        List<Meal> meals,
        Double total,
        LocalDateTime deliveryTime) {

    @Override
    public int hashCode() {
        return Objects.hash(address, deliveryTime.getHour(), deliveryTime.getMinute());
    }
}
