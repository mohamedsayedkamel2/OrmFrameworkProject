package execute.ddl;

import generator.ddl.TableQueryGenerator;

import java.sql.Connection;
import java.sql.SQLException;

public class TableQueryExecutor {

    private final Connection connection;

    public TableQueryExecutor(Connection connection) {
        this.connection = connection;
    }

    public void execute(Object userObject) {
        TableQueryGenerator tableQueryGenerator = new TableQueryGenerator();
        String query = tableQueryGenerator.create(userObject);
        System.out.println("This SQL Query will be executed: " + query);
        try {
            connection.createStatement().executeUpdate(query);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("An error happened while creating the new table! " + e);
        }
    }
}
