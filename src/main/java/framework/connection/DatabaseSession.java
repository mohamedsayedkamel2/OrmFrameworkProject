package framework.connection;

import framework.executor.insert.InsertionQueryExecutor;

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

//
//    public void update(Object userObject) {
//        Executor insertOperationExecutor = new UpdateQueryExecutor(userObject);
//        insertOperationExecutor.update();
//    }
//
//    public void findOne(Class<?> type, Object id) {
//        Executor selectionQueryExecutor = new SelectionQueryExecutor(type, id);
//        selectionQueryExecutor.execute();
//    }
//
//    public List<Object> findAll(Class<?> type, Object id) {
//        Executor selectionQueryExecutor = new SelectionQueryExecutor(type, id);
//        List<Object> list = selectionQueryExecutor.execute();
//        return list;
//    }
//
//    public void delete(Class<?> type, Object id) {
//        Executor deleteQueryExecutor = new DeletionQueryExecutor(type, id);
//        deleteQueryExecutor.execute();
//    }
}