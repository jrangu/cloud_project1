package db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Utility to get and release database connection.
 */
public final class DatabaseConnectionManager {

  private static HikariDataSource ds;

  static {
    ds = new HikariDataSource(getDatabaseConfig());
  }

  /**
   * Returns database connection. Call {@code close} after done using it.
   */
  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

  public static void close(Optional<Statement> statementOpt) {
	    statementOpt.ifPresent(stmt -> {
	      try {
	        stmt.close();
	      } catch (SQLException e) {}
	    });
	  }
  
  public static void close(Optional<Statement> statementOpt, Optional<ResultSet> rsOpt) {
    statementOpt.ifPresent(stmt -> {
      try {
        stmt.close();
      } catch (SQLException e) {}
    });
    rsOpt.ifPresent(rs -> {
      try {
        rs.close();
      } catch (SQLException e) {}
    });
  }

  private static HikariConfig getDatabaseConfig() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:mysql://localhost:3306/sys?serverTimezone=UTC");
    config.setUsername("cloudproject");
    config.setPassword("prasanna123!");
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    return config;
  }
}
