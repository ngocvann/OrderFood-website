package com.foodgo.controller;

import com.foodgo.Exception.RestaurantException;
import com.foodgo.Exception.UserException;
import com.foodgo.model.Category;
import com.foodgo.model.User;
import com.foodgo.model.dto.CategoryRequestDTO;
import com.foodgo.service.CategoryService;
import com.foodgo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

	@Autowired
	public CategoryService categoryService;

	@Autowired
	public UserService userService;

	@PostMapping("/admin/category")
	public ResponseEntity<Category> createdCategory(
			@RequestHeader("Authorization") String jwt,
			@RequestBody CategoryRequestDTO categoryDTO
	) throws RestaurantException, UserException {

		// Kiểm tra user có hợp lệ
		userService.findUserProfileByJwt(jwt);

		// Gọi service tạo category với name + restaurantId
		Category createdCategory = categoryService.createCategory(
				categoryDTO.getName(),
				categoryDTO.getRestaurantId()
		);

		return new ResponseEntity<>(createdCategory, HttpStatus.OK);
	}

	@GetMapping("/category/restaurant/{id}")
	public ResponseEntity<List<Category>> getRestaurantsCategory(
			@PathVariable Long id,
			@RequestHeader("Authorization") String jwt
	) throws RestaurantException, UserException {

		userService.findUserProfileByJwt(jwt);
		List<Category> categories = categoryService.findCategoryByRestaurantId(id);
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
}
