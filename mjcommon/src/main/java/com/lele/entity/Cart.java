package com.lele.entity;

import java.io.Serializable;

public class Cart implements Serializable {

	private static final long serialVersionUID = 1259482110079267748L;
	private CartType cartType;
	private int code;
	private CartStatus cartStatus = CartStatus.IN;
	private long playTime;
	private MeetType meetType=MeetType.NONE;

	public Cart(CartType cartType, int code) {
		this.setCartType(cartType);
		this.setCode(code);
	}

	public CartType getCartType() {
		return cartType;
	}

	public void setCartType(CartType cartType) {
		this.cartType = cartType;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	public CartStatus getCartStatus() {
		return cartStatus;
	}

	public void setCartStatus(CartStatus cartStatus) {
		this.cartStatus = cartStatus;
	}
	
	public long getPlayTime() {
		return playTime;
	}

	public void setPlayTime(long playTime) {
		this.playTime = playTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cartType == null) ? 0 : cartType.hashCode());
		result = prime * result + code;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cart other = (Cart) obj;
		if (cartType != other.cartType)
			return false;
		if (code != other.code)
			return false;
		return true;
	}

	public MeetType getMeetType() {
		return meetType;
	}

	public void setMeetType(MeetType meetType) {
		this.meetType = meetType;
	}

	@Override
	public String toString() {
		return "Cart [cartType=" + cartType + ", code=" + code + ", cartStatus=" + cartStatus + ", playTime="
				+ playTime + ", meetType=" + meetType + "]";
	}
}
