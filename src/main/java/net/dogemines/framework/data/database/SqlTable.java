package net.dogemines.framework.data.database;

import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.util.Named;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;

public class SqlTable<T extends SqlRow> implements Named {
    private final String name;
    private final Class<T> type;

    private final String primaryKey;

    private final String[] requiredFields;
    private final String[] mutableFields;
    private final String[] finalFields;

    public static final HashSet<SqlTable<?>> TABLES = new HashSet<>();

    public SqlTable(Class<T> clazz, String name) {
        String primaryField = "";
        final ArrayList<String> mutableFields = new ArrayList<>();
        final ArrayList<String> finalFields = new ArrayList<>();
        final ArrayList<String> requiredFields = new ArrayList<>();

        /*StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(name)
                .append(" ( ");

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                String fieldName = field.getName();
                String columnSql = fieldName + " " + column.value() + " ";

                //field is primary key
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    columnSql += "PRIMARY KEY";
                    primaryField = fieldName;
                }

                //add field to seperate list if field is final
                if (Modifier.isFinal(field.getModifiers())) {
                    finalFields.add(fieldName);
                }
                else {
                    mutableFields.add(fieldName);
                }

                //if it is required
                if (column.required()) {
                    requiredFields.add(fieldName);
                }

                columnSql += ", ";
                sql.append(columnSql);
            }

        }
        replaceComma(sql, " )");*/

        this.primaryKey = primaryField;
        this.mutableFields = mutableFields.toArray(new String[mutableFields.size()]);
        this.finalFields = mutableFields.toArray(new String[finalFields.size()]);
        this.requiredFields = requiredFields.toArray(new String[requiredFields.size()]);

        //this.tableSQL = sql.toString();
        this.name = name;
        this.type = clazz;

        //DogeMinesFramework.info(tableSQL);
        TABLES.add(this);
    }
    public SqlTable(Class<T> clazz) {
        this(clazz, clazz.getSimpleName().toLowerCase());
    }

    private static void replaceComma(StringBuilder builder, String text) {
        builder.replace(builder.length() - 2, builder.length(), text);
    }

    public String listFields(String[] fields, String sep, boolean skipPrimaryKey) {
        StringBuilder sql = new StringBuilder();
        for (String field : fields) {
            if (field.equals(primaryKey) && skipPrimaryKey) {
                continue;
            }
            sql.append(field)
                    .append(sep)
                    .append(", ");
        }
        replaceComma(sql, "");
        return sql.toString();
    }

    public Class<T> getType() {
        return type;
    }
    @Override
    public String getName() {
        return name;
    }
    public String getPrimaryKey() {
        return primaryKey;
    }
    public String[] getRequiredFields() {
        return requiredFields;
    }
    public String[] getAllMutableFields() {
        return mutableFields;
    }
    public String[] getFinalFields() {
        return finalFields;
    }
    public String[] getAllFields() {
        return ArrayUtils.addAll(mutableFields, requiredFields); //all fields
    }
    public static String repeatPlaceholder(int length) {
        return "?, ".repeat(length - 1) + "?";
    }
}
