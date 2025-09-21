package net.dogemines.framework.data.database;

import net.dogemines.framework.DogeMinesFramework;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SqlRow {
    public Object[] mapFieldsToValues(String[] fields) {
        Object[] values = new Object[fields.length];

        for (int i = 0; i < fields.length; i++) {
            //reflective code
            String fieldName = fields[i];
            values[i] = getField(fieldName);
        }

        return values;
    }

    public void mapResult(ResultSet rs) {
        Class<? extends SqlRow> clazz = getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                String fieldName = field.getName();

                field.setAccessible(true);
                try {
                    field.set(this, rs.getObject(fieldName));
                }
                catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
                catch (SQLException e) {
                    DogeMinesFramework.warning("ResultSet does not contain column %s of %s"
                            .formatted(fieldName, clazz.getName()));
                }
            }
        }
    }

    public Object getField(String fieldName) {
        try {
            Field field = getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(this);

        } catch (ReflectiveOperationException e) {
            DogeMinesFramework.warning("Field %s in class %s not found or was private!".formatted(
                    fieldName,
                    getClass().getSimpleName()));
            e.printStackTrace();
        }

        return null;
    }
}
