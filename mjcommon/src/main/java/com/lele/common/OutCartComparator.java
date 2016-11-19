package com.lele.common;

import java.util.Comparator;

import com.lele.entity.Cart;

public class OutCartComparator implements Comparator<Cart> {

	public int compare(Cart cart1, Cart cart2) {
		if (cart1.getPlayTime()<cart2.getPlayTime()){
			return -1;
		}
		
		if (cart1.getPlayTime()>cart2.getPlayTime()){
			return 1;
		}
		
		return 0;
	}

}
