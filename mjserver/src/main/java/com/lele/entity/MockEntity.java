package com.lele.entity;

import java.util.Map;

public class MockEntity {

	public static Map<String, User> users;

	static {
		User user1 = new User("Jordan");
		user1.setRoomCart(3);
		users.put(user1.getId(),user1);
		
		User user2 = new User("Kelly");
		users.put(user2.getId(),user2);
		
		User user3 = new User("Father");
		users.put(user3.getId(),user3);
		
		User user4 = new User("Mather");
		users.put(user4.getId(),user4);
		
		User user5 = new User("Lele");
		users.put(user5.getId(),user5);
	}

}
