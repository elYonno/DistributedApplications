package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealExistsException;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.ExemptionMechanismException;
import java.util.Collection;
import java.util.Optional;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/cheapest")
    Meal getCheapestMeal() {
        return mealsRepository.getCheapestMeal();
    }

    @GetMapping("restrpc/largest")
    Meal getLargestMeal() {
        return mealsRepository.getLargestMeal();
    }

    /**
     * Adds a new meal to the list. Put is used for adding/overwrite data.
     * <a href="https://stackoverflow.com/questions/630453/what-is-the-difference-between-post-and-put-in-http">...</a>
     * @param newMeal New meal to be added from the request body (JSON).
     * @return Meal added, or null if meal already exists.
     */
    @PutMapping("/restrpc/add")
    Meal addMeal(@RequestBody Meal newMeal) {
        if (mealsRepository.addMeal(newMeal)) return newMeal;
        else throw new MealExistsException(newMeal.getId());
    }

    /**
     * Edit an existing meal from. Post is used to update data.
     * <a href="https://stackoverflow.com/questions/630453/what-is-the-difference-between-post-and-put-in-http">...</a>
     * @param id ID of the meal to be edited from the path variable.
     * @param newMeal New data from the request body (JSON).
     * @return The new meal
     */
    @PostMapping("/restrpc/update/{id}")
    Meal updateMeal(@PathVariable String id, @RequestBody Meal newMeal) {
        Meal updated = mealsRepository.updateMeal(id, newMeal);

        if (updated != null) return newMeal;
        else throw new MealNotFoundException(id);
    }

    @DeleteMapping("/restrpc/delete/{id}")
    Meal deleteMeal(@PathVariable String id) {
        if (mealsRepository.deleteMeal(id)) return null;
        else throw new MealNotFoundException(id);
    }
}
