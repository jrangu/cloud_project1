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

	private static final String GET_FILES_FOR_USER_ID_QUERY = "SELECT * from sys.user_file_table where user_id = ? and not delete_flag";

	private static final String GET_USER_FILE_RELATION_FOR_GIVEN_ID = "SELECT * from sys.user_file_table where id = ?";

	private static final String INSERT_USER_FILE_RELATION_QUERY = "INSERT "
			+ "sys.user_file_table(user_id, first_name, last_name, file_name, file_desc, creation_timestamp, last_updated_timestamp) "
			+ "values (?, ?, ?, ?, ?, ?, ?)";

	private static final String UPDATE_USER_FILE_RELATION_QUERY = "UPDATE sys.user_file_table"
			+ "set file_name = ?, file_name = ?, last_updated_timestamp = ? " + "where file_name = ?";

	private static final String DELETE_USER_FILE_RELATION_QUERY = "UPDATE sys.user_file_table "
			+ "set delete_flag = 1 " + "where id = ?";

	private Connection connection;

	public UserFileRelationDb(Connection connection) {
		this.connection = connection;
	}

	public Set<UserFileRelation> get(String userId) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = connection.prepareStatement(GET_FILES_FOR_USER_ID_QUERY);
			preparedStatement.setString(1, userId);
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

	public Optional<UserFileRelation> get(int id) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = connection.prepareStatement(GET_USER_FILE_RELATION_FOR_GIVEN_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return Optional.of(readUserFileRelation(rs));
			}
		} finally {
			close(Optional.ofNullable(preparedStatement), Optional.ofNullable(rs));
		}
		return Optional.empty();
	}

	public void upload(UserFileRelation userFileRelation) throws SQLException {
		PreparedStatement preparedStatement = null;
		int paramCounter = 1;
		try {
			preparedStatement = connection.prepareStatement(INSERT_USER_FILE_RELATION_QUERY);
			preparedStatement.setString(paramCounter++, userFileRelation.getUserId());
			preparedStatement.setString(paramCounter++, userFileRelation.getFirstName());
			preparedStatement.setString(paramCounter++, userFileRelation.getLastName());
			preparedStatement.setString(paramCounter++, userFileRelation.getFileName());
			preparedStatement.setString(paramCounter++, userFileRelation.getFileDesc());
			preparedStatement.setTimestamp(paramCounter++, userFileRelation.getCreatedTimestamp());
			preparedStatement.setTimestamp(paramCounter++, userFileRelation.getUpdatedTimestamp());
			preparedStatement.execute();
		} finally {
			close(Optional.ofNullable(preparedStatement), Optional.empty());
		}
	}

	public void delete(String id) throws SQLException {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(DELETE_USER_FILE_RELATION_QUERY);
			preparedStatement.setInt(1, Integer.valueOf(id));
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} finally {
			close(Optional.ofNullable(preparedStatement));
		}
	}

//	public void update(String oldFileKey, String newFileKey) {
//		PreparedStatement preparedStatement = null;
//		ResultSet rs = null;
//		try {
//			preparedStatement = connection.prepareStatement(DELETE_USER_FILE_RELATION_QUERY);
//			preparedStatement.setString(1, fileKey);
//			rs = preparedStatement.executeQuery();
//		} finally {
//			close(Optional.ofNullable(preparedStatement), Optional.ofNullable(rs));
//		}
//	}

	private UserFileRelation readUserFileRelation(ResultSet rs) throws SQLException {
		return new UserFileRelation().setId(rs.getInt("id")).setCreatedTimestamp(rs.getTimestamp("creation_timestamp"))
				.setFileDesc(rs.getString("file_desc")).setFileName(rs.getString("file_name"))
				.setFirstName(rs.getString("first_name")).setIsDeleted(rs.getBoolean("delete_flag"))
				.setLastName(rs.getString("last_name")).setUpdatedTimestamp(rs.getTimestamp("last_updated_timestamp"))
				.setUserId(rs.getString("user_id"));
	}
}
