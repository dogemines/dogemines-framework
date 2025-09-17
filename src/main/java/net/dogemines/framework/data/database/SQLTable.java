package net.dogemines.framework.data.database;

import net.dogemines.framework.DogeMinesFramework;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SQLTable<T> {
    private final Class<T> scema;
    private final String name;

    public SQLTable(Class<T> scema) {
        this.scema = scema;
        this.name = scema.getName();
    }

    public String createTableSQL() {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS %s ( ".formatted(name));

        for (Field field : scema.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(SQLColumn.class)) {
                SQLColumn annotation = field.getAnnotation(SQLColumn.class);
                sqlBuilder.append("%s %s, ".formatted(field.getName(), annotation.value()));
            }
        }

        return sqlBuilder.toString();
    }

    public Class<T> getScema() {
        return scema;
    }

    private static Map<String, Object> getRowAsMap(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        Map<String, Object> rowMap = new HashMap<>();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = meta.getColumnName(i);
            Object value = rs.getObject(i);
            rowMap.put(columnName, value);
        }
        return rowMap;
    }

    public void createTable(SQLConnection connection) {
        try(Statement statement = connection.getConnection().createStatement()) {
            statement.executeUpdate(this.createTableSQL());
        }
        catch (SQLException e) {
            DogeMinesFramework.warning(e.getMessage());
        }
    }

    public T getValue(SQLConnection connection, String primaryKeyName, String primaryKeyValue) {
        try (PreparedStatement pstmt = connection.getConnection().prepareStatement("SELECT * FROM %s WHERE ? = ?".formatted(name))) {
            pstmt.setString(1, primaryKeyName);
            pstmt.setString(1, primaryKeyValue);
            ResultSet rs = pstmt.executeQuery();

            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            if (rs.next()) {
                T scemaObject = scema.getDeclaredConstructor().newInstance();
                Map<String, Object> row = getRowAsMap(rs);
                for (Field field : scema.getFields()) {
                    field.setAccessible(true);
                    field.set(scemaObject, row.get(field.getName()));
                }

                return scemaObject;
            }
        } catch (Exception e) {
            DogeMinesFramework.warning(e.getMessage());
        }
        return null;
    }

}
