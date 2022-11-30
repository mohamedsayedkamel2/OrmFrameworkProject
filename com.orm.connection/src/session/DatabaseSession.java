package session;

import execute.delete.DeletionQueryExecutor;
import execute.insert.InsertionQueryExecutor;
import execute.select.SelectionQueryExecutor;
import execute.update.UpdateQueryExecutor;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSession {
    private Connection connection = null;

    private Statement statement = null;

    DatabaseSession(Connection connection) {
        this.connection = connection;
    }
    public void insert(Object userObject) {
        InsertionQueryExecutor executor = new InsertionQueryExecutor(connection);
        executor.execute(userObject);
    }
    public Object findOne(Class<?> type, Object id) {
        SelectionQueryExecutor selectionQueryExecutor = new SelectionQueryExecutor(connection);
        return selectionQueryExecutor.execute(type, id);
    }
    public void update(Object userObject) {
        UpdateQueryExecutor updateQueryExecutor = new UpdateQueryExecutor(connection);
        updateQueryExecutor.update(userObject);
    }
    public void delete(Class<?> type, Object id) {
        DeletionQueryExecutor deletionQueryExecutor = new DeletionQueryExecutor(connection);
        deletionQueryExecutor.delete(type, id);
    }

}