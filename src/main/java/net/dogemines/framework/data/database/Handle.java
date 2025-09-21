package net.dogemines.framework.data.database;

import net.dogemines.framework.DogeMinesFramework;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;
import java.util.function.Supplier;

public class Handle {
    private final DataSource dataSource;

    public Handle(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void withStatement(SqlConsumer<Statement> consumer, boolean autoCommit) {
        with(consumer, Connection::createStatement, autoCommit);
    }

    public void withPreparedStatement(String sql, SqlConsumer<PreparedStatement> consumer, boolean autoCommit) {
        DogeMinesFramework.info(sql);
        with(consumer, connection -> connection.prepareStatement(sql), autoCommit);
    }

    public void execute(String sql) {
        withStatement(statement -> statement.execute(sql), true);
    }

    private <S extends Statement> void with(SqlConsumer<S> consumer, SqlFunction<Connection, S> creator, boolean autoCommit) {
        try(Connection conn = dataSource.getConnection();
            S statement = creator.apply(conn);)
        {
            if (autoCommit) {
                consumer.accept(statement);
            }
            else {
                conn.setAutoCommit(autoCommit);
                consumer.accept(statement);
                conn.commit();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
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
