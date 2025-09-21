package net.dogemines.framework.data.database;

import net.dogemines.framework.DogeMinesFramework;
import net.dogemines.framework.util.Named;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;

public class SqlTable<T extends SqlRow> implements Named {
    private final String tableSQL;
    private final String name;
    private final Class<T> type;

    private final String primaryKey;
    private final String[] allFields;
    private final String[] requiredFields;
    private final String[] allFieldsWithPrimaryKey;

    public static final HashSet<SqlTable<?>> TABLES = new HashSet<>();

    public SqlTable(Class<T> clazz, String name) {
        String primaryField = "";
        final ArrayList<String> allFields = new ArrayList<>();
        final ArrayList<String> requiredFields = new ArrayList<>();

        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(name)
                .append(" ( ");

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                String columnSql = field.getName() + " " + column.value() + " ";

                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    columnSql += "PRIMARY KEY";
                    primaryField = field.getName();
                }
                else {
                    //don't add primary key to fields
                    allFields.add(field.getName());
                }

                if (column.required()) {
                    requiredFields.add(field.getName());
                }

                columnSql += ", ";
                sql.append(columnSql);
            }

        }
        replaceComma(sql, " )");

        this.primaryKey = primaryField;
        this.allFields = allFields.toArray(new String[allFields.size()]);
        this.requiredFields = requiredFields.toArray(new String[requiredFields.size()]);

        this.allFieldsWithPrimaryKey = allFields.toArray(new String[allFields.size() + 1]);
        allFieldsWithPrimaryKey[allFieldsWithPrimaryKey.length - 1] = primaryKey;

        this.tableSQL = sql.toString();
        this.name = name;
        this.type = clazz;

        DogeMinesFramework.info(tableSQL);
        TABLES.add(this);
    }
    public SqlTable(Class<T> clazz) {
        this(clazz, clazz.getSimpleName());
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
    public String[] getFieldsWithoutPrimaryKey() {
        return allFields;
    }
    public String[] getAllFieldsWithPrimaryKey() {
        return allFieldsWithPrimaryKey;
    }
    public void createTable(Handle handle) {
        handle.execute(tableSQL);
    }
    public static String repeatPlaceholder(int length) {
        return "?, ".repeat(length - 1) + "?";
    }
}
