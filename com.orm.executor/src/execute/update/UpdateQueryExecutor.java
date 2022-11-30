package execute.update;

import generator.update.UpdateQueryGenerator;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateQueryExecutor {
    private Connection connection;
    private UpdateQueryGenerator updateQueryGenerator;

    public UpdateQueryExecutor(Connection connection) {
        this.connection = connection;
        this.updateQueryGenerator = new UpdateQueryGenerator();
    }

    public void update(Object userObject) {
        String updateSqlQuery = updateQueryGenerator.update(userObject);
        try {
            connection.prepareStatement(updateSqlQuery).executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("The erro code is " + e.getErrorCode());
        }

    }
}
