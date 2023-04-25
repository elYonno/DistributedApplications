package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.domain.OrderConfirmation;
import be.kuleuven.foodrestservice.exceptions.InvalidOrderException;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/rest/meals/{id}")
    EntityModel<Meal> getMealById(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id).orElseThrow(() -> new MealNotFoundException(id));

        return mealToEntityModel(id, meal);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }

    @GetMapping("/rest/cheapest")
    EntityModel<Meal> getCheapestMeal() {
        Meal meal = mealsRepository.getCheapestMeal();
        return mealToEntityModel(meal.getId(), meal);
    }

    @GetMapping("/rest/largest")
    EntityModel<Meal> getLargestMeal() {
        Meal meal = mealsRepository.getLargestMeal();
        return mealToEntityModel(meal.getId(), meal);
    }

    @PutMapping("/rest/add")
    ResponseEntity<?> addMeal(@RequestBody Meal newMeal) {
        if (mealsRepository.addMeal(newMeal)) {
            // success
            EntityModel<Meal> model = mealToEntityModel(newMeal.getId(), newMeal);
            return ResponseEntity.ok(model);
        } else {
            // fail
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    @PostMapping("/rest/update/{id}")
    ResponseEntity<?> updateMeal(@PathVariable String id, @RequestBody Meal newMeal) {
        Meal meal = mealsRepository.updateMeal(id, newMeal);
        if (meal != null) {
            EntityModel<Meal> model = mealToEntityModel(meal.getId(), meal);
            return ResponseEntity.ok(model);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/rest/delete/{id}")
    ResponseEntity<?> deleteMeal(@PathVariable String id) {
        if (mealsRepository.deleteMeal(id))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/rest/addOrder")
    ResponseEntity<?> addOrder(@RequestBody Order order) {
        try {
            OrderConfirmation confirmation = mealsRepository.addOrder(order);
            return ResponseEntity.ok(confirmationToEntityModel(confirmation.id(), confirmation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/rest/orders/{id}")
    EntityModel<?> getOrder(@PathVariable int id) {
        OrderConfirmation order = mealsRepository.findOrderConfirmation(id).orElseThrow(() -> new InvalidOrderException("Order not found"));

        return confirmationToEntityModel(id, order);
    }

    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"));
    }

    private EntityModel<OrderConfirmation> confirmationToEntityModel(int id, OrderConfirmation order) {
        return EntityModel.of(order,
                linkTo(methodOn(MealsRestController.class).getOrder(id)).withSelfRel());
    }

}
