package com.lele.common;

import java.util.Comparator;

import com.lele.entity.Cart;

public class InCartComparator implements Comparator<Cart> {

	public int compare(Cart cart1, Cart cart2) {
		if (cart1.getCartType().getSequence() < cart2.getCartType().getSequence()) {
			return -1;
		}
		if (cart1.getCartType().getSequence() > cart2.getCartType().getSequence()) {
			return 1;
		}
		if (cart1.getCartType().getSequence() == cart2.getCartType().getSequence()) {
			if (cart1.getCode()<cart2.getCode()){
				return -1;
			} 
			
			if (cart1.getCode()>=cart2.getCode()){
				return 1;
			}
		}
		return 0;
	}
}
