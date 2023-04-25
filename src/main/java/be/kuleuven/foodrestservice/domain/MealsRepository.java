package be.kuleuven.foodrestservice.domain;

import be.kuleuven.foodrestservice.exceptions.InvalidOrderException;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class MealsRepository {
    // map: id -> meal
    private static final Map<String, Meal> meals = new HashMap<>();

    private static final Map<Integer, OrderConfirmation> orders = new HashMap<>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setId("5268203c-de76-4921-a3e3-439db69c462a");
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealType(MealType.MEAT);
        a.setKcal(1100);
        a.setPrice((10.00));

        meals.put(a.getId(), a);

        Meal b = new Meal();
        b.setId("4237681a-441f-47fc-a747-8e0169bacea1");
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealType(MealType.VEGAN);
        b.setKcal(637);
        b.setPrice((7.00));

        meals.put(b.getId(), b);

        Meal c = new Meal();
        c.setId("cfd1601f-29a0-485d-8d21-7607ec0340c8");
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealType(MealType.FISH);
        c.setKcal(950);
        c.setPrice(5.00);

        meals.put(c.getId(), c);
    }

    public Optional<Meal> findMeal(String id) {
        Assert.notNull(id, "The meal id must not be null");
        Meal meal = meals.get(id);
        return Optional.ofNullable(meal);
    }

    public List<Meal> findMeals(List<String> names) throws MealNotFoundException {
        List<Meal> mealList = new ArrayList<>(names.size());

        for (String name : names) {
            mealList.add(
                    findMeal(name)
                    .orElseThrow(()-> new MealNotFoundException(name))
            );
        }

        return mealList;
    }

    public Collection<Meal> getAllMeal() {
        return meals.values();
    }

    public Meal getCheapestMeal() {
        return meals.values()
                .stream()
                .min(Comparator.comparing(Meal::getPrice))
                .orElseThrow();
    }

    public Meal getLargestMeal() {
        return meals.values()
                .stream()
                .max(Comparator.comparing(Meal::getKcal))
                .orElseThrow();
    }

    public boolean addMeal(Meal meal) {
        if (meal == null || meal.getId() == null)
            return false;

        if (meals.containsKey(meal.id)) {
            return false;
        } else {
            meals.put(meal.id, meal);
            return true;
        }
    }

    public Meal updateMeal(String id, Meal newMeal) {
        if (id == null || newMeal == null)
            return null;

        Meal oldMeal = meals.get(id);

        if (oldMeal == null)
            return null;
        else {
            oldMeal.update(newMeal);
            return oldMeal;
        }
    }

    public boolean deleteMeal(String id) {
        if (id == null)
            return false;
        else {
            AtomicBoolean result = new AtomicBoolean(false);

            findMeal(id).ifPresentOrElse(meal -> {
                meals.remove(id);
                result.set(true);
            }, ()-> result.set(false));

            return result.get();
        }
    }

    /**
     * Open the order and generates a reply
     * @param order Message order
     * @return Order reply, including order total
     * @throws MealNotFoundException thrown when one of the meals in the order is not found
     * @throws InvalidOrderException thrown when the order is invalid (null or empty)
     */
    public OrderConfirmation addOrder(Order order)  {
        if (order == null)
            throw new InvalidOrderException("Order is null");

        List<String> orderList = order.getMeals();
        if (orderList.size() == 0)
            throw new InvalidOrderException("Order is empty");
        else {
            List<Meal> orderMeals = findMeals(orderList);

            double total = orderMeals.stream()
                    .mapToDouble(Meal::getPrice)
                    .sum();

            LocalDateTime time = LocalDateTime.now().plusMinutes(30);

            OrderConfirmation confirmation = new OrderConfirmation(
                    String.format("Thank you for your order! The total is £%.2f. " +
                            "We will deliver to your address, %s, " +
                            "at approximately %d:%d",
                            total, order.getAddress(), time.getHour(), time.getMinute()),
                    order.getAddress(), orderMeals, total, time
            );

            if (orders.containsKey(confirmation.hashCode()))
                throw new InvalidOrderException("Order already exists");

            orders.put(confirmation.hashCode(), confirmation);

            return confirmation;
        }
    }
}
