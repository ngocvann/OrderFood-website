package com.foodgo.controller;

import com.foodgo.Exception.CartException;
import com.foodgo.Exception.OrderException;
import com.foodgo.Exception.RestaurantException;
import com.foodgo.Exception.UserException;
import com.foodgo.model.Order;
import com.foodgo.model.PaymentResponse;
import com.foodgo.model.User;
import com.foodgo.request.CreateOrderRequest;
import com.foodgo.service.OrderService;
import com.foodgo.service.UserService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;
	
    @PostMapping("/order")
	public ResponseEntity<PaymentResponse>  createOrder(@RequestBody CreateOrderRequest order,
			@RequestHeader("Authorization") String jwt) 
					throws UserException, RestaurantException, 
					CartException, 
					StripeException,
					OrderException{
		User user=userService.findUserProfileByJwt(jwt);
		System.out.println("req user "+user.getEmail());
    	if(order!=null) {
			PaymentResponse res = orderService.createOrder(order,user);
			return ResponseEntity.ok(res);
			
    	}else throw new OrderException("Vui lòng cung cấp thông tin hợp lệ!");
		
    }
    
 
    
    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getAllUserOrders(	@RequestHeader("Authorization") String jwt) throws OrderException, UserException{
    
    	User user=userService.findUserProfileByJwt(jwt);
    	
    	if(user.getId()!=null) {
    	List<Order> userOrders = orderService.getUserOrders(user.getId());
    	return ResponseEntity.ok(userOrders);
    	}else {
    		return new ResponseEntity<List<Order>>(HttpStatus.BAD_REQUEST);
    	}
    }
    

    

	
}
