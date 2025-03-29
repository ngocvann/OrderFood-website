package com.eatzy.service;

import com.eatzy.model.Order;
import com.eatzy.model.User;
import com.eatzy.request.OrderRequest;

import java.util.List;

public interface OrderService {

    public Order createOrder(OrderRequest order, User user) throws Exception;

    public Order updateOrder(Long orderId, String orderStatus) throws Exception;

    public void cancelOrder(Long orderId) throws Exception;

    public List<Order> getUserOrders(Long userId) throws Exception;

    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception;

    public Order findOrderById(Long orderId) throws Exception;
}
