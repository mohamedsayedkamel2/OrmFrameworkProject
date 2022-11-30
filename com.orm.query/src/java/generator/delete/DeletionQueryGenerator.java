package generator.delete;

import annotations.Table;
import generator.exceptions.EntityClassNotAnnotatedException;
import generator.util.QueryUtility;

public class DeletionQueryGenerator {

    private QueryUtility queryUtility;

    public DeletionQueryGenerator() {
        this.queryUtility = new QueryUtility();
    }

    public String delete(Class<?> userClass, Object id) {
        if (userClass == null || id == null) {
            throw new IllegalArgumentException();
        }

        Table tableAnnotation = userClass.getAnnotation(Table.class);

        if (tableAnnotation == null) {
            throw new EntityClassNotAnnotatedException("The class " + userClass.getSimpleName() + " Isn't annotated as an Entity!");
        }

        // Lowercase the class name to match the database covention
        String className = queryUtility.getClassName(userClass);

        StringBuilder deleteQuery = new StringBuilder();
        deleteQuery.append("DELETE");
        deleteQuery.append(" ");
        deleteQuery.append("FROM");
        deleteQuery.append(" ");
        deleteQuery.append(className);
        deleteQuery.append(" ");
        deleteQuery.append("WHERE");
        deleteQuery.append(" ");
        deleteQuery.append(queryUtility.getFieldName(queryUtility.getPrimaryKeyField(userClass)));
        deleteQuery.append("=");
        deleteQuery.append(id);
        deleteQuery.append(";");


        return deleteQuery.toString();
    }
}
