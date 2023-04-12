package com.example.pirog.Interface;

import com.example.pirog.Model.CartItem;

import java.util.List;

public interface CartListener {
    void onCartLoaded(List<CartItem> cartItems);
}

