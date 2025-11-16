package net.dogemines.framework.data.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class GenericDao<T extends SqlRow> {
    private final SqlTable<T> table;
    private final Handle handle;

    public GenericDao(SqlTable<T> table, Handle handle) {
        this.table = table;
        this.handle = handle;
    }

    //helper method that sets a list of arguments to a PreparedStatement
    private static void applyArgs(PreparedStatement stmt, Object[] args) throws SQLException {
        for (int i = 1; i <= args.length; i++) {
            stmt.setObject(i, args[i - 1]);
        }
    }

    //SELECT * FROM table_name WHERE column = ?
    public void select(String whereField, Object whereValue, T instance) {
        handle.withPreparedStatement("SELECT * FROM %s WHERE %s = ?".formatted(
                table.getName(),
                whereField),

            statement -> {
                statement.setObject(1, whereValue);
                ResultSet rs = statement.executeQuery();

                //find one and map result to the instance
                if (rs.next()) {
                    instance.mapResult(rs);
                }
        }, false);
    }
    public void selectWherePrimaryKey(String whereValue, T instance) {
        select(table.getPrimaryKey(), whereValue, instance);
    }

    //INSERT OR IGNORE INTO table_name (column) VALUES (?);
    public void insert(T sqlRow, String[] fields) {
        handle.withPreparedStatement("INSERT INTO %s (%s) VALUES (%s) ON CONFLICT DO NOTHING".formatted(
                table.getName(),
                table.listFields(fields, "", false),
                SqlTable.repeatPlaceholder(fields.length)),

            statement -> {
                applyArgs(statement, sqlRow.mapFieldsToValues(fields));
                statement.executeUpdate();
        }, false);
    }
    public void insertAllFields(T data) {
        insert(data, table.getAllFields());
    }
    public void insertRequiredFields(T data) {
        insert(data, table.getRequiredFields());
    }

    //UPDATE table_name SET column1 = ?, column2 = ? WHERE column = ?;
    //we need the update statement for multiple methods
    private String getUpdateStatement(String[] fields, String whereField) {
        return "UPDATE %s SET %s WHERE %s = ?".formatted(
                table.getName(),
                table.listFields(fields, " = ?", true),
                whereField);
    }

    public void update(T sqlRow, String[] fields, String whereField, Object whereValue) {
        handle.withPreparedStatement(getUpdateStatement(fields, whereField), statement -> {
            applyArgs(statement, sqlRow.mapFieldsToValues(fields));
            statement.setObject(fields.length + 1, whereValue); //WHERE column = ?
            statement.executeUpdate();
        }, false);
    }
    //helper methods
    public void updateMutableFields(T sqlRow, String whereField, Object whereValue) {
        update(sqlRow, table.getAllMutableFields(), whereField, whereValue);
    }
    public void updateByPrimaryKey(T sqlRow) {
        String primaryKey = table.getPrimaryKey();
        updateMutableFields(sqlRow, primaryKey, sqlRow.getField(primaryKey));
    }

    //update batch
    public void updateBatch(Collection<T> rows, String[] fields, String whereField) {
        handle.withPreparedStatement(getUpdateStatement(fields, whereField), statement -> {

            //loop through all rows and add them to batch
            for (T row : rows) {
                applyArgs(statement, row.mapFieldsToValues(fields));
                statement.setObject(fields.length + 1, row.getField(whereField)); //WHERE column = ?
                statement.addBatch();
            }
            statement.executeBatch();

        }, true); //sql transaction
    }


    //table methods
    public SqlTable<T> getTable() {
        return table;
    }
}
