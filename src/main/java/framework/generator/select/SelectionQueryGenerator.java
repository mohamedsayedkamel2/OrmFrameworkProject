package framework.generator.select;

import framework.model.annotations.Table;
import framework.generator.QueryUtility;
import framework.generator.exceptions.EntityClassNotAnnotatedException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

class SelectionQueryGenerator {

    private QueryUtility queryUtility;

    SelectionQueryGenerator() {
        this.queryUtility = new QueryUtility();
    }

    public String findOne(Class<?> userClass, Object id) throws IllegalAccessException {
        if (Objects.isNull(userClass)) {
            throw new IllegalAccessException();
        }

        Table tableAnnotation = userClass.getAnnotation(Table.class);
        List<Field> fields = queryUtility.getClassFields(userClass);

        if (Objects.isNull(tableAnnotation)) {
            throw new EntityClassNotAnnotatedException("The class " + userClass.getSimpleName() + " Isn't annotated as an Entity!");
        }

        String className = userClass.getSimpleName().toLowerCase();

        StringBuilder selectQuery = new StringBuilder();
        selectQuery.append("SELECT ");
        for (int index = 0; index < fields.size(); index++) {
            selectQuery.append(fields.get(index).getName());
            if (index != fields.size() - 1) {
                selectQuery.append(",");
            }
        }
        selectQuery.append(" ");
        selectQuery.append("FROM ");
        selectQuery.append(className);
        selectQuery.append(" WHERE ");
        selectQuery.append("id=" + id);
        selectQuery.append(";");

        return selectQuery.toString();
    }
}
