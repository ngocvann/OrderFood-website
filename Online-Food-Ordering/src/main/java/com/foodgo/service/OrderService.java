package com.foodgo.service;

import com.foodgo.Exception.CartException;
import com.foodgo.Exception.OrderException;
import com.foodgo.Exception.RestaurantException;
import com.foodgo.Exception.UserException;
import com.foodgo.model.Order;
import com.foodgo.model.PaymentResponse;
import com.foodgo.model.User;
import com.foodgo.request.CreateOrderRequest;
import com.stripe.exception.StripeException;

import java.util.List;

public interface OrderService {
	
	 public PaymentResponse createOrder(CreateOrderRequest order, User user) throws UserException, RestaurantException, CartException, StripeException;
	 
	 public Order updateOrder(Long orderId, String orderStatus) throws OrderException;
	 
	 public void cancelOrder(Long orderId) throws OrderException;
	 
	 public List<Order> getUserOrders(Long userId) throws OrderException;
	 
	 public List<Order> getOrdersOfRestaurant(Long restaurantId,String orderStatus) throws OrderException, RestaurantException;
	 

}
