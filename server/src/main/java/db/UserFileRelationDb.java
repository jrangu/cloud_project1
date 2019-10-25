package db;

import static db.DatabaseConnectionManager.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import model.UserFileRelation;

/**
 * Provides interface to create/read/update/delete user-file relation
 * information from database.
 */
public final class UserFileRelationDb {

	private static final String GET_FILES_FOR_USER_ID_QUERY = "SELECT file_key, user_id, "
			+ "first_name, last_name, file_name, creation_timestamp, last_updated_timestamp,"
			+ " delete_flag from sys.user_file_table where user_id = ? and not delete_flag";

	private static final String INSERT_USER_FILE_RELATION_QUERY = "INSERT "
			+ "user_file_relation(file_key, user_id, first_name, last_name, file_name, creation_timestamp, last_updated_timestamp) "
			+ "values (?, ?, ?, ?, ?, ?, ?)";

	private Connection connection;

	public UserFileRelationDb(Connection connection) {
		this.connection = connection;
	}

	public Set<UserFileRelation> get(String user_id) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = connection.prepareStatement(GET_FILES_FOR_USER_ID_QUERY);
			preparedStatement.setString(1, user_id);
			rs = preparedStatement.executeQuery();
			Set<UserFileRelation> set = new HashSet<>();
			while (rs.next()) {
				set.add(readUserFileRelation(rs));
			}
			return Collections.unmodifiableSet(set);
		} finally {
			close(Optional.ofNullable(preparedStatement), Optional.ofNullable(rs));
		}
	}

	public void upload(UserFileRelation userFileRelation) throws SQLException {
		PreparedStatement preparedStatement = null;
		int paramCounter = 1;
		try {
			preparedStatement = connection.prepareStatement(INSERT_USER_FILE_RELATION_QUERY);
			preparedStatement.setString(paramCounter++, userFileRelation.getFileKey());
			preparedStatement.setString(paramCounter++, userFileRelation.getUserId());
			preparedStatement.setString(paramCounter++, userFileRelation.getFirstName());
			preparedStatement.setString(paramCounter++, userFileRelation.getLastName());
			preparedStatement.setString(paramCounter++, userFileRelation.getFileName());
			preparedStatement.setTimestamp(paramCounter++, userFileRelation.getCreatedTimestamp());
			preparedStatement.setTimestamp(paramCounter++, userFileRelation.getUpdatedTimestamp());
			preparedStatement.execute();
		} finally {
			close(Optional.ofNullable(preparedStatement), Optional.empty());
		}
	}

	private UserFileRelation readUserFileRelation(ResultSet rs) throws SQLException {
		return new UserFileRelation().setCreatedTimestamp(rs.getTimestamp("creation_timestamp"))
				.setFileKey(rs.getString("file_key")).setFileName(rs.getString("file_name"))
				.setFirstName(rs.getString("first_name")).setIsDeleted(rs.getBoolean("delete_flag"))
				.setLastName(rs.getString("last_name")).setUpdatedTimestamp(rs.getTimestamp("last_updated_timestamp"))
				.setUserId(rs.getString("user_id"));
	}
}
