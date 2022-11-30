package generator.util;

import annotations.PrimaryKey;
import annotations.Table;
import annotations.Transient;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryUtility {

    public QueryUtility(){}

    /**
     *
     * @param userClass
     * @param annotationClass
     * @return a list of fields which are annotated with a certain annotation
     */
    public List<Field> getAnnotatedFields(Class<?> userClass, Class<? extends Annotation> annotationClass) {
        List<Field> arrayList = new ArrayList<>();
        for (Field field : userClass.getDeclaredFields()) {
            field.setAccessible(true);

            if (isFieldPersistable(field) == false) continue;

            if (annotationClass != null) {
                if (!field.isAnnotationPresent(annotationClass)) {
                    continue;
                }
            }
            arrayList.add(field);
        }
        return Collections.unmodifiableList(arrayList);
    }

    public List<Field> getClassFields(Class<?> userClass) {
        return getAnnotatedFields(userClass, null);
    }

    public String getType(Class<?> type) {
        if (type.equals(int.class) || type.equals(Integer.class)) {
            return "INT";
        }
        else if (type.equals(float.class) || type.equals(Float.class)) {
            return "FLOAT";
        }
        else if (type.equals(String.class)) {
            return "TEXT";
        }
        else if (type.equals(double.class) || type.equals(Double.class)) {
            return "DOUBLE";
        }
        else if (type.equals(LocalDate.class)) {
            return "DATE";
        }
        else if (type.equals(LocalTime.class)) {
            return "TIME";
        }
        else if (type.equals(LocalDateTime.class)) {
            return "DATETIME";
        }
        throw new IllegalArgumentException("Unsupported datatype! Please send a supported data type.");
    }

    public boolean isObjectValidEntity(Class<?> userObjectClass) {
        return isPrimaryKeyFound(userObjectClass) && isObjectAnEntity(userObjectClass);
    }


    public boolean isPrimaryKeyFound(Class<?> aClass) {
        Field primaryKeyField = getPrimaryKeyField(aClass);
        return primaryKeyField == null ? false : true;
    }

    public Field getPrimaryKeyField(Class<?> aClass) {
        for (Field field : aClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return field;
            }
        }
        throw new RuntimeException("There is no primary key field found in the class!");
    }

    public boolean isObjectAnEntity(Class<?> userObjectClass) {
        Table tableAnnotation = userObjectClass.getAnnotation(Table.class);
        return tableAnnotation != null;
    }

    public void appendComma(int fieldsNumber, StringBuilder query, int i) {
        if (i != fieldsNumber - 1) {
            query.append(", ");
        }
    }

    public boolean isFieldPersistable(Field field) {
        if (field.isAnnotationPresent(annotations.Field.class) == false || (field.isAnnotationPresent(Transient.class))) {
            return false;
        }
        return true;
    }

    public boolean isPrimaryKey(Field field) {
        return field.isAnnotationPresent(PrimaryKey.class);
    }

    public String getClassName(Class<?> objectClass) {
        if (objectClass == null) {
            throw new IllegalArgumentException("Invalid operation! You can't send a null class and except the function to work!");
        }
        String tableAnnotationValue = objectClass.getAnnotation(Table.class).name();
        if (tableAnnotationValue.isEmpty() == false) {
            return tableAnnotationValue;
        }
        else {
            return objectClass.getSimpleName().toLowerCase();
        }
    }

    public String getFieldName(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("Invalid operation! You can't send a null class and except the function to work!");
        }
        annotations.Field fieldAnnotation = field.getAnnotation(annotations.Field.class);
        if (fieldAnnotation == null) {
            throw new RuntimeException("The field is not annotated as a Field");
        }
        String fieldAnnotationValue = fieldAnnotation.name();
        if (fieldAnnotationValue.isEmpty() == false) {
            return fieldAnnotationValue;
        }
        else {
            return field.getName();
        }
    }
}
