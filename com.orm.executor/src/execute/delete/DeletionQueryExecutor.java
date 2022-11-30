package execute.delete;

import generator.delete.DeletionQueryGenerator;

import java.sql.Connection;
import java.sql.SQLException;

public class DeletionQueryExecutor {
    private Connection connection;

    public DeletionQueryExecutor(Connection connection) {
        this.connection = connection;
    }

    public void delete(Class<?> userClass, Object id) {
        if (userClass == null || id == null) {
            throw new IllegalArgumentException("You can't send null arguemnts!");
        }
        DeletionQueryGenerator deletionQueryGenerator = new DeletionQueryGenerator();
        String deleteSqlQuery = deletionQueryGenerator.delete(userClass, id);
        boolean isDeleted = false;
        try {
            isDeleted = connection.prepareStatement(deleteSqlQuery).execute();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
