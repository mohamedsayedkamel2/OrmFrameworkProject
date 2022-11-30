package execute.insert;

import execute.ddl.TableQueryExecutor;
import generator.insert.InsertionQueryGenerator;

import java.sql.Connection;
import java.sql.SQLException;

public class InsertionQueryExecutor {

    private final InsertionQueryGenerator generator;

    private final Connection connection;

    private static final int TABLE_NOT_FOUND_ERROR_CODE = 1146;

    public InsertionQueryExecutor(Connection connection) {
        this.connection = connection;
        generator = new InsertionQueryGenerator();
    }

    public void execute(Object userObject)  {
        String query = generator.insert(userObject);
        try {
            executeInsertQuery(query);
        } catch (SQLException e) {
            // This error means the table wasn't found
            if (e.getErrorCode() == TABLE_NOT_FOUND_ERROR_CODE) {
                System.out.println("The SQL table is not found, the table will be created.");
                new TableQueryExecutor(connection).execute(userObject);
                System.out.println("Missing SQL table has been created");
                try {
                    executeInsertQuery(query);
                } catch (SQLException ex) {
                    throw new RuntimeException("Error happened while trying to re-insert the object after creating the table! " + e);
                }
            }
        }
    }
    private void executeInsertQuery(String query) throws SQLException  {
            connection.createStatement().execute(query);
            connection.commit();
    }
}
