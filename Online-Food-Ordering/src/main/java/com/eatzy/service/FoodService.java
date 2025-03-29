package com.eatzy.service;

import com.eatzy.model.Category;
import com.eatzy.model.Food;
import com.eatzy.model.Restaurant;
import com.eatzy.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);

    void deleteFood(Long foodId) throws Exception;

    public List<Food> getRestaurantFoods(Long restaurantId,
                                         boolean isVegitarian,
                                         boolean isNonveg,
                                         boolean isSeasonal,
                                         String foodCategory);

    public  List<Food> searchFood(String keyword);
    public Food findFoodById(Long foodId) throws Exception;

    public Food updateAvailibilityStatus(Long foodId) throws Exception;

}

