package framework.generator.update;

import framework.model.annotations.Table;
import framework.generator.QueryUtility;
import framework.generator.exceptions.EntityClassNotAnnotatedException;
import framework.generator.exceptions.PrimaryKeyNotFoundException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

class UpdateQueryGenerator {

    private QueryUtility queryUtility;

    UpdateQueryGenerator() {
        this.queryUtility = new QueryUtility();
    }

    public String update(Object userObject) throws IllegalAccessException, NoSuchFieldException {
        if (Objects.isNull(userObject)) {
            throw new IllegalAccessException();
        }

        boolean isPrimaryKeyFound = queryUtility.isPrimaryKeyFound(userObject.getClass());
        if (!isPrimaryKeyFound) {
            throw new PrimaryKeyNotFoundException("The primary key was not found! Please go and create a primary key field.");
        }

        Class<?> classObject = userObject.getClass();
        Table tableAnnotation = classObject.getAnnotation(Table.class);

        if (Objects.isNull(tableAnnotation)) {
            throw new EntityClassNotAnnotatedException("The class " + classObject.getSimpleName() + " Isn't annotated as an Entity!");
        }

        String className = classObject.getSimpleName().toLowerCase();
        List<Field> fields = queryUtility.getAnnotatedFields(userObject.getClass(), framework.model.annotations.Field.class);

        StringBuilder query = new StringBuilder();
        query.append("UPDATE " + className + " ");
        query.append("SET ");

        for (int i = 0; i < fields.size(); i++) {
            Field currentField = fields.get(i);
            Object fieldValue = currentField.get(userObject);
            if (Objects.isNull(fieldValue)) {
                continue;
            }
            query.append(currentField.getName());
            query.append("=");
            query.append(fieldValue);
            if (i != fields.size() - 1) {
                query.append(" AND ");
            }
        }
        query.append(";");
        return query.toString();
    }
}
