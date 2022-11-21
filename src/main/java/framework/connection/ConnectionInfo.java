package framework.connection;

import framework.connection.exceptions.InvalidConnectionInformationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @apiNote a class that holds jdbc connection info
 */
class ConnectionInfo {
    private String jdbcUrl;
    private String username;
    private String password;
    Connection dbConnection;

    ConnectionInfo(Properties properties) {
        setJdbcConnectionInfo(properties);
    }

    void setJdbcConnectionInfo(Properties properties)  {
        jdbcUrl = properties.getProperty("JdbcUrl");
        username = properties.getProperty("username");
        password = properties.getProperty("password");

        try {
            dbConnection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new InvalidConnectionInformationException(e);
        }
        try {
            dbConnection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Exception -- Could not set AutoCommit, SQL error code: " + e.getErrorCode());
        }
    }
}