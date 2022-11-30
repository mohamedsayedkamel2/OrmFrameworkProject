package generator.update;

import generator.exceptions.InvalidEntityException;
import generator.util.QueryUtility;

import java.lang.reflect.Field;
import java.util.List;

public class UpdateQueryGenerator {

    private QueryUtility queryUtility;

    public UpdateQueryGenerator() {
        this.queryUtility = new QueryUtility();
    }

    public String update(Object userObject) {
        if (userObject == null) {
            throw new IllegalArgumentException("You can't send null arguments and expect the function to work properly!");
        }

        if (queryUtility.isObjectValidEntity(userObject.getClass()) == false) {
            throw new InvalidEntityException("Invalid user entitiy please check if you didn't mark the object as an entity");
        }

        Class<?> userObjectClass = userObject.getClass();

        String className = queryUtility.getClassName(userObjectClass);
        List<Field> fields = queryUtility.getAnnotatedFields(userObjectClass, annotations.Field.class);

        StringBuilder query = new StringBuilder();
        query
                .append("UPDATE")
                .append(" ")
                .append(className)
                .append(" ")
                .append("SET")
                .append(" ");
        for (int i = 0; i < fields.size(); i++) {
            Field currentField = fields.get(i);
            Object fieldValue = getFieldValue(currentField, userObject);

            if (fieldValue == null || queryUtility.isPrimaryKey(currentField)) {
                continue;
            }
            query.append(queryUtility.getFieldName(currentField));
            query.append("=");
            query.append(fieldValue);
            if (i != fields.size() - 1) {
                query
                        .append(" ")
                        .append("AND")
                        .append(" ");
            }
        }
        Field primaryKeyField = queryUtility.getPrimaryKeyField(userObjectClass);
        primaryKeyField.setAccessible(true);
            query
                    .append(" ")
                    .append("WHERE")
                    .append(" ")
                    .append(queryUtility.getFieldName(primaryKeyField))
                    .append("=")
                    .append(getFieldValue(primaryKeyField, userObject));
        query.append(";");
        System.out.println(query);
        return query.toString();
    }

    public Object getFieldValue(Field field, Object userObject) {
        Object fieldValue = null;
        try {
            fieldValue = field.get(userObject);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return fieldValue;
    }
}
