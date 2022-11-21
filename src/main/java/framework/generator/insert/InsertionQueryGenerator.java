package framework.generator.insert;

import framework.generator.exceptions.InvalidEntityException;
import framework.generator.QueryUtility;

import java.lang.reflect.Field;
import java.util.List;

public class InsertionQueryGenerator {

    private QueryUtility queryUtil;

    public InsertionQueryGenerator() {
        this.queryUtil = new QueryUtility();
    }

    public String insert(Object userObject) {

        if (userObject == null) {
            throw new IllegalArgumentException("The input is 'NULL' which is an invalid input, please enter a valid input.");
        }

        Class<?> userObjectClass = userObject.getClass();

        if (queryUtil.isObjectValidEntity(userObjectClass) == false) {
            throw new InvalidEntityException("The passed object is not valid, check if the object is marked as entity and have an empty public contructor");
        }

        String className = userObjectClass.getSimpleName().toLowerCase();
        List<Field> fields = queryUtil.getAnnotatedFields(userObjectClass, framework.model.annotations.Field.class);

        StringBuilder queryString = new StringBuilder();

        // The intialisation statements are like "INSERT INTO", the object class name, opening brackets
        // After this line the SQL query will be like this
        // INSERT INTO ${className} (
        appendInsertionQueryInitalisationStatements(className, queryString);
        // After this line the SQL query will be like this
        // INSERT INTO ${className} (field1Name, field2Name, fieldNname)
        appendFieldNamesToQueryString(fields, queryString);

        queryString.append(")");
        queryString.append(" ");
        queryString.append("VALUES");
        queryString.append(" ");
        queryString.append("(");

        try {
            appendFieldsValuesToQueryString(userObject, fields, queryString);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        queryString.append(");");
        return queryString.toString();
    }

    private void appendFieldsValuesToQueryString(Object userObject, List<Field> fields, StringBuilder queryString) throws IllegalAccessException {
        for (int i = 0; i < fields.size(); i++) {
            Field currentField = fields.get(i);

            if (queryUtil.isFieldPersistable(currentField) == false) {
                continue;
            }

            Object fieldValue = currentField.get(userObject);

            if (fieldValue.getClass().equals(String.class)) {
                queryString.append('\'');
                queryString.append(fields.get(i).get(userObject));
                queryString.append('\'');
            }
            else {
                queryString.append(fields.get(i).get(userObject));
            }

            queryUtil.appendComma(fields.size(), queryString, i);
        }
    }

    private void appendInsertionQueryInitalisationStatements(String className, StringBuilder queryString) {
        queryString.append("INSERT INTO");
        queryString.append(" ");
        queryString.append(className);
        queryString.append("(");
    }

    private void appendFieldNamesToQueryString(List<Field> fields, StringBuilder query) {
        for (int i = 0; i < fields.size(); i++) {
            Field currentField = fields.get(i);

            if (queryUtil.isFieldPersistable(currentField) == false) {
                continue;
            }

            query.append(fields.get(i).getName());
            queryUtil.appendComma(fields.size(), query, i);
        }
    }
}
