package be.kuleuven.foodrestservice.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {

    protected String address;
    protected List<String> meals;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getMeals() {
        if (meals == null)
            meals = new ArrayList<>();

        return meals;
    }

    public void setMeals(List<String> meals) {
        this.meals = meals;
    }
}
