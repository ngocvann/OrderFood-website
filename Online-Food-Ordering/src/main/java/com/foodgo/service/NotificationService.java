package com.foodgo.service;

import com.foodgo.model.Notification;
import com.foodgo.model.Order;
import com.foodgo.model.Restaurant;
import com.foodgo.model.User;

import java.util.List;

public interface NotificationService {
	
	public Notification sendOrderStatusNotification(Order order);
	public void sendRestaurantNotification(Restaurant restaurant, String message);
	public void sendPromotionalNotification(User user, String message);
	
	public List<Notification> findUsersNotification(Long userId);

}
