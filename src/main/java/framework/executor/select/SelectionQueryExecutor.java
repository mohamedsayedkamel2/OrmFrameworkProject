package framework.executor.select;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

class SelectionQueryExecutor {
    private String queryString;
    private Connection connection;

    SelectionQueryExecutor(String queryString, Connection connection) {
        this.queryString = queryString;
        this.connection = connection;
    }

    String execute(Class<?> objectClass) {

        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(queryString);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            resultSet.first();
            System.out.println(resultSet.getString("name"));
            System.out.println(resultSet.getInt("id"));

            Constructor<?> classConstructor = objectClass.getConstructor();
            classConstructor.setAccessible(true);
            if (classConstructor.getParameterCount() != 0) {
                throw new IllegalArgumentException("The entity class must have one empty constructor at least");
            }
            Object userObject = classConstructor.newInstance();

            Field[] fields = objectClass.getDeclaredFields();
            for (Field field : fields) {

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return queryString;
    }
}
