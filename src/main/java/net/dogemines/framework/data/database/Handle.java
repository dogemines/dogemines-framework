package net.dogemines.framework.data.database;

import net.dogemines.framework.DogeMinesFramework;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

public class Handle {
    private final DataSource dataSource;
    private final Consumer<SQLException> onException;

    public Handle(DataSource dataSource, Consumer<SQLException> onException) {
        this.dataSource = dataSource;
        this.onException = onException;
    }

    public void withStatement(SqlConsumer<Statement> consumer, boolean isTransaction) {
        with(consumer, Connection::createStatement, isTransaction);
    }

    public void withPreparedStatement(String sql, SqlConsumer<PreparedStatement> consumer, boolean isTransaction) {
        DogeMinesFramework.info(sql);
        with(consumer, connection -> connection.prepareStatement(sql), isTransaction);
    }

    public void execute(String sql) {
        withStatement(statement -> statement.execute(sql), false);
    }

    private <S extends Statement> void with(SqlConsumer<S> consumer, SqlFunction<Connection, S> creator, boolean isTransaction) {
        try(Connection conn = dataSource.getConnection();
            S statement = creator.apply(conn);)
        {
            if (isTransaction) {
                conn.setAutoCommit(false);

                //rollback if transaction fails
                try {
                    consumer.accept(statement);
                } catch (SQLException e) {
                    conn.rollback();
                    onException.accept(e);
                }

                conn.commit();
            }
            else {
                consumer.accept(statement);
            }
        }
        catch (SQLException e) {
            onException.accept(e);
        }
    }

    //interfaces that throw SQLException
    public static interface SqlConsumer<T> {
        public void accept(T t) throws SQLException;
    }
    public static interface SqlFunction<T, R> {
        public R apply(T t) throws SQLException;
    }
}
