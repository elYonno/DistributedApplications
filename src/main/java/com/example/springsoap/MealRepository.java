package com.example.springsoap;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;


import io.foodmenu.gt.webservice.*;


import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MealRepository {
    private static final Map<String, Meal> meals = new HashMap<String, Meal>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealtype(Mealtype.MEAT);
        a.setKcal(1100);
        a.setPrice(19.99);

        meals.put(a.getName(), a);

        Meal b = new Meal();
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealtype(Mealtype.VEGAN);
        b.setKcal(637);
        b.setPrice(8.99);

        meals.put(b.getName(), b);

        Meal c = new Meal();
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealtype(Mealtype.FISH);
        c.setKcal(950);
        c.setPrice(4.99);

        meals.put(c.getName(), c);
    }

    public Meal findMeal(String name) {
        Assert.notNull(name, "The meal's code must not be null");
        return meals.get(name);
    }

    private List<Meal> findMeals(List<String> names) {
        List<Meal> mealList = new ArrayList<>(names.size());

        for (String name : names) {
            Meal meal = findMeal(name);

            if (meal != null)
                mealList.add(meal);
            else
                return null;
        }

        return mealList;
    }

    public Meal findBiggestMeal() {
        if (meals.size() == 0) return null;

        return  meals.values().stream()
                .max(Comparator.comparing(Meal::getKcal))
                .orElseThrow(NoSuchElementException::new);

    }

    public Meal findCheapestMeal() {
        if (meals.size() == 0) return null;

        return  meals.values().stream()
                .min(Comparator.comparing(Meal::getPrice))
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Add up all the orders and return an order message
     * @param order Food to order
     * @return The string response of the order service
     */
    public String compileOrder(Order order) {
        List<String> orderList = order.getMealist();

        if (orderList.size() == 0) {
            return "Order was empty!";
        }
        else {
            List<Meal> orderMeals = findMeals(orderList);

            if (orderMeals == null)
                return "One of the meals are unknown";

            double total = orderMeals.stream()
                    .mapToDouble(Meal::getPrice)
                    .sum();

            LocalDateTime time = LocalDateTime.now().plusMinutes(30);

            return String.format(
                    "Thank you for your order! The total is £%.2f. " +
                            "We will deliver to your address, %s, " +
                            "at approximately %d:%d",
                    total,
                    order.getAddress(),
                    time.getHour(),
                    time.getMinute()
            );
        }
    }

}