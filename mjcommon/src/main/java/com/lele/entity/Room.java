package com.lele.entity;

import org.apache.mina.core.session.IoSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Room implements Serializable {

	private static final long serialVersionUID = -2023984380660960798L;
	private final List<User> users = new ArrayList<User>();
	private final int id;
	private final Map<User, List<Cart>> inCartsMap = new ConcurrentHashMap<User, List<Cart>>();
	private final Map<User, List<Cart>> outCartsMap = new ConcurrentHashMap<User, List<Cart>>();
	private final Map<User, List<Cart>> meetCartsMap = new ConcurrentHashMap<User, List<Cart>>();
	private Cart currentPutCart;
	public List<Cart> carts = new ArrayList<Cart>();
	private int nextCartIndex;
	private Cart currentGetCart;
	private boolean isRoundOver=false;
	private int currentRound = 1;
	private int totalRound = 2;
	private int readyNumberOfNextRound;
	private User roomOwner;

	public Room(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public List<User> getUsers() {
		return users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Room other = (Room) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Cart getCurrentPutCart() {
		return currentPutCart;
	}

	public void setCurrentPutCart(Cart currentPutCart) {
		this.currentPutCart = currentPutCart;
	}

	public int getNextCartIndex() {
		return nextCartIndex;
	}

	public void setNextCartIndex(int nextCartIndex) {
		this.nextCartIndex = nextCartIndex;
	}

	public Map<User, List<Cart>> getInCartsMap() {
		return inCartsMap;
	}

	public Map<User, List<Cart>> getOutCartsMap() {
		return outCartsMap;
	}

	public Map<User, List<Cart>> getMeetCartsMap() {
		return meetCartsMap;
	}

	public Cart getCurrentGetCart() {
		return currentGetCart;
	}

	public void setCurrentGetCart(Cart currentGetCart) {
		this.currentGetCart = currentGetCart;
	}

	public boolean isRoundOver() {
		return isRoundOver;
	}

	public void setRoundOver(boolean isRoundOver) {
		this.isRoundOver = isRoundOver;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}

	public int getTotalRound() {
		return totalRound;
	}

	public void setTotalRound(int totalRound) {
		this.totalRound = totalRound;
	}

	public int getReadyNumberOfNextRound() {
		return readyNumberOfNextRound;
	}

	public void setReadyNumberOfNextRound(int readyNumberOfNextRound) {
		this.readyNumberOfNextRound = readyNumberOfNextRound;
	}

	public User getRoomOwner() {
		return roomOwner;
	}

	public void setRoomOwner(User roomOwner) {
		this.roomOwner = roomOwner;
	}

	@Override
	public String toString() {
		return "Room{" +
				"users=" + users +
				", id=" + id +
				'}';
	}
}
