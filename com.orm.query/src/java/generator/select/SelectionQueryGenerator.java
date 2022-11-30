package generator.select;

import annotations.Table;
import generator.exceptions.EntityClassNotAnnotatedException;
import generator.util.QueryUtility;

import java.lang.reflect.Field;
import java.util.List;

public class SelectionQueryGenerator {

    private QueryUtility queryUtility;

    public SelectionQueryGenerator() {
        this.queryUtility = new QueryUtility();
    }

    public String findOne(Class<?> userClass, Object id) {

        if (userClass == null) {
            throw new IllegalArgumentException("You can't expect a function with null arguments to work!!!");
        }

        Table tableAnnotation = userClass.getAnnotation(Table.class);
        List<Field> fields = queryUtility.getClassFields(userClass);

        if (tableAnnotation == null) {
            throw new EntityClassNotAnnotatedException("The class " + userClass.getSimpleName() + " Isn't annotated as an Entity!");
        }

        String className = queryUtility.getClassName(userClass);

        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT ");
        for (int index = 0; index < fields.size(); index++) {
            selectQuery.append(queryUtility.getFieldName(fields.get(index)));
            if (index != fields.size() - 1) {
                selectQuery.append(",");
            }
        }
        selectQuery.append(" ");
        selectQuery.append("FROM ");
        selectQuery.append(className);
        selectQuery.append(" WHERE ");
        selectQuery.append(queryUtility.getFieldName(queryUtility.getPrimaryKeyField(userClass)));
        selectQuery.append("=" + id);
        selectQuery.append(";");

        return selectQuery.toString();
    }
}
