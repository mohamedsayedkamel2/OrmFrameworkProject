package framework.connection.exceptions;

import java.sql.SQLException;

public class InvalidConnectionInformationException extends RuntimeException {
    public InvalidConnectionInformationException(SQLException e) {
        System.err.println("Error! Invalid DB connection info");
        System.err.println("The SQL code error is " + e.getErrorCode());
    }
}
