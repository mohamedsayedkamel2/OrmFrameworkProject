package framework.generator;

import framework.model.annotations.PrimaryKey;
import framework.model.annotations.Table;
import framework.model.annotations.Transient;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
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
        else if (type.equals(BigInteger.class)) {
            return "BIGINT";
        }
        else if (type.equals(BigDecimal.class)) {
            return "DECIMAL";
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
        for (Field field : aClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                return true;
            }
        }
        return false;
    }

    public boolean isObjectAnEntity(Class<?> userObjectClass) {
        Table tableAnnotation = userObjectClass.getAnnotation(Table.class);
        if (tableAnnotation == null) {
            return false;
        }
        return true;
    }

    public void appendComma(int fieldsNumber, StringBuilder query, int i) {
        if (i != fieldsNumber - 1) {
            query.append(", ");
        }
    }

    public boolean isFieldPersistable(Field field) {
        if (!field.isAnnotationPresent(framework.model.annotations.Field.class) || (field.isAnnotationPresent(Transient.class))) {
            return false;
        }
        return true;
    }
}
