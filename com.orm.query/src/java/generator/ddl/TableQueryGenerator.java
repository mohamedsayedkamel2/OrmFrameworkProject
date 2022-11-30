package generator.ddl;

import generator.util.QueryUtility;

import java.lang.reflect.Field;

public class TableQueryGenerator {

    private final QueryUtility queryUtility;

    public TableQueryGenerator() {
        this.queryUtility = new QueryUtility();
    }

    public String create(Object userObject) {
        return createTable(userObject);
    }

    private String createTable(Object userObject) {
        Class<?> objectClass = userObject.getClass();
        // Get all the declared fields in the class including private, protected, public and package-private ones
        Field[] fields = objectClass.getDeclaredFields();
        StringBuilder tableQuery = new StringBuilder();
        // Perform query initalisation statements like CREATE TABLE
        initQuery(tableQuery);
        // Append the table name to the query string
        tableQuery.append(queryUtility.getClassName(objectClass));
        tableQuery.append("(");
        // A variable that remembers if a primary key was found and it's used to avoid uncessary method calls when the primary key is found
        boolean isPrimaryKeyFound = false;
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            // Get the field name
            tableQuery.append(queryUtility.getFieldName(fields[i]));
            tableQuery.append(" ");
            // Get the field type name and apppend that name to the query
            tableQuery.append(getFieldTypeName(fields[i]));
            // Check if the primary key is found
            if (isPrimaryKeyFound == false) {
                // Check if the current field is a primary key
                if (queryUtility.isPrimaryKey(fields[i])) {
                    // If so then append the primary key to the query
                    appendPrimaryKeyToQuery(tableQuery, fields[i]);
                    // Announce that the primary key is found
                    // This update will help avoiding uncessary method calls
                    isPrimaryKeyFound = true;
                }
            }
            // If we aren't at the end of the loop then a comma will be appened
            queryUtility.appendComma(fields.length, tableQuery, i);
        }
        // End of the query
        tableQuery.append(");");
        return tableQuery.toString();
    }
    private void initQuery(StringBuilder tableQuery) {
        tableQuery.append("CREATE");
        tableQuery.append(" ");
        tableQuery.append("TABLE");
        tableQuery.append(" ");
    }

    private String getFieldTypeName(Field fields) {
        return queryUtility.getType(fields.getType());
    }

    private void appendPrimaryKeyToQuery(StringBuilder tableQuery, Field primaryKeyField) {
        tableQuery.append(" AUTO_INCREMENT, ");
        tableQuery.append("PRIMARY KEY");
        tableQuery.append("(");
        tableQuery.append(queryUtility.getFieldName(primaryKeyField));
        tableQuery.append(")");
    }

}
