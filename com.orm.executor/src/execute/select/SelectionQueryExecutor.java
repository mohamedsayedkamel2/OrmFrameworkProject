package execute.select;

import generator.select.SelectionQueryGenerator;
import generator.util.QueryUtility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SelectionQueryExecutor {
    private Connection connection;
    private QueryUtility queryUtility;

    public SelectionQueryExecutor(Connection connection) {
        this.connection = connection;
        queryUtility = new QueryUtility();
    }

    public Object execute(Class<?> objectClass, Object id) {

        // Get the SELECT query
        SelectionQueryGenerator queryGenerator = new SelectionQueryGenerator();
        String query = queryGenerator.findOne(objectClass, id);

        ResultSet resultSet = null;
        try {
//            resultSet = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(query);
            resultSet =
                     connection.
                     prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).
                     executeQuery();
            resultSet.first();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Object userObject = null;

        try {
            Constructor<?> classConstructor = objectClass.getConstructor();
            classConstructor.setAccessible(true);
            if (classConstructor.getParameterCount() != 0) {
                throw new IllegalArgumentException("The entity class must have one empty constructor at least");
            }

            userObject = classConstructor.newInstance();

            resultSet.first();
            for (Field field : queryUtility.getClassFields(userObject.getClass())) {
                if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                    field.set(userObject, resultSet.getInt(queryUtility.getFieldName(field)));
                }
                else if (field.getType().equals(String.class)) {
                    field.set(userObject, resultSet.getString(queryUtility.getFieldName(field)));
                }
                else if (field.getType().equals(float.class) || field.getType().equals(Float.class)) {
                    field.set(userObject, resultSet.getFloat(queryUtility.getFieldName(field)));
                }
                else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                    field.set(userObject, resultSet.getDouble(queryUtility.getFieldName(field)));
                }
                else if (field.getType().equals(LocalDate.class)) {
                    field.set(userObject, resultSet.getObject(queryUtility.getFieldName(field)));
                }
                else if (field.getType().equals(LocalTime.class)) {
                    field.set(userObject, resultSet.getObject(queryUtility.getFieldName(field)));
                }
                else if (field.getType().equals(LocalDateTime.class)) {
                    field.set(userObject, resultSet.getObject(queryUtility.getFieldName(field)));
                }
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

        return userObject;
    }
}
