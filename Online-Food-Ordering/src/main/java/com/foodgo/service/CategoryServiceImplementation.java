package com.foodgo.service;

import com.foodgo.Exception.RestaurantException;
import com.foodgo.model.Category;
import com.foodgo.model.Restaurant;
import com.foodgo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImplementation implements CategoryService {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category createCategory(String name, Long restaurantId) throws RestaurantException {
		Restaurant restaurant = restaurantService.findRestaurantById(restaurantId); // ✅ lấy theo restaurantId

		Category createdCategory = new Category();
		createdCategory.setName(name);
		createdCategory.setRestaurant(restaurant);

		return categoryRepository.save(createdCategory);
	}

	@Override
	public List<Category> findCategoryByRestaurantId(Long id) throws RestaurantException {
		Restaurant restaurant=restaurantService.findRestaurantById(id);
		return categoryRepository.findByRestaurantId(id);
	}

	@Override
	public Category findCategoryById(Long id) throws RestaurantException {
		Optional<Category> opt=categoryRepository.findById(id);
		
		if(opt.isEmpty()) {
			throw new RestaurantException("category not exist with id "+id);
		}
		
		return opt.get();
	}

}
