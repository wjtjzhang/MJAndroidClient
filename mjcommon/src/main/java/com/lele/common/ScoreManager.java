package com.lele.common;

import com.lele.entity.Room;
import com.lele.entity.User;

public class ScoreManager {
	public static void calclateWin(Room room) {
		for (User user : room.getUsers()) {
			if (user.isWin()) {
				user.setScore(user.getScore() + 6);
				user.setZmValue(user.getZmValue() + 1);
			} else {
				user.setScore(user.getScore() - 2);
			}
		}
	}

	public static void calclatePutMingGang(Room room, User inputUser) {
		for (User user : room.getUsers()) {
			if (user.equals(inputUser)) {
				user.setScore(user.getScore() + 3);
				user.setJpValue(user.getJpValue() + 1);
			}

			if (user.equals(inputUser.getMeetTriggerUser())) {
				user.setScore(user.getScore() - 3);
				user.setDpValue(user.getDpValue() + 1);
			}
		}
	}

	public static void calclateGetMingGang(Room room, User inputUser) {
		for (User user : room.getUsers()) {
			if (user.equals(inputUser)) {
				user.setScore(user.getScore() + 3);
				user.setMgValue(user.getMgValue() + 1);
			} else {
				user.setScore(user.getScore() - 1);
			}
		}
	}
	
	public static void calclateGetAnGang(Room room, User inputUser) {
		for (User user : room.getUsers()) {
			if (user.equals(inputUser)) {
				user.setScore(user.getScore() + 6);
				user.setAgValue(user.getAgValue() + 1);
			} else {
				user.setScore(user.getScore() - 2);
			}
		}
	}
}
