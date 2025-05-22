package com.foodgo.service;

import com.foodgo.Exception.CartException;
import com.foodgo.Exception.CartItemException;
import com.foodgo.Exception.FoodException;
import com.foodgo.Exception.UserException;
import com.foodgo.model.Cart;
import com.foodgo.model.CartItem;
import com.foodgo.request.AddCartItemRequest;

public interface CartSerive {

	public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws UserException, FoodException, CartException, CartItemException;

	public CartItem updateCartItemQuantity(Long cartItemId,int quantity) throws CartItemException;

	public Cart removeItemFromCart(Long cartItemId, String jwt) throws UserException, CartException, CartItemException;

	public Long calculateCartTotals(Cart cart) throws UserException;
	
	public Cart findCartById(Long id) throws CartException;
	
	public Cart findCartByUserId(Long userId) throws CartException, UserException;
	
	public Cart clearCart(Long userId) throws CartException, UserException;
	

	

}
