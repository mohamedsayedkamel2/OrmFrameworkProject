package framework.executor.insert;

import framework.model.annotations.PrimaryKey;
import framework.generator.QueryUtility;
import framework.generator.insert.InsertionQueryGenerator;
import framework.model.annotations.Table;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

public class InsertionQueryExecutor {

    private InsertionQueryGenerator generator;
    private Connection connection;

    private QueryUtility utility;

    public InsertionQueryExecutor(Connection connection) {
        this.connection = connection;
        generator = new InsertionQueryGenerator();
        utility = new QueryUtility();
    }

    public void execute(Object userObject) {
        String query = generator.insert(userObject);
        try {
            connection.createStatement().execute(query);
            connection.commit();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1146) {
             Class<?> objectClass = userObject.getClass();
             String className = objectClass.getSimpleName().toLowerCase();
             Field[] fields = objectClass.getDeclaredFields();
             StringBuilder tableQuery = new StringBuilder();
             tableQuery.append("CREATE");
             tableQuery.append(" ");
             tableQuery.append("TABLE");
             tableQuery.append(" ");
             if (objectClass.getAnnotation(Table.class).name().isEmpty() == false) {
                 tableQuery.append(objectClass.getAnnotation(Table.class).name());
             }
             else {
                 tableQuery.append(className);
             }
             tableQuery.append("(");
             for (int i = 0; i < fields.length; i++) {
                 fields[i].setAccessible(true);
                 tableQuery.append(fields[i].getName());
                 tableQuery.append(" ");
                 tableQuery.append(utility.getType(fields[i].getType()));
                 if (fields[i].isAnnotationPresent(PrimaryKey.class)) {
                     tableQuery.append(" AUTO_INCREMENT, ");
                     tableQuery.append("PRIMARY KEY");
                     tableQuery.append("(");
                     tableQuery.append(fields[i].getName());
                     tableQuery.append(")");
                 }
                 utility.appendComma(fields.length, tableQuery, i);
             }
             tableQuery.append(");");
//                try {
//                    connection.createStatement().executeUpdate(tableQuery.toString());
//                } catch (SQLException ex) {
//                    System.out.println(ex);
//                }
                System.out.println(tableQuery);
            }
        }
    }
}
