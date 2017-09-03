package me.petko.hellochat.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
	private Date timeStamp;
	private String author;
	private String text;
	private static final String LAST_SQL = "SELECT * FROM messages LIMIT ?;";
	private static final String INSERT_SQL = "INSERT INTO messages (time_stamp, author, text) VALUES(?,?,?);";

	public Message(Date time_stamp, String author, String text) {
		this.timeStamp = time_stamp;
		this.author = author;
		this.text = text;
	}
	
	public Date getTimeStamp() {
		return timeStamp;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean save(Connection connection) throws SQLException {
		int changedRows = 0;
		
		PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
		statement.setTimestamp(1, new Timestamp(timeStamp.getTime()));
		statement.setString(2, author);
		statement.setString(3, text);

		try {
			changedRows = statement.executeUpdate();
		} finally {
			statement.close();
		}
		
		return changedRows > 0;
	}

	public static List<Message> last(int limit, Connection connection) throws SQLException {
		List<Message> messages = new ArrayList<Message>(limit);
		PreparedStatement statement = connection.prepareStatement(LAST_SQL);
		statement.setInt(1, limit);
		ResultSet resultSet = null;
		
		try {
			resultSet = statement.executeQuery();
		
			while(resultSet.next()) {
				messages.add(new Message(resultSet.getTimestamp("time_stamp"),
										 resultSet.getString("author"),
					                     resultSet.getString("text")));
			
		    }
		} finally {
			if(resultSet != null) {
				resultSet.close();
			}
			statement.close();
		}
		
		return messages;
	}
	
	public static List<Message> last(Connection connection) throws SQLException {
		return last(1, connection);
	}
}
