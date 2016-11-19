package com.lele.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.lele.entity.User;

public class UserDao {

	static JdbcTemplate jdbcTemplate = new JdbcTemplate();

	static {
		String url = "jdbc:mysql:///test";
		String userName = "mjowner";
		String password = "mjowner_1";
		DriverManagerDataSource dataSource = new DriverManagerDataSource(url, userName, password);
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		jdbcTemplate.setDataSource(dataSource);
	}

	public void insert(User user) {
		List<Object[]> values = new ArrayList<Object[]>();
		Object object[] = { user.getId(), user.getName(), user.getPassword(), user.getRoomCart() };
		values.add(object);
		jdbcTemplate.batchUpdate("INSERT INTO user(id, name, password, room_cart) VALUES (?,?,?,?)", values);
	}

	public boolean updatePassword(User user) {
		List<Object[]> values = new ArrayList<Object[]>();
		Object object[] = { user.getPassword(), user.getId() };
		values.add(object);
		int[] impactRow = jdbcTemplate.batchUpdate("UPDATE user SET password=? where id=?", values);

		return impactRow.length > 0;

	}

	public boolean delete(User user) {
		List<Object[]> values = new ArrayList<Object[]>();
		Object object[] = { user.getId() };
		values.add(object);
		int[] impactRow = jdbcTemplate.batchUpdate("DELETE FROM user where id=?", values);
		return impactRow.length > 0;
	}

	public List<User> select(String id) {
		List<User> users = jdbcTemplate.query("SELECT * FROM user where id=?", new Object[] { id }, getUserRowMapper());
		return users;
	}

	public User getUser(String id) {
		List<User> users = select(id);
		if (users.size() > 0) {
			return users.get(0);
		} else {
			return null;
		}
	}

	public List<User> listAll() {
		List<User> users = jdbcTemplate.query("SELECT * FROM user", getUserRowMapper());
		return users;
	}

	public boolean isValid(User user) {
		List<User> users = jdbcTemplate.query("SELECT * FROM user where id=? and password=?",
				new Object[] { user.getId(), user.getPassword() }, getUserRowMapper());

		if (users.size() == 0) {
			return false;
		}
		return true;
	}

	private RowMapper<User> getUserRowMapper() {
		RowMapper<User> userRowMapper = new RowMapper<User>() {
			public User mapRow(final ResultSet rs, final int rowNum) throws SQLException {
				User user = new User(rs.getString("id"));
				user.setId(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setRoomCart(rs.getInt("room_cart"));
				return user;
			}
		};
		return userRowMapper;
	}

	public boolean updateRoomCart(User user) {
		List<Object[]> values = new ArrayList<Object[]>();
		Object object[] = { user.getRoomCart(), user.getId() };
		values.add(object);
		int[] impactRow = jdbcTemplate.batchUpdate("UPDATE user SET room_cart=? where id=?", values);
		return impactRow.length > 0;

	}
}
