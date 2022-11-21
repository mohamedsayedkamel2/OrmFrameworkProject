package framework.generator.delete;

import framework.model.annotations.Table;
import framework.generator.exceptions.EntityClassNotAnnotatedException;

import java.util.Objects;

class DeletionQueryGenerator {
    public String delete(Class<?> userClass, Object id) throws IllegalAccessException {
        if (Objects.isNull(userClass)) {
            throw new IllegalAccessException();
        }

        Table tableAnnotation = userClass.getAnnotation(Table.class);

        if (Objects.isNull(tableAnnotation)) {
            throw new EntityClassNotAnnotatedException("The class " + userClass.getSimpleName() + " Isn't annotated as an Entity!");
        }

        // Lowercase the class name to match the database covention
        String className = userClass.getSimpleName().toLowerCase();

        String deleteQuery = "DELETE FROM " + className +  " WHERE id=" + id + ";";

        return deleteQuery;
    }
}
