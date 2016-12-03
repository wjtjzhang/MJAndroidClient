package com.lele.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class MJDatabase {
	
	static JdbcTemplate jdbcTemplate=new JdbcTemplate();

	static {
		String url = "jdbc:mysql:///test";
		String userName = "mjowner";
		String password = "mjowner_1";
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url, userName, password);
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		jdbcTemplate.setDataSource(dataSource);
	}

	public static void buildDatabase() {
		jdbcTemplate.batchUpdate("CREATE TABLE USER(ID VARCHAR(20),NAME VARCHAR(50),PASSWORD VARCHAR(50),ROOM_CART INT(10) default 0);");
		
	}
	
	public static void addTestUsers() {
		jdbcTemplate.batchUpdate("INSERT INTO USER VALUES('Me','Me','P1','2');");
		jdbcTemplate.batchUpdate("INSERT INTO USER VALUES('Kelly','Kelly','P1','2');");
		jdbcTemplate.batchUpdate("INSERT INTO USER VALUES('Father','Father','P1','2');");
		jdbcTemplate.batchUpdate("INSERT INTO USER VALUES('Mother','Mother','P1','2');");
		jdbcTemplate.batchUpdate("INSERT INTO USER VALUES('Lele','Lele','P1','2');");
	}
	
	public static void cleanDatabase() {
		jdbcTemplate.batchUpdate("DROP TABLE USER;");
		
	}
	
	public static void main(String[] args) {
		MJDatabase.cleanDatabase();
		MJDatabase.buildDatabase();
		MJDatabase.addTestUsers();
	}
}
