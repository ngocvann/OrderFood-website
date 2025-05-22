package com.foodgo.service;

import com.foodgo.Exception.FoodException;
import com.foodgo.Exception.RestaurantException;
import com.foodgo.model.Category;
import com.foodgo.model.Food;
import com.foodgo.model.Restaurant;
import com.foodgo.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

	public Food createFood(CreateFoodRequest req,Category category,
						   Restaurant restaurant) throws FoodException, RestaurantException;

	void deleteFood(Long foodId) throws FoodException;
	
	public List<Food> getRestaurantsFood(Long restaurantId,
			boolean isVegetarian, boolean isNonveg, boolean isSeasonal,String foodCategory) throws FoodException;
	
	public List<Food> searchFood(String keyword);
	
	public Food findFoodById(Long foodId) throws FoodException;

	public Food updateAvailibilityStatus(Long foodId) throws FoodException;
}
