package com.lele.entity;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 2756640132439753341L;
	private String id;
	private boolean yourTurn;
	private MeetType meetType = MeetType.NONE;
	private User meetTriggerUser;
	private boolean isWin;
	private int score;
	private int roomCart;
	private String name;
	private String password;
	private boolean onLive;

	public User(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setYourTurn(boolean yourTurn) {
		this.yourTurn = yourTurn;
	}

	public boolean isYourTurn() {
		return yourTurn;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", yourTurn=" + yourTurn + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public MeetType getMeetType() {
		return meetType;
	}

	public void setMeetType(MeetType meetType) {
		this.meetType = meetType;
	}

	public User getMeetTriggerUser() {
		return meetTriggerUser;
	}

	public void setMeetTriggerUser(User meetTriggerUser) {
		this.meetTriggerUser = meetTriggerUser;
	}

	public boolean isWin() {
		return isWin;
	}

	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRoomCart() {
		return roomCart;
	}

	public void setRoomCart(int roomCart) {
		this.roomCart = roomCart;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getOnLive() {
		return onLive;
	}
	
	public void setOnLive(boolean onLive) {
		this.onLive=onLive;
		
	}


}
